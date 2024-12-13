import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ProductionLine {
    private static final BlockingQueue<Integer> queue = new LinkedBlockingQueue<>(10);

    public static void main(String[] args) {
        Thread producer = new Thread(() -> {
            for (int i = 1; i <= 20; i++) {
                try {
                    System.out.println("Производитель произвел: " + i);
                    queue.put(i);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.err.println("Продюссер прервался: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        });

        Thread consumer = new Thread(() -> {
            for (int i = 1; i <= 20; i++) {
                try {
                    int data = queue.take();
                    System.out.println("Пользователь потребляет: " + data);
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    System.err.println("Пользователь прервался: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            }
        });


        producer.start();
        consumer.start();
    }
}