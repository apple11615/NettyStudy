package com.suhong.netty.heart.detect;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * netty-心跳案例，心跳探测
 *
 */
public class NettyServer {

    public static void main(String[] args) {
         EventLoopGroup bossGroup = new NioEventLoopGroup(1);
         EventLoopGroup workGroup = new NioEventLoopGroup(10);

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.handler(new LoggingHandler());
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                protected void initChannel(SocketChannel socketChannel) throws Exception {
                    ChannelPipeline channelPipeline = socketChannel.pipeline();

                    //加入netty -IdleStateHandler
                    /**
                     *IdleStateHandler netty提供的空闲的状态的处理器
                     * readerIdleTime 读空间时间
                     * writeIdleTime   写空闲时间
                     * allIdleTime     读写空间时间间隔
                     * 文档说明
                     * trigger
                     *当IdleStateHandle触发后，会触发下一个handle处理，处理的方法是，userEventTrigger
                     */
                    channelPipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS));
                    //自己的异常处理
                    channelPipeline.addLast(new HeartHandler());

                    channelPipeline.addLast(new SimpleChannelInboundHandler<String>() {

                        protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
                            System.out.println("接收到消息:"+s);
                        }

                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            System.out.println("接收到消息:"+(String)msg);
                        }

                        @Override
                        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
                            Channel channel = ctx.channel();
                            String addr = channel.remoteAddress().toString();
                            System.out.println(addr+",客户端已下线....");
                        }

                        @Override
                        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                            if (cause instanceof IOException) {
                                Channel channel = ctx.channel();
                                String addr = channel.remoteAddress().toString();
                                System.out.println(addr+",客户端主动关闭");
                            }

                        }


                    });

                }
            });

            ChannelFuture channelFuture =  serverBootstrap.bind(7777).sync();
            channelFuture.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }

    }
}
