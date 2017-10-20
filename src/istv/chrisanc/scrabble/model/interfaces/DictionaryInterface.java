package istv.chrisanc.scrabble.model.interfaces;

import java.util.List;

/**
 * <p>
 * This class represents a dictionary, used to check words existence and to find words having given letters.
 *
 * @author Christopher Anciaux
 */
public interface DictionaryInterface {
    /**
     * @return the name of the dictionary
     */
    String getName();

    /**
     * @param word The word we want to check existence
     *
     * @return true if the word exists, false otherwise
     */
    boolean wordExists(String word);

    /**
     * @param letters The letters used to find words (the found words should have only these letters and no others !)
     *
     * @return a list of the found words
     */
    List<String> findWordsHavingLetters(List<LetterInterface> letters);

    /**
     * @param minLength        The minimum number of letters in the searched words
     * @param maxLength        The maximum number of letters in the searched words
     * @param startingLetters  The letters starting the searched words
     * @param availableLetters The letters available to begin the search words
     *
     * @return the found words, having length between minLength and maxLength, starting with startingLetters and containing availableLetters
     */
    List<String> findWordsStartingWithAndHavingLetters(int minLength, int maxLength, List<LetterInterface> startingLetters, List<LetterInterface> availableLetters);

    /**
     * @param minLength        The minimum number of letters in the searched words
     * @param maxLength        The maximum number of letters in the searched words
     * @param endingLetters    The letters ending the searched words
     * @param availableLetters The letters available to begin the search words
     *
     * @return the found words, having length between minLength and maxLength, ending with endingLetters and containing availableLetters
     */
    List<String> findWordsEndingWithAndHavingLetters(int minLength, int maxLength, List<LetterInterface> endingLetters, List<LetterInterface> availableLetters);
}
