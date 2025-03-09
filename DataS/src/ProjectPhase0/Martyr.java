package ProjectPhase0;

public class Martyr {
	
	private String name;
	private int age;
	private String gender;
	private String dateOfDeath;
	private String eventLocation;

	public Martyr() {

	}

	public Martyr(String name, int age, String eventLocation, String dateOfDeath, String gender) {

		this.name = name;
		this.age = age;
		this.gender = gender;
		this.dateOfDeath = dateOfDeath;
		this.eventLocation = eventLocation;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDateOfDeath() {
		return dateOfDeath;
	}

	public void setDateOfDeath(String dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
	}

	public String getEventLocation() {
		return eventLocation;
	}

	public void setEventLocation(String eventLocation) {
		this.eventLocation = eventLocation;
	}

	@Override
	public String toString() {
		return "Martyr [name=" + name + ", age=" + age + ", gender=" + gender + ", dateOfDeath=" + dateOfDeath
				+ ", eventLocation=" + eventLocation + "]";
	}

}
