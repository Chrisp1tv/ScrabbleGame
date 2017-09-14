package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.SquareInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
     * There are 15 lines, and 15 columns.
     */
    protected ObservableList<ObservableList<SquareInterface>> squares = FXCollections.observableArrayList();

    /**
     * All the {@link LetterInterface} placed on the {@link SquareInterface}. The indexes represent the position of the {@link LetterInterface} on the {@link Board}.
     * The first index represents the lines, the second the columns.
     * There are 15 lines, and 15 columns.
     */
    protected ObservableList<ObservableList<LetterInterface>> letters = FXCollections.observableArrayList();

    /**
     * All the {@link WordInterface} placed on the {@link Board}.
     */
    protected ObservableList<WordInterface> playedWords = FXCollections.observableArrayList();

    public Board() {
        this.buildSquaresList();
        this.buildLettersList();
    }

    /**
     * Add the given {@link WordInterface} to the board.
     *
     * @param word The {@link WordInterface} to add to the board
     */
    public void addWord(WordInterface word) {
        /* TODO:
            - Check if the word exists and is correct (good length, good positions,...)
            - Add all the word's letters in the letters attribute at the good positions
            - Add the word to the played words
        */
    }

    /**
     * @return a read-only list of the board's squares
     */
    public ObservableList<ObservableList<SquareInterface>> getSquares() {
        return FXCollections.unmodifiableObservableList(squares);
    }

    /**
     * @return a read-only list of the board's letters
     */
    public ObservableList<ObservableList<LetterInterface>> getLetters() {
        return FXCollections.unmodifiableObservableList(letters);
    }

    /**
     * @return a read-only list of the played {@link WordInterface}
     */
    public ObservableList<WordInterface> getPlayedWords() {
        return FXCollections.unmodifiableObservableList(playedWords);
    }

    protected void buildSquaresList() {
        // TODO: Place all the squares correctly, according to the Scrabble rules.
    }

    protected void buildLettersList() {
        // TODO @AC: Create the letters ArrayList with a fixed size of 15*15
    }
}
