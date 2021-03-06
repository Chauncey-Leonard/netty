package com.example.io.nio.cases.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * NIO 网络编程应用实例：群聊系统
 * <p>
 * 实例要求：
 * 1) 编写一个NIO群聊系统，实现服务器端和客户端之间的数据简单通讯(非阻塞)
 * 2) 实现多人群聊
 * 3) 服务器端：可以监测用户上线，离线，并实现消息转发功能
 * 4) 客户端：通过Channel可以无阻塞发送消息给其他所有客户，同时可以接收其他用户发送的消息(由服务器转发得到)
 * 5) 目的：进一步理解NIO非阻塞网络编程机制
 *
 * @author Chauncey
 */
public class ChatServer {

    /** 多路复用器 */
    private Selector selector;

    /** 服务端绑定的端口 */
    private static final int PORT = 8888;

    /** 服务端的通道 */
    private ServerSocketChannel serverSocketChannel;

    /**
     * 初始化
     */
    public ChatServer() {
        try {
            // 获取选择器
            selector = Selector.open();
            // 获取通道对象
            serverSocketChannel = ServerSocketChannel.open();
            // 绑定端口
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            // 设置非阻塞模式
            serverSocketChannel.configureBlocking(false);
            // 将通道注册到 Selector 中
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("server started on port: " + PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 监听客户端连接
     */
    public void listen() {
        try {
            while (true) {
                int count = selector.select();
                if (count > 0) {
                    // 获取SelectionKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        // 监听到accept
                        if (key.isAcceptable()) {
                            SocketChannel sc = serverSocketChannel.accept();
                            // 设置通道为非阻塞
                            sc.configureBlocking(false);
                            // 将连接到的客户端注册到 Selector 中
                            sc.register(selector, SelectionKey.OP_READ);
                            // 提示
                            System.out.println(sc.getRemoteAddress().toString().substring(1) + " 上线");
                        }

                        if (key.isReadable()) {
                            // 读取客户端信息
                            readData(key);
                        }

                        // 删除当前的key，防止重复处理
                        iterator.remove();
                    }
                } else {
                    System.out.println("暂无事件……");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取客户端信息
     */
    public void readData(SelectionKey key) {
        // 获取关联的通道
        SocketChannel sc = null;

        try {
            // 获取通道
            sc = (SocketChannel) key.channel();
            // 创建缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = sc.read(buffer);

            // 根据读取的字节数进行相应的处理
            if (count > 0) {
                // 将缓冲区的数据转换成字符串
                String message = new String(buffer.array());
                // 打印输出
                System.out.println(sc.getRemoteAddress().toString().substring(1) + ": " + message.trim());

                // 向其他客户端转发消息
                sendMessage(message, sc);
            }
        } catch (IOException e) {
            try {
                System.out.println(sc.getRemoteAddress().toString().substring(1) + " 离线了");
                // 取消注册
                key.channel();
                // 关闭通道
                sc.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
    }

    /**
     * 转发消息给其他客户端(通道)
     *
     * @param message 消息
     * @param self    当前通道
     */
    private void sendMessage(String message, SocketChannel self) throws IOException {
        System.out.println("消息转发中……");
        // 遍历所有注册到多路复用器中的通道并排除当前通道
        for (SelectionKey key : selector.keys()) {
            Channel channel = key.channel();
            if (channel instanceof SocketChannel && channel != self) {
                SocketChannel dest = (SocketChannel) channel;
                // 将消息写入缓冲区中
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
                // 将缓冲区中的数据写入通道
                dest.write(buffer);
            }
        }
    }

    public static void main(String[] args) {
        ChatServer server = new ChatServer();
        server.listen();
    }

}
