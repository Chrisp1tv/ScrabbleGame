package istv.chrisanc.scrabble.exceptions.model.Bag;

import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.Player;

/**
 * This {@link Exception} should be thrown when a {@link Player} tries to exchange a numbre of letters when the {@link BagInterface} don't have enough
 *
 * @author Eguinane Chavatte
 */
public class NotEnoughLettersException extends Exception {
    public NotEnoughLettersException() {
        super("exceptions.model.bag.notEnoughLetters");
    }
}
