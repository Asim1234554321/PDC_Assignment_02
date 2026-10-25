public class TelemetryThread extends Thread {
    public void run() {
        Runtime rt = Runtime.getRuntime();
        for (int i = 1; i <= 5; i++) {
            long usedMb = (rt.totalMemory() - rt.freeMemory()) / (1024 * 1024);
            System.out.println("[TelemetryThread] Health update " + i
                    + " | CPU cores: " + rt.availableProcessors()
                    + " | Memory used: " + usedMb + " MB"
                    + " | Thread: " + Thread.currentThread().getName());
            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                System.out.println("Telemetry thread interrupted");
            }
        }
    }
}