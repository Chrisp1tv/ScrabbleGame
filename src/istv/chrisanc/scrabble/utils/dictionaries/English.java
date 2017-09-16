package istv.chrisanc.scrabble.utils.dictionaries;

import istv.chrisanc.scrabble.exceptions.utils.dictionaries.ErrorLoadingDictionaryException;

/**
 * @author Christopher Anciaux
 */
public class English extends SerializedDictionary {
    private static English instance = null;

    private English() throws ErrorLoadingDictionaryException {
        super();
    }

    public static English getInstance() throws ErrorLoadingDictionaryException {
        if (null == instance) {
            instance = new English();
        }

        return instance;
    }

    @Override
    public String getName() {
        return "utils.dictionaries.english";
    }

    @Override
    protected String getSourceFileName() {
        return "English.dictionnary";
    }
}
