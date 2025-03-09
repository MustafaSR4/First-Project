package MethodsManual;
import java.util.Scanner;
public class Methods3 {
	public static boolean isPrime(int number) {
		while (number<10000) {
			if (number%2==0||number%3==0)
				return false;
			else 
				return true;
		}
		return false;
	}
	public static void main(String[] args) {
		 Scanner input = new Scanner(System.in);
	        System.out.println("enter a number to check wiether its prime or not");
	        int number = input.nextInt();
	        if (isPrime(number)==true)
	        System.out.println(number+" is prime");
	        else
	        	 System.out.println(number+" is not prime");
	}

}
