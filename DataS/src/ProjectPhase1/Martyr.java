package ProjectPhase1;


public class Martyr {
    private String name;
    private String dateOfDeath;
    private byte age;
    private String location;
    private String district;
    private char gender;

    public Martyr(String name, String dateOfDeath, byte age, String location, String district, char gender) {
        this.name = name;
        this.dateOfDeath = dateOfDeath;
        this.age = age;
        this.location = location;
        this.district = district;
        this.gender = gender;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfDeath() {
        return dateOfDeath;
    }

    public void setDateOfDeath(String dateOfDeath) {
        this.dateOfDeath = dateOfDeath;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

   

        @Override
    public String toString() {
        return "Martyr{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", dateOfDeath='" + dateOfDeath + '\'' +
                ", location='" + location + '\'' +
                ", district='" + district + '\'' +
                ", gender=" + gender +
                '}';
    }
       

}
