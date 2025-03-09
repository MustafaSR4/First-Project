package Summer1;
import java .util.Arrays;
public class SortingArrays {
		public static int LinearSearch (int []x,int key) {
		for(int i=0;i<x.length;i++)	
			if (key==x[i])
				return i;
			else
				return -1;
		return key;
			
		}
	public static void main(String[] args) {
		int [] x= {5,8,2,5,1,8};
		
		System.out.println("index is "+ 	LinearSearch(x, 2));
		
		Arrays.sort(x);
		for (int i=0;i<x.length;i++) 
			System.out.print(x[i]+" ");
		System.out.println("\nindex is "+Arrays.binarySearch(x, 3));
	}

}
