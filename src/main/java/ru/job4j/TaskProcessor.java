package ru.job4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskProcessor {

    private final BlockingQueue<Runnable> queue;
    private final List<Thread> workers = new ArrayList<>();
    private volatile boolean running = true;

    public TaskProcessor(int queueCapacity, int workerCount) {
        this.queue = new LinkedBlockingQueue<>(queueCapacity);
        for (int i = 0; i < workerCount; i++) {
            Thread worker = new Thread(new TaskWorker(), "worker-" + i);
            workers.add(worker);
        }
    }

    public void start() {
        for (Thread worker : workers) {
            worker.start();
        }
    }

    /**
     * Добавляем задачу в очередь.
     * Если сервис остановлен, новую задачу не принимаем.
     */
    public void submit(Runnable task) throws InterruptedException {
        if (!running) {
            throw new IllegalStateException("TaskProcessor is stopped. New tasks are not accepted.");
        }
        queue.put(task);
    }

    /**
     * Мягкая остановка:
     * 1. перестаём принимать новые задачи
     * 2. даём worker'ам доработать очередь
     * 3. прерываем worker'ов, если они ждут на queue.take()
     */
    public void shutdown() {
        running = false;
        for (Thread worker : workers) {
            worker.interrupt();
        }
    }

    /**
     * Ждём завершения всех worker'ов.
     */
    public void awaitTermination() throws InterruptedException {
        for (Thread worker : workers) {
            worker.join();
        }
    }

    private class TaskWorker implements Runnable {
        @Override
        public void run() {
            while (running || !queue.isEmpty()) {
                try {
                    Runnable task = queue.take();
                    task.run();
                } catch (InterruptedException e) {
                    /*
                     * Если interrupt пришёл:
                     * - при running = false выходим, если очередь уже пуста
                     * - если очередь ещё не пуста, цикл продолжится и дочистит задачи
                     */
                    if (!running && queue.isEmpty()) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } catch (Exception e) {
                    System.err.println(Thread.currentThread().getName()
                            + " task failed: " + e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        TaskProcessor processor = new TaskProcessor(10, 3);
        processor.start();

        AtomicInteger counter = new AtomicInteger();

        for (int i = 1; i <= 15; i++) {
            int taskId = i;
            processor.submit(() -> {
                System.out.println(Thread.currentThread().getName()
                        + " started task " + taskId);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.out.println(Thread.currentThread().getName()
                            + " interrupted during task " + taskId);
                    return;
                }
                counter.incrementAndGet();
                System.out.println(Thread.currentThread().getName()
                        + " finished task " + taskId);
            });
        }

        Thread.sleep(2000);

        processor.shutdown();
        processor.awaitTermination();

        System.out.println("Completed tasks: " + counter.get());
        System.out.println("Main finished");
    }
}
