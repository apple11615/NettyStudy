package com.suhong.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * websocket 长连接 netty实现
 *
 * */
public class WebSocketServer {
    public static void main(String[] args) {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup work = new NioEventLoopGroup(10);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(boss,work);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.handler(new LoggingHandler());
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    //增加http协议的编解码器，
                    pipeline.addLast(new HttpServerCodec());
                    //以块方式处理,ChunkedWriteHandler处理器
                    pipeline.addLast(new ChunkedWriteHandler());

                    //http传输数据会分段，HttpObjectAggregator可以将数据聚合，所以使用该handler
                    pipeline.addLast(new HttpObjectAggregator(8192));


                    //websocket ,它的数据是已帧的方式传递的
                    //浏览器请求使用的是WS方式，识别请求的URL地址
                    //重要功能：讲http协议升级为WS协议，即WS长连接协议
                    pipeline.addLast(new WebSocketServerProtocolHandler("/webSocket"));

                    //自定义handler处理业务
                    pipeline.addLast(new WebSocketLongHandler());


                }
            });


            ChannelFuture channelFuture = serverBootstrap.bind(7777).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }


    }

}
