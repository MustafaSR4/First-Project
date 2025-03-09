package ProjectPhase0;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ProjectPhase0 extends Application {


	// Declaration of variables and GUI components
	private FileChooser fChooser;
	private MenuBar menuBar;
	private Menu menu1, menu2, menu3;
	private MenuItem insert, delete, search, btOpen, age, date, gender;
	private BorderPane bdPane;
	private Martyr[] martyrsArray;
	private int size;
	private TextField nameField;
	private TextArea txtArea;
	private VBox vbox;
	private TextField ageField;
	private TextField dateField;
	private HBox hbox;
	private Label dateLabel;
	private Label ageLabel;
	private VBox vbox2;
	private VBox vbox3;
	private Label nameLabel;

	@Override
	public void start(Stage primaryStage) {
		// Initialize variables and GUI components

		martyrsArray = new Martyr[16];// intial size of the array is 16
		menu1 = new Menu("File");      //menuItems in Menu in menuBar
		btOpen = new MenuItem("Open");
		menu1.getItems().add(btOpen);

		menu2 = new Menu("Operation");
		insert = new MenuItem("Insert");
		delete = new MenuItem("Delete");
		search = new MenuItem("Search");
		menu2.getItems().addAll(insert, delete, search);

		age = new MenuItem("Age");
		date = new MenuItem("Date");
		gender = new MenuItem("Gender");

		menu3 = new Menu("Display");
		menu3.getItems().addAll(age, date, gender);
		menuBar = new MenuBar();
		menuBar.getMenus().addAll(menu1, menu2, menu3);
		bdPane = new BorderPane();

		fChooser = new FileChooser();
		fChooser.setTitle("fileChooser");

		ageLabel = new Label("Enter age to display numbers");
		dateLabel = new Label("Enter date of death to display numbers");
		ageField = new TextField();
		dateField = new TextField();
		ageField.setPrefSize(2, 2);
		dateField.setPrefSize(10, 5);

		vbox2 = new VBox(10);
		vbox2.getChildren().addAll(ageLabel, ageField);
		vbox3 = new VBox(10);
		vbox3.getChildren().addAll(dateLabel, dateField);

		hbox = new HBox(50);
		hbox.getChildren().addAll(vbox2, vbox3);

		nameField = new TextField(); //customization for the Labels and txtArea
		nameField.setPrefSize(10, 10);
		nameLabel = new Label("Enter the name to do any Operation");
		txtArea = new TextArea();
		txtArea.setPrefHeight(1000);
		txtArea.setPrefWidth(300);
		vbox = new VBox(20);
		vbox.setPadding(new Insets(10, 10, 10, 10));
		vbox.setAlignment(Pos.CENTER);
		vbox.setMaxWidth(600);
		vbox.getChildren().addAll(txtArea, nameLabel, nameField);



		// Event handlers for menu items (open file chooser)

		btOpen.setOnAction(e -> {
			fChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Downloads"));//intial Directory
			fChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));//extention that is can have to use it in the file chooser
			File selectedFile = fChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
				txtArea.setText(selectedFile.getName() + " has been opened\n");
				martyrsArray = readCSV(selectedFile);//read all the date from the selected file (data.csv) and store it in the martyr array
			}
			//these menuItems for display the number of age and date and gender
			gender.setOnAction(e4 -> { 

				DisplayGender();
			});

			date.setOnAction(e5 -> {
				DisplayDate();
			});
			age.setOnAction(e6 -> {
				DisplayAge();
			});

			insert.setOnAction(e1 -> {//this menu Item for adding in the martyr array

				// Assuming the text in nameField is in the format: "Name, Age, EventLocation,
				// DateOfDeath, Gender"
				String martyrInfo = nameField.getText();

				String[] parts = martyrInfo.split(",");

				if (parts.length != 5) {//it should have 5 parts (Format)
					txtArea.setText(
							"Invalid record format. Please enter a record in the format: Name, Age, EventLocation, DateOfDeath, Gender");
					return;
				}

				String name = parts[0].trim();
				int age = -1;
				try {
					age = Integer.parseInt(parts[1].trim());//age it should be an integer and when it reads from the file it should put a ddefault vaule (ex :-1) for whose that doesn't know their age 
					if (age < 0) {
						txtArea.setText("Invalid age. Please enter a non-negative integer.");
						return;
					}
				} catch (NumberFormatException ex) {
					txtArea.setText("Invalid age format. Please enter a valid integer for age.");
					return;
				}

				String eventLocation = parts[2].trim();
				String dateOfDeath = parts[3].trim();
				String gender = parts[4].trim();

				if (!isDateFormatCorrect(dateOfDeath)) {
					txtArea.setText("Invalid date format. Date of death must be in MM/DD/YY format");//it should have a format and its consider in the method
					return;
				}

				// Check if the gender is valid
				if (!gender.equalsIgnoreCase("M") && !gender.equalsIgnoreCase("F") && !gender.equalsIgnoreCase("NA")) {
					txtArea.setText("Invalid gender. Gender must be 'M', 'F', or 'NA'.");
					return;
				}

				Martyr newMartyr = new Martyr(name, age, eventLocation, dateOfDeath, gender);

				// Check for duplicate records
				for (Martyr existingMartyr : martyrsArray) {// there is a duplicated record in the file so it shouldn't store in the array unless those whose name is unknown
					if (existingMartyr != null && existingMartyr.toString().equalsIgnoreCase(newMartyr.toString())) {
						txtArea.setText("Duplicate record. Martyr with the same name already exists.");
						return;
					}
				}

				if (size >= martyrsArray.length) {// when the size is equal the number of martyrs it should resize
					martyrsArray = resizeArray(martyrsArray);
				}

				martyrsArray[size] = newMartyr;//adding the new martyr in the Array
				size++;

				txtArea.setText(newMartyr.getName() + " added successfully to the array.\n");

			});

			delete.setOnAction(e2 -> {//take the name and goes to the method
				// Get the name to delete
				String nameToDelete = nameField.getText();

				// Call the deleteMartyr method with the provided name
				deleteMartyr(nameToDelete);
			});

			search.setOnAction(e3 -> {
				String nameToSearch = nameField.getText();
				ArrayList<String> matchingMartyrs = searchByName(nameToSearch);//this arraylist that have the record of certain martyr

				if (!matchingMartyrs.isEmpty()) {
					// Display the found martyr details in the txtArea
					String result = "Martyrs found:\n\n";
					for (String martyrString : matchingMartyrs) {
						result += martyrString + "\n";//it display on txtArea 
					}
					txtArea.setText(result);
				} else {
					// Display a message indicating that no martyrs were found
					txtArea.setText("Martyrs not found");
				}
			});

		});
		//the main pane is borderPane in this GUI
		bdPane.setTop(menuBar);
		bdPane.setCenter(vbox);
		bdPane.setBottom(hbox);
		hbox.setAlignment(Pos.CENTER);

		Scene scene = new Scene(bdPane, 600, 600);
		primaryStage.setTitle("GUI");
		primaryStage.setScene(scene);
		primaryStage.show();
	}


	//it checks for the formate of the date thaqt have been entered in the minsert mode and in Display Date method
	private boolean isDateFormatCorrect(String date) {
		if (date.isEmpty()) {
			return true; 
		}

		String[] parts = date.split("/");//it should spilt in 3 parts 

		if (parts.length != 3) {
			return false; // Date must have three parts separated by "/"
		}

		try {
			int month = Integer.parseInt(parts[0]);
			int day = Integer.parseInt(parts[1]);
			int year = Integer.parseInt(parts[2]);

			// Check if the month, day, and year values are within valid ranges
			if (month < 1 || month > 12 || day < 1 || day > 31 || year < 0 || parts[2].length() != 4) {//the month should be from 1 and 12 and day between 1 and 31 and year have 4 characters
				return false; 
			}

		} catch (NumberFormatException e) {
			return false; 
		}

		return true; // Date correct
	}


	//this method iterate all over the martyr Array and count the gender of each Martyr and display on txtARea
	private void DisplayGender() {
		int totalCount = 0;
		int maleCount = 0;
		int femaleCount = 0;
		int unknownCount = 0;

		for (Martyr martyr : martyrsArray) {
			totalCount = size;
			if (martyr != null) {
				if (martyr.getGender().equalsIgnoreCase("M")) {
					maleCount++;
				} else if (martyr.getGender().equalsIgnoreCase("F")) {
					femaleCount++;
				} else if (martyr.getGender().equalsIgnoreCase("NA")) {
					unknownCount++; // Include "NA" in the count of unknown genders
				}
			}

		}

		txtArea.setText("");
		txtArea.appendText("Total Martyrs: " + totalCount + "\n");
		txtArea.appendText("Male Martyrs: " + maleCount + "\n");
		txtArea.appendText("Female Martyrs: " + femaleCount + "\n");
		txtArea.appendText("Unknown Gender Martyrs: " + unknownCount + "\n");
	}


	//this method iterate all over the martyr Array and count the age of each Martyr and display on txtARea
	private void DisplayAge() {
		int ageCount = 0;

		if (!ageField.getText().isEmpty()) {
			String ageInput = ageField.getText();

			try {     //there is restriction on the input of age it should be Numaric and when we enter -1 it will count all the martyrs that dont have a age value in the file
				int ageToSearch = Integer.parseInt(ageInput);

				for (Martyr martyr : martyrsArray) {
					if (martyr != null) {
						if (martyr.getAge() == ageToSearch) {
							ageCount++;
						}
					}
				}

				txtArea.setText(" ");
				txtArea.appendText("Martyrs with Age " + ageInput + ": " + ageCount + "\n");
			} catch (NumberFormatException e) {
				txtArea.setText(" ");
				txtArea.appendText("Please enter a valid age (numeric characters only).\n");
			}
		} else {
			txtArea.setText(" ");
			txtArea.appendText("You should enter an age\n");
		}
	}
	//this method iterate all over the martyrArray searching for the exact date of death and it should be in the correct form MM/DD/YY and display the count on txtArea
	private void DisplayDate() {
		int dateCount = 0;
		if (!dateField.getText().trim().isEmpty()) {
			if (isDateFormatCorrect(dateField.getText())) {
				for (Martyr martyr : martyrsArray) {
					if (martyr != null) {
						if (martyr.getDateOfDeath().equalsIgnoreCase(dateField.getText().trim())) {
							dateCount++;
						}

						txtArea.setText(" ");
						if (dateCount == 0) {
							txtArea.appendText("Check the date that entered and check for the correct format MM/DD/YY");

						} else {
							txtArea.appendText(
									"Martyrs with date of death " + dateField.getText() + ": " + dateCount + "\n");
						}

					}
				}
			} else {
				txtArea.setText("Invalid date format. Please enter a date in the format MM/DD/YY");
			}
		} else {
			txtArea.setText("You should enter a date");
		}

	}



	//this method for deleteing a certain martyr from the array and it workrd by searching for the index of the martyr that the name is given in nameField
	private void deleteMartyr(String name) {
		int indexToRemove = -1;

		// Find the index of the martyr to remove
		for (int i = 0; i < martyrsArray.length; i++) {
			if (martyrsArray[i] != null) {
				if (martyrsArray[i].getName().equalsIgnoreCase(name)) {//taking the name from the NameField
					indexToRemove = i;
					break;
				}
			}
		}

		if (indexToRemove != -1) {//when we remove the index it the last element went to null and the size will dicrease
			for (int i = indexToRemove; i < size - 1; i++) {
				martyrsArray[i] = martyrsArray[i + 1];
			}
			martyrsArray[size - 1] = null;
			size--;
			txtArea.setText(name.toString() + " has been deleted from the array.\n");
		} else {
			txtArea.setText("Martyr not found with the given name.(check Martyrs's name)");

		}

	}
	//searching for a certain martyr in the array by using the correct name
	private ArrayList<String> searchByName(String name) {
		ArrayList<String> martyrList = new ArrayList<>();
		for (Martyr martyr : martyrsArray) {
			if (martyr != null) {
				if (martyr.getName().toLowerCase().contains(name.toLowerCase())) {
					martyrList.add(martyr.toString());//when the program find the martyr it adds it in am array list 
				}
			}
		}
		return martyrList;
	}
	//this method is for reading the data (martyr's record ) from the file i choosed it from the file chooser
	private Martyr[] readCSV(File file) {
		Martyr[] martyrs = new Martyr[16];

		try (Scanner scanner = new Scanner(file)) {

			boolean skipHeaders = true;//this varible is for header to skip it and don't read it and store it in the array
			txtArea.appendText("The file (" + file.getName() + ") has been read \n");

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();

				if (skipHeaders) {
					skipHeaders = false;
					continue;
				}

				//it should split each line and  pass it to a new object using the  argument construtor from the martyr class
				String[] parts = line.split(",");
				if (parts.length == 5) {
					String name = parts[0];
					int age1 = -1; // Default value for age
					String eventLocation = parts[2];
					String dateOfDeath = parts[3];
					String gender = parts[4];

					// Check if age field is empty or null
					if (!parts[1].trim().isEmpty()) {
						try {
							age1 = Integer.parseInt(parts[1].trim());
						} catch (NumberFormatException e) {
						}
					}

					// Create a new Martyr object
					Martyr martyr = new Martyr(name, age1, eventLocation, dateOfDeath, gender);

					// Check if the array is full, and resize if necessary
					if (size == martyrs.length) {
						martyrs = resizeArray(martyrs);
					}

					// there is a duplicated record in the file so it shouldn't store in the array unless those whose name is unknown
					boolean martyrExists = false;
					for (int i = 0; i < size; i++) {

						if (martyr != null) {
							if (martyrs[i].toString().equalsIgnoreCase(martyr.toString())
									&& !martyr.getName().equals("Name unknown to B'Tselem")) {
								martyrExists = true;
								break;
							}
						}
					}

					if (!martyrExists) {
						martyrs[size++] = martyr;
					}
				} else {
					System.out.println(
							"This is the wrong format for reading (Name, Age, EventLocation, DateOFDeath, Gender)");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return martyrs;
	}
	//this method resize the array whenever it becoms full by doubling the list size.

	private Martyr[] resizeArray(Martyr[] array) {
		int newCapacity = array.length * 2;
		Martyr[] newArray = new Martyr[newCapacity];
		System.arraycopy(array, 0, newArray, 0, array.length);
		txtArea.appendText("Array has new Capacity to " + newCapacity + "\n");
		return newArray;
	}

	public static void main(String[] args) {
		launch(args);
	}
}