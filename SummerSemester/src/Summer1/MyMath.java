package Summer1;
import java.util.Scanner;
public class MyMath{
	public static long factorial (long number){

	    long res=1;
	    for (int i=2;i<=number;i++)
	       res*=i;
	    return res;

	  }
	 public static boolean isPrime(long number) {
		 boolean isPrime = true;
		 
		 if(number <= 1)  {
		 isPrime = false;
		  
		 return isPrime;
		 }
		 else{
		 for (int i = 2; i<= number/2; i++) 
		 {
		 if ((number % i) == 0){
		 isPrime = false;
		  
		 break;
		 }
		 }
		  
		 return isPrime;
		 }
	 }
	 public static boolean isPerfect (long number){
	   int sum=0;
	   int counter=1;
	   while (counter < number){
	     if (number % counter == 0)
	       sum+=counter;
	     counter++;
	   }
	  
	   if (sum==number)
	     return true;
	   
	    return false;

	 }

	  public static void main (String [] args){
	   
	     int number;
	     Scanner input= new Scanner (System.in);
	     System.out.print("Please enter a number: ");
	     number=input.nextInt();
	     System.out.println (isPerfect (number)==true? number+" is perfect": number + " is not perfect"); 
	     System.out.println("Factorial of " + number + " is " + factorial(number)); 
	     System.out.println(isPrime(number)==true?number + " is prime": number+ " is not prime");
	      
	 }


	}
	


