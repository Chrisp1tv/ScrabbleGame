package istv.chrisanc.scrabble.model.languages.French;

import istv.chrisanc.scrabble.model.languages.Language;

import java.util.Arrays;
import java.util.List;

/**
 * @author Christopher Anciaux
 */
public class French extends Language {
    protected List<String> letters = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");

    protected byte[] lettersValues = new byte[]{1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 10, 1, 2, 1, 1, 3, 8, 1, 1, 1, 1, 4, 10, 10, 10, 10};

    protected byte[] lettersDistribution = new byte[]{9, 2, 2, 3, 15, 2, 2, 2, 8, 1, 1, 5, 3, 6, 6, 2, 1, 6, 6, 6, 6, 2, 1, 1, 1, 1};

    protected byte numberOfJokers = 2;

    public French() {
        super(FrenchDictionary.class);
    }

    public String getName() {
        return "utils.languages.french";
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
