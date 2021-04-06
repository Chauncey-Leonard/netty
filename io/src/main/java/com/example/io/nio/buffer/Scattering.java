package com.example.io.nio.buffer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * 将数据写入buffer时，可以采用buffer数组，依次写入[分散]
 *
 * @author Chauncey
 * @since 1.0.0
 */
public class Scattering {

    public static void main(String[] args) {
        // 使用ServerSocketChannel 和 SocketChannel网络
        try (ServerSocketChannel socketChannel = ServerSocketChannel.open()) {
            InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);
            // 绑定端口到socket，并启动
            socketChannel.socket().bind(inetSocketAddress);
            // 创建buffer数组
            ByteBuffer[] byteBuffers = new ByteBuffer[2];
            byteBuffers[0] = ByteBuffer.allocate(5);
            byteBuffers[1] = ByteBuffer.allocate(3);

            // 等待客户端连接
            SocketChannel accept = socketChannel.accept();
            // 假设从客户端接收8个字节
            int messageLen = 8;
            while (true) {
                int read = 0;
                while (read < messageLen) {
                    long l = accept.read(byteBuffers);
                    read += l; // 累计读取的字节数
                    // 使用流打印，查看当前buffer的position和limit
                    Arrays.stream(byteBuffers)
                            .map(byteBuffer -> "position = " + byteBuffer.position() + ", limit = " + byteBuffer.limit())
                            .forEach(System.out::println);
                    // 将所有的buffer进行flip
                    Arrays.stream(byteBuffers).forEach(Buffer::flip);

                    // 将数据读出显示在客户端
                    int write = 0;
                    while (write < messageLen) {
                        long writeLength = accept.write(byteBuffers);
                        write += writeLength;
                    }

                    // 将所有的buffer，进行clear
                    Arrays.stream(byteBuffers).forEach(Buffer::clear);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
