import java.util.ArrayList;

/**
 * A custom implementation of a HashMap using an array of buckets and separate chaining for collision handling.
 * Supports basic operations like put, get, remove, and resize.
 *
 * @param <K> The type of keys maintained by this map.
 * @param <V> The type of mapped values.
 */
public class CustomHashMap<K, V> {

    private static final int INITIAL_CAPACITY = 16; // Initial capacity of the HashMap
    private static final float LOAD_FACTOR = 0.75f; // Load factor for resizing

    private ArrayList<Entry<K, V>>[] buckets; // Array of buckets to store entries
    private int size; // Current size of the HashMap

    /**
     * Constructs a new CustomHashMap with default initial capacity and load factor.
     */
    public CustomHashMap() {
        buckets = new ArrayList[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Represents a key-value pair stored in the HashMap.
     */
    private static class Entry<K, V> {
        K key;
        V value;

        /**
         * Constructs an entry with the specified key and value.
         *
         * @param key   The key for the entry.
         * @param value The value for the entry.
         */
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * Computes the bucket index for the given key.
     *
     * @param key The key for which the index is to be calculated.
     * @return The bucket index for the key.
     */
    private int getBucketIndex(K key) {
        int hashCode = (key == null) ? 0 : key.hashCode();
        return Math.abs(hashCode) % buckets.length;
    }


    /**
     * Adds or updates a key-value pair in the HashMap.
     *
     * @param key   The key to add or update.
     * @param value The value to associate with the key.
     * @return 0 if the entry is added successfully, or -1 if the key already exists.
     */
    public int put(K key, V value) {
        int index = getBucketIndex(key);

        if (buckets[index] == null) {
            buckets[index] = new ArrayList<>();
        }

        for (Entry<K, V> entry : buckets[index]) {
            if ((key == null && entry.key == null) || (key != null && key.equals(entry.key))) {
                entry.value=value;
                return 0; // Key already exists
            }
        }

        buckets[index].add(new Entry<>(key, value));
        size++;

        // Resize if load factor is exceeded
        if ((float) size / buckets.length > LOAD_FACTOR) {
            resize();
        }
        return 0;
    }



    /**
     * Retrieves a value associated with a key.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the key, or null if the key is not found.
     */
    public V get(K key) {
        int index = getBucketIndex(key);

        if (buckets[index] != null) {
            for (Entry<K, V> entry : buckets[index]) {
                if ((key == null && entry.key == null) || (key != null && key.equals(entry.key))) {
                    return entry.value;
                }
            }
        }

        return null; // Key not found
    }

    /**
     * Removes a key-value pair from the HashMap.
     *
     * @param key The key to remove.
     * @return The value associated with the removed key, or null if the key is not found.
     */
    public V remove(K key) {
        int index = getBucketIndex(key);

        if (buckets[index] != null) {
            for (Entry<K, V> entry : buckets[index]) {
                if ((key == null && entry.key == null) || (key != null && key.equals(entry.key))) {
                    V value = entry.value;
                    buckets[index].remove(entry);
                    size--;
                    return value;
                }
            }
        }

        return null; // Key not found
    }

    /**
     * Checks if the map contains a given key.
     *
     * @param key The key to check.
     * @return true if the key exists in the map, false otherwise.
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Returns the current size of the HashMap.
     *
     * @return The number of key-value pairs in the map.
     */
    public int size() {
        return size;
    }

    /**
     * Resizes the HashMap when the load factor is exceeded.
     */
    private void resize() {
        ArrayList<Entry<K, V>>[] oldBuckets = buckets;
        buckets = new ArrayList[buckets.length * 2];
        size = 0;

        for (ArrayList<Entry<K, V>> bucket : oldBuckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    put(entry.key, entry.value);
                }
            }
        }
    }



    public ArrayList<V> values() {
        ArrayList<V> values = new ArrayList<>();
        for (ArrayList<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    values.add(entry.value);
                }
            }
        }
        return values;
    }

    public ArrayList<K> keys() {
        ArrayList<K> keys = new ArrayList<>();
        for (ArrayList<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    keys.add(entry.key);
                }
            }
        }
        return keys;
    }


    /**
     * Returns a string representation of the map.
     *
     * @return A string representing the key-value pairs in the map.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        boolean firstEntry = true;

        for (ArrayList<Entry<K, V>> bucket : buckets) {
            if (bucket != null) {
                for (Entry<K, V> entry : bucket) {
                    if (!firstEntry) {
                        sb.append(", ");
                    }
                    sb.append(entry.key).append("=").append(entry.value);
                    firstEntry = false;
                }
            }
        }

        sb.append("}");
        return sb.toString();
    }
}
