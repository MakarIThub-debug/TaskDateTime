import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(4);

        int numTasks = 20;
        for (int i = 0; i < numTasks; i++) {
            int taskNumber = i + 1;
            executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Поток " + threadName + " выполнил задачу " + taskNumber);
            });
        }

        executor.shutdown();

        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
                System.out.println("Задачи, которые не были выполнены в течение тайм-аута.");
            }
        } catch (InterruptedException e) {
            System.err.println("Прерванное завершение работы исполнителя: " + e.getMessage());
            executor.shutdownNow();
        }

        System.out.println("Все задания выполнены.");
    }
}
