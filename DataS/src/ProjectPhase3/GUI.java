package ProjectPhase3;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.io.PrintStream;


public class GUI extends Application {

    private HashTable hashTable = new HashTable(11); // Initialize the hash table with size 11

    private Button btOpen; // Button to open the file chooser
    private MenuBar menuBar; // Menu bar for the application
    private Menu menu1, menu2, menu3;
    private MenuItem item1, item2, item3, item4, item5, item7, item8, item9, item10, item11, item12, item13;
    private TextArea hashTableOutputArea; // Text area to display the hash table contents
    private CheckBox includeEmptySpotsCheckBox; // Checkbox to include empty spots in the hash table display

    private FileChooser fChooser; // File chooser for selecting CSV files
    private HashEntry currentEntry; // Current hash entry

    private BorderPane bdPane; // Main layout container
    private Label FeedBack = new Label(); // Label for feedback messages


    
        
           


    @Override
    public void start(Stage primaryStage) {
        btOpen = new Button("Open File Browser");
        fChooser = new FileChooser();

        // Set the initial directory correctly
        File initialDirectory = new File(System.getProperty("user.home"), "Desktop/First 2 years/DATA STRUCTURE/Data Structure/Project Phase3");
        if (initialDirectory.exists() && initialDirectory.isDirectory()) {
            fChooser.setInitialDirectory(initialDirectory);
        } else {
            System.out.println("Initial directory does not exist: " + initialDirectory.getAbsolutePath());
        }

        fChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"));

        btOpen.setOnAction(e -> {
            File selectedFile = fChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                readCSV(selectedFile); // Read the selected CSV file
                openMenuStage(primaryStage, selectedFile); // Open the menu stage
            }
        });

        StackPane spane = new StackPane();
        spane.getChildren().add(btOpen);
        Scene scene = new Scene(spane, 300, 250);
        primaryStage.setTitle("First Stage");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to read CSV file and populate the hash table
    private void readCSV(File file) {
        try (Scanner scanner = new Scanner(file)) {
            boolean skipHeaders = true;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (skipHeaders) {
                    skipHeaders = false;
                    continue;
                }
                String[] parts = line.split(",");

                if (parts.length < 6) {
                    System.out.println("Skipping incomplete or malformed line: " + line);
                    continue;
                }

                String martyrName = parts[0].trim();
                String dateOfDeath = parts[1].trim();
                byte age = parts[2].trim().isEmpty() ? -1 : Byte.parseByte(parts[2].trim());
                char gender = parts[5].trim().charAt(0);
                String districtName = parts[4].trim();
                String locationName = parts[3].trim();

                try {
                    LocalDate parsedDate = LocalDate.parse(dateOfDeath, DateTimeFormatter.ofPattern("M/d/yyyy"));
                    String formattedDate = parsedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

                    Martyr martyr = new Martyr(martyrName, formattedDate, age, gender);
                    insertMartyr(hashTable, martyr, districtName, locationName);

                } catch (DateTimeParseException e) {
                    System.out.println("Date parse error: " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.out.println("Number format error: " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("The file was not found: " + e.getMessage());
        }
    }

 // Method to insert a martyr into the hash table
    private void insertMartyr(HashTable hashTable, Martyr martyr, String districtName, String locationName) {
        String dateKey = martyr.getDateOfDeath(); // Get the date of death of the martyr to use as the key

        HashEntry dateRecord = (HashEntry) hashTable.get(dateKey); // Retrieve the entry from the hash table using the date key

        if (dateRecord == null) { // If no entry exists for this date
            AVLTree newTree = new AVLTree(); // Create a new AVL tree
            newTree.insert(dateKey, districtName, locationName, martyr); // Insert the martyr into the new AVL tree
            dateRecord = new HashEntry(dateKey, newTree); // Create a new hash entry with the AVL tree as its value
            hashTable.put(dateKey, dateRecord); // Insert the new entry into the hash table
        } else { // If an entry already exists for this date
            AVLTree existingTree = (AVLTree) dateRecord.getValue(); // Get the existing AVL tree from the entry
            existingTree.insert(dateKey, districtName, locationName, martyr); // Insert the martyr into the existing AVL tree
        }

        if (hashTable.isHalfFull()) { 
            hashTable.rehash(); // Rehash the hash table to maintain efficiency
        }

        refreshHashTableDisplay();//printing on textArea
    }

    // Method to show the form for inserting a new date
    private void showInsertDateForm() {
        FeedBack.setText("");
        Stage insertStage = new Stage(); 
        VBox layout = new VBox(10);

        DatePicker datePicker = new DatePicker(); // Create a date picker for selecting the date
        datePicker.setPromptText("Select Date"); 

        Button insertButton = new Button("Insert"); 
        insertButton.setOnAction(e -> {
            LocalDate selectedDate = datePicker.getValue(); // Get the selected date
            if (selectedDate != null) {
                LocalDate today = LocalDate.now(); // Get today's date
                if (selectedDate.isAfter(today)) { // Check if the selected date is after today(it should be not in the future)
                    FeedBack.setText("Selected date is invalid. Date of death cannot be in the future.");
                    return; // Exit the method without inserting the date
                }

                String dateKey = selectedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")); // Format the date
                int hashIndex = hashTable.hash(dateKey); // Calculate the hash index for the date
                HashEntry dateRecord = (HashEntry) hashTable.get(dateKey); // Check if the date already exists

                if (dateRecord == null) {
                    AVLTree avlTree = new AVLTree(); // Create a new AVL tree for the date
                    dateRecord = new HashEntry(dateKey, avlTree); // Create a new HashEntry with the AVL tree
                    hashTable.put(dateKey, dateRecord); // Insert the new entry into the hash table

                    FeedBack.setText("Date " + dateKey + " inserted successfully at index: " + hashIndex);

                    avlTree.insert(dateKey, "", "", null); // Insert an empty node into the AVL tree if needed
                } else {
                    FeedBack.setText("Date " + dateKey + " already exists at index: " + hashIndex);
                }

                refreshHashTableDisplay(); // Refresh the hash table display
            } else {
                FeedBack.setText("Please select a date."); // Provide feedback if no date is selected
            }
        });

        layout.getChildren().addAll(new Label("Select Date:"), datePicker, insertButton, FeedBack); 
        Scene scene = new Scene(layout, 300, 200); 
        insertStage.setScene(scene); 
        insertStage.setTitle("Insert New Date"); 
        insertStage.show(); 
    }

    // Method to show the form for updating a date
    private void showUpdateDateForm() {
        FeedBack.setText(" "); 
        Stage updateStage = new Stage(); 
        VBox layout = new VBox(10); 

        DatePicker oldDatePicker = new DatePicker(); // Date picker for selecting the old date
        oldDatePicker.setPromptText("Old Date"); 
        DatePicker newDatePicker = new DatePicker(); // Date picker for selecting the new date
        newDatePicker.setPromptText("New Date"); 

        Button updateButton = new Button("Update"); // Create an update button
        updateButton.setOnAction(e -> {
            LocalDate oldDate = oldDatePicker.getValue(); // Get the selected old date
            LocalDate newDate = newDatePicker.getValue(); // Get the selected new date
            if (oldDate != null && newDate != null) { // Check if both dates are selected
                if (newDate.isAfter(LocalDate.now())) { // Check if the new date is in the future
                    FeedBack.setText("Selected new date is invalid. Date of death cannot be in the future.");
                    return; 
                }
                String oldDateKey = oldDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")); // Format the old date as a string
                String newDateKey = newDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")); // Format the new date as a string

                HashEntry oldEntry = (HashEntry) hashTable.get(oldDateKey); // Get the entry for the old date
                if (oldEntry != null) { // Check if the old date entry exists
                    int oldIndex = hashTable.hash(oldDateKey); // Get the hash index for the old date
                    AVLTree oldTree = (AVLTree) oldEntry.getValue(); // Get the AVL tree for the old date

                    hashTable.remove(oldDateKey); // Remove the old date entry from the hash table

                    HashEntry newEntry = (HashEntry) hashTable.get(newDateKey); // Check if the new date key already exists
                    AVLTree newTree;
                    if (newEntry == null) {  
                        newTree = new AVLTree(); // Create a new AVL tree if the new date key does not exist
                        newEntry = new HashEntry(newDateKey, newTree); // Create a new HashEntry with the new AVL tree
                        int newIndex = hashTable.hash(newDateKey); // Get the hash index for the new date
                        hashTable.put(newDateKey, newEntry); // Insert the new entry into the hash table
                        FeedBack.setText("Date updated successfully. Old Date Key: " + oldDateKey + " (Index: " + oldIndex + "), New Date Key: " + newDateKey + " (Index: " + newIndex + ")");
                    } else {
                        newTree = (AVLTree) newEntry.getValue(); // Get the existing AVL tree for the new date key
                        int newIndex = hashTable.hash(newDateKey); // Get the hash index for the new date
                        FeedBack.setText("Date updated successfully. Old Date Key: " + oldDateKey + " (Index: " + oldIndex + "), New Date Key: " + newDateKey + " (Index: " + newIndex + ")");
                    }

                    transferNodes(oldTree.getRoot(), newTree, newDateKey); // Insert all nodes from oldTree to newTree

                    refreshHashTableDisplay(); 
                } else {
                    FeedBack.setText("Old date not found. Old Date Key: " + oldDateKey); 
                }
            } else {
                FeedBack.setText("Please select both dates before updating.");
            }
        });

        layout.getChildren().addAll(new Label("Select Old Date:"), oldDatePicker,
                new Label("Select New Date:"), newDatePicker, updateButton, FeedBack); 
        Scene scene = new Scene(layout, 700, 250); 
        updateStage.setScene(scene); 
        updateStage.setTitle("Update Date Record");
        updateStage.show(); 
    }

    // Method to transfer nodes from oldTree to newTree
    private void transferNodes(AVLTreeNode node, AVLTree newTree, String newDate) {
        if (node != null) { // Check if the node is not null
            Martyr oldMartyr = node.getMartyr(); // Get the martyr from the node
            if (oldMartyr != null) { // Check if the martyr is not null
                Martyr newMartyr = new Martyr(oldMartyr.getName(), newDate, oldMartyr.getAge(), oldMartyr.getGender()); // Create a new martyr with the updated date
                newTree.insert(newDate, node.getDistrict(), node.getLocation(), newMartyr); // Insert the new martyr into the new AVL tree
            }
            transferNodes(node.getLeft(), newTree, newDate); // Transfer the left subtree
            transferNodes(node.getRight(), newTree, newDate); // Transfer the right subtree
        }
    }

    // Method to show the form for deleting a date
    private void showDeleteDateForm() {
        FeedBack.setText("");
        Stage deleteStage = new Stage();
        VBox layout = new VBox(10); 
        DatePicker datePicker = new DatePicker(); // Create a date picker for selecting the date to delete
        datePicker.setPromptText("Select Date to Delete"); 

        Button deleteButton = new Button("Delete"); 
        deleteButton.setOnAction(e -> {
            LocalDate date = datePicker.getValue(); 
            if (date == null) {
                FeedBack.setText("Please select a date.");
            } else {
                String dateKey = date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy")); 
                int hashIndex = hashTable.hash(dateKey); // Calculate the hash index for the date to print index after deleting
                boolean deleted = (hashTable.remove(dateKey) != -1); // Remove the date entry from the hash table
                if (deleted) {
                    FeedBack.setText("Date " + dateKey + " deleted successfully from index " + hashIndex); 
                    refreshHashTableDisplay(); 
                } else {
                    FeedBack.setText("Date " + dateKey + " not found."); 
                }
            }
        });

        layout.getChildren().addAll(new Label("Select Date to Delete:"), datePicker, deleteButton, FeedBack); 
        Scene scene = new Scene(layout, 500, 200); 
        deleteStage.setScene(scene);
        deleteStage.setTitle("Delete Date Record");
        deleteStage.show(); 
    }

    // Refresh the hash table display
    private void refreshHashTableDisplay() {
        if (includeEmptySpotsCheckBox != null && hashTableOutputArea != null) {
            boolean includeEmptySpots = includeEmptySpotsCheckBox.isSelected();
            String output = hashTable.printHashTable(includeEmptySpots); 
            hashTableOutputArea.setText(output);
        }
    }





    private void createNavigationStage() {
        FeedBack.setText("");

        Stage navigationStage = new Stage();
        navigationStage.setTitle("Navigate Dates");

        Label dateLabel = new Label("Date:");
        Label dateValueLabel = new Label("No date available");
        Label totalMartyrsLabel = new Label("Total Number of Martyrs:");
        Label totalMartyrsValueLabel = new Label();
        Label averageAgeLabel = new Label("Average Martyrs Age:");
        Label averageAgeValueLabel = new Label();
        Label maxMartyrsDistrictLabel = new Label("District with Maximum Number of Martyrs:");
        Label maxMartyrsDistrictValueLabel = new Label();
        Label maxMartyrsLocationLabel = new Label("Location with Maximum Number of Martyrs:");
        Label maxMartyrsLocationValueLabel = new Label();

        Button nextButton = new Button("Next");
        Button previousButton = new Button("Previous");

        int[] currentIndex = {0}; // Maintain current index in the hash table
        currentEntry = findNextNonEmptyEntry(currentIndex, 1); // Find the first non-empty entry and 1 for positive direction

        if (currentEntry != null) {
            updateNavigationUI(currentEntry, dateValueLabel, totalMartyrsValueLabel, averageAgeValueLabel, maxMartyrsDistrictValueLabel, maxMartyrsLocationValueLabel);
        } else {
            FeedBack.setText("No entries found in the hash table.");
        }

        nextButton.setOnAction(e -> {
            currentEntry = findNextNonEmptyEntry(currentIndex, 1); // Move to the next non-empty entry
            if (currentEntry != null) {
                updateNavigationUI(currentEntry, dateValueLabel, totalMartyrsValueLabel, averageAgeValueLabel, maxMartyrsDistrictValueLabel, maxMartyrsLocationValueLabel);
                previousButton.setDisable(false); // Enable the previous button
            } else {
                FeedBack.setText("Reached last entry.");
                nextButton.setDisable(true); // Disable next button at the end
            }
        });

        previousButton.setOnAction(e -> {
            currentEntry = findNextNonEmptyEntry(currentIndex, -1); // Move to the previous non-empty entry and -1 for negative direction
            if (currentEntry != null) {
                updateNavigationUI(currentEntry, dateValueLabel, totalMartyrsValueLabel, averageAgeValueLabel, maxMartyrsDistrictValueLabel, maxMartyrsLocationValueLabel);
                nextButton.setDisable(false); // Enable the next button
            } else {
                FeedBack.setText("Reached first entry.");
                previousButton.setDisable(true); // Disable previous button at the beginning
            }
        });

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(dateLabel, dateValueLabel, totalMartyrsLabel, totalMartyrsValueLabel,
                averageAgeLabel, averageAgeValueLabel, maxMartyrsDistrictLabel, maxMartyrsDistrictValueLabel,
                maxMartyrsLocationLabel, maxMartyrsLocationValueLabel, nextButton, previousButton, FeedBack);

        Scene scene = new Scene(vbox, 400, 400);
        navigationStage.setScene(scene);
        navigationStage.show();
    }

    // Updates the navigation UI with the current entry's information
    private void updateNavigationUI(HashEntry current, Label dateValueLabel, Label totalMartyrsValueLabel, Label averageAgeValueLabel, Label maxMartyrsDistrictValueLabel, Label maxMartyrsLocationValueLabel) {
        if (current != null) {
            dateValueLabel.setText(current.getKey());
            Object value = current.getValue();
// i should use the valuye because the traversal chain cant be done by the hasEntries by themselves because the ClassCastException 
            while (value instanceof HashEntry) {
                current = (HashEntry) value;
                value = current.getValue();
            }//so i use the value to be each HashEntry so i can covert it to AVltree witch to get all the values of the navigation

            if (value instanceof AVLTree) {
                AVLTree tree = (AVLTree) value;

                int totalMartyrs = tree.getTotalMartyrs();
                double averageAge = tree.getAverageAge();
                String maxDistrict = tree.getMaxMartyrsDistrict();
                String maxLocation = tree.getMaxMartyrsLocation();

                totalMartyrsValueLabel.setText(String.valueOf(totalMartyrs));//to convert the integer to string
                averageAgeValueLabel.setText(String.valueOf(averageAge));
                maxMartyrsDistrictValueLabel.setText(maxDistrict);
                maxMartyrsLocationValueLabel.setText(maxLocation);
            } else {
                totalMartyrsValueLabel.setText("0");
                averageAgeValueLabel.setText("0");
                maxMartyrsDistrictValueLabel.setText("N/A");
                maxMartyrsLocationValueLabel.setText("N/A");
            }
        } else {
            dateValueLabel.setText("No date available");
            totalMartyrsValueLabel.setText("");
            averageAgeValueLabel.setText("");
            maxMartyrsDistrictValueLabel.setText("");
            maxMartyrsLocationValueLabel.setText("");
        }
    }

    // Finds the next non-empty entry in the hash table based on the given direction
    private HashEntry findNextNonEmptyEntry(int[] currentIndex, int direction) {
        while (currentIndex[0] >= 0 && currentIndex[0] < hashTable.getSize()) {
            currentIndex[0] += direction;
            if (currentIndex[0] >= 0 && currentIndex[0] < hashTable.getSize()) {
                HashEntry entry = hashTable.getEntry(currentIndex[0]);
                if (entry != null) {
                    return entry;
                }
            }
        }
        return null; // No more non-empty entries in the given direction
    }

    // Loads martyrs for the selected date and displays them in a table
    private void loadMartyrsForSelectedDate() {
        FeedBack.setText("");
        Stage loadMartyrsStage = new Stage();
        VBox layout = new VBox(10);

        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Select Date");

        TableView<Martyr> martyrsTable = new TableView<>();
        TableColumn<Martyr, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<Martyr, Integer> ageColumn = new TableColumn<>("Age");
        ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
        TableColumn<Martyr, Character> genderColumn = new TableColumn<>("Gender");
        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        martyrsTable.getColumns().addAll(nameColumn, ageColumn, genderColumn);

        Button loadButton = new Button("Load Martyrs");
        loadButton.setOnAction(e -> {
            LocalDate selectedDate = datePicker.getValue();
            if (selectedDate == null) {
                FeedBack.setText("Please select a date.");
            } else {
                String dateKey = selectedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
                HashEntry dateRecord = (HashEntry) hashTable.get(dateKey);
                if (dateRecord != null && dateRecord.getValue() instanceof AVLTree) {
                    AVLTree tree = (AVLTree) dateRecord.getValue(); //this tree can have the avl tree in this hashEntry 
                    Martyr[] martyrs = tree.getMartyrs();
                    martyrsTable.getItems().clear();
                    for (Martyr martyr : martyrs) {
                        martyrsTable.getItems().add(martyr);
                    }
                    FeedBack.setText("");
                } else {
                    FeedBack.setText("No AVL tree found for the selected date.");
                    martyrsTable.getItems().clear(); // Clear the table if no AVL tree is found
                }
            }
        });

        layout.getChildren().addAll(new Label("Select Date:"), datePicker, loadButton, martyrsTable, FeedBack);
        loadMartyrsStage.setScene(new Scene(layout, 600, 400));
        loadMartyrsStage.setTitle("Load Martyrs for Selected Date");
        loadMartyrsStage.show();
    }











	// Method to open the main menu stage of the application.
	private void openMenuStage(Stage primaryStage, File file) {
		bdPane = new BorderPane(); // Main layout manager.
		menuBar = new MenuBar(); // Menu bar for top-level menu items.

		menu1 = new Menu("Dates");
		item1 = new MenuItem("Insert Date");
		item2 = new MenuItem("Update Date");
		item3 = new MenuItem("Delete Date");
		item4 = new MenuItem("Navigate through HashTable");
		item5=new MenuItem("Print Martyr by default");

		menu2 = new Menu("Martyr");
		item7 = new MenuItem("insert new martyr record");
		item8 = new MenuItem("update martyr record");
		item9 = new MenuItem("delete martyr record");
		item10=new MenuItem("Tree Information");
		item11=new MenuItem("Print tree Level by Level");
		item12=new MenuItem("Print Martyr sorting by age");



		menu3 = new Menu("File");
		item13 = new MenuItem("Save Data");



		// Add items to menus.
		menu1.getItems().addAll(item1, item2, item3, item4,item5);
		menu2.getItems().addAll(item7, item8, item9, item10, item11, item12);
		menu3.getItems().add(item13);

		menuBar.getMenus().addAll(menu1, menu2,menu3); // Add menus to the menu bar.

		// Set action handlers for menu items.
		item1.setOnAction(e -> showInsertDateForm());
		item2.setOnAction(e -> showUpdateDateForm());
		item3.setOnAction(e -> showDeleteDateForm());
		item4.setOnAction(e -> createNavigationStage ());
		item5.setOnAction(e -> loadMartyrsForSelectedDate());

		item7.setOnAction(e -> createInsertMartyrStage());
		item8.setOnAction(e -> showUpdateMartyrStage());
		item9.setOnAction(e -> showDeleteMartyrStage());
		item10.setOnAction(e -> showTreeInformationStage());
		item11.setOnAction(e -> printTreeLevelByLevelStage());
		item12.setOnAction(e -> showSortedMartyrsStage());

		item13.setOnAction(e -> saveDataToFile());


		includeEmptySpotsCheckBox = new CheckBox("Include Empty Spots");
		hashTableOutputArea = new TextArea();
		hashTableOutputArea.setMaxSize(650, 600);
		hashTableOutputArea.setEditable(false);
		refreshHashTableDisplay();

		Button printButton = new Button("Print Hash Table");
		printButton.setOnAction(e ->refreshHashTableDisplay());

		VBox vbox = new VBox(10);
		vbox.getChildren().addAll(includeEmptySpotsCheckBox, printButton);

		// Layout the main scene.
		bdPane.setTop(menuBar);
		bdPane.setCenter(hashTableOutputArea);
		bdPane.setBottom(vbox);

		Scene scene = new Scene(bdPane, 700, 700);
		primaryStage.setScene(scene);
		primaryStage.show(); // Show the primary stage.
	}

	// Method to validate strings (only alphabets and spaces allowed)
	private boolean isValidString(String str) {
	    if (str == null || str.isEmpty()) {
	        return false;
	    }
	    for (char c : str.toCharArray()) {
	        if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
	            return false;
	        }
	    }
	    return true;
	}


	// Method to create a stage for inserting a new martyr
	private void createInsertMartyrStage() {
	    FeedBack.setText("");

	    Stage insertStage = new Stage();
	    VBox layout = new VBox(10);

	    ComboBox<String> districtComboBox = new ComboBox<>();
	    ComboBox<String> locationComboBox = new ComboBox<>();
	    TextField nameField = new TextField();
	    TextField ageField = new TextField();
	    DatePicker dateOfDeathPicker = new DatePicker();

	    districtComboBox.setPromptText("Select District");
	    locationComboBox.setPromptText("Select Location");
	    nameField.setPromptText("Enter Name");
	    ageField.setPromptText("Enter Age");
	    dateOfDeathPicker.setPromptText("Select Date of Death");

	    populateDistrictComboBox(districtComboBox, locationComboBox);

	    ToggleGroup genderGroup = new ToggleGroup();
	    RadioButton maleRadio = new RadioButton("Male");
	    maleRadio.setToggleGroup(genderGroup);
	    RadioButton femaleRadio = new RadioButton("Female");
	    femaleRadio.setToggleGroup(genderGroup);

	    Button insertButton = new Button("Insert");
	    insertButton.setOnAction(e -> {
	        String districtName = districtComboBox.getValue();
	        String locationName = locationComboBox.getValue();
	        String name = nameField.getText().trim();
	        byte age;
	        try {
	            age = Byte.parseByte(ageField.getText().trim());
	        } catch (NumberFormatException ex) {
	            FeedBack.setText("Invalid Age: Please enter a valid age.");
	            return;
	        }

	        if (!isValidString(name)) {
	            FeedBack.setText("Invalid Name should not contain numbers.");
	            return;
	        }

	        RadioButton selectedGender = (RadioButton) genderGroup.getSelectedToggle();
	        if (selectedGender == null) {
	            FeedBack.setText("Invalid Gender: Please select a gender.");
	            return;
	        }
	        char gender = selectedGender.getText().charAt(0);

	        LocalDate dateOfDeath = dateOfDeathPicker.getValue();
	        if (dateOfDeath == null) {
	            FeedBack.setText("Invalid Date: Please select a date.");
	            return;
	        }
	        if (dateOfDeath.isAfter(LocalDate.now())) {
	            FeedBack.setText("Invalid Date: Date of death cannot be in the future.");
	            return;
	        }
	        String dateOfDeathStr = dateOfDeath.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

	        if (districtName.isEmpty() || name.isEmpty() || dateOfDeath == null) {
	            FeedBack.setText("Please provide district, name, and date of death.");
	            return;
	        }

	        Martyr newMartyr = new Martyr(name, dateOfDeathStr, age, gender);
	        insertMartyr(hashTable, newMartyr, districtName, locationName);

	        int index = hashTable.hash(dateOfDeathStr);

	        FeedBack.setText("Martyr record inserted successfully at index " + index + ".");
	        refreshHashTableDisplay();

	        districtComboBox.setValue(null);
	        locationComboBox.setValue(null);
	        nameField.clear();
	        ageField.clear();
	        genderGroup.selectToggle(null);
	        dateOfDeathPicker.setValue(null);
	    });

	    layout.getChildren().addAll(
	        new Label("District:"), districtComboBox,
	        new Label("Location:"), locationComboBox,
	        new Label("Name:"), nameField,
	        new Label("Age:"), ageField,
	        new Label("Gender:"), maleRadio, femaleRadio,
	        new Label("Date of Death:"), dateOfDeathPicker,
	        insertButton, FeedBack
	    );

	    insertStage.setScene(new Scene(layout, 400, 500));
	    insertStage.setTitle("Insert New Martyr Record");
	    insertStage.show();
	}

	// Method to create a stage for updating a martyr
	private void showUpdateMartyrStage() {
	    FeedBack.setText(""); // Clear previous feedback

	    Stage updateStage = new Stage(); // Create a new stage for updating a martyr
	    VBox layout = new VBox(10); // Create a layout with 10px spacing

	    // Create input fields and set their prompt texts
	    TextField oldNameField = new TextField();
	    oldNameField.setPromptText("Enter Martyr's Partial Old Name");
	    DatePicker oldDatePicker = new DatePicker();
	    oldDatePicker.setPromptText("Select Old Date of Death");
	    TextField newNameField = new TextField();
	    newNameField.setPromptText("Enter Martyr's New Name (Optional)");
	    ComboBox<String> newDistrictComboBox = new ComboBox<>();
	    newDistrictComboBox.setPromptText("Select New District (Optional)");
	    ComboBox<String> newLocationComboBox = new ComboBox<>();
	    newLocationComboBox.setPromptText("Select New Location (Optional)");
	    TextField newAgeField = new TextField();
	    newAgeField.setPromptText("Enter New Age (Optional)");

	    // Create radio buttons for gender selection
	    ToggleGroup genderGroup = new ToggleGroup();
	    RadioButton maleRadio = new RadioButton("Male");
	    maleRadio.setToggleGroup(genderGroup);
	    RadioButton femaleRadio = new RadioButton("Female");
	    femaleRadio.setToggleGroup(genderGroup);

	    // Populate the district combo box
	    populateDistrictComboBox(newDistrictComboBox, newLocationComboBox);

	    // Create the update button and set its action
	    Button updateButton = new Button("Update");
	    updateButton.setOnAction(e -> {
	        String oldName = oldNameField.getText().trim();
	        LocalDate oldDate = oldDatePicker.getValue();
	        String newName = newNameField.getText().trim().isEmpty() ? null : newNameField.getText().trim();
	        String newDistrict = newDistrictComboBox.getValue();
	        String newLocation = newLocationComboBox.getValue();
	        Byte newAge = null;
	        try {
	            newAge = newAgeField.getText().trim().isEmpty() ? null : Byte.parseByte(newAgeField.getText().trim());
	        } catch (NumberFormatException ex) {
	            FeedBack.setText("Invalid Age: Please enter a valid age.");
	            return;
	        }
	        RadioButton selectedGender = (RadioButton) genderGroup.getSelectedToggle();
	        Character newGender = (selectedGender == null) ? null : selectedGender.getText().charAt(0);

	        if (!oldName.isEmpty() && oldDate != null) {
	            if (newName == null && newDistrict == null && newLocation == null && newAge == null && newGender == null) {
	                FeedBack.setText("Please update at least one field.");
	            } else {
	                if ((newName != null && !isValidString(newName))) {
	                    FeedBack.setText("Invalid input: Name should not contain numbers.");
	                    return;
	                }
	                int index = updateMartyr(oldName, oldDate, newName, newDistrict, newLocation, newAge, newGender);
	                if (index != -1) {
	                    FeedBack.setText("Martyr '" + oldName + "' updated successfully at index " + index + ".");
	                    refreshHashTableDisplay();
	                } else {
	                    FeedBack.setText("Martyr '" + oldName + "' not found.");
	                }
	            }
	        } else {
	            FeedBack.setText("Please enter the martyr's partial old name and old date of death to proceed with the update.");
	        }
	    });

	    // Add all UI components to the layout
	    layout.getChildren().addAll(
	        new Label("Martyr's Partial Old Name:"), oldNameField,
	        new Label("Old Date of Death:"), oldDatePicker,
	        new Label("Martyr's New Name (Optional):"), newNameField,
	        new Label("New District (Optional):"), newDistrictComboBox,
	        new Label("New Location (Optional):"), newLocationComboBox,
	        new Label("New Age (Optional):"), newAgeField,
	        new Label("New Gender (Optional):"), maleRadio, femaleRadio,
	        updateButton, FeedBack
	    );

	    updateStage.setScene(new Scene(layout, 400, 600)); 
	    updateStage.setTitle("Update Martyr Record"); 
	    updateStage.show(); 
	}


	// Method to create a stage for deleting a martyr
	private void showDeleteMartyrStage() {
	    FeedBack.setText("");

	    Stage deleteStage = new Stage();
	    VBox layout = new VBox(10);

	    TextField nameField = new TextField();
	    DatePicker oldDatePicker = new DatePicker();
	    nameField.setPromptText("Enter Martyr's Partial Name");
	    oldDatePicker.setPromptText("Select Old Date of Death");

	    Button deleteButton = new Button("Delete");
	    deleteButton.setOnAction(e -> {
	        String name = nameField.getText().trim();
	        LocalDate oldDate = oldDatePicker.getValue();

	        if (!name.isEmpty() && oldDate != null) {
	            String feedbackMessage = deleteMartyr(name, oldDate);
	            FeedBack.setText(feedbackMessage);
	            refreshHashTableDisplay();
	        } else {
	            FeedBack.setText("Please enter the martyr's partial name and old date of death to proceed with the deletion.");
	        }
	    });

	    layout.getChildren().addAll(
	        new Label("Martyr's Partial Name:"), nameField,
	        new Label("Old Date of Death:"), oldDatePicker,
	        deleteButton, FeedBack
	    );

	    deleteStage.setScene(new Scene(layout, 750, 300));
	    deleteStage.setTitle("Delete Martyr Record");
	    deleteStage.show();
	}



	// Method to update a martyr's information
	private int updateMartyr(String oldName, LocalDate oldDate, String newName, String newDistrict, String newLocation, Byte newAge, Character newGender) {
	    // Format the old date to a string 
	    String oldDateStr = oldDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	    
	    // Iterate through each index in the hash table
	    for (int i = 0; i < hashTable.getSize(); i++) {
	        HashEntry entry = hashTable.getEntry(i);	        // Get the entry at the current index

	        
	        while (entry != null) {
	            // Get the value stored in this entry
	            Object value = entry.getValue();
	            
	            // If the value is another HashEntry, continue traversing the linked list
	            while (value instanceof HashEntry) {
	                entry = (HashEntry) value;
	                value = entry.getValue();
	            }
	            
	            // Check if the value is an AVLTree
	            if (value instanceof AVLTree) {
	                // Cast the value to an AVLTree
	                AVLTree tree = (AVLTree) value;
	                
	                // Search for the node with the given partial name and date
	                AVLTreeNode node = tree.searchNodeByPartialNameAndDate(oldName, oldDateStr);
	                
	                if (node != null) {//if its found
	                    // Get the martyr from the node
	                    Martyr martyr = node.getMartyr();
	                    boolean updated = false;

	                    // Update the martyr's information if its provided
	                    if (newName != null && !newName.trim().isEmpty()) {
	                        martyr.setName(newName);
	                        updated = true;
	                    }
	                    
	                    if (newDistrict != null && !newDistrict.trim().isEmpty()) {
	                        node.setDistrict(newDistrict);
	                        updated = true;
	                    }
	                    
	                    if (newLocation != null && !newLocation.trim().isEmpty()) {
	                        node.setLocation(newLocation);
	                        updated = true;
	                    }
	                    
	                    if (newAge != null) {
	                        martyr.setAge(newAge);
	                        updated = true;
	                    }
	                    
	                    if (newGender != null) {
	                        martyr.setGender(newGender);
	                        updated = true;
	                    }

	                    // If any field was updated, return the index of the entry
	                    if (updated) {
	                        return i;
	                    } else {
	                        // If no fields were updated, return -1
	                        return -1;
	                    }
	                }
	            } 
	            
	            // Move to the next entry in the HashTable
	            entry = entry.getNext();
	        }
	    }
	    
	    // If the martyr is not found, print a message and return -1
	    System.out.println("Martyr " + oldName + " not found.");
	    return -1;
	}

	// Method to delete a martyr's information
	private String deleteMartyr(String name, LocalDate oldDate) {
	    // Format the old date to a string in the format MM/dd/yyyy
	    String oldDateStr = oldDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	    
	    // Iterate through each index in the hash table
	    for (int i = 0; i < hashTable.getSize(); i++) {
	        // Get the entry at the current index
	        HashEntry entry = hashTable.getEntry(i);
	        
	        // Traverse the HashTable at this index
	        while (entry != null) {
	            // Get the value stored in this entry
	            Object value = entry.getValue();
	            
	            // If the value is another HashEntry, continue traversing the linked list
	            while (value instanceof HashEntry) {
	                entry = (HashEntry) value;
	                value = entry.getValue();
	            }
	            
	            // Check if the value is an AVLTree
	            if (value instanceof AVLTree) {
	                // Cast the value to an AVLTree
	                AVLTree tree = (AVLTree) value;
	                
	                boolean deleted = tree.deleteNodeByPartialNameAndDate(name, oldDateStr);
	                
	                // If the node was successfully deleted
	                if (deleted) {
	                    // Check if the tree is now empty
	                    if (tree.isEmpty()) {
	                        // If the tree is empty, set the entry value to null and mark it as deleted
	                        entry.setValue(null);
	                        entry.setStatus('D');
	                        
	                        return "Martyr " + name + " deleted successfully from tree at index " + i + ". The tree is now empty, and the entry has been marked as deleted.";
	                    } else {
	                        // Return a success message indicating the martyr was deleted
	                        return "Martyr " + name + " deleted successfully from tree at index " + i;
	                    }
	                }
	            } else {
	                // If the value is not an AVLTree, return an error message
	                return "Entry value is not an AVLTree: " + value.getClass().getName();
	            }
	            
	            // Move to the next entry in the HashTable
	            entry = entry.getNext();
	        }
	    }
	    
	    // If the martyr is not found, return a message indicating so
	    return "Martyr " + name + " not found.";
	}



	// Method to show the tree information stage
	private void showTreeInformationStage() {
	    FeedBack.setText("");

	    Stage infoStage = new Stage();
	    VBox layout = new VBox(10);

	    // Create a DatePicker for selecting the date
	    DatePicker datePicker = new DatePicker();
	    datePicker.setPromptText("Select Date");

	    Label sizeLabel = new Label("Tree Size: ");
	    Label heightLabel = new Label("Tree Height: ");

	    Button showButton = new Button("Show Information");
	    showButton.setOnAction(e -> {
	        // Clear previous feedback messages
	        FeedBack.setText("");

	        // Get the selected date
	        LocalDate selectedDate = datePicker.getValue();
	        if (selectedDate != null) {
	            // Format the selected date as a string
	            String dateKey = selectedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	            // Get the hash entry for the selected date
	            HashEntry entry = (HashEntry) hashTable.get(dateKey);
	            if (entry != null && entry.getValue() instanceof AVLTree) {
	                // If an AVL tree exists for the selected date, display its size and height
	                AVLTree tree = (AVLTree) entry.getValue();
	                sizeLabel.setText("Tree Size(Total Martyrs): " + tree.getTotalMartyrs());
	                heightLabel.setText("Tree Height: " + tree.getHeight());
	            } else {
	                // If no AVL tree is found, display default messages and feedback
	                sizeLabel.setText("Tree Size: ");
	                heightLabel.setText("Tree Height: ");
	                FeedBack.setText("No tree found for the selected date.");
	            }
	        }
	    });

	    layout.getChildren().addAll(new Label("Select Date:"), datePicker, showButton, sizeLabel, heightLabel, FeedBack);
	    Scene scene = new Scene(layout, 300, 200);
	    infoStage.setScene(scene);
	    infoStage.setTitle("Tree Information");
	    infoStage.show();
	}

	// Method to show the stage for printing tree level by level
	private void printTreeLevelByLevelStage() {
	    FeedBack.setText("");
	    Stage printStage = new Stage();
	    VBox layout = new VBox(10);

	    // Create a DatePicker for selecting the date
	    DatePicker datePicker = new DatePicker();
	    datePicker.setPromptText("Select Date");

	    TextArea outputArea = new TextArea();
	    outputArea.setEditable(false);

	    Button printButton = new Button("Print Tree");
	    printButton.setOnAction(e -> {
	        // Get the selected date
	        LocalDate selectedDate = datePicker.getValue();
	        if (selectedDate != null) {
	            // Format the selected date as a string
	            String dateKey = selectedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	            // Get the hash entry for the selected date
	            HashEntry entry = (HashEntry) hashTable.get(dateKey);
	            if (entry != null && entry.getValue() instanceof AVLTree) {
	                // If an AVL tree exists for the selected date, print it level by level
	                AVLTree tree = (AVLTree) entry.getValue();
	                outputArea.setText(tree.printLevelByLevel());
	            } else {
	                outputArea.setText("");
	                FeedBack.setText("No tree found for the selected date.");
	            }
	        }
	    });

	    layout.getChildren().addAll(new Label("Select Date:"), datePicker, printButton, outputArea, FeedBack);
	    Scene scene = new Scene(layout, 400, 400);
	    printStage.setScene(scene);
	    printStage.setTitle("Print Tree Level by Level");
	    printStage.show();
	}
	private void showSortedMartyrsStage() {
	    FeedBack.setText(""); // Clear any previous feedback
	    Stage sortedMartyrsStage = new Stage(); // Create a new stage for this view
	    VBox layout = new VBox(10); // Vertical box with spacing of 10 for layout

	    // Date picker for selecting the date
	    DatePicker datePicker = new DatePicker();
	    datePicker.setPromptText("Select Date");

	    // Table to display martyrs
	    TableView<Martyr> martyrsTable = new TableView<>();
	    TableColumn<Martyr, String> nameColumn = new TableColumn<>("Name");
	    nameColumn.setCellValueFactory(new PropertyValueFactory<>("name")); // Bind name column
	    TableColumn<Martyr, Integer> ageColumn = new TableColumn<>("Age");
	    ageColumn.setCellValueFactory(new PropertyValueFactory<>("age")); // Bind age column
	    TableColumn<Martyr, Character> genderColumn = new TableColumn<>("Gender");
	    genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender")); // Bind gender column
	    martyrsTable.getColumns().addAll(nameColumn, ageColumn, genderColumn); // Add columns to table

	    // Button to trigger sorting and display
	    Button showButton = new Button("Show Sorted Martyrs");
	    showButton.setOnAction(e -> {
	        LocalDate selectedDate = datePicker.getValue(); // Get selected date
	        if (selectedDate == null) {
	            FeedBack.setText("Please select a date."); // Prompt user to select a date
	        } else {
	            String dateKey = selectedDate.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	            HashEntry dateRecord = (HashEntry) hashTable.get(dateKey);
	            if (dateRecord != null && dateRecord.getValue() instanceof AVLTree) {
	                AVLTree tree = (AVLTree) dateRecord.getValue();
	                Martyr[] martyrs = tree.getMartyrs(); // Get martyrs from AVL tree
	                MinHeap minHeap = new MinHeap(martyrs.length); // Create a MinHeap
	                minHeap.heapSort(martyrs); // Sort martyrs using heap sort
	                martyrsTable.getItems().setAll(martyrs); // Display sorted martyrs
	                FeedBack.setText("");
	            } else {
	                FeedBack.setText("No AVL tree found for the selected date."); // No data found
	                martyrsTable.getItems().clear(); // Clear the table
	            }
	        }
	    });

	    // Add all components to layout
	    layout.getChildren().addAll(new Label("Select Date:"), datePicker, showButton, martyrsTable, FeedBack);
	    Scene scene = new Scene(layout, 600, 400); // Set the scene size
	    sortedMartyrsStage.setScene(scene); // Set the scene
	    sortedMartyrsStage.setTitle("Sorted Martyrs"); // Title for the stage
	    sortedMartyrsStage.show(); // Show the stage
	}







	// Method to save data to a file
	private void saveDataToFile() {
	    FeedBack.setText("");
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Save Data");
	    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
	    File file = fileChooser.showSaveDialog(null);

	    if (file != null) {
	        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
	            writer.println("Name,Date of Death,Age,Location,District,Gender");

	            // Iterate through the hash table and save each entry to the file
	            for (int i = 0; i < hashTable.getSize(); i++) {
	                HashEntry entry = hashTable.getEntry(i);
	                while (entry != null) {
	                    Object value = entry.getValue();
	                    while (value instanceof HashEntry) {
	                        entry = (HashEntry) value;
	                        value = entry.getValue();
	                    }
	                    if (value instanceof AVLTree) {
	                        // If the value is an AVL tree, save its nodes to the file
	                        AVLTree tree = (AVLTree) value;
	                        saveTreeToFile(writer, tree.getRoot()); //save each subtree in the file
	                    } else {
	                        System.out.println("Entry at index " + i + " with key " + entry.getKey() + " is not an AVLTree, but a " + entry.getValue().getClass().getName());
	                    }
	                    entry = entry.getNext();
	                }
	            }
	            FeedBack.setText("Data saved successfully.");
	        } catch (IOException e) {
	            FeedBack.setText("Failed to save data.");
	            e.printStackTrace();
	        }
	    }
	}

	// Recursive method to save nodes of an AVL tree to a file
	private void saveTreeToFile(PrintWriter writer, AVLTreeNode node) {
	    if (node == null) {
	        return; // Base case: if the node is null, return
	    }
	    saveTreeToFile(writer, node.getLeft()); // Recursively save the left subtree
	    Martyr martyr = node.getMartyr();
	    // Write the martyr's information to the file
	    writer.println(martyr.getName() + "," + martyr.getDateOfDeath() + "," + martyr.getAge() + "," + node.getLocation() + "," + node.getDistrict() + "," + martyr.getGender());
	    saveTreeToFile(writer, node.getRight()); // Recursively save the right subtree
	}

	// Method to populate the district and location combo boxes
	private void populateDistrictComboBox(ComboBox<String> districtComboBox, ComboBox<String> locationComboBox) {
	    // Iterate through the hash table to populate the district combo box
	    for (int i = 0; i < hashTable.getSize(); i++) {
	        HashEntry entry = hashTable.getEntry(i);
	        while (entry != null) {
	            Object value = entry.getValue();
	            while (value instanceof HashEntry) {
	                entry = (HashEntry) value;
	                value = entry.getValue();
	            }
	            if (value instanceof AVLTree) {
	                AVLTree tree = (AVLTree) value;
	                for (AVLTreeNode node : tree.getNodes()) {
	                    if (!districtComboBox.getItems().contains(node.getDistrict())) {
	                        districtComboBox.getItems().add(node.getDistrict());
	                    }
	                }
	            }
	            entry = entry.getNext();//itereate throght entries of the hashTable
	        }
	    }

	    districtComboBox.setOnAction(event -> {
	        locationComboBox.getItems().clear(); 
	        String selectedDistrict = districtComboBox.getValue(); // Get the selected district
	        // Iterate through the hash table to populate the location combo box
	        for (int i = 0; i < hashTable.getSize(); i++) {
	            HashEntry entry = hashTable.getEntry(i);
	            while (entry != null) {
	                Object value = entry.getValue();
	                while (value instanceof HashEntry) {
	                    entry = (HashEntry) value;
	                    value = entry.getValue();
	                }
	                if (value instanceof AVLTree) {
	                    AVLTree tree = (AVLTree) value;
	                    for (AVLTreeNode node : tree.getNodes()) {
	                        if (node.getDistrict().equals(selectedDistrict) && !locationComboBox.getItems().contains(node.getLocation())) {
	                            locationComboBox.getItems().add(node.getLocation());
	                        }
	                    }
	                }
	                entry = entry.getNext();
	            }
	        }
	    });
	}




	public static void main(String[] args) {
		launch(args);
	}


}
