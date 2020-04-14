package com.ydw.nnio;

import java.nio.IntBuffer;

public class nio {
    public static void main(String[] args) {
        // Buffer的简单实用
        // 创建一个Buffer， 大小为5，即可以存放5个int
        IntBuffer intBuffer = IntBuffer.allocate(5);

        for (int i = 0; i < intBuffer.capacity(); i++){
            intBuffer.put(i * 2);
        }
        // 读写切换
        intBuffer.flip();
        while (intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
