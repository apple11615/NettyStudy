package com.suhong.netty.group.chat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/*
 * netty-群聊服务端
 * * */
public class NettyBootServer {

    private int port;

    public NettyBootServer(int port) {
        this.port = port;
    }

    private void init() throws InterruptedException {
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup(10);

        ServerBootstrap serverBootstrap = null;
        try {
            serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(boss, worker);
            serverBootstrap.channel(NioServerSocketChannel.class);//设置boss selector建立channel使用的对象
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128);//boss 等待连接的 队列长度
            serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);//让客户端保持长期活动状态
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    //从channel中获取pipeline 并往里边添加Handler
                    ChannelPipeline pipeline = socketChannel.pipeline();
                    pipeline.addLast("encoder", new StringEncoder());
                    pipeline.addLast("decoder", new StringDecoder());
                    pipeline.addLast(new ServerMessageHandler());//自定义Handler来处理消息
                }
            });
            System.out.println("服务器开始启动....................");
            //绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("服务器正在启动...................");
                    }
                    if (future.isDone()) {
                        System.out.println("服务器已启动成功.................OK");
                    }
                }
            });

            channelFuture.channel().closeFuture().sync();
            channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isCancelled()) {
                        System.out.println("服务器正在关闭..............");
                    }
                    if (future.isCancellable()) {
                        System.out.println("服务器已经关闭................OK");
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) throws InterruptedException {
           System.out.println("服务端启动开始..............");
           new NettyBootServer(7777).init();
    }
}
