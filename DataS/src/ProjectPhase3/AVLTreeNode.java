package ProjectPhase3;

public class AVLTreeNode {
    private String key; // The key (date) for the AVL tree node
    private int height; // The height of the node for balancing purposes
    private String district;
    private String location; 
    private Martyr martyr; 
    private AVLTreeNode left; // Left child of the node
    private AVLTreeNode right; // Right child of the node

    public AVLTreeNode(String key, String district, String location, Martyr martyr) {
        this.key = key;
        this.height = 1; 
        this.district = district;
        this.location = location;
        this.martyr = martyr;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Martyr getMartyr() {
        return martyr;
    }

    public void setMartyr(Martyr martyr) {
        this.martyr = martyr;
    }

    public AVLTreeNode getLeft() {
        return left;
    }

    public void setLeft(AVLTreeNode left) {
        this.left = left;
    }

    public AVLTreeNode getRight() {
        return right;
    }

    public void setRight(AVLTreeNode right) {
        this.right = right;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    // Method to compare this node with another node based on district and martyr's name
    public int compareTo(AVLTreeNode o) {
        int districtComparison = this.district.compareTo(o.district);
        if (districtComparison != 0) {
            return districtComparison;
        } else {
            return this.martyr.getName().compareTo(o.getMartyr().getName());//if the districts are equal it compares the name of the martyrs
        }
    }

    @Override
    public String toString() {
        return "AVLTreeNode [key=" + key + ", height=" + height + ", district=" + district + ", location=" + location
                + ", martyr=" + martyr + ", left=" + left + ", right=" + right + "]";
    }
}
