package istv.chrisanc.scrabble.interfaces;

import istv.chrisanc.scrabble.Square;

/**
 * <p>
 * This class represents the board used during the Scrabble. It has {@link Square} where pieces ({@link LetterInterface}) can be placed on.
 *
 * @author Christopher Anciaux
 */
public interface BoardInterface {
    SquareInterface getSquare(short line, short column);

    LetterInterface getLetter(short line, short column);

    BoardInterface putLetterOnSquare(LetterInterface letter, short line, short column);
}
