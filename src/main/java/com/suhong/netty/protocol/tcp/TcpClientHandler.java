package com.suhong.netty.protocol.tcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

public class TcpClientHandler extends SimpleChannelInboundHandler<TcpMessageProtocol> {
    private int count = 10;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TcpMessageProtocol msg) throws Exception {
        int length = msg.getLength();
        byte[] content = msg.getContext();
        System.out.println("客户端接收到消息如下:");
        System.out.println("长度:"+length);
        System.out.println("内容:"+new String(content,"utf-8"));
        System.out.println("客户端接收到包数量:"+(++count));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //使用客户端发送10条  “今天天气热，吃火锅” 编号
        System.out.println("已经连接到服务端.......,开始发送数据");

        for(int i=10;i>0;i--){
            String msg = "今天天气热，吃火锅";
            byte[] content = msg.getBytes(Charset.forName("utf-8"));
            int length = content.length;
            //创建自定义协议内容
            TcpMessageProtocol protocol = new TcpMessageProtocol();
            protocol.setContext(content);
            protocol.setLength(length);
            ctx.writeAndFlush(protocol);
        }
    }
}
