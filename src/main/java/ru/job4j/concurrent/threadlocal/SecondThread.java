package ru.job4j.concurrent.threadlocal;

public class SecondThread extends Thread {

    @Override
    public void run() {
        ThreadLocalDemo.threadLocal.set("SecondThread");
        System.out.println(ThreadLocalDemo.threadLocal.get());
    }
}
