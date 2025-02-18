/**
 * Queue class for managing trucks in a circular queue.
 * Implements basic queue operations like enqueue, dequeue, and access to the front truck.
 */
public class Queue {
    int front, rear, truckLimit; // Indices for front and rear, and the truck limit (capacity) of the queue
    Truck[] queue; // Array to store trucks in the queue

    /**
     * Constructor to initialize a Queue with a given capacity.
     * Initializes the queue array and sets front and rear indices.
     *
     * @param c Maximum number of trucks the queue can hold.
     */
    Queue(int c) {
        front = 0; // Start front index at 0
        rear = -1; // Start rear index at -1, indicating the queue is initially empty
        truckLimit = c; // Set the queue capacity based on the parameter
        queue = new Truck[truckLimit]; // Initialize the truck array with the specified limit
    }

    /**
     * Adds a truck to the queue (enqueue operation).
     * Uses circular indexing to wrap around if the end of the array is reached.
     *
     * @param TData The truck to be added to the queue.
     */
    void enqueue(Truck TData) {
        rear = (rear + 1) % truckLimit; // Move rear index forward in a circular manner
        queue[rear] = TData; // Place the new truck at the rear of the queue
    }

    /**
     * Removes a truck from the front of the queue (dequeue operation).
     * Sets the front element to null and advances the front index in a circular way.
     */
    void dequeue() {
        queue[front] = null; // Remove the truck from the front of the queue
        front = (front + 1) % truckLimit; // Move front index forward in a circular manner
        return;
    }

    /**
     * Retrieves the truck at the front of the queue without removing it.
     *
     * @return The truck at the front of the queue.
     */
    public Truck front() {
        return queue[front]; // Return the truck at the front of the queue
    }
}
