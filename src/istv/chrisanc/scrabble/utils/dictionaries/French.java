package istv.chrisanc.scrabble.utils.dictionaries;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.utils.interfaces.DictionaryInterface;

import java.util.List;

/**
 * @author Christopher Anciaux
 */
public class French implements DictionaryInterface {
    @Override
    public String getName() {
        return "utils.dictionaries.french";
    }

    @Override
    public boolean wordExists(String word) {
        // TODO
    }

    @Override
    public List<String> findWordsByLetters(List<LetterInterface> letters) {
        // TODO
    }
}
