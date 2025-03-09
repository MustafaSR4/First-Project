package HuffmanProject;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class HuffmanDecompressor {
	public File decompressFile(File inputFilePath, String outputFileName) throws IOException {
	    try (DataInputStream dis = new DataInputStream(new FileInputStream(inputFilePath))) {
	        // Step 1: Read the original file extension
	        String originalExtension = dis.readUTF();
	        System.out.println("Original Extension: " + originalExtension);

	        // Step 2: Read the frequency table
	        int[] frequencies = new int[HuffmanCodingApp.ASCII_SIZE];
	        for (int i = 0; i < frequencies.length; i++) {
	            frequencies[i] = dis.readInt();
	        }
	        System.out.println("Frequency Table: " + Arrays.toString(frequencies));

	        // Step 3: Rebuild the Huffman Tree
	        HuffmanTree tree = new HuffmanTree();
	        HuffmanNode root = tree.buildTree(frequencies);
	        if (root == null) {
	            throw new IOException("Huffman Tree root is null.");
	        }
	        System.out.println("Huffman Tree Built Successfully:");
	        System.out.println("---------------------------");

	        // Step 4: Read the binary data
	        byte[] byteArray = dis.readAllBytes();
	        if (byteArray.length == 0) {
	            throw new IOException("Binary data is empty. Decompression cannot proceed.");
	        }
	        System.out.println("Binary Data Byte Array Length: " + byteArray.length);

	        // Convert byte array to binary string
	        StringBuilder binaryData = toBinaryString(byteArray);
	        System.out.println("Binary Data String Length Before Trimming: " + binaryData.length());

	        // Step 5: Read padding bits
	        int paddingBits = dis.readInt();
	        System.out.println("Padding Bits: " + paddingBits);
	        if (paddingBits < 0 || paddingBits > 7) {
	            throw new IOException("Invalid padding bits value: " + paddingBits);
	        }

	        // Adjust binary data length to remove padding
	        binaryData.setLength(binaryData.length() - paddingBits);
	        System.out.println("Binary Data String Length After Trimming: " + binaryData.length());

	        // Step 6: Decode binary data using the Huffman Tree
	        String decodedContent = decodeUsingTree(binaryData.toString(), root);
	        System.out.println("Decoded Content Sample: " + decodedContent.substring(0, Math.min(100, decodedContent.length())));

	        // Step 7: Write the decompressed content to the output file
	        File outputFile = new File(outputFileName);
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
	            writer.write(decodedContent);
	        }

	        System.out.println("Decompression completed successfully. Output file: " + outputFile.getAbsolutePath());
	        return outputFile;

	    } catch (IOException e) {
	        System.err.println("IOException during decompression: " + e.getMessage());
	        e.printStackTrace();
	        throw e;
	    } catch (Exception e) {
	        System.err.println("Unexpected error during decompression: " + e.getMessage());
	        e.printStackTrace();
	        throw e;
	    }
	}







	private StringBuilder toBinaryString(byte[] byteArray) {
	    StringBuilder binaryString = new StringBuilder();
	    for (byte b : byteArray) {
	        // Convert each byte to an 8-bit binary string and append it
	        binaryString.append(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
	    }
	    return binaryString;
	}





	private String decodeUsingTree(String binaryData, HuffmanNode root) {
	    StringBuilder decodedData = new StringBuilder();
	    HuffmanNode currentNode = root;

	    for (char bit : binaryData.toCharArray()) {
	        // Traverse left for '0' and right for '1'
	        currentNode = (bit == '0') ? currentNode.left : currentNode.right;

	        // Debugging: Print current bit and node information
	        System.out.println("Processing bit: " + bit + ", Current Node: " +
	                (currentNode.left == null && currentNode.right == null ? currentNode.character : "Internal Node"));

	        // Check if we've reached a leaf node
	        if (currentNode.left == null && currentNode.right == null) {
	            decodedData.append(currentNode.character); // Append character at the leaf
	            currentNode = root; // Reset to root for the next sequence
	        }
	    }

	    return decodedData.toString();
	}


	public String getOriginalExtension(File hofFile) {
	    try (DataInputStream dis = new DataInputStream(new FileInputStream(hofFile))) {
	        // Check if the file size is large enough to contain the header
	        if (hofFile.length() < 8) {
	            throw new IOException("File is too small to contain a valid extension.");
	        }

	        // Read and return the original extension
	        String originalExtension = dis.readUTF();
	        return originalExtension;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return null; // Return null in case of error
	    }
	}


}
