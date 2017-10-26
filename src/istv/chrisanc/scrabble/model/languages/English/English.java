package istv.chrisanc.scrabble.model.languages.English;

import istv.chrisanc.scrabble.model.languages.Language;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christopher Anciaux
 */
public class English extends Language {
    protected List<String> letters = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");

    protected byte[] lettersValues = new byte[]{1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};

    protected byte[] lettersDistribution = new byte[]{9, 2, 2, 4, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};

    protected byte numberOfJokers = 2;

    public English() {
        super(EnglishDictionary.class);
    }

    protected List<String> getAuthorizedLetters() {
        return this.letters;
    }

    protected byte[] getLettersValues() {
        return this.lettersValues;
    }

    protected byte[] getLettersDistribution() {
        return this.lettersDistribution;
    }

    protected byte getNumberOfJokers() {
        return this.numberOfJokers;
    }
}
