package Summer1;
import java.util.Scanner;
public class Vowels {
	public static void main(String[] args) {
			Scanner input=new Scanner(System.in);
			System.out.println("please enter a String");	
			String string=input.nextLine();
			System.out.println("the number of vowels is "+countVowels(string));

			
	}
	public static int countVowels(String string) {
		int counter=0;
		for(int i=0;i<string.length();i++) {
			if (string.charAt(i)=='a'|| string.charAt(i)=='e' || string.charAt(i)=='i'|| string.charAt(i)=='o'|| string.charAt(i)=='u')
			counter++;
		}
		return counter;
	}
}
