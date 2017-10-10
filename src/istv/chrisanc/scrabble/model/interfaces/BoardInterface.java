package istv.chrisanc.scrabble.model.interfaces;

import istv.chrisanc.scrabble.model.Square;
import javafx.collections.ObservableList;

import java.util.List;

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

    void addLetters(List<List<LetterInterface>> letters);

    /**
     * @return a read-only list of the played {@link WordInterface}
     */
    ObservableList<WordInterface> getPlayedWords();
}
