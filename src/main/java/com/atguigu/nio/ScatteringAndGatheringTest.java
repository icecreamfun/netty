package com.atguigu.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Scattering:将数据写入到buffer,可以采用buffer数组,依次写入[分散]
 * Gathering:从buffer读取数据时,可以采用buffer数组,依次读
 */
public class ScatteringAndGatheringTest {
    public static void main(String[] args) throws Exception {
        //使用ServerSocketChannel和sockerChannel 网络
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(7000);

        //绑定端口到socker,并启动
        serverSocketChannel.socket().bind(inetSocketAddress);

        //创建buffer数组
        ByteBuffer[] byteBuffer = new ByteBuffer[2];
        byteBuffer[0] = ByteBuffer.allocate(5);
        byteBuffer[1] = ByteBuffer.allocate(3);

        //等待客户端连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        int messageLength = 8;
        //循环的读取
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long l = socketChannel.read(byteBuffer);
                byteRead += l;
                System.out.println("byteRead" + byteRead);
                //使用流打印,看看当前的这个buffer的position和limit
                Arrays.asList(byteBuffer).stream()
                        .map(buffer -> "postion=" + buffer.position() + ",limit=" + buffer.limit())
                        .forEach(System.out::println);
            }

            //将所有的buffer进行flip
            Arrays.asList(byteBuffer).forEach(buffer -> buffer.flip());
            //将数据显示到客户端
            long byteWirte = 0;
            while (byteWirte < messageLength) {
                long l = socketChannel.write(byteBuffer);
                byteWirte += l;
            }
            Arrays.asList(byteBuffer).forEach(buffer -> buffer.clear());
            System.out.println("byteRead:=" + byteRead + "byteWrite" + byteWirte + "messagelength" + messageLength);
        }

    }
}
