package com.ydw.nnio;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class Nio02 {
    public static void main(String[] args) throws IOException {
        // 声明要读的文件
        File file = new File("e:\\file.txt");
        // 创建文件输入流
        FileInputStream fis = new FileInputStream(file);

        // 获取输入流通道
        FileChannel channel = fis.getChannel();

        // 创建Buffer
        ByteBuffer buffer = ByteBuffer.allocate((int)file.length());

        // 将 通道中的数据写入到 buffer
        channel.read(buffer);

        System.out.println(new String(buffer.array()));

        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
