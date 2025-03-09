package Dijkstra;
public class Vertix {
    private Capital capital;
    private LinkedList edges;  // Adjacency list of edges
    private double distance;   // Shortest known distance to this vertex
    private boolean visited;   // Whether the vertex has been visited
    private Vertix previous;   // Previous vertex in the shortest path

    // Constructor
    public Vertix(Capital capital) {
        this.capital = capital;
        this.edges = new LinkedList();
        this.distance = Double.MAX_VALUE;  // Infinity
        this.visited = false;
        this.previous = null;  // No previous vertex at the start
    }

    // Getter and setter for previous vertex
    public Vertix getPrevious() {
        return previous;
    }

    public void setPrevious(Vertix previous) {
        this.previous = previous;
    }

    // Other getters and setters
    public Capital getCapital() {
        return capital;
    }

    public LinkedList getEdges() {
        return edges;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    // Add an edge to the adjacency list
    public void addEdge(Edge edge) {
        edges.addLast(edge);
    }
}
