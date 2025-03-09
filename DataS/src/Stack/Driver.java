package Stack;

public class Driver {
	public static void main(String[]args) {
		LinkedListStack s =new LinkedListStack();
		
		s.push(5);
		s.push(8);

		s.push(9);

		s.push(10);

		s.print(s);
	}
}
