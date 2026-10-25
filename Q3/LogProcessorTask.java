// Task using the Runnable interface
public class LogProcessorTask implements Runnable {
    public void run() {
        for (int i = 1; i <= 5; i++) {
            System.out.println("[LogProcessorTask] Log metric iteration " + i
                    + " | Thread: " + Thread.currentThread().getName());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Log processor interrupted");
            }
        }
    }
}