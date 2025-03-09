package HuffCode2;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.geometry.*;
import javafx.scene.control.*;
import javafx.scene.text.*;

import java.io.*;
import java.util.*;

// Main scene class that represents the initial user interface.
public class MainScene extends Scene {

    // Constructor for the MainScene class.
    public MainScene(Stage primaryStage) {
        super(new StackPane(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
        StackPane layout = (StackPane) getRoot();
        layout.getStyleClass().add("root");

        // Set up the main layout using a VBox.
        VBox vBox = new VBox(250);
        vBox.setPadding(new Insets(60));
        vBox.setAlignment(Pos.TOP_CENTER);

        // Set up a welcome label.
        Label welcomeLabel = new Label("Welcome");
        welcomeLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 40));
        welcomeLabel.setStyle("-fx-text-fill: #000000;");

        // Array of button labels.
        String[] strings = {"Compress", "Uncompress"};

        // ArrayList to store buttons.
        ArrayList<Button> buttons = new ArrayList<>();

        // Method to set up buttons with labels and actions.
        setupButtons(strings, buttons, primaryStage);

        // Set up a VBox to arrange buttons vertically.
        HBox arrangementButtons = new HBox(100);
        arrangementButtons.setAlignment(Pos.TOP_CENTER);
        arrangementButtons.getChildren().addAll(buttons);

        // Add components to the main layout.
        vBox.getChildren().addAll(welcomeLabel, arrangementButtons);
        layout.getChildren().addAll(vBox);
    }

    // Method to set up buttons with actions for compression and decompression.
    public static void setupButtons(String[] strings, ArrayList<Button> buttons, Stage primaryStage) {
        // Iterate over the array of strings to create buttons.
        for (String label : strings) {
            Button button = new Button(label);
            buttons.add(button);
            button.getStyleClass().add("custom-button");
            button.setPrefHeight(50);
            button.setPrefWidth(250);
        }

        // Action for the "Compress" button.
        buttons.get(0).setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a File to Compress");
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            // Ensure the selected file is valid and not a .hof file.
            if (selectedFile == null) {
                SceneManager.showAlert("Error", "No file selected. Please select a valid file to compress.");
            } else if (selectedFile.getName().endsWith(".hof")) {
                SceneManager.showAlert("Error", "Invalid file. Compression of .hof files is not allowed.");
            } else {
                SceneManager.setScene(new CompressScene(selectedFile));
            }
        });

        // Action for the "Uncompress" button.
        buttons.get(1).setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select a .hof File to Decompress");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Huffman Compressed Files", "*.hof"));
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            // Ensure the selected file is valid and has a .hof extension.
            if (selectedFile == null) {
                SceneManager.showAlert("Error", "No file selected. Please select a .hof file to decompress.");
            } else if (!selectedFile.getName().endsWith(".hof")) {
                SceneManager.showAlert("Error", "Invalid file. Please select a valid .hof file.");
            } else {
                SceneManager.setScene(new UncompressScene(selectedFile));
            }
        });
    }
}
