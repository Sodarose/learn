package org.example.route.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @Auther: kangkang
 * @Date: 2021/10/22 18:21
 * @Description:
 */
public class TcpMsgReadHandler2 extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.err.println("客户端接受到消息:" + msg.toString());
    }
}
