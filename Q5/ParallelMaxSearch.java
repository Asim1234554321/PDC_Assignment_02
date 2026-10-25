// Finds maximum using 4 threads
public class ParallelMaxSearch extends SensorSearchEngine {
    private int numThreads;
    private double result;

    public ParallelMaxSearch(double[] data, int startIdx, int endIdx, int numThreads) {
        super(data, startIdx, endIdx);
        this.numThreads = numThreads;
    }

    public void processGrid() {
        int chunk = (endIdx - startIdx) / numThreads;
        final double[] partial = new double[numThreads];
        Thread[] workers = new Thread[numThreads];

        for (int t = 0; t < numThreads; t++) {
            final int id = t;
            final int from = startIdx + t * chunk;
            final int to = (t == numThreads - 1) ? endIdx : from + chunk;

            workers[t] = new Thread(new Runnable() {
                public void run() {
                    double max = sensorData[from];
                    for (int i = from + 1; i < to; i++) {
                        if (sensorData[i] > max) {
                            max = sensorData[i];
                        }
                    }
                    partial[id] = max;
                }
            }, "Max-Worker-" + t);
            workers[t].start();
        }

        try {
            for (int t = 0; t < numThreads; t++) {
                workers[t].join();
            }
        } catch (InterruptedException e) {
            System.out.println("Max search interrupted");
        }

        result = partial[0];
        for (int t = 1; t < numThreads; t++) {
            if (partial[t] > result) {
                result = partial[t];
            }
        }
    }

    public double getResult() {
        return result;
    }
}