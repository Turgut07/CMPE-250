/**
 * Node class for representing a parking lot in the AVL Tree.
 * Each node contains references to its left and right children, its height in the tree,
 * and a ParkingLot object that holds information about the parking lot itself.
 */
public class ParkingLotNode {
    ParkingLotNode leftChild; // Reference to the left child node
    ParkingLotNode rightChild; // Reference to the right child node
    int height; // Height of the node in the AVL tree
    ParkingLot parkingOfNode; // Parking lot data stored in this node

    /**
     * Constructor that initializes a ParkingLotNode with a given ParkingLot object.
     * Sets the initial height to 0 and left and right children to null.
     * @param pl The parking lot object to be associated with this node.
     */
    public ParkingLotNode(ParkingLot pl) {
        this.parkingOfNode = pl; // Set the parking lot information
        this.rightChild = null; // Initialize right child as null
        this.leftChild = null; // Initialize left child as null
        this.height = 0; // Start height at 0 since it's a leaf node initially
    }
}
