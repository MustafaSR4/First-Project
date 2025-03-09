package Summer1;
import java.util.Scanner;
public class Arrays {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int average;
		int mylist[]=new int[3];
		int sum=0;
		int i;
		for ( 	 i=0;i<mylist.length;i++) {
			mylist[i]=input.nextInt();
			sum+=mylist[i];
		}
		System.out.println("The sum is "+sum);
		average=sum/i;
		System.out.println("The average is "+average);
		
	}

}
