package Summer1;
import java.util.Scanner;
public class RectangleArea {

	public static void main(String[] args) {
		double length;
		double width;
		Scanner input= new Scanner (System.in);
		System.out.println("Please enter the length of the rectangle");
		length =input.nextDouble();
		System.out.println("Please enter the width of the rectangle");
		width =input.nextDouble();
		double area= length*width;
		System.out.println("the area of the rectangle is "+area);

	}

}
