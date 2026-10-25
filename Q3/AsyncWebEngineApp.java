// Main
public class AsyncWebEngineApp {
    public static void main(String[] args) {
        System.out.println("Default JVM thread name: " + Thread.currentThread().getName());

        TelemetryThread telemetry = new TelemetryThread();
        Thread logThread = new Thread(new LogProcessorTask());

        telemetry.setName("Telemetry-Worker");
        logThread.setName("Log-Worker");

        System.out.println("State before start -> " + telemetry.getName() + ": " + telemetry.getState()
                + " | " + logThread.getName() + ": " + logThread.getState());

        telemetry.start();
        logThread.start();

        System.out.println("State after start  -> " + telemetry.getName() + ": " + telemetry.getState()
                + " | " + logThread.getName() + ": " + logThread.getState());
        System.out.println("Alive -> " + telemetry.getName() + ": " + telemetry.isAlive()
                + " | " + logThread.getName() + ": " + logThread.isAlive());

        try {
            telemetry.join();
            logThread.join();
        } catch (InterruptedException e) {
            System.out.println("Main interrupted");
        }

        System.out.println("State at end       -> " + telemetry.getName() + ": " + telemetry.getState()
                + " | " + logThread.getName() + ": " + logThread.getState());
    }
}