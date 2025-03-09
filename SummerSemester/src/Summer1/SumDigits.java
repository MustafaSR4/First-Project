package Summer1;
import java.util.Scanner;
public class SumDigits {

	public static void main(String[] args) {
		 Scanner input=new Scanner(System.in);
		 System.out.println("Enter an integer to sum their digits ");
		 int num=input.nextInt();
		 int sum=0;
		 while (num>0) {
			 sum+=num%10;
			 num=num/10;
		 
		

		
	}
		 System.out.println(sum);
}
}