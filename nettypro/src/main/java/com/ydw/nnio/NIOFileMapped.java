package com.ydw.nnio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

public class NIOFileMapped {
    public static void main(String[] args) throws IOException {

        RandomAccessFile accessFile = new RandomAccessFile("e:\\file1.txt", "rw");

        FileChannel channel = accessFile.getChannel();
        /**
         * 直接内存映射
         */
        MappedByteBuffer buffer = channel.map(MapMode.READ_WRITE, 0, 3);

        buffer.put(0, (byte)'J');

        System.out.println("修改成功");

    }
}
