package Summer1;
import java.util.Scanner;
public class PrimeNumbers {
	
		public static void main(String[] args) {
			 Scanner input=new Scanner(System.in);
			  int num;
			  int counter = 0;
			  System.out.println("please enter an integer to check if its prime or not");
			  num=input.nextInt();
			  	for (int i=2;i<=num;i++) {
			  		if(num%i==0) {
					  counter++;
			  		}
			  	}
			  	if (counter==1) {
			  		System.out.println(num +" is a prime number");
			  	}
			  	else {
			  		System.out.println(num +" is not a prime number");
			  	}
				
			  
				
		}
}


