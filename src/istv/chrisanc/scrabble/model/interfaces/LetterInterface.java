package istv.chrisanc.scrabble.model.interfaces;

import istv.chrisanc.scrabble.model.Letter;

/**
 * <p>
 * This class defines a letter piece used in the Scrabble, represented by the letter itself,
 * and the value of the letter (the number of points it can give to the player).
 *
 * @author Christopher Anciaux
 */
public interface LetterInterface {
    /**
     * @return the represented letter
     */
    char getLetter();

    /**
     * @return the value, the amount of points the {@link Letter} can give to the player
     */
    byte getValue();
}
