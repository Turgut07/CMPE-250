/**
 * Tree class that represents an AVL Tree structure for managing parking lots.
 * Supports insertion, deletion, balancing, and finding successor/ancestor nodes.
 */
public class Tree {
    ParkingLotNode root; // Root node of the tree
    int nodeCount; // Keeps track of the number of nodes in the tree

    /**
     * Constructor that initializes the tree with a given root node.
     * @param root Initial root node of the tree.
     */
    public Tree(ParkingLotNode root) {
        this.root = root;
    }

    /**
     * Inserts a parking lot into the AVL tree.
     * @param parkingLot The parking lot to be inserted.
     */
    public void insertParkingLot(ParkingLot parkingLot) {
        this.root = insertHelper(root, parkingLot);
    }

    /**
     * Rotates the subtree to the right to maintain balance.
     * @param node The root node of the subtree to rotate.
     * @return The new root node after rotation.
     */
    public ParkingLotNode rotateRight(ParkingLotNode node) {
        ParkingLotNode x = node.leftChild; // Left child of the node
        ParkingLotNode sec = x.rightChild; // Right child of x
        x.rightChild = node; // Make x the new root
        node.leftChild = sec; // Update left child of the original root

        // Update heights of nodes after rotation
        node.height = 1 + Math.max(getHeight(node.leftChild), getHeight(node.rightChild));
        x.height = 1 + Math.max(getHeight(x.leftChild), getHeight(x.rightChild));
        return x; // Return new root
    }

    /**
     * Rotates the subtree to the left to maintain balance.
     * @param node The root node of the subtree to rotate.
     * @return The new root node after rotation.
     */
    public ParkingLotNode rotateLeft(ParkingLotNode node) {
        ParkingLotNode y = node.rightChild; // Right child of the node
        ParkingLotNode sec = y.leftChild; // Left child of y
        y.leftChild = node; // Make y the new root
        node.rightChild = sec; // Update right child of the original root

        // Update heights of nodes after rotation
        node.height = 1 + Math.max(getHeight(node.leftChild), getHeight(node.rightChild));
        y.height = 1 + Math.max(getHeight(y.leftChild), getHeight(y.rightChild));
        return y; // Return new root
    }

    /**
     * Gets the balance factor of a node.
     * @param node The node to calculate balance for.
     * @return The balance factor, positive if left-heavy, negative if right-heavy.
     */
    public int getBalance(ParkingLotNode node) {
        return getHeight(node.leftChild) - getHeight(node.rightChild); // Balance is left height minus right height
    }

    /**
     * Helper function to insert a parking lot into the AVL tree and balance it if needed.
     * @param node Current node to check.
     * @param parkingLot The parking lot to be inserted.
     * @return The updated root node after insertion and balancing.
     */
    public ParkingLotNode insertHelper(ParkingLotNode node, ParkingLot parkingLot) {
        if (node == null) { // Base case: Insert at the empty position
            nodeCount++;
            return new ParkingLotNode(parkingLot); // New node with the parking lot
        }

        // Recursive insertion based on capacity constraint
        if (parkingLot.capacityConstraint < node.parkingOfNode.capacityConstraint) {
            node.leftChild = insertHelper(node.leftChild, parkingLot);
        } else if (parkingLot.capacityConstraint > node.parkingOfNode.capacityConstraint) {
            node.rightChild = insertHelper(node.rightChild, parkingLot);
        } else {
            return node; // Duplicate capacity constraint, do nothing
        }

        // Update the height of the node
        node.height = 1 + Math.max(getHeight(node.leftChild), getHeight(node.rightChild));

        // Get the balance factor to check if node needs balancing
        int balance = getBalance(node);

        // Perform rotations based on balance factor
        if (balance > 1 && parkingLot.capacityConstraint < node.leftChild.parkingOfNode.capacityConstraint) {
            return rotateRight(node); // Left-left case
        }
        if (balance < -1 && parkingLot.capacityConstraint > node.rightChild.parkingOfNode.capacityConstraint) {
            return rotateLeft(node); // Right-right case
        }
        if (balance > 1 && parkingLot.capacityConstraint > node.leftChild.parkingOfNode.capacityConstraint) {
            node.leftChild = rotateLeft(node.leftChild); // Left-right case
            return rotateRight(node);
        }
        if (balance < -1 && parkingLot.capacityConstraint < node.rightChild.parkingOfNode.capacityConstraint) {
            node.rightChild = rotateRight(node.rightChild); // Right-left case
            return rotateLeft(node);
        }
        return node; // Return balanced node
    }

    /**
     * Gets the height of a node.
     * @param node Node to get the height of.
     * @return Height of the node, or -1 if node is null.
     */
    public int getHeight(ParkingLotNode node) {
        return (node == null) ? -1 : node.height;
    }

    /**
     * Deletes a node with a given capacity constraint from the tree.
     * @param capacityConstraint Capacity of the parking lot to delete.
     */
    public void delete(int capacityConstraint) {
        root = deleteHelper(root, capacityConstraint);
    }

    /**
     * Helper function to delete a node from the AVL tree and balance it if needed.
     * @param node Current node in the recursion.
     * @param capacityConstraint Capacity constraint of the node to delete.
     * @return The updated node after deletion and balancing.
     */
    public ParkingLotNode deleteHelper(ParkingLotNode node, int capacityConstraint) {
        if (node == null) {
            return node; // Node not found, end recursion
        }

        // Recursively search for the node to delete
        if (capacityConstraint < node.parkingOfNode.capacityConstraint) {
            node.leftChild = deleteHelper(node.leftChild, capacityConstraint);
        } else if (capacityConstraint > node.parkingOfNode.capacityConstraint) {
            node.rightChild = deleteHelper(node.rightChild, capacityConstraint);
        } else {
            // Node found, delete it
            if (node.leftChild == null && node.rightChild == null) {
                node = null; // Node has no children
            } else if (node.leftChild == null) {
                node = node.rightChild; // Node has only right child
            } else if (node.rightChild == null) {
                node = node.leftChild; // Node has only left child
            } else {
                // Node has two children, find the successor
                ParkingLotNode temp = Helper(node.rightChild);
                node.parkingOfNode = temp.parkingOfNode; // Replace with successor
                node.rightChild = deleteHelper(node.rightChild, temp.parkingOfNode.capacityConstraint);
            }
            nodeCount--; // Decrement the node count
        }

        if (node == null) {
            return node; // No need to balance an empty node
        }

        // Update height and balance the node if necessary
        node.height = 1 + Math.max(getHeight(node.leftChild), getHeight(node.rightChild));
        int balance = getBalance(node);

        // Balance the node if required
        if (balance > 1 && getBalance(node.leftChild) >= 0) {
            return rotateRight(node);
        }
        if (balance < -1 && getBalance(node.rightChild) <= 0) {
            return rotateLeft(node);
        }
        if (balance > 1 && getBalance(node.leftChild) < 0) {
            node.leftChild = rotateLeft(node.leftChild);
            return rotateRight(node);
        }
        if (balance < -1 && getBalance(node.rightChild) > 0) {
            node.rightChild = rotateRight(node.rightChild);
            return rotateLeft(node);
        }
        return node; // Return the balanced node
    }

    /**
     * Finds the left-most node in the subtree.
     * Used to find the in-order successor for deletion.
     * @param node Subtree root to search.
     * @return The left-most (smallest) node in the subtree.
     */
    public ParkingLotNode Helper(ParkingLotNode node) {
        ParkingLotNode temp = node;
        while (temp.leftChild != null) {
            temp = temp.leftChild;
        }
        return temp; // Return the smallest node
    }

    /**
     * Finds the parking lot with the closest capacity greater than or equal to the specified value.
     * @param capacityConstraint Capacity constraint to search for.
     * @return The closest matching node, or null if none found.
     */
    public ParkingLotNode findSuccesorr(int capacityConstraint) {
        ParkingLotNode currentNode = root;
        ParkingLotNode resultNode = null;

        while (currentNode != null) {
            if (capacityConstraint >= currentNode.parkingOfNode.capacityConstraint) {
                resultNode = currentNode; // Found a potential successor
                currentNode = currentNode.rightChild; // Move right for larger capacity
            } else {
                currentNode = currentNode.leftChild; // Move left to find smaller capacity
            }
        }
        return resultNode; // Return closest match
    }

    /**
     * Finds the parking lot with a capacity closest to or greater than the specified constraint.
     * Used to locate a suitable parking lot for specific operations.
     * @param capacityConstraint The minimum capacity constraint for the parking lot.
     * @return The node representing the suitable parking lot, or null if none found.
     */
    public ParkingLotNode findAncestor(int capacityConstraint) {
        ParkingLotNode currentNode = root;
        ParkingLotNode resultNode = null;

        // Traverse the tree to find the appropriate parking lot node
        while (currentNode != null) {
            if (capacityConstraint <= currentNode.parkingOfNode.capacityConstraint) {
                resultNode = currentNode; // Found a suitable parking lot
                currentNode = currentNode.leftChild; // Move left to find a smaller capacity constraint
            } else {
                currentNode = currentNode.rightChild; // Move right to check larger capacities
            }
        }
        return resultNode; // Return the closest matching parking lot node
    }

    /**
     * Helper function that assists in finding the next greater node in the tree
     * with a capacity greater than the specified constraint.
     * @param capacityConstraint The capacity constraint to search for.
     * @return The node representing the next greater parking lot, or null if none found.
     */
    public ParkingLotNode countHelper(int capacityConstraint) {
        ParkingLotNode current = root;
        ParkingLotNode nextGreater = null;

        // Traverse the tree to locate the next greater node
        while (current != null) {
            if (capacityConstraint < current.parkingOfNode.capacityConstraint) {
                nextGreater = current; // Potential next greater node
                current = current.leftChild; // Move left to find a smaller constraint
            } else {
                current = current.rightChild; // Move right to find a larger constraint
            }
        }
        return nextGreater; // Return the next greater node found
    }

    /**
     * Finds the next greater node in the AVL tree.
     * Used to navigate to nodes with capacities greater than a specified node.
     * @param node The reference node from which the search begins.
     * @return The next greater node, or null if none exists.
     */
    public ParkingLotNode findNextGreater(ParkingLotNode node) {
        if (node == null) {
            return null;
        }

        // If the node has a right child, the next greater node is the left-most node of the right subtree
        if (node.rightChild != null) {
            ParkingLotNode current = node.rightChild;
            while (current.leftChild != null) {
                current = current.leftChild;
            }
            return current;
        }

        // Traverse the tree to locate the next greater node
        ParkingLotNode successor = null;
        ParkingLotNode current = root;

        while (current != null) {
            if (node.parkingOfNode.capacityConstraint < current.parkingOfNode.capacityConstraint) {
                successor = current; // Update successor if it's greater than the reference node
                current = current.leftChild; // Move left to find a smaller constraint
            } else if (node.parkingOfNode.capacityConstraint > current.parkingOfNode.capacityConstraint) {
                current = current.rightChild; // Move right for larger constraints
            } else {
                break;
            }
        }
        return successor; // Return the successor node if found
    }

}
