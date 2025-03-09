package Queues;

public class LinkedListQueue {
    private QNode front, rear;

    public LinkedListQueue() {
        front = rear = null;
    }

    // Method to add an element to the queue
    public void enqueue(Object element) {
    	QNode newNode = new QNode(element);
        if (rear == null) {
            // If the queue is empty, both front and rear will point to the new node
            front = rear = newNode;
        } else {
            // Add the new node at the end of the queue and update rear
            rear.next = newNode;
            rear = newNode;
        }
    }

    // Method to remove an element from the queue
    public Object dequeue() {
        if (front == null) {
            throw new IllegalStateException("Queue is empty");
        }

        Object element = front.element;
        front = front.next;

        // If the queue becomes empty, make rear also null
        if (front == null) {
            rear = null;
        }

        return element;
    }

    // Method to get the element at the front of the queue
    public Object front() {
        if (front == null) {
            throw new IllegalStateException("Queue is empty");
        }
        return front.element;
    }

    // Method to check if the queue is empty
    public boolean isEmpty() {
        return front == null;
    }

    // Main method to demonstrate queue operations
    public static void main(String[] args) {
        LinkedListQueue queue = new LinkedListQueue();
        queue.enqueue("Hello");
        queue.enqueue("World");
        queue.enqueue("!");

        System.out.println("Front element is: " + queue.front());
        System.out.println("Removed element is: " + queue.dequeue());
        System.out.println("Front element is now: " + queue.front());
        System.out.println("Is the queue empty? " + queue.isEmpty());
        queue.dequeue();
        System.out.println("Is the queue empty after all dequeues? " + queue.isEmpty());
    }
}