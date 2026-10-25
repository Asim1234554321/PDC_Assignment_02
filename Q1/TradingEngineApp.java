// shows runtime polymorphism
public class TradingEngineApp {
    public static void main(String[] args) {
        long now = System.currentTimeMillis();

        // Superclass reference variables holding subclass objects
        TradeEvent e1 = new FIXOrderEvent("FIX-GATEWAY-1", now, 150.25, "ORD-1001", 200);
        TradeEvent e2 = new ReutersQuoteEvent("REUTERS-FEED", now + 5, 150.30, 150.20, 150.35);
        TradeEvent e3 = new TradeEvent("SYSTEM", now + 10, 150.00);

        System.out.println("=== Dispatching trade events ===\n");
        e1.dispatch();
        System.out.println();
        e2.dispatch();
        System.out.println();
        e3.dispatch();
    }
}