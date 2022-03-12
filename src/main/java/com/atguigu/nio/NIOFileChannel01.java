package com.atguigu.nio;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *FileChannel实现写数据到文件
 */
public class NIOFileChannel01 {
    public static void main(String[] args) throws Exception {

        String str ="hello lemon";

        //创建一个输出流
        FileOutputStream fileOutputStream = new FileOutputStream("d:\\a.txt");

        //通过fileOutputStream获取fileChannel
        //这个fileChannel实现类是FileChannelImpl
        FileChannel channel = fileOutputStream.getChannel();

        //创建一个缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        //放入数据
        buffer.put(str.getBytes());
        //反转
        buffer.flip();

        channel.write(buffer);
        fileOutputStream.close();

    }
}
