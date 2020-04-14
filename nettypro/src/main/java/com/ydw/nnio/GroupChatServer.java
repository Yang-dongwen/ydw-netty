package com.ydw.nnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class GroupChatServer {
    // 定义属性
    private Selector selector;
    private ServerSocketChannel listenChannel;
    private static final int Port = 7777;

    // 构造器 初始化工作

    public GroupChatServer() {
        try{
            selector = Selector.open();
            listenChannel = ServerSocketChannel.open();
            // 绑定端口
            listenChannel.socket().bind(new InetSocketAddress(Port));
            listenChannel.configureBlocking(false);
            // 将该listenChannel 注册到selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    // 监听
    public void listen(){
        try{
            while (true){
                int count = selector.select(2000);
                if (count > 0){  // 有事件处理
                    Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                    while (keyIterator.hasNext()){
                        // 取出selectorKey
                        SelectionKey key = keyIterator.next();

                        // 监听到accept
                        if (key.isAcceptable()){
                            SocketChannel sc = listenChannel.accept();
                            sc.configureBlocking(false);
                            // 注册到selector
                            sc.register(selector, SelectionKey.OP_READ);

                            // 提示
                            System.out.println(sc.getRemoteAddress() + "上线了...");
                        }
                        if (key.isReadable()){  // 通道是可读的状态
                            // 处理读操作(专门写方法....)
                            readData(key);
                        }
                        // 当前的key删除， 防止重复处理
                        keyIterator.remove();

                    }
                }else {
                    System.out.println("等待...");
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    // 读取客户端消息
    private void readData(SelectionKey key){
        // 定义一个SocketChannel
        SocketChannel channel = null;
        try{
            // 渠道关联的channel
            channel = (SocketChannel) key.channel();
            // 创建buffer
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count = channel.read(buffer);
            if(count  > 0){
                String msg = new String(buffer.array());
                System.out.println("from 客户端：" + msg);
                // 向其它的客户端转发消息(去掉自己)，专门写一个方法处理
                sendInfoToOther(msg, channel);
            }
        }catch (Exception ex){
            try {
                System.out.println(channel.getRemoteAddress() + "离线了...");
                // 取消注册
                key.cancel();;
                // 关闭通道
                channel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    // 转发消息给其它的客户（通道）
    private void sendInfoToOther(String msg, SocketChannel self) throws IOException {
        System.out.println("服务器转发消息中...");
        // 遍历所有注册到selector 上的SocketChannel, 并派出self
        for (SelectionKey key : selector.keys()){
            // 通过key 取出对应的SocketChannel
            Channel targetChannel = key.channel();
            // 排除自己
            if(targetChannel instanceof SocketChannel && targetChannel != self){
                // 转型
                SocketChannel dest = (SocketChannel) targetChannel;
                // 将msg存储到buffer
                ByteBuffer buffer = ByteBuffer.wrap(msg.getBytes());
                // 将buffer的数据写入到通道
                dest.write(buffer);
            }
        }
    }
    public static void main(String[] args) {
        GroupChatServer server = new GroupChatServer();
        server.listen();
    }
}
