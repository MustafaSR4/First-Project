import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LabOne extends Application {
	private MenuBar menuBar;
	private Menu menu1, menu2;
	private MenuItem item1, item2, openItem, saveItem, item3, item4;
	private BorderPane bdPane;
	private TextArea txtArea;
	private ArrayList<String> words;
	private ArrayList<Integer> numbers, powers, towers;
	private VBox vBox;
	private FileChooser fChooser;
	private TextField txtField;
	private int count1,count2;

	@Override
	public void start(Stage primaryStage){
		words = new ArrayList<>();
		numbers = new ArrayList<>();
		powers = new ArrayList<>();
		towers = new ArrayList<>();
		menuBar = new MenuBar();
		menu1 = new Menu("Operation");
		menu2 = new Menu("File");
		item2 = new MenuItem("Reverse Word");
		item1 = new MenuItem("Fact");
		item3 = new MenuItem("Power");
		item4 = new MenuItem("Tower of Hanoai");
		openItem = new MenuItem("Open");
		saveItem = new MenuItem("Save");
		menu1.getItems().addAll(item1, item2, item3, item4);
		menu2.getItems().addAll(openItem, saveItem);
		menuBar.getMenus().addAll(menu2, menu1);
		bdPane = new BorderPane();
		vBox = new VBox(50);
		vBox.setPadding(new Insets(10, 10, 10 , 10));
		vBox.setAlignment(Pos.CENTER);
		vBox.setMaxWidth(600);
		txtArea = new TextArea();
		txtArea.setPrefHeight(200);
		txtArea.setPrefWidth(700);
	
		txtField = new TextField();
		txtField.setPrefSize(10, 10);

		fChooser = new FileChooser();
		fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.rtf", "*.csv"));

		openItem.setOnAction(e -> {
		    File f = fChooser.showOpenDialog(primaryStage);

		    if (f != null) {
		        txtArea.setText("You opened this file: " + f.getName());
		        System.out.println("File selected: " + f.getAbsolutePath());

				Scanner read;
				try {
					read = new Scanner(f);
					while (read.hasNext()) {
						
						String s = read.nextLine();
						if (isNumeric(s)) {
							int x = Integer.parseInt(s);
							numbers.add(x);
						} else if (s.contains(",")){
							String[] line = s.split(",");
							if(line.length != 2){
								throw new InputMismatchException("Wrong number of inputs");
							}
							if(isNumeric(line[0]) && isNumeric(line[1])){
							int x = Integer.parseInt(line[0]);
							int y = Integer.parseInt(line[1]);
							powers.add(x);
							powers.add(y);
							}
						}else if(s.contains("t")){
							String[] line = s.split("t");
							System.out.println(line[0]);
							if(isNumeric(line[0])){
								int x = Integer.parseInt(line[0]);
								towers.add(x);
							}
						}
						else
							words.add(s);
					}
				} catch (FileNotFoundException e1) {
					System.out.println(e1.getMessage());
				} catch(InputMismatchException e1){
					System.out.println(e1.getMessage());
				}

				saveItem.setOnAction(e2->{
					try(FileWriter fileWriter = new FileWriter(f, true);
							PrintWriter printWriter = new PrintWriter(fileWriter);) {
							  String s = txtField.getText();
							  printWriter.println(s);
							}catch(Exception e5){
								e5.getMessage();
							}
				});
			}
		});

		item1.setOnAction(e -> {
			txtArea.setText("");
			for(Integer x : numbers)
				txtArea.appendText("The factorial of number " + x + " is: "+ fact(x) + "\n");
		});

		item2.setOnAction(e -> {
			txtArea.setText("");
			for(String s : words)
				txtArea.appendText("The reversed word of " + s + " is: "+reverseWord(s) + "\n");
		});

		item3.setOnAction(e->{
			txtArea.setText("");
			for(int i=0; i<powers.size()/2; i++)
				txtArea.appendText("The " + powers.get((2*i) + 1) + " power of number " + powers.get(2*i) + " is: "+ power(powers.get(2*i), powers.get((2*i) + 1)) + "\n");
		});

		item4.setOnAction(e -> {
		    txtArea.setText("");
		    for (Integer x : towers) {
		        count1 = 0;  
		        count2 = 0;  
		        int functionCalls = TH(x, 'A', 'B', 'C');
		        txtArea.appendText("The number of function calls: " + (functionCalls - 1) + "\n");
		        txtArea.appendText("The number of moves: " + count1 + "\n");
		    }
		});


		vBox.getChildren().addAll(txtArea, txtField);

		bdPane.setTop(menuBar);
		bdPane.setCenter(vBox);

		Scene scene = new Scene(bdPane, 500, 500);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static boolean isNumeric(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static long fact(int x) {
		if (x == 0 || x == 1)
			return 1;
		else
			return x * fact(x - 1);
	}

	public static String reverseWord(String s) {
		if (s.length() == 0 || s.length() == 1) {
			return s;
		} else {
			return reverseWord(s.substring(1)) + s.charAt(0);
		}
	}

	public static long power(int x, int y) {
		if (y == 0)
			return 1;
		else
			return (x * power(x, y - 1));
	}

	public int TH(int n, char Beg, char Aux, char End) {
	    int count = 0;  

	    if (n > 0) {
	        count += TH(n - 1, Beg, End, Aux);
	        txtArea.appendText(Beg + "-->" + End + "\n");
	        count1++;
	        count += TH(n - 1, Aux, Beg, End);
	    }

	    return count + 1;  
	}


	public static void main(String[] args) {
		launch(args);

	}
}