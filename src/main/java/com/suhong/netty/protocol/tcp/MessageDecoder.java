package com.suhong.netty.protocol.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

/**
 * 自定义解码器
 */
public class MessageDecoder extends ReplayingDecoder<Void> {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MessageDecoder decoder 调用了自定义解码器");
        int len = in.readInt();

        byte[] content = new byte[len];
        in.readBytes(content);

        //封装层TcpMessageProtocol传给下一个handler处理
        TcpMessageProtocol protocol = new TcpMessageProtocol();
        protocol.setLength(len);
        protocol.setContext(content);

        out.add(protocol);

    }
}
