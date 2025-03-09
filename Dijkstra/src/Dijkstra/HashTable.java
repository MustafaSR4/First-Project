package Dijkstra;

public class HashTable {
    private int tableSize;
    private Vertix[] table;

    public HashTable(int tableSize) {
        this.tableSize = tableSize;
        this.table = new Vertix[tableSize];
    }

    // Insert a vertex into the hash table
    public void put(Vertix value) {
        int hash = getHash(value.getCapital().getCapitalName());
        int attempts = 0;

        while (table[hash] != null && !table[hash].getCapital().getCapitalName().equals(value.getCapital().getCapitalName())) {
            hash = (hash + 1) % tableSize;
            attempts++;

            if (attempts >= tableSize) {
                System.err.println("Hash table insertion failed: " + value.getCapital().getCapitalName());
                return;
            }
        }

        // Check for duplicate insertion
        if (table[hash] != null && table[hash].getCapital().getCapitalName().equals(value.getCapital().getCapitalName())) {
            System.err.println("Duplicate vertex insertion detected: " + value.getCapital().getCapitalName());
            return;
        }

        table[hash] = value;
    }

    // Retrieve a vertex by its key
    public Vertix getVertex(String key) {
        int hash = getHash(key);
        int attempts = 0;

        while (table[hash] != null) {
            if (table[hash].getCapital().getCapitalName().equals(key)) {
                return table[hash];
            }

            hash = (hash + 1) % tableSize;
            attempts++;

            if (attempts >= tableSize) {
                break;
            }
        }

        System.err.println("Vertex not found: " + key);
        return null;
    }

    // Retrieve the index of a vertex
    public int getVertexIndex(String key) {
        int hash = getHash(key);
        int attempts = 0;

        while (table[hash] != null) {
            if (table[hash].getCapital().getCapitalName().equals(key)) {
                return hash;
            }

            hash = (hash + 1) % tableSize;
            attempts++;

            if (attempts >= tableSize) {
                break;
            }
        }

        return -1; // Vertex not found
    }

    // Hash function to compute index
    private int getHash(String key) {
        int hash = key.hashCode() % tableSize;
        return (hash < 0) ? hash + tableSize : hash;
    }

    // Get all vertices from the hash table
    public Vertix[] getAllVertices() {
        return table;
    }

    // Reset the visited status of all vertices
    public void setAllVerticesToFalse() {
        for (Vertix vertix : table) {
            if (vertix != null) {
                vertix.setVisited(false);
            }
        }
    }

    // Getters for table size and vertices
    public int getTableSize() {
        return tableSize;
    }

    public Vertix[] getTable() {
        return table;
    }
}

