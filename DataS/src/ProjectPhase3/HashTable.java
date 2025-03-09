package ProjectPhase3;

import java.util.Arrays;

// HashTable class represents a hash table data structure
public class HashTable {
    private HashEntry[] table; // Array to hold the hash table entries
    private int size; // Size of the hash table
    private int numEntries; // Number of entries in the hash table
//    private HashEntry firstEntry; // Reference to the first entry in order
//    private HashEntry lastEntry; // Reference to the last entry in order

    public HashTable(int size) {
        this.size = size;
        this.table = new HashEntry[size];
        this.numEntries = 0; // Initialize number of entries to 0
//        firstEntry = null;
//        lastEntry = null;
    }

    // Method to insert a key-value pair into the hash table
    public void put(String key, Object value) {
        int hashIndex = hash(key); // Calculate hash index for the key
        HashEntry newEntry = new HashEntry(key, value); // Create a new HashEntry

        if (table[hashIndex] == null) {
            table[hashIndex] = newEntry; // Insert new entry if spot is empty
        } else {
            hashIndex = quadraticProbe(key); // Find next available spot using quadratic probing
            if (table[hashIndex] == null) {
                table[hashIndex] = newEntry; // Insert new entry at the probed index
            } else {
                HashEntry entry = table[hashIndex];
                while (entry.getNext() != null && !entry.getKey().equals(key)) {
                    entry = entry.getNext(); // Traverse the hashTable to find the end or matching key
                }
                if (entry.getKey().equals(key)) {
                    entry.setValue(value); // Update value if key matches
                } else {
                    entry.setNext(newEntry); // Append new entry if end is reached
                }
            }
        }
        numEntries++;
//        updateInOrderLinks(newEntry); // Update in-order links for the new entry
    }

    // Method to perform quadratic probing to find an available index
    private int quadraticProbe(String key) {
        int hashIndex = hash(key); // Calculate hash index for the key
        int i = 0;
        while (table[(hashIndex + i * i) % size] != null && !table[(hashIndex + i * i) % size].getKey().equals(key)) {
            i++; // Increment i for quadratic probing if there is collasion
        }
        return (hashIndex + i * i) % size; // Return the calculated index
    }

   
    // Method to retrieve an entry at a specific index
    public HashEntry getEntry(int index) {
        if (index >= 0 && index < size) {
            return table[index]; // Return entry at the specified index
        }
        return null;
    }

//    // Method to get the next entry in order
//    public HashEntry getNextInOrder(HashEntry current) {
//        return current.getNextInOrder(); // Return next in-order entry
//    }
//
//    // Method to get the previous entry in order
//    public HashEntry getPrevInOrder(HashEntry current) {
//        return current.getPrevInOrder(); // Return previous in-order entry
//    }

    // Method to retrieve a value associated with a key
    public Object get(String key) {
        int hashIndex = hash(key); // Calculate hash index for the key
        int i = 0;
        while (table[(hashIndex + i * i) % size] != null) {
            if (table[(hashIndex + i * i) % size].getKey().equals(key)) {
                return table[(hashIndex + i * i) % size].getValue(); // Return value if key matches
            }
            i++; // Increment i for quadratic probing
        }
        return null; // Return null if key not found
    }

    // Method to remove an entry associated with a key
    public int remove(String key) {
        int hashIndex = hash(key); // Calculate hash index for the key
        int originalIndex = hashIndex;
        int probeCount = 0;

        while (table[hashIndex] != null && !table[hashIndex].getKey().equals(key)) {
            probeCount++;
            hashIndex = (originalIndex + probeCount * probeCount) % size; // Quadratic probing

            if (probeCount >= size) {
                return -1; // Prevent infinite loop
            }
        }

        if (table[hashIndex] == null) {
            return -1; // Return -1 if key not found
        }

//        unlinkInOrder(table[hashIndex]); // Unlink the entry in order

        table[hashIndex] = null; // Remove the entry

        numEntries--;

        // Re-insert subsequent entries to maintain proper probing
        hashIndex = (hashIndex + 1) % size;
        while (table[hashIndex] != null) {
            HashEntry entryToReinsert = table[hashIndex];
            table[hashIndex] = null;
            numEntries--;
            put(entryToReinsert.getKey(), entryToReinsert.getValue());
            hashIndex = (hashIndex + 1) % size;
        }

        return originalIndex; // Return the original index
    }

//    // Method to unlink an entry in order
//    private void unlinkInOrder(HashEntry entry) {
//        if (entry.getPrevInOrder() != null) {
//            entry.getPrevInOrder().setNextInOrder(entry.getNextInOrder()); // Update previous entry's next link
//        } else {
//            firstEntry = entry.getNextInOrder(); // Update first entry
//        }
//        if (entry.getNextInOrder() != null) {
//            entry.getNextInOrder().setPrevInOrder(entry.getPrevInOrder()); // Update next entry's previous link
//        } else {
//            lastEntry = entry.getPrevInOrder(); // Update last entry
//        }
//    }

    // Method to check if the table is half full
    public boolean isHalfFull() {
        return numEntries >= size / 2; // Check if the table is half full
    }

    // Method to rehash the table when it becomes too full
    public void rehash() {
        int newSize = primeAfter(size * 2); // Calculate new size
        HashEntry[] oldTable = table;
        table = new HashEntry[newSize]; // Create new table
        size = newSize;
        numEntries = 0;
//        firstEntry = null;
//        lastEntry = null;

        for (HashEntry entry : oldTable) {
            while (entry != null) {
                put(entry.getKey(), entry.getValue()); // Re-insert each entry
                entry = entry.getNext();
            }
        }
    }
    private boolean isPrime(int num) {
        if (num <= 1) return false;
        if (num <= 3) return true;
        if (num % 2 == 0 || num % 3 == 0) return false;
        for (int i = 5; i * i <= num; i += 6) {
            if (num % i == 0 || num % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    private int primeAfter(int num) {
        while (!isPrime(num)) {
            num++;
        }
        return num;
    }



    // Method to calculate hash index for a key
    public int hash(String key) {   //hashcode for computing the index for the String class
        return Math.abs(key.hashCode() % size); // Calculate hash index and abs for absolue value so not negative values  and 
    }

    // Method to print the hash table it checks all the index from top to bottom and print without any option traversal
    public String printHashTable(boolean includeEmptySpots) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            HashEntry entry = table[i];
            if (entry != null) {
                sb.append("Index ").append(i).append(": ");
                while (entry != null) {
                    sb.append(entry.getValue()).append("] -> ");
                    entry = entry.getNext();
                }
                sb.append("null\n");
            } else if (includeEmptySpots) {
                sb.append("Index ").append(i).append(": [E] null\n");
            }
        }
        return sb.toString();
    }

    // Method to get the size of the hash table
    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "HashTable [table=" + Arrays.toString(table) + ", size=" + size + ", numEntries=" + numEntries + "]";
    }
}
