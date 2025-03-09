package Summer1;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class LabHome {
	public static void main(String[] args) {
		File f = new File("Count");
		if (f.exists())
			try {
				Scanner s = new Scanner(f);
				int charCount = 0, wordsCount = 0, linesCount = 0;
				while (s.hasNext()) {
					String[] words = s.nextLine().split(" ");
					wordsCount += words.length;
					for (int j = 0; j < words.length; j++) {
						charCount += words[j].length();
					}
					linesCount++;
				}
				System.out.println("The number of characters is : " + charCount);
				System.out.println("The number of words is : " + wordsCount);
				System.out.println("The number of lines is : " + linesCount);

			} catch (IOException e) {
				System.out.println(e);

			}
		else
			System.out.println("Oh noooooo!! The file is not exists!");

	}
}