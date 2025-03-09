package Huffcode3;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
    	SceneController.setPrimaryStage(primaryStage);
        // Show the splash screen or main scene
        SceneController.setMainScene();

        // Update primary stage settings
        primaryStage.setTitle("Huffman Code");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
