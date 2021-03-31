package com.example.io.nio.buffer;

import java.nio.ByteBuffer;

/**
 * ByteBuffer的注意事项
 *
 * @author Chauncey
 * @since 1.0.0
 */
public class ByteBufferNotice {

    public static void main(String[] args) {
        // 创建一个缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        // 类型化方式放入数据
        byteBuffer.putInt(100);
        byteBuffer.putLong(1001);
        byteBuffer.putChar('唐');

        // 取出
        byteBuffer.flip();
        System.out.println(byteBuffer.getInt());
        System.out.println(byteBuffer.getLong());
        System.out.println(byteBuffer.getChar());
        // System.out.println(byteBuffer.getDouble());
    }

}
