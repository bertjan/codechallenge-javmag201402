package nl.revolution.codechallenge;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class WordListProviderTest {

    private static final int WORD_LIST_SIZE = 67230;
    private static final String FIRST_WORD_IN_LIST = "AA";
    private static final String LAST_WORD_IN_LIST = "ZZZS";

    @Test
    public void getWordList() {
        List<String> words = new WordListProvider().getWordList();
        assertThat(words.size(), is(WORD_LIST_SIZE));
        assertThat(words.get(0), is(FIRST_WORD_IN_LIST));
        assertThat(words.get(words.size() - 1), is(LAST_WORD_IN_LIST));
    }

    @Test
    public void toCharArrayList() {
        WordListProvider provider = new WordListProvider();
        List<char[]> words = provider.toCharArrayList(provider.getWordList());
        assertThat(words.size(), is(WORD_LIST_SIZE));
        assertThat(words.get(0), is(FIRST_WORD_IN_LIST.toCharArray()));
        assertThat(words.get(words.size() - 1), is(LAST_WORD_IN_LIST.toCharArray()));
    }

}