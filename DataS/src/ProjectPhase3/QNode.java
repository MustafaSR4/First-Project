package ProjectPhase3;

public class QNode {
    public Object element;
    public QNode next; // Reference to the next node in the queue

    public QNode(Object element) {
        this(element, null); 
    }

    public QNode(Object element, QNode next) {
        this.element = element; 
        this.next = next; 
    }

	
	public Object getElement() {
		return element;
	}

	
	public void setElement(Object element) {
		this.element = element;
	}

	
	public QNode getNext() {
		return next;
	}

	
	public void setNext(QNode next) {
		this.next = next;
	}
    
}
