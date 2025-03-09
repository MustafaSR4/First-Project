package ProjectPhase1;

public class DNode {
    private Object element;
    private DNode prev, next;
  


    public DNode(Object element) {
        this(element, null, null);
    }

    public DNode(Object element, DNode prev, DNode next) {
        this.element = element;
        this.prev = prev;
        this.next = next;
    }
    public DNode(DistrictList districtList) {
        this.next = null;
        this.prev = null;
    }

    public Object getElement() {
        return element;
    }

    public void setElement(Object element) {
        this.element = element;
    }

    public DNode getPrev() {
        return prev;
    }

    public void setPrev(DNode prev) {
        this.prev = prev;
    }

    public DNode getNext() {
        return next;
    }

    public void setNext(DNode next) {
        this.next = next;
    }

    
  
    public void setDistrictList(DistrictList districtList) {
        this.element = districtList;
    }

    public District getDistrictList() {
        if (this.element instanceof DistrictList) {
            return (District) this.element;
        }
        return null;
    }
  
    
   
    
   

	@Override
    public String toString() {
        return "Node [element=" + element + ", prev=" + prev + ", next=" + next + "]";
    }
	

	
}
