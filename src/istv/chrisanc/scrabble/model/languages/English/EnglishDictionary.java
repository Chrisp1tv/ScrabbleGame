package istv.chrisanc.scrabble.model.languages.English;

import istv.chrisanc.scrabble.exceptions.utils.dictionaries.ErrorLoadingDictionaryException;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.utils.SerializedDictionary;

/**
 * @author Christopher Anciaux
 */
public class EnglishDictionary extends SerializedDictionary {
    private static EnglishDictionary instance = null;

    private EnglishDictionary() throws ErrorLoadingDictionaryException {
        super();
    }

    public static EnglishDictionary getInstance() throws ErrorLoadingDictionaryException {
        if (null == instance) {
            instance = new EnglishDictionary();
        }

        return instance;
    }

    @Override
    protected String getSourceFileName() {
        return "English.dictionary";
    }

    @Override
    protected LanguageInterface getLanguage() {
        return new English();
    }
}
