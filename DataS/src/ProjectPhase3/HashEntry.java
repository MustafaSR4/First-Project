package ProjectPhase3;

// HashEntry class represents an entry in the hash table
public class HashEntry {
    private String key; // Key for the hash table entry
    private Object value; //Value but in this project i used it as AVL TREE
    private HashEntry next; // Reference to the next entry in case of a collision (separate chaining)
    private char status; // Status of the entry ('F' for full, 'E' for empty, 'D' for deleted)

    public HashEntry(String key, Object value) {
        this.key = key;
        this.value = value;
        this.next = null;
        this.status = 'F'; 
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public HashEntry getNext() {
        return next;
    }

    public void setNext(HashEntry next) {
        this.next = next;
    }

//    public HashEntry getNextInOrder() {
//        return nextInOrder;
//    }
//
//    public void setNextInOrder(HashEntry nextInOrder) {
//        this.nextInOrder = nextInOrder;
//    }
//
//    public HashEntry getPrevInOrder() {
//        return prevInOrder;
//    }
//
//    public void setPrevInOrder(HashEntry prevInOrder) {
//        this.prevInOrder = prevInOrder;
//    }

    public char getStatus() {
        return status;
    }

    public void setStatus(char status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "HashEntry [key=" + key + ", status=" + status + ", value=" + value + "]";
    }
}
