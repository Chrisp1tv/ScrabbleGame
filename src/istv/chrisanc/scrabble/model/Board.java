package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.SquareInterface;

/**
 * <p>
 * This class represents the board used during the Scrabble. It has {@link Square} where pieces ({@link LetterInterface}) can be placed on.
 *
 * @author Christopher Anciaux
 */
public class Board implements BoardInterface {
    /**
     * All the {@link SquareInterface} of the {@link Board}. The Indexes represent the position of the {@link SquareInterface} on the {@link Board}.
     * The first index represents the lines, the second the columns.
     */
    protected SquareInterface[][] squares = new SquareInterface[15][15];

    /**
     * All the {@link LetterInterface} placed on the {@link SquareInterface}. The indexes represent the position of the {@link LetterInterface} on the {@link Board}.
     * The first index represents the lines, the second the columns.
     */
    protected LetterInterface[][] letters = new LetterInterface[15][15];

    public Board() {
        // TODO: construct the Board, that is to say placing all the squares correctly, according the Scrabble rules.
    }

    public SquareInterface getSquare(short line, short column) {
        // TODO
    }

    public LetterInterface getLetter(short line, short column) {
        // TODO
    }

    public BoardInterface putLetterOnSquare(LetterInterface letter, short line, short column) {
        // TODO

        return this;
    }
}
