package MethodsManual;
import java.util.Scanner;
public class Methods1 {
	public static int sumDigits(int n) {
		int sum=0;
		
		while (n>0) {
			
			sum+=n%10;
				n/=10;
			}
		return sum;		

		}
	

	public static void main(String[]args) {
		Scanner input=new Scanner(System.in);
		System.out.println("Enter a number");
		int n=input.nextInt();
		System.out.println("the sum of the digits is "+ sumDigits(n));
	}
}
