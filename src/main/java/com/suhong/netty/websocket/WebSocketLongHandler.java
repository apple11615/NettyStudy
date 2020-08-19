package com.suhong.netty.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDateTime;


public class WebSocketLongHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    protected void channelRead0(ChannelHandlerContext ch, TextWebSocketFrame msg) throws Exception {

           System.out.println("服务器收到信息:"+msg.text());

           ch.channel().writeAndFlush(new TextWebSocketFrame("服务器时间:"+ LocalDateTime.now() +" "+msg.text()));

    }

    /**
     * 当web客户端连接后，触发的handler
     * **/
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //ID代表唯一的值，longText是唯一的，shortText可能重复
        System.out.println("handler-added 被调用"+ctx.channel().id().asLongText());
        System.out.println("handler-added 被调用"+ctx.channel().id().asShortText());

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //连接移除后，触发handler
        System.out.println("handlerRemoved 被移除"+ctx.channel().id().asShortText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //异常连接时，触发的handler
        System.out.println("exceptionCaught 异常被移除"+ctx.channel().id().asShortText());
        ctx.close();
    }
}
