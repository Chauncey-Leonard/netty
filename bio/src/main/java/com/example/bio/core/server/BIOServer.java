package com.example.bio.core.server;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 同步阻塞服务端
 *
 * @author Chauncey
 * @since 1.0.0
 */
public class BIOServer {

    public static void main(String[] args) {

        /*
         * 线程池机制
         *
         * 思路
         * 1.创建一个线程池
         * 2.如果有客户端连接，就创建一个线程，与之通讯（单独写一个方法）
         */

        ExecutorService executorService = Executors.newCachedThreadPool();

        // 创建 ServerSocket
        try {
            ServerSocket serverSocket = new ServerSocket(6666);
            System.out.println("server is running at port 6666");

            while (true) {
                // 监听，等待客户端连接
                Socket accept = serverSocket.accept();
                System.out.println("连接到一个客户端");

                // 创建线程，与之通讯
                executorService.execute(() -> handler(accept));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 与客户端进行通讯
     */
    public static void handler(Socket socket) {
        System.out.println("线程信息 id=" + Thread.currentThread() + ",name=" + Thread.currentThread().getName());

        byte[] bytes = new byte[1024];

        try {
            // 通过 socket 获取输入流
            InputStream inputStream = socket.getInputStream();

            // 循环读取客户端发送的数据
            while (true) {
                int read = inputStream.read(bytes);

                if (read == -1) {
                    break;
                }

                // 打印客户端发送的数据
                System.out.println(new String(bytes, 0, read));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭和客户端的连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
