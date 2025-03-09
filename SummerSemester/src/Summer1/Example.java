package Summer1;

public class Example {

	public static void main(String[] args) {
		
		print(5);
	}
		public static int print(int n) {
	        if (n <= 0) {
	            return n;
	        }
	        else {
	     
	        System.out.print(n+ " ");
	        print(n - 1);
	        
	    }
			return n;
}

}

