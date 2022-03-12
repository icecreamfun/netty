package com.atguigu.bio;


import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * BIO使用
 * 问题分析:
 * 1.每个请求都会创建独立的线程,与对应的客户端进行数据处理
 * 2.当并发数较大时候,需要创建大量的线程,占用系统资源
 * 3.连接建立后,如果当前线程暂时没有数据可读,则线程就阻塞在read操作上.造成线程资源浪费
 */
public class BIOServer {

    public static void main(String[] args) throws Exception {

        //使用线程池
        ExecutorService executorService = Executors.newCachedThreadPool();

        //创建serverSocket
        final ServerSocket serverSocket = new ServerSocket(6666);

        System.out.println("服务器启动了");

        while (true) {
            System.out.println("等待连接");
            //监听,等待客户端连接
            final Socket socket = serverSocket.accept();
            System.out.println("连接到一个客户端");

            //创建一个线程与之通讯
            executorService.execute(new Runnable() {
                public void run() {
                    //和客户端通讯
                    handler(socket);
                }
            });
        }
    }

    //
    public static void handler(Socket socket) {

        try {
            System.out.println("线程ID= "+Thread.currentThread().getId()+" 名字= "+Thread.currentThread().getName());
            byte[] bytes = new byte[1024];
            //获取输入流
            InputStream inputStream = socket.getInputStream();
            while (true) {
                System.out.println("线程ID= "+Thread.currentThread().getId()+" 名字= "+Thread.currentThread().getName());
                System.out.println("等待读......");
                int read = inputStream.read(bytes);
                if (read != -1) {
                   System.out.println(new String(bytes,0,read));
                } else {
                    break;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("关闭client连接");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
