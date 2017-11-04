package istv.chrisanc.scrabble.tests.utils;

import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.languages.French.French;
import istv.chrisanc.scrabble.model.languages.Global.letters.Joker;
import istv.chrisanc.scrabble.utils.LetterToStringTransformer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the {@link DictionaryInterface} features.
 *
 * @author Christopher Anciaux
 */
public class DictionaryTest {
    private static LanguageInterface language;

    private static List<LetterInterface> lettersWithoutJoker;

    private static List<LetterInterface> lettersWithJoker;

    private static List<LetterInterface> startingLetters;

    private static List<LetterInterface> endingLetters;

    /**
     * Tests that an existing word can be found in a dictionary, and that a non-existent one isn't found
     */
    @Test
    public void wordExists() throws Exception {
        assertTrue(DictionaryTest.language.getDictionary().wordExists("BONJOUR"));
        assertFalse(DictionaryTest.language.getDictionary().wordExists("NUMBER"));
    }

    /**
     * Tests that words are found by given letters, without joker
     */
    @Test
    public void findWordsHavingLettersWithoutJoker() {
        List<String> foundWords = DictionaryTest.language.getDictionary().findWordsHavingLetters(DictionaryTest.lettersWithoutJoker);

        assertEquals(88, foundWords.size());
    }

    /**
     * Tests that words are found by given letters, with joker
     */
    @Test
    public void findWordsHavingLettersWithJoker() {
        List<String> foundWords = DictionaryTest.language.getDictionary().findWordsHavingLetters(DictionaryTest.lettersWithJoker);

        assertEquals(104, foundWords.size());
    }

    /**
     * Tests that words are found by given letters, and starting by other given letters
     */
    @Test
    public void findWordsStartingWithAndHavingLetters() {
        List<String> foundWords = DictionaryTest.language.getDictionary().findWordsStartingWithAndHavingLetters(3, 10, DictionaryTest.startingLetters, DictionaryTest.lettersWithoutJoker);

        assertEquals(16, foundWords.size());
    }

    /**
     * Tests that words are found by given letters, and ending by other given letters
     */
    @Test
    public void findWordsEndingWithAndHavingLetters() {
        List<String> foundWords = DictionaryTest.language.getDictionary().findWordsEndingWithAndHavingLetters(3, 10, DictionaryTest.endingLetters, DictionaryTest.lettersWithoutJoker);

        assertEquals(10, foundWords.size());
    }

    @BeforeClass
    public static void setUp() throws Exception {
        DictionaryTest.language = new French();

        DictionaryTest.lettersWithoutJoker = Arrays.asList(LetterToStringTransformer.reverseTransform("A", DictionaryTest.language), LetterToStringTransformer.reverseTransform("H", DictionaryTest.language), LetterToStringTransformer.reverseTransform("C", DictionaryTest.language), LetterToStringTransformer.reverseTransform("N", DictionaryTest.language), LetterToStringTransformer.reverseTransform("E", DictionaryTest.language), LetterToStringTransformer.reverseTransform("D", DictionaryTest.language), LetterToStringTransformer.reverseTransform("I", DictionaryTest.language));
        DictionaryTest.lettersWithJoker = Arrays.asList(LetterToStringTransformer.reverseTransform("A", DictionaryTest.language), LetterToStringTransformer.reverseTransform("H", DictionaryTest.language), LetterToStringTransformer.reverseTransform("C", DictionaryTest.language), LetterToStringTransformer.reverseTransform("J", DictionaryTest.language), new Joker(), LetterToStringTransformer.reverseTransform("D", DictionaryTest.language));

        DictionaryTest.startingLetters = Arrays.asList(LetterToStringTransformer.reverseTransform("C", DictionaryTest.language), LetterToStringTransformer.reverseTransform("H", DictionaryTest.language), LetterToStringTransformer.reverseTransform("I", DictionaryTest.language));
        DictionaryTest.endingLetters = Arrays.asList(LetterToStringTransformer.reverseTransform("E", DictionaryTest.language), LetterToStringTransformer.reverseTransform("U", DictionaryTest.language), LetterToStringTransformer.reverseTransform("R", DictionaryTest.language));
    }
}
