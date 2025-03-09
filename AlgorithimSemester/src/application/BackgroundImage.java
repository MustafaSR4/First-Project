package application;
import javafx.scene.layout.Pane;

public class BackgroundImage {
    public static void applyBackgroundImage(Pane layout, String imagePath) {
        layout.setStyle("-fx-background-image: url('" + imagePath + "'); " +
                        "-fx-background-size: cover; " +
                        "-fx-background-position: center; " +
                        "-fx-background-repeat: no-repeat;");
    }
}
