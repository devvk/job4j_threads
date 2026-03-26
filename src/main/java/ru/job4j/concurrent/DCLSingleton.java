package ru.job4j.concurrent;

/**
 * Без volatile в double check locking возможна проблема переупорядочивания.
 * Ссылка на объект может быть записана в instance раньше, чем объект будет полностью инициализирован.
 * Тогда другой поток увидит instance != null и получит не до конца созданный объект.
 */
public final class DCLSingleton {

    private static volatile DCLSingleton instance;

    public static DCLSingleton getInstance() {
        if (instance == null) {
            synchronized (DCLSingleton.class) {
                if (instance == null) {
                    instance = new DCLSingleton();
                }
            }
        }
        return instance;
    }

    private DCLSingleton() {
    }

}
