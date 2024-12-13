import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ParallelMatrixMultiplication {

    public static int[][] multiplyMatrices(int[][] matrix1, int[][] matrix2) throws Exception {
        int rows1 = matrix1.length;
        int cols1 = matrix1[0].length;
        int rows2 = matrix2.length;
        int cols2 = matrix2[0].length;

        if (cols1 != rows2) {
            throw new IllegalArgumentException("Матрицы не могут быть умножены.");
        }

        int[][] result = new int[rows1][cols2];
        ExecutorService executor = Executors.newFixedThreadPool(rows1);

        try{
            Future<int[]>[] futures = new Future[rows1];
            for (int i = 0; i < rows1; i++) {
                futures[i] = executor.submit(new MatrixRowMultiplier(matrix1, matrix2, i));
            }

            for (int i = 0; i < rows1; i++){
                int[] row = futures[i].get();
                System.arraycopy(row, 0, result[i], 0, row.length);
            }

        }finally{
            executor.shutdown();
        }

        return result;
    }


    private static class MatrixRowMultiplier implements Callable<int[]> {
        private final int[][] matrix1;
        private final int[][] matrix2;
        private final int row;

        public MatrixRowMultiplier(int[][] matrix1, int[][] matrix2, int row) {
            this.matrix1 = matrix1;
            this.matrix2 = matrix2;
            this.row = row;
        }

        @Override
        public int[] call() {
            int cols2 = matrix2[0].length;
            int[] resultRow = new int[cols2];
            for (int j = 0; j < cols2; j++) {
                for (int k = 0; k < matrix1[0].length; k++) {
                    resultRow[j] += matrix1[row][k] * matrix2[k][j];
                }
            }
            return resultRow;
        }
    }



    public static void main(String[] args) throws Exception {
        int[][] matrixA = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        int[][] matrixB = {
                {9, 8, 7},
                {6, 5, 4},
                {3, 2, 1}
        };

        int[][] result = multiplyMatrices(matrixA, matrixB);
        for (int[] row : result) {
            System.out.println(Arrays.toString(row));
        }

        int size = 1000;
        int[][] largeA = new int[size][size];
        int[][] largeB = new int[size][size];
        for (int i = 0; i < size; i++) {
            Arrays.fill(largeA[i], 1);
            Arrays.fill(largeB[i], 1);
        }
        long start = System.currentTimeMillis();
        int[][] largeResult = multiplyMatrices(largeA, largeB);
        long end = System.currentTimeMillis();
        System.out.println("Для умножения большой матрицы потребовалось " + (end - start) + " миллисекунд.");
    }
}
