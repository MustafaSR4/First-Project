package HuffmanProject;

public class MinHeap {
    private HuffmanNode[] heap; // Array to store heap elements
    private int size; // Number of elements in the heap
    private int capacity; // Maximum capacity of the heap

    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        heap = new HuffmanNode[capacity + 1]; // Heap array is 1-indexed
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
        HuffmanNode temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
    }

    // Maintain the heap property starting from index i
    private void heapify(int i) {
        int l = left(i);
        int r = right(i);
        int smallest = i;

        if (l <= size && heap[l].frequency < heap[smallest].frequency) {
            smallest = l;
        }
        if (r <= size && heap[r].frequency < heap[smallest].frequency) {
            smallest = r;
        }
        if (smallest != i) {
            swap(i, smallest);
            heapify(smallest);
        }
    }

    // Insert a new HuffmanNode into the heap
    public void insert(HuffmanNode node) {
        if (size >= capacity) {
            throw new IllegalStateException("Heap overflow");
        }

        heap[++size] = node;
        int current = size;

        while (current > 1 && heap[current].frequency < heap[parent(current)].frequency) {
            swap(current, parent(current));
            current = parent(current);
        }
    }

    // Extract the minimum element (root) from the heap
    public HuffmanNode extractMin() {
        if (size == 0) {
            throw new IllegalStateException("Heap underflow");
        }

        HuffmanNode min = heap[1];
        heap[1] = heap[size--];
        heapify(1);

        return min;
    }

    // Build the min heap using an array of HuffmanNodes
    public void buildMinHeap(HuffmanNode[] nodes) {
        this.size = nodes.length;
        this.heap = new HuffmanNode[size + 1];
        System.arraycopy(nodes, 0, heap, 1, nodes.length);

        for (int i = size / 2; i >= 1; i--) {
            heapify(i);
        }
    }

    // Get the current size of the heap
    public int getSize() {
        return size;
    }
}
