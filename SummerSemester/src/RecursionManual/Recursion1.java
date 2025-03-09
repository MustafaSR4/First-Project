package RecursionManual;
import java.util.Scanner;
public class Recursion1{
	public static int fact(int num) {
		if (num==0)
			return 1;
		else 
			return num*fact(num-1);
	}
		public static void main(String[] args) {	
		
				Scanner input =new Scanner(System.in);
				System.out.println("Please enter a number");
				int num =input.nextInt();
				System.out.println("the factorial is "+ fact(num));


		}
	

}


