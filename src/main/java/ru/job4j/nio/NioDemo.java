package ru.job4j.nio;

import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class NioDemo {
    public static void main(String[] args) {
        int count;
        try (SeekableByteChannel byteChannel = Files.newByteChannel(Paths.get("data/nio.txt"))) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            do {
                count = byteChannel.read(buffer);
                if (count != -1) {
                    buffer.flip();
                    System.out.print(StandardCharsets.UTF_8.decode(buffer));
                    buffer.clear();
                }
            } while (count != -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
