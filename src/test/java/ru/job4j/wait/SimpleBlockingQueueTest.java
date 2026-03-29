package ru.job4j.wait;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class SimpleBlockingQueueTest {

    @Test
    void whenOfferTwoThenGetTwo() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);
        Thread producer = new Thread(() -> {
            try {
                queue.offer(100);
                queue.offer(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        List<Integer> list = new ArrayList<>();
        Thread consumer = new Thread(() -> {
            try {
                list.add(queue.poll());
                list.add(queue.poll());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertEquals(List.of(100, 200), list);
    }

    @Test
    void whenQueueIsFullThenProducerIsWaits() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(1);
        Thread producer = new Thread(() -> {
            try {
                queue.offer(100);
                queue.offer(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        producer.start();
        Thread.sleep(100);
        assertSame(Thread.State.WAITING, producer.getState());
        assertEquals(100, queue.poll());
        producer.join();
        assertSame(Thread.State.TERMINATED, producer.getState());
        assertEquals(200, queue.poll());
    }

    @Test
    void whenFetchAllThenGetIt() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(3);
        CopyOnWriteArrayList<Integer> buffer = new CopyOnWriteArrayList<>();

        Thread producer = new Thread(
                () -> {
                    try {
                        for (int i = 0; i < 5; i++) {
                            queue.offer(i);
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });

        Thread consumer = new Thread(
                () -> {
                    while (!queue.isEmpty() || !Thread.currentThread().isInterrupted()) {
                        try {
                            buffer.add(queue.poll());
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                });

        producer.start();
        consumer.start();
        producer.join();
        consumer.interrupt();
        consumer.join();
        assertEquals(List.of(0, 1, 2, 3, 4), buffer);
    }
}
