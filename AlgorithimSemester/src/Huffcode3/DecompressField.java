package Huffcode3;

import java.awt.Desktop;
import java.io.*;
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

public class DecompressField extends Scene {

    File path; // Path of the file to be uncompressed
    TreeNode[] bytes; // Array to store TreeNode objects for Huffman tree
    long lengthFileBefore; // Length of the file before uncompression
    long lengthFileAfter; // Length of the file after uncompression
    StringBuilder headerToShow = new StringBuilder(""); // StringBuilder to construct and show header information
    StringBuilder nameOfUncompressedFile; // StringBuilder to store the name of the uncompressed file

    public DecompressField(File path) {
        // Set the scene size
        super(new StackPane(), 1100, 550);
        StackPane layout = (StackPane) getRoot();
        layout.getStyleClass().add("root");
        this.path = path; // Assign the file path
        HuffmanSceneImages.setBackground(layout, "file:/Users/mustafaalayasa/Desktop/eclipse-workspace/JavaFx-workspace/AlgorithimSemester/src/a2356902725_10.jpg");

        // Creating a progress indicator for the decompression process
        Label waitLabel = new Label("Please wait, the file will be uncompressed soon");
        waitLabel.setFont(Font.font("Century Gothic", 20));
        ProgressIndicator progressIndicator = new ProgressIndicator();
        VBox vBox = new VBox(40, progressIndicator, waitLabel);
        vBox.setAlignment(Pos.CENTER);
        layout.getChildren().add(vBox);

        // Start the decompression process
        uncompress();

        // Remove progress indicator after decompression
        vBox.getChildren().removeAll(progressIndicator, waitLabel);

        // Show results after decompression
        Label welcomeLabel = new Label("Results for uncompressing the file");
        welcomeLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 30));
        welcomeLabel.setStyle("-fx-text-fill: #000000;");
        Label doneCorrectlyLabel = new Label(
            "File size Before uncompress: " + lengthFileBefore + " bytes, " +
            "File size After uncompress: " + lengthFileAfter + " bytes");
        doneCorrectlyLabel.setFont(Font.font("Century Gothic", 20));
        doneCorrectlyLabel.setStyle("-fx-text-fill: #000000;");

        Label nameOfUncompressedFileLabel = new Label(
            "The path of the uncompressed file is: " + nameOfUncompressedFile);
        nameOfUncompressedFileLabel.setFont(Font.font("Century Gothic", 20));
        nameOfUncompressedFileLabel.setStyle("-fx-text-fill: #000000;");

        // Buttons
        String[] buttonLabels = {
            "Open uncompressed file",
            "Show Huffman Table",
            "Show Header",
            "Back to main menu"
        };

        Button[] buttons = new Button[buttonLabels.length];
        setupButtons(buttonLabels, buttons);

        VBox buttonBox = new VBox(20);
        buttonBox.setAlignment(Pos.TOP_CENTER);
        buttonBox.getChildren().addAll(buttons);

        // Add elements to the VBox
        vBox.getChildren().addAll(welcomeLabel, doneCorrectlyLabel, nameOfUncompressedFileLabel, buttonBox);
    }

    private void setupButtons(String[] labels, Button[] buttons) {
        for (int i = 0; i < labels.length; i++) {
            buttons[i] = new Button(labels[i]);
            buttons[i].setStyle("-fx-background-color: #2980B9; -fx-text-fill: white; -fx-font-size: 16px; -fx-background-radius: 10;");
            buttons[i].setPrefWidth(300);
            buttons[i].setPrefHeight(50);

            final int index = i;
            buttons[i].setOnAction(e -> {
                switch (index) {
                    case 0:
                        openUncompressedFile(); // Logic to open the uncompressed file
                        break;
                    case 1:
                        SceneController.handleShowHuffmanTable(bytes, false); // Show Huffman table
                        break;
                    case 2:
                        SceneController.handleShowHeader(headerToShow); // Show header
                        break;
                    
                    case 3:
                        SceneController.setMainScene(); // Back to main menu
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid button index: " + index);
                }
            });
        }
    }

//    public void showStatistics(StackPane layout, long beforeSize, long afterSize) {
//        // Calculate the compression ratio
//        double compressionRatio = (1.0 - ((double) afterSize / beforeSize)) * 100;
//
//        // Create a label to display the decompression statistics
//        Label statsLabel = new Label(String.format(
//            "File size Before decompress: %d bytes\n" +
//            "File size After decompress: %d bytes\n" +
//            "Compression Ratio: %.2f%%",
//            beforeSize, afterSize, compressionRatio
//        ));
//
//        // Style the label
//        statsLabel.setFont(Font.font("Verdana", 14));
//        statsLabel.setStyle("-fx-background-color: #fdf5c4; -fx-text-fill: #000000; -fx-padding: 15px;");
//        statsLabel.setPadding(new Insets(10));
//
//        // Ensure the statistics label is not added multiple times
//        if (!layout.getChildren().contains(statsLabel)) {
//            layout.getChildren().add(statsLabel); // Add the label to the StackPane
//            StackPane.setAlignment(statsLabel, Pos.BOTTOM_CENTER); // Position at the bottom center
//        }
//    }



    private void openUncompressedFile() {
        try {
            Desktop.getDesktop().open(new File(nameOfUncompressedFile.toString()));
        } catch (IOException e) {
            SceneController.showAlert("Error", "Unable to open the uncompressed file.");
        }
    }

	
    public void uncompress() {
        try {
            // Record the file size before uncompression
            lengthFileBefore = path.length();

            // Create a FileInputStream to read the compressed file
            FileInputStream scan = new FileInputStream(path);

            // StringBuilder to store the file extension
            StringBuilder fileExtension = new StringBuilder();

            // Buffers for reading the file
            byte[] buffer = new byte[8]; // Buffer for general purpose
            byte[] sizeOfHeaderBuffer = new byte[4]; // Buffer for the size of the header
            int sizeOfHeader = 0;

            // Read the first 8 bytes of the file to determine the file extension
            if (scan.read(buffer) != -1) {
                for (int i = 0; i < 8; i++) {
                    if (buffer[i] != 0) {
                        fileExtension.append((char) buffer[i]);
                    }
                    headerToShow.append(SceneController.byteToBinaryString(buffer[i]));
                }

                // Read the next 4 bytes to determine the size of the header
                scan.read(sizeOfHeaderBuffer);
                sizeOfHeader = byteArrayToInt(sizeOfHeaderBuffer);

                // Append size of header to headerToShow StringBuilder
                for (int i = 0; i < sizeOfHeaderBuffer.length; i++) {
                    headerToShow.append(SceneController.byteToBinaryString(sizeOfHeaderBuffer[i]));
                }
            } else {
                scan.close();
                throw new IllegalArgumentException("The input file cannot be read");
            }

            // Calculate the number of bytes for the header
            int numberOfBytesForHeader;
            if (sizeOfHeader % 8 == 0) {
                numberOfBytesForHeader = sizeOfHeader / 8;
            } else {
                numberOfBytesForHeader = (sizeOfHeader / 8) + 1;
            }

            // StringBuilders for header and the serialized data
            StringBuilder header = new StringBuilder();
            StringBuilder serialData = new StringBuilder();
            int numberOfBytesRead = 0, counterHowManyBytesReadFromHeader = 0;

            // Read the rest of the file
            while ((numberOfBytesRead = scan.read(buffer)) != -1) {
                for (int i = 0; i < numberOfBytesRead; i++) {
                    if (counterHowManyBytesReadFromHeader < numberOfBytesForHeader) {
                        header.append(SceneController.byteToBinaryString(buffer[i]));
                        counterHowManyBytesReadFromHeader++;
                    } else {
                        serialData.append(SceneController.byteToBinaryString(buffer[i]));
                    }
                }
            }
            scan.close();
            headerToShow.append(header);

            // Huffman tree reconstruction
            Stack stack = new Stack(256);
            int counter = 0, numberOfLeafNodes = 0;
            while (counter < sizeOfHeader) {
                if (header.charAt(counter) == '0') {
                    counter++;
                    stack.push(new TreeNode((byte) Integer.parseInt(header.substring(counter, counter + 8), 2)));
                    numberOfLeafNodes++;
                    counter += 8;
                } else {
                    counter++;
                    TreeNode node = new TreeNode(0);
                    node.setRight(stack.pop());
                    node.setLeft(stack.pop());
                    stack.push(node);
                }
            }
            TreeNode rootTreeNode = stack.peek();

            // Set the code for each node in the tree
            if (rootTreeNode.getLeft() == null && rootTreeNode.getRight() == null)
                rootTreeNode.setCode("1");
            else
                TreeNode.gaveCodeForEachByte(rootTreeNode);

            // Get all leaf nodes from the Huffman tree
            bytes = new TreeNode[numberOfLeafNodes];
            rootTreeNode.getLeafNodes(bytes);

            // Generate the path for the uncompressed file
            String originalDirectory = path.getParent(); // Get the directory of the original file
            nameOfUncompressedFile = new StringBuilder(originalDirectory + File.separator + SceneController.replaceExtension(path.getName(), fileExtension.toString()));
            SceneController.getUniquName(nameOfUncompressedFile);

            // Write the uncompressed data to a file
            FileOutputStream out = new FileOutputStream(nameOfUncompressedFile.toString());
            int startIndex = serialData.length() - 8;
            int numberOfEffectiveBits = Integer.parseInt(serialData.substring(startIndex), 2);
            serialData.delete(startIndex + numberOfEffectiveBits - 8, serialData.length());

            byte[] bufferOut = new byte[8];
            int counterForBufferSerialData = 0, counterForBufferOut = 0;

            while (counterForBufferSerialData < serialData.length()) {
                TreeNode curr = rootTreeNode;

                // Traverse the Huffman tree
                while (curr != null && counterForBufferSerialData < serialData.length()) {
                    if (serialData.charAt(counterForBufferSerialData) == '0' && curr.hasLeft())
                        curr = curr.getLeft();
                    else if (curr.hasRight())
                        curr = curr.getRight();
                    else if (rootTreeNode.getLeft() == null && rootTreeNode.getRight() == null) {
                        counterForBufferSerialData++;
                        break;
                    } else
                        break;

                    counterForBufferSerialData++;
                }

                // Write byte to the buffer
                bufferOut[counterForBufferOut++] = curr.getByteContent();
                if (counterForBufferOut == 8) {
                    out.write(bufferOut);
                    counterForBufferOut = 0;
                }
            }

            if (counterForBufferOut > 0)
                out.write(bufferOut, 0, counterForBufferOut);

            out.close();

            // Check the size of the uncompressed file
            File toCheck = new File(nameOfUncompressedFile.toString());
            lengthFileAfter = toCheck.length();

        } catch (Exception e) {
        	SceneController.showAlert("Error", e.getMessage());
        }
    }

 
	
	// Method to convert a byte array to an integer
	public static int byteArrayToInt(byte[] b) {
	    // Combines 4 bytes into an integer, assuming big-endian order
	    return   b[3] & 0xFF |
	            (b[2] & 0xFF) << 8 |
	            (b[1] & 0xFF) << 16 |
	            (b[0] & 0xFF) << 24;
	}

	

	

}