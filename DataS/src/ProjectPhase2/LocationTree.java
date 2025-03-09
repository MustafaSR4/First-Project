package ProjectPhase2;

import java.util.ArrayList;


public class LocationTree {
	 private LocationNode root;

	    public LocationNode getRoot() {
	        return root;
	    }

	    public void setRoot(LocationNode root) {
	        this.root = root;
	    }

	    public LocationNode insertOrFindLocation(String locationName, StringBuilder path) {
	        if (root == null) {
	            LocationNode newNode = new LocationNode(locationName);
	            root = newNode;
	            path.append("Root location created with Location '").append(locationName).append("'\n");
	            return newNode;
	        }
	        return insertOrFindLocationRec(root, locationName, path);
	    }

	    private LocationNode insertOrFindLocationRec(LocationNode node, String locationName, StringBuilder path) {
	        if (node == null) {
	            path.append("Inserted Location '").append(locationName).append("'\n");
	            return new LocationNode(locationName);
	        }

	        int compareResult = locationName.compareToIgnoreCase(node.getLocationName());
	        if (compareResult < 0) {
	            path.append("Go Left -> ");
	            node.setLeft(insertOrFindLocationRec(node.getLeft(), locationName, path));
	        } else if (compareResult > 0) {
	            path.append("Go Right -> ");
	            node.setRight(insertOrFindLocationRec(node.getRight(), locationName, path));
	        } else {
	            path.append("Location '").append(locationName).append("' already exists.\n");
	        }
	        return node;
	    }
//	public boolean insert(String locationName) {
//	    if (root == null) {
//	        root = new LocationNode(new LocationPH2(locationName));
//	        return true;
//	    } else {
//	        return insertRec(root, locationName, GUI.sb);
//	    }
//	}
//
//	private boolean insertRec(LocationNode node, String locationName, StringBuilder path) {
//	    if (locationName.compareTo(node.getLocation().getLocationName()) < 0) {
//	        if (node.getLeft() == null) {
//	            node.setLeft(new LocationNode(new LocationPH2(locationName)));
//	            path.append("Inserted '" + locationName + "' to the left of '" + node.getLocation().getLocationName() + "'\n");
//	            return true;
//	        } else {
//	            return insertRec(node.getLeft(), locationName, path);
//	        }
//	    } else if (locationName.compareTo(node.getLocation().getLocationName()) > 0) {
//	        if (node.getRight() == null) {
//	            node.setRight(new LocationNode(new LocationPH2(locationName)));
//	            path.append("Inserted '" + locationName + "' to the right of '" + node.getLocation().getLocationName() + "'\n");
//	            return true;
//	        } else {
//	            return insertRec(node.getRight(), locationName, path);
//	        }
//	    }
//	    // Skip logging for duplicate location names
//	    return false;
//	}


	    public ArrayList<String> getAllLocationNames() {
	    	ArrayList<String> names = new ArrayList<>();
	        collectLocationNames(root, names);
	        return names;
	    }

	    private void collectLocationNames(LocationNode node, ArrayList<String> names) {
	        if (node != null) {
	            collectLocationNames(node.getLeft(), names); // Traverse left subtree
	            names.add(node.getLocationName()); // Process current node
	            collectLocationNames(node.getRight(), names); // Traverse right subtree
	        }
	    }


	    public boolean delete(String locationName) {
	       
	        if (root == null) {
	            return false;
	        }
	        root = deleteRecursive(root, locationName);
			return false;
	    }


	    private LocationNode deleteRecursive(LocationNode current, String locationName) {
	        if (current == null) {
	            return null;
	        }

	        if (locationName.equals(current.getLocationName())) {
	            // Node to delete found -> Proceed with deletion
	            return deleteNode(current);
	        }
	        if (locationName.compareTo(current.getLocationName()) < 0) {
	            current.setLeft(deleteRecursive(current.getLeft(), locationName));
	        } else {
	            current.setRight(deleteRecursive(current.getRight(), locationName));
	        }
	        return current;
	    }
	    private LocationNode deleteNode(LocationNode node) {
	        // Case 1: No children
	        if (node.getLeft() == null && node.getRight() == null) {
	            return null;
	        }

	        // Case 2: Only one child
	        if (node.getRight() == null) {
	            return node.getLeft();
	        }
	        if (node.getLeft() == null) {
	            return node.getRight();
	        }

	        // Case 3: Two children
	        String smallestValue = findSmallestValue(node.getRight());
	        node.setLocationName(smallestValue);
	        node.setRight(deleteRecursive(node.getRight(), smallestValue));
	        return node;
	    }

	    private String findSmallestValue(LocationNode root) {
	        return root.getLeft() == null ? root.getLocationName() : findSmallestValue(root.getLeft());
	    }
	    public boolean updateLocationName(String oldLocation, String newLocation) {
	        LocationNode locationNode = findLocationNode(root, oldLocation);
	        if (locationNode != null) {
	            locationNode.setLocationName(newLocation);
	            return true; // Successfully updated
	        }
	        return false; // Location not found
	    }

	    private LocationNode findLocationNode(LocationNode node, String locationName) {
	        if (node == null) {
	            return null; // Base case: node not found
	        }

	        
	        int compareResult = locationName.compareToIgnoreCase(node.getLocationName());
	        if (compareResult == 0) {
	            return node; // Node found
	        } else if (compareResult < 0) {
	            return findLocationNode(node.getLeft(), locationName); // Search in the left subtree
	        } else {
	            return findLocationNode(node.getRight(), locationName); // Search in the right subtree
	        }
	    }
	    
	    
	    
	    
//	    LocationNode insertLocation(DistrictNode districtNode, String locationName, StringBuilder path) {
//	        LocationTree locationTree = districtNode.getLocationTree();
//	        return insertLocationRec(locationTree.getRoot(), locationName, path);
//	    }
////
//	    private LocationNode insertLocationRec(LocationNode node, String locationName, StringBuilder path) {
//	        if (node == null) {
//	            path.append("Inserted Location '").append(locationName).append("'\n");
//	            return new LocationNode(new LocationPH2(locationName));
//	        }
//	        int compareResult = locationName.compareToIgnoreCase(node.getLocation().getLocationName());
//	        if (compareResult < 0) {
//	            path.append("Go Left -> ");
//	            if (node.getLeft() == null) {
//	                node.setLeft(new LocationNode(new LocationPH2(locationName)));
//	                path.append("Inserted '").append(locationName).append("' on Left\n");
//	                return node.getLeft();
//	            }
//	            return insertLocationRec(node.getLeft(), locationName, path);
//	        } else if (compareResult > 0) {
//	            path.append("Go Right -> ");
//	            if (node.getRight() == null) {
//	                node.setRight(new LocationNode(new LocationPH2(locationName)));
//	                path.append("Inserted '").append(locationName).append("' on Right\n");
//	                return node.getRight();
//	            }
//	            return insertLocationRec(node.getRight(), locationName, path);
//	        } else {
//	            path.append("Location '").append(locationName).append("' already exists.\n");
//	            return node;
//	        }
//	    }
//	    public LocationNode insertOrFindLocation(String locationName, StringBuilder path) {
//	        if (root == null) {
//	            root = new LocationNode((locationName));
//	            path.append("Inserted new root location: ").append(locationName).append("\n");
//	            return root;
//	        }
//	        return insertLocationRec(root, locationName, path);
//	    }
//
//	    private LocationNode insertLocationRec(LocationNode node, String locationName, StringBuilder path) {
//	        if (node == null) {
//	            path.append("Inserted location: ").append(locationName).append("\n");
//	            return new LocationNode((locationName));
//	        }
//	        int comp = locationName.compareTo(node.getLocationName());
//	        if (comp == 0) {
//	            path.append("Location found: ").append(locationName).append("\n");
//	            return node;
//	        } else if (comp < 0) {
//	            node.setLeft(insertLocationRec(node.getLeft(), locationName, path));
//	        } else {
//	            node.setRight(insertLocationRec(node.getRight(), locationName, path));
//	        }
//	        return node;
//	    }

	    
	    
	    
	

		
		



		



   
}
