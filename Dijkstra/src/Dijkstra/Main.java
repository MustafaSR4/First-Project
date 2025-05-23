package Dijkstra;

import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Duration;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {

	File file; // To hold the reference to the selected file

	public void start(Stage stage) throws Exception {

		BorderPane bp = new BorderPane(); // Main layout pane

		Glow glow = new Glow(); // Glow effect for interactive elements
		glow.setLevel(0.5);

		ColorAdjust colorAdjust = new ColorAdjust(); // Adjust color settings for glow effect
		colorAdjust.setBrightness(0.2);
		colorAdjust.setContrast(0.0);
		colorAdjust.setSaturation(-0.1);
		colorAdjust.setHue(0.166);

		glow.setInput(colorAdjust); // Apply color adjustment to the glow effect

		// Load the logo image
		Image logoImage = new Image(getClass().getResource("Images/map2.png").toExternalForm());
		ImageView logoView = new ImageView(logoImage);
		logoView.setFitHeight(logoImage.getHeight() / 2);
		logoView.setFitWidth(logoImage.getWidth() / 2);

		// Add a zoom effect to the logo
		SequentialTransition sequentialTransition = new SequentialTransition();
		addZoomEffect(logoView, 0.1, 1.0, 850, sequentialTransition); // Pass ImageView, not Image
		sequentialTransition.play(); // Start the sequential transition

		// Create a label for "Travel Anywhere"
		Label titleLabel = new Label("Travel Anywhere");
		titleLabel.setStyle("-fx-font-size: 55px; -fx-font-weight: bold; -fx-text-fill: #141E46;");

		
		VBox vBox = new VBox(10, logoView, titleLabel);
		vBox.setAlignment(Pos.CENTER);
		bp.setCenter(vBox); // Center the VBox in the border pane


		Button loadButton = new Button("Load The Capitals File");
		HBox optionsBox = new HBox(10, loadButton);
		optionsBox.setAlignment(Pos.CENTER);
		bp.setBottom(optionsBox); // Set the HBox with buttons at the bottom of the border pane

		Scene scene = new Scene(bp, 1200, 620);
		bp.setPadding(new Insets(15, 15, 15, 15)); // Padding around the border pane

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("File Chooser");

		// Event handler for compressing files
		loadButton.setOnAction(e -> {
		    ExtensionFilter filterTXT = new ExtensionFilter("Text files", "*.txt");
		    fileChooser.getExtensionFilters().clear();
		    fileChooser.getExtensionFilters().add(filterTXT);

		    file = fileChooser.showOpenDialog(stage);

		    try {
		        // Check if the user clicked "Cancel" (no file selected)
		        if (file == null) {
		            throw new IllegalArgumentException("No file selected");
		        }

		        if (file.length() == 0) {
		            throw new IOException("File is empty");
		        }

		        // Load the new scene with the selected file
		        MapScene mapScene = new MapScene(stage, scene, file);
		        mapScene.getStylesheets().add(getClass().getResource("Styles/LightMode2.css").toExternalForm());
		        stage.setScene(mapScene);
		        stage.setMaximized(true);
		    } catch (IllegalArgumentException e1) {
		        Alert alert = new Alert(Alert.AlertType.WARNING);
		        alert.setTitle("File Selection Error");
		        alert.setHeaderText("No File Selected");
		        alert.setContentText("Please choose a valid file before proceeding.");
		        alert.showAndWait();
		    } catch (IOException e2) {
		        Alert alert = new Alert(Alert.AlertType.ERROR);
		        alert.setTitle("File Error");
		        alert.setHeaderText("File Error");
		        alert.setContentText(e2.getMessage());
		        alert.showAndWait();
		    } catch (Exception e3) {
		        e3.printStackTrace();
		        Alert alert = new Alert(Alert.AlertType.ERROR);
		        alert.setTitle("Unknown Error");
		        alert.setHeaderText("An unexpected error occurred");
		        alert.setContentText("Please try again with a valid file.");
		        alert.showAndWait();
		    }
		});


		//		scene.setCursor(cursor);
		scene.getStylesheets().add(getClass().getResource("Styles/LightMode.css").toExternalForm()); // Apply CSS for styling
		stage.setScene(scene);
		stage.setTitle("Travel Map");
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}


	// Overloaded method to add a zoom effect to an ImageView as part of a
	// SequentialTransition
	private void addZoomEffect(ImageView imageView, double fromScale, double toScale, int durationMillis,
			SequentialTransition sequentialTransition) {
		ScaleTransition st = new ScaleTransition(Duration.millis(durationMillis), imageView);
		st.setFromX(fromScale);
		st.setFromY(fromScale);
		st.setToX(toScale);
		st.setToY(toScale);
		sequentialTransition.getChildren().add(st);
	}

}
