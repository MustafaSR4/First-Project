package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;

public class OptimalGameStrategy extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Optimal Game Strategy - Dynamic Programming");

        // Input field for the number of coins
        Label numCoinsLabel = new Label("Enter Number of Coins:");
        TextField numCoinsField = new TextField();
        numCoinsField.setPromptText("Number of coins");
        
        // Input field for the coin values
        Label coinsLabel = new Label("Enter Coin Values (comma-separated):");
        TextField coinsField = new TextField();
        coinsField.setPromptText("e.g., 4, 15, 7, 3, 8, 9");

        // Label and field to display the expected result
        Label resultLabel = new Label("Expected Result:");
        TextField resultField = new TextField();
        resultField.setEditable(false);
        resultField.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        // Button to solve the game
        Button solveButton = new Button("Solve Optimal Strategy");
        solveButton.setFont(Font.font("Verdana", FontWeight.BOLD, 14));

        // TextArea to display the DP table
        Label tableLabel = new Label("Dynamic Programming Table:");
        TextArea dpTableArea = new TextArea();
        dpTableArea.setEditable(false);

        // TextArea to display chosen coins
        Label chosenCoinsLabel = new Label("Chosen Coins:");
        TextArea chosenCoinsArea = new TextArea();
        chosenCoinsArea.setEditable(false);

        // Solve button action
        solveButton.setOnAction(e -> {
            try {
                int n = Integer.parseInt(numCoinsField.getText().trim());
                String[] coinValues = coinsField.getText().trim().split(",");
                if (coinValues.length != n) {
                    resultField.setText("Number of values doesn't match.");
                    return;
                }

                int[] coins = new int[n];
                for (int i = 0; i < n; i++) {
                    coins[i] = Integer.parseInt(coinValues[i].trim());
                }

                int[][] dp = new int[n][n];
                String chosenCoins = solveOptimalGameStrategy(coins, dp);
                resultField.setText(Integer.toString(dp[0][n - 1]));
                dpTableArea.setText(getDpTableString(dp, n));
                chosenCoinsArea.setText(chosenCoins);

            } catch (NumberFormatException ex) {
                resultField.setText("Invalid input. Please enter numbers.");
            }
        });

        // Layout setup
        VBox vbox = new VBox(10, numCoinsLabel, numCoinsField, coinsLabel, coinsField, resultLabel, resultField, solveButton, tableLabel, dpTableArea, chosenCoinsLabel, chosenCoinsArea);
        vbox.setPadding(new Insets(20));
        vbox.setStyle("-fx-background-color: #f0f8ff;");
        
        Scene scene = new Scene(vbox, 600, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to solve the Optimal Game Strategy problem and fill the DP table
    private String solveOptimalGameStrategy(int[] coins, int[][] dp) {
        int n = coins.length;
        for (int gap = 0; gap < n; gap++) {
            for (int i = 0, j = gap; j < n; i++, j++) {
                int x = (i + 2 <= j) ? dp[i + 2][j] : 0;
                int y = (i + 1 <= j - 1) ? dp[i + 1][j - 1] : 0;
                int z = (i <= j - 2) ? dp[i][j - 2] : 0;

                dp[i][j] = Math.max(coins[i] + Math.min(x, y), coins[j] + Math.min(y, z));
            }
        }

        // Retrieve the chosen coins
        StringBuilder chosenCoins = new StringBuilder();
        int i = 0, j = n - 1;
        while (i <= j) {
            if (coins[i] + Math.min((i + 2 <= j ? dp[i + 2][j] : 0), (i + 1 <= j - 1 ? dp[i + 1][j - 1] : 0)) 
                    >= coins[j] + Math.min((j - 2 >= i ? dp[i][j - 2] : 0), (i + 1 <= j - 1 ? dp[i + 1][j - 1] : 0))) {
                chosenCoins.append(coins[i]).append(" ");
                i += 2;
            } else {
                chosenCoins.append(coins[j]).append(" ");
                j -= 2;
            }
        }
        return chosenCoins.toString();
    }

    // Method to convert the DP table into a readable format
    private String getDpTableString(int[][] dp, int n) {
        StringBuilder tableString = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                tableString.append(dp[i][j]).append("\t");
            }
            tableString.append("\n");
        }
        return tableString.toString();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
