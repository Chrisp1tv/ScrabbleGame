package istv.chrisanc.scrabble.model.interfaces;

import istv.chrisanc.scrabble.controllers.GameController;

import java.util.List;
import java.util.SortedMap;

/**
 * This class represents the board used during the Scrabble game. It has squares where letters can be placed on.
 *
 * @author Christopher Anciaux
 */
public interface BoardInterface {
    /**
     * The number of lines and columns of a Scrabble board.
     */
    short BOARD_SIZE = 15;

    /**
     * Add the given word to the board.
     *
     * @param word The word to add to the board
     */
    void addWord(WordInterface word);

    /**
     * Add the given words to the board.
     *
     * @param words The words to add to the board
     */
    void addWords(List<WordInterface> words);

    /**
     * @return a read-only list of the board's squares
     */
    List<List<SquareInterface>> getSquares();

    /**
     * @return a read-only list of the board's letters
     */
    List<List<LetterInterface>> getLetters();

    /**
     * Put letters on the board
     *
     * @param letters The letters to add
     */
    void addLetters(SortedMap<GameController.BoardPosition, LetterInterface> letters);

    /**
     * @return true if the board hasn't any letter in it, false otherwise
     */
    boolean isEmpty();

    /**
     * @return a read-only list of the played words
     */
    List<WordInterface> getPlayedWords();
}
