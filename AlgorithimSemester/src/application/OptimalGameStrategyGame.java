package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

public class OptimalGameStrategyGame extends Application {
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
	private boolean isComputerVsComputer;
	private Scene mainInterfaceScene, gameModeScene, coinSetupScene, playGameScene, gameOverScene, playerNameScene;
	private Stage primaryStage;
	private int[][] dpTable;
	private String player1Name = "Player 1";
	private String player2Name = "Player 2";
	private TextField player1NameInput = new TextField("");
	private TextField player2NameInput = new TextField("");
	private Label errorMessageLabel = new Label(); // Label for displaying error messages
	private StringBuilder player1ChosenCoins = new StringBuilder();
	private StringBuilder player2ChosenCoins = new StringBuilder();

	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		primaryStage.setTitle("Optimal Game Strategy");

		setupMainInterfaceScene();
		setupGameModeScene();
		setupCoinSetupScene();
		setupPlayGameScene();
		setupGameOverScene();
//		file:/Users/mustafaalayasa/Desktop/eclipse-workspace/JavaFx-workspace/AlgorithimSemester/src/CoinGame.jpg
		primaryStage.setScene(mainInterfaceScene);
		primaryStage.show();
	}

	private void setupMainInterfaceScene() {
	    // Create a vertical layout for the main interface
	    VBox mainInterfaceLayout = new VBox(20);

	    BackgroundImage.applyBackgroundImage(mainInterfaceLayout, "file:/Users/mustafaalayasa/Desktop/eclipse-workspace/JavaFx-workspace/AlgorithimSemester/src/CoinGame.jpg");

	    mainInterfaceLayout.setPadding(new Insets(20));
	    mainInterfaceLayout.setAlignment(Pos.CENTER);

	    // Add a welcome label with a large font size and white text color
	    Label welcomeLabel = new Label("Welcome to the Optimal Game Strategy");
	    welcomeLabel.setFont(Font.font("Century Gothic"));
	    welcomeLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: white;");

	    // Add a button to start the game and navigate to the game mode scene
	    Button startGameButton = new Button("Start Game");
	    startGameButton.setOnAction(e -> primaryStage.setScene(gameModeScene));

	    // Add the label and button to the layout
	    mainInterfaceLayout.getChildren().addAll(welcomeLabel, startGameButton);

	    // Create the scene with the layout and set dimensions
	    mainInterfaceScene = new Scene(mainInterfaceLayout, 600, 450);
	}

	private void setupGameModeScene() {
	    // Create a vertical layout for the game mode selection
	    VBox gameModeLayout = new VBox(20);

	    // Set a background image for the scene
	    BackgroundImage.applyBackgroundImage(gameModeLayout, "file:/Users/mustafaalayasa/Desktop/eclipse-workspace/JavaFx-workspace/AlgorithimSemester/src/CoinGame.jpg");

	    // Add padding and alignment for the layout
	    gameModeLayout.setPadding(new Insets(20));
	    gameModeLayout.setAlignment(Pos.CENTER);

	    // Add a label to prompt the user to choose a game mode
	    Label modeLabel = new Label("Choose Game Mode:");
	    modeLabel.setStyle("-fx-font-size: 30px; -fx-font-weight: bold; -fx-text-fill: white;");

	    // Add buttons for the two game modes
	    Button pvpButton = new Button("Player vs Player");
	    pvpButton.setOnAction(e -> {
	        isComputerVsComputer = false; // Set mode to Player vs Player
	        setupPlayerNameScene(); // Initialize the player name input scene
	        primaryStage.setScene(playerNameScene);
	    });

	    Button cvcButton = new Button("Computer vs Computer");
	    cvcButton.setOnAction(e -> {
	        isComputerVsComputer = true; // Set mode to Computer vs Computer
	        player1Name = "Computer 1"; // Default names for Computer players
	        player2Name = "Computer 2";
	        primaryStage.setScene(coinSetupScene);
	    });

	    Button backButton = new Button("Back");
	    backButton.setOnAction(e -> primaryStage.setScene(mainInterfaceScene));

	    // Arrange the buttons horizontally
	    HBox gameModeButtons = new HBox(10, pvpButton, cvcButton);
	    gameModeButtons.setAlignment(Pos.CENTER);

	    gameModeLayout.getChildren().addAll(modeLabel, gameModeButtons, backButton);

	    // Create the scene with the layout and set dimensions
	    gameModeScene = new Scene(gameModeLayout, 600, 450);
	}


	private void setupPlayerNameScene() {
	    // Create a vertical layout for the player name input scene
	    VBox playerNameLayout = new VBox(20);

	    // Set a background image for the scene
	    BackgroundImage.applyBackgroundImage(playerNameLayout, "file:/Users/mustafaalayasa/Desktop/eclipse-workspace/JavaFx-workspace/AlgorithimSemester/src/Coin2.jpg");

	    // Add padding and alignment for the layout
	    playerNameLayout.setPadding(new Insets(20));
	    playerNameLayout.setAlignment(Pos.CENTER);

	    // Add a label for the title of the scene
	    Label playerNameLabel = new Label("Enter Player Names:");
	    playerNameLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

	    // Add a label to display error messages
	    Label errorLabel = new Label();
	    errorLabel.setStyle("-fx-text-fill: red;");

	    // Add labels and text fields for player names
	    Label player1NameLabel = new Label("Player 1 Name:");
	    player1NameLabel.setStyle("-fx-text-fill: white;");
	    TextField player1NameInput = new TextField();
	    player1NameInput.setPromptText("Enter Player 1 Name");

	    Label player2NameLabel = new Label("Player 2 Name:");
	    player2NameLabel.setStyle("-fx-text-fill: white;");
	    TextField player2NameInput = new TextField();
	    player2NameInput.setPromptText("Enter Player 2 Name");

	    // Add a button to proceed to the next scene
	    Button proceedButton = new Button("Proceed to Coin Setup");
	    proceedButton.setOnAction(e -> {
	        String player1Input = player1NameInput.getText().trim();
	        String player2Input = player2NameInput.getText().trim();

	        // Validation for computer mode
	        if (isComputerVsComputer) {
	            player1Name = "Computer 1";
	            player2Name = "Computer 2";
	            primaryStage.setScene(coinSetupScene);
	        } else {
	            if (player1Input.isEmpty()) {
	                errorLabel.setText("Error: Please enter a name for Player 1.");
	                return;
	            }
	            if (player2Input.isEmpty()) {
	                errorLabel.setText("Error: Please enter a name for Player 2.");
	                return;
	            }
	            if (player1Input.equalsIgnoreCase(player2Input)) {
	                errorLabel.setText("Error: Player names must be different.");
	                return;
	            }

	            // Valid input
	            player1Name = player1Input;
	            player2Name = player2Input;
	            errorLabel.setText("");
	            primaryStage.setScene(coinSetupScene);
	        }
	    });

	    // Add a Back button to return to the game mode selection scene
	    Button backButton = new Button("Back");
	    backButton.setOnAction(e -> primaryStage.setScene(gameModeScene));

	    // Add all elements to the layout
	    playerNameLayout.getChildren().addAll(
	        playerNameLabel,
	        player1NameLabel, player1NameInput,
	        player2NameLabel, player2NameInput,
	        proceedButton, backButton, errorLabel
	    );

	    // Create the scene with the layout and set dimensions
	    playerNameScene = new Scene(playerNameLayout, 600, 450);
	}

	
	
//	private void setupCoinSetupScene() {
//		VBox coinSetupLayout = new VBox(10);
//		coinSetupLayout.setPadding(new Insets(10));
//		coinSetupLayout.setAlignment(Pos.CENTER);
//		BackgroundImage.applyBackgroundImage(coinSetupLayout, "file:/Users/mustafaalayasa/Desktop/eclipse-workspace/JavaFx-workspace/AlgorithimSemester/src/Coin2.jpg");
//
//
//		Label setupLabel = new Label("Coin Setup");
//		setupLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
//
//		Label coinCountLabel = new Label("Enter number of coins:");
//		coinCountInput.setPromptText("e.g., 5");
//
//		Label inputLabel = new Label("Enter coins (comma-separated):");
//		coinsInput.setPromptText("e.g., 4, 15, 7, 3, 8, 9");
//
//		// Error message label styling
//		errorMessageLabel.setStyle("-fx-text-fill: red;");
//
//		HBox buttonBox = new HBox(10);
//
//		// Load Coins Button
//		Button loadCoinsButton = new Button("Load Coins");
//		loadCoinsButton.setOnAction(e -> {
//			if (validateCoinCount()) {
//				loadCoins();
//			} else {
//				errorMessageLabel.setText("Please enter a valid number of coins.");
//			}
//		});
//
//		// Random Coins Button
//		Button randomCoinsButton = new Button("Generate Random Coins");
//		randomCoinsButton.setOnAction(e -> {
//			if (validateCoinCount()) {
//				generateRandomCoins();
//				primaryStage.setScene(playGameScene);
//			} else {
//				errorMessageLabel.setText("Error: Please enter a valid even number of coins (1 to " + MAX_COINS + ").");
//			}
//		});
//
//		// Load from File Button
//		Button fileLoadButton = new Button("Load Coins from File");
//		fileLoadButton.setOnAction(e -> loadCoinsFromFile());
//
//		buttonBox.getChildren().addAll(loadCoinsButton, randomCoinsButton, fileLoadButton);
//		coinSetupLayout.getChildren().addAll(setupLabel, coinCountLabel, coinCountInput, inputLabel, coinsInput,
//				buttonBox, errorMessageLabel);
//		coinSetupScene = new Scene(coinSetupLayout, 450, 450);
//	}
	private void setupCoinSetupScene() {
	    VBox coinSetupLayout = new VBox(15); // Main layout with spacing
	    coinSetupLayout.setPadding(new Insets(15));
	    coinSetupLayout.setAlignment(Pos.CENTER);

	    // Set the background image
	    BackgroundImage.applyBackgroundImage(coinSetupLayout, "file:/Users/mustafaalayasa/Desktop/eclipse-workspace/JavaFx-workspace/AlgorithimSemester/src/Coin2.jpg");

	    // Title
	    Label setupLabel = new Label("Coin Setup");
	    setupLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

	    // Step 1: Enter the number of coins
	    Label step1Label = new Label("Step 1: Enter an even number of coins to play with.");
	    step1Label.setStyle("-fx-font-size: 15px; -fx-text-fill: white;");

	    Label step1ExampleLabel = new Label("Example: Enter 4, 6, or 8 (must be positive and even).");
	    step1ExampleLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: white;");

	    coinCountInput.setPromptText("e.g., 4");
	    coinCountInput.setMaxWidth(200);

	    // Error message for Step 1
	    Label step1ErrorLabel = new Label();
	    step1ErrorLabel.setStyle("-fx-text-fill: red;");

	    // Step 2: Manual Input Instructions
	    Label manualInputLabel = new Label("Option 1: Enter coins manually (comma-separated):");
	    manualInputLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: white;");

	    Label manualInputExampleLabel = new Label("Example: 5,6,8,10 (values must be positive integers).");
	    manualInputExampleLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");

	    coinsInput.setPromptText("e.g., 5,6,8");
	    coinsInput.setMaxWidth(300);

	    Label manualInputErrorLabel = new Label();
	    manualInputErrorLabel.setStyle("-fx-text-fill: red;");

	  
	    
	    // Random Generation Section
	    Label randomInputLabel = new Label("Option 2: Generate coins randomly.");
	    randomInputLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: white;");

	    Label randomInputExampleLabel = new Label("The range will be provided in a popup.");
	    randomInputExampleLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: white;");

	    // Load from File Section
	    Label fileInputLabel = new Label("Option 3: Load coins from a file.");
	    fileInputLabel.setStyle("-fx-font-size: 15px; -fx-text-fill: white;");

	    // Buttons for actions
	    Button loadCoinsButton = new Button("Load Coins");
	    Button randomCoinsButton = new Button("Generate Random Coins");
	    Button fileLoadButton = new Button("Load Coins from File");

	    loadCoinsButton.setOnAction(e -> {
	    	
		        if (validateCoinCount() && validateCoinsInput(coinsInput.getText())) {
		            loadCoins(); // Logic to load coins
		            step1ErrorLabel.setText(""); // Clear any previous errors
		            manualInputErrorLabel.setText(""); // Clear any manual input errors
		        } else {
		            manualInputErrorLabel.setText("Error: Enter values as comma-separated integers matching the coin count.");
		        }
	    	
	    
	    });

	    randomCoinsButton.setOnAction(e -> {
	        if (validateCoinCount()) {
	        	generateRandomCoins(); // Show range popup
	            step1ErrorLabel.setText(""); // Clear previous errors
	        } else {
	            step1ErrorLabel.setText("Error: Enter a valid even number of coins first.");
	        }
	    });

	    fileLoadButton.setOnAction(e -> {
	        if (validateCoinCount()) {
	            loadCoinsFromFile();
	            step1ErrorLabel.setText(""); // Clear previous errors
	        } else {
	            step1ErrorLabel.setText("Error: Enter a valid even number of coins first.");
	        }
	    });

	    // Organize Buttons in a Horizontal Layout
	    HBox buttonBox = new HBox(15, loadCoinsButton, randomCoinsButton, fileLoadButton);
	    buttonBox.setAlignment(Pos.CENTER);
	 // Organize Manual Input Section
	    VBox manualInputSection = new VBox(5, manualInputLabel, manualInputExampleLabel, coinsInput, manualInputErrorLabel);
	    manualInputSection.setAlignment(Pos.CENTER);

	    // Combine all components into the layout
	    coinSetupLayout.getChildren().addAll(
	        setupLabel,
	        step1Label,
	        step1ExampleLabel,
	        coinCountInput,
	        step1ErrorLabel,
	        manualInputSection,
	        manualInputLabel,
	        coinsInput,
	        randomInputLabel,
	        randomInputExampleLabel,
	        fileInputLabel,
	        buttonBox
	    );

	    // Create the scene
	    coinSetupScene = new Scene(coinSetupLayout, 600, 500);
	}

	    private boolean validateCoinsInput(String coinsText) {
	        if (coinsText == null || coinsText.isEmpty()) return false;

	        String[] coinValues = coinsText.split(",");
	        try {
	            for (String coin : coinValues) {
	                int value = Integer.parseInt(coin.trim());
	                if (value <= 0) return false; // Values must be positive integers
	            }
	            return coinValues.length == coinCount; // Ensure the number of coins matches the coin count
	        } catch (NumberFormatException e) {
	            return false; // Invalid input format
	        }
	    }

	private void setupPlayGameScene() {
	    // Create the main layout for the Play Game scene
	    VBox playGameLayout = new VBox(10);
	    playGameLayout.setPadding(new Insets(10));
	    playGameLayout.setAlignment(Pos.CENTER);

	    // Set a background image for the scene
	    BackgroundImage.applyBackgroundImage(playGameLayout, "file:/Users/mustafaalayasa/Desktop/eclipse-workspace/JavaFx-workspace/AlgorithimSemester/src/CoinGame.jpg");

	    // Style the max amount label
	    maxAmountLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: white;");

	    // Set player labels based on the game mode and style them with white text
	    player1ScoreLabel = new Label(isComputerVsComputer ? "Computer 1 Score: 0" : player1Name + " Score: 0");
	    player1ScoreLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

	    player2ScoreLabel = new Label(isComputerVsComputer ? "Computer 2 Score: 0" : player2Name + " Score: 0");
	    player2ScoreLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: white;");

	    // Arrange the score labels in a horizontal box
	    HBox scoresBox = new HBox(20, player1ScoreLabel, player2ScoreLabel);
	    scoresBox.setAlignment(Pos.CENTER);

	    // Style and configure the move history area
	    moveHistoryArea.setEditable(false);
	    moveHistoryArea.setPrefHeight(150);
	    moveHistoryArea.setWrapText(true);
	    moveHistoryArea.setStyle("-fx-font-size: 12px; -fx-text-fill: black; -fx-control-inner-background: white;");

	    // Align the coin row
	    coinRow.setAlignment(Pos.CENTER);

	    // Create and configure the Reset button
	    Button resetButton = new Button("Reset Game");
	    resetButton.setOnAction(e -> resetGame());

	    // Create and configure the Game Over button
	    Button gameOverButton = new Button("Go to Game Over Screen");
	    gameOverButton.setId("gameOverButton");
	    gameOverButton.setOnAction(e -> primaryStage.setScene(gameOverScene));
	    gameOverButton.setDisable(true); // Initially disabled

	    // Prepare the DP Table grid pane
	    dynamicProgrammingGrid.setPadding(new Insets(10));
	    dynamicProgrammingGrid.setHgap(5);
	    dynamicProgrammingGrid.setVgap(5);
	    dynamicProgrammingGrid.setAlignment(Pos.CENTER);

	    // Add the DP Table to the layout only in Computer vs Computer mode
	    if (isComputerVsComputer) {
	        calculateOptimalStrategy(); // Build the DP Table
	        displayDpTable(dpTable); // Populate the DP Table with data
	    }

	    // Group the gameplay sections
	    VBox gameplaySection = new VBox(10, maxAmountLabel, coinRow, scoresBox, moveHistoryArea, dynamicProgrammingGrid);
	    gameplaySection.setAlignment(Pos.CENTER);

	    // Add all elements to the main layout
	    playGameLayout.getChildren().addAll(gameplaySection, resetButton, gameOverButton);

	    // Create the Play Game scene with the specified dimensions
	    playGameScene = new Scene(playGameLayout, 800, 1000); // Increased scene size
	}


	private void setupGameOverScene() {
		VBox gameOverLayout = new VBox(20);
		gameOverLayout.setPadding(new Insets(20));
		gameOverLayout.setAlignment(Pos.CENTER);

		Label gameOverLabel = new Label("Game Over");
		gameOverLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: black;");

		finalScoresLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

		// Exit Button
		Button exitButton = new Button("Exit");
		exitButton.setOnAction(e -> Platform.exit());

		// Back to Play Screen Button
		Button backToPlayButton = new Button("Back to Play Game Screen");
		backToPlayButton.setOnAction(e -> primaryStage.setScene(playGameScene));

		HBox buttonBox = new HBox(10, backToPlayButton, exitButton);
		buttonBox.setAlignment(Pos.CENTER);

		gameOverLayout.getChildren().addAll(gameOverLabel, finalScoresLabel, buttonBox);
		gameOverScene = new Scene(gameOverLayout, 400, 300);
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

		if (coinStrings.length != coinCount || coinCount % 2 != 0) {
			errorMessageLabel.setText(
					"Error: The number of coins entered must be even and match the specified count.\n Please enter exactly "
							+ coinCount + " coins.");
			return;
		}

		resetGameState();

		try {
			for (int i = 0; i < coinCount; i++) {
				coins[i] = Integer.parseInt(coinStrings[i].trim());
				originalCoins[i] = coins[i];
			}
			moveHistoryArea.appendText("Coins loaded: ");
			for (int coin : coins) {
				moveHistoryArea.appendText(coin + " ");
			}
			moveHistoryArea.appendText("\n");

			if (isComputerVsComputer) {
				calculateOptimalStrategy(); // Build the DP Table
				displayDpTable(dpTable); // Populate the DP Table in the Play Game Scene
				handleComputerMove();
			}

			initializeCoinRow();
			errorMessageLabel.setText(""); // Clear error if successful
			primaryStage.setScene(playGameScene);
		} catch (NumberFormatException e) {
			errorMessageLabel.setText("Error: Please ensure all coins are valid integers.");
		}
	}

	private void generateRandomCoins() {
		try {
			// Create a dialog to prompt the user for the range of coin values
			TextInputDialog rangeDialog = new TextInputDialog("1-20");
			rangeDialog.setTitle("Generate Coins");
			rangeDialog.setHeaderText("Enter the range for random coin values (e.g., 1-20):");
			rangeDialog.setContentText("Range:");

			String rangeInput = rangeDialog.showAndWait().orElse(""); // Get the input or an empty string if canceled
			if (rangeInput.isEmpty()) {
				errorMessageLabel.setText("Error: No range provided.");
				return;
			}

			// Parse the range input
			String[] rangeParts = rangeInput.split("-");
			if (rangeParts.length != 2) {
				errorMessageLabel.setText("Error: Invalid range format. Use the format 'min-max'.");
				return;
			}

			int minValue = Integer.parseInt(rangeParts[0].trim());
			int maxValue = Integer.parseInt(rangeParts[1].trim());
			if (minValue >= maxValue) {
				errorMessageLabel.setText("Error: Minimum value must be less than maximum value.");
				return;
			}

			// Validate the number of coins from the input field
			if (!validateCoinCount() || coinCount % 2 != 0) {
				errorMessageLabel.setText("Error: Please enter an even number of coins.");
				return;
			}

			// Reset the game state
			resetGameState();

			// Generate random coins within the specified range
			moveHistoryArea.appendText("Random coins generated: ");
			for (int i = 0; i < coinCount; i++) {
				int randomCoin = (int) (Math.random() * (maxValue - minValue + 1)) + minValue;
				coins[i] = randomCoin; // Assign directly to the coins array
				originalCoins[i] = randomCoin; // Assign directly to the originalCoins array
				moveHistoryArea.appendText(randomCoin + " ");
			}
			moveHistoryArea.appendText("\n");

			// If it's Computer vs Computer mode, calculate and display the DP table, then
			// start the game
			if (isComputerVsComputer) {
				calculateOptimalStrategy(); // Build the DP table
				displayDpTable(dpTable); // Show the DP table in the Play Game scene
				handleComputerMove(); // Start the computer gameplay
			}

			// Initialize the coin row for display
			initializeCoinRow();
			errorMessageLabel.setText(""); // Clear any error messages
			primaryStage.setScene(playGameScene); // Switch to the Play Game scene

		} catch (NumberFormatException e) {
			errorMessageLabel.setText("Error: Invalid numeric input in the range or coin count.");
		}
	}

	private void loadCoinsFromFile() {
	    FileChooser fileChooser = new FileChooser();
	    fileChooser.setTitle("Open Coins File");
	    fileChooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));
	    fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

	    File selectedFile = fileChooser.showOpenDialog(primaryStage);

	    if (selectedFile != null) {
	        try (Scanner scanner = new Scanner(selectedFile)) {
	            int expectedCoinCount;

	            // Validate expected coin count
	            try {
	                expectedCoinCount = Integer.parseInt(coinCountInput.getText().trim());
	                if (expectedCoinCount <= 0 || expectedCoinCount % 2 != 0) {
	                    errorMessageLabel.setText("Error: Number of coins must be a positive even number.");
	                    return;
	                }
	            } catch (NumberFormatException e) {
	                errorMessageLabel.setText("Error: Invalid number of coins in input field.");
	                return;
	            }

	            // Initialize variables
	            boolean matchFound = false;
	            StringBuilder availableLinesInfo = new StringBuilder();

	            // Process lines in the file
	            while (scanner.hasNextLine()) {
	                String line = scanner.nextLine().trim();
	                if (line.isEmpty()) continue;

	                String[] coinStrings = line.split(",");
	                int coinCountInLine = coinStrings.length;

	                // Log available lines
	                availableLinesInfo.append("Line: ").append(line)
	                        .append(" → ").append(coinCountInLine).append(" coins\n");

	                if (coinCountInLine == expectedCoinCount) {
	                    matchFound = true;
	                    coins = new int[expectedCoinCount];
	                    originalCoins = new int[expectedCoinCount];

	                    // Parse and validate coin values
	                    try {
	                        for (int i = 0; i < expectedCoinCount; i++) {
	                            coins[i] = Integer.parseInt(coinStrings[i].trim());
	                            originalCoins[i] = coins[i];
	                        }
	                    } catch (NumberFormatException e) {
	                        errorMessageLabel.setText("Error: Invalid coin value in line: " + line);
	                        return;
	                    }

	                    // Reset and update the game state
	                    resetGameState();
	                    dpTable = null;
	                    initializeCoinRow();

	                    // Display loaded coins in the UI
	                    moveHistoryArea.appendText("Coins loaded from file: ");
	                    for (int coin : coins) {
	                        moveHistoryArea.appendText(coin + " ");
	                    }
	                    moveHistoryArea.appendText("\n");

	                    // If Computer vs Computer mode, calculate strategy
	                    if (isComputerVsComputer) {
	                        calculateOptimalStrategy();
	                        displayDpTable(dpTable);
	                        handleComputerMove();
	                    }

	                    errorMessageLabel.setText(""); // Clear error message
	                    primaryStage.setScene(playGameScene);
	                    return;
	                }
	            }

	            // Handle no matching line found
	            if (!matchFound) {
	                errorMessageLabel.setText(
	                        "Error: No line matches the expected number of coins (" + expectedCoinCount + ").\n"
	                                + "Available lines and their coin counts:\n" + availableLinesInfo.toString());
	            }

	        } catch (FileNotFoundException e) {
	            errorMessageLabel.setText("Error: Unable to open file.");
	        } catch (Exception e) {
	            errorMessageLabel.setText("Error: An unexpected error occurred.");
	            e.printStackTrace();
	        }
	    }
	}

	private void resetGameState() {
		player1Score = 0;
		player2Score = 0;
		player1ScoreLabel.setText(player1Name + " Score: 0");
		player2ScoreLabel.setText(isComputerVsComputer ? "Computer Score: 0" : player2Name + " Score: 0");
		isPlayerTurn = true;
		moveHistoryArea.clear();
		dynamicProgrammingGrid.getChildren().clear();
		coinRow.getChildren().clear();
	}

	private void resetGame() {
		System.arraycopy(originalCoins, 0, coins, 0, coinCount);
		resetGameState();
		initializeCoinRow();

		if (isComputerVsComputer) {
			calculateOptimalStrategy();
			displayDpTable(dpTable); // Update the DP Table display
			handleComputerMove(); // Restart Computer vs Computer gameplay
		}
	}

	private void initializeCoinRow() {
		coinRow.getChildren().clear(); // Clear any existing buttons
		for (int i = 0; i < coinCount; i++) {
			Button coinButton = new Button(String.valueOf(coins[i]));
			coinButton.setStyle("-fx-background-color: lightblue; -fx-font-size: 14;");
			int index = i;
			coinButton.setOnAction(e -> handleCoinSelection(index)); // Only for Player vs. Player mode
			coinRow.getChildren().add(coinButton);
		}
	}

	private void handleCoinSelection(int index) {
		if (!isPlayerTurn && isComputerVsComputer) {
			return; // Skip if it's not the player's turn in Computer vs Computer mode
		}

		Button selectedCoin = (Button) coinRow.getChildren().get(index);
		int coinValue = coins[index];
		coins[index] = -1; // Mark the coin as taken
		selectedCoin.setDisable(true);
		selectedCoin.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

		if (isPlayerTurn) {
			// Player 1's turn
			player1Score += coinValue;
			player1ScoreLabel.setText(player1Name + " Score: " + player1Score);

			// Add the coin to Player 1's chosen coins
			player1ChosenCoins.append(coinValue).append(" "); // Track coin value
			moveHistoryArea.appendText(player1Name + " selects coin with value " + coinValue + "\n");

			isPlayerTurn = false; // Switch turn to Player 2
		} else {
			// Player 2's turn
			player2Score += coinValue;
			player2ScoreLabel.setText(player2Name + " Score: " + player2Score);

			// Add the coin to Player 2's chosen coins
			player2ChosenCoins.append(coinValue).append(" "); // Track coin value
			moveHistoryArea.appendText(player2Name + " selects coin with value " + coinValue + "\n");

			isPlayerTurn = true; // Switch turn to Player 1
		}

		if (isGameOver()) {
			endGame(); // Check if the game is over
		}
	}

	private boolean isGameOver() {
//		System.out.println("DEBUG: Checking if game is over. Current coin states:");
//		for (int coin : coins) {
////			System.out.print(coin + " ");
//		}
		System.out.println(); // New line for readability

		for (int coin : coins) {
			// Treat both -1 and 0 as "taken" coins
			if (coin != -1 && coin != 0)
				return false;
		}
//		System.out.println("DEBUG: Game is over - all coins taken.");
		return true;
	}

	private void endGame() {
		StringBuilder result = new StringBuilder();

		if (isComputerVsComputer) {
			// Computer vs Computer Mode
			int expectedResult = dpTable[0][coinCount - 1];
			result.append("Final Scores:\n");
			result.append("Computer 1: ").append(player1Score).append("\n");
			result.append("Computer 2: ").append(player2Score).append("\n");
			result.append("Chosen Coins:\n");
			result.append("Computer 1 Coins: ").append(player1ChosenCoins.toString().trim()).append("\n");
			result.append("Computer 2 Coins: ").append(player2ChosenCoins.toString().trim()).append("\n");
			result.append("Expected Optimal Result: ").append(expectedResult).append("\n");
			result.append(player1Score == expectedResult ? "Optimal Solution Achieved!\n" : "Suboptimal Solution!\n");

			moveHistoryArea.appendText("Game Over! Click the 'Go to Game Over Screen' button to see the results.\n");
		} else {
			// Player vs Player Mode
			result.append("Final Scores:\n");
			result.append(player1Name).append(": ").append(player1Score).append("\n");
			result.append(player2Name).append(": ").append(player2Score).append("\n");
			result.append("Chosen Coins:\n");
			result.append(player1Name).append(" Coins: ").append(player1ChosenCoins.toString().trim()).append("\n");
			result.append(player2Name).append(" Coins: ").append(player2ChosenCoins.toString().trim()).append("\n");

			moveHistoryArea.appendText("Game Over! Click the 'Go to Game Over Screen' button to see the results.\n");
		}

		finalScoresLabel.setText(result.toString());

		// Enable the "Go to Game Over Screen" button
		Button gameOverButton = (Button) playGameScene.lookup("#gameOverButton");
		if (gameOverButton != null) {
			gameOverButton.setDisable(false); // Enable the button
		}
	}

	private void handleComputerMove() {
		if (isGameOver()) {
			endGame(); // Enable the button instead of transitioning directly
			return;
		}

		PauseTransition pause = new PauseTransition(Duration.seconds(2)); // 1-second pause
		pause.setOnFinished(e -> {
			int index = getOptimalCoinIndex(); // Get the best coin index based on the DP table
			if (index == -1) {
				endGame(); // Enable the button instead of transitioning directly
				return;
			}

			Button selectedCoin = (Button) coinRow.getChildren().get(index);
			int coinValue = coins[index];
			coins[index] = -1; // Mark the coin as taken
			selectedCoin.setDisable(true);
			selectedCoin.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

			if (isPlayerTurn) {
				player1Score += coinValue;
				player1ChosenCoins.append(coinValue).append(" "); // Track chosen coins for Computer 1
				player1ScoreLabel.setText("Computer 1 Score: " + player1Score);
				moveHistoryArea.appendText("Computer 1 selects coin with value " + coinValue + "\n");
				isPlayerTurn = false; // Switch turn
			} else {
				player2Score += coinValue;
				player2ChosenCoins.append(coinValue).append(" "); // Track chosen coins for Computer 2
				player2ScoreLabel.setText("Computer 2 Score: " + player2Score);
				moveHistoryArea.appendText("Computer 2 selects coin with value " + coinValue + "\n");
				isPlayerTurn = true; // Switch turn
			}

			handleComputerMove(); // Recursive call for the next move
		});

		pause.play(); // Start the delay
	}

//	private int getOptimalCoinIndex() {
//		int firstIndex = -1;
//		int lastIndex = -1;
//
//		// Find the first and last available coins
//		for (int i = 0; i < coinCount; i++) {
//			if (coins[i] != -1) {
//				firstIndex = i;
//				break;
//			}
//		}
//		for (int i = coinCount - 1; i >= 0; i--) {
//			if (coins[i] != -1) {
//				lastIndex = i;
//				break;
//			}
//		}
//
//		if (firstIndex == -1 || lastIndex == -1) {
//			return -1; // Game over
//		}
//
//		// Calculate potential outcomes
//		int pickFirst = coins[firstIndex]
//				+ Math.min(firstIndex + 2 < coinCount ? dpTable[firstIndex + 2][lastIndex] : 0,
//						firstIndex + 1 <= lastIndex - 1 ? dpTable[firstIndex + 1][lastIndex - 1] : 0);
//		int pickLast = coins[lastIndex] + Math.min(firstIndex <= lastIndex - 2 ? dpTable[firstIndex][lastIndex - 2] : 0,
//				firstIndex + 1 <= lastIndex - 1 ? dpTable[firstIndex + 1][lastIndex - 1] : 0);
//
//		return pickFirst >= pickLast ? firstIndex : lastIndex;
//	}
	
	private int getOptimalCoinIndex() {
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

	    if (firstIndex == -1 || lastIndex == -1) {
	        return -1; // Game over
	    }

	    if (isPlayerTurn) {
	        int pickFirst = coins[firstIndex]
	                + Math.min(firstIndex + 2 < coinCount ? dpTable[firstIndex + 2][lastIndex] : 0,
	                        firstIndex + 1 <= lastIndex - 1 ? dpTable[firstIndex + 1][lastIndex - 1] : 0);
	        int pickLast = coins[lastIndex]
	                + Math.min(firstIndex <= lastIndex - 2 ? dpTable[firstIndex][lastIndex - 2] : 0,
	                        firstIndex + 1 <= lastIndex - 1 ? dpTable[firstIndex + 1][lastIndex - 1] : 0);

	        return pickFirst >= pickLast ? firstIndex : lastIndex;
	    } else {
	        return coins[firstIndex] >= coins[lastIndex] ? firstIndex : lastIndex;
	    }
	}

	private void calculateOptimalStrategy() {
		if (coinCount <= 0 || coins == null || coins.length < coinCount) {
			System.out.println("DEBUG: Invalid coinCount or coins array. coinCount = " + coinCount);
			return; // Exit if coinCount or coins array is invalid
		}

		dpTable = new int[coinCount][coinCount];

		// one coin
		for (int i = 0; i < coinCount; i++) {
			dpTable[i][i] = coins[i];
		}

		// two coins
		for (int i = 0; i < coinCount - 1; i++) {
			dpTable[i][i + 1] = Math.max(coins[i], coins[i + 1]);
		}

		// Fill the DP table for subarrays of length 3 or more
		for (int length = 3; length <= coinCount; length++) {
			for (int i = 0; i < coinCount - length + 1; i++) {

				int pickFirst = coins[i] + Math.min((i + 2 <= (i + length - 1) ? dpTable[i + 2][i + length - 1] : 0),
						(i + 1 <= (i + length - 1) - 1 ? dpTable[i + 1][(i + length - 1) - 1] : 0));
				int pickLast = coins[(i + length - 1)]
						+ Math.min((i <= (i + length - 1) - 2 ? dpTable[i][(i + length - 1) - 2] : 0),
								(i + 1 <= (i + length - 1) - 1 ? dpTable[i + 1][(i + length - 1) - 1] : 0));

				dpTable[i][(i + length - 1)] = Math.max(pickFirst, pickLast);

				
			}
		}

		// Display the maximum amount
		maxAmountLabel.setText("Maximum Amount Computer 1 Can Win: " + dpTable[0][coinCount - 1]);
	}

//4,15,7,3,8,9
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

	public static void main(String[] args) {
		launch(args);
	}
}
