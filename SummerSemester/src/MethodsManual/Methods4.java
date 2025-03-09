package MethodsManual;

import java.util.Scanner;

public class Methods4 {
	public static double getArea(double n,double s) {
		double area;
		area= (n*s*s)/(4*Math.tan(Math.PI/n));
		return area;
	}
	public static void main(String[] args) {
		 Scanner input = new Scanner(System.in);
		      System.out.println("enter the number of sides");
		      double n = input.nextDouble();
		      System.out.println("enter the side");
		      double s = input.nextDouble();
		      System.out.println("the area of polygon is "+ getArea(n,s));

	}

}
