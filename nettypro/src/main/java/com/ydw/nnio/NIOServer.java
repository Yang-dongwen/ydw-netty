package com.ydw.nnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class NIOServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        // 得到Selector对象
        Selector selector = Selector.open();

        // 绑定一个端口，在服务器监听
        serverSocketChannel.socket().bind(new InetSocketAddress(6666));

        // 设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        // 把serverSocket 注册到selector  关心事件为OP_ACCEPT
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println(selector.selectedKeys().size());

        // 循环等待客户连接
        while (true){

            // 等待一秒，如果没有事件发生，跳过
            if (selector.select(1000) == 0){
                System.out.println("无连接");
                continue;
            }
            // 如果返回的 大于 0. 就表示获取到相关的selectionKey集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();

            // 遍历selectionKeys
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();

            while (keyIterator.hasNext()){

                // 获取到key
                SelectionKey selectionKey = keyIterator.next();

                // 根据key，对应的通道发生的事件做相应处理
                if(selectionKey.isAcceptable()){
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("连接成功" + socketChannel.hashCode());
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                    System.out.println(selector.selectedKeys().size());
                }
                if (selectionKey.isReadable()){
                    SocketChannel channel = (SocketChannel)selectionKey.channel();
                    ByteBuffer buffer = (ByteBuffer)selectionKey.attachment();
                    channel.read(buffer);
                    System.out.println("form 客户端" + new String(buffer.array()));
                }
                // 手动从集合中移动当前的selectionKey，防止重复操作
                keyIterator.remove();

            }

        }
    }
}
