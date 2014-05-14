package nl.revolution.codechallenge;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class AlphabetHelperTest {

    private final AlphabetHelper helper = new AlphabetHelper();

    @Test
    public void isWordWithAscendingCharacters() {
        char[] alphabet = AlphabetHelper.DEFAULT_ALPHABET.toUpperCase().toCharArray();

        assertThat(helper.isWordWithAscendingCharacters("ABC".toCharArray(), alphabet), is(true));
        assertThat(helper.isWordWithAscendingCharacters("BAC".toCharArray(), alphabet), is(false));
        assertThat(helper.isWordWithAscendingCharacters("A".toCharArray(), alphabet), is(true));
        assertThat(helper.isWordWithAscendingCharacters("BB".toCharArray(), alphabet), is(true));
        assertThat(helper.isWordWithAscendingCharacters("BBC".toCharArray(), alphabet), is(true));
        assertThat(helper.isWordWithAscendingCharacters("_!@".toCharArray(), alphabet), is(false));
        assertThat(helper.isWordWithAscendingCharacters("A_B".toCharArray(), alphabet), is(false));
        assertThat(helper.isWordWithAscendingCharacters("abc".toCharArray(), alphabet), is(false));

    }

    @Test
    public void countAscendingWordsForAlphabet() {
        WordListProvider provider = new WordListProvider();
        List<char[]> wordList = provider.toCharArrayList(provider.getWordList());
        assertThat(helper.countAscendingWordsForAlphabet(wordList, AlphabetHelper.DEFAULT_ALPHABET.toUpperCase().toCharArray()), is(860L));
        assertThat(helper.countAscendingWordsForAlphabet(wordList, ("BA" + AlphabetHelper.DEFAULT_ALPHABET.substring(2).toUpperCase()).toCharArray()), is(921L));
        assertThat(helper.countAscendingWordsForAlphabet(wordList, "BCFJWHLOAQUMVPXINTZKGERDYS".toCharArray()), is(4046L));
    }

    @Test
    public void getVariancesForAlphabet() {
        List<String> variances = new ArrayList<>(helper.getVariancesForAlphabet(AlphabetHelper.DEFAULT_ALPHABET));
        assertThat(variances.size(), is(625));
        assertThat(variances.get(0), is("bacdefghijklmnopqrstuvwxyz"));
        assertThat(variances.get(1), is("bcadefghijklmnopqrstuvwxyz"));
        assertThat(variances.get(2), is("bcdaefghijklmnopqrstuvwxyz"));
    }

}