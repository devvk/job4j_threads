package ru.job4j.pool;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {

    public static Sums[] sum(int[][] matrix) {
        int size = matrix.length;
        Sums[] result = new Sums[size];
        for (int i = 0; i < size; i++) {
            result[i] = getSums(matrix, i);
        }
        return result;
    }

    public static Sums[] asyncSum(int[][] matrix) {
        int size = matrix.length;
        Sums[] result = new Sums[size];
        CompletableFuture<Sums>[] futures = new CompletableFuture[size];

        for (int i = 0; i < size; i++) {
            final int index = i;
            futures[i] = CompletableFuture.supplyAsync(() -> getSums(matrix, index));
        }

        for (int i = 0; i < size; i++) {
            try {
                result[i] = futures[i].get();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    private static Sums getSums(int[][] matrix, int index) {
        int rowSum = 0;
        int colSum = 0;
        for (int i = 0; i < matrix.length; i++) {
            rowSum += matrix[index][i];
            colSum += matrix[i][index];
        }
        return new Sums(rowSum, colSum);
    }
}
