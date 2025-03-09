package Lab3;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Driver {

	public static void main(String[] args) {
		Node front = new Node(null);
		Node m = new Node(new Integer(3));
		Node f = new Node(new Integer(4));
		Node v = new Node(new Integer(5));
		front.setNext(m);
		m.setNext(f);
		f.setNext(v);


		LinkedList list = new LinkedList();
		list.addFirst(m);
		list.add(f);
		list.addLast(v);
		
		


		

			
		 
		
		list.print(list.getFront());
		System.out.println();
		list.printLoop(list.getFront());
	}
}