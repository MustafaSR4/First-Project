package CursorLab;
public class Cursor {
	private CNode[] cursorArray;
	private final static int MAX_SIZE = 10;
	
	/*
	 * max array size of cursor
	 */public void initialization() {
		cursorArray = new CNode[MAX_SIZE];
		for (int i = 0; i < MAX_SIZE; i++)
			cursorArray[i] = new CNode(null, i + 1);
		cursorArray[MAX_SIZE - 1].setCursorNext(0);
	}

	public int cursorAlloc() {
		int p = cursorArray[0].getCursorNext();
		cursorArray[0].setCursorNext(cursorArray[p].getCursorNext());
		return p;// return the index of the available node (most likely empty node)
	}

	public int createList() {
		/* create new empty list */
		int l = cursorAlloc();
		if (l == 0)
			System.out.println("Error:out of space");
		else
			cursorArray[l] = new CNode("-", 0);// Empty Linked List
		return l; /* Head of the list */
	}

	private void cursorFree(int p) {
		cursorArray[p].setElement(null); // free the content
		cursorArray[p].setCursorNext(cursorArray[0].getCursorNext());
		cursorArray[0].setCursorNext(p);
	}

	public boolean isNull(int l) {
		/* return true if the list not created */
		return cursorArray[l] == null;
	}

	public boolean isEmpty(int l) {
		// return true if the list is empty
		return cursorArray[l].getCursorNext() == 0;
	}

	public boolean isLast(int p) {
		// check if the node p is last or not
		return cursorArray[p].getCursorNext() == 0;
	}

	public void insertAtHead(Object data, int l) {
		if (isNull(l)) // list not created
			return;
		int p = cursorAlloc();
		if (p != 0) {
			cursorArray[p] = new CNode(data, cursorArray[l].getCursorNext());
			cursorArray[l].setCursorNext(p);
		} else
			System.out.println("Out Of Space");
	}

	public int find(Object data, int l) {
		int p = cursorArray[l].getCursorNext();
		while ((p != 0) && !cursorArray[p].getElement().equals(data))
			p = cursorArray[p].getCursorNext();
		return p;
	}

	public void remove(Object data, int l) {
		int pos = findPrevious(data, l);// Implementation left as
		// an exercise
		if (cursorArray[pos].getCursorNext() != 0) {// !isLast (pos)
			int tmp = cursorArray[pos].getCursorNext();
			cursorArray[pos].setCursorNext(cursorArray[tmp].getCursorNext());
			cursorFree(tmp);
		}
	}
	public void insert(Object data, int l, int p) {
		if (isNull(l)) { // list not created
			System.out.println("List not created");
			return;
		}

		if (p <= 0 || p >= cursorArray.length) {
			System.out.println("Invalid position or Out Of Space");
			return;
		}

		if (cursorArray[p] != null) { // Position already occupied
			System.out.println("Position already occupied");
			return;
		}

		// If the list is empty or inserting at the beginning
		if (isEmpty(l) || cursorArray[l].getCursorNext() == 0) {
			cursorArray[p] = new CNode(data, cursorArray[l].getCursorNext()); // New node points to where the head pointed
			cursorArray[l].setCursorNext(p); // Head points to the new node
		} else {
			// If inserting at some other position
			int currentPos = cursorArray[l].getCursorNext();
			int prevPos = l;

			while (currentPos != 0 && --p > 0) {
				prevPos = currentPos;
				currentPos = cursorArray[currentPos].getCursorNext();
			}

			if (p > 0) {
				System.out.println("Position exceeds list length");
				return;
			}

			cursorArray[p] = new CNode(data, cursorArray[prevPos].getCursorNext()); // New node points to the next node
			cursorArray[prevPos].setCursorNext(p); // Previous node points to the new node
		}
	}

	public void traversList(int l) {
		System.out.print("list_" + l + "-->");
		while (!isNull(l) && !isEmpty(l)) {
			l = cursorArray[l].getCursorNext();
			System.out.print(cursorArray[l] + "-->");
		}
		System.out.println("null");
	}

	

	public int findPrevious(Object data, int l) {
		while (!isNull(l) && !isEmpty(l)) {
			if (cursorArray[cursorArray[l].getCursorNext()].getElement().equals(data))
				return l;
			l = cursorArray[l].getCursorNext();
		}
		return -1; // not found
		/*
	      return find(element, l - 1);

		 */

	}
}
