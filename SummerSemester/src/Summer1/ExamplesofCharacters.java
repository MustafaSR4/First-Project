package Summer1;

public class ExamplesofCharacters {

	public static void main(String[] args) {
		Character ch1=new Character ('c');
		Character ch2=new Character ('a');
		
		int res=ch1.compareTo(ch2);
			boolean res1=ch1.equals(ch2);
			boolean res2=Character.isLetter(ch2);
			boolean res3=Character.isDigit(ch2);
			boolean res4=Character.isUpperCase(ch2);
			char c=Character.toUpperCase('m');
			char c1=Character.toLowerCase('P');
			
			
			System.out.println(res);
			System.out.println(res1);
			System.out.println(res2);
			System.out.println(res3);
			System.out.println(res4);
			System.out.println(c);
			System.out.println(c1);
			
	}

}
