package com.example.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * NIO客户端
 *
 * @author Chauncey
 * @since 1.0.0
 */
public class NIOClient {

    public static void main(String[] args) throws IOException {
        // 获取网络通道
        SocketChannel socketChannel = SocketChannel.open();
        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // 提供服务器的ip和端口
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 6666);
        // 连接服务器
        if (!socketChannel.connect(address)) {
            while (!socketChannel.finishConnect()) {
                System.out.println("未连接成功！");
            }
        }

        // 连接成功发送数据
        String message = "Chauncey";
        ByteBuffer byteBuffer = ByteBuffer.wrap(message.getBytes(StandardCharsets.UTF_8));
        // 发送数据，将数据写入通道
        socketChannel.write(byteBuffer);
        System.in.read();
    }

}
