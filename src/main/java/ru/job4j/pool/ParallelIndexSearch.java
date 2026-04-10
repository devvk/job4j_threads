package ru.job4j.pool;

import java.util.Objects;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelIndexSearch<T> extends RecursiveTask<Integer> {

    private static final int THRESHOLD = 10;

    private final T[] array;
    private final T target;
    private final int from;
    private final int to;

    public ParallelIndexSearch(T[] array, T target, int from, int to) {
        this.array = array;
        this.target = target;
        this.from = from;
        this.to = to;
    }

    @Override
    protected Integer compute() {
        if (to - from + 1 <= THRESHOLD) {
            return linearSearch();
        }

        int mid = (from + to) / 2;

        ParallelIndexSearch<T> leftTask = new ParallelIndexSearch<>(array, target, from, mid);
        ParallelIndexSearch<T> rightTask = new ParallelIndexSearch<>(array, target, mid + 1, to);

        leftTask.fork();
        int rightResult = rightTask.compute();
        int leftResult = leftTask.join();

        return leftResult != -1 ? leftResult : rightResult;
    }

    private Integer linearSearch() {
        for (int i = from; i <= to; i++) {
            if (Objects.equals(array[i], target)) {
                return i;
            }
        }
        return -1;
    }

    public static <T> int search(T[] array, T target) {
        if (array == null || array.length == 0) {
            return -1;
        }
        ForkJoinPool pool = new ForkJoinPool();
        return pool.invoke(new ParallelIndexSearch<>(array, target, 0, array.length - 1));
    }
}
