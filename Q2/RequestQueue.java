// queue
public class RequestQueue {
    private char[] elements;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    public RequestQueue(int capacity) {
        this.capacity = capacity;
        elements = new char[capacity];
        front = 0;
        rear = 0;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(char c) {
        if (size == capacity) {
            System.out.println("Queue is full");
            return;
        }
        elements[rear] = c;
        rear = (rear + 1) % capacity;
        size++;
    }

    public char dequeue() {
        if (isEmpty()) {
            System.out.println("Queue is empty");
            return '\0';
        }
        char c = elements[front];
        elements[front] = '\0';
        front = (front + 1) % capacity;
        size--;
        return c;
    }
}