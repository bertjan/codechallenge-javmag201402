package nl.revolution.codechallenge;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlphabetFinder {

    private static final Logger LOG = LoggerFactory.getLogger(AlphabetFinder.class);
    private static final int MAX_NUMBER_OF_PASSES = 50;
    private static final int MAX_NUMBER_OF_PASSES_WITH_SAME_RESULT = 2;
    private static final int MAX_NUMBER_OF_BEST_ALPHABETS_TO_KEEP_EACH_PASS = 1;
    private static final int MAX_NUMBER_OF_VARIANCES_TO_USE = 1000;
    private final AlphabetHelper helper = new AlphabetHelper();
    private final WordListProvider wordListProvider = new WordListProvider();

    public static void main(String... args) throws IOException {
        new AlphabetFinder().findOptimalAlphabet();
    }

    public void findOptimalAlphabet() {
        long start = System.currentTimeMillis();

        // Initialize source word list.
        final List<String> stringWordList = wordListProvider.getWordList();
        final List<char[]> wordList = wordListProvider.toCharArrayList(stringWordList);

        // Initialize first pass.
        List<String> alphabets = new ArrayList<>(Arrays.asList(AlphabetHelper.DEFAULT_ALPHABET.toUpperCase()));
        long bestScoreOverall = 0;
        int numberOfPassesWithSameScore = 0;

        // Find optimal alphabet in a sequence of passes.
        for (int pass = 1; pass < MAX_NUMBER_OF_PASSES; pass++) {
            LOG.info("Pass " + pass + ". Best score so far: " + bestScoreOverall);

            // Start with (subset of) best results from previous pass.
            alphabets = alphabets.stream()
                                 .limit(MAX_NUMBER_OF_BEST_ALPHABETS_TO_KEEP_EACH_PASS)
                                 .collect(Collectors.toList());

            // Generate variance for all alphabets up to a maximum number.
            alphabets.addAll(alphabets.parallelStream()
                                      .map(helper::getVariancesForAlphabet)
                                      .flatMap(var -> var.parallelStream())
                                      .limit(MAX_NUMBER_OF_VARIANCES_TO_USE)
                                      .collect(Collectors.toList()));

            // Calculate results (scores) for this pass.
            final Map<String,Long> passResults =
                    alphabets.parallelStream()
                             .map(alphabet -> new KeyValuePair<>(alphabet,
                                                                 helper.countAscendingWordsForAlphabet(wordList, alphabet.toCharArray())))
                             .collect(Collectors.toMap(KeyValuePair::key, KeyValuePair::value));

            // Determine best score of pass: sort result map on values (descending) and get first value.
            long bestScoreOfPass = passResults.values().stream()
                                                       .sorted(Comparator.reverseOrder())
                                                       .findFirst().get();

            // Keep all alphabets with score equals to best score of pass.
            alphabets = passResults.entrySet().parallelStream()
                                              .filter(entry -> entry.getValue() == bestScoreOfPass)
                                              .map(Map.Entry::getKey)
                                              .collect(Collectors.toList());

            // Determine whether this pass did something useful.
            if (bestScoreOfPass == bestScoreOverall) {
                numberOfPassesWithSameScore++;
                if (numberOfPassesWithSameScore == MAX_NUMBER_OF_PASSES_WITH_SAME_RESULT) {
                    // Nothing new for a few passes, quit searching.
                    break;
                }
            }
            bestScoreOverall = bestScoreOfPass;

        }

        // Present results.
        LOG.info("Found " + alphabets.size() + " optimal alphabets: ");
        for (String alphabet : alphabets) {
            LOG.info(alphabet + ": " + helper.countAscendingWordsForAlphabet(wordList, alphabet.toCharArray()));
        }

        LOG.info("Duration: " + (System.currentTimeMillis() - start) + " ms.");
    }

}
