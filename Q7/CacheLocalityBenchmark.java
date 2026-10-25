public class CacheLocalityBenchmark {

    static final int N = 2000;      // matrix is 2000 x 2000
    static double[][] matrix = new double[N][N];

    static double traverseRowMajor() {
        double sum = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    static double traverseColumnMajor() {
        double sum = 0;
        for (int j = 0; j < N; j++) {
            for (int i = 0; i < N; i++) {
                sum += matrix[i][j];
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = i + j;
            }
        }

        int runs = 10;
        double rowTotal = 0;
        double colTotal = 0;
        double check = 0;   // keeps the JVM from removing the loops

        // Warm-up
        check += traverseRowMajor();
        check += traverseColumnMajor();

        System.out.println("Matrix: " + N + " x " + N + " doubles | Runs: " + runs);
        System.out.println();

        for (int r = 1; r <= runs; r++) {
            long t1 = System.nanoTime();
            check += traverseRowMajor();
            double rowMs = (System.nanoTime() - t1) / 1000000.0;

            long t2 = System.nanoTime();
            check += traverseColumnMajor();
            double colMs = (System.nanoTime() - t2) / 1000000.0;

            rowTotal += rowMs;
            colTotal += colMs;
            System.out.println("Run " + r + " | Row-Major: " + rowMs + " ms | Column-Major: " + colMs + " ms");
        }

        double rowAvg = rowTotal / runs;
        double colAvg = colTotal / runs;

        System.out.println();
        System.out.println("Average Row-Major    : " + rowAvg + " ms");
        System.out.println("Average Column-Major : " + colAvg + " ms");
        System.out.println("Speedup (Row-Major over Column-Major): " + (colAvg / rowAvg) + "x");
        System.out.println("(check value: " + check + ")");
    }
}