import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    /**
     * Creates a parking lot and adds it to the necessary trees.
     * @param treeForAddTruck Tree where trucks are added.
     * @param treeCount Tree for counting trucks.
     * @param capacityConstraint The capacity limit for the parking lot.
     * @param truckLimit The maximum trucks allowed in the lot.
     */
    public static void CreateParkingLot(Tree treeForAddTruck,Tree treeCount,int capacityConstraint,int truckLimit){
        ParkingLot parkingLot=new ParkingLot(capacityConstraint,truckLimit);
        treeForAddTruck.insertParkingLot(parkingLot);
        treeCount.insertParkingLot(parkingLot);
    }

    /**
     * Deletes a parking lot from all relevant trees.
     * @param treeForAddTruck Tree for adding trucks.
     * @param treeForReady Tree for ready trucks.
     * @param treeForLoad Tree for loaded trucks.
     * @param treeCount Tree for counting trucks.
     * @param capacityConstraint Capacity of the parking lot to delete.
     */
    public static void DeleteParkingLot(Tree treeForAddTruck,Tree treeForReady,Tree treeForLoad,Tree treeCount,int capacityConstraint){
        treeForAddTruck.delete(capacityConstraint);
        treeForReady.delete(capacityConstraint);
        treeForLoad.delete(capacityConstraint);
        treeCount.delete(capacityConstraint);
    }
    /**
     * Adds a truck to the closest available parking lot that can hold it.
     * @param treeForAddTruck Tree for adding trucks.
     * @param treeForReady Tree for ready trucks.
     * @param truckId The truck ID.
     * @param loadCapacity The load capacity of the truck.
     * @return The capacity of the parking lot where the truck was added, or -1 if no suitable lot is found.
     */
    public static int addTruck(Tree treeForAddTruck,Tree treeForReady, int truckId, int loadCapacity) {
        Truck truck = new Truck(truckId, loadCapacity);
        ParkingLotNode currentNode = treeForAddTruck.findSuccesorr(loadCapacity);
        if (currentNode==null){
            return -1; //Return -1 if no suitable parkinglot is found
        }

        currentNode.parkingOfNode.addTruckPL(truck);//add truck to parking lot

        //If current parking lot is suitable for getting ready command add it to tree for Ready command
        if (currentNode.parkingOfNode.waitingTruckCount==1){
            treeForReady.insertParkingLot(currentNode.parkingOfNode);
        }
        //If parking lot have no more truck limit to accept truck remove it from addTruck tree
        if(currentNode.parkingOfNode.totalTruckCount == currentNode.parkingOfNode.truckLimit){

            int nodetemp=currentNode.parkingOfNode.capacityConstraint;
            treeForAddTruck.delete(currentNode.parkingOfNode.capacityConstraint);
            return nodetemp;
        }

        return currentNode.parkingOfNode.capacityConstraint;//return the current parking lots capacity constraint
    }

    /**
     * Adds a truck with some load to the closest parking lot that can hold it to transfer trucks around parking lots.
     * @param treeForAddTruck Tree for adding trucks.
     * @param treeForReady Tree for ready trucks.
     * @param truckId The truck ID.
     * @param loadCapacity The total load capacity of the truck.
     * @param load The initial load on the truck.
     * @return The capacity of the parking lot where the truck was added, or -1 if no lot is available.
     */
    public static int addTruck(Tree treeForAddTruck,Tree treeForReady, int truckId,int loadCapacity,int load) {
        Truck truck = new Truck(truckId,loadCapacity, load);//initailize a new truck
        int tempCapacity=loadCapacity-load;//temp capacity is the capacity left in truck
        ParkingLotNode currentNode = treeForAddTruck.findSuccesorr(tempCapacity);
        if (currentNode==null){
            return -1;
        }

        currentNode.parkingOfNode.addTruckPL(truck);
        //if parking lot is suitable for Ready command add it to ready tree
        if (currentNode.parkingOfNode.waitingTruckCount==1){
            treeForReady.insertParkingLot(currentNode.parkingOfNode);

        }
        //if parking lot is full remove it from add truck tree
        if(currentNode.parkingOfNode.totalTruckCount == currentNode.parkingOfNode.truckLimit){

            int nodetemp=currentNode.parkingOfNode.capacityConstraint;
            treeForAddTruck.delete(currentNode.parkingOfNode.capacityConstraint);
            return nodetemp;
        }
        return currentNode.parkingOfNode.capacityConstraint;
    }

    /**
     * Moves the next truck from waiting to ready in a parking lot.
     * @param treeForReady Tree for ready trucks.
     * @param treeForLoad Tree for loaded trucks.
     * @param capacityConstraint Capacity of the parking lot to check.
     * @return Truck ID and parking lot capacity, or "-1" if no truck is found.
     */
   public static String ready(Tree treeForReady,Tree treeForLoad,int capacityConstraint) {

        ParkingLotNode parkingLot = treeForReady.findAncestor(capacityConstraint);

        if (parkingLot==null){
            return "-1";
        }
                //move the specific truck to ready line from waiting line
                Truck s=parkingLot.parkingOfNode.waiting.front();
                parkingLot.parkingOfNode.ready.enqueue(s);
                parkingLot.parkingOfNode.readyTruckCount++;
                parkingLot.parkingOfNode.waiting.dequeue();
                parkingLot.parkingOfNode.waitingTruckCount--;
                treeForLoad.insertParkingLot(parkingLot.parkingOfNode);

                //If parking lot is suitable for load command add it to treeForLoad
                if (parkingLot.parkingOfNode.readyTruckCount==1){
                    treeForLoad.insertParkingLot(parkingLot.parkingOfNode);
                }
                String temp=s.truckId+" "+parkingLot.parkingOfNode.capacityConstraint;
                //if parking lot is not suitable for ready command any more delete it from TreeForReady tree
                if (parkingLot.parkingOfNode.waitingTruckCount==0){
                    treeForReady.delete(parkingLot.parkingOfNode.capacityConstraint);
                }


                return temp; //return id of truck and capacity constraint of parking lot
    }
    /**
     * Counts the total trucks in parking lots with a capacity greater than or equal to the specified one.
     * @param treeForCount Tree for counting trucks.
     * @param capacityConstraint Minimum capacity to count from.
     * @return Total truck count in these parking lots.
     */
    public static int count(Tree treeForCount,int capacityConstraint){

        ParkingLotNode current = treeForCount.countHelper(capacityConstraint);//start counting from specialized count
        int truckCount = 0;//Initialize truck count

        //Add truckcount count of trucks till there is no suitable parking lot left
        while (current != null)
        {
            truckCount += current.parkingOfNode.totalTruckCount;
            current = treeForCount.findNextGreater(current);
        }

        return truckCount;
    }

    /**
     * Loads trucks from parking lots and transfers to other lots if needed.
     * @param treeForLoad Tree for loaded trucks.
     * @param treeForAddTruck Tree for adding trucks.
     * @param treeForReady Tree for ready trucks.
     * @param capacityConstraint Parking lot capacity constraint.
     * @param loadAmount Load amount to be assigned to trucks.
     * @return A list with truck ID and lot capacity constraint, or {-1, -1} if no trucks available.
     */
    public static ArrayList<int[]> recieveLoad(Tree treeForLoad,Tree treeForAddTruck,Tree treeForReady, int capacityConstraint, int loadAmount){
        ArrayList<int[]> array = new ArrayList<>(); // Initialize list to store results
        ParkingLotNode parkingLot = treeForLoad.findAncestor(capacityConstraint); // Find the first suitable parking lot

        // Loop while there is load to distribute and parking lots are available
        while (loadAmount > 0 && parkingLot != null) {
            // Continue while there are trucks ready in the parking lot and load remains
            while (parkingLot.parkingOfNode.readyTruckCount > 0 && loadAmount > 0) {
                int[] output = new int[2]; // Array to store truck ID and capacity constraint of the lot
                Truck k = parkingLot.parkingOfNode.ready.front(); // Access the front truck in the ready queue
                int tempId = k.truckId; // Store truck ID for output
                int tempLoadCapacity = k.loadCapacity; // Store truck's load capacity for potential redistribution
                int load = Math.min(parkingLot.parkingOfNode.capacityConstraint, loadAmount); // Assign load based on lot's capacity and remaining load
                loadAmount -= load; // Reduce the remaining load by the amount assigned
                parkingLot.parkingOfNode.ready.front().addLoad(load); // Add load to the front truck in the ready queue
                int tempLoad = k.load; // Store the new load of the truck after assignment
                parkingLot.parkingOfNode.ready.dequeue(); // Remove the truck from the ready queue
                parkingLot.parkingOfNode.readyTruckCount--; // Decrease ready truck count in the parking lot
                parkingLot.parkingOfNode.totalTruckCount--; // Decrease total truck count in the parking lot

                // If the lot has space for more trucks after the load assignment
                if (parkingLot.parkingOfNode.totalTruckCount == parkingLot.parkingOfNode.truckLimit - 1) {
                    treeForAddTruck.insertParkingLot(parkingLot.parkingOfNode); // Add the lot back to treeForAddTruck
                }

                // Try to add the truck to a new parking lot if needed and get the lot’s capacity constraint
                int s = addTruck(treeForAddTruck, treeForReady, tempId, tempLoadCapacity, tempLoad);
                output[0] = tempId; // Set truck ID in output
                output[1] = s; // Set capacity constraint of the lot the truck was added to
                array.add(output); // Add the truck and lot information to the result list
            }
            int temp = parkingLot.parkingOfNode.capacityConstraint; // Store current parking lot's capacity constraint

            // If no more ready trucks are left in the lot, remove it from treeForLoad
            if (parkingLot.parkingOfNode.readyTruckCount == 0) {
                treeForLoad.delete(parkingLot.parkingOfNode.capacityConstraint);
            }

            // Move to the next parking lot with a greater capacity constraint
            parkingLot = treeForLoad.findAncestor(temp + 1);
        }

        // If no trucks were loaded, return list containing {-1, -1}
        if (array.size() == 0) {
            array.add(new int[]{-1, -1});
            return array;
        }
        return array; // Return the result list of trucks and parking lot constraints
    }

    /**
     * Main method that processes commands from an input file to manage parking lots and trucks.
     * Reads each command, executes the corresponding operation, and writes results to an output file.
     * @param args Command-line arguments (not used here).
     * @throws IOException If there's an error with file reading or writing.
     */
    public static void main(String[] args) throws IOException {

        // Initialize trees to manage different aspects of parking lot and truck management
        Tree treeForLoad = new Tree(null); // Tree for managing loaded trucks
        Tree treeForReady = new Tree(null); // Tree for managing ready trucks
        Tree treeForAddTruck = new Tree(null); // Tree for managing trucks that need to be added
        Tree treeForCountTruck = new Tree(null); // Tree for counting trucks in parking lots

        FileWriter fw = new FileWriter("output.txt"); // Writer to output results to a file
        File file = new File("type1-large.txt"); // Input file containing commands
        Scanner scanner = new Scanner(file); // Scanner to read the input file line-by-line

        // Process each line in the input file
        while (scanner.hasNextLine()) {
            String[] array = scanner.nextLine().split(" "); // Split line into command and parameters

            // Execute commands based on the first element in the array
            if (array[0].equals("create_parking_lot")) {
                // Create a new parking lot with specified capacity and truck limit
                CreateParkingLot(treeForAddTruck, treeForCountTruck, Integer.parseInt(array[1]), Integer.parseInt(array[2]));
            } else if (array[0].equals("delete_parking_lot")) {
                // Delete a parking lot with the specified capacity constraint
                DeleteParkingLot(treeForAddTruck, treeForReady, treeForLoad, treeForCountTruck, Integer.parseInt(array[1]));

            } else if (array[0].equals("add_truck")) {
                // Add a truck with specified ID and load capacity
                int s = addTruck(treeForAddTruck, treeForReady, Integer.parseInt(array[1]), Integer.parseInt(array[2]));
                fw.write(String.format(s + "\n")); // Write the result to the output file

            } else if (array[0].equals("ready")) {
                // Move the next truck from waiting to ready in the specified parking lot
                String f = ready(treeForReady, treeForLoad, Integer.parseInt(array[1]));
                fw.write(f + "\n"); // Write the result to the output file

            } else if (array[0].equals("count")) {
                // Count the number of trucks in parking lots with capacity greater than or equal to the specified limit
                int k = count(treeForCountTruck, Integer.parseInt(array[1]));
                fw.write(k + "\n"); // Write the result to the output file

            } else if (array[0].equals("load")) {
                // Distribute load among trucks in parking lots and move them if necessary
                ArrayList<int[]> z = recieveLoad(treeForLoad, treeForAddTruck, treeForReady, Integer.parseInt(array[1]), Integer.parseInt(array[2]));

                // Process the list of results and write them to the output file
                for (int[] i : z) {
                    if (i[0] == -1 && i[1] == -1) {
                        fw.write("-1"); // Write -1 if no trucks were loaded
                        break;
                    } else {
                        fw.write(i[0] + " " + i[1]); // Write truck ID and parking lot capacity constraint
                    }

                    // Add a separator if there are multiple results
                    if (z.size() > 1 && !i.equals(z.get(z.size() - 1))) {
                        fw.write(" - ");
                    }
                }
                fw.write("\n"); // New line after each command
            }
        }
        fw.close(); // Close the output file writer after all commands are processed
    }
}
