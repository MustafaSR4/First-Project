package ProjectPhase3;

public class MinHeap {
    private Martyr[] heap; // Array to store heap elements
    private int size; // Number of elements in the heap
    private int capacity; // Maximum capacity of the heap

    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        heap = new Martyr[capacity + 1]; // Heap array is 1-indexed
    }

    // Returns the index of the left child
    private int left(int i) { 
    	return 2 * i;
    }
    
    // Returns the index of the right child
    private int right(int i) { 
    	return 2 * i + 1;
    }
    
    // Returns the index of the parent
    private int parent(int i) {
    	return i / 2;
    }

    // Swap two elements in the heap
    private void swap(int i, int j) {
        Martyr temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // Maintain the heap property starting from index i
    private void heapify(int i) {
        int l = left(i);
        int r = right(i);
        int smallest = i;
        if (l <= size && heap[l].getAge() < heap[smallest].getAge()) {
            smallest = l;
        }
        if (r <= size && heap[r].getAge() < heap[smallest].getAge()) {
            smallest = r;
        }
        if (smallest != i) {
            swap(i, smallest);
            heapify(smallest);
        }
    }

    // Build the min heap using the array of martyrs
    public void buildMinHeap(Martyr[] martyrs) {
        this.size = martyrs.length;
        this.heap = new Martyr[size + 1];
        System.arraycopy(martyrs, 0, heap, 1, martyrs.length);
        for (int i = size / 2; i >= 1; i--) {
            heapify(i);
        }
    }

    // Sort the array of martyrs using heap sort
    public void heapSort(Martyr[] martyrs) {
        buildMinHeap(martyrs);
        for (int i = size; i > 1; i--) {
            swap(1, i);
            size--;
            heapify(1);
        }
        System.arraycopy(heap, 1, martyrs, 0, martyrs.length);
    }
}
