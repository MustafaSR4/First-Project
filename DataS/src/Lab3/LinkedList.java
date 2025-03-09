package Lab3;

public class LinkedList {
	private Node Front, Back;
	private int Size;

	public LinkedList() {
		Front = Back = null;
		Size = 0;
	}

	public Node getFront() {
		return Front;
	}

	public void setFront(Node front) {
		Front = front;
	}

	public Node getBack() {
		return Back;
	}

	public void setBack(Node back) {
		Back = back;
	}

	public int getSize() {
		return Size;
	}

	public void setSize(int size) {
		Size = size;
	}

	/* Methods go here */
	public void addFirst(Object element) {
		Node newNode;
		newNode = new Node(element);
		if (Size == 0) {// Empty List
			Front = Back = newNode;
		} else {
			newNode.setNext(Front);
			Front = newNode;
		}
		Size++;// update Size
	}

	public Object getFirst() {
		if (Size == 0)
			return null;
		else
			return Front.getElement();
	}

	public void addLast(Object element) {
		Node newNode;
		newNode = new Node(element);
		if (Size == 0) {// Empty List
			Front = Back = newNode;
		} else {
			Back.setNext(newNode);
			Back = newNode; // Or Back=Back.next;
		}
		Size++;// update Size
	}

	public Object getLast() {
		if (Size == 0)
			return null;
		else
			return Back.getElement();
	}

	public Object get(int index) {
		if (Size == 0)
			return null; // empty list
		else if (index == 0)
			return getFirst(); // first element
		else if (index == Size - 1)
			return getLast(); // last element
		else if (index > 0 && index < Size - 1) {
			Node current = Front;
			for (int i = 0; i < index; i++)
				current = current.getNext();
			return current.getElement();
		} else
			return null; // out of boundary
	}

	public int size() {
		return Size;
	}

	public void add(int index, Object element) {
		if (index == 0)
			addFirst(element);
		else if (index >= size())
			addLast(element);
		else {
			Node newNode = new Node(element);
			Node current = Front; // used to advance to proper position
			for (int i = 0; i < index - 1; i++)
				current = current.getNext();
			newNode.setNext(current.getNext());
			current.setNext(newNode);
			Size++;// update size
		}
	}

	public void add(Object element) {
		add(size(), element);
	}

	public boolean removeFirst() {
		if (Size == 0)
			return false; // empty list
		else if (Size == 1) // one element inside list
			Front = Back = null;
		else
			Front = Front.getNext();
		Size--; // update size
		return true;
	}

	public boolean removeLast() {
		if (Size == 0)
			return false; // empty list
		else if (Size == 1) // one element inside the list
			Front = Back = null;
		else {
			Node current = Front;
			for (int i = 0; i < Size - 2; i++)
				current = current.getNext();
			current.setNext(null);
			Back = current;
		}
		Size--; // update size
		return true;
	}

	public boolean remove(int index) {
		if (Size == 0)
			return false;// empty linked list
		else if (index == 0)
			return removeFirst(); // remove first element
		else if (index == getSize() - 1)
			return removeLast();// remove last element
		else if (index > 0 && index < Size - 1) {
			Node current = Front;
			for (int i = 0; i < index - 1; i++)
				current = current.getNext();
			current.setNext(current.getNext().getNext());
			Size--;
			return true;
		} else
			return false; // out of boundary(invalid index)
	}

	public void print(Node current) {
		if (current != null) {
			System.out.println(current.getElement());
			print(current.getNext());
		}
	}
	public void printLoop(Node current) {
		current=Front;
		for(int i=0;i<Size;i++) {
			System.out.println(current.getElement());
			current=current.getNext();
		}
		
	}
}