package Summer1;
import java.util.Scanner;
public class ConvertCurruncies {
	    public static void main(String[] args) {
	        Scanner input = new Scanner(System.in);

	        System.out.println("1. Convert Dollar to Euro");
	        System.out.println("2. Convert Euro to Dollar");
	        System.out.println("3. Convert Dollar to Shekel");
	        System.out.println("4. Convert Shekel to Dollar");
	        System.out.print("Enter your choice: ");
	        
	        int choice = input.nextInt();
	        
	        double amount;
	        double result;
	        
	        System.out.print("Enter amount: ");
	        amount = input.nextDouble();
	        
	        switch (choice) {
	            case 1:
	                result = amount * 0.85; // Dollar to Euro conversion rate
	                System.out.println("Equivalent amount in Euros: " + result); break;
	                
	            case 2:
	                result = amount * 1.18; // Euro to Dollar conversion rate
	                System.out.println("Equivalent amount in Dollars: " + result); break;
	                
	            case 3:
	                result = amount * 3.24; // Dollar to Shekel conversion rate
	                System.out.println("Equivalent amount in Shekels: " + result); break;
	                
	            case 4:
	                result = amount * 0.31; // Shekel to Dollar conversion rate
	                System.out.println("Equivalent amount in Dollars: " + result); break;
	                
	            default:
	                System.out.println("Invalid choice"); break;
	        }
	        
	   
	    }
	}

