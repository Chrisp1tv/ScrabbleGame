package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class manages the transformation of a {@link LetterInterface}'s  {@link List} to a {@link String}, and vice versa.
 *
 * @author Christopher Anciaux
 */
public class LetterListToStringTransformer {
    /**
     * Transforms a letters {@link List} to a {@link String} object.
     *
     * @param letters The letters list to be transformed into a {@link String}
     *
     * @return the transformed {@link List} to {@link String}
     */
    public static String transform(Collection<LetterInterface> letters) {
        StringBuilder stringBuilder = new StringBuilder();
        letters.forEach(stringBuilder::append);

        return stringBuilder.toString();
    }

    /**
     * Transforms a {@link String} object to a letters {@link List}.
     *
     * @param word The {@link String} object to be transformed into a letters list
     *
     * @return the transformed {@link String} to {@link List}
     */
    public static List<LetterInterface> reverseTransform(String word, LanguageInterface language) {
        List<LetterInterface> letters = new ArrayList<>();

        for (int i = 0; i < word.length(); i++) {
            letters.add(LetterToStringTransformer.reverseTransform("" + word.charAt(i), language));
        }

        return letters;
    }
}
