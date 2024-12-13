import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultithreadedTimer {
    private static final AtomicBoolean running = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException {
        Thread timerThread = new Thread(() -> {
            while (running.get()) {
                System.out.println("Текущее время: " + LocalDateTime.now());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println("поток таймера был прерван");
                    Thread.currentThread().interrupt();
                    return;
                }
            }
            System.out.println("Таймер остановлен.");
        });

        Thread stopperThread = new Thread(() -> {
            try {
                Thread.sleep(10000);
                running.set(false);
                System.out.println("Поток останавливает таймер");
            } catch (InterruptedException e) {
                System.err.println("Поток был прерван: " + e.getMessage());
                Thread.currentThread().interrupt();
            }
        });

        timerThread.start();
        stopperThread.start();
        timerThread.join();
        stopperThread.join();

        System.out.println("Программа завершена.");
    }
}
