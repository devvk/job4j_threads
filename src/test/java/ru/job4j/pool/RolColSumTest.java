package ru.job4j.pool;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RolColSumTest {

    @Test
    void whenSum() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        Sums[] expected = {
                new Sums(6, 12),
                new Sums(15, 15),
                new Sums(24, 18)
        };
        Sums[] result = RolColSum.sum(matrix);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void whenAsyncSum() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        Sums[] expected = {
                new Sums(6, 12),
                new Sums(15, 15),
                new Sums(24, 18)
        };
        Sums[] result = RolColSum.asyncSum(matrix);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    void whenSyncAndAsyncAreEqual() {
        int[][] matrix = {
                {5, 1},
                {2, 3}
        };
        Sums[] sync = RolColSum.sum(matrix);
        Sums[] async = RolColSum.asyncSum(matrix);
        assertThat(async).isEqualTo(sync);
    }
}
