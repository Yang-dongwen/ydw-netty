package com.ydw.nnio;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class ZeroCopyClient {
    public static void main(String[] args) throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost", 5555));

        FileChannel channel = new FileInputStream("C:\\Users\\DW\\Downloads\\ideaIU-2020.1.exe").getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        /*while (channel.read(buffer) != -1){
            buffer.flip();
            socketChannel.write(buffer);
        }*/
        long start = System.currentTimeMillis();
        channel.transferTo(0, channel.size(), socketChannel);
        System.out.println("传输时间： " + (System.currentTimeMillis() - start));

    }
}
