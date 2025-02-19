import java.util.ArrayList;

/**
 * A utility class for sorting an ArrayList of Post objects using the QuickSort algorithm.
 */
public class SortArrayList {

    /**
     * Sorts the given ArrayList of Post objects in descending order based on the compareTo method in the Post class.
     *
     * @param list The ArrayList of Post objects to be sorted.
     */
    public static void quickSort(ArrayList<Post> list) {
        quickSort(list, 0, list.size() - 1);
    }

    /**
     * Recursive implementation of the QuickSort algorithm.
     *
     * @param list The ArrayList of Post objects to be sorted.
     * @param low  The starting index of the sublist to be sorted.
     * @param high The ending index of the sublist to be sorted.
     */
    private static void quickSort(ArrayList<Post> list, int low, int high) {
        if (low < high) {
            // Partition the array and get the pivot index
            int pi = partition(list, low, high);

            // Recursively sort the sublists before and after the pivot
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    /**
     * Partitions the ArrayList around a pivot element such that all elements greater than or equal to the pivot
     * are on the left, and all elements less than the pivot are on the right.
     *
     * @param list The ArrayList of Post objects to be partitioned.
     * @param low  The starting index of the sublist to be partitioned.
     * @param high The ending index of the sublist to be partitioned.
     * @return The index of the pivot element after partitioning.
     */
    private static int partition(ArrayList<Post> list, int low, int high) {
        // Choose the last element as the pivot
        Post pivot = list.get(high);
        int i = low - 1; // Index of smaller element

        // Iterate through the sublist and rearrange elements based on the pivot
        for (int j = low; j < high; j++) {
            // Compare elements using the compareTo method of the Post class
            if (list.get(j).compareTo(pivot) <= 0) { // Adjust condition for descending order
                i++;
                swap(list, i, j);
            }
        }

        // Place the pivot element in the correct position
        swap(list, i + 1, high);
        return i + 1;
    }

    /**
     * Swaps two elements in the given ArrayList.
     *
     * @param list The ArrayList of Post objects.
     * @param i    The index of the first element to be swapped.
     * @param j    The index of the second element to be swapped.
     */
    private static void swap(ArrayList<Post> list, int i, int j) {
        Post temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }
}
