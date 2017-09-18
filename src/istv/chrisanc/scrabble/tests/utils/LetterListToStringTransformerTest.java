package istv.chrisanc.scrabble.tests.utils;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.letters.A;
import istv.chrisanc.scrabble.model.letters.C;
import istv.chrisanc.scrabble.model.letters.W;
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
    private static List<LetterInterface> letters;

    @Test
    public void transform() throws Exception {
        String word = LetterListToStringTransformer.transform(LetterListToStringTransformerTest.letters);

        assertEquals("ACW", word);
    }

    @Test
    public void reverseTransform() throws Exception {
        List<LetterInterface> letters = LetterListToStringTransformer.reverseTransform("ACW");

        assertThat(letters, is(LetterListToStringTransformerTest.letters));
    }

    @BeforeClass
    public static void setUp() throws Exception {
        LetterListToStringTransformerTest.letters = Arrays.asList(new A(), new C(), new W());
    }
}
