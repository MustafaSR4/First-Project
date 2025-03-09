package MethodsManual;
import java.util.Scanner;
public class Methods2 {
	static int rev;
	public static void reverse(int number) {
		
        if(number == 0) 
        	return;
        else {
        int rem = number % 10;
        rev = rev * 10 + rem;
        reverse(number/10);
        }
    }
    
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("enter a number to reverse their digits");
        int number = input.nextInt();
         rev = 0;
        reverse(number);
        System.out.println(rev);
    }
}


