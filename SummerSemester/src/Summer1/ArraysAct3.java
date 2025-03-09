package Summer1;

public class ArraysAct3 {
	public static double min (double []array) {
		double min =array[0];
			for (int i=1;i<array.length;i++) {
				if (min>array[i]) {
					min=array[i];
				}
			}
			return min;
	}
	public static double max (double []array) {
		double max =array[0];
			for (int i=1;i<array.length;i++) {
				if (max<array[i]) {
					max=array[i];
				}
			}
			return max;
	}
	public static void main(String[] args) {
		double []x= {4.0,6.5,8.8,2.4,1.2,-1.9};
		System.out.println("The min value is "+min(x));
		System.out.println("The max value is "+max(x));
	
		}
}


