package istv.chrisanc.scrabble.exceptions.model.Bag;

import istv.chrisanc.scrabble.model.Player;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;

/**
 * This {@link Exception} should be thrown when a {@link Player} tries to exchange a number of letters when the {@link BagInterface} doesn't have enough of it.
 *
 * @author Eguinane Chavatte
 */
public class NotEnoughLettersException extends Exception {
    public NotEnoughLettersException() {
        super("exceptions.model.bag.notEnoughLetters");
    }
}
