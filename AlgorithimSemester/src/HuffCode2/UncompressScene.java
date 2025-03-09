

package HuffCode2;

import java.io.*;
import java.util.*;
import javafx.application.Platform;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.Screen;

// Main UncompressScene class
public class UncompressScene extends Scene {
    private final File path;
    private TreeNode[] bytes;
    private long lengthFileBefore;
    private long lengthFileAfter;
    private final StringBuilder headerToShow = new StringBuilder("");
    private StringBuilder nameOfUncompressedFile;

    public UncompressScene(File path) {
        super(new StackPane(), Screen.getPrimary().getVisualBounds().getWidth(), Screen.getPrimary().getVisualBounds().getHeight());
        this.path = path;

        StackPane layout = (StackPane) getRoot();
        layout.getStyleClass().add("root");

        Label waitLabel = new Label("Please wait, the file will be uncompressed soon");
        waitLabel.setFont(Font.font("Century Gothic", 20));

        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(true);

        VBox vBox = new VBox(40);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(progressIndicator, waitLabel);
        layout.getChildren().addAll(vBox);

        new Thread(() -> {
            try {
                uncompress();
                Platform.runLater(() -> updateUIAfterUncompression(vBox));
            } catch (Exception e) {
                Platform.runLater(() -> showErrorAlert(e.getMessage()));
            }
        }).start();
    }

    public void uncompress() {
        try {
            // Record the file size before uncompression
            lengthFileBefore = path.length();

            // Read the compressed file
            FileInputStream scan = new FileInputStream(path);
            StringBuilder fileExtension = new StringBuilder();

            // Buffers for file reading
            byte[] buffer = new byte[8];
            byte[] sizeOfHeaderBuffer = new byte[4];
            int sizeOfHeader;

            // Read the file extension (first 8 bytes)
            if (scan.read(buffer) != -1) {
                for (int i = 0; i < 8; i++) {
                    if (buffer[i] != 0) {
                        fileExtension.append((char) buffer[i]);
                    }
                    headerToShow.append(SceneManager.byteToBinaryString(buffer[i]));
                }

                // Read the size of the header (next 4 bytes)
                scan.read(sizeOfHeaderBuffer);
                sizeOfHeader = byteArrayToInt(sizeOfHeaderBuffer);

                for (byte b : sizeOfHeaderBuffer) {
                    headerToShow.append(SceneManager.byteToBinaryString(b));
                }
            } else {
                scan.close();
                throw new IllegalArgumentException("The input file cannot be read");
            }

            // Calculate the number of bytes for the header
            int numberOfBytesForHeader = (sizeOfHeader + 7) / 8;

            // Continue reading the header and serialized data
            StringBuilder header = new StringBuilder();
            StringBuilder serialData = new StringBuilder();
            int numberOfBytesRead = 0, counterHowManyByteReadFromHeader = 0;

            while ((numberOfBytesRead = scan.read(buffer)) != -1) {
                for (int i = 0; i < numberOfBytesRead; i++) {
                    if (counterHowManyByteReadFromHeader < numberOfBytesForHeader) {
                        header.append(SceneManager.byteToBinaryString(buffer[i]));
                        counterHowManyByteReadFromHeader++;
                    } else {
                        serialData.append(SceneManager.byteToBinaryString(buffer[i]));
                    }
                }
            }
            scan.close();
            headerToShow.append(header);

            // Reconstruct the Huffman tree using custom Stack
            Stack stack = new Stack(256);
            int counter = 0, numberOfLeafNode = 0;

            while (counter < sizeOfHeader) {
                if (header.charAt(counter) == '0') {
                    counter++;
                    stack.push(new TreeNode((byte) Integer.parseInt(header.substring(counter, counter + 8), 2)));
                    numberOfLeafNode++;
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
            if (rootTreeNode.getLeft() == null && rootTreeNode.getRight() == null) {
                rootTreeNode.setCode("1");
            } else {
                TreeNode.gaveCodeForEachByte(rootTreeNode);
            }

            bytes = new TreeNode[numberOfLeafNode];
            rootTreeNode.getLeafNodes(bytes);

            // Determine the output file name
            String nameOfUncompressedFile = SceneManager.replaceExtension(path.getName(), fileExtension.toString());
            nameOfUncompressedFile = SceneManager.getUniqueName(nameOfUncompressedFile);

            // Decode the serialized data
            FileOutputStream out = new FileOutputStream(nameOfUncompressedFile);
            int startIndex = serialData.length() - 8;
            int numberOfEffectiveBits = Integer.parseInt(serialData.substring(startIndex), 2);
            serialData.setLength(startIndex + numberOfEffectiveBits - 8);

            TreeNode current = rootTreeNode;
            for (int i = 0; i < serialData.length(); i++) {
                current = serialData.charAt(i) == '0' ? current.getLeft() : current.getRight();

                if (current.isLeaf()) {
                    out.write(current.getByteContent());
                    current = rootTreeNode;
                }
            }

            out.close();
            lengthFileAfter = new File(nameOfUncompressedFile).length();

        } catch (Exception e) {
            SceneManager.showAlert("Error", e.getMessage());
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

    private void updateUIAfterUncompression(VBox vBox) {
        vBox.getChildren().clear();
        vBox.setPadding(new Insets(60));

        Label welcomeLabel = new Label("Results for uncompressing the file");
        welcomeLabel.setFont(Font.font("Century Gothic", FontWeight.BOLD, 30));
        welcomeLabel.setStyle("-fx-text-fill: #000000;");

        Label doneCorrectlyLabel = new Label(
            String.format("File size Before: %d bytes, After: %d bytes, Uncompression Ratio: %.5f",
                lengthFileBefore, lengthFileAfter, (double) lengthFileAfter / lengthFileBefore)
        );
        doneCorrectlyLabel.setFont(Font.font("Century Gothic", 20));

        Label nameLabel = new Label("Uncompressed file name: " + nameOfUncompressedFile);
        nameLabel.setFont(Font.font("Century Gothic", 20));

        vBox.getChildren().addAll(welcomeLabel, doneCorrectlyLabel, nameLabel);
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.showAndWait();
    }

   
}