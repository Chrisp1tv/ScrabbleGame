package istv.chrisanc.scrabble.model.interfaces;

/**
 * This class represents a square on the board, that is to say a position where a player can put a
 * letter on the board.
 *
 * @author Christopher Anciaux
 */
public interface SquareInterface {
    byte getLetterMultiplier();

    byte getWordMultiplier();

    String getInformation();

    String getCssClass();

    /**
     * @return true if the multiplier of the square has already been used, false otherwise
     */
    boolean isMultiplierUsed();

    /**
     * Makes the multiplier used
     */
    void makeMultiplierUsed();
}
