// runs min and max searches together
import java.util.Random;

public class SensorGridMonitorApp {
    public static void main(String[] args) {
        int size = 50000;
        double[] temperature = new double[size];   // used for the minimum search
        double[] vibration = new double[size];     // used for the maximum search

        Random random = new Random();
        for (int i = 0; i < size; i++) {
            temperature[i] = 15 + random.nextDouble() * 85;   // 15 to 100
            vibration[i] = random.nextDouble() * 10;          // 0 to 10
        }

        final ParallelMinSearch minSearch = new ParallelMinSearch(temperature, 0, size, 4);
        final ParallelMaxSearch maxSearch = new ParallelMaxSearch(vibration, 0, size, 4);

        long start = System.nanoTime();

        // run both reductions at the same time (each one uses 4 worker threads)
        Thread minRunner = new Thread(new Runnable() {
            public void run() {
                long t = System.nanoTime();
                minSearch.processGrid();
                System.out.println("Min search finished in " + (System.nanoTime() - t) / 1000 + " microseconds");
            }
        });
        Thread maxRunner = new Thread(new Runnable() {
            public void run() {
                long t = System.nanoTime();
                maxSearch.processGrid();
                System.out.println("Max search finished in " + (System.nanoTime() - t) / 1000 + " microseconds");
            }
        });

        minRunner.start();
        maxRunner.start();

        try {
            minRunner.join();
            maxRunner.join();
        } catch (InterruptedException e) {
            System.out.println("Main interrupted");
        }

        long total = System.nanoTime() - start;

        System.out.println();
        System.out.println("Sensors: " + size + " | Worker threads per search: 4");
        System.out.println("Critical minimum temperature : " + minSearch.getResult());
        System.out.println("Critical peak vibration      : " + maxSearch.getResult());
        System.out.println("Total execution time         : " + total / 1000 + " microseconds");
    }
}