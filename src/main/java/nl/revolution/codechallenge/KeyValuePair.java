package nl.revolution.codechallenge;

public class KeyValuePair<F, S> {

    private final F key;
    private final S value;

    public KeyValuePair(F key, S value) {
        this.key = key;
        this.value = value;
    }

    public F key() {
        return key;
    }

    public S value() {
        return value;
    }

}
