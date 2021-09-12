package model;

import istv.chrisanc.scrabble.model.Board;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests de {@link Board} features.
 *
 * @author Christopher Anciaux
 */
public class BoardTest {
    /**
     * Checks that the board construction is correct
     */
    @Test
    public void checkBoardConstruct() {
        BoardInterface board = new Board();

        assertEquals(15, board.getSquares().size());
        assertEquals(15, board.getLetters().size());
    }
}
