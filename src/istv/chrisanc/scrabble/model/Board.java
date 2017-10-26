package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.controllers.GameController;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.SquareInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;
import istv.chrisanc.scrabble.model.squares.DarkBlue;
import istv.chrisanc.scrabble.model.squares.Pink;
import istv.chrisanc.scrabble.model.squares.Red;
import istv.chrisanc.scrabble.model.squares.SkyBlue;
import istv.chrisanc.scrabble.model.squares.Star;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

/**
 * <p>
 * This class represents the board used during the Scrabble. It has {@link SquareInterface} where pieces ({@link LetterInterface}) can be placed on.
 *
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 */
public class Board implements BoardInterface, Serializable {
    /**
     * All the {@link SquareInterface} of the {@link Board}. The Indexes represent the position of the {@link SquareInterface} on the {@link Board}.
     * The first index represents the lines, the second the columns.
     * There are 15 lines, and 15 columns.
     */
    protected List<List<SquareInterface>> squares = new ArrayList<>();

    /**
     * All the {@link LetterInterface} placed on the {@link SquareInterface}. The indexes represent the position of the {@link LetterInterface} on the {@link Board}.
     * The first index represents the lines, the second the columns.
     * There are 15 lines, and 15 columns.
     */
    protected List<List<LetterInterface>> letters = new ArrayList<>();

    /**
     * All the {@link WordInterface} placed on the {@link Board}.
     */
    protected List<WordInterface> playedWords = new ArrayList<>();

    public Board() {
        this.buildSquaresList();
        this.buildLettersList();
    }

    /**
     * @return a read-only list of the board's squares
     */
    @Override
    public List<List<SquareInterface>> getSquares() {
        return this.squares;
    }

    @Override
    public List<List<LetterInterface>> getLetters() {
        return Collections.unmodifiableList(this.letters);
    }

    @Override
    public void addLetters(SortedMap<GameController.BoardPosition, LetterInterface> letters) {
        for (SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedEntry : letters.entrySet()) {
            this.letters.get(playedEntry.getKey().getLine()).set(playedEntry.getKey().getColumn(), playedEntry.getValue());
        }
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < BoardInterface.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardInterface.BOARD_SIZE; j++) {
                if (null != this.letters.get(i).get(j)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public List<WordInterface> getPlayedWords() {
        return Collections.unmodifiableList(this.playedWords);
    }

    /**
     * Add the given {@link WordInterface} to the board.
     *
     * @param word The {@link WordInterface} to add to the board
     */
    public void addWord(WordInterface word) {
        this.playedWords.add(word);
    }

    @Override
    public void addWords(List<WordInterface> words) {
        this.playedWords.addAll(words);
    }

    /**
     * Builds the {@link SquareInterface}'s list according to the Scrabble rules.
     */
    protected void buildSquaresList() {
        this.squares.addAll(Arrays.asList(
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        ));

        // Line 1
        this.squares.get(0).addAll(Arrays.asList(
                new Red(),
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square(),
                new Square(),
                new Red(),
                new Square(),
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square(),
                new Red()
        ));

        // Line 2
        this.squares.get(1).addAll(Arrays.asList(
                new Square(),
                new Pink(),
                new Square(),
                new Square(),
                new Square(),
                new DarkBlue(),
                new Square(),
                new Square(),
                new Square(),
                new DarkBlue(),
                new Square(),
                new Square(),
                new Square(),
                new Pink(),
                new Square()
        ));

        // Line 3
        this.squares.get(2).addAll(Arrays.asList(
                new Square(),
                new Square(),
                new Pink(),
                new Square(),
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square(),
                new Square(),
                new Pink(),
                new Square(),
                new Square()
        ));

        // Line 4
        this.squares.get(3).addAll(Arrays.asList(
                new SkyBlue(),
                new Square(),
                new Square(),
                new Pink(),
                new Square(),
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square(),
                new Square(),
                new Pink(),
                new Square(),
                new Square(),
                new SkyBlue()
        ));

        // Line 5
        this.squares.get(4).addAll(Arrays.asList(
                new Square(),
                new Square(),
                new Square(),
                new Square(),
                new Pink(),
                new Square(),
                new Square(),
                new Square(),
                new Square(),
                new Square(),
                new Pink(),
                new Square(),
                new Square(),
                new Square(),
                new Square()
        ));

        // Line 6
        this.squares.get(5).addAll(Arrays.asList(
                new Square(),
                new DarkBlue(),
                new Square(),
                new Square(),
                new Square(),
                new DarkBlue(),
                new Square(),
                new Square(),
                new Square(),
                new DarkBlue(),
                new Square(),
                new Square(),
                new Square(),
                new DarkBlue(),
                new Square()
        ));

        // Line 7
        this.squares.get(6).addAll(Arrays.asList(
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square()
        ));

        // Line 8
        this.squares.get(7).addAll(Arrays.asList(
                new Red(),
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square(),
                new Square(),
                new Star(),
                new Square(),
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square(),
                new Red()
        ));

        // Line 9
        this.squares.get(8).addAll(Arrays.asList(
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square()
        ));

        // Line 10
        this.squares.get(9).addAll(Arrays.asList(
                new Square(),
                new DarkBlue(),
                new Square(),
                new Square(),
                new Square(),
                new DarkBlue(),
                new Square(),
                new Square(),
                new Square(),
                new DarkBlue(),
                new Square(),
                new Square(),
                new Square(),
                new DarkBlue(),
                new Square()
        ));

        // Line 11
        this.squares.get(10).addAll(Arrays.asList(
                new Square(),
                new Square(),
                new Square(),
                new Square(),
                new Pink(),
                new Square(),
                new Square(),
                new Square(),
                new Square(),
                new Square(),
                new Pink(),
                new Square(),
                new Square(),
                new Square(),
                new Square()
        ));

        // Line 12
        this.squares.get(11).addAll(Arrays.asList(
                new SkyBlue(),
                new Square(),
                new Square(),
                new Pink(),
                new Square(),
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square(),
                new Square(),
                new Pink(),
                new Square(),
                new Square(),
                new SkyBlue()
        ));

        // Line 13
        this.squares.get(12).addAll(Arrays.asList(
                new Square(),
                new Square(),
                new Pink(),
                new Square(),
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square(),
                new Square(),
                new Pink(),
                new Square(),
                new Square()
        ));

        // Line 14
        this.squares.get(13).addAll(Arrays.asList(
                new Square(),
                new Pink(),
                new Square(),
                new Square(),
                new Square(),
                new DarkBlue(),
                new Square(),
                new Square(),
                new Square(),
                new DarkBlue(),
                new Square(),
                new Square(),
                new Square(),
                new Pink(),
                new Square()
        ));

        // Line 15
        this.squares.get(14).addAll(Arrays.asList(
                new Red(),
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square(),
                new Square(),
                new Red(),
                new Square(),
                new Square(),
                new Square(),
                new SkyBlue(),
                new Square(),
                new Square(),
                new Red()
        ));
    }

    /**
     * Builds the {@link LetterInterface}'s list according to the Scrabble rules.
     */
    protected void buildLettersList() {
        for (int i = 0; i < BoardInterface.BOARD_SIZE; i++) {
            this.letters.add(i, new ArrayList<>());

            for (int j = 0; j < BoardInterface.BOARD_SIZE; j++) {
                this.letters.get(i).add(null);
            }
        }
    }
}
