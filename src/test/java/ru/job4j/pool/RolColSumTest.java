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

        RolColSum.Sums[] result = RolColSum.sum(matrix);

        assertThat(result[0].getRowSum()).isEqualTo(6);
        assertThat(result[0].getColSum()).isEqualTo(12);

        assertThat(result[1].getRowSum()).isEqualTo(15);
        assertThat(result[1].getColSum()).isEqualTo(15);

        assertThat(result[2].getRowSum()).isEqualTo(24);
        assertThat(result[2].getColSum()).isEqualTo(18);
    }

    @Test
    void whenAsyncSum() {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        RolColSum.Sums[] result = RolColSum.asyncSum(matrix);

        assertThat(result[0].getRowSum()).isEqualTo(6);
        assertThat(result[0].getColSum()).isEqualTo(12);

        assertThat(result[1].getRowSum()).isEqualTo(15);
        assertThat(result[1].getColSum()).isEqualTo(15);

        assertThat(result[2].getRowSum()).isEqualTo(24);
        assertThat(result[2].getColSum()).isEqualTo(18);
    }

    @Test
    void whenSyncAndAsyncAreEqual() {
        int[][] matrix = {
                {5, 1},
                {2, 3}
        };

        RolColSum.Sums[] sync = RolColSum.sum(matrix);
        RolColSum.Sums[] async = RolColSum.asyncSum(matrix);

        for (int i = 0; i < matrix.length; i++) {
            assertThat(async[i].getRowSum()).isEqualTo(sync[i].getRowSum());
            assertThat(async[i].getColSum()).isEqualTo(sync[i].getColSum());
        }
    }
}
