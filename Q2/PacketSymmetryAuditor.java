public class PacketSymmetryAuditor {

    static boolean audit(String payload) {
        TransactionStack stack = new TransactionStack(payload.length());
        RequestQueue queue = new RequestQueue(payload.length());

        // push and enqueue at the same time
        for (int i = 0; i < payload.length(); i++) {
            char c = payload.charAt(i);
            stack.push(c);
            queue.enqueue(c);
        }

        // pop and dequeue together then compare
        boolean symmetric = true;
        int step = 1;
        while (!stack.isEmpty() && !queue.isEmpty()) {
            char fromStack = stack.pop();
            char fromQueue = queue.dequeue();
            boolean match = (fromStack == fromQueue);
            System.out.println("  Step " + step++ + ": stack='" + fromStack
                    + "' queue='" + fromQueue + "' -> " + (match ? "MATCH" : "MISMATCH"));
            if (!match) {
                symmetric = false;
            }
        }
        return symmetric;
    }

    public static void main(String[] args) {
        String[] packets = {"abcba", "X7Y7X", "packet"};

        for (int i = 0; i < packets.length; i++) {
            System.out.println("Packet payload: " + packets[i]);
            boolean result = audit(packets[i]);
            System.out.println("Verified log: " + (result
                    ? "SYMMETRIC - bi-directional signature verified"
                    : "NOT SYMMETRIC - signature check failed"));
            System.out.println();
        }
    }
}