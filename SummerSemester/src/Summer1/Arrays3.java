package Summer1;

import java.util.Scanner;

public class Arrays3 {
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int[] matrix = new int[10];
		for (int i = 0; i < matrix.length; i++) {
			System.out.print("Please enter an integer:");
			matrix[i] = input.nextInt();
		}
		for (int i = matrix.length - 1; i >= 0; i--) {
			System.out.print(matrix[i]+" ");
		}
	}
}
