// imports hash,ap and executors
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class InventoryCatalogManager {

    private static final int PRODUCTS = 1000;
    private static final int WORKER_THREADS = 10;
    private static final int OPS_PER_THREAD = 50000;   // 50,000 operations by each of the 10 threads

    static void fillCatalog(Map<String, Integer> map) {
        for (int i = 0; i < PRODUCTS; i++) {
            map.put("P" + i, 100);
        }
    }

    // Runs 10 workers, each doing lookups and updates; returns elapsed milliseconds
    static double runWorkers(final Map<String, Integer> map, final boolean useLock) {
        ExecutorService pool = Executors.newFixedThreadPool(WORKER_THREADS);
        long start = System.nanoTime();

        for (int t = 0; t < WORKER_THREADS; t++) {
            pool.submit(new Runnable() {
                public void run() {
                    Random random = new Random();
                    for (int i = 0; i < OPS_PER_THREAD; i++) {
                        String id = "P" + random.nextInt(PRODUCTS);
                        if (random.nextBoolean()) {
                            map.get(id);                      // stock lookup
                        } else {
                            if (useLock) {
                                synchronized (map) {          // make read + write atomic
                                    map.put(id, map.get(id) + 1);
                                }
                            } else {
                                map.merge(id, 1, Integer::sum);   // atomic update
                            }
                        }
                    }
                }
            });
        }

        pool.shutdown();
        try {
            pool.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.out.println("Benchmark interrupted");
        }
        return (System.nanoTime() - start) / 1000000.0;
    }

    static void benchmarkSynchronizedMap() {
        Map<String, Integer> map = Collections.synchronizedMap(new HashMap<String, Integer>());
        fillCatalog(map);
        double ms = runWorkers(map, true);
        report("Collections.synchronizedMap(HashMap)", ms);
    }

    static void benchmarkConcurrentMap() {
        Map<String, Integer> map = new ConcurrentHashMap<String, Integer>();
        fillCatalog(map);
        double ms = runWorkers(map, false);
        report("ConcurrentHashMap", ms);
    }

    static void report(String name, double ms) {
        long totalOps = (long) WORKER_THREADS * OPS_PER_THREAD;
        double opsPerSecond = totalOps / (ms / 1000.0);
        System.out.println(name);
        System.out.println("   Total operations : " + totalOps);
        System.out.println("   Execution time   : " + ms + " ms");
        System.out.println("   Throughput       : " + opsPerSecond + " operations/second");
        System.out.println();
    }

    public static void main(String[] args) {
        // Warm-up so the JVM compiles the code before timing
        benchmarkSynchronizedMap();
        benchmarkConcurrentMap();
        System.out.println("---------- warm-up done, real results below ----------\n");

        benchmarkSynchronizedMap();
        benchmarkConcurrentMap();
    }
}