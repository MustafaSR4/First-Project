package Queues;

public class QNode {
    public Object element;
    public QNode next;

    public QNode(Object element) {
        this(element, null);
    }

    public QNode(Object element, QNode next) {
        this.element = element;
        this.next = next;
    }
}




