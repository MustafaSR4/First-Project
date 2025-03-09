package Huffcode3;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.geometry.*;         
import javafx.scene.control.*;
import javafx.scene.text.*;

import java.io.*;
import java.util.*;

// Main scene class that represents the initial user interface.
public class WelcomingScene extends Scene {
	Label messageLabel = new Label();
	

    // Constructor for the MainScene class.
    public WelcomingScene(Stage primaryStage){
        super(new StackPane(),1250 , 500); // Replace with desired dimensions
        StackPane layout = (StackPane) getRoot();
//        layout.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        layout.getStyleClass().add("root");
        HuffmanSceneImages.setBackground(layout, "file:/Users/mustafaalayasa/Desktop/eclipse-workspace/JavaFx-workspace/AlgorithimSemester/src/1701484587527.png");

        
        // Set up the main layout using a VBox.
        VBox vBox = new VBox(50);
        vBox.setPadding(new Insets(60));
        vBox.setAlignment(Pos.TOP_CENTER);

        // Set up a welcome label.
        Label welcomeLabel = new Label("Huffman Program");
        welcomeLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 40));
        welcomeLabel.setStyle("-fx-text-fill: #000000;");

        // Array of button labels.
        String[] strings = {
            "Compress",
            "Uncompress",
        };
        
        
        // ArrayList to store buttons.
        ArrayList<Button> buttons = new ArrayList<Button>();   
        
        // Method to set up buttons with labels and actions.
        setupButtons(strings, buttons, primaryStage, messageLabel);
        
        // Set up a VBox to arrange buttons vertically.
        VBox ArrangementButtons = new VBox(100);
        ArrangementButtons.setAlignment(Pos.TOP_CENTER);
        ArrangementButtons.getChildren().addAll(buttons);
        
        // Add components to the main layout.
        vBox.getChildren().addAll(welcomeLabel, ArrangementButtons,messageLabel);

        layout.getChildren().addAll(vBox);
    }
    
 // This method sets up buttons based on an array of strings and adds them to a stage in a JavaFX application.
 // This method sets up buttons based on an array of strings and adds them to a stage in a JavaFX application.
    public static void setupButtons(String[] strings, ArrayList<Button> buttons, Stage primaryStage, Label messageLabel) {
    	messageLabel.setStyle("-fx-font-size: 14px;");
    	// Iterate over the array of strings to create buttons
        for (int i = 0; i < strings.length; i++) {
            Button button = new Button(strings[i]); // Create a new button with text from the strings array
            buttons.add(button); // Add the button to the buttons ArrayList
            button.getStyleClass().add("custom-button"); // Add a custom CSS class for styling
            button.setPrefHeight(50); // Set preferred height for the button
            button.setPrefWidth(250); // Set preferred width for the button
        }

        // Set an action for the first button to open a file chooser for file selection
        buttons.get(0).setOnAction(e -> {
            FileChooser fileChooser = new FileChooser(); // Create a new file chooser
            File selectedFile = fileChooser.showOpenDialog(primaryStage); // Show the file chooser and get the selected file

            // Clear previous messages
            messageLabel.setText("");

            // Check the selected file
            if (selectedFile == null) {
                // No file was selected
                messageLabel.setText("No file selected. Please select a file.");
                messageLabel.setStyle("-fx-text-fill: red;");
            } else if (selectedFile.getName().endsWith(".huf")) {
                // File ends with .huf (invalid for this button)
                messageLabel.setText("Invalid file type. Please select a non-.huf file.");
                messageLabel.setStyle("-fx-text-fill: red;");
            } else {
                // Valid file selected
                messageLabel.setText("File selected: " + selectedFile.getName());
                messageLabel.setStyle("-fx-text-fill: green;");
                
                // Proceed with scene change
                SceneController.setScene(new CompressField(selectedFile));
            }
        });

        // Set an action for the second button to open a file chooser for uncompressing files
        buttons.get(1).setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Huffman Files", "*.huf")); // Only .huf files
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            // Clear previous messages
            messageLabel.setText("");

            // Check the selected file
            if (selectedFile == null) {
                // No file was selected
                messageLabel.setText("No file selected. Please select a file.");
                messageLabel.setStyle("-fx-text-fill: red;");
            } else if (!selectedFile.getName().endsWith(".huf")) {
                // File is not a .huf file
                messageLabel.setText("Invalid file type. Please select a .huf file.");
                messageLabel.setStyle("-fx-text-fill: red;");
            } else {
                // Valid .huf file selected
                messageLabel.setText("File selected: " + selectedFile.getName());
                messageLabel.setStyle("-fx-text-fill: green;");
                
                // Proceed with scene change
                SceneController.setScene(new DecompressField(selectedFile));
            }
        });
    }

    
}