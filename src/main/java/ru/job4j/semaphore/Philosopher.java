package ru.job4j.semaphore;

import java.util.concurrent.Semaphore;

public class Philosopher extends Thread {
    private final Semaphore semaphore;
    private final String name;

    public Philosopher(String name, Semaphore semaphore) {
        this.name = name;
        this.semaphore = semaphore;
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                // садится кушать
                semaphore.acquire();
                System.out.println(name + " садится кушать.");
                // кушает
                Thread.sleep(1000);
                // выходит из-за стола
                System.out.println(name + " поел и выходит из-за стола.");
                Thread.sleep(1000);
                semaphore.release();
            }
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
    }

    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(2, true);
        new Philosopher("Сократ", semaphore).start();
        new Philosopher("Платон", semaphore).start();
        new Philosopher("Аристотель", semaphore).start();
        new Philosopher("Фалес", semaphore).start();
        new Philosopher("Пифагор", semaphore).start();
    }
}
