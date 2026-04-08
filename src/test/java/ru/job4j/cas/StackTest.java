package ru.job4j.cas;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class StackTest {
    @Test
    void when3PushThen3Poll() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertThat(stack.poll()).isEqualTo(3);
        assertThat(stack.poll()).isEqualTo(2);
        assertThat(stack.poll()).isEqualTo(1);
    }

    @Test
    void when1PushThen1Poll() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        assertThat(stack.poll()).isEqualTo(1);
    }

    @Test
    void when2PushThen2Poll() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        assertThat(stack.poll()).isEqualTo(2);
        assertThat(stack.poll()).isEqualTo(1);
    }
}