package MethodsManual;
import java.util.Scanner;
public class Methods5 {
	public static void main(String[] args) {
		System.out.print("Please enter a number between 0 and 255:");
		Scanner input = new Scanner(System.in);
		int num = input.nextInt();
		System.out.println("The binary value is "+ binary(num));
		}
		public static long binary(int n) {
		int res=0,i=0;
		while (n!=0) {
		int rem = n%2;
		res+=rem*Math.pow(10, i);
		n/=2;
		i++;
		}
		return res;
		}
}