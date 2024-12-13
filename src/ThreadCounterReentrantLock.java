import java.util.concurrent.locks.ReentrantLock;

class CounterReentrantLock {
    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();

    public void increment() {
        lock.lock();  // Acquire the lock
        try {
            count++;
        } finally {
            lock.unlock(); // Release the lock, even if exceptions occur
        }
    }

    public int getCount() {
        return count;
    }
}


public class ThreadCounterReentrantLock {
    public static void main(String[] args) throws InterruptedException {
        CounterReentrantLock counter = new CounterReentrantLock();
        int numThreads = 5;
        int numIterations = 1000;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 0; j < numIterations; j++) {
                    counter.increment();
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Итоговый счетчик: " + counter.getCount());
    }
}
