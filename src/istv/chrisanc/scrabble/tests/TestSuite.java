package istv.chrisanc.scrabble.tests;

import istv.chrisanc.scrabble.tests.model.BoardTest;
import istv.chrisanc.scrabble.tests.utils.DictionaryTest;
import istv.chrisanc.scrabble.tests.utils.LetterListToStringTransformerTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author Christopher Anciaux
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({BoardTest.class, DictionaryTest.class, LetterListToStringTransformerTest.class
})
public class TestSuite {
}