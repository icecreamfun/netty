package com.atguigu.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * NIOServer   ServerSocketChannel SocketChannel Selector  SelectionKey的关系
 */
public class NIOServer {

    public static void main(String[] args) throws Exception {

        //创建ServerSokcetChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //得到一个Selector对象
        Selector selector = Selector.open();

        //绑定监听端口
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //把ServerSocketChannel注册到Selector 关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        //循环等待
        while (true){
            //这里等待1秒,如果没有事件发生 返回
            if(selector.select(1000)==0){//没有事件发生
                System.out.println("服务器等待了1秒 无连接");
                continue;
            }
            //如果返回>0 就获取到相关的selectionKey集合
            //1.如果返回大于0 表示已经获取到关注的事件
            //2.elector.selectedKeys()表示返回关注事件的集合
            //3.通过selectionKeys反向获取通道
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                //根据key对应的通道发生的事件相应的处理
                if(key.isAcceptable()){//如果是OP_ACCEPT  有新的客户连接
                    SocketChannel socketChannel = serverSocketChannel.accept();

                    socketChannel.configureBlocking(false);
                    //将socketChannel注册到selector 关注事件为OP_READ,同时关联一个buffer
                    socketChannel.register(selector,SelectionKey.OP_READ, ByteBuffer.allocate(1024));

                }
                if(key.isReadable()){ //OP_READ
                    //通过key反向获取到对应channel
                    SocketChannel channel = (SocketChannel) key.channel();
                    //获取到关联的buffer
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    channel.read(buffer);
                    System.out.println("from 客户端 "+new String(buffer.array()));
                }

                //手动从集合中移动selectionKeys,防止重复操作
                iterator.remove();
            }
        }
    }
}
