package Summer1;

public class PrintNumUp {
	
	
	public static void fact(int n) {
		 if (n > 0) {
	          
	            fact(n - 1);
	            System.out.println(n);	
	        }
	    }
	
	public static void main(String[] args) {
	fact (6);

	}

}
