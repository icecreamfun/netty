package com.atguigu.nio;



import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 读取文件数据
 */
public class NIOFileChannel02 {

    public static void main(String[] args) throws Exception {
        //创建文件输入流
        File file = new File("d:\\a.txt");
        FileInputStream fileInputStream = new FileInputStream(file);

        FileChannel channel = fileInputStream.getChannel();

        //创建缓冲区
        ByteBuffer buffer = ByteBuffer.allocate((int) file.length());
        //将通道的数据读入buffer
        channel.read(buffer);

        //将buffer的字节数据转换成string
        System.out.println(new java.lang.String(buffer.array()));
        fileInputStream.close();

    }
}
