package LoopsManual;
import java.util.Scanner;

public class Loops6 {

	public static void main(String[] args) {
		Scanner input= new Scanner(System.in);
		double n=input.nextDouble();
		int res=0;
			for (int i=2;i<n;i++) {
				for (int j=1;j<i;j++) {
					if (i%j==0) {
						res+=j;
					}
				}
					if (res==i)
						System.out.println(i+" is perfect number");
						res=0;
	}
	}
}



