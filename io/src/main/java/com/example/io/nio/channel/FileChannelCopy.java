package com.example.io.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 应用实例3：使用一个Buffer完成文件的读取、写入
 * 实例要求：
 * - 使用FileChannel和方法read、write完成文件的拷贝
 *
 * @author Chauncey
 * @since 1.0.0
 */
public class FileChannelCopy {

    public static void main(String[] args) {

        try (FileInputStream fis = new FileInputStream("test.txt");
             FileOutputStream fos = new FileOutputStream("test-copy.txt")) {
            // 获取对应的输入输出通道
            FileChannel fisChannel = fis.getChannel();
            FileChannel fosChannel = fos.getChannel();

            // 创建缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(512);

            // 循环读取数据
            while (true) {
                // 读取数据之前一定要先清空
                byteBuffer.clear();
                int read = fisChannel.read(byteBuffer);
                // 表示读取完毕
                if (read == -1) {
                    break;
                }
                // 读写转换
                byteBuffer.flip();
                // 写入文件
                fosChannel.write(byteBuffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
