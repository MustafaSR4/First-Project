package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.*;

public class Optimal extends Application {
    private TextArea resultArea = new TextArea();
    private TextField coinsInput = new TextField();
    private List<Integer> coins = new ArrayList<>();
    private GridPane dpGrid = new GridPane();
    private Label maxAmountLabel = new Label();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Optimal Game Strategy");

        // Input Section
        VBox inputOptions = new VBox(10);
        inputOptions.setPadding(new Insets(10));
        Label titleLabel = new Label("Optimal Game Strategy Solver");
        
        Label inputLabel = new Label("Enter coins (comma-separated):");
        coinsInput.setPromptText("e.g., 4, 15, 7, 3, 8, 9");
        
        HBox buttonBox = new HBox(10);
        Button loadCoinsButton = new Button("Load Coins");
        loadCoinsButton.setOnAction(e -> loadCoins());
        
        Button randomCoinsButton = new Button("Generate Random Coins");
        randomCoinsButton.setOnAction(e -> generateRandomCoins());

        buttonBox.getChildren().addAll(loadCoinsButton, randomCoinsButton);
        inputOptions.getChildren().addAll(titleLabel, inputLabel, coinsInput, buttonBox);

        // Game Mode Options
        VBox gameModeOptions = new VBox(10);
        gameModeOptions.setPadding(new Insets(10));
        Label modeLabel = new Label("Choose Game Mode:");
        
        HBox gameButtonBox = new HBox(10);
        Button pvpButton = new Button("Player vs Player");
        pvpButton.setOnAction(e -> playGame(false));

        Button pvcButton = new Button("Player vs Computer");
        pvcButton.setOnAction(e -> playGame(true));
        
        gameButtonBox.getChildren().addAll(pvpButton, pvcButton);
        gameModeOptions.getChildren().addAll(modeLabel, gameButtonBox);

        // DP Table Grid for Display
        dpGrid.setPadding(new Insets(10));
        dpGrid.setHgap(5);
        dpGrid.setVgap(5);
        dpGrid.setAlignment(Pos.CENTER);
        dpGrid.setStyle("-fx-border-color: gray; -fx-background-color: lightgray;");

        // Results Display Area
        maxAmountLabel.setStyle("-fx-font-size: 16;");
        resultArea.setEditable(false);
        resultArea.setPrefHeight(150);
        resultArea.setWrapText(true);

        VBox resultsBox = new VBox(10, maxAmountLabel, resultArea);
        resultsBox.setPadding(new Insets(10));

        // Main Layout
        BorderPane mainLayout = new BorderPane();
        mainLayout.setTop(inputOptions);
        mainLayout.setCenter(dpGrid);
        mainLayout.setBottom(resultsBox);
        mainLayout.setRight(gameModeOptions);

        Scene scene = new Scene(mainLayout, 700, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void loadCoins() {
        String input = coinsInput.getText();
        coins.clear();
        try {
            Arrays.stream(input.split(","))
                  .map(String::trim)
                  .mapToInt(Integer::parseInt)
                  .forEach(coins::add);
            resultArea.appendText("Coins loaded: " + coins + "\n");
        } catch (Exception e) {
            resultArea.appendText("Invalid input. Please enter numbers separated by commas.\n");
        }
    }

    private void generateRandomCoins() {
        Random rand = new Random();
        coins.clear();
        for (int i = 0; i < 6; i++) {
            coins.add(rand.nextInt(20) + 1);
        }
        resultArea.appendText("Random coins generated: " + coins + "\n");
    }

    private void playGame(boolean isPlayerVsComputer) {
        if (coins.isEmpty()) {
            resultArea.appendText("No coins loaded. Please load or generate coins first.\n");
            return;
        }

        int[][] dp = new int[coins.size()][coins.size()];
        calculateOptimalStrategy(dp);
        int maxAmount = dp[0][coins.size() - 1];
        maxAmountLabel.setText("Maximum Amount First Player Can Win: " + maxAmount);

        List<Integer> selectedCoins = traceCoins(dp);

        resultArea.appendText("\n--- Game Result ---\n");
        resultArea.appendText("Optimal sequence of coins chosen: " + selectedCoins + "\n");
        if (isPlayerVsComputer) {
            resultArea.appendText("Player vs Computer Mode\nComputer played optimally.\n");
        } else {
            resultArea.appendText("Player vs Player Mode\nBoth players played optimally.\n");
        }

        displayDpTable(dp);
    }

    private void calculateOptimalStrategy(int[][] dp) {
        int n = coins.size();
        for (int i = 0; i < n; i++) {
            dp[i][i] = coins.get(i);
        }

        for (int length = 2; length <= n; length++) {
            for (int i = 0; i < n - length + 1; i++) {
                int j = i + length - 1;
                int pickFirst = coins.get(i) + Math.min(i + 2 < n ? dp[i + 2][j] : 0,
                                                       i + 1 <= j - 1 ? dp[i + 1][j - 1] : 0);
                int pickLast = coins.get(j) + Math.min(i <= j - 2 ? dp[i][j - 2] : 0,
                                                      i + 1 <= j - 1 ? dp[i + 1][j - 1] : 0);
                dp[i][j] = Math.max(pickFirst, pickLast);
            }
        }
    }

    private List<Integer> traceCoins(int[][] dp) {
        List<Integer> selectedCoins = new ArrayList<>();
        int i = 0, j = coins.size() - 1;
        while (i <= j) {
            if (i == j) {
                selectedCoins.add(coins.get(i));
                break;
            }
            int pickFirst = coins.get(i) + Math.min(i + 2 <= j ? dp[i + 2][j] : 0,
                                                   i + 1 <= j - 1 ? dp[i + 1][j - 1] : 0);
            int pickLast = coins.get(j) + Math.min(i <= j - 2 ? dp[i][j - 2] : 0,
                                                  i + 1 <= j - 1 ? dp[i + 1][j - 1] : 0);

            if (pickFirst >= pickLast) {
                selectedCoins.add(coins.get(i));
                i++;
            } else {
                selectedCoins.add(coins.get(j));
                j--;
            }
        }
        return selectedCoins;
    }

    private void displayDpTable(int[][] dp) {
        dpGrid.getChildren().clear();  // Clear previous DP table display
        for (int i = 0; i < dp.length; i++) {
            for (int j = i; j < dp[i].length; j++) {
                Label cell = new Label(String.valueOf(dp[i][j]));
                cell.setStyle("-fx-border-color: black; -fx-padding: 8; -fx-background-color: white;");
                cell.setPrefSize(50, 50);
                dpGrid.add(cell, j, i);  // Place each cell in the grid
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}