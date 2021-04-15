package com.example.io.nio.cases.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

/**
 * 群聊系统客户端
 *
 * @author Chauncey
 * @since 1.0.0
 */
public class ChatClient {

    /** 服务器的ip地址 */
    private final String HOST = "127.0.0.1";
    /** 服务器的端口 */
    private final int PORT = 8888;

    /** 多路复用器 */
    private Selector selector;
    /** 客户端的通道 */
    private SocketChannel socketChannel;

    private String username;

    public ChatClient() {
        try {
            selector = Selector.open();
            socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
            // 设置客户端通道非阻塞
            socketChannel.configureBlocking(false);
            // 将通道注册到多路复用器
            socketChannel.register(selector, SelectionKey.OP_READ);
            username = socketChannel.getRemoteAddress().toString().substring(1);
            System.out.println(username + " is ok……");
            
            // 向服务器发送消息
            sendMessage("Hello World!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 向服务器发送消息
     *
     * @param message 消息内容
     */
    private void sendMessage(String message) {
        message = username + " : " + message;

        try {
            socketChannel.write(ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取服务端的数据
     */
    public void readMessage() {
        try {
            int readChannels = selector.select();
            if (readChannels > 0) {
                Iterator<SelectionKey> iterator = selector.keys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    if (key.isReadable()) {
                        // 获取通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        // 获取缓冲区
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        // 读取数据
                        int read = sc.read(buffer);
                        if (read > 0) {
                            String s = new String(buffer.array());
                            System.out.println(s.trim());
                        }
                    }
                }
            } else {
                System.out.println("没有可用的通道……");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
