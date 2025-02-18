import java.util.ArrayList;

public class MyPriorityQueue {
    private ArrayList<Node> heap;

    public MyPriorityQueue() {
        heap = new ArrayList<>();
    }

    public void add(Node node) {
        heap.add(node);
        int currentIndex = heap.size() - 1;
        // Bubble up
        while (currentIndex > 0) {
            int parentIndex = (currentIndex - 1) / 2;
            if (heap.get(currentIndex).distance >= heap.get(parentIndex).distance) {
                break;
            }
            // Swap with parent
            Node temp = heap.get(parentIndex);
            heap.set(parentIndex, heap.get(currentIndex));
            heap.set(currentIndex, temp);
            currentIndex = parentIndex;
        }
    }

    public Node poll() {
        if (heap.isEmpty()) return null;
        Node root = heap.get(0);
        Node lastNode = heap.remove(heap.size() - 1);
        if (!heap.isEmpty()) {
            heap.set(0, lastNode);
            heapify(0);
        }
        return root;
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    private void heapify(int index) {
        int smallest = index;
        int leftChild = 2 * index + 1;
        int rightChild = 2 * index + 2;

        if (leftChild < heap.size() && heap.get(leftChild).distance < heap.get(smallest).distance) {
            smallest = leftChild;
        }

        if (rightChild < heap.size() && heap.get(rightChild).distance < heap.get(smallest).distance) {
            smallest = rightChild;
        }

        if (smallest != index) {
            Node temp = heap.get(index);
            heap.set(index, heap.get(smallest));
            heap.set(smallest, temp);
            heapify(smallest);
        }
    }
}
