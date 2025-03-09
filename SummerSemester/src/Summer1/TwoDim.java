package Summer1;
import java.util.Scanner;
public class TwoDim {
		public static int sumColumns(int [][] m , int columnIndex) {
			int sum=0;
			int average = 0;
			for (int i = 0; i < m.length; i++) {
				sum += m[i][columnIndex];
			//	average=sum/i;
			}
			//	return average;
			return sum;
				}
		public static int sumRows(int [][] m , int rowIndex) {
			int sum=0;
			for (int i = 0; i < m.length; i++)
				sum += m[i][rowIndex];
				return sum;
				}
			
	

	public static void main(String[] args) {
		
			Scanner input=new Scanner(System.in);
			int sum=0;
			int avarege = 0;
			System.out.println("Enter number of building length");
			int [][]list=new int [input.nextInt()][input.nextInt()];
			for (int i=0;i<list.length;i++) {
						for (int j=0;j<list[i].length;j++) {
							System.out.println("Enter the elements in the colum of  each row");
								list[i][j]=input.nextInt();
						}
			}
								
									for (int i = 0; i < list[0].length; i++)
									System.out.println("Sum of the elements at column " +i+ " is:"+sumColumns(list, i));
									for (int i = 0; i < list.length; i++)
									System.out.println("Sum of the elements at row " +i+ " is:"+sumRows(list, i));		
									
						}
			
			
		
			
	}

