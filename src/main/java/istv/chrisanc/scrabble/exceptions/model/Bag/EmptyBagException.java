package istv.chrisanc.scrabble.exceptions.model.Bag;

/**
 * This {@link Exception} should be thrown when a player tries to draw a letter when the bag is empty.
 *
 * @author Christopher Anciaux
 */
public class EmptyBagException extends Exception {
    private static final long serialVersionUID = -5791350247760210867L;

    public EmptyBagException() {
        super("exceptions.model.bag.emptyBag");
    }
}
