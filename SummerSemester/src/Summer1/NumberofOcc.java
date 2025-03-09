package Summer1;

public class NumberofOcc {
	public static int NumberofOccurnce(int []numbers,int searchNumber) {
		int occ=0;
			for (int i=0;i<numbers.length;i++) {
				if (searchNumber==numbers[i])
					occ++;
	
			}
			return occ;
		
	}
	public static void main(String[] args) {
		int searchNumber =6;
		int[]x= {6,7,4,1,1,6,6,7,8};
		
		System.out.print(searchNumber+" occus "+NumberofOccurnce(x,searchNumber)+" times");
		
		
	}
	


}
