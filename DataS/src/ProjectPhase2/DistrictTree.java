package ProjectPhase2;

import java.util.ArrayList;



public class DistrictTree {
    private  DistrictNode root;
    

    public DistrictTree() {
    	
	}
    
	public DistrictTree(DistrictNode root) {
		this.root = root;
	}

	public DistrictNode getRoot() {
		return root;
	}

	public void setRoot(DistrictNode root) {
		this.root = root;
	}
	
	public void insert(String districtName, StringBuilder path) {
	    if (root == null) {
	        root = new DistrictNode((districtName)); // Create root if it doesn't exist
	        path.append("Root Created -> Inserted District '").append(districtName).append("'\n");
	    } else {
	        root = insertDistrictRec(root, districtName, path);
	    }
	}

	

	private DistrictNode insertDistrict(String districtName, StringBuilder path) {
	    if (root == null) {
	        root = new DistrictNode((districtName));
	        path.append("Root Created -> Inserted District '").append(districtName).append("'\n");
	        return root;
	    } else {
	        return insertDistrictRec(root, districtName, path);
	    }
	}

//	private DistrictNode insertDistrictRec(DistrictNode node, String districtName, StringBuilder path) {
//	    if (node == null) {
//	        path.append("Inserted District '").append(districtName).append("'\n");
//	        return new DistrictNode(new DistrictPH2(districtName));
//	    }
//	    int compareResult = districtName.compareToIgnoreCase(node.getDistrict().getDistrictName());
//	    if (compareResult < 0) {
//	        path.append("Go Left -> ");
//	        if (node.getLeft() == null) {
//	            node.setLeft(new DistrictNode(new DistrictPH2(districtName)));
//	            path.append("Inserted '").append(districtName).append("' on Left\n");
//	            return node.getLeft();
//	        }
//	        return insertDistrictRec(node.getLeft(), districtName, path);
//	    } else if (compareResult > 0) {
//	        path.append("Go Right -> ");
//	        if (node.getRight() == null) {
//	            node.setRight(new DistrictNode(new DistrictPH2(districtName)));
//	            path.append("Inserted '").append(districtName).append("' on Right\n");
//	            return node.getRight();
//	        }
//	        return insertDistrictRec(node.getRight(), districtName, path);
//	    } else {
//	        path.append("District '").append(districtName).append("' already exists.\n");
//	        return node;
//	    }
//	}


	public void insert(String districtName, String locationName, String dateOfDeath, MartyrPH2 martyr, StringBuilder path) {
	    // Insert or find the district
	    DistrictNode districtNode = insertOrFindDistrict(districtName, path);
	    if (districtNode == null) {
	        path.append("Failed to insert or find district: ").append(districtName).append("\n");
	        return;
	    } else {
	    }

	    // Insert or find the location
	    LocationNode locationNode = districtNode.getLocationTree().insertOrFindLocation(locationName, path);
	    if (locationNode == null) {
	        path.append("Failed to insert or find location: ").append(locationName).append("\n");
	        System.out.println("DEBUG: Failed to insert or find location: " + locationName);
	        return;
	    } else {
	        path.append("Location found or inserted: ").append(locationName).append("\n");
	    }

	    // Insert date and martyr
	    boolean inserted = locationNode.getMartyrdatetree().insertMartyr(dateOfDeath, martyr, path);
	    if (!inserted) {
	        path.append("Failed to insert martyr: ").append(martyr.getName()).append(" on date: ").append(dateOfDeath).append("\n");
	    } else {
	        path.append("Martyr ").append(martyr.getName()).append(" added on ").append(dateOfDeath).append("\n");
	    }
	}




	private DistrictNode insertOrFindDistrict(String districtName, StringBuilder path) {
	    if (root == null) {
	        path.append("Root district created: ").append(districtName).append("\n");
	        root = new DistrictNode((districtName));
	        return root;
	    }
	    return insertDistrictRec(root, districtName, path);
	}

	private DistrictNode insertDistrictRec(DistrictNode node, String districtName, StringBuilder path) {
	    if (node == null) {
	        path.append("Inserted district: ").append(districtName).append("\n");
	        return new DistrictNode((districtName));
	    }
	    int comp = districtName.compareTo(node.getDistrictName());
	    if (comp == 0) {
	        path.append("District found: ").append(districtName).append("\n");
	        return node;
	    } else if (comp < 0) {
	        node.setLeft(insertDistrictRec(node.getLeft(), districtName, path));
	    } else {
	        node.setRight(insertDistrictRec(node.getRight(), districtName, path));
	    }
	    return node;
	}


	







	    public boolean updateDistrictName(String oldName, String newName) {
	        DistrictNode node = findDistrictNode(root, oldName);
	        if (node != null) {
	        	node.setDistrictName(newName);
	            return true;
	        }
	        return false;
	    }

	    private DistrictNode findDistrictNode(DistrictNode current, String districtName) {
	        if (current == null) return null;
	        int cmp = districtName.compareTo(current.getDistrictName());
	        if (cmp < 0) return findDistrictNode(current.getLeft(), districtName);
	        else if (cmp > 0) return findDistrictNode(current.getRight(), districtName);
	        else return current;
	    }

 // Method to print all districts with their locations and martyrs
    public String printDistrictsWithLists() {
        if (root == null) {
            System.out.println("No districts available.");
            return null;
        }
        printDistrictsRecursively(root);
		return null;
    }

    // Recursive helper method to traverse the DistrictTree and print details
    private void printDistrictsRecursively(DistrictNode node) {
        if (node != null) {
            // Print district details
            System.out.println("\nDistrict: " + node.getDistrictName());
            printLocations(node.getLocationTree().getRoot());

            // Recurse on the left subtree
            printDistrictsRecursively(node.left);
            // Recurse on the right subtree
            printDistrictsRecursively(node.right);
        }
    }

    // Method to print all locations within a district
    private void printLocations(LocationNode node) {
        if (node == null) {
            System.out.println("\tNo locations available");
        } else {
            System.out.println("\t-->Location: " + node.getLocationName());
           // printMartyrs(node.location.getMartyrDateTree().getRoot());

            // Recurse to print all locations
            printLocations(node.left);
            printLocations(node.right);
        }
    }

//    // Method to print all martyrs in a location
// // Method to print all martyrs in a location
//    private void printMartyrs(DateNode node) {
//        if (node == null) {
//            System.out.println("\t\tNo martyrs listed");
//        } else {
//            System.out.print("\t\tDate: " + node.date + " -> ");
//            for (MartyrPH2 martyr : node.martyrs) {  // Assuming martyrs is an iterable collection
//                System.out.print("Martyr: " + martyr.getName() + ", Age: " + martyr.getAge() + "; ");
//            }
//            System.out.println();
//
//            // Recurse to print all martyrs
//            printMartyrs(node.left);
//            printMartyrs(node.right);
//        }
//    }


    public boolean deleteDistrict(String districtName) {
        if (root == null) return false; // No nodes to delete
        
        if (root.getDistrictName().equals(districtName)) {
            // Special case: the root is the node to delete
            root = replaceWithChild(root);
            return true;
        }

        return deleteDistrictRec(root, districtName);
    }

    private boolean deleteDistrictRec(DistrictNode parent, String districtName) {
        DistrictNode current = null;
        boolean isLeftChild = true;

        if (parent.getLeft() != null && parent.getLeft().getDistrictName().equals(districtName)) {
            current = parent.getLeft();
            isLeftChild = true;
        } else if (parent.getRight() != null && parent.getRight().getDistrictName().equals(districtName)) {
            current = parent.getRight();
            isLeftChild = false;
        }

        if (current != null) {
            DistrictNode replacement = replaceWithChild(current);
            if (isLeftChild) parent.setLeft(replacement);
            else parent.setRight(replacement);
            return true;
        }

        // Recur down the tree
        if (parent.getLeft() != null && deleteDistrictRec(parent.getLeft(), districtName)) return true;
        if (parent.getRight() != null && deleteDistrictRec(parent.getRight(), districtName)) return true;

        return false;
    }

    private DistrictNode replaceWithChild(DistrictNode node) {
        if (node.getRight() != null) return node.getRight();
        if (node.getLeft() != null) return node.getLeft();
        return null; // No children, node effectively deleted
    }

 // Method to gather all district names
    public ArrayList<String> getAllDistrictNames() {
    	ArrayList<String> names = new ArrayList<>();
        collectDistrictNames(root, names);
        return names;
    }

    // Helper recursive method to traverse the tree and collect names
    private void collectDistrictNames(DistrictNode node, ArrayList<String> names) {
        if (node != null) {
            collectDistrictNames(node.getLeft(), names); // Visit left child
            names.add(node.getDistrictName()); // Visit node itself
            collectDistrictNames(node.getRight(), names); // Visit right child
        }
    }

//    public boolean insertLocation(String districtName, String locationName) {
//        DistrictNode districtNode = findDistrict(districtName);
//        if (districtNode == null) {
//            System.out.println("Insertion failed: District '" + districtName + "' not found.");
//            return false;
//        }
//        boolean inserted = districtNode.getDistrict().getLocationTree().insert(locationName);
//        if (!inserted) {
//            System.out.println("Insertion failed: Location '" + locationName + "' already exists or other error.");
//        }
//        return inserted;
//    }

    public DistrictNode findDistrict(String districtName) {
        return findDistrictRec(root, districtName);
    }

    private DistrictNode findDistrictRec(DistrictNode node, String districtName) {
        if (node == null) {
            System.out.println("Reached a leaf without finding: " + districtName);
            return null; // District not found
        }
        int compareResult = districtName.compareTo(node.getDistrictName());
        if (compareResult == 0) {
            return node; // District found
        } else if (compareResult < 0) {
            return findDistrictRec(node.getLeft(), districtName); // Continue search on the left subtree
        } else {
            return findDistrictRec(node.getRight(), districtName); // Continue search on the right subtree
        }
    }


    public boolean deleteLocation(String selectedDistrict, String locationName) {
        DistrictNode districtNode = findDistrict(selectedDistrict);
        if (districtNode != null) {
            // Assuming LocationTree has a method to delete a location by name
            return districtNode.getLocationTree().delete(locationName);
        }
        return false;
    }

    public boolean updateLocationName(String selectedDistrict, String oldLocation, String newLocation) {
        DistrictNode districtNode = findDistrictNode(selectedDistrict);
        if (districtNode != null) {
            return districtNode.getLocationTree().updateLocationName(oldLocation, newLocation);
        }
        return false; // District not found
    }

    private DistrictNode findDistrictNode(String districtName) {
        // Traverse the tree to find the node
        return findDistrictNodeRecursive(root, districtName);
    }
    private DistrictNode findDistrictNodeRecursive(DistrictNode node, String districtName) {
        if (node == null) {
            return null;
        } else if (node.getDistrictName().equals(districtName)) {
            return node;
        } else if (node.getDistrictName().compareTo(districtName) > 0) {
            return findDistrictNodeRecursive(node.getLeft(), districtName);
        } else {
            return findDistrictNodeRecursive(node.getRight(), districtName);
        }
    }
//TRAVERSAL IT SHOULD BE IN COMBO BOX JUST THE RIGHT AND LEFT NODE OF THE ROOT

    // Find method to locate a district by name
    public DistrictNode find(String districtName) {
        return findRec(root, districtName);
    }

    // Recursive helper method for finding a district node
    private DistrictNode findRec(DistrictNode node, String districtName) {
        // Base case: if the node is null, district is not found
        if (node == null) {
            return null;
        }

        // Compare the districtName with the node's district name
        int compareResult = districtName.compareTo(node.getDistrictName());

        // If the districtName matches, return the node
        if (compareResult == 0) {
            return node;
        }
        // If the districtName is less than the current node's name, search left
        else if (compareResult < 0) {
            return findRec(node.getLeft(), districtName);
        }
        // If the districtName is greater than the current node's name, search right
        else {
            return findRec(node.getRight(), districtName);
        }
    }

	

	

//    public void insertLocationAndMartyr(DistrictNode districtNode2, String districtName, String locationName, MartyrPH2 martyr, StringBuilder path) {
//        DistrictNode districtNode = findDistrictNode(root, districtName);
//        if (districtNode == null) {
//            path.append("District '").append(districtName).append("' not found.\n");
//        } else {
//            LocationTree locationTree = districtNode.getDistrict().getLocationTree();
//            boolean inserted = locationTree.insertLocation(locationName, martyr, path);
//            if (inserted) {
//                path.append("Inserted Location '").append(locationName).append("' with Martyr '").append(martyr.getName()).append("' in District '").append(districtName).append("'\n");
//            } else {
//                path.append("Failed to insert Location '").append(locationName).append("' or Martyr '").append(martyr.getName()).append("' in District '").append(districtName).append("'\n");
//            }
//        }
//    }

	

	




	



    
}

