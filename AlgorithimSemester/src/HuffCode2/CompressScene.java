package HuffCode2;

import java.io.*;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class CompressScene extends Scene {

    private File path; // File to be compressed
    private TreeNode rootTreeNode; // Root of the Huffman tree
    private long lengthFileBefore, lengthFileAfter; // File sizes before and after compression
    private StringBuilder headerToShow = new StringBuilder(); // Header details
    private TreeNode[] bytes; // Array to store TreeNode objects for Huffman tree

    public CompressScene(File path) {
        super(new VBox(), 800, 600);
        this.path = path;

        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        // Buttons
        Button statsButton = createButton("Show Statistics", e -> showStatistics());
        Button huffmanTableButton = createButton("Huffman Code Table", e -> handleShowHuffmanTable());
        Button headerButton = createButton("Show Header and Huffman Tree", e -> showHeaderAndTree());
        Button backButton = createButton("Back to Main Page", e -> SceneManager.setMainScene());

        // Add buttons to layout
        layout.getChildren().addAll(statsButton, huffmanTableButton, headerButton, backButton);

        // Set the scene root
        this.setRoot(layout);

        // Start the compression process
        compress();
    }

    private Button createButton(String text, EventHandler<javafx.event.ActionEvent> action) {
        Button button = new Button(text);
        button.setPrefSize(300, 50);
        button.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 16;");
        button.setOnAction(action);
        return button;
    }

    public void compress() {
	    try {
	        // Initialize an array of TreeNode objects representing all possible byte values
	        String nameOfCompressedFile = path.getName();

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
	            SceneManager.showAlert("Error", e.getMessage());
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
	                headerToShow.append(SceneManager.byteToBinaryString(bufferIn[i]));
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
	            headerToShow.append(SceneManager.byteToBinaryString(b));
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
	        nameOfCompressedFile = new String(SceneManager.replaceExtension(path.getName(), "huf"));
	        SceneManager.getUniqueName(nameOfCompressedFile);
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
	            SceneManager.showAlert("Error", e.getMessage());
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

	        // Check the size of the compressed file
	        File toCheck = new File(nameOfCompressedFile.toString());
	        lengthFileAfter = toCheck.length();

	    } catch (Exception e) {
	        SceneManager.showAlert("Error", e.getMessage());
	    }
	}

   

    private void showStatistics() {
        Alert statsAlert = new Alert(Alert.AlertType.INFORMATION);
        statsAlert.setTitle("Compression Statistics");
        statsAlert.setHeaderText("Statistics");
        statsAlert.setContentText(
            "File Size Before Compression: " + lengthFileBefore + " bytes\n" +
            "File Size After Compression: " + lengthFileAfter + " bytes\n" +
            "Compression Ratio: " + String.format("%.5f", (double) lengthFileAfter / lengthFileBefore)
        );
        statsAlert.showAndWait();
    }

   

    private void handleShowHuffmanTable() {
    	ScrollPane subScenePane = new ScrollPane();
        HBox hBox = new HBox(80);
        hBox.setAlignment(Pos.CENTER);
        hBox.setPadding(new Insets(50));
        hBox.setStyle("-fx-background-color: #64fc9c;");
        
        GridPane gridPane = new GridPane();
        gridPane.setVgap(10); // Vertical gap between rows
        gridPane.setHgap(35); // Horizontal gap between columns
        gridPane.setPadding(new Insets(15));
        gridPane.setAlignment(Pos.CENTER);

        // Header
        Label[] headers = {
            new Label("Byte"),
            new Label("Huffman"),
            new Label("Frequency"),
            new Label("Length")
        };
        int columnIndex = 0;
        for (Label header : headers) {
            header.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
            GridPane.setConstraints(header, columnIndex, 0); // column, row
            GridPane.setHalignment(header, HPos.CENTER); // Center alignment
            gridPane.getChildren().add(header);
            columnIndex++;
        }

        // Add TreeNode details to the GridPane
        int rowIndex = 1;
        for (TreeNode node : bytes) {
            if (node.getCode() != null) {
                Label byteLabel = new Label(node.getByteContent() + "");
                Label codeLabel = new Label(node.getCode());
                Label frequencyLabel = new Label(node.getFrequency() + "");
                Label lengthLabel = new Label(node.getCode().length() + "");

                byteLabel.setStyle("-fx-text-fill: white;");
                codeLabel.setStyle("-fx-text-fill: white;");
                frequencyLabel.setStyle("-fx-text-fill: white;");
                lengthLabel.setStyle("-fx-text-fill: white;");
                GridPane.setHalignment(byteLabel, HPos.CENTER);
                GridPane.setHalignment(codeLabel, HPos.CENTER);
                GridPane.setHalignment(frequencyLabel, HPos.CENTER);
                GridPane.setHalignment(lengthLabel, HPos.CENTER);

                // Add labels to grid
                gridPane.add(byteLabel, 0, rowIndex);
                gridPane.add(codeLabel, 1, rowIndex);
                gridPane.add(frequencyLabel, 2, rowIndex);
                gridPane.add(lengthLabel, 3, rowIndex);

                rowIndex++;
            }
        }

        // Create a new VBox to act as the main container for the contents
        VBox background = new VBox(30);
        Label huffmanLabel = new Label("Huffman Tabel");
        huffmanLabel.setFont(Font.font("Verdana", 15.5));
        huffmanLabel.setStyle("-fx-text-fill: #FFFFFF;");
        background.setStyle("-fx-background-color: #2b2b2b; " +
                "-fx-border-color: #555555; " +
                "-fx-border-width: 2px; " +
                "-fx-padding: 10px;");
        background.setAlignment(Pos.CENTER);
        background.setPadding(new Insets(40));
        background.getChildren().addAll(huffmanLabel,gridPane);
        
        hBox.getChildren().addAll(background);
        subScenePane.setContent(hBox);
        Scene scene = new Scene(subScenePane, 500, 400);
        Stage newStage = new Stage();
        newStage.setScene(scene);
        hBox.prefWidthProperty().bind(newStage.widthProperty());
        hBox.prefHeightProperty().bind(newStage.heightProperty());
        newStage.show();
    }

    private void showHeaderAndTree() {
        VBox layout = new VBox(20);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);

        Label headerLabel = new Label("Header Information:\n" + headerToShow);
        headerLabel.setWrapText(true);
        headerLabel.setFont(Font.font("Century Gothic", 16));

        Label treeLabel = new Label("Huffman Tree:");
        treeLabel.setFont(Font.font("Century Gothic", 18));

        TreeView<String> treeView = buildHuffmanTree(rootTreeNode);

        layout.getChildren().addAll(headerLabel, treeLabel, treeView);

        Stage stage = new Stage();
        stage.setScene(new Scene(layout, 500, 600));
        stage.setTitle("Header and Huffman Tree");
        stage.show();
    }

    private TreeView<String> buildHuffmanTree(TreeNode root) {
        TreeItem<String> rootItem = new TreeItem<>("Root: " + root.getFrequency());
        buildTreeNodes(root, rootItem);
        return new TreeView<>(rootItem);
    }

    private void buildTreeNodes(TreeNode node, TreeItem<String> treeItem) {
        if (node == null) return;

        if (node.getLeft() != null) {
            TreeItem<String> leftItem = new TreeItem<>("Left: " + node.getLeft().getFrequency());
            treeItem.getChildren().add(leftItem);
            buildTreeNodes(node.getLeft(), leftItem);
        }

        if (node.getRight() != null) {
            TreeItem<String> rightItem = new TreeItem<>("Right: " + node.getRight().getFrequency());
            treeItem.getChildren().add(rightItem);
            buildTreeNodes(node.getRight(), rightItem);
        }
    }
}
