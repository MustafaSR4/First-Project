package Huffcode3;

import javafx.scene.layout.*;
import javafx.scene.image.Image;

public class HuffmanSceneImages{

    // Method to set a background image on a layout
    public static void setBackground(Pane layout, String imagePath) {
        // Load the image
        Image backgroundImage = new Image(imagePath, true);

        // Create a BackgroundImage and apply scaling options
        BackgroundImage bgImage = new BackgroundImage(
            backgroundImage,
            BackgroundRepeat.NO_REPEAT,  // No repeat for the background
            BackgroundRepeat.NO_REPEAT,  // No repeat for the background
            BackgroundPosition.CENTER,   // Center the image
            new BackgroundSize(
                BackgroundSize.AUTO, BackgroundSize.AUTO, true, true, true, true
            )
        );

        // Set the BackgroundImage to the layout
        layout.setBackground(new Background(bgImage));
    }
}
