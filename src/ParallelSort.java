import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ParallelSort {

    public static void parallelSort(int[] arr) throws InterruptedException {
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);

        int len = arr.length;
        int chunkSize = len / numThreads;
        int[][] chunks = new int[numThreads][];

        for (int i = 0; i < numThreads; i++) {
            int start = i * chunkSize;
            int end = (i == numThreads - 1) ? len : start + chunkSize;
            chunks[i] = Arrays.copyOfRange(arr, start, end);
        }


        FutureTaskSorter[] sorters = new FutureTaskSorter[numThreads];
        for (int i = 0; i < numThreads; i++) {
            sorters[i] = new FutureTaskSorter(chunks[i]);
            executor.submit(sorters[i]);
        }

        int[] sortedArr = new int[len];
        int index = 0;
        for (FutureTaskSorter sorter : sorters) {
            int[] sortedChunk = sorter.get();
            System.arraycopy(sortedChunk, 0, sortedArr, index, sortedChunk.length);
            index += sortedChunk.length;
        }

        Arrays.sort(sortedArr);

        System.arraycopy(sortedArr, 0, arr, 0, len);
        executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);

    }


    private static class FutureTaskSorter implements Callable<int[]> {
        private final int[] arr;

        public FutureTaskSorter(int[] arr) {
            this.arr = arr;
        }

        @Override
        public int[] call() {
            Arrays.sort(arr);
            return arr;
        }

        public int[] get()
        {
            return arr;
        }
    }


    public static void main(String[] args) throws InterruptedException {
        int[] arr = {5, 2, 8, 1, 9, 4, 7, 3, 6};

        System.out.println("Исходный массив: " + Arrays.toString(arr));
        long start = System.currentTimeMillis();
        parallelSort(arr);
        long end = System.currentTimeMillis();
        System.out.println("Отсортированный массив: " + Arrays.toString(arr));
        System.out.println("Потраченное время (мс): " + (end - start));
    }
}
