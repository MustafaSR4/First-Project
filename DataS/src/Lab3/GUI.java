package Lab3;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GUI extends Application {
	private MenuBar menuBar;
	private Menu menu1;
	private MenuItem openItem;
	private BorderPane bdPane;
	private VBox vBox;
	private HBox hBox;
	private FileChooser fChooser;
	private TextArea txtArea;
	private Button next, previous;
	private LinkedList list1;
	private int currentIndex;

	@Override
	public void start(Stage primaryStage) {
		menuBar = new MenuBar();
		menu1 = new Menu("File");
		openItem = new MenuItem("Open");
		menu1.getItems().addAll(openItem);
		menuBar.getMenus().addAll(menu1);
		bdPane = new BorderPane();
		vBox = new VBox(50);
		vBox.setPadding(new Insets(10, 10, 10, 10));
		vBox.setAlignment(Pos.CENTER);
		vBox.setMaxWidth(600);
		hBox = new HBox(20);
		hBox.setPadding(new Insets(10, 10, 10, 10));
		hBox.setAlignment(Pos.CENTER);
		fChooser = new FileChooser();
		txtArea = new TextArea();
		txtArea.setPrefHeight(300);
		txtArea.setPrefWidth(600);
		next = new Button("Next");
		previous = new Button("Previous");
		list1 = new LinkedList();

		openItem.setOnAction(e -> {
			fChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt"));
			File f = fChooser.showOpenDialog(primaryStage);

			try (Scanner read = new Scanner(f)) {

				while (read.hasNext()) {
					String line = read.nextLine();
					String[] data = line.split(",");
					if (data.length == 3) {
						int age = Integer.parseInt(data[1]);
						int mark = Integer.parseInt(data[2]);

						// We are just checking the first node which index is zero
						if (list1.getSize() == 0) {
							list1.addFirst(new Student(data[0], age, mark));
							currentIndex = 0;
						} else if (age < ((Student) list1.get(0)).getAge()) {
							list1.add(0, new Student(data[0], age, mark));
							currentIndex = 0;
						} else {
							list1.add(new Student(data[0], age, mark));
						}
						
					}
				}
			} catch (FileNotFoundException e1) {
				System.out.println(e1.getMessage());
			}
//			list1.print(list1.getFront());
			
			txtArea.setText("");
			Student s = ((Student) list1.getFront().getElement());
			txtArea.appendText(
					"Name: " + s.getName() + "\n" + "Age: " + s.getAge() + "\n" + "Mark: " + s.getMark() + "\n");
		});

		

		next.setOnAction(e -> {
		    currentIndex++;
		    if (currentIndex < list1.getSize()) {
		        Student s = (Student) list1.get(currentIndex);
		        txtArea.setText( "Name: " + s.getName() + "\n" + "Age: " + s.getAge() + "\n" +  "Mark: " + s.getMark() + "\n" );		
		        } else {
		        currentIndex = list1.getSize() - 1; 
		    }
		});

		previous.setOnAction(e -> {
		    currentIndex--;
		    if (currentIndex >= 0) {
		        Student s = (Student) list1.get(currentIndex);
		        txtArea.setText( "Name: " + s.getName() + "\n" + "Age: " + s.getAge() + "\n" +  "Mark: " + s.getMark() + "\n" );		
		    } else {
		        currentIndex = 0; 
		    }
		});


		hBox.getChildren().addAll(previous, next);
		vBox.getChildren().addAll(txtArea, hBox);

		bdPane.setTop(menuBar);
		bdPane.setCenter(vBox);

		Scene scene = new Scene(bdPane, 600, 600);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

}
