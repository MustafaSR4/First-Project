package Lab;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class MyFile1 {

	public static void main(String[] args) throws FileNotFoundException {
			File  fileObj=new File("MyFile1.txt");
			PrintWriter output=new PrintWriter(fileObj);
			
			output.println("Welcome to Java 122");
			output.println("Welcome to Comp 1331");
			
			output.close();
			
	}

}
