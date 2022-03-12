package com.atguigu.netty.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/*
 * 说明
 * 1.我们自定义一个handler 需要继承netty 规定好的某个HandlerAdapter(规范)
 * 这是我们自定义一个handler 才能称为一个handler
 */
//public class NettyServerHandler extends ChannelInboundHandlerAdapter {

public class NettyServerHandler extends SimpleChannelInboundHandler<StudentPOJO.Student> {

    /*
           读取数据(这里我们可以读取客户端发送的消息)
           1.ChannelHandlerContext ctx 上下文对象 含有管道pipeline ,通道channel,地址
           2.Object msg :就是客户端发送的数据 默认Object

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //读取从客户端发送的StudentPojo.student
        StudentPOJO.Student student = (StudentPOJO.Student) msg;
        System.out.println("客户端发送的数据 id ="+student.getId()+" 名字= "+student.getName());
    }
    */

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, StudentPOJO.Student student) throws Exception {
        System.out.println("客户端发送的数据 id ="+student.getId()+" 名字= "+student.getName());
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