package Summer1;
import java.util.InputMismatchException;
import java.lang.ArithmeticException;
import java.util.Scanner;
public class ExceptionTest {

	public static void main(String[] args) {
			Scanner input = new Scanner(System.in);
		
			System.out.println("Enter two integers");
			
			try {
					int num1=input.nextInt();
					int num2=input.nextInt();
				
					System.out.println(num1+ " "+ num2+" "+num1/num2);
			}catch(InputMismatchException ex) {
					System.out.println("Incorrect input "+ ex.toString());
			}
				catch(ArithmeticException ex) {
					System.out.println("Incorrect num2's input "+ex);
				}
			System.out.println("Bye");
			
	}

}
