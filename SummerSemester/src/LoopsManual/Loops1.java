package LoopsManual;

public class Loops1{

	public static void main(String[] args) {
		System.out.println("all the numbers that are divisble by 3 and 4 from 1 to 1000");
		int j=0;
		for (int i=1;i<=1000;i++) {
			if (i%3==0||i%4==0){
				System.out.print(i + " ");
				j++;
				if (j % 10 == 0)
				System.out.println();
			}
		}
	}

}
