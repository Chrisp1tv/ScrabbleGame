package istv.chrisanc.scrabble.exceptions.Bag;

import istv.chrisanc.scrabble.interfaces.BagInterface;
import istv.chrisanc.scrabble.Player;

/**
 * This {@link Exception} should be thrown when a {@link Player} tries to draw a letter when the {@link BagInterface} is empty.
 *
 * @author Christopher Anciaux
 */
public class EmptyBagException extends Exception {
    public EmptyBagException() {
        super("Le sac est vide.");
    }
}
