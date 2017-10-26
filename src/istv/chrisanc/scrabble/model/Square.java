package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.SquareInterface;

import java.io.Serializable;

/**
 * <p>
 * This class represents a square on the board, that is to say a position where a {@link PlayerInterface} can put a
 * {@link LetterInterface} on the board.
 *
 * @author Christopher Anciaux
 */
public class Square implements SquareInterface, Serializable {
    /**
     * This boolean states whether the multiplier has already been used or not
     */
    protected boolean multiplierUsed = false;

    /**
     * This number represents the multiplier applied to the won points (of the current square only !) when a letter of the word is placed on the actual square
     */
    @Override
    public byte getLetterMultiplier() {
        return 1;
    }

    /**
     * This number represents the multiplier applied to the won points (of the newly formed word) when a letter of the word is placed on the actual square
     */
    @Override
    public byte getWordMultiplier() {
        return 1;
    }

    /**
     * This string represents the key translation of the information displayed about the board, like "Word double" for example
     */
    @Override
    public String getInformation() {
        return null;
    }

    /**
     * This string represents the CSS class used to stylise the square of the board
     */
    @Override
    public String getCssClass() {
        return "normal-square";
    }

    @Override
    public void makeMultiplierUsed() {
        this.multiplierUsed = true;
    }

    @Override
    public boolean isMultiplierUsed() {
        return multiplierUsed;
    }
}
