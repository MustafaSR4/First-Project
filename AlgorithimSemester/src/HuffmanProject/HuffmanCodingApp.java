package HuffmanProject;



import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Arrays;
import java.util.Optional;

public class HuffmanCodingApp extends Application {

    static final int ASCII_SIZE = 256;
    private TextArea outputArea;
    private Button compressButton, decompressButton, statisticsButton, huffmanTableButton, headerButton;
    private File selectedFile;
    private File compressedFile;
    private long originalSize;
    private int numCharacters;
    private long compressedSize;
    private String[] huffmanCodes;
    private int[] frequencies;
    private String originalExtension;
    private HuffmanNode huffmanTreeRoot;
    private VBox mainLayout;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Huffman Compression");

        // Main Layout
         mainLayout = new VBox(20);
        mainLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

        // Buttons
        compressButton = new Button("Compress");
        decompressButton = new Button("Decompress");
        statisticsButton = new Button("Show Statistics");
        huffmanTableButton = new Button("Show Huffman Table");
        headerButton = new Button("Show Header");

        // Disable buttons initially
        statisticsButton.setDisable(true);
        huffmanTableButton.setDisable(true);
        headerButton.setDisable(true);

        // TextArea for output
        outputArea = new TextArea();
        outputArea.setEditable(false);
        outputArea.setWrapText(true);
        outputArea.setPrefHeight(300);

        // Initialize and add TableView for Huffman Table
        setupTableView(); // Ensure the TableView is initialized
        mainLayout.getChildren().add(huffmanTableView);

        // Add other components to layout
        mainLayout.getChildren().addAll(compressButton, decompressButton, statisticsButton, huffmanTableButton, headerButton, outputArea);

        // Set the Scene and Show
        Scene scene = new Scene(mainLayout, 700, 900);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Button Actions
        compressButton.setOnAction(event -> compressFile(primaryStage));
        decompressButton.setOnAction(event -> decompressFile(primaryStage));
        statisticsButton.setOnAction(event -> showStatistics());
        huffmanTableButton.setOnAction(event -> showHuffmanTable()); // Show populated Huffman table
        headerButton.setOnAction(event -> showHeader());
    }

    private void compressFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select File to Compress");
        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            try {
                // Validate file
                if (getFileExtension(selectedFile).equalsIgnoreCase("hof")) {
                    outputArea.setText("Error: Cannot compress an already compressed file.");
                    return;
                }
                if (selectedFile.length() == 0) {
                    outputArea.setText("Error: Selected file is empty and cannot be compressed.");
                    return;
                }

                // Calculate frequencies
                frequencies = new int[ASCII_SIZE];
                numCharacters = readFileAndCountFrequencies(selectedFile, frequencies);

                // Build Huffman Tree and generate codes
                HuffmanTree huffmanTree = new HuffmanTree();
                huffmanTreeRoot = huffmanTree.buildTree(frequencies);
                huffmanCodes = new String[ASCII_SIZE];
                huffmanTree.generateCodes(huffmanTreeRoot, "", huffmanCodes);

                // Create compressed file name
                originalExtension = getFileExtension(selectedFile);
                compressedFile = new File(selectedFile.getParent(),
                        selectedFile.getName().replaceFirst("[.][^.]+$", "") + ".hof");

                // Check if the file already exists
                if (compressedFile.exists()) {
                    if (!confirmOverwrite("A compressed file with this name already exists.")) {
                        outputArea.setText("Compression cancelled. File overwrite not confirmed.");
                        return;
                    }
                }

                // Compress the file
                HuffmanCompressor compressor = new HuffmanCompressor();
                compressor.compressFile(selectedFile, compressedFile, huffmanCodes, originalExtension, frequencies);

                // Update output
                outputArea.setText("Compression completed!\nCompressed file: " + compressedFile.getAbsolutePath());
                enableButtons();

            } catch (IOException e) {
                outputArea.setText("Error during compression: " + e.getMessage());
            }
        } else {
            outputArea.setText("No file selected.");
        }
    }

    private boolean confirmOverwrite(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("File Already Exists");
        alert.setHeaderText(message);
        alert.setContentText("Do you want to overwrite it?");
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    private void enableButtons() {
        statisticsButton.setDisable(false);
        huffmanTableButton.setDisable(false);
        headerButton.setDisable(false);
    }
    private void decompressFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Compressed File (.hof)");
        File hofFile = fileChooser.showOpenDialog(stage);

        if (hofFile != null) {
            try {
                // Validate file
                if (!getFileExtension(hofFile).equalsIgnoreCase("hof")) {
                    outputArea.setText("Error: Selected file is not a compressed (.hof) file.");
                    return;
                }
                if (hofFile.length() == 0) {
                    outputArea.setText("Error: Selected file is empty and cannot be decompressed.");
                    return;
                }

                // Initialize decompressor
                HuffmanDecompressor decompressor = new HuffmanDecompressor();
                String originalExtension = decompressor.getOriginalExtension(hofFile);

                // Get output file name
                String outputFileName = getOutputFileName(stage, originalExtension);
                if (outputFileName == null) {
                    outputArea.setText("Decompression cancelled. No output file name provided.");
                    return;
                }

                // Check if output file already exists
                File outputFile = new File(hofFile.getParent(), outputFileName);
                if (outputFile.exists()) {
                    if (!confirmOverwrite("File already exists with the name: " + outputFileName)) {
                        outputArea.setText("Decompression cancelled. File overwrite not confirmed.");
                        return;
                    }
                }

                File decompressedFile = decompressor.decompressFile(hofFile, outputFile.getAbsolutePath());
                if (decompressedFile != null && decompressedFile.exists()) {
                    outputArea.setText("Decompression completed successfully!\nDecompressed file: "
                        + decompressedFile.getAbsolutePath());
                } else {
                    throw new IOException("Decompression failed. Output file not created.");
                }


            } catch (IOException e) {
                outputArea.setText("Error during decompression: " + e.getMessage());
            }
        } else {
            outputArea.setText("No file selected for decompression.");
        }
    }

    private String getOutputFileName(Stage stage, String extension) {
        TextInputDialog dialog = new TextInputDialog("decompressed_output");
        dialog.setTitle("Output File Name");
        dialog.setHeaderText("Enter the name for the decompressed file:");
        dialog.setContentText("File name:");
        Optional<String> result = dialog.showAndWait();
        return result.isPresent() && !result.get().isBlank() ? result.get() + "." + extension : null;
    }




  
   



	private void showStatistics() {
        if (selectedFile == null || compressedFile == null) {
            outputArea.setText("Please compress a file first.");
            return;
        }

        // Calculate compression ratio
        double compressionRatio = (1.0 - ((double) compressedSize / originalSize)) * 100;

        // Convert sizes
        long originalSizeBits = originalSize * 8; // Original size in bits
        long compressedSizeBits = compressedSize * 8; // Compressed size in bits

        double originalSizeKB = originalSize / 1024.0; // Original size in kilobytes
        double compressedSizeKB = compressedSize / 1024.0; // Compressed size in kilobytes

        // Prepare header for statistics
        StringBuilder stats = new StringBuilder();
        stats.append(String.format(
            "Statistics:\n" +
            "Original Size:\n" +
            "- %d bits\n" +
            "- %d bytes\n" +
            "- %.2f KB\n\n" +
            "Compressed Size:\n" +
            "- %d bits\n" +
            "- %d bytes\n" +
            "- %.2f KB\n\n" +
            "Compression Ratio: %.2f%%\n\n",
            originalSizeBits, originalSize, originalSizeKB,
            compressedSizeBits, compressedSize, compressedSizeKB,
            compressionRatio
        ));

       

        // Display statistics in the output area
        outputArea.setText(stats.toString());
    }



 // Declare the TableView
    private TableView<HuffmanTableRow> huffmanTableView= new TableView<>();

    private void setupTableView() {
        // Clear previous columns and data
        huffmanTableView.getColumns().clear();
        huffmanTableView.getItems().clear();

        // Column for ASCII Code
        TableColumn<HuffmanTableRow, Integer> asciiCol = new TableColumn<>("ASCII Code");
        asciiCol.setCellValueFactory(new PropertyValueFactory<>("asciiCode"));
        asciiCol.setPrefWidth(100);

        // Column for Character
        TableColumn<HuffmanTableRow, String> charCol = new TableColumn<>("Character");
        charCol.setCellValueFactory(new PropertyValueFactory<>("character"));
        charCol.setPrefWidth(100);

        // Column for Frequency
        TableColumn<HuffmanTableRow, Integer> freqCol = new TableColumn<>("Frequency");
        freqCol.setCellValueFactory(new PropertyValueFactory<>("frequency"));
        freqCol.setPrefWidth(100);

        // Column for Huffman Code
        TableColumn<HuffmanTableRow, String> huffmanCodeCol = new TableColumn<>("Huffman Code");
        huffmanCodeCol.setCellValueFactory(new PropertyValueFactory<>("huffmanCode"));
        huffmanCodeCol.setPrefWidth(200);

        // Column for Huffman Size
        TableColumn<HuffmanTableRow, Integer> huffmanSizeCol = new TableColumn<>("Huffman Size (Bits)");
        huffmanSizeCol.setCellValueFactory(new PropertyValueFactory<>("huffmanSize"));
        huffmanSizeCol.setPrefWidth(150);

        // Add all columns to the table
        huffmanTableView.getColumns().addAll(asciiCol, charCol, freqCol, huffmanCodeCol, huffmanSizeCol);
    }



    private void showHuffmanTable() {
        if (huffmanCodes == null || frequencies == null) {
            outputArea.setText("Please compress a file first.");
            return;
        }

        // Clear existing data
        huffmanTableView.getItems().clear();

        // Populate TableView with Huffman codes
        for (int i = 0; i < ASCII_SIZE; i++) {
            if (frequencies[i] > 0) {
                String huffmanCode = huffmanCodes[i];
                char character = (char) i;

                // Label whitespace and control characters appropriately
                String charDisplay;
                if (Character.isWhitespace(character)) {
                    switch (character) {
                        case ' ' -> charDisplay = "Space";
                        case '\t' -> charDisplay = "Tab";
                        case '\n' -> charDisplay = "Newline";
                        case '\r' -> charDisplay = "Carriage Return";
                        case '\f' -> charDisplay = "Form Feed";
                        default -> charDisplay = "Other Whitespace";
                    }
                } else if (Character.isISOControl(character)) {
                    charDisplay = "Control Character";
                } else {
                    charDisplay = String.valueOf(character);
                }

                // Calculate Huffman size (length of Huffman code)
                int huffmanSize = huffmanCode.length();

                // Add data to the TableView
                huffmanTableView.getItems().add(new HuffmanTableRow(i, charDisplay, frequencies[i], huffmanCode, huffmanSize));
            }
        }

        // Set output area with a message
        outputArea.setText("Huffman table displayed in TableView.");
    }

    
    
    
    
    
//
//    private void displayHuffmanTree(HuffmanNode root, String prefix, StringBuilder treeRepresentation) {
//        if (root == null) return;
//
//        if (root.left == null && root.right == null) {
//            // Leaf node: Display character and Huffman code
//            char character = root.character;
//            String charDisplay;
//            if (Character.isWhitespace(character)) {
//                switch (character) {
//                    case ' ': charDisplay = "Space"; break;
//                    case '\t': charDisplay = "Tab"; break;
//                    case '\n': charDisplay = "Newline"; break;
//                    case '\r': charDisplay = "Carriage Return"; break;
//                    default: charDisplay = "Other Whitespace"; break;
//                }
//            } else {
//                charDisplay = String.valueOf(character);
//            }
//            treeRepresentation.append(prefix).append(" -> ").append(charDisplay)
//                               .append(" (Huffman Code: ").append(prefix).append(")\n");
//        } else {
//            // Internal node
//            treeRepresentation.append(prefix).append(" -> [ ]\n");
//        }
//
//        // Traverse left (0) and right (1)
//        displayHuffmanTree(root.left, prefix + "0", treeRepresentation);
//        displayHuffmanTree(root.right, prefix + "1", treeRepresentation);
//    }
//
//
//
//    private void showHeader() {
//        if (compressedFile == null) {
//            outputArea.setText("Please compress a file first.");
//            return;
//        }
//
//        StringBuilder headerInfo = new StringBuilder();
//        headerInfo.append("Header:\n")
//                  .append("File Extension: ").append(originalExtension).append("\n")
//                  .append("Compressed File Path: ").append(compressedFile.getAbsolutePath()).append("\n\n");
//
//        if (huffmanTreeRoot != null) {
//            StringBuilder treeRepresentation = new StringBuilder("Huffman Tree Structure:\n");
//            displayHuffmanTree(huffmanTreeRoot, "", treeRepresentation);
//            headerInfo.append(treeRepresentation);
//        } else {
//            headerInfo.append("Huffman tree not available.\n");
//        }
//
//        outputArea.setText(headerInfo.toString());
//    }
    private void displayHuffmanTree(HuffmanNode root, String prefix, StringBuilder treeRepresentation) {
        if (root == null) return;

        if (root.left == null && root.right == null) {
            char character = root.character;
            String charDisplay;
            if (Character.isWhitespace(character)) {
                switch (character) {
                    case ' ': charDisplay = "Space"; break;
                    case '\t': charDisplay = "Tab"; break;
                    case '\n': charDisplay = "Newline"; break;
                    case '\r': charDisplay = "Carriage Return"; break;
                    default: charDisplay = "Other Whitespace"; break;
                }
            } else {
                charDisplay = String.valueOf(character);
            }
            treeRepresentation.append(prefix).append(" [Leaf] Character: ").append(charDisplay)
                               .append(" -> Huffman Code: ").append(prefix).append("\n");
        } else {
            treeRepresentation.append(prefix).append(" [Node] Prefix: ").append(prefix).append("\n");
        }

        displayHuffmanTree(root.left, prefix + "0", treeRepresentation);
        displayHuffmanTree(root.right, prefix + "1", treeRepresentation);
    }



    
    private void showHeader() {
        if (compressedFile == null) {
            outputArea.setText("Please compress a file first.");
            return;
        }

        StringBuilder headerInfo = new StringBuilder();
        headerInfo.append("Header:\n")
                  .append("File Extension: ").append(originalExtension).append("\n")
                  .append("Compressed File Path: ").append(compressedFile.getAbsolutePath()).append("\n\n");

        if (huffmanTreeRoot != null) {
            StringBuilder treeRepresentation = new StringBuilder("Huffman Tree Structure (Character -> Huffman Code):\n\n");
            displayHuffmanTree(huffmanTreeRoot, "", treeRepresentation);
            headerInfo.append(treeRepresentation);
        } else {
            headerInfo.append("Huffman tree not available.\n");
        }

        outputArea.setText(headerInfo.toString());
    }

    
    
    
//    private void showHeader() {
//        if (compressedFile == null) {
//            outputArea.setText("Please compress a file first.");
//            return;
//        }
//
//        StringBuilder headerInfo = new StringBuilder();
//        headerInfo.append("Header:\n")
//                  .append("File Extension: ").append(originalExtension).append("\n")
//                  .append("Compressed File Path: ").append(compressedFile.getAbsolutePath()).append("\n\n");
//
//        if (huffmanTreeRoot != null) {
//            StringBuilder treeRepresentation = new StringBuilder("Huffman Tree Structure:\n");
//            displayHuffmanTree(huffmanTreeRoot, "", treeRepresentation);
//            headerInfo.append(treeRepresentation);
//
//            // Add instruction to visualize the tree
//            headerInfo.append("\nTree visualization included below:");
//        } else {
//            headerInfo.append("Huffman tree not available.\n");
//        }
//
//        outputArea.setText(headerInfo.toString());
//
//        // Add tree visualization directly below the header
//        Pane graphPane = new Pane();
//        renderHuffmanTreeForceDirected(graphPane, huffmanTreeRoot, 400, 50, 200, 100);
//
//        VBox headerBox = new VBox(10, outputArea, graphPane);
//        headerBox.setAlignment(Pos.TOP_CENTER);
//
//        Scene headerScene = new Scene(headerBox, 800, 600);
//        Stage headerStage = new Stage();
//        headerStage.setTitle("Header Information with Tree Visualization");
//        headerStage.setScene(headerScene);
//        headerStage.show();
//    }
    

    
    




//    
//    private void displayHuffmanTreeStage() {
//        if (huffmanTreeRoot == null) {
//            outputArea.setText("Please compress a file first.");
//            return;
//        }
//
//        // Create a pane for the visualization
//        Pane graphPane = new Pane();
//
//        // Dimensions of the pane
//        double paneWidth = 800;
//        double paneHeight = 600;
//
//        // Render the Huffman tree statically
//        renderHuffmanTreeForceDirected(graphPane, huffmanTreeRoot, paneWidth / 2, 50, paneWidth / 4, paneHeight / calculateTreeHeight());
//
//        // Wrap the pane in a scrollable view
//        ScrollPane scrollPane = new ScrollPane(graphPane);
//
//        // Create and display the stage
//        Scene scene = new Scene(scrollPane, paneWidth, paneHeight);
//        Stage treeStage = new Stage();
//        treeStage.setTitle("Huffman Tree Visualization");
//        treeStage.setScene(scene);
//        treeStage.show();
//    }


//    private void renderHuffmanTreeForceDirected(Pane pane, HuffmanNode node, double x, double y, double xOffset, double yOffset, int currentDepth) {
//        if (node == null) return;
//
//        // Draw the current node as a circle
//        Circle circle = new Circle(x, y, 20);
//        circle.setFill(node.left == null && node.right == null ? Color.LIGHTGREEN : Color.LIGHTBLUE);
//        circle.setStroke(Color.BLACK);
//        pane.getChildren().add(circle);
//
//        // Add text inside the node for character or frequency
//        String textContent = (node.left == null && node.right == null) ? String.valueOf(node.character) : "";
//        Text text = new Text(x - 5, y + 5, textContent);
//        pane.getChildren().add(text);
//
//        // Recursively draw left and right children with updated depth
//        if (node.left != null) {
//            double childX = x - xOffset; // Move left for left child
//            double childY = y + yOffset; // Move down for next level
//            Line line = new Line(x, y, childX, childY); // Line to left child
//            pane.getChildren().add(line);
//            renderHuffmanTreeForceDirected(pane, node.left, childX, childY, xOffset / 2, yOffset, currentDepth + 1); // Increase depth
//        }
//
//        if (node.right != null) {
//            double childX = x + xOffset; // Move right for right child
//            double childY = y + yOffset; // Move down for next level
//            Line line = new Line(x, y, childX, childY); // Line to right child
//            pane.getChildren().add(line);
//            renderHuffmanTreeForceDirected(pane, node.right, childX, childY, xOffset / 2, yOffset, currentDepth + 1); // Increase depth
//        }
//    }





  
    


    private static int readFileAndCountFrequencies(File file, int[] freq) throws IOException {
        Arrays.fill(freq, 0);
        int totalCharacters = 0;

        try (FileInputStream fis = new FileInputStream(file)) {
            int ch;
            while ((ch = fis.read()) != -1) {
                freq[ch]++;
                totalCharacters++;
            }
        }

        return totalCharacters;
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf('.');
        return (lastIndex == -1) ? "" : name.substring(lastIndex + 1);
    }

    private static long calculateCompressedSize(int[] freq, String[] huffmanCodes) {
        long size = 0;
        for (int i = 0; i < ASCII_SIZE; i++) {
            if (freq[i] > 0 && huffmanCodes[i] != null) {
                size += freq[i] * huffmanCodes[i].length();
            }
        }
        return (size + 7) / 8;
    }
  

}
