package com.example.io.nio.channel;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * 使用FileChannel向文件中写入内容
 *
 * @author Chauncey
 * @since 1.0.0
 */
public class FileChannelTest {

    public static void main(String[] args) {
        String message = "Chauncey Leonard";
        FileOutputStream fos = null;
        try {
            // 创建一个输出流
            fos = new FileOutputStream("E:\\test.txt");
            // 通过 FileOutputStream 获取对应的 FileChannel
            FileChannel channel = fos.getChannel();
            // 创建一个缓冲区
            ByteBuffer allocate = ByteBuffer.allocate(1024);
            // 将message放入缓冲区
            allocate.put(message.getBytes(StandardCharsets.UTF_8));
            // 对ByteBuffer进行flip操作
            allocate.flip();
            // 将缓冲区中的数据写入到通道中
            channel.write(allocate);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    // 关闭流
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
