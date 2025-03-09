package Summer1;
import java.util.Scanner;
public class ExofStrings {
		
	public static String reversedString(String s) {
		String reversed = "";
        for (int i = s.length()-1 ; i >= 0; i--) {
            reversed += s.charAt(i);
        }
		return reversed;
	}
	
	
	public static int CountVowels(String s) {
		int counter=0;
		for (int i=0;i<s.length();i++) {
			  char ch = s.charAt(i);
			if (ch=='a' ||ch=='e' ||ch== 'i' || ch=='o' || ch=='u') {
				counter++;
			}
		}
		return counter;
	}
	
	
	public static Boolean isPalendrome(String s) {
		 boolean isPalindrome = true;
		 
		int left=0;
		int right=s.length()-1;
		while (left <right ) {
			if (s.charAt(left) != s.charAt(right)) {
				isPalindrome=false;
				break;
			}
		left ++;
		right --;
		}
		if (isPalindrome) {
           return true;
        } else {
            return false;
        }
	
	}
	
	public static int CountWords(String s) {
		
		String[]words=s.split(" ",2);
		for(String temp:words) {
			System.out.println(temp);
		}
	
		
		
		
		return words.length;
	}
	
	
	public static void main(String[] args) {
			Scanner input=new Scanner (System.in);
			System.out.println("Enter a your first and last name");
			String s1=input.nextLine();
			//StringBuilder s1= new StringBuilder(input.nextLine());
			//StringBuilder s2= new StringBuilder(input.nextLine());
			//s1.append(s2);
			
		//	s1.reverse();
			//System.out.println(s1);
	   //  /*1*/   System.out.println("The reversed string is "+reversedString(s));
	   //  /*2*/ 	System.out.println("The number of vowels in the string is "+CountVowels(s));
	     ///*3*/ 	System.out.println("Is the string Palendrome "+isPalendrome(s));
	     /*4*/ 	System.out.println("The number of words is  "+CountWords(s1)); 
	     


	}

}
