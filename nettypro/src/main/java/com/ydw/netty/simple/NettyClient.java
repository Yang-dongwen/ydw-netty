package com.ydw.netty.simple;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {
    public static void main(String[] args) throws InterruptedException {

            // 客户端需要一个事件循环组
            NioEventLoopGroup group = new NioEventLoopGroup();
            // 创建客户端启动对象
            //注意客户端使用的不是ServerBootstrap 而是 Bootstrap
            Bootstrap bootstrap = new Bootstrap();
        try {
            // 设置相关参数
            bootstrap.group(group)// 设置线程组
                    .channel(NioSocketChannel.class) // 设置客户端的实现类（反射）
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new NettyClientHandler());
                    }
                });
            System.out.println("客户端ok...");
            // 启动客户端连接服务端
            // 关于ChannelFuture 要分析， 涉及到netty的异步模型
            ChannelFuture channelFuture = bootstrap.connect("localhost", 6661).sync();
            channelFuture.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully();
        }

    }
}
