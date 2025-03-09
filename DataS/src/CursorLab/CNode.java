package CursorLab;

public class CNode {
	private Object element;
	private int cursorNext;

	public CNode(Object element) {
		this(element, 0);

	}

	public CNode(Object element, int next) {
		this.element = element;
		this.cursorNext = next;
	}

	public Object getElement() {
		return element;
	}

	public void setElement(Object element) {
		this.element = element;
	}

	public int getCursorNext() {
		return cursorNext;
	}

	public void setCursorNext(int cursorNext) {
		this.cursorNext = cursorNext;
	}

	@Override
	public String toString() {
		return "Node [element=" + element + ", next=" + cursorNext + "]";
	}
}