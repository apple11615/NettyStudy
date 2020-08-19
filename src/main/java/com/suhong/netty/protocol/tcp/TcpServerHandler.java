package com.suhong.netty.protocol.tcp;

import com.sun.deploy.util.StringUtils;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * server 端handler处理
 */
public class TcpServerHandler extends SimpleChannelInboundHandler<TcpMessageProtocol> {
    //
    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TcpMessageProtocol msg) throws Exception {
        System.out.println("服务端接收处理............");
         //接收到数据处理
        int len = msg.getLength();
        int size = Integer.valueOf(len);
        byte[] content = msg.getContext();

           System.out.println("服务端接收到长度:"+len);
           System.out.println("服务端接收到内容:"+new String(content,"utf-8"));
           System.out.println("服务端接收到消息包数量="+(++this.count));

           //回复消息
        String responseContent = UUID.randomUUID().toString();
        int responseLen = responseContent.getBytes("utf-8").length;
        byte[] responseContent2 = responseContent.getBytes("utf-8");
        //构建一个新协议包
        TcpMessageProtocol protocol = new TcpMessageProtocol();
        protocol.setLength(responseLen);
        protocol.setContext(responseContent2);
        ctx.writeAndFlush(protocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
