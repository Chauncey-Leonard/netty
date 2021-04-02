package com.example.io.nio.buffer;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 可让文件直接在内存中修改，操作系统不需要拷贝一次
 *
 * @author Chauncey
 * @since 1.0.0
 */
public class MappedByteBufferTest {

    public static void main(String[] args) throws Exception {
        RandomAccessFile raf = new RandomAccessFile("test.txt", "rw");
        // 获取对应的通道
        FileChannel channel = raf.getChannel();
        /*
         * 参数一：使用的读写模式
         * 参数二：可以直接修改的位置
         * 参数三：是映射到内存的大小
         */
        MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE,
                0, 5);
        mappedByteBuffer.put(1, (byte) 'H');
        mappedByteBuffer.put(3, (byte) '9');
        raf.close();
    }

}
