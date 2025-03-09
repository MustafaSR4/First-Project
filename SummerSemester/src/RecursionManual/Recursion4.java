package RecursionManual;
import java.util.Scanner;
public class Recursion4 {
	public static void reverseDisplay(int value) {
			if (value <=9) {
				System.out.println(value);
				return;
			}
			else	{
				System.out.print(value%10);
				reverseDisplay(value/10);
			}
		
	}

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.print("Please enter a number:");
		int value = input.nextInt();
		reverseDisplay(value);
	}
}

