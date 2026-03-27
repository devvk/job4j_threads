package ru.job4j.rc;

public class RaceConditionExample {

    public static int number = 0;

    public synchronized void increment() {
        for (int i = 0; i < 99999; i++) {
            int current = number;
            int next = ++number;
            if (current + 1 != next) {
                throw new IllegalStateException("Некорректное сравнение: " + current + " + 1 = " + next);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        RaceConditionExample raceCondition = new RaceConditionExample();
        Thread thread1 = new Thread(raceCondition::increment);
        thread1.start();
        Thread thread2 = new Thread(raceCondition::increment);
        thread2.start();
        thread1.join();
        thread2.join();
    }
}
