package com.suhong.netty.protocol.tcp;

import java.util.Arrays;

/**
 * 定义协议格式，自定义TCP协议格式，报文长度(length)+报文体
 *
 */
public class TcpMessageProtocol {

    private int length;

    private byte[] context;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContext() {
        return context;
    }

    public void setContext(byte[] context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "TcpMessageProtocol{" +
                "length='" + length + '\'' +
                ", context=" + Arrays.toString(context) +
                '}';
    }
}
