package istv.chrisanc.scrabble.tests.utils;

import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.languages.French.French;
import istv.chrisanc.scrabble.utils.LetterListToStringTransformer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author Christopher Anciaux
 */
public class LetterListToStringTransformerTest {
    private static LanguageInterface language;

    private static List<LetterInterface> letters;

    @Test
    public void transform() throws Exception {
        String word = LetterListToStringTransformer.transform(LetterListToStringTransformerTest.letters);

        assertEquals("ACW", word);
    }

    @Test
    public void reverseTransform() throws Exception {
        List<LetterInterface> letters = LetterListToStringTransformer.reverseTransform("ACW", new French());

        assertThat(letters, is(LetterListToStringTransformerTest.letters));
    }

    @BeforeClass
    public static void setUp() throws Exception {
        LetterListToStringTransformerTest.language = new French();
        LetterListToStringTransformerTest.letters = Arrays.asList(language.getLetter("A"), language.getLetter("C"), language.getLetter("W"));
    }
}
