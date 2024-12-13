import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreExample {
    private static final Semaphore semaphore = new Semaphore(2);

    public static void accessResource(int threadId) {
        try {
            if (semaphore.tryAcquire(1, 5, TimeUnit.SECONDS)) {
                System.out.println("Поток " + threadId + " приобретенный ресурс.");
                // Simulate resource usage
                Thread.sleep(2000);
                System.out.println("Поток " + threadId + " высвобождение ресурса.");
                semaphore.release(); // Release the permit after use
            } else {
                System.out.println("Поток " + threadId + " не удалось получить ресурс (время ожидания истекло).");
            }
        } catch (InterruptedException e) {
            System.err.println("Поток " + threadId + " прерванн: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        int numThreads = 5;
        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            int threadId = i + 1;
            threads[i] = new Thread(() -> accessResource(threadId));
            threads[i].start();
        }
    }
}
