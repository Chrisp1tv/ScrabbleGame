package istv.chrisanc.scrabble.tests.utils;

import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.languages.French.French;
import istv.chrisanc.scrabble.model.languages.French.letters.A;
import istv.chrisanc.scrabble.model.languages.French.letters.C;
import istv.chrisanc.scrabble.model.languages.French.letters.D;
import istv.chrisanc.scrabble.model.languages.French.letters.E;
import istv.chrisanc.scrabble.model.languages.French.letters.H;
import istv.chrisanc.scrabble.model.languages.French.letters.I;
import istv.chrisanc.scrabble.model.languages.French.letters.J;
import istv.chrisanc.scrabble.model.languages.French.letters.N;
import istv.chrisanc.scrabble.model.languages.French.letters.R;
import istv.chrisanc.scrabble.model.languages.French.letters.U;
import istv.chrisanc.scrabble.model.languages.Global.letters.Joker;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Christopher Anciaux
 */
public class DictionaryTest {
    private static DictionaryInterface dictionary;

    private static List<LetterInterface> lettersWithoutJoker;

    private static List<LetterInterface> lettersWithJoker;

    private static List<LetterInterface> startingLetters;

    private static List<LetterInterface> endingLetters;

    @Test
    public void wordExists() throws Exception {
        assertTrue(DictionaryTest.dictionary.wordExists("BONJOUR"));
        assertFalse(DictionaryTest.dictionary.wordExists("NUMBER"));
    }

    @Test
    public void findWordsHavingLettersWithoutJoker() {
        List<String> foundWords = dictionary.findWordsHavingLetters(DictionaryTest.lettersWithoutJoker);

        assertEquals(88, foundWords.size());
    }

    @Test
    public void findWordsHavingLettersWithJoker() {
        List<String> foundWords = dictionary.findWordsHavingLetters(DictionaryTest.lettersWithJoker);

        assertEquals(104, foundWords.size());
    }

    @Test
    public void findWordsStartingWithAndHavingLetters() {
        List<String> foundWords = DictionaryTest.dictionary.findWordsStartingWithAndHavingLetters(3, 10, DictionaryTest.startingLetters, DictionaryTest.lettersWithoutJoker);

        assertEquals(16, foundWords.size());
    }

    @Test
    public void findWordsEndingWithAndHavingLetters() {
        List<String> foundWords = DictionaryTest.dictionary.findWordsEndingWithAndHavingLetters(3, 10, DictionaryTest.endingLetters, DictionaryTest.lettersWithoutJoker);

        assertEquals(10, foundWords.size());
    }

    @BeforeClass
    public static void setUp() throws Exception {
        DictionaryTest.dictionary = (new French()).getDictionary();

        DictionaryTest.lettersWithoutJoker = Arrays.asList(new A(), new H(), new C(), new N(), new E(), new D(), new I());
        DictionaryTest.lettersWithJoker = Arrays.asList(new A(), new H(), new C(), new J(), new Joker(), new D());

        DictionaryTest.startingLetters = Arrays.asList(new C(), new H(), new I());
        DictionaryTest.endingLetters = Arrays.asList(new E(), new U(), new R());
    }
}
