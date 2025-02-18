/**
 * Represents a parking lot with capacity constraints and a limit on the number of trucks it can hold.
 * Each parking lot has queues for trucks that are ready and waiting, and counters for the total,
 * waiting, and ready trucks.
 */
public class ParkingLot {

    int capacityConstraint; // Maximum load capacity the parking lot can handle
    int truckLimit; // Maximum number of trucks allowed in the parking lot
    Queue ready; // Queue to store trucks that are ready to be loaded
    Queue waiting; // Queue to store trucks that are waiting
    int totalTruckCount; // Counter for the total number of trucks in the lot
    int waitingTruckCount; // Counter for the number of waiting trucks
    int readyTruckCount; // Counter for the number of ready trucks

    /**
     * Constructor to initialize a ParkingLot with a given capacity constraint and truck limit.
     * Initializes the ready and waiting queues based on the truck limit, and sets counters to zero.
     *
     * @param capacityConstraint Maximum load capacity the parking lot can handle.
     * @param truckLimit Maximum number of trucks allowed in the parking lot.
     */
    public ParkingLot(int capacityConstraint, int truckLimit) {
        this.capacityConstraint = capacityConstraint; // Set the capacity limit
        this.truckLimit = truckLimit; // Set the truck limit for the parking lot
        ready = new Queue(truckLimit); // Initialize the ready queue
        waiting = new Queue(truckLimit); // Initialize the waiting queue
        totalTruckCount = 0; // Start total truck count at zero
        waitingTruckCount = 0; // Start waiting truck count at zero
        readyTruckCount = 0; // Start ready truck count at zero
    }

    /**
     * Adds a truck to the parking lot's waiting queue.
     * Updates the total truck count and the waiting truck count.
     *
     * @param truck The truck to be added to the parking lot.
     */
    public void addTruckPL(Truck truck) {
        waiting.enqueue(truck); // Add the truck to the waiting queue
        totalTruckCount += 1; // Increment the total truck count
        waitingTruckCount += 1; // Increment the waiting truck count
    }
}
