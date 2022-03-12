package com.atguigu.netty.codec;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;

/*
 *使用protobuf传输数据

 */

public class NettyServer {

    public static void main(String[] args) throws InterruptedException {

        //创建BossGroup和WorkerGroup
        /*
         * 1.bossGroup只是处理连接请求,真正和客户端处理会交给workerGroup完成
           2.两个都是无限循环
           3.bossGroup和workerGroup 含有的子线程(NioEventLoop)的个数 默认cpu核数 * 2
         * */
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务器端的启动对象,配置参数
            ServerBootstrap bootstrap = new ServerBootstrap();

            //使用链式编程进行设置
            bootstrap.group(bossGroup, workerGroup) //设置两个线程组
                    .channel(NioServerSocketChannel.class)//使用NioServerSocketChannel作为服务器通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)//设置线程队列得到连接个数 如果超过会放进队列中等待
                    .childOption(ChannelOption.SO_KEEPALIVE, true)//设置保持活动连接状态
                    //.handler(null)//该handler对应bossGroup   childHandler 对应是workerGroup
                    .childHandler(new ChannelInitializer<SocketChannel>() {//创建一个通道初始化对象(匿名对象)
                        //给Pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //需要指定对哪种对象进行解码
                            ch.pipeline().addLast("decoder",new ProtobufDecoder(StudentPOJO.Student.getDefaultInstance()));
                            ch.pipeline().addLast(new NettyServerHandler());
                        }
                    });
            System.out.println("....服务器 is ready");

            //绑定一个端口并且同步,生成一个ChannnelFuture对象
            //启动服务器
            ChannelFuture cf = bootstrap.bind(6668).sync();

            //给cf注册一个监听器,监控我们关心的事情,这里是异步执行的
            cf.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(cf.isSuccess()){
                        System.out.println("监听端口6668成功");
                    }else {
                        System.out.println("监听端口6668失败");
                    }
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
