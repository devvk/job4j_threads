package ru.job4j.wait;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

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
}