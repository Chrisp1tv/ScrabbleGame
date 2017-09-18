package istv.chrisanc.scrabble.tests.utils;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.letters.*;
import istv.chrisanc.scrabble.utils.dictionaries.DictionaryFactory;
import istv.chrisanc.scrabble.utils.interfaces.DictionaryInterface;
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

    @BeforeClass
    public static void setUp() throws Exception {
        DictionaryFactory dictionaryFactory = new DictionaryFactory();
        DictionaryTest.dictionary = dictionaryFactory.getDictionary(DictionaryFactory.FRENCH);

        DictionaryTest.lettersWithoutJoker = Arrays.asList(new A(), new H(), new C(), new N(), new E(), new D(), new I());
        DictionaryTest.lettersWithJoker = Arrays.asList(new A(), new H(), new C(), new J(), new Joker(), new D());
    }
}
