package nl.revolution.codechallenge;

/**
 * Simple key value pair.
 * @param <K> Generic type for the key.
 * @param <V> Generic type for the value.
 */
public class KeyValuePair<K, V> {

    private final K key;
    private final V value;

    public KeyValuePair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K key() {
        return key;
    }

    public V value() {
        return value;
    }

}
