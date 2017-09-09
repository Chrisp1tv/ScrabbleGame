package istv.chrisanc.scrabble.interfaces;

/**
 * <p>
 * This class represents a square on the board, that is to say a position where a {@link PlayerInterface} can put a
 * {@link LetterInterface} on the board.
 *
 * @author Christopher Anciaux
 */
public interface SquareInterface {
    byte getLetterMultiplier();

    byte getWordMultiplier();

    boolean isMultiplierUsed();

    SquareInterface setMultiplierUsed(boolean multiplierUsed);
}
