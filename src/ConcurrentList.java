import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ConcurrentList {
    public static void main(String[] args) throws InterruptedException {
        List<Integer> sharedList = new CopyOnWriteArrayList<>();
        int numThreads = 10;
        int range = 100;

        Thread[] threads = new Thread[numThreads];
        for (int i = 0; i < numThreads; i++) {
            threads[i] = new Thread(() -> {
                for (int j = 1; j <= range; j++) {
                    sharedList.add(j);
                }
            });
            threads[i].start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Итоговый размер списка: " + sharedList.size());

        boolean allPresent = true;
        for (int i = 1; i <= range; i++){
            if(!sharedList.contains(i)){
                allPresent = false;
                break;
            }
        }
        System.out.println("Присутствуют ли все числа (1-100)? "+ allPresent);
    }
}
