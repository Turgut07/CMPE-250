import java.util.ArrayList;

/**
 * A generic Priority Queue implemented using a max-heap.
 * The priority queue is backed by an ArrayList to store elements.
 *
 * @param <T> The type of elements in the priority queue. Must implement Comparable.
 */
public class PriorityQueue<T extends Comparable<? super T>> {

    // Internal representation of the heap
    public ArrayList<T> heap;

    /**
     * Constructs an empty PriorityQueue.
     */
    public PriorityQueue() {
        heap = new ArrayList<>();
    }

    /**
     * Copy constructor to create a new PriorityQueue with the same elements as another queue.
     *
     * @param other The PriorityQueue to copy.
     */
    public PriorityQueue(PriorityQueue<T> other) {
        heap = new ArrayList<>(other.heap); // Copy the internal array
    }

    /**
     * Creates a deep copy of the PriorityQueue.
     *
     * @return A new PriorityQueue containing the same elements.
     */
    public PriorityQueue<T> deepCopy() {
        return new PriorityQueue<>(this); // Use the copy constructor
    }

    /**
     * Inserts a new element into the priority queue.
     *
     * @param value The element to insert.
     */
    public void insert(T value) {
        heap.add(value); // Add the new value to the end of the heap
        siftUp(heap.size() - 1); // Restore the heap property
    }

    /**
     * Removes and returns the maximum (root) element of the priority queue.
     *
     * @return The maximum element in the priority queue.
     * @throws IllegalStateException If the priority queue is empty.
     */
    public T removeMax() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }

        T maxValue = heap.get(0); // Get the root element
        T lastValue = heap.remove(heap.size() - 1); // Remove the last element

        if (!isEmpty()) {
            heap.set(0, lastValue); // Replace root with the last element
            siftDown(0); // Restore the heap property
        }

        return maxValue;
    }

    /**
     * Returns the maximum (root) element without removing it.
     *
     * @return The maximum element in the priority queue.
     * @throws IllegalStateException If the priority queue is empty.
     */
    public T getMax() {
        if (isEmpty()) {
            throw new IllegalStateException("Heap is empty");
        }
        return heap.get(0);
    }

    /**
     * Checks if the priority queue is empty.
     *
     * @return true if the priority queue is empty, false otherwise.
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Returns the number of elements in the priority queue.
     *
     * @return The size of the priority queue.
     */
    public int size() {
        return heap.size();
    }

    /**
     * Updates an element in the priority queue with a new value.
     *
     * @param oldValue The existing value to update.
     * @param newValue The new value to replace the old value.
     * @throws IllegalArgumentException If the old value is not found in the heap.
     */
    public void update(T oldValue, T newValue) {
        int index = findIndex(oldValue); // Find the index of the old value
        if (index == -1) {
            throw new IllegalArgumentException("Value not found in the heap");
        }

        heap.set(index, newValue); // Update the value

        // Restore heap property
        if (newValue.compareTo(oldValue) > 0) {
            siftUp(index); // New value is larger, sift up
        } else if (newValue.compareTo(oldValue) < 0) {
            siftDown(index); // New value is smaller, sift down
        }
    }

    /**
     * Finds the index of a specific value in the heap.
     *
     * @param value The value to search for.
     * @return The index of the value, or -1 if not found.
     */
    private int findIndex(T value) {
        for (int i = 0; i < heap.size(); i++) {
            if (heap.get(i).equals(value)) {
                return i; // Return the index if the value is found
            }
        }
        return -1; // Return -1 if the value is not found
    }

    /**
     * Restores the heap property by sifting up the element at the specified index.
     *
     * @param index The index of the element to sift up.
     */
    private void siftUp(int index) {
        int parentIndex = (index - 1) / 2; // Calculate parent index

        while (index > 0 && heap.get(index).compareTo(heap.get(parentIndex)) > 0) {
            swap(index, parentIndex); // Swap with the parent
            index = parentIndex; // Update index to parent
            parentIndex = (index - 1) / 2; // Recalculate parent index
        }
    }

    /**
     * Restores the heap property by sifting down the element at the specified index.
     *
     * @param index The index of the element to sift down.
     */
    private void siftDown(int index) {
        int leftChild = 2 * index + 1; // Calculate left child index
        int rightChild = 2 * index + 2; // Calculate right child index
        int largest = index; // Assume current index is the largest

        // Check if the left child is larger
        if (leftChild < heap.size() && heap.get(leftChild).compareTo(heap.get(largest)) > 0) {
            largest = leftChild;
        }

        // Check if the right child is larger
        if (rightChild < heap.size() && heap.get(rightChild).compareTo(heap.get(largest)) > 0) {
            largest = rightChild;
        }

        // If the largest is not the current node, swap and continue sifting down
        if (largest != index) {
            swap(index, largest);
            siftDown(largest); // Recursive call
        }
    }

    /**
     * Swaps two elements in the heap.
     *
     * @param i The index of the first element.
     * @param j The index of the second element.
     */
    private void swap(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    /**
     * Returns a string representation of the priority queue.
     *
     * @return A string representing the elements of the queue in array order.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PriorityQueue: [");
        for (int i = 0; i < heap.size(); i++) {
            sb.append(heap.get(i));
            if (i < heap.size() - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
