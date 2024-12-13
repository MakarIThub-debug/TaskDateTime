import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {
    private static final int NUM_PHILOSOPHERS = 5;
    private static final ReentrantLock[] forks = new ReentrantLock[NUM_PHILOSOPHERS];

    static {
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            forks[i] = new ReentrantLock();
        }
    }

    public static void philosopher(int i) {
        try {
            while (true) {
                think(i);
                int leftFork = i;
                int rightFork = (i + 1) % NUM_PHILOSOPHERS;
                if (i==0){
                    forks[leftFork].lock();
                    forks[rightFork].lock();
                }else {
                    forks[rightFork].lock();
                    forks[leftFork].lock();
                }


                eat(i);
                forks[leftFork].unlock();
                forks[rightFork].unlock();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void think(int i) throws InterruptedException {
        System.out.println("Философ " + i + " думает");
        Thread.sleep(1000); //Simulate thinking
    }

    private static void eat(int i) throws InterruptedException {
        System.out.println("Философ " + i + " ест.");
        Thread.sleep(2000); //Simulate eating
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[NUM_PHILOSOPHERS];
        for (int i = 0; i < NUM_PHILOSOPHERS; i++) {
            int finalI = i;
            threads[i] = new Thread(() -> philosopher(finalI));
            threads[i].start();
        }
    }
}
