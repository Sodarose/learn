package org.example.route.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.example.route.handler.TcpMsgReadHandler2;

import java.net.SocketAddress;

/**
 * @Auther: kangkang
 * @Date: 2021/10/22 16:59
 * @Description:
 */
public class WClient {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        // 客户端配置
        bootstrap
                .group(workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new TcpMsgReadHandler2());
                    }
                });
        // 连接服务端
        final ChannelFuture channelFuture = bootstrap.connect("127.0.0.1",8080);
        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            public void operationComplete(Future<? super Void> future) throws Exception {
                if(future.isSuccess()){
                    System.err.println("连接成功");
                    Channel channel = channelFuture.channel();
                    channel.writeAndFlush("客户端发送消息，服务端收到了么？");
                }
            }
        });
        channelFuture.sync();
        Channel channel = channelFuture.channel();
        System.err.println("channel");
    }
}
