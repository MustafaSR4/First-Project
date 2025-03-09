package ProjectPhase2;


public class DistrictNode {
	String districtName;
	DistrictNode left, right;
    LocationTree locationTree;
   
    
   
	public DistrictNode(String districtName) {
	
		this.districtName = districtName;
		this.locationTree = new LocationTree();
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}

	public DistrictNode getLeft() {
		return left;
	}

	public void setLeft(DistrictNode left) {
		this.left = left;
	}

	public DistrictNode getRight() {
		return right;
	}

	public void setRight(DistrictNode right) {
		this.right = right;
	}

	public LocationTree getLocationTree() {
		return locationTree;
	}

	public void setLocationTree(LocationTree locationTree) {
		this.locationTree = locationTree;
	}

	public LocationNode insertOrFindLocation(String locationName, StringBuilder path) {
	    if (locationTree.getRoot() == null) {
	        locationTree.setRoot(new LocationNode(locationName));
	        path.append("Inserted new root location: ").append(locationName).append("\n");
	        return locationTree.getRoot();
	    } else {
	        return insertOrFindLocationRecursive(locationTree.getRoot(), locationName, path);
	    }
	}

	private LocationNode insertOrFindLocationRecursive(LocationNode currentNode, String locationName, StringBuilder path) {
	    if (locationName.compareTo(currentNode.getLocationName()) < 0) {
	        if (currentNode.getLeft() == null) {
	            currentNode.setLeft(new LocationNode(locationName));
	            path.append("Inserted location: ").append(locationName).append("\n");
	            return currentNode.getLeft();
	        } else {
	            return insertOrFindLocationRecursive(currentNode.getLeft(), locationName, path);
	        }
	    } else if (locationName.compareTo(currentNode.getLocationName()) > 0) {
	        if (currentNode.getRight() == null) {
	            currentNode.setRight(new LocationNode(locationName));
	            path.append("Inserted location: ").append(locationName).append("\n");
	            return currentNode.getRight();
	        } else {
	            return insertOrFindLocationRecursive(currentNode.getRight(), locationName, path);
	        }
	    } else {
	        path.append("Location found: ").append(locationName).append("\n");
	        return currentNode;
	    }
	}


    
}
