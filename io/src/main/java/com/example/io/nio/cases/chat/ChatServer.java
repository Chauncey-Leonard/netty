package com.example.io.nio.cases.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
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

    private Selector selector;

    private static final int PORT = 8888;

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            while (true) {
                int count = selector.select(2000);
                if (count > 0) {
                    // 获取SelectionKey集合
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        SelectionKey key = iterator.next();

                        // 监听到accept
                        if (key.isAcceptable()) {
                            SocketChannel sc = serverSocketChannel.accept();
                            // 将连接到的客户端注册到 Selector 中
                            sc.register(selector, SelectionKey.OP_READ);
                            // 提示
                            System.out.println(sc.getRemoteAddress() + " 上线 ");
                        }

                        if (key.isReadable()) {
                            // 处理读
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

    public void readData() {

    }

    public static void main(String[] args) {

    }

}
