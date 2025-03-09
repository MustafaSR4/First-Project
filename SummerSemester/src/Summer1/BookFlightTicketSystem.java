package Summer1;
import java.io.File;
import  java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

class Flight {			// Class representing Flight details
	String flightNumber;
	String origin;
	String destination;
	String departureDate;
	String departureTime;
	int totalTickets;
	double ticketPrice;


	Flight() {

	}

	Flight(String flightNumber, String origin, String destination, String departureDate, String departureTime,
			int totalTickets, double ticketPrice) {    // Constructor for Flight class
		this.flightNumber = flightNumber;
		this.origin = origin;
		this.destination = destination;
		this.departureDate = departureDate;
		this.departureTime = departureTime;
		this.totalTickets = totalTickets;
		this.ticketPrice = ticketPrice;

	}
	// Getters and setters for Flight class

	public String getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(String flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
	}

	public int getTotalTickets() {
		return totalTickets;
	}

	public void setTotalTickets(int totalTickets) {
		this.totalTickets = totalTickets;
	}

	public double getTicketPrice() {
		return ticketPrice;
	}

	public void setTicketPrice(double ticketPrice) {
		this.ticketPrice = ticketPrice;
	}

}
class Passenger {				// Class representing Passenger details
	String flightNumber;
	String passengerName;

	Passenger() {

	}

	Passenger(String flightNumber, String passengerName) { // Constructor for Passenger class
		this.flightNumber = flightNumber;
		this.passengerName = passengerName;
	}
}

//Main class for the Flight Ticket Booking System
public class BookFlightTicketSystem {
	static Scanner input = new Scanner(System.in);
	static Flight[] flights = new Flight[100];
	static Passenger[] passengers = new Passenger[100];
	static String passengerName;
	static int flightCount = 0;
	Passenger p = new Passenger();
	static int passengerCount = 0;



	public static void addnewflight() {		// this method used to add a new flight in the airport
		if (flightCount >= flights.length) {
			System.out.println("Cannot add more flights. Capacity reached.");
			return;
		}
		//flight attributes that should take it from the user
		System.out.print("Enter flight number = ");
		String flightNumber = input.next();

		input.nextLine();		 
		System.out.print("Enter origin airport = ");
		String origin = input.nextLine();

		System.out.print("Enter destination airport = ");
		String destination = input.nextLine();

		System.out.print("Enter departure date = ");
		String departureDate = input.next();

		input.nextLine();
		System.out.print("Enter departure time = ");
		String departureTime = input.nextLine();

		System.out.print("Enter total number of tickets = ");
		int totalTickets = input.nextInt();

		System.out.print("Enter ticket price = ");
		double ticketPrice = input.nextDouble();

		for (int i = 0; i < flightCount; i++) {

		}
		// Create a new Flight object and add it to the flights array
		Flight newFlight = new Flight(flightNumber, origin, destination, departureDate, departureTime, totalTickets,ticketPrice);

		flights[flightCount] = newFlight;
		flightCount++;

		System.out.println("Flight added successfully!");
	}




	public static void bookTicket() {		//we use this method for booking tickets for passengers in a certain flight (we choose the certain flight)
		boolean flightFound = false;
		System.out.print("Enter the flight number to book tickets: ");
		String flightToBook = input.next(); 	//entering the flight number that we would book for passenger in it

		for (int i = 0; i < flightCount; i++) {
			if (flights[i].flightNumber.equals(flightToBook)) {//if the flight number that entered is correct and exist then book a ticket
				flightFound = true;
				try {
					if (flights[i].totalTickets > 0) {	
						System.out.print("Enter passenger name: ");
						String passengerName = input.next();

						Passenger newPassenger = new Passenger(flightToBook, passengerName );	//build an object from the Passenger class 

						passengers[passengerCount++] = newPassenger; 			//store the name of the passenger in the array

						flights[i].totalTickets--;//if the progran booked an ticked it will decrement total tickets of the flight
						System.out.println("Ticket booked for Flight " + flightToBook + " for passenger " + passengerName);
					} else {
						throw new IllegalArgumentException("No available seats for this flight.");//if there is no seats available throw this exception
					}
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
				}
			}
		}


		if (flightFound== false) {
			System.out.println("Flight not found.");
		}
	}

	public static void updateTicket() {	   //this method for changing the passenger's name that they are booked an ticket in a cetain flight(FlightNumber)

		System.out.print("\nEnter the flight number to update tickets: ");
		String flightNumber = input.next();
		//input.nextLine();
		System.out.print("Enter the passenger name to update ticket: ");
		String passengerName = input.next();

		System.out.print("Enter the new passenger name: ");
		String passengerNewName = input.next();
		try {
			boolean passengerFound = false;

		for (int i = 0; i < passengerCount; i++) {
			if (passengers[i].flightNumber.equals(flightNumber) && passengers[i].passengerName.equals(passengerName)) { //if the entered flight number and the entered passenger name are equal to those that are stored in the passenger array
				passengerFound = true;
				if(passengerName.equals(passengerNewName)) {
					System.out.println("ERROR =The new passenger cannot be the same old passenger");//it will breaks if the entered name is the same old name
					break;
				}
				passengers[i].passengerName = passengerNewName;//change the old name to the new name
				System.out.println("Ticket updated for Flight " + flightNumber + " for passenger " + passengerNewName);
				break;
			}
		}

			if (passengerFound== false) {
			 throw new NoSuchElementException ("Ticket hasn't been booked for passenger: " + passengerName);//if the entered name isn't found it will  throw this exception
			}
		} catch (NoSuchElementException e) {
        System.out.println("Error: " + e.getMessage());
		}
	}

	public static void removeTicket() { 			//this method for removing the passenger's ticket that are reserved in a certain flight
		try {
		boolean passengerRemoved = false;
		boolean flightFound = false;
		String passengerToRemove = " ";
		
		System.out.print("Enter flight number to remove ticket: ");
		String flightToRemove = input.next();

		for (int i = 0; i < passengerCount; i++) {
			if (passengers[i].flightNumber.equals(flightToRemove) ) {//if the entered flight number and the entered passenger name are equal to those that are stored in the passenger array
				flightFound=true;
				System.out.print("Enter passenger name to remove ticket: ");
				 passengerToRemove = input.next();
					if(passengers[i].passengerName.equals(passengerToRemove)) {
						passengerRemoved = true;

				for (int j = 0; j < flightCount; j++) {
					if (flights[j].flightNumber.equals(passengers[i].flightNumber)) {
						flights[j].totalTickets++;//if the program remove the ticket so the total will increment because it remove one
						break;
					}
				}
				passengers[i] = passengers[passengerCount - 1];
				passengerCount--;//when we increment the total ticket ==passenger down
				System.out.println("Ticket for passenger " + passengerToRemove + " removed successfully.");
				break;
					}
			}	
		}
		if(flightFound==false) {
			System.out.println("Flight not found.");
		}
		else if (passengerRemoved== false) {
				 throw new NoSuchElementException ("Ticket hasn't been booked for passenger: " + passengerToRemove);//it will throw this exception if the name of the passenger wrong or not found in a certain flight number
	        }
	    } catch (NoSuchElementException e) {
	        System.out.println("Error: " + e.getMessage());
	    }
	}

	public static void printBookedPassengers() { //this method for printing all the passenger name that are reserved in certain flight
		boolean flightFoundForView = false;
		System.out.print("\nEnter Flight number to print the booked passengers: ");
		String flightNumberToView = input.next();


		System.out.println("Booked Passengers for Flight " + flightNumberToView + ":");
		for (int i = 0; i < passengerCount; i++) {
			if (passengers[i].flightNumber.equals(flightNumberToView)) { 
				flightFoundForView = true;
				System.out.println("Passenger Name: " + passengers[i].passengerName);//print all the passenger in this flight
			}
		}

		if (flightFoundForView== false) {
			System.out.println("No passengers found for Flight " + flightNumberToView );
		}
	}



	public static void displayAvailableFlights() { //print all the details of the flight that the program added with their attributes
		Flight f = new Flight();
		for (int i = 0; i < flightCount; i++) {
			System.out.println("\nAvailable Flight:");
			System.out.println("Flight Number: " + flights[i].flightNumber);
			System.out.println("origin: " + flights[i].origin);
			System.out.println("destination: " + flights[i].destination);
			System.out.println("departureDate: " + flights[i].departureDate);
			System.out.println("departureTime: " + flights[i].departureTime);
			System.out.println("departureTime: " + flights[i].departureTime);
			System.out.println("totalTickets: " + flights[i].totalTickets);
			System.out.println("ticketPrice: " + flights[i].ticketPrice + "$");
		}
		if (flightCount == 0) {//if there isn't an reserved flight it will print this
			System.out.println("\nThere are no flights to be shown, please add flights");
		}
	}

	static void readFlightDataFromFile() {     // this method for scanning from a file named flights.txt
		try {
			File file =new File ("flights.txt");

			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {//it will keep reading each line until it finished
				String line = scan.nextLine();
				String[] parts = line.split(",");//it will seperate the string that scan it from the file to an array called parts
				if (parts.length != 7) {
					System.out.println("Error: Invalid data format in 'flights.txt'.");
					return;
				}
				String flightNumber = parts[0].trim();//the seperation string will be stored in each string
				String origin = parts[1].trim();
				String destination = parts[2].trim();
				String departureDate = parts[3].trim();
				String departureTime = parts[4].trim();
				int totalTickets = Integer.parseInt(parts[5].trim());
				double ticketPrice = Double.parseDouble(parts[6].trim());

				Flight newFlight = new Flight(flightNumber, origin, destination,
						departureDate, departureTime,
						totalTickets, ticketPrice);

				flights[flightCount] = newFlight;
				flightCount++;

			}
			scan.close();
			System.out.println("Flight data loaded successfully from 'flights.txt'.");
		} catch (FileNotFoundException e) {//this exception for scanning from a undefined file (Doesn't exsist)
			System.out.println("Error: 'flights.txt' not found.");
		}catch (IOException e) {
			System.out.println("Error reading flight data from 'flights.txt'.");
		} catch (Exception e) {
			System.out.println("Error reading flight data from 'flights.txt'.");
		}

	}
	static void saveBookedPassengersToFile(){// this method for printing all the booked passengers in a notepad named passengers.txt
		try {
			File file=new File("passengers.txt");
			PrintWriter input=new PrintWriter(file);
			for (int i = 0; i < passengerCount; i++) {
				input.println(passengers[i].flightNumber + "," + passengers[i].passengerName);//the way to print in the passengers.txt
			}

			input.close();
			System.out.println("Booked passengers saved to 'passengers.txt'.");
		} catch (IOException e) {
			System.out.println("Error saving booked passengers to 'passengers.txt'.");
		}

	}




	// Main Method
	public static void main(String[] args) {		// Main method to start the program

		readFlightDataFromFile() ;//this method for scanning the inputs in a flights.txt
		Scanner input = new Scanner(System.in);// let the user pick an option from the menu
		int choice = 0;
		System.out.println("Flight Ticket Booking System");// the menu itself(what can the user do)

		while (choice != 7) { // the program will continue taking numbers until the user enter number 7
			System.out.println("\n1.Add a new flight");
			System.out.println("2.Book Ticket");
			System.out.println("3.Update Ticket");
			System.out.println("4.Remove ticket");
			System.out.println("5.Print Booked Passengers");
			System.out.println("6.Display Avalabile Flights");
			System.out.println("7.Exit");
			System.out.print("Enter your choice = ");
			try {
				choice = input.nextInt(); // store the option that the user want in an variable called choice

				switch (choice) { // the user should enter an number between 1 and 7

				case 1:
					addnewflight();

					break;

				case 2:
					bookTicket();

					break;
				case 3:
					updateTicket();
					break;
				case 4:
					removeTicket();
					break;
				case 5:
					printBookedPassengers();
					break;
				case 6:
					displayAvailableFlights();
					break;
				case 7:
					saveBookedPassengersToFile();
					System.out.println("\nThank you for using the Flight Ticket Booking System. Goodbye!\n");
					break;
				default:
					System.out.println("\n Error input. Please enter an integer between 1-7 ");  // when the user enter a different number between 1 and 7 it will

				}
			}catch (InputMismatchException e) {
				System.out.println("\n Only you can enter an integer "+e);//this exception for entering anything unless integer data type
				input.nextLine(); // Consume the invalid input
			}
		}

	}

}
