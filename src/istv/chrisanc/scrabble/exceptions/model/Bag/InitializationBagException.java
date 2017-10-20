package istv.chrisanc.scrabble.exceptions.model.Bag;

/**
 * This {@link Exception} should be thrown when the initialization of letters bag has failed.
 *
 * @author Christopher Anciaux
 */
public class InitializationBagException extends Exception {
    public InitializationBagException() {
        super("exceptions.model.bag.initialization");
    }
}
