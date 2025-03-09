package ProjectPhase1;

public class Location {
	
		private String locName;
		private LocationList martyrList;
	   

		
		public Location() {
			
		}
		public Location(String name) {
			this.locName=name;
			this.martyrList=new LocationList();
	       

		}
		
		
		public Location(String disName, LocationList locationDlist) {
			this.locName = disName;
			this.martyrList = locationDlist;
		}
		public String getLocName() {
			return locName;
		}
		public void setLocName(String disName) {
			this.locName = disName;
		}
		public LocationList getMartyrlist() {
			return martyrList;
		}
		public void setMartyrlist(LocationList locationDlist) {
			this.martyrList = locationDlist;
		
	}


		@Override
		public String toString() {
			return "District [disName=" + locName + ", locationDlist=" + martyrList + "]";
		}
	}


