package nl.revolution.codechallenge;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class KeyValuePairTest {

    @Test
    public void construct() {
        String key = "key";
        Long value = 1L;
        KeyValuePair<String, Long> keyValuePair = new KeyValuePair<>(key, value);
        assertThat(keyValuePair.key(), is(key));
        assertThat(keyValuePair.value(), is(value));
    }

}