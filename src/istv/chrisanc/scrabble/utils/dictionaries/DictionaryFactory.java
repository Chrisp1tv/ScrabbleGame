package istv.chrisanc.scrabble.utils.dictionaries;

import istv.chrisanc.scrabble.exceptions.utils.dictionaries.ErrorLoadingDictionaryException;
import istv.chrisanc.scrabble.utils.interfaces.DictionaryInterface;

/**
 * @author Christopher Anciaux
 */
public class DictionaryFactory {
    public static final short FRENCH = 0;

    public static final short ENGLISH = 1;

    public DictionaryInterface getDictionary(short dictionaryType) throws ErrorLoadingDictionaryException {
        switch (dictionaryType) {
            case ENGLISH:
                return English.getInstance();
            case FRENCH:
            default:
                return French.getInstance();
        }
    }
}
