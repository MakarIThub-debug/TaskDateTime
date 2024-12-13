import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {

    public static void main(String[] args) {
        final int NUMBER_OF_THREADS = 5;
        CyclicBarrier barrier = new CyclicBarrier(NUMBER_OF_THREADS, new Runnable() {
            @Override
            public void run() {
                System.out.println("Все потоки завершили свои задачи, продолжаем к следующей фазе.");
            }
        });

        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            final int threadNumber = i + 1;
            new Thread(() -> {
                try {
                    System.out.println("Поток " + threadNumber + " выполняет свою задачу.");
                    Thread.sleep((long) (Math.random() * 1000));
                    System.out.println("Поток " + threadNumber + " завершил свою задачу.");
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }
}