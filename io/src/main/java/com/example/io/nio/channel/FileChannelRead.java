package com.example.io.nio.channel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 本地文件数据读取
 *
 * @author Chauncey
 * @since 1.0.0
 */
public class FileChannelRead {

    public static void main(String[] args) {
        FileInputStream fis = null;
        try {
            // 创建文件输入流
            File file = new File("E:\\test.txt");
            fis = new FileInputStream(file);

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
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
