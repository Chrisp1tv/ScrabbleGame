package istv.chrisanc.scrabble.utils.dictionaries;

import istv.chrisanc.scrabble.exceptions.utils.dictionaries.ErrorLoadingDictionaryException;

/**
 * @author Christopher Anciaux
 */
public class French extends SerializedDictionary {
    private static French instance = null;

    private French() throws ErrorLoadingDictionaryException {
        super();
    }

    public static French getInstance() throws ErrorLoadingDictionaryException {
        if (null == instance) {
            instance = new French();
        }

        return instance;
    }

    @Override
    public String getName() {
        return "utils.dictionaries.french";
    }

    @Override
    protected String getSourceFileName() {
        return "French.dictionary";
    }
}
