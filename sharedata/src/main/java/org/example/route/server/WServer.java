package org.example.route.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.example.route.handler.TcpMsgReadHandler;

/**
 * @Auther: kangkang
 * @Date: 2021/10/22 16:58
 * @Description:
 */
public class WServer {

    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        // 设置服务端配置
        serverBootstrap
                .group(bossGroup, workerGroup) // 线程组
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.ALLOCATOR, UnpooledByteBufAllocator.DEFAULT)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_RCVBUF, 1024 * 32)
                .childOption(ChannelOption.SO_SNDBUF, 1024 * 32);
        // 绑定处理器
        serverBootstrap.handler(new LoggingHandler());
        serverBootstrap.childHandler(new ChannelInitializer<Channel>() {
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline()
                        .addLast(new StringEncoder())
                        .addLast(new StringDecoder())
                        .addLast(new TcpMsgReadHandler());
            }
        });
        // 绑定端口
        ChannelFuture channelFuture = serverBootstrap.bind("127.0.0.1",8080);
        channelFuture.addListener(new GenericFutureListener<Future<? super Void>>() {
            public void operationComplete(Future<? super Void> future) throws Exception {
                if (future.isSuccess()) {
                    System.err.println("服务器开启成功");
                }
            }
        });
        //
        channelFuture.sync();
    }
}
