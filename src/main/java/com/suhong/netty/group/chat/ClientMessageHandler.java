package com.suhong.netty.group.chat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class ClientMessageHandler extends SimpleChannelInboundHandler<String> {


    /**
     * 处理收到的消息
     * */
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
          System.out.println(s);
    }

    /***
     * 异常后处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
