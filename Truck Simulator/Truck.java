/**
 * Truck class representing a truck with a unique ID, load capacity, and current load.
 * Each truck can have a specific capacity constraint, and the load can be updated
 * with the addLoad method.
 */
public class Truck {
    int truckId; // Unique ID of the truck
    int load; // Current load of the truck
    int loadCapacity; // Maximum load capacity of the truck
    int CapacityConstraint; // Specific capacity constraint for the truck

    /**
     * Constructor to initialize a Truck with an ID and load capacity.
     * Sets the initial load to 0.
     *
     * @param truckId Unique ID of the truck.
     * @param loadCapacity Maximum load capacity of the truck.
     */
    public Truck(int truckId, int loadCapacity) {
        this.truckId = truckId; // Set the truck ID
        this.loadCapacity = loadCapacity; // Set the truck's load capacity
    }

    /**
     * Overloaded constructor to initialize a Truck with an ID, load capacity, and initial load.
     *
     * @param truckId Unique ID of the truck.
     * @param loadCapacity Maximum load capacity of the truck.
     * @param load Initial load of the truck.
     */
    public Truck(int truckId, int loadCapacity, int load) {
        this.truckId = truckId; // Set the truck ID
        this.loadCapacity = loadCapacity; // Set the truck's load capacity
        this.load = load; // Set the truck's initial load
    }

    /**
     * Adds a specified load to the truck.
     * If the added load equals the truck's capacity, resets load to 0.
     *
     * @param addLoad Amount of load to add to the truck.
     */
    public void addLoad(int addLoad) {
        this.load += addLoad; // Add the specified load to the current load
        if (this.load == loadCapacity) { // Check if the load reaches the capacity
            load = 0; // Reset load to 0 if full capacity is reached
        }
    }
}
