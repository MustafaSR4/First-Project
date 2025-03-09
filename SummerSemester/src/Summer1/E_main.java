package Summer1;

public class E_main {

	public static void main(String[] args) {

		Employee emp1 = new Employee();
		Employee emp2 = new Employee("Hubertus Andrea", "Software Engineer", 60000);
		int [] mylist={1, 2, 3, 4, 6};
		
		System.out.println("\nEmployee Details:");
		emp1.printEmployeeDetails();
		emp2.printEmployeeDetails();
		System.out.println(mylist[4]);

	}

}
