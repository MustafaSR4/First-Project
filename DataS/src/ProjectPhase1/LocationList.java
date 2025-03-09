package ProjectPhase1;

public class LocationList {
    private SNode Front, Back;
    private int Size;
    private String locationName; // The name of this specific location
    private SNode martyrFront; // Head of the martyrs' linked list


    public LocationList(String locationName) {
        this.locationName = locationName;
        this.martyrFront = null;    
        }

    public LocationList() {
        Front = Back = null;
        Size = 0;
    }

    public SNode getFront() {
        return Front;
    }

    public void setFront(SNode front) {
        Front = front;
    }

    public SNode getBack() {
        return Back;
    }

    public void setBack(SNode back) {
        Back = back;
    }

    public int getSize() {
        return Size;
    }

    public void setSize(int size) {
        Size = size;
    }

    

    public boolean isEmpty() {
        return Front == null;
    }
    public String getName() {
        return this.locationName;
    }
    public SNode getMartyrsFront() {
        return this.martyrFront;
    }

    public SNode insertNewLocation(String locationName) {
        // Check if the location already exists
        SNode existingLocationNode = findLocationNode(locationName);
        if (existingLocationNode == null) {
            // Create a new Location object
            Location newLocation = new Location(locationName);
            SNode newLocationNode = new SNode(newLocation);

            if (getFront() == null) {
                // If the list is empty, insert at first
                setFront(newLocationNode);
                setBack(newLocationNode); // Adjust for singly linked list if necessary
            } else {
                // Find the correct position to insert
                SNode current = getFront();
                SNode previous = null;
                while (current != null && ((Location)current.getElement()).getLocName().compareToIgnoreCase(locationName) < 0) {
                    previous = current;
                    current = current.getNext();
                }

                if (previous == null) {
                    // Inserting at the front 
                    newLocationNode.setNext(getFront());
                    setFront(newLocationNode);
                } else {
                    // Inserting in the middle or at the back
                    newLocationNode.setNext(current);
                    if (previous != null) { 
                        previous.setNext(newLocationNode);
                    }
                }

                if (current == null) { // If inserting at the end
                    setBack(newLocationNode);
                }
            }
            setSize(getSize() + 1); 
            return newLocationNode;
        } else {
            System.out.println("Location '" + locationName + "' already exists.");
            return existingLocationNode;
        }
       
    }
    public void printAllLocations() {
        System.out.println("All Locations:");
        SNode current = getFront();
        while (current != null) {
            Location location = (Location) current.getElement();
            System.out.println(location.getLocName());
            current = current.getNext();
        }
    }


    
    
    
    


    public boolean updateLocationName(String oldLocationName, String newLocationName) {
        SNode current = Front;
        SNode previous = null;
        boolean found = false;

        while (current != null) {
            Location location = (Location)current.getElement();
            if (location.getLocName().equalsIgnoreCase(oldLocationName)) {
                found = true;  // Mark that we found the location
                location.setLocName(newLocationName);
                							
                if (previous == null) {   // Remove the node from its current position
                    Front = current.getNext();  // Removing the first node
                } else {
                    previous.setNext(current.getNext());
                }
                break;
            }
            previous = current;
            current = current.getNext();
        }

        // If found, reinsert the node in the correct order
        if (found) {
            // Reinsert the location in sorted order
            SNode newNode = new SNode((Location)current.getElement(), null);
            if (Front == null || ((Location) Front.getElement()).getLocName().compareTo(newLocationName) > 0) {
                newNode.setNext(Front);
                Front = newNode;
            } else {
                SNode scan = Front;
                while (scan.getNext() != null && ((Location) scan.getNext().getElement()).getLocName().compareTo(newLocationName) <= 0) {
                    scan = scan.getNext();
                }
                newNode.setNext(scan.getNext());
                scan.setNext(newNode);
            }
            System.out.println("Location '" + oldLocationName + "' updated to '" + newLocationName + "'.");
            return true;
        } else {
            System.out.println("Location '" + oldLocationName + "' not found.");
            return false;
        }
    }




    public boolean deleteLocation(String locationName) {
        if (Front == null) {
            System.out.println("Location list is empty.");
            return false;
        }

        SNode current = Front;
        SNode previous = null;
        
        while (current != null) {
            Location location = (Location)current.getElement();
            if (location.getLocName().equalsIgnoreCase(locationName)) {
                if (previous == null) {
                    Front = current.getNext(); // Remove the first element
                } else {
                    previous.setNext(current.getNext()); // Remove middle or last element
                }
                System.out.println("Location '" + locationName + "' deleted successfully.");
                return true;
            }
            previous = current;
            current = current.getNext();
        }

        System.out.println("Location '" + locationName + "' not found.");
        return false;
    }

    public boolean deleteLocation(DistrictList districtList, String districtName, String locationName) {
        District district = districtList.findOrAddDistrict(districtName);
        if (district != null) {
            return district.getLocationDlist().deleteLocation(locationName);
        }
        return false;
    }



    
    
    
    




    public SNode findLocationNode(String locationName) {
        SNode current = Front;
        while (current != null) {
          
            if (current.getElement() instanceof Location) {
                Location locationList = (Location) current.getElement();
                if (locationList.getLocName().equalsIgnoreCase(locationName.trim())) {
                    return current;
                }
            }
            current = current.getNext();
        }
        return null;
    }




    

   

    public void addMartyrSorted(Martyr martyr) {
        SNode newNode = new SNode(martyr);
        if (martyrFront == null || ((Martyr)martyrFront.getElement()).getAge() >= martyr.getAge()) {
            newNode.setNext(martyrFront);
            martyrFront = newNode;
        } else {
            SNode current = martyrFront;
            while (current.getNext() != null && ((Martyr)current.getNext().getElement()).getAge() < martyr.getAge()) {
                current = current.getNext();
            }
            newNode.setNext(current.getNext());
            current.setNext(newNode);
        }
    }

    public Location findLocationByName(String locationName) {
        SNode current = Front;
        while (current != null) {
            Location location = (Location)current.getElement();
            if (location.getLocName().equalsIgnoreCase(locationName)) {
                return location; // Found matching location
            }
            current = current.getNext();
        }
        return null; 
    }

   

   
    public Location findOrAddLocation(String locationName) {
        SNode current = Front;

        while (current != null) {
            Location currentLocation = (Location)current.getElement();
            if (currentLocation.getLocName().equalsIgnoreCase(locationName)) {
                return currentLocation;
            }
            current = current.getNext();
        }

        Location newLocation = new Location(locationName);
        addLocation(newLocation); // Now adds in alphabetical order
        return newLocation;
    }

    

    private void addLocation(Location location) {
        SNode newNode = new SNode(location, null);
        if (Front == null || ((Location) Front.getElement()).getLocName().compareTo(location.getLocName()) > 0) {
        	//at first
        	newNode.setNext(Front);
            Front = newNode;
        } else {
            SNode current = Front;
            while (current.getNext() != null && ((Location) current.getNext().getElement()).getLocName().compareTo(location.getLocName()) < 0) {
                current = current.getNext();
            }
            newNode.setNext(current.getNext());
            current.setNext(newNode);
        }
    }
    public String printMartyrList() {
		return "MartyrList [Front=" + martyrFront ;
	}

	

	public boolean deleteMartyrByName(String martyrName) {
	    SNode current = this.Front;
	    SNode previous = null;
	    while (current != null) {
	        if (((Martyr) current.getElement()).getName().equalsIgnoreCase(martyrName)) {
	            if (previous == null) {
	                this.Front = current.getNext(); // If it's the first element.
	            } else {
	                previous.setNext(current.getNext()); // Bypass the current node.
	            }
	            return true;
	        }
	        previous = current;
	        current = current.getNext();
	    }
	    return false;
	}

	public boolean deleteMartyr(String martyrName) {
	    SNode current = Front;
	    SNode previous = null;
	    while (current != null) {
	        if (((Martyr) current.getElement()).getName().equalsIgnoreCase(martyrName)) {
	            if (previous == null) {
	                Front = current.getNext();
	            } else {
	                previous.setNext(current.getNext());
	            }
	            return true; // Successful deletion
	        }
	        previous = current;
	        current = current.getNext();
	    }
	    return false; // Martyr not found
	}


	public boolean updateMartyrName(String oldName, String newName) {
	    SNode current = martyrFront;
	    SNode previous = null;
	    Martyr foundMartyr = null;

	    oldName = oldName.toLowerCase();

	    while (current != null) {
	        Martyr martyr = (Martyr) current.getElement();
	        if (martyr.getName().toLowerCase().equals(oldName)) {
	            foundMartyr = martyr;
	            break;
	        }
	        previous = current;
	        current = current.getNext();
	    }

	    if (foundMartyr == null) {
	        System.out.println("Martyr not found: " + oldName);
	        return false; // Martyr not found, exit method.
	    }

	    // Update the martyr's name.
	    foundMartyr.setName(newName);

	    if (previous == null) {
	        martyrFront = current.getNext(); 
	    } else {
	        previous.setNext(current.getNext()); 
	    }

	    // Reinsert the martyr 
	    insertMartyrSorted(foundMartyr);
	    System.out.println("Martyr name updated from " + oldName + " to " + newName);
	    return true;
	}

	private void insertMartyrSorted(Martyr martyr) {
	    SNode newNode = new SNode(martyr, null);
	    if (martyrFront == null || ((Martyr) martyrFront.getElement()).getName().compareToIgnoreCase(martyr.getName()) > 0) {
	        newNode.setNext(martyrFront);
	        martyrFront = newNode;
	    } else {
	        SNode current = martyrFront;
	        while (current.getNext() != null && ((Martyr) current.getNext().getElement()).getName().compareToIgnoreCase(martyr.getName()) <= 0) {
	            current = current.getNext();
	        }
	        newNode.setNext(current.getNext());
	        current.setNext(newNode);
	    }
	}


	public Martyr findMartyrByName(String name) {
	    SNode current = Front;
	    name = name.trim().toLowerCase(); // Normalize input for comparison
	    while (current != null) {
	        Martyr martyr = (Martyr) current.getElement();
	        if (martyr.getName().trim().toLowerCase().equals(name)) {
	            return martyr; // Martyr found
	        }
	        current = current.getNext();
	    }
	    return null; // Martyr not found
	}

	 // Method to delete a martyr by name
	public boolean deleteMartyrRecordByName(String martyrName) {
	    SNode current = martyrFront;
	    SNode previous = null;

	    while (current != null) {
	        Martyr martyr = (Martyr) current.getElement();
	        System.out.println("Comparing " + martyr.getName() + " with " + martyrName);
	        if (martyr.getName().equalsIgnoreCase(martyrName)) {
	            if (previous == null) {
	            	martyrFront = current.getNext(); // Remove the first element if matched
	                System.out.println("Deleting first element: " + martyr.getName());
	            } else {
	                previous.setNext(current.getNext()); // Bypass the current node
	                System.out.println("Deleting " + martyr.getName());
	            }
	            return true; 
	        }
	        previous = current;
	        current = current.getNext();
	    }

	    System.out.println("Martyr not found: " + martyrName);
	    return false; // Martyr not found
	}




	@Override
	public String toString() {
		return "LocationList [Front=" + Front + ", Back=" + Back + ", Size=" + Size + ", locationName=" + locationName
				+ ", martyrFront=" + martyrFront + "]";
	}


	
	



    
	

    
    
    

}
