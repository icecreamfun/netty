package com.atguigu.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/*
netty心跳处理器  IdleStateHandler
 */
public class MyServer {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //加入一个netty 提供IdleStateHandler
                            /*
                            说明IdleStateHandler 是netty 提供的处理空闲状态的处理器
                            参数
                            long readerIdleTime:表示多长时间没有读,就会发送一个心跳检测包检测是否连接状态
                            long writerIdleTime:表示多长时间没有写 就会发送一个心跳检测包检测是否连接状态
                             long allIdleTime:表示多长时间没有读和写 就会发送一个心跳检测包检测是否连接状态
                             当IdleStateHandler触发后就会传递给管道的下一个handler处理
                             通过调用(触发)下一个handler的userEventTiggered,在该方法中处理
                             */
                            pipeline.addLast(new IdleStateHandler(3, 5, 7, TimeUnit.SECONDS));
                            //加入一个队空闲检测进一步处理的handler(自定义)
                            pipeline.addLast(new MyServerHandler());

                        }
                    });

            ChannelFuture channelFuture = b.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
