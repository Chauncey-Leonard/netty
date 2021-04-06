package com.example.io.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 应用实例2：本地文件读数据
 * 实例要求：
 * - 使用ByteBuffer和FileChannel，将test.txt中的数据读取到程序并在控制台打印
 *
 * @author Chauncey
 * @since 1.0.0
 */
public class FileChannelRead {

    public static void main(String[] args) {
        File file = new File("test.txt");
        try (FileInputStream fis = new FileInputStream(file)) {
            // 获取通道
            FileChannel channel = fis.getChannel();
            // 创建缓冲区
            ByteBuffer allocate = ByteBuffer.allocate((int) file.length());

            // 从通道中读取数据并放到缓冲区中
            channel.read(allocate);

            // 将ByteBuffer的字节数据转化为String
            System.out.println(new String(allocate.array()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
