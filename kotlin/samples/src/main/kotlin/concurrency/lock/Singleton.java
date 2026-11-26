package concurrency.lock;

import concurrency.ThreadSafe;

/**
 * Correct implementation of thread safe singleton in Java
 */
@ThreadSafe
public class Singleton {

    private static volatile Singleton INSTANCE;

    private Singleton() {}

    public static Singleton getInstance() {
        if (INSTANCE == null) {
            synchronized (Singleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }

}
