import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CallableFutureExample {

    public static long factorial(int n) {
        if (n == 0) {
            return 1;
        } else {
            long result = 1;
            for (int i = 1; i <= n; i++) {
                result *= i;
            }
            return result;
        }
    }


    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newFixedThreadPool(10);
        List<Future<Long>> futures = new ArrayList<>();
        int numTasks = 10;

        for (int i = 0; i < numTasks; i++) {
            int num = i + 1;
            Callable<Long> task = () -> factorial(num);
            Future<Long> future = executor.submit(task);
            futures.add(future);
        }

        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);

        for (int i = 0; i < numTasks; i++) {
            long result = futures.get(i).get();
            int num = i + 1;
            System.out.println("Факториал " + num + " это" + result);
        }
    }
}
