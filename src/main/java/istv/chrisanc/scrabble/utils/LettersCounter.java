package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Christopher Anciaux
 */
public class LettersCounter {
    /**
     * @param letters The given letters we have to count
     *
     * @return a map having for key the {@link LetterInterface}, and for value the number of occurrences of the {@link LetterInterface}
     */
    public static Map<LetterInterface, Integer> countOccurrencesOfEachLetter(List<LetterInterface> letters) {
        Set<LetterInterface> uniqueLetters = new HashSet<>(letters);
        Map<LetterInterface, Integer> nbLetters = new HashMap<>();

        for (LetterInterface uniqueLetter : uniqueLetters) {
            nbLetters.put(uniqueLetter, Collections.frequency(letters, uniqueLetter));
        }

        return nbLetters;
    }

    /**
     * @param word The word for which we want to count occurrences of each letter
     *
     * @return a map having for key the letter of the word, and for value the number of occurrences of the letter
     */
    public static Map<Character, Integer> countOccurrencesOfEachLetter(String word) {
        Map<Character, Integer> nbLettersOfCurrentWord = new HashMap<>();

        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            if (nbLettersOfCurrentWord.containsKey(c)) {
                int cnt = nbLettersOfCurrentWord.get(c);
                nbLettersOfCurrentWord.put(c, ++cnt);
            } else {
                nbLettersOfCurrentWord.put(c, 1);
            }
        }

        return nbLettersOfCurrentWord;
    }
}
