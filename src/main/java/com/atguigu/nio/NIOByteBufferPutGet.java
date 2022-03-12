package com.atguigu.nio;

import java.nio.ByteBuffer;

/**
 * 如果不按类型取出有可能报错
 */
public class NIOByteBufferPutGet {
    public static void main(String[] args) {

        ByteBuffer buffer = ByteBuffer.allocate(25);
        buffer.putInt(2);
        buffer.putChar('好');
        buffer.putShort((short) 6);

        buffer.flip();
        System.out.println(buffer.getInt());
        System.out.println(buffer.getChar());
        System.out.println(buffer.getShort());
    }


}
