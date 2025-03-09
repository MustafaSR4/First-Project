package HuffCode2;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneManager.setPrimaryStage(primaryStage);

        // Show the splash screen or main scene
        SceneManager.setMainScene();

        // Update primary stage settings
        primaryStage.setTitle("Huffman Compressor and Decompressor");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
