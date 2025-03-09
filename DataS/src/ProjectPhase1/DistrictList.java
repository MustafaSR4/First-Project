package ProjectPhase1;

public class DistrictList {
	private DNode Front, Back;
	private int Size ;
	private String name; // Add a name field to store the name of the district

	// Constructors, getters, and setters

	public DistrictList(String name) {
		this.name = name;
		this.Front = null;
		this.Back = null;
		this.Size = 0;
	}

	// Getter for the name field
	public String getName() {
		return name;
	}

	public void setName(String newName) {
		this.name = newName;
	}

	public DistrictList() {
		Front = Back = null;
		Size = 0;
	}

	public DNode getFront() {
		return Front;
	}

	public void setFront(DNode front) {
		Front = front;
	}

	public DNode getBack() {
		return Back;
	}

	public void setBack(DNode back) {
		Back = back;
	}

	public int getSize() {
		int count = 0;
		DNode node = Front;
		while (node != null) {
			count++;
			node = node.getNext();
		}
		return count;
	}

	public void setSize(int size) {
		Size = size;
	}


//Inserting a district method in a sorted way 
	public DNode insertNewDistrictRecord(District newDistrict) {
		DNode newNode = new DNode(newDistrict);
		if (Front == null) {
			Front = Back = newNode;
		} else {
			//At first	
			if (newDistrict.getDisName().compareToIgnoreCase(((District) Front.getElement()).getDisName()) <= 0) {
				newNode.setNext(Front);
				Front = newNode;
			}
			// At Last
			else if (newDistrict.getDisName().compareToIgnoreCase(((District) Back.getElement()).getDisName()) > 0) {
				Back.setNext(newNode);
				Back = newNode;
			}
			// In middle
			else {
				DNode current = Front;
				while (current.getNext() != null && ((District) current.getNext().getElement()).getDisName()
						.compareToIgnoreCase(newDistrict.getDisName()) < 0) {
					current = current.getNext();
				}
				newNode.setNext(current.getNext());
				current.setNext(newNode);
			}
		}
		Size++;
		return newNode;
	}

	public void updateDistrictName(String oldName, String newName) {
		DNode previous = null;
		DNode current = Front;

		while (current != null) {
			District district = (District) current.getElement();
			if (district.getDisName().equals(oldName)) {
				district.setDisName(newName);				// Found the district to update


				if (previous != null) {
					previous.setNext(current.getNext());
				} else {
					Front = current.getNext();
				}

				//re insert in sorted way
				insertSortedDistrict(district);
				return; // Exit after updating
			}
			previous = current;
			current = current.getNext();
		}

		throw new IllegalArgumentException("District '" + oldName + "' not found.");
	}

	private void insertSortedDistrict(District district) {
		DNode newNode = new DNode(district);
		if (Front == null || ((District) Front.getElement()).getDisName().compareTo(district.getDisName()) > 0) {
			newNode.setNext(Front);
			Front = newNode;
		} else {
			DNode current = Front;
			while (current.getNext() != null
					&& ((District) current.getNext().getElement()).getDisName().compareTo(district.getDisName()) <= 0) {
				current = current.getNext();
			}
			newNode.setNext(current.getNext());
			current.setNext(newNode);
		}
	}

	public boolean deleteDistrict(String districtName) {
		if (Front == null) {
			return false; // list is empty 
		}

		DNode current = Front;
		DNode previous = null;

		while (current != null) {
			District district = (District) current.getElement();
			if (district.getDisName().equalsIgnoreCase(districtName)) {
				if (previous == null) {
					Front = current.getNext(); // Removing the first element
					Size--;
				} else {
					previous.setNext(current.getNext());
					if (current.getNext() != null) {
						current.getNext().setPrev(previous); 
					}
				}
				Size--;
				return true; 
			}
			previous = current;
			current = current.getNext();
		}

		return false; // District name not found.
	}

	public DNode findDistrictNode(String districtName) {
		DNode current = this.Front; 
		while (current != null) {
			if (((District) current.getElement()).getDisName().equalsIgnoreCase(districtName)) {
				return current; // Found the district node
			}
			current = current.getNext();
		}
		return null; // District not found
	}

	
	
	public District findOrAddDistrict(String districtName) {
		for (DNode node = this.getFront(); node != null; node = node.getNext()) {
			District district = (District) node.getElement();
			if (district.getDisName().equalsIgnoreCase(districtName)) {
				return district;
			}
		}
		
		District newDistrict = new District(districtName);// If not found, create a new district and add it
		this.addDistrict(newDistrict); // implement in the DistrictList
		return newDistrict;
	}

	public void addDistrict(District newDistrict) {
		DNode newNode = new DNode(newDistrict);
		if (Front == null) {
			Front = newNode;
			Back = newNode;
			//addFirst
		} else if (newDistrict.getDisName().compareToIgnoreCase(((District) Front.getElement()).getDisName()) <= 0) {
			newNode.setNext(Front);
			Front.setPrev(newNode); 
			Front = newNode;
		} else if (newDistrict.getDisName().compareToIgnoreCase(((District) Back.getElement()).getDisName()) > 0) {
			
			newNode.setPrev(Back); // Only if it's a doubly linked list
			Back.setNext(newNode);
			Back = newNode;
		} else {
			DNode current = Front;
			while (current.getNext() != null && newDistrict.getDisName()
					.compareToIgnoreCase(((District) current.getNext().getElement()).getDisName()) > 0) {
				current = current.getNext();
			}
			// Place the new node after 'current'
			newNode.setNext(current.getNext());
			if (current.getNext() != null) {
				current.getNext().setPrev(newNode); // Only if it's a doubly linked list
			}
			newNode.setPrev(current); // Only if it's a doubly linked list
			current.setNext(newNode);
		}
		Size++;

	}

	public District findDistrictByName(String districtName) {
		DNode current = Front; 
		while (current != null) {
			District currentDistrict = (District) current.getElement();
			if (currentDistrict.getDisName().equalsIgnoreCase(districtName)) {
				return currentDistrict; // District found
			}
			current = current.getNext(); 
		}
		return null; // District not found
	}


	
	
	
	

	
	
	
	
	

	public int getTotalMartyrs(DNode current) {
		int totalMartyrs = 0;

		if(current != null) {
			District district = (District) current.getElement();
			LocationList locationlist = district.getLocationDlist();
			SNode current2 = locationlist.getFront();
			while (current2 != null) {
				Location location = (Location) current2.getElement();
				LocationList martyrList = location.getMartyrlist();
				
				SNode martyrCurr = martyrList.getMartyrsFront();
				while (martyrCurr != null) {
					if(martyrCurr.getElement() != null)
						totalMartyrs++;
					martyrCurr = martyrCurr.getNext();
				}
				current2 = current2.getNext();
			}
		}
		return totalMartyrs;
	}

	public String getMaleFemaleMartyrs(DNode current) {
		if (current == null) {
			return "No data available"; 
		}

		int maleCount = 0;
		int femaleCount = 0;

		if (current != null) {
			District district = (District) current.getElement();
			LocationList locationlist = district.getLocationDlist();
			SNode current2 = locationlist.getFront();
			while (current2 != null) {

				Location location = (Location) current2.getElement();
				LocationList martyrList = location.getMartyrlist();
				SNode current3 = martyrList.getMartyrsFront();
				while (current3 != null) {
					Martyr martyr = (Martyr) current3.getElement();
					if (martyr.getGender() == 'M') {
						maleCount++;
					} else if (martyr.getGender() == 'F') {
						femaleCount++;
					}
					current3 = current3.getNext();
				}

				current2 = current2.getNext();
			}

			
		}

		return "Male Martyrs: " + maleCount + ", Female Martyrs: " + femaleCount;
	}

	public double getAverageAge(DNode current) {
		int totalAge = 0;
		if (current == null) {
			return 0; 
		}

		if (current != null) {
			District district = (District) current.getElement();
			LocationList locationlist = district.getLocationDlist();
			SNode current2 = locationlist.getFront();
			while (current2 != null) {

				Location location = (Location) current2.getElement();
				LocationList martyrList = location.getMartyrlist();
				SNode current3 = martyrList.getMartyrsFront();
				while (current3 != null) {
					Martyr martyr = (Martyr) current3.getElement();
					totalAge += martyr.getAge();
					current3 = current3.getNext();
				}

				current2 = current2.getNext();
			}

			
		}

		return (double) totalAge / getTotalMartyrs(current);
	}

	public String getMaxMartyrsDate(DNode current) {
		if (current == null) {
			return "No data available"; // Return a message if the list is empty
		}

		String maxDate = null;
		int maxCount = 0;

		DNode currentDistrictNode = current;
		if (currentDistrictNode != null) {
			District district = (District) currentDistrictNode.getElement();
			SNode current1 = district.getLocationDlist().getFront();
			while (current1 != null) {
				Location location = (Location) current1.getElement();
				SNode currentMartyrNode = location.getMartyrlist().getMartyrsFront();

				while (currentMartyrNode != null) {
					Martyr martyr = (Martyr) currentMartyrNode.getElement();
					String date = martyr.getDateOfDeath();

					
					int count = countMartyrsForDate(date,current);

					
					if (count > maxCount) {
						maxCount = count;
						maxDate = date;
					}

					currentMartyrNode = currentMartyrNode.getNext();
				}
				current1 = current1.getNext();
			}
			
		}

		return  maxDate + " (Martyrs: " + maxCount + ")";
	}

	private int countMartyrsForDate(String date,DNode current) {
		int count = 0;
		
		if ( current!= null) {
			District district = (District) current.getElement();
			LocationList locationlist = district.getLocationDlist();
			SNode current2 = locationlist.getFront();
			while (current2 != null) {

				Location location = (Location) current2.getElement();
				LocationList martyrList = location.getMartyrlist();
				SNode current3 = martyrList.getMartyrsFront();
				while (current3 != null) {
					Martyr martyr = (Martyr) current3.getElement();
					if (martyr.getDateOfDeath().equals(date)) {
						count++;
					}
					current3 = current3.getNext();
				}

				current2 = current2.getNext();
			}

		
		}
		return count;
	}



	
}
