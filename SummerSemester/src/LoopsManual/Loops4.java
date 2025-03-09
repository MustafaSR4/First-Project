package LoopsManual;
import java.util.Scanner;
public class Loops4 {

	public static void main(String[] args) {
		Scanner input= new Scanner(System.in);
		 System.out.println("enter a number to know the value of pie");

		int n= input.nextInt();
		double res=0;
		for (int i=1; i<=n; i++) {
		res+=Math.pow(-1, i+1)/(2*i-1);
		}
		res*=4;
		System.out.println(res);
	}

}
