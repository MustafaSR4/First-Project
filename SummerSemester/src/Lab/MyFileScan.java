package Lab;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
public class MyFileScan {
	 //String file = "flights.txt";
	  //Path filePath = Paths.get("src", "all", "flights.txt"); // Adjust the path as needed
	  // Get the absolute path of the file
	//String absolutePath = filePath.toAbsolutePath().toString();
	//System.out.println("The absolute path of the file is: " + absolutePath);
	public static void main(String[] args) throws FileNotFoundException {
			File fileObj=new File ("MyFile1.txt");
			Scanner input=new Scanner(fileObj);
			while (input.hasNext()){
				try {
					String n1=input.next();
					String n2=input.next();
					String n3=input.next();
					
					int n5=input.nextInt();
					System.out.println(n1+" "+n2+" "+n3+" "+n5);//This is the error
						}catch(InputMismatchException ex) {
							System.out.println("Incorrect Input");
						}
						catch(NoSuchElementException ex) {
							System.out.println("Incorrect Input");
						}
				
				
			}
			input.close();
	}

}
