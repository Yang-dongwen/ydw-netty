package com.ydw.bbio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class bio {
    public static void main(String[] args) throws IOException {
        // 线程池机制

        // 思路
        // 1.创建一个线程池
        // 2.如果有客户端连接，就创建一个线程，与之通讯(单独写一个方法)
        ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

        // 创建ServerSocket
        ServerSocket serverSocket = new ServerSocket(6666);
        System.out.println("服务器启动了");

        while (true){
            // 监听
            final Socket socket = serverSocket.accept();
            // 连接到一个客户端
            System.out.println("连接到一个客户端");

            // 创建一个线程池，与之通讯
            newCachedThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    handler(socket);
                }
            });

        }
    }

    // 编写一个handler方法，和客户端通信
    public static void handler(Socket socket){
        byte[] bytes = new byte[1024];
        // 通过socket 获取输入流
        try {
            System.out.println("线程id:" + Thread.currentThread().getId() + "线程名字:" + Thread.currentThread().getName());
            InputStream inputStream = socket.getInputStream();

            // 循环读取客户端发送的数据
            while (true){
                int read = inputStream.read(bytes);
                if (read != -1){
                    System.out.println(new String(bytes, 0, read));
                }else{
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
