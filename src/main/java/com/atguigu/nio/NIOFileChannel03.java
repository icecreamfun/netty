package com.atguigu.nio;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *完成一个buffer对文件的读写
 *
 */
public class NIOFileChannel03 {

    public static void main(String[] args) throws Exception {


        //创建文件输入流
        FileInputStream fileInputStream = new FileInputStream("d:\\a.txt");
        FileChannel fileChannel01 = fileInputStream.getChannel();

        //创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\aaa.txt");
        FileChannel fileChannel02 = fileOutputStream.getChannel();

        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(512);
        while (true) {
            //重要的操作
            buffer.clear();
            int read = fileChannel01.read(buffer);
            System.out.println("red "+read);
            if (read == -1) {
                break;
            }
            buffer.flip();
            fileChannel02.write(buffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }
}
