package ProjectPhase1;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;




public class GUI_PH1 extends Application {
	private DistrictList district;
	



	private Button btOpen; // Button to open the file chooser
	private MenuBar menuBar; // Menu bar for the application
	private Menu menu1, menu2; // Menus within the menu bar
	private MenuItem item1, item2, item3, item4, item5, item6, item7, item8, item9, item10, item11, item12, item13;

	private FileChooser fChooser; // File chooser for selecting CSV files

	private BorderPane bdPane; // Main layout container
	TextArea t1 = new TextArea(); // Text area for displaying information
	Label FeedBack = new Label(); // Label for feedback messages
	 DNode current; // Start navigation from the front

	TableView searchResultsTable = new TableView(); // Table view for displaying search results
	
	@Override
	public void start(Stage primaryStage) {
	    district = new DistrictList();
	    btOpen = new Button("Open File Browser");
	    fChooser = new FileChooser();

	    // Set the initial directory directly with an absolute path
	    File initialDir = new File("/Users/mustafaalayasa/Desktop/First 2 years/DATA STRUCTURE/Data Structure/Project Phase1");
	    if (initialDir.exists() && initialDir.isDirectory()) {
	        fChooser.setInitialDirectory(initialDir);
	    } else {
	        System.out.println("The specified directory does not exist: " + initialDir.getAbsolutePath());
	    }

	    // Add filter to show only .csv files
	    fChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));

	    // Open file chooser on button click
	    btOpen.setOnAction(e -> {
	        File selectedFile = fChooser.showOpenDialog(primaryStage);
	        if (selectedFile != null) {
	            readCSV(selectedFile, district);
	            openMenuStage(primaryStage, selectedFile);
	        }
	    });

	    StackPane spane = new StackPane();
	    spane.getChildren().add(btOpen);
	    Scene scene = new Scene(spane, 300, 250);
	    primaryStage.setTitle("First Stage");
	    primaryStage.setScene(scene);
	    primaryStage.show();
	}


	public void readCSV(File file, DistrictList districtList) {
		try (Scanner scanner = new Scanner(file)) {
			boolean skipHeaders = true;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if (skipHeaders) {
					skipHeaders = false;
					continue;
				}
				String[] parts = line.split(",");
				if (parts.length >= 5) {
					String districtName = parts[4].trim();
					String locationName = parts[3].trim();
					String martyrName = parts[0].trim();
					String dateOfDeath = parts[1].trim();
					byte age = parts[2].trim().isEmpty() ? -1 : Byte.parseByte(parts[2].trim());
					char gender = parts[5].trim().charAt(0);

					Martyr martyr = new Martyr(martyrName, dateOfDeath, age, locationName, districtName, gender);
					addMartyrToLocation(districtList, districtName, locationName, martyr);
				} else {
					System.out.println("Skipping incomplete or malformed line: " + line);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("The file was not found: " + e.getMessage());
		} catch (NumberFormatException e) {
			System.out.println("Number format error: " + e.getMessage());
		}
	}


	// Method to add a martyr to a specific location within a district
	public void addMartyrToLocation(DistrictList districtList, String districtName, String locationName,
			Martyr martyr) {
		District district = districtList.findOrAddDistrict(districtName);
		if (district == null) {
			t1.appendText("Error: District '" + districtName + "' could not be found or created.");
			return;
		}
		LocationList locationList = district.getLocationDlist();
		Location location = locationList.findOrAddLocation(locationName);
		if (location == null) {
			t1.appendText("Error: Location '" + locationName + "' could not be found or created in District '"
					+ districtName + "'.");
			return;
		}
		location.getMartyrlist().addMartyrSorted(martyr);
	}

	// Method to print all districts with their locations and martyrs
	public void printDistrictsWithLists() {
		t1.setText("");
		DNode currentDistrict = district.getFront();
		if (currentDistrict == null) {
			t1.setText("No districts available.");
			return;
		}
		while (currentDistrict != null) {
			District dist = (District) currentDistrict.getElement();
			t1.appendText("\nDistrict: " + dist.getDisName());
			LocationList locationList = dist.getLocationDlist();
			if (locationList == null || locationList.getFront() == null) {
				t1.appendText("\n\tNo locations available\n");
			} else {
				SNode currentLocation = locationList.getFront();
				while (currentLocation != null) {
					Location loc = (Location) currentLocation.getElement();
					t1.appendText("\n\t-->Location: " + loc.getLocName() + "\n");
					LocationList martyrList = loc.getMartyrlist();
					if (martyrList == null || martyrList.getMartyrsFront() == null) {
						t1.appendText("\t\tNo martyrs listed\n");
					} else {
						SNode currentMartyrNode = martyrList.getMartyrsFront();
						while (currentMartyrNode != null) {
							Martyr martyr = (Martyr) currentMartyrNode.getElement();
							t1.appendText("\t\t\tMartyr: " + martyr.getName() + ", Age: " + martyr.getAge()
							+ ", Date of Death: " + martyr.getDateOfDeath() + "\n");
							currentMartyrNode = currentMartyrNode.getNext();
						}
					}
					currentLocation = currentLocation.getNext();
				}
			}
			currentDistrict = currentDistrict.getNext();
		}
	}

	// Method to display a form for inserting a new district.
	private void showInsertDistrictForm() {
		Stage insertStage = new Stage(); 
		VBox layout = new VBox(10); 

		TextField districtNameField = new TextField();
		districtNameField.setPromptText("District Name");

		Button insertButton = new Button("Insert");
		insertButton.setOnAction(e -> {
			String districtName = districtNameField.getText().trim();
			if (!districtName.isEmpty()) {
				District newDistrict = new District(districtName); 
				this.district.insertNewDistrictRecord(newDistrict); 
				printDistrictsWithLists(); // Refresh the displayed list of districts
				insertStage.close(); 
			} else {
				FeedBack.setText("District name cannot be empty."); 
				districtNameField.clear();
			}
		});

		layout.getChildren().addAll(new Label("Enter New District Name:"), districtNameField, insertButton, FeedBack);
		Scene scene = new Scene(layout, 300, 200); 
		insertStage.setScene(scene); 
		insertStage.setTitle("Insert New District"); 
		insertStage.show(); 
	}

	// Method to display a form for updating an existing district's name.
	private void showUpdateDistrictForm() {
		Stage updateStage = new Stage(); 
		VBox layout = new VBox(10); 

		TextField districtNameField = new TextField();
		districtNameField.setPromptText("Existing District Name");
		TextField newDistrictNameField = new TextField();
		newDistrictNameField.setPromptText("New District Name");

		Button updateButton = new Button("Update");
		updateButton.setOnAction(e -> {
			String oldName = districtNameField.getText().trim();
			String newName = newDistrictNameField.getText().trim();
			if (!oldName.isEmpty() && !newName.isEmpty()) {
				try {
					district.updateDistrictName(oldName, newName); 
					FeedBack.setText("District name updated successfully."); 
//					printDistrictsWithLists(); // Refresh the displayed list of districts
					updateStage.close(); 
				} catch (IllegalArgumentException ex) {
					FeedBack.setText(ex.getMessage()); // Set error message if the update fails
				}
			} else {
				FeedBack.setText("Please fill in both fields before updating."); // Set error message if fields are
				// empty
			}
		});

		// Add all UI components to the layout
		layout.getChildren().addAll(new Label("Enter District Name to Update:"), districtNameField,
				new Label("Enter New District Name:"), newDistrictNameField, updateButton, FeedBack);
		Scene scene = new Scene(layout, 500, 250); // Create a scene with specified dimensions
		updateStage.setScene(scene); // Set the scene for the stage
		updateStage.setTitle("Update District Record"); // Set the title of the stage
		updateStage.show(); // Display the stage
	}

	private void showDeleteDistrictForm() {
		Stage deleteStage = new Stage(); 
		VBox layout = new VBox(10); // Vertical box layout with spacing of 10 pixels

		TextField districtNameField = new TextField();
		districtNameField.setPromptText("District Name to Delete");

		// Button to trigger the deletion action
		Button deleteButton = new Button("Delete");
		deleteButton.setOnAction(e -> {
			String districtName = districtNameField.getText().trim();
			if (districtName.isEmpty()) {
				FeedBack.setText("Please enter a district name."); 
			} else {
				boolean deleted = district.deleteDistrict(districtName); // Attempt to delete the district
				if (deleted) {
					FeedBack.setText("District deleted successfully."); // Set success message
//					printDistrictsWithLists(); 
					deleteStage.close(); // Close the deletion form
				} else {
					FeedBack.setText("District not found or could not be deleted."); 
				}
			}
		});

		// Add all UI components to the layout
		layout.getChildren().addAll(new Label("Enter District Name to Delete:"), districtNameField, deleteButton,
				FeedBack);
		Scene scene = new Scene(layout, 500, 200); // Create a scene with specified dimensions
		deleteStage.setScene(scene); // Set the scene for the stage
		deleteStage.setTitle("Delete District Record"); // Set the title of the stage
		deleteStage.show(); // Display the stage
	}
	
	
	
	
	private void createNavigationStage() {
	    Stage navigationStage = new Stage();
	    navigationStage.setTitle("Navigation Stage");

	    // UI components
	    Label nameLabel = new Label("District Name:");
	    Label nameValueLabel = new Label("No district available");
	    Label totalMartyrsLabel = new Label("Total Number of Martyrs:");
	    Label totalMartyrsValueLabel = new Label();
	    Label maleFemaleLabel = new Label("Total Number of Male and Female Martyrs:");
	    Label maleFemaleValueLabel = new Label();
	    Label averageAgeLabel = new Label("Average Martyrs Age:");
	    Label averageAgeValueLabel = new Label();
	    Label maxMartyrsDateLabel = new Label("Date with Maximum Number of Martyrs:");
	    Label maxMartyrsDateValueLabel = new Label();

	    // Navigation buttons
	    Button next = new Button("Next");
	    Button previous = new Button("Previous");

	    current=district.getFront();	
	    updateUI(current, nameValueLabel, totalMartyrsValueLabel, maleFemaleValueLabel, averageAgeValueLabel, maxMartyrsDateValueLabel);
	      
	    next.setOnAction(e -> {
	        if (current != null && current.getNext() != null) {
	            current = current.getNext();
	            updateUI(current, nameValueLabel, totalMartyrsValueLabel, maleFemaleValueLabel, averageAgeValueLabel, maxMartyrsDateValueLabel);
	        } else {
	            System.out.println("No next node available");
	        }
	    });

	    previous.setOnAction(e -> {
	        if (current != null && current.getPrev() != null) {
	            current = current.getPrev();
	            updateUI(current, nameValueLabel, totalMartyrsValueLabel, maleFemaleValueLabel, averageAgeValueLabel, maxMartyrsDateValueLabel);
	        } else {
	            System.out.println("No previous node available");
	        }
	    });



	    VBox vbox = new VBox(10);
	    vbox.getChildren().addAll(nameLabel, nameValueLabel, totalMartyrsLabel, totalMartyrsValueLabel,
	                              maleFemaleLabel, maleFemaleValueLabel, averageAgeLabel, averageAgeValueLabel,
	                              maxMartyrsDateLabel, maxMartyrsDateValueLabel, next, previous);

	    Scene scene = new Scene(vbox, 400, 400);
	    navigationStage.setScene(scene);
	    navigationStage.show();

	   
	}

	private void updateUI(DNode current, Label nameValueLabel, Label totalMartyrsValueLabel, Label maleFemaleValueLabel, Label averageAgeValueLabel, Label maxMartyrsDateValueLabel) {
	    if (current != null) {
	     
	    	nameValueLabel.setText(((District)(current.getElement())).getDisName());					  
	        totalMartyrsValueLabel.setText(district.getTotalMartyrs(current)+"");
	        maleFemaleValueLabel.setText(district.getMaleFemaleMartyrs(current));
	        averageAgeValueLabel.setText(district.getAverageAge(current)+"");
	        maxMartyrsDateValueLabel.setText(district.getMaxMartyrsDate(current));
	    } else {
	        nameValueLabel.setText("No district available");
	        totalMartyrsValueLabel.setText("");
	        maleFemaleValueLabel.setText("");
	        averageAgeValueLabel.setText("");
	        maxMartyrsDateValueLabel.setText("");
	    }
	}



	


	
	
	
	
	

	// Method to display and print locations by district.
	private void printLocationsByDistrict() throws IOException {
		VBox layout = new VBox(); 
		TextField districtField = new TextField(); 
		districtField.setPromptText("Enter District Name"); 
		Button searchButton = new Button("Search"); 

		TableView tableView = new TableView<>(); 
		
		TableColumn nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn locationColumn = new TableColumn<>("Location");
		locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

		// Add columns to the table view.
		tableView.getColumns().addAll(nameColumn, locationColumn);

		searchButton.setOnAction(e -> {
		    FeedBack.setText("");

			tableView.getItems().clear(); 
			String districtName = districtField.getText().trim(); 
			if (districtName.isEmpty()) {
				FeedBack.setText("Please enter a district name."); 
				return;
			} else if (!districtName.equals(district.getName())) {
			    FeedBack.setText("The entered District name is incorrect");
			} else {
			    FeedBack.setText("This is all the locations of " + districtName);
			}

			
			File inputFile = new File(
					"/Users/mustafaalayasa/Desktop/DATA STRUCTURE/Data Structure/Project Phase1/sample.csv"); // Input
			
			File outputFile = new File(districtName + ".txt"); // Output file path.

			try (Scanner scanner = new Scanner(inputFile); PrintWriter writer = new PrintWriter(outputFile)) {
				scanner.nextLine(); // Skip the header line.
				while (scanner.hasNextLine()) {
					String line = scanner.nextLine();
					String[] data = line.split(",");
					byte age = data[2].isEmpty() ? -1 : Byte.parseByte(data[2]);
					Martyr martyr = new Martyr(data[0], data[1], age, data[3], data[4], data[5].charAt(0));

					if (martyr.getDistrict().equalsIgnoreCase(districtName)) {
						tableView.getItems().add(martyr); 
						writer.println(martyr.toString()); 
					}
				}
			} catch (FileNotFoundException ex) {
				FeedBack.setText("File not found: " + ex.getMessage()); 
			} catch (NumberFormatException ex) {
				FeedBack.setText("Invalid age format"); 
			}
		});

		layout.getChildren().addAll(districtField, searchButton, tableView, FeedBack); 
		Stage stage = new Stage(); 
		Scene scene = new Scene(layout, 600, 400); 
		stage.setScene(scene); 
		stage.setTitle("Locations by District"); 
		stage.show(); 
	}

	// Method to open the main menu stage of the application.
	private void openMenuStage(Stage primaryStage, File file) {
		bdPane = new BorderPane(); // Main layout manager.
		menuBar = new MenuBar(); // Menu bar for top-level menu items.
	printDistrictsWithLists(); // Refresh the displayed list of districts

		// Define menus and menu items.
		menu1 = new Menu("District");
		menu2 = new Menu("Location");
		item1 = new MenuItem("insert new district record");
		item2 = new MenuItem("update a district record.");
		item3 = new MenuItem("delete a district record");
		item4 = new MenuItem("Navigation through district records");
		item5 = new MenuItem("Printing certain district on (table view)");

		menu2 = new Menu("Location");
		item6 = new MenuItem("insert new location record");
		item7 = new MenuItem("update a location record.");
		item8 = new MenuItem("delete a location record");
		item9 = new MenuItem("Navigation through location records");
		item10 = new MenuItem("insert new martyr record");
		item11 = new MenuItem("update martyr record");
		item12 = new MenuItem("delete martyr record");
		item13 = new MenuItem("search martyr record");

		// Add items to menus.
		menu1.getItems().addAll(item1, item2, item3, item4, item5);
		menu2.getItems().addAll(item6, item7, item8, item9, item10, item11, item12, item13);
		menuBar.getMenus().addAll(menu1, menu2); // Add menus to the menu bar.

		// Set action handlers for menu items.
		item1.setOnAction(e -> showInsertDistrictForm());
		item2.setOnAction(e -> showUpdateDistrictForm());
		item3.setOnAction(e -> showDeleteDistrictForm());
		item4.setOnAction(e -> createNavigationStage());
		item5.setOnAction(e -> {
			try {
				printLocationsByDistrict();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		item6.setOnAction(e -> showInsertLocationForm(district));
		item7.setOnAction(e -> showUpdateLocationForm(district));
		item8.setOnAction(e -> showDeleteLocationForm(district));
//		item9.setOnAction(e -> createNavigationStage2());
		item10.setOnAction(e -> createInsertMartyrStage(district));
		item11.setOnAction(e -> showUpdateMartyrStage(district));
		item12.setOnAction(e -> showDeleteMartyrStage(district));
		item13.setOnAction(e -> createSearchMartyrStage(district));

		// Layout the main scene.
		bdPane.setTop(menuBar);
		bdPane.setCenter(t1); 
		bdPane.setBottom(new Label("Pick an operation")); 

		Scene scene = new Scene(bdPane, 700, 700); 
		primaryStage.setScene(scene); 
		primaryStage.show(); // Show the primary stage.
	}

	// Displays a form to insert a new location into a specified district.
	private void showInsertLocationForm(DistrictList districtList) {
		Stage insertStage = new Stage(); 
		VBox layout = new VBox(10); 

		ComboBox<String> districtComboBox = new ComboBox<>(); 
		districtComboBox.setPromptText("Select District");
		for (DNode node = districtList.getFront(); node != null; node = node.getNext()) {
			District district = (District) node.getElement();
			districtComboBox.getItems().add(district.getDisName());
		}

		TextField locationNameField = new TextField(); 
		locationNameField.setPromptText("Location Name");
		Button insertButton = new Button("Insert"); // Button 
		Label feedbackLabel = new Label(); 

		insertButton.setOnAction(e -> {
			String districtName = districtComboBox.getValue();
			String locationName = locationNameField.getText().trim();
			if (districtName != null && !locationName.isEmpty()) {
				District selectedDistrict = districtList.findOrAddDistrict(districtName);
				if (selectedDistrict != null) {
					selectedDistrict.getLocationDlist().insertNewLocation(locationName);
					feedbackLabel.setText("Location inserted successfully.");
//					printDistrictsWithLists();
					insertStage.close();
				} else {
					feedbackLabel.setText("District not found.");
				}
			} else {
				feedbackLabel.setText("Please select a district and enter a location name.");
			}
		});

		layout.getChildren().addAll(new Label("Select District:"), districtComboBox,
				new Label("Enter New Location Name:"), locationNameField, insertButton, feedbackLabel);

		Scene scene = new Scene(layout, 400, 250);
		insertStage.setScene(scene);
		insertStage.setTitle("Insert New Location");
		insertStage.show();
	}

	// Displays a form to update an existing location within a district.
	private void showUpdateLocationForm(DistrictList districtList) {
		Stage updateStage = new Stage(); // Stage for the update form.
		VBox layout = new VBox(10); // Layout manager with vertical spacing.

		ComboBox<String> districtComboBox = new ComboBox<>(); 
		districtComboBox.setPromptText("Select District");
		for (DNode node = districtList.getFront(); node != null; node = node.getNext()) {
			District district = (District) node.getElement();
			districtComboBox.getItems().add(district.getDisName());
		}

		TextField oldLocationField = new TextField(); // Field for the old location name.
		oldLocationField.setPromptText("Old Location Name");
		TextField newLocationField = new TextField(); // Field for the new location name.
		newLocationField.setPromptText("New Location Name");
		Button updateButton = new Button("Update"); // Button 
		Label feedbackLabel = new Label();

		updateButton.setOnAction(e -> {
			String selectedDistrictName = districtComboBox.getValue();
			String oldLocation = oldLocationField.getText().trim();
			String newLocation = newLocationField.getText().trim();
			if (selectedDistrictName != null && !oldLocation.isEmpty() && !newLocation.isEmpty()) {
				District selectedDistrict = districtList.findOrAddDistrict(selectedDistrictName);
				if (selectedDistrict != null) {
					boolean updated = selectedDistrict.getLocationDlist().updateLocationName(oldLocation, newLocation);
					if (updated) {
						feedbackLabel.setText("Location '" + oldLocation + "' updated to '" + newLocation
								+ "' in district '" + selectedDistrictName + "'.");
//						printDistrictsWithLists();
					} else {
						feedbackLabel.setText(
								"Location '" + oldLocation + "' not found in district '" + selectedDistrictName + "'.");
					}
				} else {
					feedbackLabel.setText("District '" + selectedDistrictName + "' not found.");
				}
			} else {
				feedbackLabel.setText("Please select a district and fill in both old and new location names.");
			}
		});

		layout.getChildren().addAll(new Label("Select District:"), districtComboBox, new Label("Old Location Name:"),
				oldLocationField, new Label("New Location Name:"), newLocationField, updateButton, feedbackLabel);

		Scene scene = new Scene(layout, 400, 250);
		updateStage.setScene(scene);
		updateStage.setTitle("Update Location");
		updateStage.show();
	}

	// Displays a form to delete a location from a district.
	private void showDeleteLocationForm(DistrictList districtList) {
		Stage deleteStage = new Stage();
		VBox layout = new VBox(10); 

		ComboBox<String> districtComboBox = new ComboBox<>(); 
		districtComboBox.setPromptText("Select District");
		for (DNode node = districtList.getFront(); node != null; node = node.getNext()) {
			District district = (District) node.getElement();
			districtComboBox.getItems().add(district.getDisName());
		}

		TextField locationNameField = new TextField(); 
		locationNameField.setPromptText("Location Name");
		Button deleteButton = new Button("Delete"); // Button to trigger deletion.
		Label feedbackLabel = new Label();

		deleteButton.setOnAction(e -> {
			String districtName = districtComboBox.getValue();
			String locationName = locationNameField.getText().trim();
			if (districtName != null && !locationName.isEmpty()) {
				District selectedDistrict = districtList.findOrAddDistrict(districtName);
				if (selectedDistrict != null && selectedDistrict.getLocationDlist().deleteLocation(locationName)) {
					feedbackLabel.setText("Location deleted successfully.");
//					printDistrictsWithLists();
				} else {
					feedbackLabel.setText("Error deleting location.");
				}
			} else {
				feedbackLabel.setText("Please select a district and specify a location name.");
			}
		});

		layout.getChildren().addAll(new Label("Select District:"), districtComboBox,
				new Label("Location Name to Delete:"), locationNameField, deleteButton, feedbackLabel);

		Scene scene = new Scene(layout, 400, 250);
		deleteStage.setScene(scene);
		deleteStage.setTitle("Delete Location Record");
		deleteStage.show();
	}

	

	private boolean isDateFormatCorrect(String date) {
		if (date.isEmpty()) {
			return true;
		}

		String[] parts = date.split("/");// it should spilt in 3 parts

		if (parts.length != 3) {
			return false; // Date must have three parts separated by "/"
		}

		try {
			int month = Integer.parseInt(parts[0]);
			int day = Integer.parseInt(parts[1]);
			int year = Integer.parseInt(parts[2]);

			// Check if the month, day, and year values are within valid ranges
			if (month < 1 || month > 12 || day < 1 || day > 31 || year < 0 || parts[2].length() != 4) {// the month
				// should be
				// from 1 and 12
				// and day
				// between 1 and
				// 31 and year
				// have 4
				// characters
				return false;
			}

		} catch (NumberFormatException e) {
			return false;
		}

		return true; // Date correct
	}

	private void createInsertMartyrStage(DistrictList districtList) {
	    // ComboBox for district and location selection
	    ComboBox<String> districtComboBox = new ComboBox<>();
	    ComboBox<String> locationComboBox = new ComboBox<>();
	    districtComboBox.setPromptText("Select District");
	    locationComboBox.setPromptText("Select Location");

	    for (DNode node = districtList.getFront(); node != null; node = node.getNext()) {
	        District district = (District) node.getElement();
	        districtComboBox.getItems().add(district.getDisName());
	    }

	    districtComboBox.setOnAction(event -> {
	        locationComboBox.getItems().clear();
	        String selectedDistrict = districtComboBox.getValue();
	        District district = districtList.findDistrictByName(selectedDistrict);
	        if (district != null) {
	            for (SNode node = district.getLocationDlist().getFront(); node != null; node = node.getNext()) {
	                Location location = (Location) node.getElement();
	                locationComboBox.getItems().add(location.getLocName());
	            }
	        }
	    });

	    TextField nameField = new TextField();
	    TextField ageField = new TextField();
	    TextField genderField = new TextField();
	    TextField dateOfDeathField = new TextField(); 

	    Button insertButton = new Button("Insert");
	    insertButton.setOnAction(e -> {
	        String districtName = districtComboBox.getValue();
	        String locationName = locationComboBox.getValue();
	        String name = nameField.getText();
	        byte age;
	        try {
	            age = Byte.parseByte(ageField.getText());
	        } catch (NumberFormatException ex) {
	            FeedBack.setText("Invalid Age: Please enter a valid age.");
	            return;
	        }
	        String genderInput = genderField.getText().trim().toUpperCase();
	    	if (!genderInput.equals("M") && !genderInput.equals("F")) {
	    	    FeedBack.setText("Invalid Gender: Please enter M or F.");
	    	    return; 
	    	}
	    	char gender = genderInput.charAt(0); // Get the first character now that it's validated
	        String dateOfDeath = dateOfDeathField.getText(); // Capture date of death
	    	if (!isDateFormatCorrect(dateOfDeath)) {
	    		FeedBack.setText("Invalid Date: Please enter a date in the format MM/DD/YYYY.");
	    		return;
	    	}
	    	
	    	
	    	


	        // Create and add the martyr
	        Martyr newMartyr = new Martyr(name, dateOfDeath, age, locationName, districtName, gender);
	        addMartyrToLocation(districtList, districtName, locationName, newMartyr);

	        FeedBack.setText("Martyr record inserted successfully.");
//	        printDistrictsWithLists();

	        // Clear fields after insertion
	        nameField.clear();
	        ageField.clear();
	        genderField.clear();
	        dateOfDeathField.clear();
	    });

	    VBox insertLayout = new VBox(10);
	    insertLayout.getChildren().addAll(new Label("Select District:"), districtComboBox,
	                                      new Label("Select Location:"), locationComboBox,
	                                      new Label("Name:"), nameField, new Label("Age:"), ageField,
	                                      new Label("Gender (M/F):"), genderField,
	                                      new Label("Date of Death (MM/DD/YYYY):"), dateOfDeathField,
	                                      insertButton,FeedBack);

	    // Set up the scene and stage
	    Stage insertStage = new Stage();
	    insertStage.setTitle("Insert New Martyr Record");
	    insertStage.setScene(new Scene(insertLayout, 350, 500));
	    insertStage.show();
	}

	
	private void showUpdateMartyrStage(DistrictList districtList) {
		TextField selectedOldNameField = new TextField();
		TextField selectedNewNameField = new TextField();

		ComboBox<String> districtComboBox = new ComboBox<>();
		ComboBox<String> locationComboBox = new ComboBox<>();
		Label feedbackLabel = new Label();

		districtComboBox.setPromptText("Select District");
		locationComboBox.setPromptText("Select Location");

		for (DNode node = districtList.getFront(); node != null; node = node.getNext()) {
			District district = (District) node.getElement();
			districtComboBox.getItems().add(district.getDisName());
		}

		districtComboBox.setOnAction(event -> {
			locationComboBox.getItems().clear();
			String selectedDistrict = districtComboBox.getValue();
			District district = districtList.findDistrictByName(selectedDistrict);
			if (district != null) {
				for (SNode node = district.getLocationDlist().getFront(); node != null; node = node.getNext()) {
					Location location = (Location) node.getElement();
					locationComboBox.getItems().add(location.getLocName());
				}
			}
		});

		Button updateButton = new Button("Update");
		updateButton.setOnAction(e -> {
			String districtName = districtComboBox.getValue();
			String locationName = locationComboBox.getValue();
			String martyrOldName = selectedOldNameField.getText().trim();
			String martyrNewName = selectedNewNameField.getText().trim();

			if (districtName != null && locationName != null && !martyrOldName.isEmpty() && !martyrNewName.isEmpty()) {
				District selectedDistrict = districtList.findDistrictByName(districtName);
				if (selectedDistrict != null) {
					Location selectedLocation = selectedDistrict.getLocationDlist().findLocationByName(locationName);
					if (selectedLocation != null) {
						boolean updated = selectedLocation.getMartyrlist().updateMartyrName(martyrOldName,
								martyrNewName);
						if (updated) {
							feedbackLabel.setText(
									"Martyr '" + martyrOldName + "' updated successfully to '" + martyrNewName + "'.");
//							printDistrictsWithLists();
						} else {
							feedbackLabel.setText("Martyr '" + martyrOldName + "' not found.");
						}
					} else {
						feedbackLabel.setText("Location '" + locationName + "' not found.");
					}
				} else {
					feedbackLabel.setText("District '" + districtName + "' not found.");
				}
			} else {
				feedbackLabel.setText("Please fill all fields to proceed with the update.");
			}
		});

		VBox updateLayout = new VBox(10);
		updateLayout.getChildren().addAll(new Label("Select District:"), districtComboBox,
				new Label("Select Location:"), locationComboBox, new Label("Martyr Old Name:"), selectedOldNameField,
				new Label("Martyr New Name:"), selectedNewNameField, updateButton, feedbackLabel);

		Stage updateStage = new Stage();
		updateStage.setTitle("Update Martyr Record");
		updateStage.setScene(new Scene(updateLayout, 400, 300));
		updateStage.show();
	}

	private void showDeleteMartyrStage(DistrictList districtList) {
		TextField selectedNameField = new TextField();
		ComboBox<String> districtComboBox = new ComboBox<>();
		ComboBox<String> locationComboBox = new ComboBox<>();
		Button deleteButton = new Button("Delete");
		Label feedbackLabel = new Label();

		districtComboBox.setPromptText("Select District");
		locationComboBox.setPromptText("Select Location");

		for (DNode node = district.getFront(); node != null; node = node.getNext()) {
			District district = (District) node.getElement();
			districtComboBox.getItems().add(district.getDisName());
		}

		districtComboBox.setOnAction(event -> {
			locationComboBox.getItems().clear();
			String selectedDistrict = districtComboBox.getValue();
			District district1 = district.findDistrictByName(selectedDistrict);
			if (district1 != null) {
				for (SNode node = district1.getLocationDlist().getFront(); node != null; node = node.getNext()) {
					Location location = (Location) node.getElement();
					locationComboBox.getItems().add(location.getLocName());
				}
			}
		});

		deleteButton.setOnAction(e -> {
			String districtName = districtComboBox.getValue();
			String locationName = locationComboBox.getValue();
			String martyrName = selectedNameField.getText().trim();

			if (districtName != null && locationName != null && !martyrName.isEmpty()) {
				District selectedDistrict = district.findDistrictByName(districtName);
				if (selectedDistrict != null) {
					LocationList locList = selectedDistrict.getLocationDlist();
					Location loc = locList.findLocationByName(locationName);
					if (loc != null) {
						boolean deleted = loc.getMartyrlist().deleteMartyrRecordByName(martyrName);

						if (deleted) {
							feedbackLabel.setText("Martyr '" + martyrName + "' deleted successfully.");
							printDistrictsWithLists();
						} else {
							feedbackLabel.setText("Martyr '" + martyrName + "' not found.");
						}
					} else {
						feedbackLabel.setText("Location '" + locationName + "' not found.");
					}
				} else {
					feedbackLabel.setText("District '" + districtName + "' not found.");
				}
			} else {
				feedbackLabel.setText("Please fill all fields to proceed with deletion.");
			}
		});

		VBox layout = new VBox(10);
		layout.getChildren().addAll(new Label("Select District:"), districtComboBox, new Label("Select Location:"),
				locationComboBox, new Label("Martyr Name to Delete:"), selectedNameField, deleteButton, feedbackLabel);

		Stage deleteStage = new Stage();
		deleteStage.setTitle("Delete Martyr Record");
		deleteStage.setScene(new Scene(layout, 400, 300));
		deleteStage.show();
	}

	private void createSearchMartyrStage(DistrictList districtList) {
		// Text field for entering search input
		TextField searchField = new TextField();

		ComboBox<String> districtComboBox = new ComboBox<>();
		districtComboBox.setPromptText("Select District");
		FeedBack.setText("Filling district ComboBox");
		for (DNode node = districtList.getFront(); node != null; node = node.getNext()) {
			District district = (District) node.getElement();
			districtComboBox.getItems().add(district.getDisName());
			FeedBack.setText("Added district: " + district.getDisName());
		}

		TableView searchResultsTable = new TableView<>();
		TableColumn nameColumn = new TableColumn("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

		TableColumn ageColumn = new TableColumn("Age");
		ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));

		TableColumn genderColumn = new TableColumn("Gender");
		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));

		TableColumn locationColumn = new TableColumn("Location");
		locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));

		// Add columns to TableView
		searchResultsTable.getColumns().addAll(nameColumn, ageColumn, genderColumn, locationColumn);

		// Button to trigger search
		Button searchButton = new Button("Search");
		searchButton.setOnAction(e -> {
			String searchInput = searchField.getText();
			String selectedDistrict = districtComboBox.getValue();
			FeedBack.setText("Search initiated for: " + searchInput + " in district: " + selectedDistrict);
			searchResultsTable.getItems().clear(); // Clear previous search results
			if (selectedDistrict != null && !searchInput.isEmpty()) {
				searchMartyrByNamePart(searchInput, selectedDistrict, searchResultsTable); // Adjusted method call
			} else {
				FeedBack.setText("Please select a district and enter a name to search.");
			}
		});

		VBox searchLayout = new VBox(10); // Vertical box with 10 pixels spacing
		searchLayout.getChildren().addAll(new Label("Search:"), searchField, new Label("Select District:"),
				districtComboBox, searchButton, FeedBack, searchResultsTable // Add TableView to layout
				);

		Stage searchStage = new Stage();
		searchStage.setTitle("Search Martyr Record");
		searchStage.setScene(new Scene(searchLayout, 600, 400)); // Increased width and height
		searchStage.show();
	}

	private void searchMartyrByNamePart(String searchInput, String districtName, TableView searchResultsTable) {
		DNode currentDistrict = district.findDistrictNode(districtName);
		if (currentDistrict == null) {
			FeedBack.setText("District '" + districtName + "' does not exist.\n");
			return;
		}

		FeedBack.setText("District found: " + districtName + "\n");

		LocationList locationList = ((District) currentDistrict.getElement()).getLocationDlist();
		if (locationList == null || locationList.getFront() == null) {
			FeedBack.setText("No locations available in district: " + districtName + "\n");
			return;
		}

		SNode currentLocation = locationList.getFront();
		while (currentLocation != null) {
			Location location = (Location) currentLocation.getElement();
			FeedBack.setText("Checking location: " + location.getLocName() + "\n");

			if (location.getMartyrlist() == null || location.getMartyrlist().getMartyrsFront() == null) {
				FeedBack.setText("No martyrs available in district"+ "\n");
			} else {
				SNode currentMartyr = location.getMartyrlist().getMartyrsFront();
				boolean found = false;
				while (currentMartyr != null) {
					Martyr martyr = (Martyr) currentMartyr.getElement();
					if (martyr.getName().toLowerCase().contains(searchInput.toLowerCase())) {
						searchResultsTable.getItems().add(martyr);
						FeedBack.setText(
								"Found martyr: " + martyr.getName()+ "\n");
						found = true;
					}
					currentMartyr = currentMartyr.getNext();
				}
				if (!found) {
					FeedBack.setText(
							"No match for '" + searchInput + "' found "+ "\n");
				}
			}
			currentLocation = currentLocation.getNext();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}
