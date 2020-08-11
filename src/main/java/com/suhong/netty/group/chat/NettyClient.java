package com.suhong.netty.group.chat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.Scanner;

/**
 * Netty-客户端
 * */
public class NettyClient {

    private String ip;
    private int port;

    public NettyClient(String ip, int port){
        this.ip = ip;
        this.port = port;
    }

    //客户端初始化
    private void init(){
        EventLoopGroup worker = new NioEventLoopGroup(10);
        //创建监听事件的监听器
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(worker);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                    ChannelPipeline pipeline = nioSocketChannel.pipeline();
                    pipeline.addLast("encoder",new StringEncoder());
                    pipeline.addLast("decoder",new StringDecoder());
                    pipeline.addLast(new ClientMessageHandler());
                }
            });

            ChannelFuture future = bootstrap.connect(ip,port).sync();
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

            System.out.println(future.channel().localAddress().toString());
            System.out.println("#################################################");
            System.out.println("~~~~~~~~~~~~~~端口号#消息内容~~这样可以给单独一个用户发消息~~~~~~~~~~~~~~~~~~");
            System.out.println("#################################################");

            Channel channel = future.channel();
            Scanner scanner = new Scanner(System.in);
            while(scanner.hasNextLine()){
                String s = scanner.nextLine();
                channel.writeAndFlush(s+"\r\n");

            }
            future.channel().closeFuture().sync();
            scanner.close();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new NettyClient("127.0.0.1",7777).init();
    }

}
