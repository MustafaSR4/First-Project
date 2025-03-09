package ProjectPhase1;

public class SNode {
    private Object element;
    private SNode next;
    

  

	public SNode(String locationName) {
        this.element = locationName; // Assuming element holds the location name
        this.next = null;
        
    }

    public SNode(Object element) {
        this(element, null);
    }

    public SNode(Object element, SNode next) {
        this.element = element;
        this.next = next;
        
    }
   

    public Object getElement() {
        return element;
    }

    public void setElement(Object element) {
        this.element = element;
    }

    public SNode getNext() {
        return next;
    }

    public void setNext(SNode next) {
        this.next = next;
    }

   

   

   
	@Override
    public String toString() {
        return "SNode{" +
                "element=" + element +
                '}';
    }

	

}
