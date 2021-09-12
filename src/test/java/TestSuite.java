import model.BoardTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import utils.DictionaryTest;
import utils.GameSaverTest;
import utils.LetterListToStringTransformerTest;

/**
 * This method regroups all the tests and can be used to execute each one directly.
 *
 * @author Christopher Anciaux
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({BoardTest.class, DictionaryTest.class, GameSaverTest.class, LetterListToStringTransformerTest.class
})
public class TestSuite {
}