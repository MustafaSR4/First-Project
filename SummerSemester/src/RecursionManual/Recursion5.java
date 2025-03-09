package RecursionManual;

import java.util.Scanner;

public class Recursion5 {

	public static long sumDigits(long n) {

		if (n <=9) {
			return n;
		}
		else	{
			
			return n%10+sumDigits(n/10);
		}
	
}

	public static void main(String[] args) {
	
			Scanner input = new Scanner(System.in);
			System.out.print("Please enter a number:");
			int n = input.nextInt();
			System.out.println("the sum is "+ 	sumDigits(n));
			
	
	}
}


