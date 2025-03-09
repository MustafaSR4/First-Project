package ProjectPhase3;
//this class is used for printing the AVL Tree level by level
public class LinkedListQueue {
    private QNode front, rear;
    private int size;

    public LinkedListQueue() {
        front = rear = null;
        size = 0;
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
        size++;
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
        size--;
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

    // Method to get the size of the queue
    public int size() {
        return size;
    }
    @Override
	public String toString() {
		return "LinkedListQueue [front=" + front + ", rear=" + rear + ", size=" + size + "]";
	}

   
}
