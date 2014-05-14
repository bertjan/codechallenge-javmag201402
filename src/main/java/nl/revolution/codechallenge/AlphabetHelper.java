package nl.revolution.codechallenge;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AlphabetHelper {

    public static final String DEFAULT_ALPHABET = "abcdefghijklmnopqrstuvwxyz";

    public boolean isWordWithAscendingCharacters(char[] word, char[] alphabet) {
        int previousIndex = -1;
        for (char c : word) {
            int currentIndex = ArrayUtils.indexOf(alphabet, c);
            if (currentIndex < previousIndex || currentIndex == -1) {
                return false;
            }
            previousIndex = currentIndex;
        }
        return true;
    }

    public long countAscendingWordsForAlphabet(List<char[]> words, char[] alphabet) {
        return words.parallelStream()
                    .filter(word -> isWordWithAscendingCharacters(word, alphabet))
                    .count();
    }

    public Set<String> getVariancesForAlphabet(String inputAlphabet) {
        List<Character> inputAlphabetCharacters = Arrays.asList(ArrayUtils.toObject(inputAlphabet.toCharArray()));
        int size = inputAlphabetCharacters.size();
        Set<String> variances = Collections.synchronizedSet(new LinkedHashSet<>());

        // Move characters around.
        inputAlphabetCharacters.stream().forEach(charToMove -> {
            int charIndex = inputAlphabet.indexOf(charToMove);
            for (int i = 1; i < size; i++) {
                List<Character> tmpList = new ArrayList<>(inputAlphabetCharacters);
                tmpList.remove(charIndex);
                tmpList.add((charIndex + i) % size, charToMove);
                variances.add(StringUtils.join(tmpList.toArray()));
            }
        });
        return variances;
    }

}
