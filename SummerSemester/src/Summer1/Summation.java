package Summer1;
import java.util.Scanner;
public class Summation {

	public static void main(String[] args) {
		int digit;
		int sum=0;
		int number;
		Scanner input=new Scanner(System.in);
		System.out.println("please enter an ink");
		number=input.nextInt();
		while(number > 0)  {  
		digit = number % 10;  
		sum = sum + digit;  
		number = number / 10;  
		}  
		System.out.println("the sum the digits is "+sum);
		}

	}


