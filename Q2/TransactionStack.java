// Transaction Stack
public class TransactionStack {
    private char[] elements;
    private int top;
    private int capacity;

    public TransactionStack(int capacity) {
        this.capacity = capacity;
        elements = new char[capacity];
        top = -1;
    }

    public boolean isEmpty() {
        return top == -1;
    }

    public void push(char c) {
        if (top == capacity - 1) {
            System.out.println("Stack is full");
            return;
        }
        elements[++top] = c;
    }

    public char pop() {
        if (isEmpty()) {
            System.out.println("Stack is empty");
            return '\0';
        }
        char c = elements[top];
        elements[top--] = '\0';
        return c;
    }

    public char peek() {
        if (isEmpty()) {
            System.out.println("Stack is empty");
            return '\0';
        }
        return elements[top];
    }
}