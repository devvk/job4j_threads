package ru.job4j.wait;

public class Barrier {
    private boolean flag = false;
    private final Object monitor = this;

    public void on() {
        synchronized (monitor) {
            flag = true;
            System.out.println("Открываю и бужу всех.");
            // переводит все нити из состояния WAIT в RUNNABLE
            monitor.notifyAll();
        }
    }

    public void off() {
        synchronized (monitor) {
            flag = false;
            System.out.println("Закрываю.");
        }
    }

    public void check() {
        synchronized (monitor) {
            while (!flag) {
                try {
                    System.out.println(Thread.currentThread().getName() + " ждёт");
                    monitor.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            System.out.println(Thread.currentThread().getName() + " продолжил работу");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Barrier barrier = new Barrier();
        for (int i = 0; i < 10; i++) {
            new Thread(barrier::check, "Thread_" + i).start();
        }
        barrier.on();
    }
}