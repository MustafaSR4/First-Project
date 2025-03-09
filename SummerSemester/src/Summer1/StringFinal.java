package Summer1;
import java.util.Scanner;
public class StringFinal {

	public static void main(String[] args) {
			Scanner input=new Scanner(System.in);
			System.out.println("Enter a full name");
			String s= input.nextLine();
			
			int k = s.indexOf(" ");
			String First= s.substring(0,k);
			String Last= s.substring(k,s.length());
			
			System.out.println("The full name is "+ First +""+Last);
	}

}
