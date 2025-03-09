//package ProjectPhase2;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.util.ArrayList;
//import java.util.LinkedList;
//import java.util.Scanner;
//
//import ProjectPhase1.DNode;
//import ProjectPhase1.District;
//import ProjectPhase1.DistrictList;
//import ProjectPhase1.Location;
//import ProjectPhase1.LocationList;
//import ProjectPhase1.Martyr;
//import ProjectPhase1.SNode;
//import Queues.LinkedListQueue;
//import Stack.LinkedListStack;
//import javafx.application.Application;
//import javafx.scene.Node;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.control.ButtonBase;
//import javafx.scene.control.ComboBox;
//import javafx.scene.control.Label;
//import javafx.scene.control.Menu;
//import javafx.scene.control.MenuBar;
//import javafx.scene.control.MenuItem;
//import javafx.scene.control.TableColumn;
//import javafx.scene.control.TableView;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.StackPane;
//import javafx.scene.layout.VBox;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//
//public class GUI extends Application {
//
//    private MenuBar menuBar; // Menu bar for the application
//    private Menu menuDistrict, menuLocation, menuMartyr; // Menus for District, Location, and Martyr
//    private MenuItem menuItemInsert, menuItemUpdate, menuItemDelete; // Common MenuItems
//    private BorderPane mainLayout; // Main layout for the application after file selection
//    private FileChooser fileChooser; // File chooser for selecting the initial file
//   private  Label FeedBack = new Label(); // Label for feedback messages
//   
//   private TextArea t1=new TextArea();
//   private DistrictTree districtTree=new DistrictTree();
//   public static StringBuilder sb=new StringBuilder();
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("File Chooser Stage");
//        setupInitialFileChooser(primaryStage);
//    }
//
//    private void setupInitialFileChooser(Stage primaryStage) {
//        fileChooser = new FileChooser();
//        fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop/DATA STRUCTURE/Data Structure/Project Phase2"));
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));
//
//        Button btOpen = new Button("Open File Browser");
//        btOpen.setOnAction(e -> {
//            File selectedFile = fileChooser.showOpenDialog(primaryStage);
//            if (selectedFile != null) {
//                openMenuStage(primaryStage, selectedFile);
//            }
//        });
//
//        StackPane root = new StackPane();
//        root.getChildren().add(btOpen);
//        Scene scene = new Scene(root, 300, 250);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }
//    
//    
//    public StringBuilder readCSV(File file, DistrictTree districtTree) {
//        StringBuilder log = new StringBuilder();
//        try (Scanner scanner = new Scanner(file)) {
//            boolean skipHeaders = true;
//            while (scanner.hasNextLine()) {
//                String line = scanner.nextLine();
//                if (skipHeaders) {
//                    skipHeaders = false;
//                    continue;
//                }
//                String[] parts = line.split(",");
//                if (parts.length >= 6) {
//                    String districtName = parts[4].trim();
//                    String locationName = parts[3].trim();
//                    String dateOfDeath = parts[1].trim();
//                    String name = parts[0].trim();
//                    int age = parts[2].trim().isEmpty() ? -1 : Integer.parseInt(parts[2].trim());
//                    char gender = parts[5].trim().charAt(0);
//
//                    MartyrPH2 martyr = new MartyrPH2(name, age, gender);
//                    StringBuilder path = new StringBuilder();
//                    districtTree.insert(districtName, locationName, dateOfDeath, martyr, path);
//                    log.append(path.toString()).append("\n");
//                } else {
//                    log.append("Skipping incomplete or malformed line: ").append(line).append("\n");
//                }
//            }
//        } catch (FileNotFoundException e) {
//            log.append("File not found: ").append(e.getMessage()).append("\n");
//        } catch (NumberFormatException e) {
//            log.append("Number format error: ").append(e.getMessage()).append("\n");
//        }
//        //System.out.println(districtTree.getRoot().getDistrict().getLocationTree().getRoot().getLocation().getMartyrDateTree().getRoot().getMartyrs().getHead());
//        return log;
//    }
//
//    public void printDistrictsWithLists(TextArea t1) {
//        t1.setText("");
//        DistrictNode currentDistrict = districtTree.getRoot(); // Assuming districtTree is an instance of your DistrictTree class
//        if (currentDistrict == null) {
//            t1.setText("No districts available.");
//            System.out.println("DEBUG: No districts available.");
//            return;
//        }
//        StringBuilder sb = new StringBuilder();
//        printDistrictsRecursive(currentDistrict, sb);
//        t1.setText(sb.toString());
//    }
//
//    private void printDistrictsRecursive(DistrictNode node, StringBuilder sb) {
//        if (node != null) {
//            printDistrictsRecursive(node.getLeft(), sb); // Traverse left
//            String dist = node.getDistrictName();
//            sb.append("\nDistrict: ").append(dist);
//            System.out.println("DEBUG: Printing District: " + dist);
//
//            LocationNode currentLocation = node.getLocationTree().getRoot();
//            if (currentLocation == null) {
//                sb.append("\n\tNo locations available\n");
//                System.out.println("DEBUG: No locations available for district: " + node.getDistrictName());
//            } else {
//                printLocationsRecursive(currentLocation, sb);
//            }
//            printDistrictsRecursive(node.getRight(), sb); // Traverse right
//        }
//    }
//
//    private void printLocationsRecursive(LocationNode node, StringBuilder sb) {
//        if (node != null) {
//            printLocationsRecursive(node.getLeft(), sb); // Traverse left
//            String loc = node.getLocationName();
//            sb.append("\n\t-->Location: ").append(loc).append("\n");
//            System.out.println("DEBUG: Printing Location: " + loc);
//
//            DateNode currentDateNode = node.getMartyrdatetree().getRoot();
//            if (currentDateNode == null) {
//                sb.append("\t\tNo martyrs listed\n");
//                System.out.println("DEBUG: No martyrs listed for location: " + node.getLocationName());
//            } else {
//                printMartyrsRecursive(currentDateNode, sb);
//            }
//            printLocationsRecursive(node.getRight(), sb); // Traverse right
//        }
//    }
//
//    private void printMartyrsRecursive(DateNode node, StringBuilder sb) {
//        if (node != null) {
//            printMartyrsRecursive(node.getLeft(), sb); // Traverse left
//            sb.append("\t\tDate: ").append(node.getDate()).append("\n");
//            System.out.println("DEBUG: Printing Date: " + node.getDate());
//
//            MartyrNode currentMartyrNode = node.getMartyrs().getHead();
//            if (currentMartyrNode == null) {
//                sb.append("\t\t\tNo martyrs listed\n");
//                System.out.println("DEBUG: No martyrs listed for date: " + node.getDate());
//            } else {
//                while (currentMartyrNode != null) {
//                    MartyrPH2 martyr = currentMartyrNode.getMartyr();
//                    sb.append("\t\t\tMartyr: ").append(martyr.getName()).append(", Age: ").append(martyr.getAge())
//                            .append(", Gender: ").append(martyr.getGender()).append("\n");
//                    System.out.println("DEBUG: Printing Martyr: " + martyr.getName() + ", Age: " + martyr.getAge()
//                            + ", Gender: " + martyr.getGender());
//                    currentMartyrNode = currentMartyrNode.getNext();
//                }
//            }
//            printMartyrsRecursive(node.getRight(), sb); // Traverse right
//        }
//    }
//
//
//
//
//
//
//
//
//
//   
//
// 
//
//    private void openMenuStage(Stage primaryStage, File selectedFile) {
//        mainLayout = new BorderPane();
//        menuBar = new MenuBar(); // Initialize the MenuBar here
//        
//       
//
//        // Read CSV and populate the table directly in the reading method
//     // Assuming t1 is your TextArea and selectedFile is the file selected by the user
//        StringBuilder result = readCSV(selectedFile, districtTree);
//        t1.setText(result.toString());
//        Button viewTreeButton = new Button("View Districts");
//        viewTreeButton.setOnAction(e -> printDistrictsWithLists( t1));
//
//
//        Button checkRootChildrenButton = new Button("Check Root Children");
//        checkRootChildrenButton.setOnAction(event -> updateTextAreaWithRootChildren());
//       VBox vbox=new  VBox(10);
//       vbox.getChildren().addAll(viewTreeButton,checkRootChildrenButton);
//       
//
//        Menu menuDistrict = new Menu("District");
//        Menu menuLocation = new Menu("Location");
//        Menu menuMartyr = new Menu("Martyr");
//
//        // District Menu Items
//        MenuItem insertDistrict = new MenuItem("Insert District");
//        MenuItem updateDistrict = new MenuItem("Update District");
//        MenuItem deleteDistrict = new MenuItem("Delete District");
//        MenuItem navigateDistricts = new MenuItem("Navigate Districts");
//        menuDistrict.getItems().addAll(insertDistrict, updateDistrict, deleteDistrict, navigateDistricts);
//
//        // Location Menu Items
//        MenuItem insertLocation = new MenuItem("Insert Location");
//        MenuItem updateLocation = new MenuItem("Update Location");
//        MenuItem deleteLocation = new MenuItem("Delete Location");
//        MenuItem navigateLocations = new MenuItem("Navigate Locations");
//        menuLocation.getItems().addAll(insertLocation, updateLocation, deleteLocation, navigateLocations);
//
//        // Martyr Menu Items
//        MenuItem navigateMartyrs = new MenuItem("Navigate Martyrs");
//        MenuItem insertMartyr = new MenuItem("Insert Martyr");
//        MenuItem updateDeleteMartyr = new MenuItem("Update Martyr");
//        MenuItem deleteMartyr =new MenuItem("Delete Martyr");
//        MenuItem searchMartyrs = new MenuItem("Search Martyrs");
//        menuMartyr.getItems().addAll(navigateMartyrs, insertMartyr,deleteMartyr, updateDeleteMartyr, searchMartyrs);
//        menuBar.getMenus().addAll(menuDistrict, menuLocation, menuMartyr);
//
//        mainLayout.setTop(menuBar);
//        mainLayout.setCenter(t1); // Set TextArea in the center of main layout
//        mainLayout.setBottom(vbox);
//        
//        insertDistrict.setOnAction(e -> showInsertDistrictForm());
//        updateDistrict.setOnAction(e -> showUpdateDistrictForm());
//        deleteDistrict.setOnAction(e -> showDeleteDistrictForm());
//        navigateDistricts.setOnAction(e -> navigateDistricts());
//
//        insertLocation.setOnAction(e -> showInsertLocationForm());
//        updateLocation.setOnAction(e -> showUpdateLocationForm());
//        deleteLocation.setOnAction(e -> showDeleteLocationForm());
//        navigateLocations.setOnAction(e -> navigateLocations());
//
//        navigateMartyrs.setOnAction(e -> navigateMartyrs());
//        insertMartyr.setOnAction(e -> createInsertMartyrStage());
//        deleteMartyr.setOnAction(e-> showDeleteMartyrStage());
//        updateDeleteMartyr.setOnAction(e -> showUpdateMartyrStage());
//        searchMartyrs.setOnAction(e -> createSearchMartyrStage());
//    
//
//        Scene scene = new Scene(mainLayout, 765, 650);
//        primaryStage.setScene(scene);
//        primaryStage.show();
//     // displayCompleteTreeStructure();
//
//    }
//
//    private void displayCompleteTreeStructure() {
//        printDistrictsWithLists( t1);
//    }
//
//	private void updateTextAreaWithRootChildren() {
//        DistrictNode root = districtTree.getRoot();
//        DistrictNode leftChild = root != null ? root.getLeft() : null;
//        DistrictNode rightChild = root != null ? root.getRight() : null;
//
//        String leftChildName = leftChild == null ? "None" : leftChild.getDistrictName();
//        String rightChildName = rightChild == null ? "None" : rightChild.getDistrictName();
//
//        t1.setText("Root's Left Child: " + leftChildName + "\nRoot's Right Child: " + rightChildName);
//    }
//
//	
//	private void showInsertDistrictForm() {
//        Stage insertStage = new Stage();
//        VBox layout = new VBox(10);
//
//        TextField districtNameField = new TextField();
//        districtNameField.setPromptText("District Name");
//
//        Label feedbackLabel = new Label(); // Label for displaying feedback to the user
//
//        Button insertButton = new Button("Insert");
//        insertButton.setOnAction(e -> {
//            String districtName = districtNameField.getText().trim();
//            if (!districtName.isEmpty()) {
//                StringBuilder path = new StringBuilder();
//                districtTree.insert(districtName, path); 
//                feedbackLabel.setText("District added successfully. Path: " + path.toString());
//                districtNameField.clear();
//                printDistrictsWithLists( t1);
//                insertStage.close();
//            } else {
//                feedbackLabel.setText("District name cannot be empty.");
//                districtNameField.clear();
//            }
//        });
//
//
//
//        layout.getChildren().addAll(new Label("Enter New District Name:"), districtNameField, insertButton, feedbackLabel);
//        Scene scene = new Scene(layout, 300, 200);
//        insertStage.setScene(scene);
//        insertStage.setTitle("Insert New District");
//        insertStage.show();
//    }
//    
//
//   
//	private void showUpdateDistrictForm() {
//	    Stage updateStage = new Stage(); 
//	    VBox layout = new VBox(10);
//
//	    ComboBox<String> districtNameComboBox = new ComboBox<>();
//	    districtNameComboBox.setPromptText("Select Existing District");
//	 // Assuming you have a method in DistrictTree to get all district names
//	    ArrayList<String> districtNames = districtTree.getAllDistrictNames(); // Directly accessing
//	    districtNameComboBox.getItems().setAll(districtNames); // Directly setting items
//	    TextField newDistrictNameField = new TextField();
//	    newDistrictNameField.setPromptText("New District Name");
//
//	    Button updateButton = new Button("Update");
//	    updateButton.setOnAction(e -> {
//	        String oldName = districtNameComboBox.getValue();
//	        String newName = newDistrictNameField.getText().trim();
//	        if (oldName != null && !newName.isEmpty()) {
//	            boolean updated = districtTree.updateDistrictName(oldName, newName);
//	            if (updated) {
//	                FeedBack.setText("District name updated successfully.");
//	                printDistrictsWithLists( t1);
//	                updateStage.close(); 
//	            } else {
//	                FeedBack.setText("Update failed.");
//	            }
//	        } else {
//	            FeedBack.setText("Please select a district and enter a new name.");
//	        }
//	    });
//
//	    layout.getChildren().addAll(new Label("Select District to Update:"), districtNameComboBox,
//	            new Label("Enter New District Name:"), newDistrictNameField, updateButton, FeedBack);
//	    Scene scene = new Scene(layout, 500, 250);
//	    updateStage.setScene(scene);
//	    updateStage.setTitle("Update District Record");
//	    updateStage.show();
//	}
//	private void showDeleteDistrictForm() {
//	    Stage deleteStage = new Stage(); 
//	    VBox layout = new VBox(10);
//
//	    ComboBox<String> districtNameComboBox = new ComboBox<>();
//	    districtNameComboBox.setPromptText("Select District to Delete");
//	    ArrayList<String> districtNames = districtTree.getAllDistrictNames(); // Directly accessing
//	    districtNameComboBox.getItems().setAll(districtNames); // Directly setting items
//
//	    Button deleteButton = new Button("Delete");
//	    deleteButton.setOnAction(e -> {
//	        String districtName = districtNameComboBox.getValue();
//	        if (districtName != null) {
//	            boolean deleted = districtTree.deleteDistrict(districtName);
//	            if (deleted) {
//	                FeedBack.setText("District deleted successfully.");
//	                printDistrictsWithLists( t1);
//	                deleteStage.close(); 
//	            } else {
//	                FeedBack.setText("District not found or could not be deleted.");
//	            }
//	        } else {
//	            FeedBack.setText("Please select a district to delete.");
//	        }
//	    });
//
//	    layout.getChildren().addAll(new Label("Select District to Delete:"), districtNameComboBox, deleteButton, FeedBack);
//	    Scene scene = new Scene(layout, 500, 200);
//	    deleteStage.setScene(scene);
//	    deleteStage.setTitle("Delete District Record");
//	    deleteStage.show();
//	}
//
//    
//    
//    
//    
//    
//    
//
//    private void showInsertLocationForm() {
//		Stage insertStage = new Stage(); 
//		VBox layout = new VBox(10); 
//
//		ComboBox<String> districtComboBox = new ComboBox<>(); 
//		districtComboBox.setPromptText("Select District");
//		 ArrayList<String> districtNames = districtTree.getAllDistrictNames(); // Directly accessing
//		 districtComboBox.getItems().setAll(districtNames); // Directly setting items
//		 
//		 
//
//
//		TextField locationNameField = new TextField(); 
//		locationNameField.setPromptText("Location Name");
//		Button insertButton = new Button("Insert"); // Button 
//		Label feedbackLabel = new Label(); 
//
////		insertButton.setOnAction(e -> {
////		    String districtName = districtComboBox.getValue();
////		    String locationName = locationNameField.getText().trim();
////		    if (districtName != null && !locationName.isEmpty()) {
////		        boolean inserted = districtTree.insertLocation(districtName, locationName);
////		        if (inserted) {
////		            feedbackLabel.setText("Location inserted successfully.");
////	                printAllDistricts(districtTree.getRoot(), t1); // Refresh the displayed tree structure
////		        } else {
////		            feedbackLabel.setText("District not found.");
////		        }
////		    } else {
////		        feedbackLabel.setText("Please select a district and enter a location name.");
////		    }
////		});
//
//
//		layout.getChildren().addAll(new Label("Select District:"), districtComboBox,
//				new Label("Enter New Location Name:"), locationNameField, insertButton, feedbackLabel);
//
//		Scene scene = new Scene(layout, 400, 250);
//		insertStage.setScene(scene);
//		insertStage.setTitle("Insert New Location");
//		insertStage.show();
//	}
//
//    private void showUpdateLocationForm() {
//        Stage updateStage = new Stage();
//        VBox layout = new VBox(10);
//
//        ComboBox<String> districtComboBox = new ComboBox<>();
//        districtComboBox.setPromptText("Select District");
//        districtComboBox.getItems().setAll(districtTree.getAllDistrictNames());
//
//        ComboBox<String> locationComboBox = new ComboBox<>();
//        locationComboBox.setPromptText("Select Location");
//
//
//        // Update location ComboBox when a district is selected
//        districtComboBox.setOnAction(e -> {
//            String selectedDistrict = districtComboBox.getValue();
//            DistrictNode districtNode = districtTree.findDistrict(selectedDistrict);
//            if (districtNode != null) {
//                locationComboBox.getItems().setAll(districtNode.getLocationTree().getAllLocationNames());
//            } else {
//                locationComboBox.getItems().clear();
//            }
//        });
//
//        TextField newLocationNameField = new TextField();
//        newLocationNameField.setPromptText("New Location Name");
//
//        Button updateButton = new Button("Update");
//        Label feedbackLabel = new Label();
//
//        updateButton.setOnAction(e -> {
//            String selectedDistrict = districtComboBox.getValue();
//            String oldLocation = locationComboBox.getValue();
//            String newLocation = newLocationNameField.getText().trim();
//            if (selectedDistrict != null && oldLocation != null && !newLocation.isEmpty()) {
//                boolean updated = districtTree.updateLocationName(selectedDistrict, oldLocation, newLocation);
//                if (updated) {
//                    feedbackLabel.setText("Location updated successfully.");
//                } else {
//                    feedbackLabel.setText("Failed to update location.");
//                }
//            } else {
//                feedbackLabel.setText("Please fill in all fields.");
//            }
//        });
//
//        layout.getChildren().addAll(
//            new Label("Select District:"), districtComboBox,
//            new Label("Select Location:"), locationComboBox,
//            new Label("Enter New Location Name:"), newLocationNameField,
//            updateButton, feedbackLabel
//        );
//
//        Scene scene = new Scene(layout, 400, 250);
//        updateStage.setScene(scene);
//        updateStage.setTitle("Update Location");
//        updateStage.show();
//    }
//    private void showDeleteLocationForm() {
//        Stage deleteStage = new Stage();
//        VBox layout = new VBox(10);
//
//        ComboBox<String> districtComboBox = new ComboBox<>();
//        districtComboBox.setPromptText("Select District");
//        districtComboBox.getItems().setAll(districtTree.getAllDistrictNames());
//
//        ComboBox<String> locationComboBox = new ComboBox<>();
//        locationComboBox.setPromptText("Select Location");
//
//        // Update location ComboBox when a district is selected
//        districtComboBox.setOnAction(e -> {
//            String selectedDistrict = districtComboBox.getValue();
//            DistrictNode districtNode = districtTree.findDistrict(selectedDistrict);
//            if (districtNode != null) {
//            	locationComboBox.getItems().setAll(districtNode.getLocationTree().getAllLocationNames());
//            	} else {
//                locationComboBox.getItems().clear();
//            }
//        });
//
//        Button deleteButton = new Button("Delete");
//        Label feedbackLabel = new Label();
//
//        deleteButton.setOnAction(e -> {
//            String selectedDistrict = districtComboBox.getValue();
//            String locationName = locationComboBox.getValue();
//            if (selectedDistrict != null && locationName != null) {
//                boolean deleted = districtTree.deleteLocation(selectedDistrict, locationName);
//                if (deleted) {
//                    feedbackLabel.setText("Location deleted successfully.");
//                } else {
//                    feedbackLabel.setText("Failed to delete location.");
//                }
//            } else {
//                feedbackLabel.setText("Please select a district and a location.");
//            }
//        });
//
//        layout.getChildren().addAll(
//            new Label("Select District:"), districtComboBox,
//            new Label("Select Location:"), locationComboBox,
//            new Label("Location Name to Delete:"), deleteButton, feedbackLabel
//        );
//
//        Scene scene = new Scene(layout, 400, 250);
//        deleteStage.setScene(scene);
//        deleteStage.setTitle("Delete Location Record");
//        deleteStage.show();
//    }
//
// 	
// 	
// 	
// 	
// 	
// 	
//    
//    
//    
//    private void createInsertMartyrStage() {
//	    // ComboBox for district and location selection
//	    ComboBox<String> districtComboBox = new ComboBox<>();
//	    ComboBox<String> locationComboBox = new ComboBox<>();
//	    districtComboBox.setPromptText("Select District");
//	    locationComboBox.setPromptText("Select Location");
//
////	    for (DNode node = districtList.getFront(); node != null; node = node.getNext()) {
////	        District district = (District) node.getElement();
////	        districtComboBox.getItems().add(district.getDisName());
////	    }
//
//	    districtComboBox.setOnAction(event -> {
//	        locationComboBox.getItems().clear();
//	        String selectedDistrict = districtComboBox.getValue();
////	        District district = districtList.findDistrictByName(selectedDistrict);
////	        if (district != null) {
////	            for (SNode node = district.getLocationDlist().getFront(); node != null; node = node.getNext()) {
////	                Location location = (Location) node.getElement();
////	                locationComboBox.getItems().add(location.getLocName());
////	            }
//	        //}
//	    });
//
//	    TextField nameField = new TextField();
//	    TextField ageField = new TextField();
//	    TextField genderField = new TextField();
//	    TextField dateOfDeathField = new TextField(); 
//
//	    Button insertButton = new Button("Insert");
//	    insertButton.setOnAction(e -> {
//	        String districtName = districtComboBox.getValue();
//	        String locationName = locationComboBox.getValue();
//	        String name = nameField.getText();
//	        byte age;
//	        try {
//	            age = Byte.parseByte(ageField.getText());
//	        } catch (NumberFormatException ex) {
//	            FeedBack.setText("Invalid Age: Please enter a valid age.");
//	            return;
//	        }
//	        String genderInput = genderField.getText().trim().toUpperCase();
//	    	if (!genderInput.equals("M") && !genderInput.equals("F")) {
//	    	    FeedBack.setText("Invalid Gender: Please enter M or F.");
//	    	    return; 
//	    	}
//	    	char gender = genderInput.charAt(0); // Get the first character now that it's validated
//	        String dateOfDeath = dateOfDeathField.getText(); // Capture date of death
//	    	if (!isDateFormatCorrect(dateOfDeath)) {
//	    		FeedBack.setText("Invalid Date: Please enter a date in the format MM/DD/YYYY.");
//	    		return;
//	    	}
//	    	
//	    	
//	    	
//
//
//	        // Create and add the martyr
//	        Martyr newMartyr = new Martyr(name, dateOfDeath, age, locationName, districtName, gender);
////	        addMartyrToLocation(districtList, districtName, locationName, newMartyr);
//
//	        FeedBack.setText("Martyr record inserted successfully.");
////	        printDistrictsWithLists();
//
//	        // Clear fields after insertion
//	        nameField.clear();
//	        ageField.clear();
//	        genderField.clear();
//	        dateOfDeathField.clear();
//	    });
//
//	    VBox insertLayout = new VBox(10);
//	    insertLayout.getChildren().addAll(new Label("Select District:"), districtComboBox,
//	                                      new Label("Select Location:"), locationComboBox,
//	                                      new Label("Name:"), nameField, new Label("Age:"), ageField,
//	                                      new Label("Gender (M/F):"), genderField,
//	                                      new Label("Date of Death (MM/DD/YYYY):"), dateOfDeathField,
//	                                      insertButton,FeedBack);
//
//	    // Set up the scene and stage
//	    Stage insertStage = new Stage();
//	    insertStage.setTitle("Insert New Martyr Record");
//	    insertStage.setScene(new Scene(insertLayout, 350, 500));
//	    insertStage.show();
//	}
//    private boolean isDateFormatCorrect(String date) {
//		if (date.isEmpty()) {
//			return true;
//		}
//
//		String[] parts = date.split("/");// it should spilt in 3 parts
//
//		if (parts.length != 3) {
//			return false; // Date must have three parts separated by "/"
//		}
//
//		try {
//			int month = Integer.parseInt(parts[0]);
//			int day = Integer.parseInt(parts[1]);
//			int year = Integer.parseInt(parts[2]);
//
//			// Check if the month, day, and year values are within valid ranges
//			if (month < 1 || month > 12 || day < 1 || day > 31 || year < 0 || parts[2].length() != 4) {// the month
//				// should be
//				// from 1 and 12
//				// and day
//				// between 1 and
//				// 31 and year
//				// have 4
//				// characters
//				return false;
//			}
//
//		} catch (NumberFormatException e) {
//			return false;
//		}
//
//		return true; // Date correct
//	}
//
//	
//	private void showUpdateMartyrStage() {
//		TextField selectedOldNameField = new TextField();
//		TextField selectedNewNameField = new TextField();
//
//		ComboBox<String> districtComboBox = new ComboBox<>();
//		ComboBox<String> locationComboBox = new ComboBox<>();
//		Label feedbackLabel = new Label();
//
//		districtComboBox.setPromptText("Select District");
//		locationComboBox.setPromptText("Select Location");
//
////		for (DNode node = districtList.getFront(); node != null; node = node.getNext()) {
////			District district = (District) node.getElement();
////			districtComboBox.getItems().add(district.getDisName());
////		}
//
//		districtComboBox.setOnAction(event -> {
//			locationComboBox.getItems().clear();
//			String selectedDistrict = districtComboBox.getValue();
////			District district = districtList.findDistrictByName(selectedDistrict);
////			if (district != null) {
////				for (SNode node = district.getLocationDlist().getFront(); node != null; node = node.getNext()) {
////					Location location = (Location) node.getElement();
////					locationComboBox.getItems().add(location.getLocName());
////				}
////			}
//		});
//
//		Button updateButton = new Button("Update");
//		updateButton.setOnAction(e -> {
//			String districtName = districtComboBox.getValue();
//			String locationName = locationComboBox.getValue();
//			String martyrOldName = selectedOldNameField.getText().trim();
//			String martyrNewName = selectedNewNameField.getText().trim();
//
//			if (districtName != null && locationName != null && !martyrOldName.isEmpty() && !martyrNewName.isEmpty()) {
//				//District selectedDistrict = districtList.findDistrictByName(districtName);
////				if (selectedDistrict != null) {
////					Location selectedLocation = selectedDistrict.getLocationDlist().findLocationByName(locationName);
////					if (selectedLocation != null) {
////						boolean updated = selectedLocation.getMartyrlist().updateMartyrName(martyrOldName,
////								martyrNewName);
////						if (updated) {
////							feedbackLabel.setText(
////									"Martyr '" + martyrOldName + "' updated successfully to '" + martyrNewName + "'.");
//////							printDistrictsWithLists();
////						} else {
////							feedbackLabel.setText("Martyr '" + martyrOldName + "' not found.");
////						}
////					} else {
////						feedbackLabel.setText("Location '" + locationName + "' not found.");
////					}
////				} else {
////					feedbackLabel.setText("District '" + districtName + "' not found.");
////				}
////			} else {
//				feedbackLabel.setText("Please fill all fields to proceed with the update.");
//			}
//		});
//
//		VBox updateLayout = new VBox(10);
//		updateLayout.getChildren().addAll(new Label("Select District:"), districtComboBox,
//				new Label("Select Location:"), locationComboBox, new Label("Martyr Old Name:"), selectedOldNameField,
//				new Label("Martyr New Name:"), selectedNewNameField, updateButton, feedbackLabel);
//
//		Stage updateStage = new Stage();
//		updateStage.setTitle("Update Martyr Record");
//		updateStage.setScene(new Scene(updateLayout, 400, 300));
//		updateStage.show();
//	}
//
//	private void showDeleteMartyrStage() {
//		TextField selectedNameField = new TextField();
//		ComboBox<String> districtComboBox = new ComboBox<>();
//		ComboBox<String> locationComboBox = new ComboBox<>();
//		Button deleteButton = new Button("Delete");
//		Label feedbackLabel = new Label();
//
//		districtComboBox.setPromptText("Select District");
//		locationComboBox.setPromptText("Select Location");
//
////		for (DNode node = district.getFront(); node != null; node = node.getNext()) {
////			District district = (District) node.getElement();
////			districtComboBox.getItems().add(district.getDisName());
////		}
//
//		districtComboBox.setOnAction(event -> {
//			locationComboBox.getItems().clear();
//			String selectedDistrict = districtComboBox.getValue();
////			District district1 = district.findDistrictByName(selectedDistrict);
////			if (district1 != null) {
////				for (SNode node = district1.getLocationDlist().getFront(); node != null; node = node.getNext()) {
////					Location location = (Location) node.getElement();
////					locationComboBox.getItems().add(location.getLocName());
////				}
//			//}
//		});
//
//		deleteButton.setOnAction(e -> {
//			String districtName = districtComboBox.getValue();
//			String locationName = locationComboBox.getValue();
//			String martyrName = selectedNameField.getText().trim();
//
//			if (districtName != null && locationName != null && !martyrName.isEmpty()) {
////				District selectedDistrict = district.findDistrictByName(districtName);
////				if (selectedDistrict != null) {
////				LocationList locList = selectedDistrict.getLocationDlist();
//	//				Location loc = locList.findLocationByName(locationName);
////				if (loc != null) {
////						boolean deleted = loc.getMartyrlist().deleteMartyrRecordByName(martyrName);
////
////						if (deleted) {
////							feedbackLabel.setText("Martyr '" + martyrName + "' deleted successfully.");
////						} else {
////							feedbackLabel.setText("Martyr '" + martyrName + "' not found.");
////						}
////					} else {
////						feedbackLabel.setText("Location '" + locationName + "' not found.");
////					}
////				} 
////				else {
////					feedbackLabel.setText("District '" + districtName + "' not found.");
////				}
//			} else {
//				//feedbackLabel.setText("Please fill all fields to proceed with deletion.");
//			}
//		});
//
//		VBox layout = new VBox(10);
//		layout.getChildren().addAll(new Label("Select District:"), districtComboBox, new Label("Select Location:"),
//				locationComboBox, new Label("Martyr Name to Delete:"), selectedNameField, deleteButton, feedbackLabel);
//
//		Stage deleteStage = new Stage();
//		deleteStage.setTitle("Delete Martyr Record");
//		deleteStage.setScene(new Scene(layout, 400, 300));
//		deleteStage.show();
//	}
//	private void createSearchMartyrStage() {
//		// Text field for entering search input
//		TextField searchField = new TextField();
//
//		ComboBox<String> districtComboBox = new ComboBox<>();
//		districtComboBox.setPromptText("Select District");
//		FeedBack.setText("Filling district ComboBox");
////		for (DNode node = districtList.getFront(); node != null; node = node.getNext()) {
////			District district = (District) node.getElement();
////			districtComboBox.getItems().add(district.getDisName());
////			FeedBack.setText("Added district: " + district.getDisName());
////		}
//
//		TableView searchResultsTable = new TableView<>();
//		TableColumn nameColumn = new TableColumn("Name");
//		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
//
//		TableColumn ageColumn = new TableColumn("Age");
//		ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
//
//		TableColumn genderColumn = new TableColumn("Gender");
//		genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
//
//		TableColumn locationColumn = new TableColumn("Location");
//		locationColumn.setCellValueFactory(new PropertyValueFactory<>("Location"));
//
//		// Add columns to TableView
//		searchResultsTable.getColumns().addAll(nameColumn, ageColumn, genderColumn, locationColumn);
//
//		// Button to trigger search
//		Button searchButton = new Button("Search");
//		searchButton.setOnAction(e -> {
//			String searchInput = searchField.getText();
//			String selectedDistrict = districtComboBox.getValue();
//			FeedBack.setText("Search initiated for: " + searchInput + " in district: " + selectedDistrict);
//			searchResultsTable.getItems().clear(); // Clear previous search results
//			if (selectedDistrict != null && !searchInput.isEmpty()) {
//				searchMartyrByNamePart(); // Adjusted method call
//			} else {
//				FeedBack.setText("Please select a district and enter a name to search.");
//			}
//		});
//
//		VBox searchLayout = new VBox(10); // Vertical box with 10 pixels spacing
//		searchLayout.getChildren().addAll(new Label("Search:"), searchField, new Label("Select District:"),
//				districtComboBox, searchButton, FeedBack, searchResultsTable // Add TableView to layout
//				);
//
//		Stage searchStage = new Stage();
//		searchStage.setTitle("Search Martyr Record");
//		searchStage.setScene(new Scene(searchLayout, 600, 400)); // Increased width and height
//		searchStage.show();
//	}
//
//	private void searchMartyrByNamePart() {
////		DNode currentDistrict = district.findDistrictNode(districtName);
////		if (currentDistrict == null) {
////			FeedBack.setText("District '" + districtName + "' does not exist.\n");
////			return;
////		}
////
////		FeedBack.setText("District found: " + districtName + "\n");
////
////		LocationList locationList = ((District) currentDistrict.getElement()).getLocationDlist();
////		if (locationList == null || locationList.getFront() == null) {
////			FeedBack.setText("No locations available in district: " + districtName + "\n");
////			return;
////		}
//
////		SNode currentLocation = locationList.getFront();
////		while (currentLocation != null) {
////			Location location = (Location) currentLocation.getElement();
////			FeedBack.setText("Checking location: " + location.getLocName() + "\n");
////
////			if (location.getMartyrlist() == null || location.getMartyrlist().getMartyrsFront() == null) {
////				FeedBack.setText("No martyrs available in district"+ "\n");
////			} else {
////				SNode currentMartyr = location.getMartyrlist().getMartyrsFront();
////				boolean found = false;
////				while (currentMartyr != null) {
////					Martyr martyr = (Martyr) currentMartyr.getElement();
////					if (martyr.getName().toLowerCase().contains(searchInput.toLowerCase())) {
////						searchResultsTable.getItems().add(martyr);
////						FeedBack.setText(
////								"Found martyr: " + martyr.getName()+ "\n");
////						found = true;
////					}
////					currentMartyr = currentMartyr.getNext();
////				}
////				if (!found) {
////					FeedBack.setText(
////							"No match for '" + searchInput + "' found "+ "\n");
////				}
////			}
////			currentLocation = currentLocation.getNext();
//	//	}
//	}  
//	// Method to navigate through districts in an in-order traversal
//	public void inOrderTraversal(BSTDistrictNode root) {
//		if (root != null) {
//			// Traverse left subtree
//			inOrderTraversal(root.getLeft()); 
//
//			// Push current district to the stack
//			memoryStack.push(((District) root.getElement()).getName()); 
//
//			// Traverse right subtree
//			inOrderTraversal(root.getRight()); 
//		}
//	}
//	private void navigateDisScene(Stage primaryStage) {
//		// Organize the nodes on the scene
//		txtAreaDisNavigation.setText("");
//		txtAreaDisNavigation.setVisible(true);
//		Node lblDisNavInfo;
//		lblDisNavInfo.setText("");
//		lblDisNavInfo.setVisible(true); // Show the label
//		clear();
//		btDisNavMain.setOnAction(e -> returnMain(primaryStage));
//
//		forwardStack = new LinkedListStack();
//		LinkedListStack visitedStack = new LinkedListStack();
//
//		if (bdPane.getCenter() != vBoxNavDis) {
//			bdPane.setCenter(vBoxNavDis);
//		}
//
//		// Ensure the stack is initialized with districts in order
//		districtTree.inOrderTraversal(districtTree.getRoot());
//		forwardStack = districtTree.getMemoryStack();
//
//		if (forwardStack.isEmpty()) {
//			lblDisNavInfo.setText("There are no districts!");
//			return;
//		}
//
//		// Reverse the districts in the forwardStack and push them onto visitedStack
//		while (!forwardStack.isEmpty()) {
//			visitedStack.push(forwardStack.pop());
//		}
//
//		// Peek the first district for display
//		if (!visitedStack.isEmpty()) {
//			current = (String) visitedStack.pop();
//			lblDisNavInfo.setText("District: " + current + "\n\n" + "Number of martyrs: "
//					+ districtTree.martyrCountInLocations(current));
//		}
//
//		ButtonBase btDisNavPrev;
//		// Event handler for btDisNavPrev button
//		btDisNavPrev.setOnAction(e -> {
//			lblDisNavInfo.setVisible(true);
//			txtAreaDisNavigation.setText("");
//
//			if (!forwardStack.isEmpty()) {
//				visitedStack.push(current);
//				current = (String) forwardStack.pop();
//				lblDisNavInfo.setText("District: " + current + "\n\n" + "Number of martyrs: "
//						+ districtTree.martyrCountInLocations(current));
//			}
//		});
//
//		// Event handler for btDisNavNext button
//		btDisNavNext.setOnAction(e -> {
//			lblDisNavInfo.setVisible(true);
//			txtAreaDisNavigation.setText("");
//
//			if (!visitedStack.isEmpty()) {
//				forwardStack.push(current);
//				current = (String) visitedStack.pop();
//				lblDisNavInfo.setText("District: " + current + "\n\n" + "Number of martyrs: "
//						+ districtTree.martyrCountInLocations(current));
//			}
//		});
//
//		// Event handler for load button
//		btDisNavLoad.setOnAction(e5 -> {
//			// Create a new stage to show the district statistics
//			Stage stage = new Stage();
//			VBox vBoxDis = new VBox(20);
//			vBoxDis.setAlignment(Pos.CENTER);
//			vBoxDis.setPadding(new Insets(10, 10, 10, 10));
//
//			Label lbl = new Label();
//			lbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
//			lbl.setPadding(new Insets(10, 10, 30, 10));
//			lbl.setStyle(
//					"-fx-font-weight: bold; -fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 2;");
//			lbl.setText(current);
//
//			vBoxDis.getChildren().addAll(lbl, txtAreaDisNav);
//
//			currentDistrict = districtTree.find(new District(current));
//
//			if (currentDistrict != null) {
//				districtTree.btLoadDisNavigation(currentDistrict, txtAreaDisNav);
//			}
//
//			Scene scene = new Scene(vBoxDis, 1100, 800);
//			stage.setScene(scene);
//			stage.show();
//		});
//
//		primaryStage.setTitle("Navigate District");
//		primaryStage.show();
//	}
//	// Method that finds the next location of a district using level-by-level
//		// traversal
//		public LocationNode getNextLocationInDistrict(LocationNode root, LocationNode currentLocation) {
//			// Check if the root or currentLocation is null
//			if (root == null || currentLocation == null)
//				return null;
//
//			// Initialize a queue for level-by-level traversal
//			LinkedListQueue queue = new LinkedListQueue();
//			queue.enqueue(root);
//
//			boolean found = false;
//			while (!queue.isEmpty()) {
//				LocationNode node = (LocationNode) queue.dequeue();
//
//				// Check if the current node is the next location
//				if (found)
//					return node;
//
//				// Mark found as true if currentLocation is found in the tree
//				if (node == currentLocation)
//					found = true;
//
//				// Enqueue the left and right children if they exist
//				if (node.getLeft() != null)
//					queue.enqueue(node.getLeft());
//				if (node.getRight() != null)
//					queue.enqueue(node.getRight());
//			}
//
//			// Return null if the next location is not found
//			return null;
//		}
//
//		// Method that finds the previous location of a district using level-by-level
//		// traversal
//		public LocationNode getPrevLocationInDistrict(LocationNode root, LocationNode currentLocation) {
//			// Check if the root or currentLocation is null
//			if (root == null || currentLocation == null)
//				return null;
//
//			// Initialize a queue for level-by-level traversal
//			LinkedListQueue queue = new LinkedListQueue();
//			queue.enqueue(root);
//
//			LocationNode prev = null;
//			while (!queue.isEmpty()) {
//				int size = queue.size();
//
//				// Traverse each level of the tree
//				for (int i = 0; i < size; i++) {
//					LocationNode node = (LocationNode) queue.dequeue();
//
//					// Check if the current node is the previous location
//					if (node == currentLocation) {
//						// Return the previous location if it exists
//						if (prev != null)
//							return prev;
//						else
//							return null; // The currentLocation is the first node, no previous node
//					}
//
//					// Enqueue the left and right children if they exist
//					if (node.getLeft() != null)
//						queue.enqueue(node.getLeft());
//					if (node.getRight() != null)
//						queue.enqueue(node.getRight());
//
//					// Update prev to the current node
//					prev = node;
//				}
//			}
//
//			// Return null if the previous location is not found
//			return null;
//		}
//		private void navigateLocScene(Stage primaryStage) {
//			clear();
//			lblLocNavInfo.setVisible(false);
//			btLocNavLoad.setVisible(false);
//			btLocNavMain.setOnAction(e -> returnMain(primaryStage));
//
//			if (bdPane.getCenter() != vBoxNavLoc) {
//				bdPane.setCenter(vBoxNavLoc);
//			}
//
//			if (!vBoxNavLoc.getChildren().contains(disCmBox)) {
//				vBoxNavLoc.getChildren().clear();
//				vBoxNavLoc.getChildren().addAll(disCmBox, hBoxNavLoc2, hBoxNavLoc, btLocNavMain);
//			}
//
//			disCmBox.setOnAction(e4 -> {
//				String disName = disCmBox.getValue();
//				if (disName != null && !disName.equals("-") && !disName.isEmpty()) {
//					lblLocNavInfo.setVisible(true);
//					// Find the chosen district
//					BSTDistrictNode currentDistrict = districtTree.find(new District(disName));
//					btLocNavLoad.setVisible(true);
//
//					if (currentDistrict == null) {
//						lblLocNavInfo.setVisible(true);
//						lblLocNavInfo.setText("District not found!");
//						btLocNavLoad.setVisible(false);
//						return;
//					}
//
//					// Get the location tree for the chosen district
//					BSTLocation locationTree = currentDistrict.getHead();
//					currentLocation = locationTree.getRoot();
//
//					if (locationTree == null || locationTree.getRoot() == null) {
//						lblLocNavInfo.setVisible(true);
//						lblLocNavInfo.setText("This district has no locations!");
//						btLocNavLoad.setVisible(false);
//						return;
//					}
//
//					btLocNavLoad.setOnAction(e -> {
//						lblLocNavInfo.setVisible(true);
//						Stage stage = new Stage();
//						VBox vBoxDis = new VBox(20);
//						vBoxDis.setAlignment(Pos.CENTER);
//						vBoxDis.setPadding(new Insets(10, 10, 10, 10));
//
//						Label lbl = new Label();
//						lbl.setFont(Font.font("Times New Roman", FontWeight.BOLD, 30));
//						lbl.setPadding(new Insets(10, 10, 30, 10));
//						lbl.setStyle(
//								"-fx-font-weight: bold; -fx-background-color: transparent; -fx-border-color: red; -fx-border-width: 2;");
//						lbl.setText(((Location) currentLocation.getElement()).getName());
//
//						vBoxDis.getChildren().addAll(lbl, txtAreaDisNav);
//
//						if (currentLocation != null) {
//							districtTree.btLoadLocNavigation(currentLocation, txtAreaDisNav);
//						}
//
//						Scene scene = new Scene(vBoxDis, 1100, 800);
//						stage.setScene(scene);
//						stage.show();
//					});
//
//					lblLocNavInfo.setText("Location: " + ((Location) currentLocation.getElement()).getName()
//							+ "\n\nThe earliest date that has martyrs: "
//							+ locationTree.findEarliestDateWithMartyrs(currentLocation)
//							+ "\n\nThe latest date that has martyrs: "
//							+ locationTree.findLatestDateWithMartyrs(currentLocation)
//							+ "\n\nThe date with maximum number is: "
//							+ locationTree.findDateWithMaxMartyrs(currentLocation));
//
//					// Event handler for btDisLocNavNext button (using locationTree)
//					btDisLocNavNext.setOnAction(e -> {
//						lblLocNavInfo.setVisible(true);
//						if (currentLocation != null) {
//							BSTLocationNode nextLocation = locationTree.getNextLocationInDistrict(locationTree.getRoot(),
//									currentLocation);
//							if (nextLocation != null) {
//								currentLocation = nextLocation;
//								lblLocNavInfo.setText("Location: " + ((Location) currentLocation.getElement()).getName()
//										+ "\n\nThe earliest date that has martyrs: "
//										+ locationTree.findEarliestDateWithMartyrs(currentLocation)
//										+ "\n\nThe latest date that has martyrs: "
//										+ locationTree.findLatestDateWithMartyrs(currentLocation)
//										+ "\n\nThe date with maximum number is: "
//										+ locationTree.findDateWithMaxMartyrs(currentLocation));
//							}
//						} else
//							return;
//					});
//
//					// Event handler for btDisNavLocPrev button (using locationTree)
//					btDisNavLocPrev.setOnAction(e -> {
//						lblLocNavInfo.setVisible(true);
//						if (currentLocation != null) {
//							BSTLocationNode prevLocation = locationTree.getPrevLocationInDistrict(locationTree.getRoot(),
//									currentLocation);
//							if (prevLocation != null) {
//								currentLocation = prevLocation;
//								lblLocNavInfo.setText("Location: " + ((Location) currentLocation.getElement()).getName()
//										+ "\n\nThe earliest date that has martyrs: "
//										+ locationTree.findEarliestDateWithMartyrs(currentLocation)
//										+ "\n\nThe latest date that has martyrs: "
//										+ locationTree.findLatestDateWithMartyrs(currentLocation)
//										+ "\n\nThe date with maximum number is: "
//										+ locationTree.findDateWithMaxMartyrs(currentLocation));
//							}
//						} else
//							return;
//					});
//
//				} else {
//					lblLocNavInfo.setVisible(true);
//					lblLocNavInfo.setText("Error: Please choose a district first!");
//					btLocNavLoad.setVisible(false);
//					currentLocation = null;
//					return;
//				}
//
//			});
//
//			primaryStage.setTitle("Navigate Locations");
//			primaryStage.show();
//		}
//		private void navigateDateScene(Stage primaryStage) {
//			// Organize the nodes on the scene
//			txtAreaLocSearch.setText("");
//			txtAreaLocSearch.setVisible(true);
//			lblLocSeaInfo.setText("");
//			lblLocSeaInfo.setVisible(false); // Show the label
//			locCmBox.setDisable(true);
//			clear();
//			btLocSeaMain.setOnAction(e -> returnMain(primaryStage));
//
//			if (bdPane.getCenter() != vBoxSeaLoc) {
//				bdPane.setCenter(vBoxSeaLoc);
//			}
//
//			if (!hBoxSeaDisLoc.getChildren().contains(disCmBox)) {
//				hBoxSeaDisLoc.getChildren().add(disCmBox);
//			}
//
//			if (!hBoxSeaLoc.getChildren().contains(locCmBox)) {
//				hBoxSeaLoc.getChildren().clear();
//				hBoxSeaLoc.getChildren().addAll(lblLocSea, locCmBox);
//			}
//
//			forwardStackDates = new LinkedListStack();
//
//			// Event handler for the district combo box
//			disCmBox.setOnAction(e3 -> {
//				lblLocSeaInfo.setVisible(false);
//				String disName = disCmBox.getValue();
//
//				if (disName == null || disName.isEmpty() || disName.equals("-")) {
//					txtAreaLocSearch.setText("");
//					tableViewDatesNavigation.getColumns().clear();
//					lblLocSeaInfo.setVisible(true);
//					lblLocSeaInfo.setText("Please choose a district!");
//					locCmBox.setDisable(true);
//					return;
//				}
//				locCmBox.setDisable(false);
//
//				BSTDistrictNode disNode = districtTree.find(new District(disName));
//
//				if (disNode == null) {
//					lblLocSeaInfo.setText("The district doesn't exist!");
//					return;
//				}
//				districtTree.insertLocationsToComboBox(disName, locCmBox, lblLocInstInfo);
//
//				BSTLocation locationTree = disNode.getHead();
//
//				// Event handler for the location combo box
//				locCmBox.setOnAction(e4 -> {
//					String locName = locCmBox.getValue();
//
//					if (locName == null || locName.isEmpty() || locName.equals("-")) {
//						txtAreaLocSearch.setText("");
//						tableViewDatesNavigation.getColumns().clear();
//						lblLocSeaInfo.setVisible(true);
//						lblLocSeaInfo.setText("Please choose a location!");
//						return;
//					}
//
//					BSTLocationNode locNode = locationTree.find(new Location(locName));
//
//					if (locNode == null) {
//						txtAreaLocSearch.setText("");
//						tableViewDatesNavigation.getColumns().clear();
//						lblLocSeaInfo.setVisible(true);
//						lblLocSeaInfo.setText("The location doesn't exist!");
//						return;
//					}
//
//					BSTMartyrDate datesTree = locNode.getHead();
//
//					LinkedListStack visitedStack = new LinkedListStack();
//
//					datesTree.inOrderTraversal(datesTree.getRoot());
//
//					forwardStackDates = datesTree.getMemoryStack();
//
//					if (forwardStackDates.isEmpty()) {
//						lblLocSeaInfo.setVisible(true);
//						lblLocSeaInfo.setText("There are no dates!");
//						txtAreaLocSearch.setText("");
//						return;
//					}
//
//					// Reverse the districts in the forwardStack and push them onto visitedStack
//					while (!forwardStackDates.isEmpty()) {
//						visitedStack.push(forwardStackDates.pop());
//					}
//
//					// Peek the first date for display
//					if (!visitedStack.isEmpty()) {
//						txtAreaLocSearch.setText("");
//						lblLocSeaInfo.setText("");
//						currentDate = (String) visitedStack.pop();
//
//						BSTMartyrDateNode dateNode = datesTree.find(new MartyrDate(currentDate));
//						MartyrLinkedList martyrList = null;
//
//						if (dateNode != null) {
//							martyrList = dateNode.getHead();
//							martyrList.displayAllMartyrs(tableViewDatesNavigation);
//						} else
//							System.out.println("1663");
//
//						if (currentDate != null)
//							txtAreaLocSearch.setText("Date: " + currentDate + "\n\n" + "Average martyrs ages: "
//									+ martyrList.getMartyrAgeAvg() + "\n\nThe youngest martyr: "
//									+ martyrList.getYoungestMartyrName() + " ,Age: " + martyrList.getYoungestAge()
//									+ "\n\nThe oldest martyr: " + martyrList.getOldestMartyrName() + " ,Age: "
//									+ martyrList.getOlderAge());
//
//						tempDateNavData = txtAreaLocSearch.getText();
//					} else {
//						lblLocSeaInfo.setVisible(true);
//						lblLocSeaInfo.setText("No more previous dates available.");
//						txtAreaLocSearch.setText(tempDateNavData);
//					}
//
//					// Event handler for btDisNavPrev button
//					btDatesNavPrev.setOnAction(e -> {
//						lblLocSeaInfo.setVisible(false);
//						txtAreaLocSearch.setText("");
//
//						if (!forwardStackDates.isEmpty()) {
//							lblLocSeaInfo.setText("");
//							visitedStack.push(currentDate);
//							currentDate = (String) forwardStackDates.pop();
//
//							BSTMartyrDateNode dateNode = datesTree.find(new MartyrDate(currentDate));
//							MartyrLinkedList martyrList = null;
//
//							if (dateNode != null) {
//								martyrList = dateNode.getHead();
//								martyrList.displayAllMartyrs(tableViewDatesNavigation);
//							} else
//								System.out.println("1688");
//
//							if (currentDate != null)
//								txtAreaLocSearch.setText("Date: " + currentDate + "\n\n" + "Average martyrs ages: "
//										+ martyrList.getMartyrAgeAvg() + "\n\nThe youngest martyr: "
//										+ martyrList.getYoungestMartyrName() + " ,Age: " + martyrList.getYoungestAge()
//										+ "\n\nThe oldest martyr: " + martyrList.getOldestMartyrName() + " ,Age: "
//										+ martyrList.getOlderAge());
//							tempDateNavData = txtAreaLocSearch.getText();
//						} else {
//							// No more previous dates available
//							lblLocSeaInfo.setVisible(true);
//							lblLocSeaInfo.setText("No more previous dates available.");
//							txtAreaLocSearch.setText(tempDateNavData);
//						}
//					});
//
//					// Event handler for btDisNavNext button
//					btDatesNavNext.setOnAction(e -> {
//						lblLocSeaInfo.setVisible(false);
//						txtAreaLocSearch.setText("");
//
//						if (!visitedStack.isEmpty()) {
//							lblLocSeaInfo.setText("");
//							forwardStackDates.push(currentDate);
//							currentDate = (String) visitedStack.pop();
//							BSTMartyrDateNode dateNode = datesTree.find(new MartyrDate(currentDate));
//							MartyrLinkedList martyrList = null;
//
//							if (dateNode != null) {
//								martyrList = dateNode.getHead();
//								martyrList.displayAllMartyrs(tableViewDatesNavigation);
//							} else
//								System.out.println("1713");
//
//							if (currentDate != null)
//								txtAreaLocSearch.setText("Date: " + currentDate + "\n\n" + "Average martyrs ages: "
//										+ martyrList.getMartyrAgeAvg() + "\n\nThe youngest martyr: "
//										+ martyrList.getYoungestMartyrName() + " ,Age: " + martyrList.getYoungestAge()
//										+ "\n\nThe oldest martyr: " + martyrList.getOldestMartyrName() + " ,Age: "
//										+ martyrList.getOlderAge());
//
//							tempDateNavData = txtAreaLocSearch.getText();
//						} else {
//							// No more next dates available
//							lblLocSeaInfo.setVisible(true);
//							lblLocSeaInfo.setText("No more next dates available.");
//							txtAreaLocSearch.setText(tempDateNavData);
//						}
//					});
//
//				});
//
//			});
//
//			primaryStage.setTitle("Navigate Date");
//			primaryStage.show();
//		}
//
//	
//	
//	private void navigateLocations() {}
//
//    private void navigateMartyrs() {}
//    
//    public void saveMartyrListToFile() {}
//    private void navigateDistricts() {}
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
