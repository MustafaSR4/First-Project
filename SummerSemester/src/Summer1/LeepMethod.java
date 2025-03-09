package Summer1;
import java.util.Scanner;
public class LeepMethod {
	public static boolean isLeapYear(int year) {
		if (year%4==0 && year%100!=0 && year%400==0){
			return true;
		}
		else 
		return false;
	}
	public static void main(String[] args) {
		Scanner input= new Scanner(System.in);
		System.out.println("Enter a year");
		int year =input.nextInt();
		if (isLeapYear(year)==true) {
			System.out.println(year+" is a leap year");
		}
		else {
			System.out.println(year +" is not a leap year");

		}
				
	}

}
