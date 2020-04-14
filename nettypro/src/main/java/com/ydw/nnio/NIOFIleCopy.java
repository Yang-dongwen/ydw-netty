package com.ydw.nnio;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class NIOFIleCopy {

    public static void main(String[] args) throws IOException {

        // 创建文件输出流
        FileOutputStream ofs = new FileOutputStream("e:\\file1.txt");
        // 创建文件输入流
        FileInputStream fis = new FileInputStream("e:\\file.txt");

        // 获取通道
        FileChannel ofschannel = ofs.getChannel();
        FileChannel fisChannel = fis.getChannel();

        // 声明缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(512);

        // 读取文件 并 写入
        while (true){
            buffer.clear();
            int read = fisChannel.read(buffer);
            if (read == -1){
                break;
            }
            buffer.flip();
            ofschannel.write(buffer);
        }
        fisChannel.close();
        ofschannel.close();
        ofs.close();
        fis.close();

    }
}
