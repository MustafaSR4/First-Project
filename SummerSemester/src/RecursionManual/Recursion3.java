package RecursionManual;
import java.util.Scanner;
public class Recursion3 {
		public static double m(double i) {
			if (i==1) return 1/i;
			return (1/i)+m(i=i-1);
		}
		public static void count(double i) {
			if (i!=0) {
			count(i-1);
			System.out.println("when i= "+ i + " the series= "+ m(i));
			}
		}
			
			
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("Please enter the value of i:");
		double i= input.nextDouble();
		count(i);
		}
	}


