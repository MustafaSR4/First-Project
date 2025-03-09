package ProjectPhase1;

public class District {
	private DNode Front;
	private int Size=0;
	private String disName;
	private LocationList locationDlist;
	
	public District() {
		
	}
	public District(String name) {
		this.disName = name;
        this.locationDlist = new LocationList(); 
        Size++;
    }
	
	
	
	public District(String disName, LocationList locationDlist) {
		this.disName = disName;
		this.locationDlist = locationDlist;
		Size++;
	}
	public int getSize() {
		return Size;
	}
	public void setSize(int size) {
		Size = size;
	}
	public String getDisName() {
		return disName;
	}
	public void setDisName(String disName) {
		this.disName = disName;
	}
	public LocationList getLocationDlist() {
		return locationDlist;
	}
	public void setLocationDlist(LocationList locationDlist) {
		this.locationDlist = locationDlist;
	}

    
    
   
	
	
	@Override
	public String toString() {
		return "District [disName=" + disName + ", locationDlist=" + locationDlist + "]";
	}
}