package ru.job4j.concurrent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;

public class FileDownload implements Runnable {

    private final String url;
    private final int speed;
    private final String fileName;

    public FileDownload(String url, String fileName, int speed) {
        this.url = url;
        this.speed = speed;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        var uri = URI.create(url);
        var file = new File(fileName);

        try (var input = uri.toURL().openStream(); var output = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            int downloaded = 0;
            long start = System.currentTimeMillis();

            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
                downloaded += bytesRead;

                if (downloaded >= speed) {
                    long elapsed = System.currentTimeMillis() - start;
                    if (elapsed < 1000) {
                        Thread.sleep(1000 - elapsed);
                    }
                    downloaded -= speed;
                    start = System.currentTimeMillis();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length != 3) {
            throw new IllegalArgumentException("Usage: <url> <fileName> <speed>");

        }
        String url = args[0];
        String fileName = args[1];
        int speed = Integer.parseInt(args[2]);
        if (speed <= 0) {
            throw new IllegalArgumentException("Speed must be > 0");
        }
        Thread thread = new Thread(new FileDownload(url, fileName, speed));
        thread.start();
        thread.join();
    }
}
