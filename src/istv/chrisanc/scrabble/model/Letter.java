package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * <p>
 * This class defines a letter piece used in the Scrabble, represented by the letter itself,
 * and the value of the letter (the number of points it can give to the player).
 *
 * @author Christopher Anciaux
 */
public abstract class Letter implements LetterInterface {
    /**
     * @return the represented letter
     */
    public abstract char getLetter();

    /**
     * @return the value, the amount of points the {@link Letter} can give to the player
     */
    public abstract byte getValue();
}
