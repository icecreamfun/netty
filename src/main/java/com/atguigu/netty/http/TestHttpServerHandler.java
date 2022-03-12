package com.atguigu.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;

/*
  SimpleChannelInboundHandler 是  ChannelInboundHandlerAdapter
  HttpObject 客户端和服务器端相互通讯的数据被封装成HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    //读取客户端数据
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        //判断ms是不是httprequest请求

        if (msg instanceof HttpRequest){
            System.out.println("msg 类型= "+msg.getClass());
            System.out.println("客户端地址" + ctx.channel().remoteAddress());


            HttpRequest httpRequest= (HttpRequest) msg;
            //获取uri过滤特定资源
            URI uri = new URI(httpRequest.uri());
            if("/favicon.ico".equals(uri.getPath())){
                System.out.println("请求了 favicon.ico 不做响应 ");
                return;
            }

            //回复信息给浏览器[http协议]
            ByteBuf content = Unpooled.copiedBuffer("hello 我是服务器", CharsetUtil.UTF_8);

            //构造一个http的响应, 即httpResponse
            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, content);
            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,content.readableBytes());

            //返回
            ctx.writeAndFlush(response);
        }

    }
}
