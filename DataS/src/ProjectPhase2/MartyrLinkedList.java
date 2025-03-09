package ProjectPhase2;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class MartyrLinkedList {
    DateNode root;
    MartyrNode head;

    

        // Method to insert a martyr in a sorted order (example by age)
        public void insertSorted(MartyrPH2 martyr) {
            MartyrNode newNode = new MartyrNode(martyr);
            if (head == null || head.getMartyr().getAge() >= martyr.getAge()) {
                newNode.setNext(head);
                head = newNode;
            } else {
                MartyrNode current = head;
                while (current.getNext() != null && current.getNext().getMartyr().getAge() < martyr.getAge()) {
                    current = current.getNext();
                }
                newNode.setNext(current.getNext());
                current.setNext(newNode);
            }
        }

        // Optionally, a method to print all martyrs in the list
        public void printMartyrs(StringBuilder path) {
            MartyrNode temp = head;
            while (temp != null) {
                path.append("Martyr: ").append(temp.getMartyr().getName())
                    .append(", Age: ").append(temp.getMartyr().getAge())
                    .append("; ");
                temp = temp.getNext();
            }
        }
    

   
   

   
	

	
    public void insertMartyr(MartyrPH2 martyr) {
        MartyrNode newNode = new MartyrNode(martyr);
        if (head == null) {
            head = newNode;
        } else {
            MartyrNode current = head;
            MartyrNode previous = null;
            while (current != null && (martyr.getAge() > current.getMartyr().getAge() || 
                    (martyr.getAge() == current.getMartyr().getAge() && martyr.getGender() >= current.getMartyr().getGender()))) {
                previous = current;
                current = current.getNext();
            }
            if (previous == null) {
                newNode.setNext(head);
                head = newNode;
            } else {
                previous.setNext(newNode);
                newNode.setNext(current);
            }
        }
    }

    public boolean insertSorted(MartyrPH2 martyr, StringBuilder path) {
        MartyrNode newNode = new MartyrNode(martyr);
        // Check if the list is empty or if the new node should be placed before the head
        if (head == null || compare(head.martyr, martyr) > 0) {
            newNode.next = head;
            head = newNode;
        } else {
            // Find the correct position for the new node
            MartyrNode current = head;
            while (current.next != null && compare(current.next.martyr, martyr) <= 0) {
                current = current.next;
            }
            // Insert the new node
            newNode.next = current.next;
            current.next = newNode;
        }
		return false;
    }

    // Helper method to compare two martyrs first by age, then by gender
    private int compare(MartyrPH2 m1, MartyrPH2 m2) {
        if (m1.getAge() != m2.getAge()) {
            return Integer.compare(m1.getAge(), m2.getAge());
        }
        // Compare gender, assuming gender is stored as char (M/F); adjust if using another data type
        return Character.compare(m1.getGender(), m2.getGender());
    }

	
	

	public MartyrNode getHead() {
		return head;
	}

	public void setHead(MartyrNode head) {
		this.head = head;
	}

	public DateNode getRoot() {
		return root;
	}

	public void setRoot(DateNode root) {
		this.root = root;
	}

	

	

	

	
}