package istv.chrisanc.scrabble.exceptions.utils.dictionaries;

/**
 * This {@link RuntimeException} should be thrown when an error occurred while loading a dictionary.
 *
 * @author Christopher Anciaux
 */
public class ErrorLoadingDictionaryException extends RuntimeException {
    private static final long serialVersionUID = -5791350269810867L;

    public ErrorLoadingDictionaryException() {
        super("exceptions.utils.dictionaries.errorLoadingDictionary");
    }
}
