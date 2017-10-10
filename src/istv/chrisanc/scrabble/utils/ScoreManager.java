package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.model.Player;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;

import java.util.List;

/**
 * This class handles all the calculations of the {@link Player}'s score, and manages all score-relative logic.
 *
 * @author Christopher Anciaux
 */
public class ScoreManager {
    public static void updateScore(BoardInterface board, List<WordInterface> addedWords) {
        /* TODO
        This method should actualize all the Square states :
        For each letter placed on the square, the MultiplierUsed value of the square must be changed to true
        */
    }
}
