package istv.chrisanc.scrabble.utils.interfaces;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

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
    List<String> findWordsByLetters(List<LetterInterface> letters);
}
