public class FIXOrderEvent extends TradeEvent {
    private String clOrdId;
    private int quantity;

    public FIXOrderEvent(String senderId, long timestamp, double basePrice,
                         String clOrdId, int quantity) {
        super(senderId, timestamp, basePrice);
        this.clOrdId = clOrdId;
        this.quantity = quantity;
    }

    public void dispatch() {
        double cost = basePrice * quantity;
        double fee = cost * 0.0015;          // 0.15% exchange fee
        double total = cost + fee;

        System.out.println("[FIX Order] ClOrdID=" + clOrdId + " | sender=" + senderId
                + " | time=" + timestamp);
        System.out.println("   BUY " + quantity + " shares @ " + basePrice);
        System.out.printf("   Cost = %.2f | Fee (0.15%%) = %.2f | Total = %.2f%n", cost, fee, total);
        System.out.println("   Order executed.");
    }
}