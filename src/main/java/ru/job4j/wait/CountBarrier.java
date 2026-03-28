package ru.job4j.wait;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 1. Пока метод count() не вызвали нужное число раз, все потоки в await() стоят и ждут.
 * 2. Как только count дошёл до total, ожидающие потоки могут идти дальше.
 */
@ThreadSafe
public class CountBarrier {

    private final int total;

    private final Object monitor = this;

    @GuardedBy("monitor")
    private int count = 0;

    public CountBarrier(final int total) {
        this.total = total;
    }

    public void count() {
        synchronized (monitor) {
            count++;
            monitor.notifyAll();
        }

    }

    public void await() {
        synchronized (monitor) {
            while (count < total) {
                try {
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public static void main(String[] args) {
        int limit = 3;
        CountBarrier barrier = new CountBarrier(limit);

        Thread waiter = new Thread(() -> {
            System.out.println("waiter: жду, пока count станет >= total");
            barrier.await();
            System.out.println("waiter: count >= total. Продолжаю работу.");
        });
        waiter.start();

        for (int i = 0; i < limit; i++) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    System.out.println("Thread: выполняю count()");
                    barrier.count();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }
    }
}
