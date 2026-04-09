package ru.job4j.pool;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

class ThreadPoolTest {
    @Test
    void whenSubmitTasksThenTheyAreExecuted() throws InterruptedException {
        int tasksCount = 20;
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(tasksCount);
        ThreadPool threadPool = new ThreadPool();
        for (int i = 0; i < tasksCount; i++) {
            threadPool.work(() -> {
                counter.incrementAndGet();
                latch.countDown();
            });
        }
        latch.await(); // ждём все задачи
        threadPool.shutdown();
        assertThat(counter.get()).isEqualTo(tasksCount);
    }

    @Test
    void whenTaskThrowsExceptionThenPoolContinuesToWork() throws InterruptedException {
        int tasksCount = 10;
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(tasksCount);
        ThreadPool threadPool = new ThreadPool();
        threadPool.work(() -> {
            throw new RuntimeException("Task failed!");
        });
        for (int i = 0; i < tasksCount; i++) {
            threadPool.work(() -> {
                counter.incrementAndGet();
                latch.countDown();
            });
        }
        latch.await();
        threadPool.shutdown();
        assertThat(counter.get()).isEqualTo(tasksCount);
    }

    @Test
    void whenShutdownThenWorkersAreInterrupted() throws Exception {
        ThreadPool threadPool = new ThreadPool();
        threadPool.shutdown();
        Thread.sleep(200);
        List<Thread> threads = threadPool.getThreads();
        for (Thread thread : threads) {
            assertThat(thread.isAlive()).isFalse();
        }
    }
}