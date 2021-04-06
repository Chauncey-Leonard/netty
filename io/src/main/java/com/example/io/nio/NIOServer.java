package com.example.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO服务器端
 *
 * @author Chauncey
 * @since 1.0.0
 */
public class NIOServer {

    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 获取Selector
        Selector selector = Selector.open();
        // 绑定端口,在服务器端监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));
        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 注册到Selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        // 等待客户端连接
        while (true) {
            if (selector.select(10000) == 0) { // 没有事件发生
                System.out.println("无客户端连接");
            }

            // 通过selectionKeys反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) { // 新的客户端连接
                    // 生成一个客户端连接 SocketChannel
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    System.out.println("客户端连接成功！");
                    // 将客户端注册到Selector
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }

                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    // 获取到通道关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println(" From Client " + new String(buffer.array()));
                }

                // 手动移除，防止重复操作
                iterator.remove();
            }
        }
    }

}
