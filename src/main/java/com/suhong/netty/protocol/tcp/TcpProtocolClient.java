package com.suhong.netty.protocol.tcp;

import com.suhong.netty.group.chat.ClientMessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.nio.channels.SocketChannel;

/**
 * tcp自定义协议结构，报文长度+报文体  客户端程序
 */
public class TcpProtocolClient {
    public static void main(String[] args) {
        EventLoopGroup work = new NioEventLoopGroup(10);

        try {
            Bootstrap client = new Bootstrap();
            client.group(work);
            client.channel(NioSocketChannel.class);
            client.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                    ChannelPipeline pipeline = nioSocketChannel.pipeline();
                    pipeline.addLast("encoder",new MessageEncoder());
                    pipeline.addLast("decoder",new MessageDecoder());
                    pipeline.addLast(new TcpClientHandler());
                }

            });


           ChannelFuture future =  client.connect("127.0.0.1",7777).sync();
            future.addListener(new GenericFutureListener<Future<? super Void>>() {
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("客户端启动中...");
                    }
                    if(future.isDone()){
                        System.out.println("客户端启动成功..........OK！");
                    }
                }
            });

            future.channel().closeFuture().sync();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            work.shutdownGracefully();
        }
    }
}
