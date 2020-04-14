package com.ydw.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class NettyServer {
    public static void main(String[] args) throws InterruptedException {
        // 创建BossGroup 和 WorkerGroup
        // 说明
        // 1.创建两个线程组
        //2.bossGroup只是处理连接请求，真正的客户端业务处理，会交给workerGroup完成
        // 3.两个都是无限循环
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            // 创建服务器端的启动对象
            ServerBootstrap bootstrap = new ServerBootstrap();

            // 使用链式b编程来进行设置
            bootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class) //使用NioSocketChannel 作为服务器的通道实现
                    .option(ChannelOption.SO_BACKLOG, 128) //设置线程队列得到连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        // 给pipeline 设置处理器
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                        }
                    }) ;// 给我们的workerGroup 的EventLoop 对应的管道设置处理器

            System.out.println("服务器 is ready...");

            // 绑定一个端口并且同步， 生成一个ChannelFuture对象
            ChannelFuture cf = bootstrap.bind(6661).sync();

            // 对关闭的通道进行监听
            cf.channel().closeFuture().sync();
        } finally {
            //优雅的关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }}
