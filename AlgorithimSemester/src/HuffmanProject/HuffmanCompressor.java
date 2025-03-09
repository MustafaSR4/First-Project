package HuffmanProject;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;


public class HuffmanCompressor {
	public void compressFile(File inputFile, File outputFile, String[] huffmanCodes, String originalExtension, int[] frequencies) throws IOException {
	    try (DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile))) {
	        // Write the original extension
	        dos.writeUTF(originalExtension);

	        // Write the frequency table
	        for (int frequency : frequencies) {
	            dos.writeInt(frequency);
	        }

	        // Encode the file contents using Huffman codes
	        StringBuilder binaryData = new StringBuilder();
	        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
	            int character;
	            while ((character = reader.read()) != -1) {
	                binaryData.append(huffmanCodes[character]);
	            }
	        }

	        // Convert binary string to byte array and write it
	        byte[] byteArray = toByteArray(binaryData.toString());
	        dos.write(byteArray);

	        // Write padding bits at the end
	        int paddingBits = 8 - (binaryData.length() % 8); // Calculate the number of padding bits needed
	        if (paddingBits == 8) paddingBits = 0; // No padding needed if divisible by 8
	        dos.writeInt(paddingBits);
	    }
	}


	private byte[] toByteArray(String binaryString) {
	    int byteArrayLength = (binaryString.length() + 7) / 8; // Calculate the number of bytes needed
	    byte[] byteArray = new byte[byteArrayLength];

	    for (int i = 0; i < binaryString.length(); i += 8) {
	        // Extract 8-bit chunks from the binary string
	        String byteChunk = binaryString.substring(i, Math.min(i + 8, binaryString.length()));
	        // Convert the chunk to a byte and store it in the array
	        byteArray[i / 8] = (byte) Integer.parseInt(byteChunk, 2);
	    }

	    return byteArray;
	}

	

	  

	
	
//	    public void compressFile(File inputFile, File outputFile, String[] codes, String fileExtension, int[] frequencies) throws IOException {
//	        try (FileInputStream fis = new FileInputStream(inputFile);
//	             DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFile))) {
//
//	            // Write the header (file extension and frequency array)
//	            dos.writeUTF(fileExtension);
//	            for (int frequency : frequencies) {
//	                dos.writeInt(frequency);
//	            }
//
//	            // Compress file content
//	            StringBuilder bitBuffer = new StringBuilder();
//	            int byteRead;
//	            while ((byteRead = fis.read()) != -1) {
//	                bitBuffer.append(codes[byteRead]);
//	                while (bitBuffer.length() >= 8) {
//	                    dos.writeByte(Integer.parseInt(bitBuffer.substring(0, 8), 2));
//	                    bitBuffer.delete(0, 8);
//	                }
//	            }
//
//	            // Write remaining bits
//	            if (bitBuffer.length() > 0) {
//	                while (bitBuffer.length() < 8) {
//	                    bitBuffer.append("0");
//	                }
//	                dos.writeByte(Integer.parseInt(bitBuffer.toString(), 2));
//	            }
//	        }
//	    }
	

}
