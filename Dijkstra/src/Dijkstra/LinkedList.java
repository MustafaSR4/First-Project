package Dijkstra;

public class LinkedList {

    private LinkedListNode first;
    private int size; // Keep track of the number of elements

    public LinkedList() {
        first = null;
        size = 0;
    }

    public LinkedListNode getFirstNode() {
        return first;
    }

    public void setFirstNode(LinkedListNode first) {
        this.first = first;
    }

    public void addFirst(Edge edge) { // Add at the start
        LinkedListNode temp = new LinkedListNode(edge);
        temp.setNext(first);
        first = temp;
        size++;
    }

    public void addLast(Edge edge) { // Add at the end
        LinkedListNode temp = new LinkedListNode(edge);
        if (first == null) {
            first = temp;
        } else {
            LinkedListNode current = first;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(temp);
        }
        size++;
    }

    public boolean deleteFirst() { // Remove first
        if (first == null) {
            return false;
        }
        first = first.getNext();
        size--;
        return true;
    }

    public boolean deleteLast() { // Remove last
        if (first == null) {
            return false;
        }
        if (first.getNext() == null) {
            first = null;
        } else {
            LinkedListNode current = first;
            while (current.getNext().getNext() != null) {
                current = current.getNext();
            }
            current.setNext(null);
        }
        size--;
        return true;
    }

    public Edge get(String name) { // Get by name
        LinkedListNode current = first;
        while (current != null) {
            if (current.getEdge().getDestination().getCapital().getCapitalName().equals(name)) {
                return current.getEdge();
            }
            current = current.getNext();
        }
        return null; // Not found
    }

    public Edge getFirst() {
        return first != null ? first.getEdge() : null;
    }

    public Edge getLast() {
        LinkedListNode current = first;
        while (current != null && current.getNext() != null) {
            current = current.getNext();
        }
        return current != null ? current.getEdge() : null;
    }

    public void printList() {
        LinkedListNode current = first;
        while (current != null) {
            System.out.println(current.toString());
            current = current.getNext();
        }
    }

    public int getSize() {
        return size;
    }

    public void removeSelfLoops() { // Remove edges where source equals destination
        LinkedListNode current = first;
        LinkedListNode previous = null;

        while (current != null) {
            Edge edge = current.getEdge();
            if (edge.getSource().equals(edge.getDestination())) {
                if (previous == null) {
                    first = current.getNext(); // Remove head
                } else {
                    previous.setNext(current.getNext());
                }
                size--;
            } else {
                previous = current;
            }
            current = current.getNext();
        }
    }

    public void addAll(LinkedList list) { // Add all elements from another list
        LinkedListNode current = list.getFirstNode();
        while (current != null) {
            this.addLast(current.getEdge());
            current = current.getNext();
        }
    }
}
