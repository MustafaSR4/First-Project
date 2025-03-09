package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;

import HuffmanProject.HuffmanDecompressor;


public class HuffmanProject extends Application {

	static final int ASCII_SIZE = 256;
	private TextArea outputArea;
	private Button compressButton, decompressButton, statisticsButton, huffmanTableButton, headerButton;
	private File selectedFile;
	private File compressedFile;
	private long originalSize;
	private int numCharacters;
	private long compressedSize;
	private String[] huffmanCodes;
	private int[] frequencies;
	private String originalExtension;
	
	public static void main(String[] args) {
		launch(args); 
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Huffman Compression");

		// Main Layout
		VBox mainLayout = new VBox(20);
		mainLayout.setStyle("-fx-padding: 20; -fx-alignment: center;");

		// Buttons
		compressButton = new Button("Compress");
		decompressButton = new Button("Decompress");
		statisticsButton = new Button("Show Statistics");
		huffmanTableButton = new Button("Show Huffman Table");
		headerButton = new Button("Show Header");

		// Disable buttons initially
		statisticsButton.setDisable(true);
		huffmanTableButton.setDisable(true);
		headerButton.setDisable(true);

		// TextArea for output
		outputArea = new TextArea();
		outputArea.setEditable(false);
		outputArea.setWrapText(true);
		outputArea.setPrefHeight(300);

		// Add components to layout
		mainLayout.getChildren().addAll(compressButton, decompressButton, statisticsButton, huffmanTableButton,
				headerButton, outputArea);

		// Set the Scene and Show
		Scene scene = new Scene(mainLayout, 600, 500);
		primaryStage.setScene(scene);
		primaryStage.show();

		// Button Actions
		compressButton.setOnAction(event -> compressFile(primaryStage));
		decompressButton.setOnAction(event -> decompressFile(primaryStage));
		statisticsButton.setOnAction(event -> showStatistics());
		huffmanTableButton.setOnAction(event -> showHuffmanTable());
		headerButton.setOnAction(event -> showHeader());
	}

	private void compressFile(Stage stage) {
		// Use FileChooser to select the file
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select File to Compress");
		selectedFile = fileChooser.showOpenDialog(stage);

		if (selectedFile != null) {
			// Check if the file is already compressed
			if (getFileExtension(selectedFile).equalsIgnoreCase("hof")) {
				outputArea.setText("Error: Cannot compress an already compressed file.");
				return;
			}

			// Check if the file is empty
			if (selectedFile.length() == 0) {
				outputArea.setText("Error: Selected file is empty and cannot be compressed.");
				return;
			}

			try {
				frequencies = new int[ASCII_SIZE];

				// Step 1: Read file and populate frequency array
				originalSize = selectedFile.length();
				numCharacters = readFileAndCountFrequencies(selectedFile, frequencies);

				// Step 2: Build Huffman Tree
				Node root = buildHuffmanTree(frequencies);

				// Step 3: Generate Huffman codes
				huffmanCodes = new String[ASCII_SIZE];
				generateHuffmanCodes(root, huffmanCodes, "");

				// Step 4: Calculate compressed size
				compressedSize = calculateCompressedSize(frequencies, huffmanCodes);

				// Step 5: Compress the file
				originalExtension = getFileExtension(selectedFile);
				compressedFile = new File(selectedFile.getParent(),
						selectedFile.getName().replaceFirst("[.][^.]+$", "") + ".hof");
				compressFile(selectedFile, compressedFile, huffmanCodes, originalExtension, frequencies);

				// Update output area
				outputArea.setText("Compression completed!\nCompressed file: " + compressedFile.getAbsolutePath());

				// Enable additional buttons
				statisticsButton.setDisable(false);
				huffmanTableButton.setDisable(false);
				headerButton.setDisable(false);

			} catch (IOException e) {
				outputArea.setText("Error during compression: " + e.getMessage());
			}
		} else {
			outputArea.setText("No file selected.");
		}
	}

	private void decompressFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Compressed File (.hof)");
        File hofFile = fileChooser.showOpenDialog(stage);

        if (hofFile != null) {
            if (!getFileExtension(hofFile).equalsIgnoreCase("hof")) {
                outputArea.setText("Error: Selected file is not a compressed (.hof) file.");
                return;
            }

            if (hofFile.length() == 0) {
                outputArea.setText("Error: Selected file is empty and cannot be decompressed.");
                return;
            }

            try {
                HuffmanDecompressor decompressor = new HuffmanDecompressor();
                File decompressedFile = decompressor.decompressFile(hofFile, originalExtension);

                // Compare original and decompressed files
                boolean isCorrect = compareFiles(selectedFile, decompressedFile);
                String comparisonMessage;
                if (isCorrect) {
                    comparisonMessage = "Decompression successful! Files match.";
                } else {
                    comparisonMessage = "Decompression failed. Files do not match.";
                }

                // Build the content preview if files match
                StringBuilder content = new StringBuilder();
                if (isCorrect) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(decompressedFile))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            content.append(line).append("\n");
                        }
                    }
                }

                // Display the final output
                outputArea.setText(String.format(
                        "Decompression completed successfully!\nDecompressed file: %s\nComparison: %s\n\n",
                        decompressedFile.getAbsolutePath(), comparisonMessage));
                if (isCorrect) {
                    outputArea.appendText("Decompressed Content Preview:\n" + content.toString());
                }

            } catch (IOException e) {
                outputArea.setText("Error during decompression: " + e.getMessage());
            }
        } else {
            outputArea.setText("No file selected for decompression.");
        }
    }

    public static boolean compareFiles(File original, File decompressed) throws IOException {
        try (FileInputStream originalStream = new FileInputStream(original);
             FileInputStream decompressedStream = new FileInputStream(decompressed)) {

            int byteOriginal, byteDecompressed;

            while ((byteOriginal = originalStream.read()) != -1) {
                byteDecompressed = decompressedStream.read();
                if (byteOriginal != byteDecompressed) {
                    return false; // Files do not match
                }
            }

            // Check if the decompressed file has extra data
            return decompressedStream.read() == -1; // True if files are identical
        }
    }

	// Show Statistics
    private void showStatistics() {
        if (selectedFile == null || compressedFile == null) {
            outputArea.setText("Please compress a file first.");
            return;
        }

        // Calculate compression ratio
        double compressionRatio = (1.0 - ((double) compressedSize / originalSize)) * 100;

        // Convert sizes
        long originalSizeBits = originalSize * 8; // Original size in bits
        long compressedSizeBits = compressedSize * 8; // Compressed size in bits

        double originalSizeKB = originalSize / 1024.0; // Original size in kilobytes
        double compressedSizeKB = compressedSize / 1024.0; // Compressed size in kilobytes

        // Prepare statistics output
        String stats = String.format(
            "Statistics:\n" +
            "Original Size:\n" +
            "- %d bits\n" +
            "- %d bytes\n" +
            "- %.2f KB\n\n" +
            "Compressed Size:\n" +
            "- %d bits\n" +
            "- %d bytes\n" +
            "- %.2f KB\n\n" +
            "Compression Ratio: %.2f%%",
            originalSizeBits, originalSize, originalSizeKB,
            compressedSizeBits, compressedSize, compressedSizeKB,
            compressionRatio
        );

        // Display statistics
        outputArea.setText(stats);
    }


	// Show Huffman Table
	private void showHuffmanTable() {
		if (huffmanCodes == null || frequencies == null) {
			outputArea.setText("Please compress a file first.");
			return;
		}

		StringBuilder table = new StringBuilder("Huffman Table:\n");
		table.append(String.format("%-10s%-15s%-20s%-20s%-10s\n", "ASCII", "Char", "Fixed-Length", "Huffman",
				"Size (bits)"));
		table.append("-".repeat(75)).append("\n"); // Separator line

		for (int i = 0; i < ASCII_SIZE; i++) {
			if (frequencies[i] > 0) {
				int size = huffmanCodes[i].length(); // Length of Huffman code
				String fixedLength = String.format("%8s", Integer.toBinaryString(i)).replace(' ', '0');
				char character = (char) i;
				String charDisplay = Character.isWhitespace(character) ? " " : String.valueOf(character);

				table.append(String.format("%-20s%-18s%-20s%-20s%-20s\n", i, charDisplay, fixedLength, huffmanCodes[i],
						size));
			}
		}

		outputArea.setText(table.toString());
	}

	// Show Header
	private void showHeader() {
		if (compressedFile == null) {
			outputArea.setText("Please compress a file first.");
			return;
		}
		outputArea.setText("Header:\nFile Extension: " + originalExtension + "\nCompressed File Path: "
				+ compressedFile.getAbsolutePath());
	}

	// Reads the file and populates the frequency array
	public static int readFileAndCountFrequencies(File file, int[] freq) throws IOException {
		Arrays.fill(freq, 0); // Initialize all frequencies to 0
		int totalCharacters = 0;

		try (FileInputStream fis = new FileInputStream(file)) {
			int ch;
			while ((ch = fis.read()) != -1) { // Read one byte at a time
				freq[ch]++;
				totalCharacters++;
			}
		}

		return totalCharacters;
	}

	public static void compressFile(File inputFile, File outputFile, String[] huffmanCodes, String fileExtension,
			int[] frequencies) throws IOException {
		try (FileInputStream fis = new FileInputStream(inputFile);
				DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile))) {

			// Write the header (original file extension)
			dos.writeUTF(fileExtension); // Write original file extension (e.g., "txt")

			// Write the frequency table
			for (int i = 0; i < ASCII_SIZE; i++) {
				dos.writeInt(frequencies[i]); // Write frequency of each character
			}

			// Compress data
			byte[] buffer = new byte[8];
			StringBuilder bitBuffer = new StringBuilder();

			int bytesRead;
			while ((bytesRead = fis.read(buffer)) != -1) {
				for (int i = 0; i < bytesRead; i++) {
					bitBuffer.append(huffmanCodes[buffer[i] & 0xFF]);
				}

				while (bitBuffer.length() >= 8) {
					dos.writeByte(Integer.parseInt(bitBuffer.substring(0, 8), 2));
					bitBuffer.delete(0, 8);
				}
			}

			// Write remaining bits (if any)
			if (bitBuffer.length() > 0) {
				while (bitBuffer.length() < 8) {
					bitBuffer.append("0");
				}
				dos.writeByte(Integer.parseInt(bitBuffer.toString(), 2));
			}
		}
	}

	public static File decompressFile(File inputFile) throws IOException {
		String originalExtension;
		File decompressedFile;

		try (DataInputStream dis = new DataInputStream(new FileInputStream(inputFile))) {
			// Step 1: Read the header (original file extension)
			originalExtension = dis.readUTF(); // Read original file extension
			System.out.println("DEBUG: Original Extension: " + originalExtension);

			// Step 2: Read the frequency table
			int[] frequencies = new int[ASCII_SIZE];
			for (int i = 0; i < ASCII_SIZE; i++) {
				frequencies[i] = dis.readInt(); // Read frequency of each character
				if (frequencies[i] > 0) {
					System.out.println("DEBUG: Character '" + (char) i + "' Frequency: " + frequencies[i]);
				}
			}

			// Step 3: Rebuild the Huffman Tree
			Node root = buildHuffmanTree(frequencies);
			System.out.println("DEBUG: Huffman Tree Built Successfully");

			// Step 4: Read the compressed binary data
			StringBuilder binaryData = new StringBuilder();
			while (dis.available() > 0) {
				int byteRead = dis.readUnsignedByte();
				binaryData.append(String.format("%8s", Integer.toBinaryString(byteRead)).replace(' ', '0'));
			}
			System.out.println("DEBUG: Binary Data Read: " + binaryData);

			// Step 5: Decode the binary data using the Huffman tree
			String decodedContent = decodeUsingTree(binaryData.toString(), root);
			System.out.println("DEBUG: Decoded Content Length: " + decodedContent.length());

			// Step 6: Write the decompressed content to a new file
			String outputFileName = inputFile.getName().replaceFirst("[.][^.]+$", "") + "." + originalExtension;
			decompressedFile = new File(inputFile.getParent(), outputFileName);
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(decompressedFile))) {
				writer.write(decodedContent);
			}

			System.out.println("Decompression completed. Decompressed file: " + decompressedFile.getAbsolutePath());
		}
		return decompressedFile;
	}

	public static String decodeUsingTree(String binaryData, Node root) {
		StringBuilder decodedData = new StringBuilder();
		Node currentNode = root;

		for (char bit : binaryData.toCharArray()) {
			currentNode = (bit == '0') ? currentNode.left : currentNode.right;

			// Debug print to track traversal
			System.out.println("DEBUG: Traversing: " + (bit == '0' ? "Left" : "Right"));

			// If we reach a leaf node, append the character and reset to root
			if (currentNode.left == null && currentNode.right == null) {
				decodedData.append(currentNode.character);
				System.out.println("DEBUG: Decoded Character: " + currentNode.character);
				currentNode = root;
			}
		}
		return decodedData.toString();
	}

	// Builds the Huffman Tree using an array-based approach
	public static Node buildHuffmanTree(int[] freq) {
	    // Priority queue to hold nodes, ordered by frequency
	    PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> a.frequency - b.frequency);

	    // Create nodes for each character with non-zero frequency and add to the priority queue
	    for (int i = 0; i < ASCII_SIZE; i++) {
	        if (freq[i] > 0) {
	            pq.offer(new Node((char) i, freq[i]));
	        }
	    }

	    // Build the Huffman Tree
	    while (pq.size() > 1) {
	        // Remove the two nodes with the smallest frequency
	        Node left = pq.poll();
	        Node right = pq.poll();

	        // Create a new internal node with their combined frequency
	        Node combined = new Node('\0', left.frequency + right.frequency);
	        combined.left = left;
	        combined.right = right;

	        // Add the new node back to the priority queue
	        pq.offer(combined);
	    }

	    // The remaining node in the queue is the root of the Huffman Tree
	    return pq.poll();
	}
	// Print the Huffman tree for debugging
	public static void printTree(Node node, String code) {
		if (node == null)
			return;

		if (node.left == null && node.right == null) {
			System.out.println("DEBUG: Character '" + node.character + "' Code: " + code);
		}

		printTree(node.left, code + "0");
		printTree(node.right, code + "1");
	}

	static class Node implements Comparable<Node> {
	    int frequency;
	    char character;
	    Node left, right;

	    Node(char character, int frequency) {
	        this.character = character;
	        this.frequency = frequency;
	        this.left = this.right = null;
	    }

	    @Override
	    public int compareTo(Node other) {
	        return Integer.compare(this.frequency, other.frequency);
	    }
	}


	// Extract file extension
	private static String getFileExtension(File file) {
		String name = file.getName();
		int lastIndex = name.lastIndexOf('.');
		return (lastIndex == -1) ? "" : name.substring(lastIndex + 1);
	}

	// Generate Huffman codes
	private static void generateHuffmanCodes(Node node, String[] codes, String code) {
		if (node == null)
			return;

		if (node.left == null && node.right == null) {
			codes[node.character] = code; // Assign code to the character
		}

		generateHuffmanCodes(node.left, codes, code + "0");
		generateHuffmanCodes(node.right, codes, code + "1");
	}

	// Calculate the compressed size
	private static long calculateCompressedSize(int[] freq, String[] huffmanCodes) {
		long size = 0;
		for (int i = 0; i < ASCII_SIZE; i++) {
			if (freq[i] > 0 && huffmanCodes[i] != null) {
				size += freq[i] * huffmanCodes[i].length();
			}
		}
		return (size + 7) / 8; // Convert bits to bytes
	}

}
