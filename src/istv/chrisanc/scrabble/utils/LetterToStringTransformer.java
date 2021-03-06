package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * This class manages the transformation of a {@link LetterInterface} to a {@link String}, and vice versa.
 *
 * @author Christopher Anciaux
 */
public class LetterToStringTransformer {
    /**
     * Transforms a {@link LetterInterface} to a {@link String} object.
     * This method isn't very useful but is implemented to keep a certain logic within the current lass.
     *
     * @param letter The {@link LetterInterface} to be transformed into a {@link String}
     *
     * @return the transformed {@link LetterInterface} to {@link String}
     */
    public static String transform(LetterInterface letter) {
        return letter.toString();
    }

    /**
     * Transforms a {@link String} object to a letters {@link LetterInterface}.
     *
     * @param letter The {@link String} object to be transformed into a {@link LetterInterface}
     *
     * @return the transformed {@link String} to {@link LetterInterface}
     * @throws IllegalArgumentException if the corresponding {@link LetterInterface} isn't found or an
     *                                  error occurred during creation of the letter
     */
    public static LetterInterface reverseTransform(String letter, LanguageInterface language) {
        return language.getLetter(letter);
    }
}
