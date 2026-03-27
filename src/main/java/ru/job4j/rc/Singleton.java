package ru.job4j.rc;

/**
 * Race condition возникает в методе getInstance(), так как проверка instance == null
 * и создание объекта не синхронизированы, из-за чего несколько потоков могут создать
 * несколько экземпляров Singleton.
 */
public class Singleton {
    private static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
