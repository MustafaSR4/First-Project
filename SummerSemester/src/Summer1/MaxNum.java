package Summer1;

import java.util.Scanner;

public class MaxNum {
	public static int findMax(int num1,int num2, int num3) {
		
		return Math.max(num1, Math.max(num2, num3));
	}
	
	public static void main(String[] args) {
		int num1;
		int num2;
		int num3;

		
		Scanner input= new Scanner (System.in);
		System.out.println("Please enter the first number");
		num1 =input.nextInt();
		System.out.println("Please enter the second number");
		num2 =input.nextInt();
		System.out.println("Please enter the third number");
		num3 =input.nextInt();
		findMax( num1, num2,  num3);
		System.out.println("the biggest number is "+ findMax( num1, num2,  num3));
	}

}
