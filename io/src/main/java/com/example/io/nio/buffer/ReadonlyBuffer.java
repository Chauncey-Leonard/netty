package com.example.io.nio.buffer;

import java.nio.ByteBuffer;

/**
 * 只读Buffer
 *
 * @author Chauncey
 * @since 1.0.0
 */
public class ReadonlyBuffer {

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        for (int i = 0; i < 64; i++) {
            byteBuffer.put((byte) i);
        }

        // 读取
        byteBuffer.flip();
        // 获取一个只读Buffer
        ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
        System.out.println(readOnlyBuffer.getClass());

        while (readOnlyBuffer.hasRemaining()) {
            System.out.println("readOnlyBuffer.get() = " + readOnlyBuffer.get());
        }

        // ReadOnlyBufferException
        // readOnlyBuffer.put((byte) 100);
    }

}
