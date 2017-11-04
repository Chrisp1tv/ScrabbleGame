package istv.chrisanc.scrabble.exceptions.model.Bag;

/**
 * This {@link Exception} should be thrown when a player tries to exchange letters whit the bag when it doesn't have enough of it.
 *
 * @author Eguinane Chavatte
 */
public class NotEnoughLettersException extends Exception {
    private static final long serialVersionUID = -57913502036210867L;

    public NotEnoughLettersException() {
        super("exceptions.model.bag.notEnoughLetters");
    }
}
