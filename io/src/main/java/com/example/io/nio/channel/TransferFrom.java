package com.example.io.nio.channel;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 应用实例4：使用transferFrom实现文件拷贝
 *
 * @author Chauncey
 * @since 1.0.0
 */
public class TransferFrom {

    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream("test.txt");
             FileOutputStream fos = new FileOutputStream("test-copy.txt")
        ) {
            // 获取对应的通道
            FileChannel fisChannel = fis.getChannel();
            FileChannel fosChannel = fos.getChannel();

            // 使用 transferFrom 完成拷贝
            fosChannel.transferFrom(fisChannel, 0, fisChannel.size());

            // 关闭相关通道
            fisChannel.close();
            fosChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
