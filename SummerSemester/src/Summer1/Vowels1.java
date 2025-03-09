package Summer1;

import java.util.Scanner;

public class Vowels1 {

	public static void main(String[] args) {
		Scanner input=new Scanner(System.in);
		 System.out.print("Check all the characters of the said string are vowels or not!\n");

		System.out.println("please enter a String");	
		String string=input.nextLine();
		    System.out.print(isVowels( string));
		
}
public static boolean isVowels(String string) {

	for(int i=0;i<string.length();i++) {
		if (string.charAt(i)=='a'|| string.charAt(i)=='e' || string.charAt(i)=='i'|| string.charAt(i)=='o'|| string.charAt(i)=='u') 
			return false;
		else
			return true;
			
		
	}
	return false;
}

}
