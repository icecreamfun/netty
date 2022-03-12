package com.atguigu.nio;

import java.nio.IntBuffer;

/**
 * 简单实用buffer
 */
public class BasicBuffer {
    public static void main(String[] args) {
        IntBuffer buffer = IntBuffer.allocate(5);
        //存放数据
        for (int i = 0; i < buffer.capacity(); i++) {
           buffer.put(i*2);
        }
        //buffer转换
        buffer.flip();
        while (buffer.hasRemaining()){
            //get会有一个指针
            System.out.println(buffer.get());
        }
    }
}
