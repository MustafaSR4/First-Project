package Summer1;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
public class PrintWriterEX {

	public static void main(String[] args) throws FileNotFoundException {
			File f=new File ("numbers.txt");
			PrintWriter output= new PrintWriter(f);
			for(int i=0;i<=10;i++) {
				output.println(i+" ");
			}
			
			output.close();
	}

}
