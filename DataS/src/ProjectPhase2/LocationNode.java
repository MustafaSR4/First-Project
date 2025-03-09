package ProjectPhase2;


// Basic binary tree node class for LocationTree
public class LocationNode {
	
	   String locationName;  
	   LocationNode left, right;
	   MartyrDateTree martyrdatetree;
	   
	   
	public LocationNode(String locationName) {
		this.locationName = locationName;
		this.martyrdatetree = new MartyrDateTree();
	}
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public LocationNode getLeft() {
		return left;
	}
	public void setLeft(LocationNode left) {
		this.left = left;
	}
	public LocationNode getRight() {
		return right;
	}
	public void setRight(LocationNode right) {
		this.right = right;
	}
	public MartyrDateTree getMartyrdatetree() {
		return martyrdatetree;
	}
	public void setMartyrdatetree(MartyrDateTree martyrdatetree) {
		this.martyrdatetree = martyrdatetree;
	}
	

	

	

	
}
