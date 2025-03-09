package Summer1;
import java.util.Scanner;
public class Methods1 {
public static void main(String[] args) {
		printPrimeNumbers(5,50);

	}

		public static boolean isPrime(int n) {
			for (int i=2;i<=n;i++) 
				if (n%i==0) 
					return false;
				
			
		return true;
		}
			public static void printPrimeNumbers(int start,int end) {
				for (int i=start;i<=end;i++) {
					if (isPrime(i))
						System.out.println(i+" ");
				}
			}
	
		}

