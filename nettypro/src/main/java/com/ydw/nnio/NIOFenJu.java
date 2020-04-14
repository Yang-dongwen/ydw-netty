package com.ydw.nnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class NIOFenJu {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel channel = ServerSocketChannel.open();

        InetSocketAddress address = new InetSocketAddress(6666);

        channel.socket().bind(address);

        ByteBuffer[] buffers = new ByteBuffer[2];
        buffers[0] = ByteBuffer.allocate(3);
        buffers[1] = ByteBuffer.allocate(4);
        SocketChannel socketChannel = channel.accept();  // 等待客户端连接

        int bufferLength = 7;
        while (true){

            int bufferIndex = 0;
            while (bufferIndex < bufferLength){
                long read = socketChannel.read(buffers);

            }


        }

    }
}
