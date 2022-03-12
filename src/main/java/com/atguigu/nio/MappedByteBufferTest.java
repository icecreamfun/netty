package com.atguigu.nio;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer可以让文件在内存修改(堆外内存),操作系统不需要拷贝一次
 */
public class MappedByteBufferTest {
    public static void main(String[] args) throws Exception {

        RandomAccessFile randomAccessFile = new RandomAccessFile("d:\\a.txt","rw");
        FileChannel channel = randomAccessFile.getChannel();

        /**
         * 参数1:FileChannel.MapMode.READ_WRITE使用的读写模式
         * 参数2 : 0 :可以修改的起始位置
         * 参数3:  5 :是映射到内存的大小,即将a.txt的多少个字节映射到内存
         * 可以直接修改的范式就是0-5
         * 实际类型是 DirectByteBuffer
         */
        MappedByteBuffer map = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
        map.put(0, (byte) 'H');
        map.put(3, (byte) '9');
       // map.put(5, (byte) 'Y'); 会报java.lang.IndexOutOfBoundsException
        randomAccessFile.close();

    }
}
