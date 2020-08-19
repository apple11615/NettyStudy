package com.suhong.netty.protocol.tcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 自定义协议 编码器
 *
 */
public class MessageEncoder  extends MessageToByteEncoder<TcpMessageProtocol> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TcpMessageProtocol msg, ByteBuf out) throws Exception {
           System.out.println("MessageEncoder encoder 自定义编码器被调用");
          out.writeInt(msg.getLength());
          out.writeBytes(msg.getContext());
    }

    protected String headLength(int len){
        int size = 8;
        StringBuffer buffer = new StringBuffer();
        for(int i=1;i<8-len;i++){
            buffer.append("0");
        }
        buffer.append(len);
        return buffer.toString();
    }
}
