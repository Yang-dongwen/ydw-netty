package com.ydw.nnio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Nio01 {
    public static void main(String[] args) throws IOException {
        // 要写入文件的字符串
        String str = "hello, yang";
        // 创建文件输出流
        FileOutputStream ofs = new FileOutputStream("e:\\file.txt");
        // 获取管道
        FileChannel channel = ofs.getChannel();

        // 创建buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);

        // 将字符写入buffer
        buffer.put(str.getBytes());

        // 对缓冲区进行反转，用于读取写入文件
        buffer.flip();

        // 将buffer写入到通道
        channel.write(buffer);

        // 关闭通道
        channel.close();
    }
}
