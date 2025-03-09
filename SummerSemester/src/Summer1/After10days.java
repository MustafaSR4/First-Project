package Summer1;
import java.util.Scanner;
public class After10days {
	public static void main(String[]args) {
		int day;
		Scanner input =new Scanner(System.in);
		System.out.println("Please enter our day");
		day=input.nextInt();
		switch (day) {
		case 0: System.out.println("Its Sunday") ; break ;
		case 1: System.out.println("Its Monday") ; break ;
		case 2: System.out.println ("Its Tuesday") ; break ;
		case 3: System.out.println ("Its Wednesday") ; break ;
		case 4: System.out.println("Its Thursday") ; break ;
		case 5: System.out.println("Its Friday") ; break ;
		case 6: System.out.println ("Its Saturday") ; break ;
		default: System.out.println("Error input.") ; break ;
	    }
		int after=(day + 10) %7;
		System.out.println("The order of day in the week in 10 days  is "+ after);
		
	}
}
