package Huffcode3;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

public class CompressField extends Scene {
	
	// Class member variables
	File path; // The file to be compressed
	TreeNode[] bytes; // Array to store TreeNode objects for Huffman tree
	long lengthFileBefore; // Length of the file before compression
	long lengthFileAfter; // Length of the file after compression
	StringBuilder headerToShow = new StringBuilder(""); // StringBuilder to build and display header information
	StringBuilder nameOfCompressedFile; // Name of the compressed file

	// Construct the Huffman tree (example for demonstration)
    TreeNode root = new TreeNode(null, 
        new TreeNode('A', null, null), 
        new TreeNode(null, 
            new TreeNode('B', null, null), 
            new TreeNode('C', null, null)
        )
    );
	// Constructor for CompressScene
	public CompressField(File path) {
	    // Setting up the scene with a GridPane as the root and adjusting its size to the screen's dimensions
	    super(new GridPane(),700,500);
	    GridPane layout = (GridPane) getRoot();
        HuffmanSceneImages.setBackground(layout, "file:/Users/mustafaalayasa/Desktop/eclipse-workspace/JavaFx-workspace/AlgorithimSemester/src/1*A-kNqopygUZKc73UtsFgvw.jpg");

	    layout.getStyleClass().add("root");
	    this.path = path; // Assigning the file path

	    // Creating and configuring a label to indicate the compression process
	    Label waitLabel = new Label("Please wait, the file will be compressed soon");
	    waitLabel.setFont(Font.font("Arial", FontPosture.ITALIC, 22));
	    waitLabel.setStyle("-fx-text-fill: #5D6D7E; -fx-border-color: #AF7AC5; -fx-border-radius: 10; -fx-padding: 8;");

	    // Creating a circular progress indicator
	    ProgressIndicator progressIndicator = new ProgressIndicator();
	    progressIndicator.setStyle("-fx-progress-color: #2980B9; -fx-min-width: 70; -fx-min-height: 70;");

	    // VBox for arranging the progress indicator and label vertically
	    BorderPane borderPane = new BorderPane();
	    borderPane.setTop(progressIndicator);
	    borderPane.setCenter(waitLabel);
	    BorderPane.setAlignment(progressIndicator, Pos.TOP_CENTER);
	    BorderPane.setAlignment(waitLabel, Pos.CENTER);

	 // String array for remaining button labels
	    String[] strings = {
	        "Show header",
	        "Show Huffman table","show statistics",
	        "Back to main page"
	    };


	    // Adding the borderPane to the layout
	    layout.add(borderPane, 0, 0);
	    layout.setPrefSize(Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());

	    // Simulating the compression process without threads
	    compress(); // Compressing the file

	    // Updating the UI elements after the compression is completed
	    if (lengthFileBefore > 0) {
	        // Displaying results of the compression
	        Label resultsLabel = new Label("Results of file compression:");
	        resultsLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
	        resultsLabel.setStyle("-fx-text-fill: #117864; -fx-border-color: #2ECC71; -fx-border-radius: 10; -fx-padding: 12;");

//	        Label doneCorrectlyLabel = new Label(
//	            "File size Before compress: " + lengthFileBefore + " bytes\n" +
//	            "File size After compress: " + lengthFileAfter + " bytes\n" +
//	            "Compression Ratio: " + String.format("%.5f", (double) lengthFileAfter / lengthFileBefore)
//	        );
//	        doneCorrectlyLabel.setFont(Font.font("Cambria", 18));
//	        doneCorrectlyLabel.setStyle("-fx-background-color: #F9E79F; -fx-border-color: #F4D03F; -fx-border-radius: 8;");

	        Label compressedFileNameLabel = new Label("Compressed file name: " + nameOfCompressedFile);
	        compressedFileNameLabel.setFont(Font.font("Tahoma", 18));
	        compressedFileNameLabel.setStyle("-fx-text-fill: #1F618D; -fx-border-color: #85C1E9; -fx-border-radius: 8;");

	        // Creating buttons and setting them up
	        ButtonBase[] buttonBases = new ButtonBase[strings.length];
	        setupButtons(strings, buttonBases);

	        GridPane buttonGrid = new GridPane();
	        buttonGrid.setHgap(20);
	        buttonGrid.setVgap(20);
	        for (int i = 0; i < buttonBases.length; i++) {
	            buttonGrid.add(buttonBases[i], i % 2, i / 2);
	        }

	        // Adding all elements to the layout
	        layout.getChildren().clear();
	        layout.add(resultsLabel, 0, 0);
//	        layout.add(doneCorrectlyLabel, 0, 1);
	        layout.add(compressedFileNameLabel, 0, 2);
	        layout.add(buttonGrid, 0, 3);

	    } else {
	        // Handling the case where the file is empty
	        Label failLabel = new Label("The file is empty and cannot be compressed.");
	        failLabel.setFont(Font.font("Georgia", FontWeight.BOLD, 24));
	        failLabel.setStyle("-fx-text-fill: #CB4335; -fx-background-color: #FADBD8; -fx-border-color: #EC7063; -fx-border-radius: 8;");

	        Button backButton = new Button("Back to main page");
	        backButton.setStyle("-fx-background-color: #CB4335; -fx-text-fill: white; -fx-font-size: 16px; -fx-border-radius: 25;");
	        backButton.setOnAction(e -> SceneController.setMainScene());

	        VBox failVBox = new VBox(20, failLabel, backButton);
	        failVBox.setAlignment(Pos.CENTER);

	        layout.getChildren().clear();
	        layout.add(failVBox, 0, 0);
	    }
	}
	private TreeNode buildHuffmanTree(StringBuilder header) {
	    // Initialize your custom Stack with an appropriate size
	    Stack stack = new Stack(header.length());
	    int index = 0;

	    while (index < header.length()) {
	        if (header.charAt(index) == '0') {
	            // Internal node
	            stack.push(new TreeNode(null, null, null));
	            index++;
	        } else if (header.charAt(index) == '1') {
	            // Leaf node
	            index++;
	            if (index + 8 <= header.length()) {
	                String binaryValue = header.substring(index, index + 8);
	                char character = (char) Integer.parseInt(binaryValue, 2);
	                stack.push(new TreeNode(character, null, null)); // Create leaf node
	                index += 8;
	            }
	        }

	        // Combine nodes into a subtree if stack has at least two nodes
	        if (stack.size() >= 2) {
	            TreeNode right = stack.pop(); // Right child
	            TreeNode left = stack.pop(); // Left child
	            stack.push(new TreeNode(null, left, right)); // Create parent node and push it back
	        }
	    }

	    // Return the final tree root (top of the stack)
	    return stack.isEmpty() ? null : stack.pop();
	}

	public void setupButtons(String[] strings, ButtonBase[] buttonBases) {

	    // Loop through the remaining strings to create and configure buttons
	    for (int i = 0; i < strings.length; i++) {
	        Button button = new Button(strings[i]); // Create a new button with label from the strings array
	        buttonBases[i] = button; // Store the button in the 'buttonBases' array
	        button.getStyleClass().add("custom-button"); // Add a CSS class for styling
	        button.setPrefHeight(50); // Set the preferred height for the button
	        button.setPrefWidth(300); // Set the preferred width for the button
	    }

	 

	    // Call handleShowHeader with a valid TreeNode root
	    buttonBases[0].setOnAction(e -> SceneController.handleShowHeader(headerToShow));

	    // Set action for the second button: Show Huffman table
	    buttonBases[1].setOnAction(e -> SceneController.handleShowHuffmanTable(bytes, true));
	 // Set action for the second button: Show Huffman table
	    buttonBases[2].setOnAction(e -> showstatistics((GridPane) getRoot(), lengthFileBefore, lengthFileAfter));
	    // Set action for the third button: Return to the main scene
	    buttonBases[3].setOnAction(e -> SceneController.setMainScene());
	}


	public void showstatistics(GridPane layout, long beforeSize, long afterSize) {
	    // Calculate the compression ratio
		double compressionRatio = (1.0 - ((double) afterSize / beforeSize)) * 100; // Correct space saving percentage

	    // Create a label to display the statistics
	    Label statsLabel = new Label(String.format(
	        "File size Before compress: %d bytes\n" +
	        "File size After compress: %d bytes\n" +
	        "Compression Ratio: %.2f%%",
	        beforeSize, afterSize, compressionRatio
	    ));

	    // Style the label
	    statsLabel.setFont(Font.font("Verdana", 14));
	    statsLabel.setStyle("-fx-background-color: #fdf5c4; -fx-text-fill: #000000; -fx-padding: 15px;");
	    statsLabel.setPadding(new Insets(10));

	    // Add the statistics label dynamically to the layout
	    if (!layout.getChildren().contains(statsLabel)) { // Ensure no duplicate label
	        layout.add(statsLabel, 0, 4); // Add below the other content in row 4
	    }
	}


	public void compress() {
	    try {
	    	
	        // Initialize an array of TreeNode objects representing all possible byte values
	        bytes = new TreeNode[256];
	        for (int i = 0; i < 256; i++) {
	            bytes[i] = new TreeNode((byte) i);
	        }

	        // Record the file size before compression
	        lengthFileBefore = path.length();

	        // If the file is empty, exit the method
	        if (lengthFileBefore == 0)
	            return;

	        // Buffer for reading the file
	        byte[] bufferIn = new byte[8];

	        // Read the file and count the frequency of each byte
	        try (FileInputStream scan = new FileInputStream(path)) {
	            int countOfBytesInBuffer;
	            while ((countOfBytesInBuffer = scan.read(bufferIn)) != -1) {
	                for (int i = 0; i < countOfBytesInBuffer; i++) {
	                    if (bufferIn[i] < 0)
	                        bytes[bufferIn[i] + 256].increment();
	                    else
	                        bytes[bufferIn[i]].increment();
	                }
	            }
	        } catch (Exception e) {
	        	SceneController.showAlert("Error", e.getMessage());
	        }

	        // Create a heap to build the Huffman tree
	        Heap heap = new Heap(256);
	        for (TreeNode byteNode : bytes) {
	            if (byteNode.getFrequency() != 0)
	                heap.insert(byteNode);
	        }

	        // Build the Huffman tree
	        while (heap.getSize() > 1) {
	            TreeNode x = heap.remove();
	            TreeNode y = heap.remove();
	            TreeNode z = new TreeNode(x.getFrequency() + y.getFrequency());
	            z.setLeft(x);
	            z.setRight(y);
	            heap.insert(z);
	        }
	        TreeNode rootTreeNode = heap.remove();

	        // Set the code for the root node if it's the only node
	        if (rootTreeNode.getLeft() == null && rootTreeNode.getRight() == null)
	            rootTreeNode.setCode("1");
	        else
	            TreeNode.gaveCodeForEachByte(rootTreeNode);

	        // Build the header for the Huffman tree
	        StringBuilder header = new StringBuilder(rootTreeNode.traverse());
	        
	        // Get the file extension
	        String fileExtension = path.getName().substring(path.getName().lastIndexOf(".") + 1);
	        for (int i = 0; i < bufferIn.length; i++) {
	            if (i < fileExtension.length()) {
	                bufferIn[i] = (byte) fileExtension.charAt(i);
	                headerToShow.append(SceneController.byteToBinaryString(bufferIn[i]));
	            } else {
	                bufferIn[i] = (byte) 0;
	                headerToShow.append("00000000");
	            }
	        }

	        // Convert the header length to a byte array
	        byte[] bufferForHeaderSize = {
	                (byte) (header.length() >> 24),
	                (byte) (header.length() >> 16),
	                (byte) (header.length() >> 8),
	                (byte) header.length()
	            };
	        for (byte b : bufferForHeaderSize) {
	            headerToShow.append(SceneController.byteToBinaryString(b));
	        }

	        // Padding the header to make its length a multiple of 8
	        if (header.length() % 8 != 0) {
	            int paddingLength = 8 - header.length() % 8;
	            for (int i = 0; i < paddingLength; i++) {
	                header.append("0");
	            }
	        }
	        headerToShow.append(header);

	        // Prepare the name for the compressed file
	        nameOfCompressedFile = new StringBuilder(SceneController.replaceExtension(path.getName(), "huf"));
	        SceneController.getUniquName(nameOfCompressedFile);
	        FileOutputStream out = new FileOutputStream(nameOfCompressedFile.toString());

	        // Write the file extension and header size to the compressed file
	        out.write(bufferIn);
	        out.write(bufferForHeaderSize);

	        // Write the header to the compressed file
	        int numOfBytes = header.length() / 8;
	        int sizeForLastBuffer = numOfBytes % 8;
	        for (int i = 0; i < numOfBytes; i++) {
	            String byteString = header.substring(i * 8, (i + 1) * 8);
	            bufferIn[i % 8] = (byte) Integer.parseInt(byteString, 2);
	            if (i % 8 == 7)
	                out.write(bufferIn);
	        }
	        if (sizeForLastBuffer > 0)
	            out.write(bufferIn, 0, sizeForLastBuffer);

	        // Compress the data using the Huffman codes
	        StringBuilder data = new StringBuilder();
	        byte[] bufferOut = new byte[8];
	        int bufferLength;
	        try (FileInputStream scan = new FileInputStream(path)) {
	            while ((bufferLength = scan.read(bufferIn)) != -1) {
	                for (int k = 0; k < bufferLength; k++) {
	                    if (bufferIn[k] < 0)
	                        data.append(bytes[bufferIn[k] + 256].getCode());
	                    else
	                        data.append(bytes[bufferIn[k]].getCode());

	                    if (data.length() >= 64) {
	                        for (int i = 0; i < 8; i++) {
	                            bufferOut[i] = (byte) Integer.parseInt(data.substring(0, 8), 2);
	                            data.delete(0, 8);
	                        }
	                        out.write(bufferOut);
	                    }
	                }
	            }
	        } catch (Exception e) {
	        	SceneController.showAlert("Error", e.getMessage());
	        }

	        // Handle the remaining bits
	        int numberOfEffectiveBits = data.length() % 8;
	        if (numberOfEffectiveBits % 8 != 0) {
	            int paddingLength = 8 - numberOfEffectiveBits;
	            for (int i = 0; i < paddingLength; i++)
	                data.append("0");
	        } else {
	            numberOfEffectiveBits = 8;
	        }

	        if (data.length() > 0) {
	            int remainsBytes = data.length() / 8;
	            byte[] bufferOut1 = new byte[remainsBytes];
	            for (int i = 0; i < remainsBytes; i++) {
	                bufferOut1[i] = (byte) Integer.parseInt(data.substring(0, 8), 2);
	                data.delete(0, 8);
	            }
	            out.write(bufferOut1);
	        }

	        // Write the number of effective bits in the last byte
	        out.write((byte) numberOfEffectiveBits);
	        out.close();
            System.out.println("Compressed file saved at: " + nameOfCompressedFile);

	        // Check the size of the compressed file
	        File toCheck = new File(nameOfCompressedFile.toString());
	        lengthFileAfter = toCheck.length();

	    } catch (Exception e) {
	    	SceneController.showAlert("Error", e.getMessage());
	    }
	}

	
	
    
//	private void handleShowHuffmanTable() {
//	    ScrollPane subScenePane = new ScrollPane();
//	    subScenePane.setStyle("-fx-background: #212121;"); // Dark background for the scroll pane
//
//	    HBox hBox = new HBox(80);
//	    hBox.setAlignment(Pos.CENTER);
//	    hBox.setPadding(new Insets(50));
//	    hBox.setStyle("-fx-background-color: #1e272e;"); // Darker background for contrast
//
//	    GridPane gridPane = new GridPane();
//	    gridPane.setVgap(15); // Increased vertical gap for better readability
//	    gridPane.setHgap(50); // Increased horizontal gap for better spacing
//	    gridPane.setPadding(new Insets(20));
//	    gridPane.setAlignment(Pos.CENTER);
//	    gridPane.setStyle("-fx-background-color: #485460; -fx-border-radius: 15px; -fx-background-radius: 15px;"); // Rounded corners
//
//	    // Header
//	    Label[] headers = {
//	        new Label("Character"),
//	        new Label("ASCII"),
//	        new Label("Huffman"),
//	        new Label("Frequency"),
//	        new Label("Length")
//	    };
//	    int columnIndex = 0;
//	    for (Label header : headers) {
//	        header.setStyle("-fx-text-fill: #ffdd59; -fx-font-weight: bold; -fx-font-size: 14px;"); // Yellow header text
//	        GridPane.setConstraints(header, columnIndex, 0); // column, row
//	        GridPane.setHalignment(header, HPos.CENTER); // Center alignment
//	        gridPane.getChildren().add(header);
//	        columnIndex++;
//	    }
//
//	 // Add TreeNode details to the GridPane
//	    int rowIndex = 1;
//	    for (TreeNode node : bytes) {
//	        if (node.getCode() != null) {
//	            String byteContentBinary = String.format("%8s", Integer.toBinaryString(node.getByteContent() & 0xFF)).replace(' ', '0'); // Convert Byte to binary string
//	            int asciiValue;
//	            char character;
//
//	            try {
//	                asciiValue = Integer.parseInt(byteContentBinary, 2); // Convert binary string to ASCII value
//	                character = (char) asciiValue; // Convert ASCII value to character
//	            } catch (NumberFormatException e) {
//	                // Handle cases where byteContent is not a valid binary string
//	                asciiValue = -1; // Invalid ASCII value
//	                character = '?'; // Placeholder for invalid characters
//	            }
//
//	            // Create labels for each row
//	            Label charLabel = new Label(character == 0 ? "NULL" : String.valueOf(character)); // Handle null character
//	            Label asciiLabel = new Label(String.valueOf(asciiValue));
//	            Label codeLabel = new Label(node.getCode());
//	            Label frequencyLabel = new Label(String.valueOf(node.getFrequency()));
//	            Label lengthLabel = new Label(String.valueOf(node.getCode().length()));
//
//	            // Apply styling
//	            charLabel.setStyle("-fx-text-fill: #d2dae2;");
//	            asciiLabel.setStyle("-fx-text-fill: #d2dae2;");
//	            codeLabel.setStyle("-fx-text-fill: #d2dae2;");
//	            frequencyLabel.setStyle("-fx-text-fill: #d2dae2;");
//	            lengthLabel.setStyle("-fx-text-fill: #d2dae2;");
//
//	            GridPane.setHalignment(charLabel, HPos.CENTER);
//	            GridPane.setHalignment(asciiLabel, HPos.CENTER);
//	            GridPane.setHalignment(codeLabel, HPos.CENTER);
//	            GridPane.setHalignment(frequencyLabel, HPos.CENTER);
//	            GridPane.setHalignment(lengthLabel, HPos.CENTER);
//
//	            // Add labels to grid
//	            gridPane.add(charLabel, 0, rowIndex);
//	            gridPane.add(asciiLabel, 1, rowIndex);
//	            gridPane.add(codeLabel, 2, rowIndex);
//	            gridPane.add(frequencyLabel, 3, rowIndex);
//	            gridPane.add(lengthLabel, 4, rowIndex);
//
//	            rowIndex++;
//	        }
//	    }
//
//	    // Create a new BorderPane to hold the table with padding and background
//	    BorderPane tableContainer = new BorderPane();
//	    tableContainer.setCenter(gridPane);
//	    tableContainer.setPadding(new Insets(20));
//	    tableContainer.setStyle("-fx-background-color: #0fbcf9; -fx-border-color: #2f3542; " +
//	            "-fx-border-width: 3px; -fx-border-radius: 15px; -fx-background-radius: 15px;");
//
//	    // Create a VBox for the title and table container
//	    VBox background = new VBox(30);
//	    Label huffmanLabel = new Label("Huffman Table :");
//	    huffmanLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
//	    huffmanLabel.setStyle("-fx-text-fill: #1e90ff;");
//	    background.setAlignment(Pos.CENTER);
//	    background.setPadding(new Insets(40));
//	    background.setStyle("-fx-background-color: #1e272e; -fx-border-radius: 20px; -fx-background-radius: 20px;");
//	    background.getChildren().addAll(huffmanLabel, tableContainer);
//
//	    hBox.getChildren().addAll(background);
//	    subScenePane.setContent(hBox);
//	    subScenePane.setFitToWidth(true);
//	    subScenePane.setFitToHeight(true);
//
//	    Scene scene = new Scene(subScenePane, 800, 600); // Increased scene size for better viewing
//	    Stage newStage = new Stage();
//	    newStage.setScene(scene);
//	    hBox.prefWidthProperty().bind(newStage.widthProperty());
//	    hBox.prefHeightProperty().bind(newStage.heightProperty());
//	    newStage.show();
//	}

//	public static void handleShowHeader(StringBuilder headerToShow, TreeNode[] bytes) {
//	    StringBuilder header = new StringBuilder(headerToShow);
//
//	    // Create the layout
//	    ScrollPane subScenePane = new ScrollPane();
//	    VBox background = new VBox(15);
//	    background.setPadding(new Insets(40));
//	    background.setStyle("-fx-background-color: #2b2b2b; -fx-border-color: #555555; -fx-border-width: 2px; -fx-background-radius: 10px;");
//
//	    // Extract the file extension
//	    String extension = "";
//	    for (int i = 0; i < 8; i++) {
//	        extension += (char) Integer.parseInt(header.substring(i * 8, (i + 1) * 8), 2);
//	    }
//	    header.delete(0, 64); // Remove the processed extension
//	    Label extensionLabel = new Label("File Extension: " + extension.trim());
//	    extensionLabel.setFont(Font.font("Verdana", 15));
//	    extensionLabel.setStyle("-fx-text-fill: #FFFFFF;");
//
//	    // Post-order traversal
//	    Label treeLabel = new Label("Binary Tree Representation (Postorder):");
//	    treeLabel.setFont(Font.font("Verdana", 15));
//	    treeLabel.setStyle("-fx-text-fill: #FFFFFF;");
//
//	    StringBuilder treeRepresentation = new StringBuilder();
//
//	    // Create a Huffman code lookup map from the bytes array
//	    Map<Character, String> huffmanCodeMap = new HashMap<>();
//	    for (TreeNode node : bytes) {
//	        if (node.getCode() != null && node.getCharacter() != null) {
//	            huffmanCodeMap.put(node.getCharacter(), node.getCode());
//	        }
//	    }
//
//	    try {
//	        postOrderTraversal(header, 0, treeRepresentation, huffmanCodeMap, "");
//	    } catch (IllegalArgumentException e) {
//	        treeRepresentation.append("Error during traversal: ").append(e.getMessage());
//	    }
//
//	    TextArea treeTextArea = new TextArea(treeRepresentation.toString());
//	    treeTextArea.setEditable(false);
//	    treeTextArea.setStyle("-fx-control-inner-background: #2b2b2b; -fx-text-fill: #64fc9c;");
//	    treeTextArea.setPrefHeight(400);
//
//	    // Add all components to the layout
//	    background.getChildren().addAll(extensionLabel, treeLabel, treeTextArea);
//	    subScenePane.setContent(background);
//	    Scene scene = new Scene(subScenePane, 800, 600);
//	    Stage newStage = new Stage();
//	    newStage.setScene(scene);
//	    newStage.show();
//	}
//
//	// Updated Post-order Traversal with Huffman Code Lookup
//	private static int postOrderTraversal(StringBuilder header, int index, StringBuilder representation, Map<Character, String> huffmanCodeMap, String code) {
//	    if (index >= header.length()) return index;
//
//	    if (header.charAt(index) == '1') {
//	        // Leaf node
//	        index++;
//	        if (index + 8 > header.length()) {
//	            representation.append("Leaf: Unknown (Malformed Header)\n");
//	            throw new IllegalArgumentException("Incomplete header for a leaf node");
//	        }
//	        String binaryValue = header.substring(index, index + 8);
//	        char character = (char) Integer.parseInt(binaryValue, 2);
//
//	        String charDisplay = (character == ' ') ? "Space" : character == '\n' ? "Newline" : String.valueOf(character);
//	        String huffmanCode = huffmanCodeMap.getOrDefault(character, "Unknown");
//
//	        representation.append("Leaf: '").append(charDisplay)
//	                .append("' (Huffman Code: ").append(huffmanCode).append(")\n");
//	        return index + 8;
//	    } else if (header.charAt(index) == '0') {
//	        // Internal node
//	        representation.append("Internal Node ").append(")\n");
//	        index++;
//	    }
//
//	    // Traverse left and right children
//	    index = postOrderTraversal(header, index, representation, huffmanCodeMap, code + "0");
//	    index = postOrderTraversal(header, index, representation, huffmanCodeMap, code + "1");
//
//	    return index;
//	}






}