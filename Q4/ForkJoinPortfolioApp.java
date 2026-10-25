//main
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinPortfolioApp {

    static double serialSum(double[] a) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += a[i];
        }
        return sum;
    }

    static void serialPenalty(double[] a, double cap) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] > cap) {
                a[i] = a[i] * 0.95;
            }
        }
    }

    public static void main(String[] args) {
        int size = 10000;
        double riskCap = 800.0;
        int runs = 200;

        double[] original = new double[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            original[i] = random.nextDouble() * 1000;
        }

        int aboveCap = 0;
        for (int i = 0; i < size; i++) {
            if (original[i] > riskCap) {
                aboveCap++;
            }
        }

        ForkJoinPool pool = new ForkJoinPool();

        // Warm-up
        for (int r = 0; r < 50; r++) {
            double[] w = original.clone();
            serialPenalty(w, riskCap);
            serialSum(w);
            pool.invoke(new PortfolioThresholdTask(w, 0, size, riskCap));
            pool.invoke(new ParallelRiskCalculator(w, 0, size));
        }

        long serialTotal = 0;
        long parallelTotal = 0;
        double serialRisk = 0;
        double parallelRisk = 0;

        for (int r = 0; r < runs; r++) {
            double[] s = original.clone();
            double[] p = original.clone();

            long t1 = System.nanoTime();
            serialPenalty(s, riskCap);
            serialRisk = serialSum(s);
            serialTotal += System.nanoTime() - t1;

            long t2 = System.nanoTime();
            pool.invoke(new PortfolioThresholdTask(p, 0, size, riskCap));
            parallelRisk = pool.invoke(new ParallelRiskCalculator(p, 0, size));
            parallelTotal += System.nanoTime() - t2;
        }
        pool.shutdown();

        double serialMs = serialTotal / 1000000.0 / runs;
        double parallelMs = parallelTotal / 1000000.0 / runs;

        System.out.println("Assets: " + size + " | Risk cap: " + riskCap
                + " | Assets above cap: " + aboveCap);
        System.out.println("Total risk before penalty : " + serialSum(original));
        System.out.println("Serial total risk after   : " + serialRisk);
        System.out.println("Parallel total risk after : " + parallelRisk);
        System.out.println();
        System.out.println("Serial time   : " + serialMs + " ms (average of " + runs + " runs)");
        System.out.println("Parallel time : " + parallelMs + " ms (average of " + runs + " runs)");
        System.out.println("Speedup       : " + (serialMs / parallelMs) + "x");
    }
}