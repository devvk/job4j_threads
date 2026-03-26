package ru.job4j.concurrent;

public class Flag {
    public static volatile boolean flag = true;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (flag) {
                try {
                    System.out.println(Thread.currentThread().getName());
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted");
                }
            }
        });
        thread.start();
        Thread.sleep(1000);
        flag = false;
        thread.join();
    }
}
