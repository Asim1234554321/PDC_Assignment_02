import java.util.concurrent.RecursiveTask;

public class ParallelRiskCalculator extends RecursiveTask<Double> {

    private static final int THRESHOLD = 1000;

    private double[] assets;
    private int start;
    private int end;

    public ParallelRiskCalculator(double[] assets, int start, int end) {
        this.assets = assets;
        this.start = start;
        this.end = end;
    }

    protected Double compute() {
        if (end - start <= THRESHOLD) {
            double sum = 0;
            for (int i = start; i < end; i++) {
                sum += assets[i];
            }
            return sum;
        } else {
            int mid = (start + end) / 2;
            ParallelRiskCalculator left = new ParallelRiskCalculator(assets, start, mid);
            ParallelRiskCalculator right = new ParallelRiskCalculator(assets, mid, end);

            left.fork();
            right.fork();

            return left.join() + right.join();
        }
    }
}