public class ReutersQuoteEvent extends TradeEvent {
    private double bidPrice;
    private double askPrice;

    public ReutersQuoteEvent(String senderId, long timestamp, double basePrice,
                             double bidPrice, double askPrice) {
        super(senderId, timestamp, basePrice);
        this.bidPrice = bidPrice;
        this.askPrice = askPrice;
    }

    public void dispatch() {
        double spread = askPrice - bidPrice;
        double mid = (askPrice + bidPrice) / 2;
        double spreadPercent = (spread / mid) * 100;

        System.out.println("[Reuters Quote] sender=" + senderId + " | time=" + timestamp);
        System.out.println("   Bid = " + bidPrice + " | Ask = " + askPrice);
        System.out.printf("   Spread = %.4f | Mid = %.4f | Spread%% = %.4f%n", spread, mid, spreadPercent);
    }
}