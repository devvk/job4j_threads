package ru.job4j.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;

public class FileDownload implements Runnable {

    private final String url;
    private final int speed;

    public FileDownload(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        var startAt = System.currentTimeMillis();
        var uri = URI.create(url);
        String fileName = new File(uri.getPath()).getName();
        if (fileName.isBlank()) {
            fileName = "download.file";
        }
        var file = new File(System.getProperty("user.home") + "/Downloads/" + fileName);
        try (var input = uri.toURL().openStream(); var output = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            var dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                long start = System.nanoTime();
                output.write(dataBuffer, 0, bytesRead);
                long elapsed = System.nanoTime() - start;

                long expectedTimeMs = bytesRead / speed;
                long actualTimeMs = elapsed / 1_000_000;

                if (actualTimeMs < expectedTimeMs) {
                    try {
                        Thread.sleep(expectedTimeMs - actualTimeMs);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            System.out.println("Download finished");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.println(Files.size(file.toPath()) + " bytes");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: <url> <speed>");

        }
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        if (speed <= 0) {
            throw new IllegalArgumentException("Speed must be > 0");
        }
        System.out.println("Downloading " + url);
        System.out.println("Speed: " + speed);
        Thread thread = new Thread(new FileDownload(url, speed));
        thread.start();
        thread.join();
    }
}
