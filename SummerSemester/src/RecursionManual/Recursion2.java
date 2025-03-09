package RecursionManual;
import java.util.Scanner;
public class Recursion2 {
	public static int gcd(int num1,int num2) {
		if (num1%num2==0)
			return num2;
		else
			return gcd(num2,num1%num2);
	}
	public static void main(String[] args) {
		Scanner input =new Scanner(System.in);
		System.out.println("Please enter two numbers");
		int num1 =input.nextInt();
		int num2=input.nextInt();
		System.out.println("gcd is "+ gcd(num1,num2));


	}

}
