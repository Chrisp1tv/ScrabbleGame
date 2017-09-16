package istv.chrisanc.scrabble.exceptions.model.Bag;

import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.Player;

/**
 * This {@link Exception} should be thrown when a {@link Player} tries to draw a letter when the {@link BagInterface} is empty.
 *
 * @author Christopher Anciaux
 */
public class EmptyBagException extends Exception {
    public EmptyBagException() {
        super("exceptions.model.bag.emptyBag");
    }
}
