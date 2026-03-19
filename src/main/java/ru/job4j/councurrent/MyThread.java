package ru.job4j.councurrent;

public class MyThread extends Thread {

    @Override
    public void run() {
        System.out.println(getName());
    }

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new MyThread().start();
        }
    }
}
