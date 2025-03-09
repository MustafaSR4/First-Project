package Stack;

public class LinkedListStack {
	private int size; // number of elements in the stack
	private Node Front; // pointer to the top node

	public LinkedListStack() {
		// empty stack
		Front = null;
		size = 0;
	}

	public void push(Object element) {

		Node newNode;
		newNode = new Node(element);

		newNode.setNext(Front);
		Front = newNode;

		size++;// update size
	}

	public Object pop() {

		if (!isEmpty()) {
			Node top = Front; // save reference
			Front = Front.getNext();// Remove first node
			size--;
			return top.getElement();// Return the element from the saved ref
		} else
			return null;

	}

	public Object peek() {
		// Return the top element without changing the stack
		if (!isEmpty())
			return Front.getElement();
		else
			return null;
	}

	public int Size() {
		return size;
	}

	public boolean isEmpty() {
		return (Front == null); // return true if the stack is empty
	}

	public int max() {
		LinkedListStack tempStack = new LinkedListStack();
		
		int maxValue = (int) pop();
		tempStack.push(maxValue);
		
		while(!isEmpty()) {
			int nextValue = (int) pop();
			maxValue = Math.max(maxValue, nextValue);
			tempStack.push(nextValue);
		}
		
		while(!tempStack.isEmpty()) {
			push(tempStack.pop());
		}
			
		return maxValue;
	}

	public int min() {
		LinkedListStack tempStack = new LinkedListStack();
		
		int maxValue = (int) pop();
		tempStack.push(maxValue);
		
		while(!isEmpty()) {
			int nextValue = (int) pop();
			maxValue = Math.min(maxValue, nextValue);
			tempStack.push(nextValue);
		}
		
		while(!tempStack.isEmpty()) {
			push(tempStack.pop());
		}
			
		return maxValue;
	}
	public static void print(LinkedListStack s) {

			while (!s.isEmpty()) {
				System.out.println(s.pop());
			}
	}
	
	


}