package Summer1;
import java.util.Scanner;
public class LoopsEx {

	public static void main (String[] args) {
		 Scanner input=new Scanner(System.in);
		 System.out.println("Enter random numbers");
		int num=input.nextInt();
		 int max=num;
		 int min=num;
		while (true) {
		 num=input.nextInt();
		if (num<0)
			break;
		 if (num>max) 
			 max=num;
			 else 
				 min=num;
			 
		 }
		 
		System.out.println("max is "+max);
		System.out.println("min is "+min);

		 
}	
	}

			
		
			

		
	


