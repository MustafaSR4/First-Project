package HuffmanProject;

public class HuffmanTree {
    private HuffmanNode root;

    // Builds the Huffman Tree using MinHeap
    public HuffmanNode buildTree(int[] frequencies) {
        // Count the number of non-zero frequencies
        int nonZeroFrequencies = 0;
        for (int frequency : frequencies) {
            if (frequency > 0) nonZeroFrequencies++;
        }

        // Debug: Print frequency array
        System.out.println("Frequency Array: ");
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                System.out.println("Character: " + (char) i + " Frequency: " + frequencies[i]);
            }
        }

        if (nonZeroFrequencies == 0) {
            throw new IllegalArgumentException("No valid frequencies found to build a Huffman Tree.");
        }

        // Initialize an array of Huffman nodes
        HuffmanNode[] nodes = new HuffmanNode[nonZeroFrequencies];
        int index = 0;
        for (int i = 0; i < HuffmanCodingApp.ASCII_SIZE; i++) {
            if (frequencies[i] > 0) {
                nodes[index++] = new HuffmanNode((char) i, frequencies[i]);
            }
        }

        // Initialize the MinHeap
        MinHeap minHeap = new MinHeap(nodes.length);
        minHeap.buildMinHeap(nodes);

        // Build the Huffman Tree
        while (minHeap.getSize() > 1) {
            HuffmanNode left = minHeap.extractMin();
            HuffmanNode right = minHeap.extractMin();

            // Create a new internal node with combined frequency
            HuffmanNode combined = new HuffmanNode('\0', left.frequency + right.frequency);
            combined.left = left;
            combined.right = right;

            // Insert the combined node back into the heap
            minHeap.insert(combined);
        }

        // Extract the root of the Huffman Tree
        root = minHeap.extractMin();

        // Debug: Print the entire tree
        System.out.println("Huffman Tree Built Successfully:");
        System.out.println("---------------------------");
        printTree(root, "");

        return root;
    }

    public void printTree(HuffmanNode node, String prefix) {
        if (node == null) return;

        if (node.left == null && node.right == null) {
            System.out.println("Leaf Node: " + node.character + " Frequency: " + node.frequency + " Code: " + prefix);
        } else {
            printTree(node.left, prefix + "0");
            printTree(node.right, prefix + "1");
        }
    }


    // Generates Huffman Codes for each character
    public void generateCodes(HuffmanNode node, String code, String[] huffmanCodes) {
        if (node == null) return;

        // If the node is a leaf
        if (node.left == null && node.right == null) {
            huffmanCodes[node.character] = code;

            // Debug: Print the code for the character
            System.out.println("Character: " + node.character + " Code: " + code);
            return;
        }

        // Recursively traverse left and right
        generateCodes(node.left, code + "0", huffmanCodes);
        generateCodes(node.right, code + "1", huffmanCodes);
    }

    public HuffmanNode getRoot() {
        return root;
    }
}
