package ProjectPhase3;

public class Martyr {
  private String name;
  private String dateOfDeath;
  private byte age;
  private char gender;

  public Martyr(String name, String dateOfDeath, byte age, char gender) {
    this.name = name;
    this.dateOfDeath = dateOfDeath;
    this.age = age;
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
           ", gender=" + gender +
           '}';
  }
  
  
}
