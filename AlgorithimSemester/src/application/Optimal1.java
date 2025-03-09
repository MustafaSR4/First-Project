package application;
import java.util.ArrayList;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Optimal1 extends Application {
    private static final int MAX_COINS = 10;
    private int[] coins = new int[MAX_COINS];
    private int[] originalCoins = new int[MAX_COINS];
    private int coinCount = 0;
    private TextArea moveHistoryArea = new TextArea();
    private TextField coinsInput = new TextField();
    private TextField coinCountInput = new TextField();
    private GridPane dynamicProgrammingGrid = new GridPane();
    private HBox coinRow = new HBox(10);
    private Label maxAmountLabel = new Label("Maximum Amount: ");
    private Label player1ScoreLabel = new Label("Player 1 Score: 0");
    private Label player2ScoreLabel = new Label("Player 2 / Computer Score: 0");
    private Label finalScoresLabel = new Label(); // Declare a label for displaying final scores
    private int player1Score = 0;
    private int player2Score = 0;
    private boolean isPlayerTurn = true;
    private boolean isPlayerVsComputer;
    private Scene mainInterfaceScene, gameModeScene, coinSetupScene, playGameScene, gameOverScene, dpTableScene,playerNameScene;
    private Stage primaryStage;
    private int[][] dpTable;
    private String player1Name="Player 1";
    private String player2Name="Player 2";
    private TextField player1NameInput = new TextField("");
    private TextField player2NameInput = new TextField("");
    private Label errorMessageLabel = new Label(); // Label for displaying error messages
    private ArrayList<Integer> chosenCoins = new ArrayList<>();


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Optimal Game Strategy");

        setupMainInterfaceScene();
        setupGameModeScene();
        setupCoinSetupScene();
        setupPlayGameScene();
        setupGameOverScene();
        setupDpTableScene();

        primaryStage.setScene(mainInterfaceScene);
        primaryStage.show();
    }

    

        

    private void setupMainInterfaceScene() {
        VBox mainInterfaceLayout = new VBox(20);
        mainInterfaceLayout.setPadding(new Insets(20));
        mainInterfaceLayout.setAlignment(Pos.CENTER);
        Label welcomeLabel = new Label("Welcome to the Optimal Game Strategy");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button startGameButton = new Button("Start Game");
        startGameButton.setOnAction(e -> primaryStage.setScene(gameModeScene));

        mainInterfaceLayout.getChildren().addAll(welcomeLabel, startGameButton);
        mainInterfaceScene = new Scene(mainInterfaceLayout, 400, 300);
    }

    private void setupGameModeScene() {
        VBox gameModeLayout = new VBox(20);
        gameModeLayout.setPadding(new Insets(20));
        gameModeLayout.setAlignment(Pos.CENTER);
        Label modeLabel = new Label("Choose Game Mode:");
        modeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        Button pvpButton = new Button("Player vs Player");
        pvpButton.setOnAction(e -> {
            isPlayerVsComputer = false;
            setupPlayerNameScene();  // Ensure playerNameScene is initialized
            primaryStage.setScene(playerNameScene);
        });

        Button pvcButton = new Button("Player vs Computer");
        pvcButton.setOnAction(e -> {
            isPlayerVsComputer = true;
            setupPlayerNameScene();  // Ensure playerNameScene is initialized
            primaryStage.setScene(playerNameScene);
        });

        HBox gameModeButtons = new HBox(10, pvpButton, pvcButton);
        gameModeButtons.setAlignment(Pos.CENTER);

        gameModeLayout.getChildren().addAll(modeLabel, gameModeButtons);
        gameModeScene = new Scene(gameModeLayout, 400, 400);
    }
    private void setupPlayerNameScene() {
        VBox playerNameLayout = new VBox(20);
        playerNameLayout.setPadding(new Insets(20));
        playerNameLayout.setAlignment(Pos.CENTER);

        Label playerNameLabel = new Label("Enter Player Names");
        playerNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        // Label to show error messages
        Label errorLabel = new Label();
        errorLabel.setStyle("-fx-text-fill: red;");

        // Labels and input fields for player names
        Label player1NameLabel = new Label("Player 1 Name:");
        player1NameInput.setPromptText("Enter Player 1 Name");

        Label player2NameLabel = new Label("Player 2 Name:");
        player2NameInput.setPromptText("Enter Player 2 Name");

        Button proceedButton = new Button("Proceed to Coin Setup");
        proceedButton.setOnAction(e -> {
            String player1Input = player1NameInput.getText().trim();
            String player2Input = player2NameInput.getText().trim();

            // Check if player 1 name is empty
            if (player1Input.isEmpty()) {
                errorLabel.setText("Please enter a name for Player 1.");
                return;
            }

            // Check if player 2 name is empty if it's PvP mode
            if (!isPlayerVsComputer && player2Input.isEmpty()) {
                errorLabel.setText("Please enter a name for Player 2.");
                return;
            }

            // Set the player names
            player1Name = player1Input;
            player2Name = isPlayerVsComputer ? "Computer" : player2Input;

            // Clear the error message and proceed to coin setup scene
            errorLabel.setText("");
            primaryStage.setScene(coinSetupScene);
        });

        // Add components to the layout
        playerNameLayout.getChildren().addAll(playerNameLabel, player1NameLabel, player1NameInput);
        if (!isPlayerVsComputer) {
            playerNameLayout.getChildren().addAll(player2NameLabel, player2NameInput);
        }
        playerNameLayout.getChildren().addAll(proceedButton, errorLabel);

        playerNameScene = new Scene(playerNameLayout, 400, 400);
    }

//    private void setupCoinSetupScene() {
//        VBox coinSetupLayout = new VBox(10);
//        coinSetupLayout.setPadding(new Insets(10));
//        coinSetupLayout.setAlignment(Pos.CENTER);
//
//        Label setupLabel = new Label("Coin Setup");
//        setupLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
//        
//        Label coinCountLabel = new Label("Enter number of coins:");
//        coinCountInput.setPromptText("e.g., 5");
//
//        Label inputLabel = new Label("Enter coins (comma-separated):");
//        coinsInput.setPromptText("e.g., 4, 15, 7, 3, 8, 9");
//
//        HBox buttonBox = new HBox(10);
//        Button loadCoinsButton = new Button("Load Coins");
//        loadCoinsButton.setOnAction(e -> {
//            if (validateCoinCount()) {
//                loadCoins();
//            }
//        });
//
//        Button randomCoinsButton = new Button("Generate Random Coins");
//        randomCoinsButton.setOnAction(e -> {
//            if (validateCoinCount()) {
//                generateRandomCoins();
//                primaryStage.setScene(playGameScene);
//            }
//        });
//
//        buttonBox.getChildren().addAll(loadCoinsButton, randomCoinsButton);
//        coinSetupLayout.getChildren().addAll(setupLabel, coinCountLabel, coinCountInput, inputLabel, coinsInput, buttonBox);
//        coinSetupScene = new Scene(coinSetupLayout, 400, 400);
//    }
//
//    private boolean validateCoinCount() {
//        try {
//            coinCount = Integer.parseInt(coinCountInput.getText().trim());
//            if (coinCount <= 0 || coinCount > MAX_COINS) {
//                moveHistoryArea.appendText("Error: Please enter a valid coin count (1 to " + MAX_COINS + ").\n");
//                return false;
//            }
//            return true;
//        } catch (NumberFormatException e) {
//            moveHistoryArea.appendText("Error: Invalid input for number of coins.\n");
//            return false;
//        }
//    }
//
//    private void loadCoins() {
//        String input = coinsInput.getText();
//        String[] coinStrings = input.split(",");
//
//        if (coinStrings.length != coinCount) {
//            moveHistoryArea.appendText("Error: The number of coins entered does not match the specified count. Please enter exactly " + coinCount + " coins.\n");
//            return;
//        }
//
//        resetGameState();
//        try {
//            for (int i = 0; i < coinCount; i++) {
//                coins[i] = Integer.parseInt(coinStrings[i].trim());
//                originalCoins[i] = coins[i];
//            }
//            moveHistoryArea.appendText("Coins loaded: ");
//            for (int i = 0; i < coinCount; i++) {
//                moveHistoryArea.appendText(coins[i] + " ");
//            }
//            moveHistoryArea.appendText("\n");
//
//            initializeCoinRow();
//            calculateOptimalStrategy();
//            primaryStage.setScene(playGameScene);
//        } catch (NumberFormatException e) {
//            moveHistoryArea.appendText("Error: Please ensure all coins are valid integers.\n");
//        }
//    }
//
//    private void generateRandomCoins() {
//        resetGameState();
//        for (int i = 0; i < coinCount; i++) {
//            coins[i] = (int) (Math.random() * 20) + 1;
//            originalCoins[i] = coins[i];
//        }
//        moveHistoryArea.appendText("Random coins generated: ");
//        for (int i = 0; i < coinCount; i++) {
//            moveHistoryArea.appendText(coins[i] + " ");
//        }
//        moveHistoryArea.appendText("\n");
//
//        initializeCoinRow();
//        calculateOptimalStrategy();
//        primaryStage.setScene(playGameScene);
//    }

    
    

    private void setupCoinSetupScene() {
        VBox coinSetupLayout = new VBox(10);
        coinSetupLayout.setPadding(new Insets(10));
        coinSetupLayout.setAlignment(Pos.CENTER);

        Label setupLabel = new Label("Coin Setup");
        setupLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        Label coinCountLabel = new Label("Enter number of coins:");
        coinCountInput.setPromptText("e.g., 5");

        Label inputLabel = new Label("Enter coins (comma-separated):");
        coinsInput.setPromptText("e.g., 4, 15, 7, 3, 8, 9");

        // Error message label styling
        errorMessageLabel.setStyle("-fx-text-fill: red;");

        HBox buttonBox = new HBox(10);
        Button loadCoinsButton = new Button("Load Coins");
        loadCoinsButton.setOnAction(e -> {
            if (validateCoinCount()) {
                loadCoins();
            } else {
                errorMessageLabel.setText("Please enter a valid number of coins.");
            }
        });

        Button randomCoinsButton = new Button("Generate Random Coins");
        randomCoinsButton.setOnAction(e -> {
            if (validateCoinCount()) {
                generateRandomCoins();
                primaryStage.setScene(playGameScene);
            } else {
            	 errorMessageLabel.setText("Error: Please enter a valid even number of coins (1 to " + MAX_COINS + ").");
            }
        });

        buttonBox.getChildren().addAll(loadCoinsButton, randomCoinsButton);
        coinSetupLayout.getChildren().addAll(setupLabel, coinCountLabel, coinCountInput, inputLabel, coinsInput, buttonBox, errorMessageLabel);
        coinSetupScene = new Scene(coinSetupLayout, 450, 450);
    }

    private boolean validateCoinCount() {
        try {
            coinCount = Integer.parseInt(coinCountInput.getText().trim());
            if (coinCount <= 0 || coinCount > MAX_COINS || coinCount % 2 != 0) {
                errorMessageLabel.setText("Error: Please enter a valid even number of coins (1 to " + MAX_COINS + ").");
                return false;
            }
            errorMessageLabel.setText(""); // Clear error if valid
            return true;
        } catch (NumberFormatException e) {
            errorMessageLabel.setText("Error: Invalid input for number of coins.");
            return false;
        }
    }
    private void loadCoins() {
        String input = coinsInput.getText();
        String[] coinStrings = input.split(",");

        if (coinStrings.length != coinCount) {
            errorMessageLabel.setText("Error: The number of coins entered does not match the specified count.\n Please enter exactly " + coinCount + " coins.");
            return;
        }

        resetGameState();
        try {
            for (int i = 0; i < coinCount; i++) {
                coins[i] = Integer.parseInt(coinStrings[i].trim());
                originalCoins[i] = coins[i];
            }
            moveHistoryArea.appendText("Coins loaded: ");
            for (int i = 0; i < coinCount; i++) {
                moveHistoryArea.appendText(coins[i] + " ");
            }
            moveHistoryArea.appendText("\n");

            initializeCoinRow();
            calculateOptimalStrategy();
            errorMessageLabel.setText(""); // Clear error if successful
            primaryStage.setScene(playGameScene);
        } catch (NumberFormatException e) {
            errorMessageLabel.setText("Error: Please ensure all coins are valid integers.");
        }
    }

    private void generateRandomCoins() {
        resetGameState();
        for (int i = 0; i < coinCount; i++) {
            coins[i] = (int) (Math.random() * 20) + 1;
            originalCoins[i] = coins[i];
        }
        moveHistoryArea.appendText("Random coins generated: ");
        for (int i = 0; i < coinCount; i++) {
            moveHistoryArea.appendText(coins[i] + " ");
        }
        moveHistoryArea.appendText("\n");

        initializeCoinRow();
        calculateOptimalStrategy();
        errorMessageLabel.setText(""); // Clear error if successful
        primaryStage.setScene(playGameScene);
    }
    private void resetGameState() {
        player1Score = 0;
        player2Score = 0;
        player1ScoreLabel.setText(player1NameInput.getText()+" Score: 0");
        player2ScoreLabel.setText(isPlayerVsComputer ? "Computer Score: 0" : player2NameInput.getText()+" Score: 0");
        isPlayerTurn = true;
        moveHistoryArea.clear();
        dynamicProgrammingGrid.getChildren().clear();
        coinRow.getChildren().clear();
    }

    private void resetGame() {
        System.arraycopy(originalCoins, 0, coins, 0, coinCount);
        resetGameState();
        initializeCoinRow();
        calculateOptimalStrategy();
    }

    private void initializeCoinRow() {
        coinRow.getChildren().clear();
        for (int i = 0; i < coinCount; i++) {
            Button coinButton = new Button(String.valueOf(coins[i]));
            coinButton.setStyle("-fx-background-color: lightblue; -fx-font-size: 14;");
            int index = i;
            coinButton.setOnAction(e -> handleCoinSelection(index));
            coinRow.getChildren().add(coinButton);
        }
    }

    private void handleCoinSelection(int index) {
        System.out.println("DEBUG: handleCoinSelection called with index " + index); // Debug

        if (!isPlayerTurn && isPlayerVsComputer) {
            System.out.println("DEBUG: Not player's turn, waiting for computer's move."); // Debug
            return;
        }

        Button selectedCoin = (Button) coinRow.getChildren().get(index);
        int coinValue = coins[index];
        coins[index] = -1; // Mark the coin as taken
        selectedCoin.setDisable(true);
        selectedCoin.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

        chosenCoins.add(coinValue);
        if (isPlayerTurn) {
            // Player 1's turn
            player1Score += coinValue;
            player1ScoreLabel.setText(player1NameInput.getText() + " Score: " + player1Score);
            moveHistoryArea.appendText(player1NameInput.getText() + " selects coin with value " + coinValue + "\n");
            isPlayerTurn = false;

            System.out.println("DEBUG: Player 1 selected coin with value " + coinValue); // Debug

            if (isGameOver()) {
                endGame();
                return;
            }

            // If the game mode is Player vs Computer, let the computer take the next turn
            if (isPlayerVsComputer) {
                handleComputerMove();
            }
        } else {
            // Player 2 or Computer's turn
            player2Score += coinValue;
            if (isPlayerVsComputer) {
                player2ScoreLabel.setText("Computer Score: " + player2Score);
                moveHistoryArea.appendText("Computer selects coin with value " + coinValue + "\n");
                System.out.println("DEBUG: Computer selected coin with value " + coinValue); // Debug
            } else {
                player2ScoreLabel.setText(player2NameInput.getText() + " Score: " + player2Score);
                moveHistoryArea.appendText(player2NameInput.getText() + " selects coin with value " + coinValue + "\n");
                System.out.println("DEBUG: Player 2 selected coin with value " + coinValue); // Debug
            }
            isPlayerTurn = true;

            if (isGameOver()) {
                endGame();
            }
        }
    }

    private boolean isGameOver() {
        System.out.println("DEBUG: Checking if game is over. Current coin states:");
        for (int coin : coins) {
            System.out.print(coin + " ");
        }
        System.out.println(); // New line for readability

        for (int coin : coins) {
            // Treat both -1 and 0 as "taken" coins
            if (coin != -1 && coin != 0) return false;
        }
        System.out.println("DEBUG: Game is over - all coins taken.");
        return true;
    }

//    private void endGame() {
//        System.out.println("DEBUG: Game over triggered."); // Debug
//        StringBuilder result = new StringBuilder();
//        result.append("Final Scores:\n");
//        result.append(player1NameInput.getText()+": ").append(player1Score).append("\n");
//        result.append(isPlayerVsComputer ? "Computer: " : player2NameInput.getText()+": ").append(player2Score).append("\n");
//
//        if (player1Score > player2Score) {
//            result.append(player1NameInput.getText()+" wins!\n");
//        } else if (player2Score > player1Score) {
//            result.append(isPlayerVsComputer ? "Computer wins!\n" : player2NameInput.getText()+" wins!\n");
//        } else {
//            result.append("It's a tie!\n");
//        }
//
//     // Display chosen coin sequence
//        result.append("Chosen Coins Sequence: ").append(chosenCoins).append("\n");
//
//       
//
//        finalScoresLabel.setText(result.toString());
//        primaryStage.setScene(gameOverScene);
//        System.out.println("DEBUG: Switching to Game Over screen."); // Debug
//    }
    private void endGame() {
        System.out.println("DEBUG: Game over triggered."); // Debug
        StringBuilder result = new StringBuilder();
        int expectedResult = dpTable[0][coinCount - 1];
        
        result.append("Final Scores:\n");
        result.append(player1NameInput.getText()).append(": ").append(player1Score).append("\n");
        result.append(isPlayerVsComputer ? "Computer: " : player2NameInput.getText()).append(": ").append(player2Score).append("\n");

        if (player1Score > player2Score) {
            result.append(player1NameInput.getText()).append(" wins!\n");
        } else if (player2Score > player1Score) {
            result.append(isPlayerVsComputer ? "Computer wins!\n" : player2NameInput.getText()).append(" wins!\n");
        } else {
            result.append("It's a tie!\n");
        }

        // Display chosen coin sequence
        result.append("Chosen Coins Sequence: ").append(chosenCoins).append("\n");

        // Display Expected vs Actual Result comparison
        result.append("Expected Optimal Score for ").append(player1NameInput.getText()).append(": ").append(expectedResult).append("\n");
        result.append("Actual Score for ").append(player1NameInput.getText()).append(": ").append(player1Score).append("\n");

        if (player1Score == expectedResult) {
            result.append("Congratulations! ").append(player1NameInput.getText()).append(" achieved the optimal score!\n");
        } else {
            result.append(player1NameInput.getText()).append(" did not achieve the optimal score.\n");
        }

        finalScoresLabel.setText(result.toString());
        primaryStage.setScene(gameOverScene);
        System.out.println("DEBUG: Switching to Game Over screen."); // Debug
    }
    
    
    
    private void handleComputerMove() {
        System.out.println("DEBUG: Computer's move initiated."); // Debug
        PauseTransition pause = new PauseTransition(Duration.seconds(0.5));
        pause.setOnFinished(e -> {
            int firstIndex = -1;
            int lastIndex = -1;

            // Find the first and last available coins
            for (int i = 0; i < coinCount; i++) {
                if (coins[i] != -1) {
                    firstIndex = i;
                    break;
                }
            }
            for (int i = coinCount - 1; i >= 0; i--) {
                if (coins[i] != -1) {
                    lastIndex = i;
                    break;
                }
            }

            // If no coins are left, end the game
            if (firstIndex == -1 || lastIndex == -1) {
                System.out.println("DEBUG: No coins left, ending game."); // Debug
                endGame();
                return;
            }

            // Choose a coin (for simplicity, we pick the first available)
            int chosenIndex = firstIndex;
            System.out.println("DEBUG: Computer selects index " + chosenIndex + " with value " + coins[chosenIndex]); // Debug

            // Update the button in the UI to reflect the computer's choice
            Button selectedCoin = (Button) coinRow.getChildren().get(chosenIndex);
            int coinValue = coins[chosenIndex];
            coins[chosenIndex] = -1; // Mark the coin as taken
            selectedCoin.setDisable(true);
            selectedCoin.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

            // Update computer's score and display it
            player2Score += coinValue;
            player2ScoreLabel.setText("Computer Score: " + player2Score);
            moveHistoryArea.appendText("Computer selects coin with value " + coinValue + "\n");

            // Check if game is over after computer's move
            if (isGameOver()) {
                endGame();
            } else {
                isPlayerTurn = true; // Switch turn back to Player 1
            }
        });
        pause.play();
    }
    
    
    private void calculateOptimalStrategy() {
        dpTable = new int[coinCount][coinCount];
        for (int i = 0; i < coinCount; i++) {
            dpTable[i][i] = coins[i];
        }
        for (int length = 2; length <= coinCount; length++) {
            for (int i = 0; i < coinCount - length + 1; i++) {
                int j = i + length - 1;
                int pickFirst = coins[i] + Math.min(i + 2 < coinCount ? dpTable[i + 2][j] : 0,
                                                    i + 1 <= j - 1 ? dpTable[i + 1][j - 1] : 0);
                int pickLast = coins[j] + Math.min(i <= j - 2 ? dpTable[i][j - 2] : 0,
                                                   i + 1 <= j - 1 ? dpTable[i + 1][j - 1] : 0);
                dpTable[i][j] = Math.max(pickFirst, pickLast);
            }
        }
        maxAmountLabel.setText("Maximum Amount First Player Can Win: " + dpTable[0][coinCount - 1]);
    }

    private void displayDpTable(int[][] dp) {
        dynamicProgrammingGrid.getChildren().clear();
        for (int i = 0; i < dp.length; i++) {
            for (int j = i; j < dp[i].length; j++) {
                Label cell = new Label(String.valueOf(dp[i][j]));
                cell.setStyle("-fx-border-color: black; -fx-padding: 8; -fx-background-color: white;");
                cell.setPrefSize(50, 50);
                dynamicProgrammingGrid.add(cell, j, i);
            }
        }
    }
    
    
    
    

    private void setupPlayGameScene() {
        VBox playGameLayout = new VBox(10);
        playGameLayout.setPadding(new Insets(10));
        playGameLayout.setAlignment(Pos.CENTER);

        maxAmountLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        // Set the player score labels with names entered by the user
        player1ScoreLabel = new Label(player1Name + " Score: 0");
        player2ScoreLabel = new Label(player2Name + " Score: 0");

        HBox scoresBox = new HBox(20, player1ScoreLabel, player2ScoreLabel);
        scoresBox.setAlignment(Pos.CENTER);

        moveHistoryArea.setEditable(false);
        moveHistoryArea.setPrefHeight(150);
        moveHistoryArea.setWrapText(true);

        Button dpTableButton = new Button("Show DP Table");
        dpTableButton.setOnAction(e -> {
            displayDpTable(dpTable);
            primaryStage.setScene(dpTableScene);
        });

        Button resetButton = new Button("Reset Game");
        resetButton.setOnAction(e -> resetGame());

        // Ensure coinRow is centered
        coinRow.setAlignment(Pos.CENTER);

        VBox gameplaySection = new VBox(10, maxAmountLabel, coinRow, scoresBox, moveHistoryArea, dpTableButton, resetButton);
        gameplaySection.setAlignment(Pos.CENTER);

        playGameLayout.getChildren().addAll(gameplaySection);
        playGameScene = new Scene(playGameLayout, 600, 800);
    }


    private void setupGameOverScene() {
        VBox gameOverLayout = new VBox(20);
        gameOverLayout.setPadding(new Insets(20));
        gameOverLayout.setAlignment(Pos.CENTER);

        Label gameOverLabel = new Label("Game Over");
        gameOverLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        finalScoresLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;"); // Style for final scores

        Button mainMenuButton = new Button("Exit");
        mainMenuButton.setOnAction(e -> Platform.exit());


        gameOverLayout.getChildren().addAll(gameOverLabel, finalScoresLabel, mainMenuButton);
        gameOverScene = new Scene(gameOverLayout, 400, 300);
    }

    private void setupDpTableScene() {
        VBox dpTableLayout = new VBox(10);
        dpTableLayout.setPadding(new Insets(10));
        dpTableLayout.setAlignment(Pos.CENTER);

        Label dpTableLabel = new Label("DP Table");
        dpTableLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        dynamicProgrammingGrid.setPadding(new Insets(10));
        dynamicProgrammingGrid.setHgap(5);
        dynamicProgrammingGrid.setVgap(5);
        dynamicProgrammingGrid.setAlignment(Pos.CENTER);

        Button backButton = new Button("Back to Game");
        backButton.setOnAction(e -> primaryStage.setScene(playGameScene));

        dpTableLayout.getChildren().addAll(dpTableLabel, dynamicProgrammingGrid, backButton);
        dpTableScene = new Scene(dpTableLayout, 500, 500);

    }
    

    public static void main(String[] args) {
        launch(args);
    }
}
