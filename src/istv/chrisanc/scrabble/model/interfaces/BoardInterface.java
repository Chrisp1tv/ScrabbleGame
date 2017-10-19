package istv.chrisanc.scrabble.model.interfaces;

import istv.chrisanc.scrabble.controllers.GameController;
import istv.chrisanc.scrabble.model.Square;
import javafx.collections.ObservableList;

import java.util.List;
import java.util.SortedMap;

/**
 * <p>
 * This class represents the board used during the Scrabble. It has {@link Square} where pieces ({@link LetterInterface}) can be placed on.
 *
 * @author Christopher Anciaux
 */
public interface BoardInterface {
    /**
     * The number of lines and columns of a Scrabble board.
     */
    short BOARD_SIZE = 15;

    /**
     * Add the given {@link WordInterface} to the board.
     *
     * @param word The {@link WordInterface} to add to the board
     */
    void addWord(WordInterface word);

    /**
     * Add the given {@link WordInterface} to the board.
     *
     * @param words The {@link WordInterface} to add to the board
     */
    void addWords(List<WordInterface> words);

    /**
     * @return a read-only list of the board's squares
     */
    ObservableList<ObservableList<SquareInterface>> getSquares();

    /**
     * @return a read-only list of the board's letters
     */
    List<List<LetterInterface>> getLetters();

    void addLetters(SortedMap<GameController.BoardPosition, LetterInterface> letters);

    /**
     * @return true if the board hasn't any letter in it, false otherwise
     */
    boolean isEmpty();

    /**
     * @return a read-only list of the played {@link WordInterface}
     */
    ObservableList<WordInterface> getPlayedWords();
}
