package com.example.io.nio;

import java.nio.IntBuffer;

/**
 * NIO：同步非阻塞
 *
 * @author Chauncey
 * @since 1.0.0
 */
public class App {

    public static void main(String[] args) {
        // 创建一个容量为5的缓冲区
        IntBuffer allocate = IntBuffer.allocate(5);

        // 向缓冲区中存放数据
        for (int i = 0; i < allocate.capacity(); i++) {
            allocate.put(i * 2);
        }

        // 从缓冲区中获取数据
        // 读写切换
        allocate.flip();

        while (allocate.hasRemaining()) {
            System.out.println(allocate.get());
        }
    }

}
