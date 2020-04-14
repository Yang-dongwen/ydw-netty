package com.ydw.nnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ZiroCopyServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress(5555));
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();

            long start = System.currentTimeMillis();
            int read = 0;
            while (read != -1){
                try {
                    read = socketChannel.read(buffer);
                } catch (IOException e) {
                    break;
                }
                buffer.rewind();
            }
            System.out.println(System.currentTimeMillis() - start);

        }




    }
}
