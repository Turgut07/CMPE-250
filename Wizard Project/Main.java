
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    /**
     * Finds the shortest path between two nodes using Dijkstra's algorithm.
     *
     * @param start       The starting node of the path.
     * @param end         The destination node of the path.
     * @param NodeHashMap The custom HashMap containing all nodes.
     * @return An ArrayList representing the shortest path from start to end, or an empty list if no path is found.
     */
    public static ArrayList<Node> findShortestPath(Node start, Node end, CustomHashMap NodeHashMap) {
        MyPriorityQueue priorityQueue = new MyPriorityQueue(); // Priority queue to process nodes by shortest distance
        CustomHashMap<Node, Double> distances = new CustomHashMap<>(); // Map to store distances to each node
        CustomHashMap<Node, Node> previousNodes = new CustomHashMap<>(); // Map to track the previous node for path reconstruction
        ArrayList<Node> shortestPath = new ArrayList<>(); // The resulting shortest path

        // Initialize the starting node's distance
        distances.put(start, 0.0);
        priorityQueue.add(start);

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();

            // If the destination is reached, reconstruct the path
            if (current.equals(end)) {
                Node temp = end;
                while (temp != null) {
                    shortestPath.add(0, temp); // Add nodes to the path in reverse order
                    temp = previousNodes.get(temp);
                }
                return shortestPath;
            }

            // Update distances for neighbors
            for (String edgeKey : current.edges.keys()) {
                Edge edge = current.edges.get(edgeKey);
                Node neighbor = getNodeFromEdge(current, edge, NodeHashMap);

                if (neighbor == null) continue; // Skip if the neighbor does not exist
                if (neighbor.nodeInfo == 1) continue; // Skip if the neighbor is impassable

                double newDist = distances.get(current) + edge.length; // Calculate new distance

                // Update the distance if it's shorter
                if (distances.get(neighbor) == null || newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    previousNodes.put(neighbor, current);
                    priorityQueue.add(new Node(neighbor.x, neighbor.y, neighbor.nodeInfo, newDist, neighbor.edges));
                }
            }
        }

        return shortestPath; // Return empty path if no path is found
    }

    /**
     * Determines the neighbor node from an edge and the current node.
     *
     * @param current     The current node.
     * @param edge        The edge connected to the current node.
     * @param nodeHashMap The HashMap containing all nodes.
     * @return The neighbor node connected by the edge, or null if not found.
     */
    private static Node getNodeFromEdge(Node current, Edge edge, CustomHashMap<String, Node> nodeHashMap) {
        String neighborKey;

        // Determine which node is the neighbor based on edge endpoints
        if (current.x == edge.x0 && current.y == edge.y0) {
            neighborKey = edge.x1 + " " + edge.y1;
        } else {
            neighborKey = edge.x0 + " " + edge.y0;
        }

        return nodeHashMap.get(neighborKey); // Return the neighbor node
    }

    /**
     * Walks along a given path and handles obstacles or impassable nodes within a radius.
     *
     * @param radius      The radius to check for impassable nodes.
     * @param path        The path to follow.
     * @param nodeHashMap The HashMap containing all nodes.
     * @param start       The starting node.
     * @param fw          The FileWriter to write the output.
     * @return The last valid node before encountering an impassable node, or null if the path is passable.
     * @throws IOException If writing to the file fails.
     */
    public static Node pathwalk(int radius, ArrayList<Node> path, CustomHashMap<String, Node> nodeHashMap, Node start, FileWriter fw) throws IOException {
        for (Node currentNode : path) {
            if (!currentNode.equals(path.get(0))) {
                fw.write("Moving to " + currentNode.x + "-" + currentNode.y + "\n"); // Log movement
            }

            ArrayList<Node> nodesInRadius = new ArrayList<>();

            // Calculate the bounds of the radius
            int minX = currentNode.x - radius;
            int maxX = currentNode.x + radius;
            int minY = currentNode.y - radius;
            int maxY = currentNode.y + radius;

            // Find all nodes within the radius
            for (int i = minX; i <= maxX; i++) {
                for (int j = minY; j <= maxY; j++) {
                    Node candidate = nodeHashMap.get(i + " " + j);
                    if (candidate == null) continue;

                    double dx = candidate.x - currentNode.x;
                    double dy = candidate.y - currentNode.y;
                    double dist = Math.sqrt(dx * dx + dy * dy);

                    if (dist <= radius) {
                        nodesInRadius.add(candidate);
                    }
                }
            }

            boolean impassable = false;

            // Check for impassable nodes in the radius
            for (Node node : nodesInRadius) {
                if (node.nodeInfo >= 2) {
                    node.nodeInfo = 1; // Mark the node as impassable

                    if (path.contains(node)) {
                        impassable = true;
                    }
                }
            }

            if (impassable) {
                fw.write("Path is impassable!\n");
                return currentNode; // Return the last valid node
            }
        }

        return null; // Path is passable
    }

    /**
     * Finds the shortest distance between two nodes.
     *
     * @param start       The starting node.
     * @param end         The destination node.
     * @param NodeHashMap The HashMap containing all nodes.
     * @return The shortest distance between the nodes, or -1 if no path is found.
     */
    public static double findShortestPathDistance(Node start, Node end, CustomHashMap<String, Node> NodeHashMap) {
        MyPriorityQueue priorityQueue = new MyPriorityQueue();
        CustomHashMap<Node, Double> distances = new CustomHashMap<>();

        // Initialize distances
        distances.put(start, 0.0);
        priorityQueue.add(start);

        while (!priorityQueue.isEmpty()) {
            Node current = priorityQueue.poll();

            // Return the distance if the destination is reached
            if (current.equals(end)) {
                return distances.get(current);
            }

            // Update distances to neighbors
            for (String edgeKey : current.edges.keys()) {
                Edge edge = current.edges.get(edgeKey);
                Node neighbor = getNodeFromEdge(current, edge, NodeHashMap);

                if (neighbor == null) continue; // Skip if the neighbor does not exist
                if (neighbor.nodeInfo == 1) continue; // Skip if the neighbor is impassable

                double newDist = distances.get(current) + edge.length; // Calculate new distance

                // Update the distance if it's shorter
                if (distances.get(neighbor) == null || newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    priorityQueue.add(new Node(neighbor.x, neighbor.y, neighbor.nodeInfo, newDist, neighbor.edges));
                }
            }
        }

        return -1; // Return -1 if no path is found
    }

    /**
     * Temporarily changes nodeInfo values for nodes with a specific nodeData value.
     *
     * @param nodeHashMap    The HashMap containing all nodes.
     * @param targetNodeInfo The nodeData value to search for.
     * @return A list of nodes whose nodeInfo values were changed.
     */
    public static ArrayList<Node> changeNodeInfos(CustomHashMap<String, Node> nodeHashMap, int targetNodeInfo) {
        ArrayList<Node> changedNodes = new ArrayList<>();

        for (String key : nodeHashMap.keys()) {
            Node node = nodeHashMap.get(key);
            if (node != null && node.nodeData == targetNodeInfo) {
                if (node.nodeInfo != 1) {
                    changedNodes.add(node);
                    node.nodeInfo = 0;
                } else {
                    node.revelad = true;
                    node.nodeInfo = 0;
                    changedNodes.add(node);
                }
            }
        }

        return changedNodes; // Return the list of changed nodes
    }

    /**
     * Restores the original nodeInfo values for a list of nodes.
     *
     * @param changedNodes  The list of nodes whose values were changed.
     * @param originalValue The original value to restore.
     */
    public static void restoreNodeInfos(ArrayList<Node> changedNodes, int originalValue) {
        for (Node node : changedNodes) {
            if (node.revelad) {
                node.nodeInfo = 1;
            } else {
                node.nodeInfo = originalValue;
                node.nodeData = originalValue;
            }
        }
    }

    /**
     * Chooses the best option from a list based on the shortest path.
     *
     * @param options           The list of options to evaluate.
     * @param start             The starting node.
     * @param end               The destination node.
     * @param nodeCustomHashMap The HashMap containing all nodes.
     * @return The chosen option.
     */
    public static int chooseHelp(ArrayList<Integer> options, Node start, Node end, CustomHashMap<String, Node> nodeCustomHashMap) {
        double shortestDistance = Double.MAX_VALUE;
        int chosenOption = 0;

        for (int option : options) {
            ArrayList<Node> changedNodes = changeNodeInfos(nodeCustomHashMap, option);
            double distance = findShortestPathDistance(start, end, nodeCustomHashMap);

            if (distance < shortestDistance) {
                shortestDistance = distance;
                chosenOption = option;
            }

            restoreNodeInfos(changedNodes, option); // Restore nodeInfo values
        }

        changeNodeInfos(nodeCustomHashMap, chosenOption);
        changeNodeDatas(nodeCustomHashMap, chosenOption, 0);

        return chosenOption;
    }

    /**
     * Temporarily changes the nodeData values for nodes with a specific nodeData value.
     *
     * @param nodeHashMap    The HashMap containing all nodes.
     * @param targetNodeInfo The nodeData value to search for.
     * @param temporaryValue The value to temporarily set for the matching nodes.
     * @return A list of nodes whose nodeData values were changed.
     */
    public static ArrayList<Node> changeNodeDatas(CustomHashMap<String, Node> nodeHashMap, int targetNodeInfo, int temporaryValue) {
        ArrayList<Node> changedNodes = new ArrayList<>();

        // Traverse all nodes in the nodeHashMap
        for (String key : nodeHashMap.keys()) {
            Node node = nodeHashMap.get(key);
            if (node != null && node.nodeData == targetNodeInfo) {
                // Add to changed nodes list to revert later
                changedNodes.add(node);
                // Temporarily change the nodeData value
                node.nodeData = temporaryValue;
            }
        }
        return changedNodes; // Return the list of nodes that were changed
    }

    public static void main(String[] args) throws IOException, FileNotFoundException {
        // Initialize timing and output file
        float time = System.nanoTime();
        File file = new File(args[3]);
        FileWriter fw = new FileWriter(file);


        // Load input files from arguments
        File file1 = new File(args[1]); // Edges file
        File file2 = new File(args[0]); // Nodes file
        File file3 = new File(args[2]); // Objectives file
        Scanner scanner1 = new Scanner(file1);
        Scanner scanner2 = new Scanner(file2);
        Scanner scanner3 = new Scanner(file3);
        Scanner scanner4 = new Scanner(file3); // For a second scan of the objectives file

        // HashMap to store all nodes
        CustomHashMap<String, Node> NodeHashMap = new CustomHashMap();

        // Read nodes from the file
        scanner2.nextLine(); // Skip header
        while (scanner2.hasNextLine()) {
            String[] split = scanner2.nextLine().split(" ");
            Node node = new Node(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]), 0);
            NodeHashMap.put(split[0] + " " + split[1], node);
        }

        // Read edges and connect nodes
        while (scanner1.hasNextLine()) {
            String[] split = scanner1.nextLine().split(" ");
            String[] splitNodes = split[0].split(",");
            String[] splitallnodes = splitNodes[0].split("-");
            String[] splitallnodesadd = splitNodes[1].split("-");
            Edge edge = new Edge(Double.parseDouble(split[1]),
                    Integer.parseInt(splitallnodesadd[0]), Integer.parseInt(splitallnodesadd[1]),
                    Integer.parseInt(splitallnodes[0]), Integer.parseInt(splitallnodes[1]));

            // Add edges to nodes in the HashMap
            NodeHashMap.get(splitallnodesadd[0] + " " + splitallnodesadd[1]).edges.put(splitallnodes[0] + " " + splitallnodes[1], edge);
            NodeHashMap.get(splitallnodes[0] + " " + splitallnodes[1]).edges.put(splitallnodesadd[0] + " " + splitallnodesadd[1], edge);
        }

        // Read the radius for pathwalk and start position
        int radius = Integer.parseInt(scanner3.nextLine());
        scanner4.nextLine(); // Skip header
        String[] pather = scanner3.nextLine().split(" ");
        scanner4.nextLine(); // Skip the same header for the second scanner
        Node start = NodeHashMap.get(pather[0] + " " + pather[1]); // Starting node
        ArrayList<Integer> alreadyChosen = new ArrayList<>(); // Track already chosen options

        // Reveal impassable nodes within the radius of the starting node
        int minX = start.x - radius;
        int maxX = start.x + radius;
        int minY = start.y - radius;
        int maxY = start.y + radius;
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                Node candidate = NodeHashMap.get(i + " " + j);
                if (candidate == null) continue;

                double dx = candidate.x - start.x;
                double dy = candidate.y - start.y;
                double dist = Math.sqrt(dx * dx + dy * dy);

                if (dist <= radius && candidate.nodeInfo >= 2) {
                    candidate.nodeInfo = 1; // Mark as impassable
                }
            }
        }

        // Process objectives
        int obj = 1; // Objective counter
        scanner4.nextLine(); // Skip header
        while (scanner3.hasNextLine()) {
            String[] patherEnd = scanner3.nextLine().split(" "); // Target node for this objective
            Node end = NodeHashMap.get(patherEnd[0] + " " + patherEnd[1]);
            Node secretEndNode = null;

            // Check if there is an alternate secret end node
            if (scanner3.hasNextLine()) {
                String[] secretEnd = scanner4.nextLine().split(" ");
                secretEndNode = NodeHashMap.get(secretEnd[0] + " " + secretEnd[1]);
            }

            ArrayList<Integer> options = new ArrayList<>(); // Options for decision-making

            // Keep trying to walk the path until it's passable
            boolean passable = false;
            while (!passable) {
                ArrayList<Node> path = findShortestPath(start, end, NodeHashMap); // Find the shortest path
                start = pathwalk(radius, path, NodeHashMap, start, fw); // Walk the path and check impassable nodes
                if (start == null) {
                    start = end; // If the path is completed, update start to end
                    passable = true; // Mark the path as passable
                    break;
                }
            }

            fw.write("Objective " + obj + " reached!\n"); // Write objective completion
            obj++;

            // Process additional options (if any)
            if (patherEnd.length > 2) {
                for (int i = 2; i < patherEnd.length; i++) {
                    if (!alreadyChosen.contains(Integer.parseInt(patherEnd[i]))) {
                        options.add(Integer.parseInt(patherEnd[i])); // Add unchosen options
                    }
                }

                // Choose the best option
                int k = chooseHelp(options, start, secretEndNode, NodeHashMap);
                changeNodeInfos(NodeHashMap, k); // Update node infos based on the choice
                changeNodeDatas(NodeHashMap, k, 0); // Update node data
                if (k != 0) {
                    alreadyChosen.add(k); // Mark the option as chosen
                    fw.write("Number " + k + " is chosen!\n"); // Write chosen option
                }
            }
        }

        // Close the file writer and print execution time
        fw.close();
    }

}
