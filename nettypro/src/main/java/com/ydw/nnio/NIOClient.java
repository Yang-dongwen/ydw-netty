package com.ydw.nnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NIOClient {
    public static void main(String[] args) throws IOException {
        // 得到一个网络通道
        SocketChannel socketChannel = SocketChannel.open();

        // 设置非阻塞
        socketChannel.configureBlocking(false);
        // 提供服务器的ip 和端口
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 6666);

        if(!socketChannel.connect(address)){
            while (!socketChannel.finishConnect()){
                System.out.println("因为连接需要事件，客户端不会阻塞，可以做其他工作");
            }
        }
        String str = "hello xiaoyang";
        //新的缓冲区将由给定的字节数组支持
        ByteBuffer buffer = ByteBuffer.wrap(str.getBytes());
        // 将buffer数据写入到channel
        socketChannel.write(buffer);
        System.in.read();


    }

}
