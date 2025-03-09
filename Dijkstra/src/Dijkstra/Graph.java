package Dijkstra;


public class Graph {
    private int numberOfVertices;
    private HashTable hashTable;

    public Graph(int numberOfVertices) {
        this.numberOfVertices = numberOfVertices;
        hashTable = new HashTable(this.numberOfVertices);
    }

    public void addVertix(Vertix vertix) {
        hashTable.put(vertix);
    }

    public Vertix getVertix(String CapitalName) {
        return hashTable.getVertex(CapitalName);
    }

    public HashTable getHashTable() {
        return hashTable;
    }
    public void addEdge(String sourceName, String destinationName, double cost, double time) {
        Vertix source = getVertix(sourceName);
        Vertix destination = getVertix(destinationName);

        if (source == null || destination == null) {
            System.err.println("Source or destination vertex not found: " + sourceName + ", " + destinationName);
            return;
        }

        if (source.equals(destination)) {
            System.out.println("Self-loops are not allowed: " + sourceName + " -> " + destinationName);
            return;
        }

        Edge edge = new Edge(source, destination, cost, time);
        source.addEdge(edge);
    }
    
    
    

    
    
 // Method to find the optimal path from a source to a destination based on a specified filter (distance, cost, or time).
    public LinkedList getOptimalPath(String sourceName, String destinationName, String filter) {
        Vertix sourceVertix = getVertix(sourceName);
        Vertix destinationVertix = getVertix(destinationName);

        // Check if source or destination vertices are null (not found).
        if (sourceVertix == null || destinationVertix == null) {
            System.err.println("Source or destination vertex not found.");
            return null;
        }

        // Initialize the graph for Dijkstra's Algorithm
        initializeGraph(sourceVertix);

        // Main Dijkstra Loop (O(V²) 
        while (true) {
            Vertix currentVertix = null;  // Vertex with the smallest unknown distance.
            double smallestDistance = Double.MAX_VALUE;  // Infinity.

            // Find the vertex with the smallest distance that is still unvisited (O(V) search).
            for (Vertix vertix : hashTable.getAllVertices()) {
                if (vertix != null && !vertix.isVisited() && vertix.getDistance() < smallestDistance) {
                    smallestDistance = vertix.getDistance();  // Update the smallest distance.
                    currentVertix = vertix;  // Update the current vertex.
                }
            }

            // Break condition: If there are no more unvisited vertices or the destination is reached.
            if (currentVertix == null || currentVertix.equals(destinationVertix)) {
                break;
            }

            // Mark the current vertex as visited (known).
            currentVertix.setVisited(true);

            // Relaxation: Update distances for neighboring vertices.
            LinkedList edges = currentVertix.getEdges();  // Get the list of edges for the current vertex.
            LinkedListNode edgeNode = edges.getFirstNode();  // Start at the first edge in the list.

            while (edgeNode != null) {
                Edge edge = edgeNode.getEdge();  // Get the current edge.
                Vertix neighbor = edge.getDestination();  // Get the destination vertex (neighbor).

                if (!neighbor.isVisited()) {  // If the neighbor is not yet visited.
                    double weight;  // Variable to store the weight of the edge based on the filter.
                    switch (filter.toLowerCase()) {
                        case "cost":
                            weight = edge.getCost();  
                            break;
                        case "time":
                            weight = edge.getTime();  
                            break;
                        case "distance":
                        default:
                            weight = edge.getDistance();  
                            break;
                    }

                    // Calculate the new potential distance for the neighbor vertex.
                    double newDistance = currentVertix.getDistance() + weight;

                    // If the new distance is shorter than the current distance, update it.
                    if (newDistance < neighbor.getDistance()) {
                        neighbor.setDistance(newDistance);  // Update the distance to the neighbor vertex.
                        neighbor.setPrevious(currentVertix);  // Save the current vertex as the previous vertex for backtracking.
                    }
                }

                edgeNode = edgeNode.getNext();  // Move to the next edge in the list.
            }
        }

        // Build the shortest path from the source to the destination.
        LinkedList path = new LinkedList(); 
        Vertix current = destinationVertix; 

        // If the distance is still "infinity," there is no path.
        if (current.getDistance() == Double.MAX_VALUE) {
            System.out.println("No path found.");
            return null;  // Return null if there is no valid path.
        }

        // Backtrack from destination to source using the previous pointers.
        while (current != null && current.getPrevious() != null) {
            Edge edge = getEdgeBetween(current.getPrevious(), current);  // Get the edge between the previous vertex and the current vertex.
            if (edge != null) {
                path.addFirst(edge);  // Add the edge to the path at the beginning (to maintain the correct order).
            }
            current = current.getPrevious();  // Move to the previous vertex.
        }

        return path;  // Return the constructed path.
    }

    // Method to initialize the graph for Dijkstra's Algorithm
    private void initializeGraph(Vertix sourceVertix) {
        for (Vertix vertix : hashTable.getAllVertices()) {
            if (vertix != null) {
                vertix.setDistance(Double.MAX_VALUE);  
                vertix.setVisited(false);              
                vertix.setPrevious(null);              
            }
        }
        sourceVertix.setDistance(0);  
    }

    

    
    
    
    Edge getEdgeBetween(Vertix v1, Vertix v2) {
        LinkedList edges = v1.getEdges();  // Get the list of edges for vertex v1.
        LinkedListNode edgeNode = edges.getFirstNode();  // Start at the first edge.

        // Iterate through the edges to find the edge that points to v2.
        while (edgeNode != null) {
            Edge edge = edgeNode.getEdge();  // Get the current edge.
            if (edge.getDestination().equals(v2)) {  // Check if the destination of the edge is v2.
                return edge;  // Return the edge if found.
            }
            edgeNode = edgeNode.getNext();  // Move to the next edge.
        }

        return null;  // Return null if no edge is found.
    }
    
    
    
    
    
    
    
    
    
    
// // Method to find the optimal path from a source to a destination based on a specified filter (distance, cost, or time).
//    public LinkedList getOptimalPath(String sourceName, String destinationName, String filter) {
//        // Retrieve the source and destination vertices using their capital names.
//        Vertix sourceVertix = getVertix(sourceName);
//        Vertix destinationVertix = getVertix(destinationName);
//
//        // Check if source or destination vertices are null (not found).
//        if (sourceVertix == null || destinationVertix == null) {
//            System.err.println("Source or destination vertex not found.");
//            return null;
//        }
//
//        
//        
//        
//        // Initialize Dijkstra.
//        
//        for (Vertix vertix : hashTable.getAllVertices()) {
//            if (vertix != null) {
//                vertix.setDistance(Double.MAX_VALUE);  // Set initial distance to infinity.
//                vertix.setVisited(false);              // Mark vertex as unvisited.
//                vertix.setPrevious(null);              // No previous vertex (for path reconstruction).
//            }
//        }
//
//        sourceVertix.setDistance(0);  // Set the distance of the source vertex to 0 (start point).
//
//        
//        
//        
//        
//        // Main Dijkstra Loop (O(V²).
//        while (true) {
//            Vertix currentVertix = null;  // Vertex with the smallest unknown distance.
//            double smallestDistance = Double.MAX_VALUE;  // infinity.
//
//            // Find the vertex with the smallest distance that is still unvisited (O(V) search).**
//            for (Vertix vertix : hashTable.getAllVertices()) {
//                if (vertix != null && !vertix.isVisited() && vertix.getDistance() < smallestDistance) {
//                    smallestDistance = vertix.getDistance();  // Update the smallest distance.
//                    currentVertix = vertix;  // Update the current vertex.
//                }
//            }
//
//            // Break condition
//            // If there are no more unvisited vertices or the destination is reached.
//            if (currentVertix == null || currentVertix.equals(destinationVertix)) {
//                break;
//            }
//
//            // Mark the current vertex as visited (known).
//            currentVertix.setVisited(true);
//
//            //Relaxation: Update distances for neighboring vertices 
//            LinkedList edges = currentVertix.getEdges();  // Get the list of edges for the current vertex.
//            LinkedListNode edgeNode = edges.getFirstNode();  // Start at the first edge in the list.
//
//            while (edgeNode != null) {
//                Edge edge = edgeNode.getEdge();  // Get the current edge.
//                Vertix neighbor = edge.getDestination();  // Get the destination vertex (neighbor).
//
//                if (!neighbor.isVisited()) {  // If the neighbor is not yet visited.
//                    double weight;  
//                    switch (filter.toLowerCase()) {
//                        case "cost":
//                            weight = edge.getCost(); 
//                            break;
//                        case "time":
//                            weight = edge.getTime();  
//                            break;
//                        case "distance":
//                        default:
//                            weight = edge.getDistance();  
//                            break;
//                    }
//
//                    // Calculate the new potential distance for the neighbor vertex.
//                    double newDistance = currentVertix.getDistance() + weight;
//
//                    
//                    // If the new distance is shorter than the current distance, update it.
//                    if (newDistance < neighbor.getDistance()) {
//                        neighbor.setDistance(newDistance);  // Update the distance to the neighbor vertex.
//                        neighbor.setPrevious(currentVertix);  // Save the current vertex as the previous vertex for backtracking.
//                    }
//                }
//
//                edgeNode = edgeNode.getNext();  // Move to the next edge in the list.
//            }
//        }
//
//        // Build the shortest path from the source to the destination.**
//        LinkedList path = new LinkedList();  // Create a new linked list to store the path.
//        Vertix current = destinationVertix;  // Start from the destination vertex.
//
//        // If the distance is still "infinity," there is no path.
//        if (current.getDistance() == Double.MAX_VALUE) {
//            System.out.println("No path found.");
//            return null;  // Return null if there is no valid path.
//        }
//
//        //Backtrack from destination to source using the previous pointers.**
//        while (current != null && current.getPrevious() != null) {
//            Edge edge = getEdgeBetween(current.getPrevious(), current);  // Get the edge between the previous vertex and the current vertex.
//            if (edge != null) {
//                path.addFirst(edge);  // Add the edge to the path at the beginning (to maintain the correct order).
//            }
//            current = current.getPrevious();  // Move to the previous vertex.
//        }
//
//        return path;  // Return the constructed path.
//    }
//

    
    
    
    


    
    
    
    
    

}