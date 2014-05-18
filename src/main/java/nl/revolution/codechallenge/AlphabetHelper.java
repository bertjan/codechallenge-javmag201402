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

    /**
     * Determines whether a word consists of an ascending set of characters,
     * given a specific alphabet (ordering of characters). */
    public boolean isWordWithAscendingCharacters(char[] word, char[] alphabet) {
        int previousIndex = -1;
        for (char c : word) {
            // Compare index in alphabet of current character to index of previous character.
            int currentIndex = ArrayUtils.indexOf(alphabet, c);
            // When current character has an alphabet position before previous character, or
            // current character is not present in the given alphabet, this is not a match.
            if (currentIndex < previousIndex || currentIndex == -1) {
                return false;
            }
            previousIndex = currentIndex;
        }
        return true;
    }

    /**
     * Counts the number of words with only ascending characters given a set of words and an alphabet.
     */
    public long countAscendingWordsForAlphabet(List<char[]> words, char[] alphabet) {
        return words.parallelStream()
                    .filter(word -> isWordWithAscendingCharacters(word, alphabet))
                    .count();
    }

    /**
     * Determines variances for a given alphabet: new alphabets which are deduced from the input alphabet.
     */
    public Set<String> determineVariancesForAlphabet(String inputAlphabet) {
        List<Character> inputAlphabetCharacters = Arrays.asList(ArrayUtils.toObject(inputAlphabet.toCharArray()));
        int size = inputAlphabetCharacters.size();
        // Used to keep track of the results. Synchronized because of concurrent access in loops below.
        Set<String> variances = Collections.synchronizedSet(new LinkedHashSet<>());

        // Determine variance:
        // - for all characters in the alphabet:
        //   - shift the character one position
        //   - keep the result
        //   - keep shifting this character one position until all positions have been visited.
        //   - do the same for the next character in the input alphabet.
        inputAlphabetCharacters.stream().forEach(charToMove -> {
            int charIndex = inputAlphabet.indexOf(charToMove);
            for (int i = 1; i < size; i++) {
                // Shift character a number of positions, rotate when necessary.
                List<Character> newAlphabet = new ArrayList<>(inputAlphabetCharacters);
                newAlphabet.remove(charIndex);
                newAlphabet.add((charIndex + i) % size, charToMove);
                variances.add(StringUtils.join(newAlphabet.toArray()));
            }
        });
        return variances;
    }

}
