package Summer1;
import java.util.Arrays;
import java.util.Scanner;
public class ArraysEX {
	public static int[] reverse(int []x) {
		int result[]=new int[x.length];
		
		for (int i =0, j=result.length-1;i<x.length;i++,j--){ 
				result[j]=x[i];
				
		}
		return result;
	}
	public static void main(String[] args) {
		int [] x = {2, 3, 4, 5 ,6}; 
		int [] y=reverse(x);
		for (int i =0;i<x.length;i++){
			System.out.print(x[i]+" ");
		}
		for (int i =0;i<y.length;i++){
			System.out.print(y[i]+"\t ");
		}
		
		
	
		
		
	
		
		
		

			

	}
}
