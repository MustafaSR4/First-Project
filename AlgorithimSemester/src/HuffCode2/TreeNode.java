package HuffCode2;



import java.util.ArrayList;
import java.util.Arrays;

public class TreeNode implements Comparable<TreeNode> {
    // Class fields
    private Byte ByteContent; // The byte content of the node
    private int frequency; // The frequency of occurrence of the byte
    private String code = null; // The binary code representation of the byte
    private TreeNode left = null; // Left child of the node
    private TreeNode right = null; // Right child of the node
    private static int numberOfLeafNode; // Counter for the number of leaf nodes

    // Constructors
    public TreeNode(byte Data, int frequency) {
        // Constructor for creating a node with byte data and frequency
        this.ByteContent = Data;
        this.frequency = frequency;
    }

    public TreeNode(byte Data) {
        // Constructor for creating a node with byte data, frequency is 0 by default
        this.ByteContent = Data;
        this.frequency = 0;
    }

    public TreeNode(int frequency) {
        // Constructor for creating a node with frequency, byte content is null
        this.frequency = frequency;
        this.ByteContent = null;
    }

    // Getters and Setters for the fields
    public Byte getByteContent() { return ByteContent; }
    public TreeNode getLeft() { return left; }
    public TreeNode getRight() { return right; }
    public boolean hasLeft() { return left != null; }
    public boolean hasRight() { return right != null; }
    public int getFrequency() { return frequency; }
    public void increment() { frequency++; }
    public void setLeft(TreeNode left) { this.left = left; }
    public void setRight(TreeNode right) { this.right = right; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    // Method for traversing the tree and building a string representation
    public String traverse() { return traverse(this); }
    private String traverse(TreeNode curr) {
        if (curr != null)
            return traverse(curr.left) + traverse(curr.right) + curr;
        return "";
    }

    // Custom toString method
    @Override
    public String toString() {
        // If the byte content is not null, return its binary string representation
        if (ByteContent != null)
            return "0" + SceneManager.byteToBinaryString(ByteContent);
        else
            return "1"; // For non-leaf nodes, return "1"
    }

    // Method to assign binary codes to each byte in the tree
    public static void gaveCodeForEachByte(TreeNode root) {
        StringBuilder okBuilder = new StringBuilder();
        getPath(root, okBuilder);
    }
    private static void getPath(TreeNode root, StringBuilder code) {
        if (root == null)
            return;
        if (root.ByteContent == null) {
            code.append('0');
            getPath(root.left, code);
            code.deleteCharAt(code.length() - 1);
            code.append('1');
            getPath(root.right, code);
            code.deleteCharAt(code.length() - 1);
        } else {
            root.setCode(code.toString());
        }
    }

//    // Method to retrieve all leaf nodes in the tree
//    public void getLeafNodes(TreeNode[] array) {
//        numberOfLeafNode = 0;
//        addLeafNodes(this, array);
//        Arrays.sort(array);
//    }
//    private void addLeafNodes(TreeNode node, TreeNode[] array) {
//        if (node == null) {
//            return; // Base case for recursion
//        }
//        if (!node.hasLeft() && !node.hasRight()) {
//            array[numberOfLeafNode++] = node; // Add leaf node to array
//        } else {
//            addLeafNodes(node.getLeft(), array); // Traverse left subtree
//            addLeafNodes(node.getRight(), array); // Traverse right subtree
//        }
//    }

    // Implementation of the Comparable interface
    public int compareTo(TreeNode other) {
        // Comparing the unsigned byte values of the nodes
        int thisVal = this.getByteContent() & 0xFF;
        int otherVal = other.getByteContent() & 0xFF;
        return Integer.compare(thisVal, otherVal);
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    public static void assignCodes(TreeNode node) {
        assignCodes(node, "");
    }

    private static void assignCodes(TreeNode node, String code) {
        if (node == null) return;
        node.setCode(code);
        assignCodes(node.left, code + "0");
        assignCodes(node.right, code + "1");
    }

    public void getLeafNodes(TreeNode[] bytes) {
        getLeafNodes(this, bytes, 0);
    }

    private int getLeafNodes(TreeNode node, TreeNode[] bytes, int index) {
        if (node == null) return index;
        if (node.isLeaf()) {
            bytes[index++] = node;
        } else {
            index = getLeafNodes(node.left, bytes, index);
            index = getLeafNodes(node.right, bytes, index);
        }
        return index;
    }

//    public byte[] getData() {
//        // List to hold the byte data from leaf nodes
//        List<Byte> dataList = new ArrayList<>();
//
//        // Stack to traverse the tree iteratively
////        Stack<TreeNode> stack = new Stack();
//        stack.push(this); // Start with the current node (root)
//
//        while (!stack.isEmpty()) {
//            TreeNode current = stack.pop();
//
//            // If it's a leaf node, collect its data
//            if (current.isLeaf()) {
//                dataList.add(current.getData());
//            } else {
//                // Push right and left children to the stack (if present)
//                if (current.getRight() != null) {
//                    stack.push(current.getRight());
//                }
//                if (current.getLeft() != null) {
//                    stack.push(current.getLeft());
//                }
//            }
//        }
//
//        // Convert List<Byte> to byte[] and return
//        byte[] result = new byte[dataList.size()];
//        for (int i = 0; i < dataList.size(); i++) {
//            result[i] = dataList.get(i);
//        }
//
//        return result;
//    }


}