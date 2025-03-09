package LoopsManual;
import java.util.Scanner;
public class Loops3 {

	public static void main(String[] args) {
		int d;
		int gcd=1;
		 Scanner input = new Scanner(System.in);
		 System.out.println("enter two numbers to get the Gcd");
		 int n1=input.nextInt();
		 int n2=input.nextInt();
		 if (n1>n2)
			 d=n1;
		 else
			 d=n2;
		 for(int i=1;i<=d;i++) {
			 if (n1%i==0&&n2%i==0){
				 gcd=i;
			 }
		 }
		
		 System.out.println(gcd);


	}

}
