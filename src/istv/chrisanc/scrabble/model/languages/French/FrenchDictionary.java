package istv.chrisanc.scrabble.model.languages.French;

import istv.chrisanc.scrabble.exceptions.utils.dictionaries.ErrorLoadingDictionaryException;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.utils.SerializedDictionary;

/**
 * @author Christopher Anciaux
 */
public class FrenchDictionary extends SerializedDictionary {
    private static FrenchDictionary instance = null;

    private FrenchDictionary() throws ErrorLoadingDictionaryException {
        super();
    }

    public static FrenchDictionary getInstance() throws ErrorLoadingDictionaryException {
        if (null == FrenchDictionary.instance) {
            FrenchDictionary.instance = new FrenchDictionary();
        }

        return FrenchDictionary.instance;
    }

    @Override
    protected String getSourceFileName() {
        return "French.dictionary";
    }

    @Override
    protected LanguageInterface getLanguage() {
        return new French();
    }
}
