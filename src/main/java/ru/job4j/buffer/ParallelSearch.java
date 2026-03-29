package ru.job4j.buffer;

import ru.job4j.wait.SimpleBlockingQueue;

public class ParallelSearch {

    public static void main(String[] args) {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(10);

        final Thread consumer = new Thread(
                () -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            System.out.println(queue.poll());
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
        );
        consumer.start();

        Thread producer = new Thread(
                () -> {
                    for (int i = 0; i < 3; i++) {
                        try {
                            queue.offer(i);
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

        );
        producer.start();

        try {
            // ждём пока producer завершит работу
            producer.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // выход из wait
        consumer.interrupt();
    }
}
