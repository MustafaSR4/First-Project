package Summer1;
import java.util.Scanner;
public class MethodsEx {
	public static int average(int num1,int num2,int num3) {
		return (num1+num2+num3/3);
	}
	
	
	
	public static int findMax(int num1,int num2,int num3) {
	
		if(num1>num2&&num1>num3) {
			
			return num1;
		}
			else if (num2 >1&&num2 >num3) {
				return num2;
			 }
			else if (num3 >num1&&num3 >num2) {
				return num3;
		    }
		return num1;
	}
	
	
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System .out .println("Enter 3 numbers");
		int num1 = input.nextInt();
		int num2 = input.nextInt();
		int num3 = input.nextInt();
		System.out.println("the largest integer is "+ findMax(num1,num2,num3));
		System.out.println("the average is "+ average(num1,num2,num3));
		}
	

		

	}


