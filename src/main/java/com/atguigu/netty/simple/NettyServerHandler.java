package com.atguigu.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/*
 * 说明
 * 1.我们自定义一个handler 需要继承netty 规定好的某个HandlerAdapter(规范)
 * 这是我们自定义一个handler 才能称为一个handler
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {


    /*
           读取数据(这里我们可以读取客户端发送的消息)
           1.ChannelHandlerContext ctx 上下文对象 含有管道pipeline ,通道channel,地址
           2.Object msg :就是客户端发送的数据 默认Object
         */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx=" + ctx);
        //将msg转成一个ByteBuf(netty提供的)
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("客户端发送消息是:" + buf.toString(CharsetUtil.UTF_8));
        System.out.println("客户端地址: " + ctx.channel().remoteAddress());
        /*
        比如这里我们有一个非常耗时的任务, 可以异步执行 提交该channel对应的NIOEventLoop的taskQueue中
        如果再添加一个任务,他们都是一个线程顺序执行

            场景1(方案1是用户程序自定义的普通任务):

        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {

            }
        });
         */

        /*场景2(用户自定义定时任务)该任务是提交到scheduleTaskQueue:
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {

            }
         },5,TimeUnit.SECONDS);
         */
        /*
        场景3
         */


    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //writeAndFlush 是write +flush
        //将数据写入缓存,并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello 客户端 ", CharsetUtil.UTF_8));
    }

    //处理异常,一般是需要关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
