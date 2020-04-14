package com.ydw.nnio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

public class GroupChatClient {
    // 定义相关属性
    private final String HOST = "localhost";
    private final int PORT = 7777;
    private Selector selector;
    private SocketChannel socketChannel;
    private String username;

    //构造器

    public GroupChatClient() throws IOException {
        socketChannel = socketChannel.open(new InetSocketAddress(HOST, PORT));
        selector = Selector.open();

        socketChannel.configureBlocking(false);

        socketChannel.register(selector, SelectionKey.OP_READ);
        // 得到username
        username = socketChannel.getLocalAddress().toString();
        System.out.println(username + "is ok");
    }
    // 向服务器发送消息
    public void sendInfo(String info){
        info = username + "说：" + info;
        try{
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    // 读取从服务器端回复的消息
    public void readInfo(){
        try{
            int readChannel = selector.select();
            if(readChannel > 0){  // 有可用的通道
                Iterator<SelectionKey> keyIterator = selector.selectedKeys().iterator();
                while (keyIterator.hasNext()){
                    SelectionKey key = keyIterator.next();
                    if (key.isReadable()){
                        // 得到相关的通道
                        SocketChannel sc = (SocketChannel) key.channel();
                        // 得到一个Buffer
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        sc.read(buffer);
                        // 把督导的缓冲区的数据转换成字符串
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                    // 删除当前的selectorKey ,防止重复操作
                    keyIterator.remove();
                }
            }else{
                System.out.println("没有可用的通道");

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        // 启动我们的客户端
        GroupChatClient groupChatClient = new GroupChatClient();

        // 启动一个线程, 每隔3秒从服务器读取数据
        new Thread(){
            public void run(){
                while (true){
                    groupChatClient.readInfo();
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        // 发送数据给服务器
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){
            String s = scanner.nextLine();
            groupChatClient.sendInfo(s);
        }

    }

}
