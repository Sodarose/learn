package org.example.route.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.example.route.handler.TcpMsgReadHandler;

import java.net.InetSocketAddress;

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
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new StringDecoder())
                                .addLast(new StringEncoder())
                                .addLast(new TcpMsgReadHandler());
                    }
                });
        // 连接服务端
        ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("127.0.0.1",8080));
        channelFuture.addListener((ChannelFutureListener) channelFuture1 -> {
            if(channelFuture1.isSuccess()){
                System.err.println("连接成功");
            }else{
                System.err.println("连接失败");
            }
        });
        channelFuture.sync();
    }
}
