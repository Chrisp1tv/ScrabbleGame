package istv.chrisanc.scrabble;

import istv.chrisanc.scrabble.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.interfaces.LetterInterface;
import istv.chrisanc.scrabble.interfaces.SquareInterface;

/**
 * <p>
 * This class represents a square on the board, that is to say a position where a {@link PlayerInterface} can put a
 * {@link LetterInterface} on the board.
 *
 * @author Christopher Anciaux
 */
public class Square implements SquareInterface {
    /**
     * This number represents the multiplier applied to the won points (of the current square only !) when a letter of the word is placed on the actual square
     */
    protected byte letterMultiplier = 1;

    /**
     * This number represents the multiplier applied to the won points (of the newly formed word) when a letter of the word is placed on the actual square
     */
    protected byte wordMultiplier = 1;

    /**
     * This boolean states whether the multiplier has already been used or not
     */
    protected boolean multiplierUsed = false;

    @Override
    public byte getLetterMultiplier() {
        return letterMultiplier;
    }

    @Override
    public byte getWordMultiplier() {
        return wordMultiplier;
    }

    @Override
    public boolean isMultiplierUsed() {
        return multiplierUsed;
    }

    @Override
    public SquareInterface setMultiplierUsed(boolean multiplierUsed) {
        this.multiplierUsed = multiplierUsed;

        return this;
    }
}
