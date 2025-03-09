package Dijkstra;

public class Edge {
    private Vertix source;
    private Vertix destination;
    private double distance; 
    private double cost;
    private double time;

    // Constructor
    public Edge(Vertix source, Vertix destination, double cost, double time) {
        this.source = source;
        this.destination = destination;
        this.cost = cost;
        this.time = time;
        this.distance = calculateDistance(); // Use the new Distance method
    }

 // Method to calculate the geographical distance using the Haversine formula
    private double calculateDistance() {
        final double EARTH_RADIUS_KM = 6371.0; 

        // Take latitude and longitude for the source and destination capitals(Degress)
        double lat1 = source.getCapital().getLatitude();  
        double lon1 = source.getCapital().getLongitude(); 
        double lat2 = destination.getCapital().getLatitude();  
        double lon2 = destination.getCapital().getLongitude(); 

        // Convert differences in latitude and longitude to radians
        double dLat = Math.toRadians(lat2 - lat1);  // فرق دوائر العرض (converted to radians)
        double dLon = Math.toRadians(lon2 - lon1);  // فرق خطوط الطول (converted to radians)

        // Convert original latitudes from Degree to radians for use in trigonometric functions
        lat1 = Math.toRadians(lat1);  
        lat2 = Math.toRadians(lat2); 

        // Apply the Haversine formula
        // The Haversine formula: a = sin²(Δlat/2) + cos(lat1) * cos(lat2) * sin²(Δlon/2)
        double a = Math.pow(Math.sin(dLat / 2), 2) +  
                   Math.cos(lat1) * Math.cos(lat2) *  
                   Math.pow(Math.sin(dLon / 2), 2);  

        // Calculate the great-circle distance (central angle in radians)
        double c = 2 * Math.asin(Math.sqrt(a)); 

        // Convert the central angle to distance in kilometers
        return EARTH_RADIUS_KM * c;  // Distance in kilometers (R * θ where θ is in radians)
    }

    
    
    


    // Getters
    public double getDistance() {
        return distance;
    }

    public double getCost() {
        return cost;
    }

    public double getTime() {
        return time;
    }

    public Vertix getSource() {
        return source;
    }

    public Vertix getDestination() {
        return destination;
    }
}




