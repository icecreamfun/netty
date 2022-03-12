package com.atguigu.nio;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 *拷贝文件
 */
public class NIOFileChannel04 {

    public static void main(String[] args) throws Exception {
        //创建文件输入流
        FileInputStream fileInputStream = new FileInputStream("d:\\atna-joy-5.jpg");
        FileChannel sourceCh = fileInputStream.getChannel();

        //创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("e:\\aaa.jpg");
        FileChannel destCh = fileOutputStream.getChannel();

        destCh.transferFrom(sourceCh,0,sourceCh.size());

        sourceCh.close();
        destCh.close();
        fileInputStream.close();
        fileOutputStream.close();
    }
}
