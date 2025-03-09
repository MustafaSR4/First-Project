package ProjectPhase3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AVLTree {

    private AVLTreeNode root; 

    public AVLTree() {
        this.root = null; 
    }

    // Public method to insert a node
    public void insert(String key, String district, String location, Martyr martyr) {
        root = insert(root, key, district, location, martyr);
    }

    // Private method to insert a AVLTREE nodes and it compares by  2 fields: The district and full name 
    private AVLTreeNode insert(AVLTreeNode node, String key, String district, String location, Martyr martyr) {
        if (node == null) {
            return new AVLTreeNode(key, district, location, martyr);
        }

        AVLTreeNode newNode = new AVLTreeNode(key, district, location, martyr);
        int compareResult = newNode.compareTo(node);

        if (compareResult < 0) {
            node.setLeft(insert(node.getLeft(), key, district, location, martyr));
        } else if (compareResult > 0) {
            node.setRight(insert(node.getRight(), key, district, location, martyr));
        } else {
            // Update existing node's data
            node.setDistrict(district);
            node.setLocation(location);
            node.setMartyr(martyr);
            return node;
        }

        // Update the height of this ancestor node
        updateHeight(node);

        // Balance the tree at this node
        return balance(node, newNode);
    }

    // Method to update the height of a node
    private void updateHeight(AVLTreeNode node) {
        node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);//plus one for counting the root
    }
    public int getSize() {
        return getSize(root);
    }

    private int getSize(AVLTreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + getSize(node.getLeft()) + getSize(node.getRight());
    }
    public int getHeight() {
        return getHeight(root);
    }

    private int getHeight(AVLTreeNode node) {
        if (node == null) {
            return 0;
        }
        return Math.max(getHeight(node.getLeft()), getHeight(node.getRight())) + 1;
    }
    // Method to get the height of a node
    private int height(AVLTreeNode node) {
        return (node == null) ? 0 : node.getHeight();
    }

    // Method to get the balance factor of a node
    private int getBalance(AVLTreeNode node) {
        return (node == null) ? 0 : height(node.getLeft()) - height(node.getRight());//Balance factor
    }

    // Method to balance the tree at a node
    private AVLTreeNode balance(AVLTreeNode node, AVLTreeNode newNode) {
        int balance = getBalance(node);

        // Left Left Case
        if (balance > 1 && newNode.compareTo(node.getLeft()) < 0) {
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && newNode.compareTo(node.getRight()) > 0) {
            return leftRotate(node);
        }

        // Left Right Case
        if (balance > 1 && newNode.compareTo(node.getLeft()) > 0) {
            node.setLeft(leftRotate(node.getLeft()));
            return rightRotate(node);
        }

        // Right Left Case
        if (balance < -1 && newNode.compareTo(node.getRight()) < 0) {
            node.setRight(rightRotate(node.getRight()));
            return leftRotate(node);
        }

        return node;
    }

    // Method to perform a right rotation
    private AVLTreeNode rightRotate(AVLTreeNode y) {
        AVLTreeNode x = y.getLeft();
        AVLTreeNode T2 = x.getRight();

        // Perform rotation
        x.setRight(y);
        y.setLeft(T2);

        // Update heights
        updateHeight(y);
        updateHeight(x);

        // Return new root
        return x;
    }

    // Method to perform a left rotation
    private AVLTreeNode leftRotate(AVLTreeNode x) {
        AVLTreeNode y = x.getRight();
        AVLTreeNode T2 = y.getLeft();

        // Perform rotation
        y.setLeft(x);
        x.setRight(T2);

        // Update heights
        updateHeight(x);
        updateHeight(y);

        // Return new root
        return y;
    }

    // Method to get the root node (getter and setter for the root)
    public AVLTreeNode getRoot() {
        return root;
    }

    public void setRoot(AVLTreeNode root) {
        this.root = root;
    }

    // Method to get an observable list of nodes
    public ObservableList<AVLTreeNode> getNodes() {
        ObservableList<AVLTreeNode> nodes = FXCollections.observableArrayList();
        inOrderTraversal(root, nodes);
        return nodes;
    }

    // Helper method for in-order traversal to fill the observable list
    private void inOrderTraversal(AVLTreeNode node, ObservableList<AVLTreeNode> nodes) {
        if (node != null) {
            inOrderTraversal(node.getLeft(), nodes);
            nodes.add(node);
            inOrderTraversal(node.getRight(), nodes);
        }
    }

    // Method to search a node by partial name and date
    public AVLTreeNode searchNodeByPartialNameAndDate(String partialName, String dateOfDeath) {
        return searchNodeByPartialNameAndDate(root, partialName.toLowerCase(), dateOfDeath);
    }

    // Helper method to search a node by partial name and date
    private AVLTreeNode searchNodeByPartialNameAndDate(AVLTreeNode node, String partialName, String dateOfDeath) {
        if (node == null) {
            return null;
        }
        if (node.getMartyr().getName().toLowerCase().contains(partialName) && node.getMartyr().getDateOfDeath().equals(dateOfDeath)) {
            return node;
        }
        AVLTreeNode leftResult = searchNodeByPartialNameAndDate(node.getLeft(), partialName, dateOfDeath);
        if (leftResult != null) {
            return leftResult;
        }
        return searchNodeByPartialNameAndDate(node.getRight(), partialName, dateOfDeath);
    }

    // Method to delete a node by partial name and date
    public boolean deleteNodeByPartialNameAndDate(String partialName, String dateOfDeath) {
        AVLTreeNode nodeToDelete = searchNodeByPartialNameAndDate(partialName, dateOfDeath);
        if (nodeToDelete != null) {
            root = delete(root, nodeToDelete.getKey(), nodeToDelete.getMartyr().getName());
            return true;
        }
        return false;
    }

    // Private method to delete a node and after deleting it checks for the Balance Factor
    private AVLTreeNode delete(AVLTreeNode node, String key, String name) {
        if (node == null) {
            return null;
        }

        int compareResult = key.compareTo(node.getKey());
        
        // Traverse to the left or right based on comparison with the key
        if (compareResult < 0) {
            node.setLeft(delete(node.getLeft(), key, name));
        } else if (compareResult > 0) {
            node.setRight(delete(node.getRight(), key, name));
        } else {
            // Key matches, now compare the names
            if (!node.getMartyr().getName().equalsIgnoreCase(name)) {
                // Continue searching in both subtrees since name doesn't match
                node.setLeft(delete(node.getLeft(), key, name));
                node.setRight(delete(node.getRight(), key, name));
            } else {
                // Key and name both match, perform deletion
                if (node.getLeft() == null) {
                    return node.getRight();
                } else if (node.getRight() == null) {
                    return node.getLeft();
                }
                //this case it goes to successor 
                // Node with two children: Get the inorder successor (smallest in the right subtree)
                AVLTreeNode successor = minValueNode(node.getRight());
                
                // Copy the inorder successor's data to this node
                node.setKey(successor.getKey());
                node.setDistrict(successor.getDistrict());
                node.setLocation(successor.getLocation());
                node.setMartyr(successor.getMartyr());
                
                // Delete the inorder successor
                node.setRight(delete(node.getRight(), successor.getKey(), successor.getMartyr().getName()));
            }
        }

        // Update the height of the current node
        node.setHeight(Math.max(height(node.getLeft()), height(node.getRight())) + 1);
        
        // Get the balance factor of this node to check whether it became unbalanced
        int balance = getBalance(node);

        // If the node becomes unbalanced, then there are 4 cases

        // Left Left Case
        if (balance > 1 && getBalance(node.getLeft()) >= 0) {
            return rightRotate(node);
        }

        // Left Right Case
        if (balance > 1 && getBalance(node.getLeft()) < 0) {
            node.setLeft(leftRotate(node.getLeft()));
            return rightRotate(node);
        }

        // Right Right Case
        if (balance < -1 && getBalance(node.getRight()) <= 0) {
            return leftRotate(node);
        }

        // Right Left Case
        if (balance < -1 && getBalance(node.getRight()) > 0) {
            node.setRight(rightRotate(node.getRight()));
            return leftRotate(node);
        }

        return node;
    }

    // Method to find the node with the smallest key value //to replace with it the deleting one
    private AVLTreeNode minValueNode(AVLTreeNode node) {
        AVLTreeNode current = node;
        while (current.getLeft() != null) {
            current = current.getLeft();
        }
        return current;
    }

    // Method to get the total number of martyrs in the tree
    public int getTotalMartyrs() {
        int totalMartyrs = countMartyrs(root);
        return totalMartyrs;
    }

    //  Method to count the number of martyrs in the tree
    private int countMartyrs(AVLTreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + countMartyrs(node.getLeft()) + countMartyrs(node.getRight()); //plus one to count the martyr root
    }

    // Method to get the average age of martyrs in the tree
    public double getAverageAge() {
        int[] ageData = calculateTotalAgeAndCount(root);
        if (ageData[1] == 0) {
            return 0.0;
        }
        double averageAge = (double) ageData[0] / ageData[1];
        return averageAge;
    }

    // Private method to calculate the total age and count of martyrs in the tree
    private int[] calculateTotalAgeAndCount(AVLTreeNode node) {
        if (node == null) {
            return new int[]{0, 0};
        }
        int[] leftData = calculateTotalAgeAndCount(node.getLeft());
        int[] rightData = calculateTotalAgeAndCount(node.getRight());
        int totalAge = leftData[0] + rightData[0] + node.getMartyr().getAge();
        int count = leftData[1] + rightData[1] + 1;
        return new int[]{totalAge, count};
    }

    // Method to get the district with the maximum number of martyrs
    public String getMaxMartyrsDistrict() {
        if (root == null) {
            return null;
        }
        String[] maxDistrict = new String[1];
        int[] maxCount = new int[1];
        findMaxMartyrsDistrict(root, maxDistrict, maxCount);
        return maxDistrict[0];
    }

    // method to find the district with the maximum number of martyrs
    private void findMaxMartyrsDistrict(AVLTreeNode node, String[] maxDistrict, int[] maxCount) {
        if (node == null) {
            return;
        }
        int currentCount = countMartyrsInDistrict(root, node.getDistrict());
        if (currentCount > maxCount[0]) {
            maxCount[0] = currentCount;
            maxDistrict[0] = node.getDistrict();
        }
        findMaxMartyrsDistrict(node.getLeft(), maxDistrict, maxCount);
        findMaxMartyrsDistrict(node.getRight(), maxDistrict, maxCount);
    }

    // Private method to count the number of martyrs in a specific district
    private int countMartyrsInDistrict(AVLTreeNode node, String district) {
        if (node == null) {
            return 0;
        }
        int count = district.equals(node.getDistrict()) ? 1 : 0;
        count += countMartyrsInDistrict(node.getLeft(), district);
        count += countMartyrsInDistrict(node.getRight(), district);
        return count;
    }

    // Method to get the location with the maximum number of martyrs
    public String getMaxMartyrsLocation() {
        if (root == null) {
            return null;
        }
        String[] maxLocation = new String[1];
        int[] maxCount = new int[1];
        findMaxMartyrsLocation(root, maxLocation, maxCount);
        return maxLocation[0];
    }

    // Private method to find the location with the maximum number of martyrs
    private void findMaxMartyrsLocation(AVLTreeNode node, String[] maxLocation, int[] maxCount) {
        if (node == null) {
            return;
        }
        String location = node.getLocation();
        int currentCount = countMartyrsInLocation(root, location);
        if (currentCount > maxCount[0]) {
            maxCount[0] = currentCount;
            maxLocation[0] = location;
        }
        findMaxMartyrsLocation(node.getLeft(), maxLocation, maxCount);
        findMaxMartyrsLocation(node.getRight(), maxLocation, maxCount);
    }

    // Private method to count the number of martyrs in a specific location
    private int countMartyrsInLocation(AVLTreeNode node, String location) {
        if (node == null) {
            return 0;
        }
        int count = location.equals(node.getLocation()) ? 1 : 0;
        count += countMartyrsInLocation(node.getLeft(), location);
        count += countMartyrsInLocation(node.getRight(), location);
        return count;
    }

 // Method to print the tree level by level
    public String printLevelByLevel() {
        StringBuilder sb = new StringBuilder(); // StringBuilder to accumulate the output
        LinkedListQueue queue = new LinkedListQueue(); // Queue to facilitate level order traversal
        if (root != null) { // Check if the root is not null
            queue.enqueue(root); // Enqueue the root node
        }
        while (!queue.isEmpty()) { 
            int levelSize = queue.size(); // Get the number of nodes at the current level
            String[] levelNodes = new String[levelSize]; // Array to hold the nodes at the current level
            for (int i = 0; i < levelSize; i++) { // Iterate over all nodes at the current level
                AVLTreeNode currentNode = (AVLTreeNode) queue.dequeue(); // Dequeue a node
                levelNodes[i] = currentNode.getMartyr().getName() + " (Age: " + currentNode.getMartyr().getAge() + ")"; // Get the martyr's name and age
                if (currentNode.getRight() != null) { // Check if the right child exists
                    queue.enqueue(currentNode.getRight()); // Enqueue the right child
                }
                if (currentNode.getLeft() != null) { // Check if the left child exists
                    queue.enqueue(currentNode.getLeft()); // Enqueue the left child
                }
            }
            sb.append(String.join(", ", levelNodes)).append("\n"); // Append the current level's nodes to the output
        }
        return sb.toString(); // Return the  output
    }


    // Method to get the number of nodes in the tree
    public int countNodes() {
        return countNodes(root);
    }

    // Private method to count the number of nodes in the tree
    private int countNodes(AVLTreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + countNodes(node.getLeft()) + countNodes(node.getRight());
    }

    // Method to check if the tree is empty
    public boolean isEmpty() {
        return root == null;
    }

    

    // Method to get martyrs as an array
    public Martyr[] getMartyrs() {
        int size = getNodeCount(root);
        Martyr[] martyrsArray = new Martyr[size];
        fillMartyrsArray(root, martyrsArray, new int[]{0});
        return martyrsArray;
    }

    // Helper method to fill the martyrs array using in-order traversal
    private void fillMartyrsArray(AVLTreeNode node, Martyr[] martyrsArray, int[] index) {
        if (node != null) {
            fillMartyrsArray(node.getLeft(), martyrsArray, index);
            martyrsArray[index[0]] = node.getMartyr();
            index[0]++;
            fillMartyrsArray(node.getRight(), martyrsArray, index);
        }
    }

    // Private method to count the number of nodes in the tree
    private int getNodeCount(AVLTreeNode node) {
        if (node == null) {
            return 0;
        }
        return 1 + getNodeCount(node.getLeft()) + getNodeCount(node.getRight());
    }
    @Override
    public String toString() {
        return "AVLTree [root=" + root + "]";
    }
}
