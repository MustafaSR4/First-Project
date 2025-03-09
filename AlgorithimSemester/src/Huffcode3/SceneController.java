package Huffcode3;

import java.awt.Desktop;
import java.io.File;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class SceneController {

    // Reference to the primary stage of the application.
    private static Stage primaryStage;

    // Setter method to set the primary stage.
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    // Set the main scene using a custom MainScene class.
    public static void setMainScene() {
        primaryStage.setScene(new WelcomingScene(primaryStage));
    }

    // Set a general scene for the primary stage.
    public static void setScene(Scene scene) {
        primaryStage.setScene(scene);
    }

    // Display an alert with specified title and content
    // Because I used it in all the scenes i put it here to avoid repeating it in every scene
    public static void showAlert(String title, String content) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
	public static void getUniquName(StringBuilder fileName){
	     // Create a File object based on the input file name.
	     File file = new File(fileName.toString());
	     // Initialize a counter and a flag for the while loop.
	     int number = 1, flag = 0;
	     // Loop to check if the file exists and modify the file name accordingly.
	     while (file.exists()) {
	         int lastDotIndex;
	         if (flag == 0) {
	             // Find the last dot (.) position to locate the extension.
	             lastDotIndex = fileName.lastIndexOf(".");
	             // Insert a number before the extension for the first time.
	             fileName.insert(lastDotIndex, "("+ (number++) +")");
	         }
	         else {
	             // For subsequent iterations, remove the old number and add a new one.
	             int startIndex = fileName.lastIndexOf("(");
	             int endIndex = fileName.lastIndexOf(")") + 1;
	             fileName.delete(startIndex, endIndex);
	             lastDotIndex = fileName.lastIndexOf(".");
	             fileName.insert(lastDotIndex, "(" + (number++) + ")");
	         }
	         // Update the file object with the new file name.
	         file = new File(fileName.toString()); 
	         // Set flag to 1 to indicate that the file name has been modified at least once.
	         flag = 1;
	     }
	 }
	
	public static String replaceExtension(String fileName, String newExtension){
	     // Find the last dot (.) position to locate the extension.
	     int lastDotIndex = fileName.lastIndexOf(".");
	     if (lastDotIndex == -1) {
	         // If there's no extension, simply append the new extension.
	         return fileName + "." + newExtension;
	     }
	     // Replace the old extension with the new extension.
	     return fileName.substring(0, lastDotIndex) + "." + newExtension;
	 }
	
	public static void openFolder(String path) {
		try {
			Desktop desktop = Desktop.getDesktop();
	        File directory = new File(path);
	        desktop.open(directory);
		} catch (Exception e) {
			SceneController.showAlert("Error","Can't open file by java copy the path of file and go to open it.");
		}
	}
	public static void handleShowHuffmanTable(TreeNode[] bytes, boolean includeFrequency) {
	    ScrollPane subScenePane = new ScrollPane();
	    subScenePane.setStyle("-fx-background: #212121;"); // Dark background for the scroll pane

	    HBox hBox = new HBox(80);
	    hBox.setAlignment(Pos.CENTER);
	    hBox.setPadding(new Insets(50));
	    hBox.setStyle("-fx-background-color: #1e272e;"); // Darker background for contrast

	    GridPane gridPane = new GridPane();
	    gridPane.setVgap(15); // Increased vertical gap for better readability
	    gridPane.setHgap(50); // Increased horizontal gap for better spacing
	    gridPane.setPadding(new Insets(20));
	    gridPane.setAlignment(Pos.CENTER);
	    gridPane.setStyle("-fx-background-color: #485460; -fx-border-radius: 15px; -fx-background-radius: 15px;"); // Rounded corners

	    // Dynamic column headers based on whether frequency is included
	    Label[] headers = includeFrequency
	        ? new Label[] {
	            new Label("Character"),
	            new Label("ASCII"),
	            new Label("Huffman Code"),
	            new Label("Frequency"),
	            new Label("Length")
	        }
	        : new Label[] {
	            new Label("Character"),
	            new Label("ASCII"),
	            new Label("Huffman Code"),
	            new Label("Length")
	        };

	    // Add headers to the grid
	    int columnIndex = 0;
	    for (Label header : headers) {
	        header.setStyle("-fx-text-fill: #ffdd59; -fx-font-weight: bold; -fx-font-size: 14px;"); // Yellow header text
	        GridPane.setConstraints(header, columnIndex++, 0); // column, row
	        GridPane.setHalignment(header, HPos.CENTER); // Center alignment
	        gridPane.getChildren().add(header);
	    }

	    // Populate table rows with TreeNode data
	    int rowIndex = 1;
	    for (TreeNode node : bytes) {
	        if (node.getCode() != null) {
	            // Handle character representation
	            String character;
	            int asciiValue = node.getByteContent() != null ? (node.getByteContent() & 0xFF) : -1; // Ensure unsigned byte value

	            if (asciiValue >= 0 && asciiValue <= 31) {
	                // Control characters (0–31)
	                switch (asciiValue) {
	                    case 0:
	                        character = "NULL";
	                        break;
	                    case 10:
	                        character = "LF";//line fine text formating
	                        break;
	                    case 13:
	                        character = "CR";//carrige return 
	                        break;
	                    default:
	                        character = String.format("CTRL-%d", asciiValue);// control characters
	                }
	            } else if (asciiValue == 127) {
	                // Delete character
	                character = "DEL";
	            } else if (asciiValue >= 32 && asciiValue <= 126) {
	                // Printable ASCII characters
	                character = String.valueOf((char) asciiValue);
	            } else if (asciiValue >= 128 && asciiValue <= 255) {
	                // Extended ASCII characters
	                character = String.valueOf((char) asciiValue);
	            } else {
	                // Null or invalid byte content
	                character = "NULL";
	            }


	            // Create labels for each row
	            Label charLabel = new Label(character);
	            Label asciiLabel = new Label(asciiValue >= 0 ? String.valueOf(asciiValue) : "NULL");
	            Label codeLabel = new Label(node.getCode());
	            Label lengthLabel = new Label(String.valueOf(node.getCode().length()));

	            // Apply styling
	            charLabel.setStyle("-fx-text-fill: #d2dae2;");
	            asciiLabel.setStyle("-fx-text-fill: #d2dae2;");
	            codeLabel.setStyle("-fx-text-fill: #d2dae2;");
	            lengthLabel.setStyle("-fx-text-fill: #d2dae2;");

	            GridPane.setHalignment(charLabel, HPos.CENTER);
	            GridPane.setHalignment(asciiLabel, HPos.CENTER);
	            GridPane.setHalignment(codeLabel, HPos.CENTER);
	            GridPane.setHalignment(lengthLabel, HPos.CENTER);

	            // Add labels to grid
	            gridPane.add(charLabel, 0, rowIndex);
	            gridPane.add(asciiLabel, 1, rowIndex);
	            gridPane.add(codeLabel, 2, rowIndex);
	            if (includeFrequency) {
	                Label frequencyLabel = new Label(String.valueOf(node.getFrequency()));
	                frequencyLabel.setStyle("-fx-text-fill: #d2dae2;");
	                GridPane.setHalignment(frequencyLabel, HPos.CENTER);
	                gridPane.add(frequencyLabel, 3, rowIndex);
	            }
	            gridPane.add(lengthLabel, includeFrequency ? 4 : 3, rowIndex);

	            rowIndex++;
	        }
	    }

	    // Create a new BorderPane to hold the table with padding and background
	    BorderPane tableContainer = new BorderPane();
	    tableContainer.setCenter(gridPane);
	    tableContainer.setPadding(new Insets(20));
	    tableContainer.setStyle("-fx-background-color: #0fbcf9; -fx-border-color: #2f3542; " +
	            "-fx-border-width: 3px; -fx-border-radius: 15px; -fx-background-radius: 15px;");

	    // Create a VBox for the title and table container
	    VBox background = new VBox(30);
	    Label huffmanLabel = new Label("Huffman Table:");
	    huffmanLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
	    huffmanLabel.setStyle("-fx-text-fill: #1e90ff;");
	    background.setAlignment(Pos.CENTER);
	    background.setPadding(new Insets(40));
	    background.setStyle("-fx-background-color: #1e272e; -fx-border-radius: 20px; -fx-background-radius: 20px;");
	    background.getChildren().addAll(huffmanLabel, tableContainer);

	    hBox.getChildren().addAll(background);
	    subScenePane.setContent(hBox);
	    subScenePane.setFitToWidth(true);
	    subScenePane.setFitToHeight(true);

	    Scene scene = new Scene(subScenePane, 800, 600); // Scene size
	    Stage newStage = new Stage();
	    newStage.setScene(scene);
	    hBox.prefWidthProperty().bind(newStage.widthProperty());
	    hBox.prefHeightProperty().bind(newStage.heightProperty());
	    newStage.show();
	}


	public static void handleShowHeader(StringBuilder headerToShow) {

	    // Copy the header string for manipulation
	    StringBuilder header = new StringBuilder(headerToShow);

	    // Create a scrollable pane
	    ScrollPane subScenePane = new ScrollPane();

	    // Horizontal box for layout with spacing of 80
	    HBox hBox = new HBox(80);
	    hBox.setAlignment(Pos.CENTER);
	    hBox.setPadding(new Insets(50));
	    hBox.setStyle("-fx-background-color: #1a1a2e;"); // Dark blue background

	    // Vertical box for layout with spacing of 15
	    VBox background = new VBox(15);
	    background.setPadding(new Insets(40));
	    background.setStyle("-fx-background-color: #16213e; " +
	            "-fx-border-color: #0f3460; " +
	            "-fx-border-width: 2px; " +
	            "-fx-border-radius: 10px; " +
	            "-fx-background-radius: 10px;");

	    // Label for the header
	    Label headerLabel = new Label("Header");
	    headerLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
	    headerLabel.setStyle("-fx-text-fill: #e94560;"); // Pinkish-red text

	    // Processing header string to extract and display extension in bits and chars
	    String extensionInbitString = "";
	    String extensionInCharString = "";
	    for (int i = 0; i < 8; i++) {
	        // Convert 8-bit binary sequences to characters
	        extensionInCharString += (char) Integer.parseInt(header.substring(i * 8, (i + 1) * 8), 2);
	        extensionInbitString += header.substring(i * 8, (i + 1) * 8) + " ";
	    }
	    header.delete(0, 64); // Remove processed part (file extension) from the header
	    TextFlow extensionInbitTextFlow = creatTextFlow("Extension in bits: ", extensionInbitString, "#00adb5", "#e94560");

	    Label extensionInCharLabel = new Label("Extension in char: " + extensionInCharString);
	    extensionInCharLabel.setStyle("-fx-text-fill: #00adb5;"); // Cyan text

	    // Display size of header in bits
//	    TextFlow sizeInbitTextFlow = creatTextFlow("Size of header in bits: ", header.substring(0, 32), "#00adb5", "#e94560");
	    int sizeOfHeader = Integer.parseInt(header.substring(0, 32), 2); // Convert size from bits to integer
	    header.delete(0, 32); // Remove processed part (header size) from the header
//	    Label sizeInIntLabel = new Label("Size of header in integer: " + sizeOfHeader);
//	    sizeInIntLabel.setStyle("-fx-text-fill: #00adb5;");

	    // Label for post-order traversal
	    Label postOrderLabel = new Label("Post Order for binary tree:");
	    postOrderLabel.setStyle("-fx-text-fill: #e94560;");

	    background.getChildren().addAll(headerLabel, extensionInbitTextFlow, extensionInCharLabel,  postOrderLabel);

	    // Process and display individual bits of the header
	    int counter = 0, numberOfExtrabit = 8 - sizeOfHeader % 8;
	    while (counter < sizeOfHeader) {
	        if (header.charAt(counter) == '0') {
	            // Internal node is represented by '1'
	            counter++;
	            Label label = new Label("1 " + header.substring(counter, counter + 8));
	            label.setStyle("-fx-text-fill: #00adb5;"); // Cyan text for internal node
	            background.getChildren().add(label);
	            counter += 8;
	        } else {
	            // Leaf node is represented by '0'
	            counter++;
	            Label label = new Label("0");
	            label.setStyle("-fx-text-fill: #00adb5;"); // Cyan text for leaf node
	            background.getChildren().add(label);
	        }
	    }

	    // Handle case when no extra bit is added
	    if (numberOfExtrabit == 8) {
	        Label label = new Label("The size of the header is divided by 8 no extra bit added");
	        label.setStyle("-fx-text-fill: #00adb5;");
	        background.getChildren().add(label);
	    } else {
	        // Extra bits in the header for alignment
	        TextFlow textFlow = creatTextFlow("Extra bit: ", header.substring(counter, counter + numberOfExtrabit), "#00adb5", "#e94560");
	        background.getChildren().add(textFlow);
	    }

	    // Add the VBox to the HBox and set the scene
	    hBox.getChildren().addAll(background);
	    subScenePane.setContent(hBox);
	    Scene scene = new Scene(subScenePane, 700, 400);
	    Stage newStage = new Stage();
	    newStage.setScene(scene);
	    hBox.prefWidthProperty().bind(newStage.widthProperty());
	    hBox.prefHeightProperty().bind(newStage.heightProperty());
	    newStage.show();
	}

	// Helper method to create a TextFlow with two differently colored parts
	private static TextFlow creatTextFlow(String labelText, String valueText, String labelColor, String valueColor) {
	    Text label = new Text(labelText);
	    label.setStyle("-fx-fill: " + labelColor + "; -fx-font-weight: bold;");

	    Text value = new Text(valueText);
	    value.setStyle("-fx-fill: " + valueColor + ";");

	    return new TextFlow(label, value);
	}


	
	

	public static String byteToBinaryString(byte b) {
	    StringBuilder binaryString = new StringBuilder();
	    for (int i = 7; i >= 0; i--) {
	        int bit = (b >> i) & 1;
	        binaryString.append(bit);
	    }
	    return binaryString.toString();
	}
}