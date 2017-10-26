package istv.chrisanc.scrabble.model.interfaces;

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

    String getInformation();

    String getCssClass();

    boolean isMultiplierUsed();

    void makeMultiplierUsed();
}
