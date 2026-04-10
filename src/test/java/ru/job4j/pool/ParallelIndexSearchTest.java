package ru.job4j.pool;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ParallelIndexSearchTest {

    @Test
    void whenIntegerArrayAndElementFound() {
        Integer[] array = {1, 2, 3, 4, 5};
        int rsl = ParallelIndexSearch.search(array, 4);
        assertThat(rsl).isEqualTo(3);
    }

    @Test
    void whenStringArrayAndElementFound() {
        String[] array = {"a", "b", "c", "d", "e"};
        int rsl = ParallelIndexSearch.search(array, "c");
        assertThat(rsl).isEqualTo(2);
    }

    @Test
    void whenSmallArrayThenLinearSearch() {
        Integer[] array = {10, 20, 30, 40, 50, 60};
        int rsl = ParallelIndexSearch.search(array, 50);
        assertThat(rsl).isEqualTo(4);
    }

    @Test
    void whenBigArrayThenRecursiveSearch() {
        Integer[] array = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int rsl = ParallelIndexSearch.search(array, 14);
        assertThat(rsl).isEqualTo(13);
    }

    @Test
    void whenElementNotFound() {
        Integer[] array = {1, 2, 3, 4, 5};
        int rsl = ParallelIndexSearch.search(array, 100);
        assertThat(rsl).isEqualTo(-1);
    }

    @Test
    void whenEmptyArray() {
        Integer[] array = {};
        int rsl = ParallelIndexSearch.search(array, 1);
        assertThat(rsl).isEqualTo(-1);
    }

    @Test
    void whenNullElementSearch() {
        String[] array = {"a", null, "c"};
        int rsl = ParallelIndexSearch.search(array, null);
        assertThat(rsl).isEqualTo(1);
    }
}
