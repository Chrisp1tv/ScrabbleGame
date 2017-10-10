package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.SquareInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;
import istv.chrisanc.scrabble.model.squares.DarkBlue;
import istv.chrisanc.scrabble.model.squares.Pink;
import istv.chrisanc.scrabble.model.squares.Red;
import istv.chrisanc.scrabble.model.squares.SkyBlue;
import istv.chrisanc.scrabble.model.squares.Star;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    protected ObservableList<ObservableList<SquareInterface>> squares;

    /**
     * All the {@link LetterInterface} placed on the {@link SquareInterface}. The indexes represent the position of the {@link LetterInterface} on the {@link Board}.
     * The first index represents the lines, the second the columns.
     * There are 15 lines, and 15 columns.
     */
    protected List<List<LetterInterface>> letters;

    /**
     * All the {@link WordInterface} placed on the {@link Board}.
     */
    protected ObservableList<WordInterface> playedWords;

    public Board() {
        this.initialize();
        this.buildSquaresList();
        this.buildLettersList();
    }

    /**
     * @return a read-only list of the board's squares
     */
    @Override
    public ObservableList<ObservableList<SquareInterface>> getSquares() {
        return FXCollections.unmodifiableObservableList(this.squares);
    }

    @Override
    public List<List<LetterInterface>> getLetters() {
        return Collections.unmodifiableList(this.letters);
    }

    @Override
    public void addLetters(List<List<LetterInterface>> letters) {
        for (int i = 0, lettersLineSize = letters.size(); i < lettersLineSize; i++) {
            for (int j = 0, lettersColumnSize = letters.get(j).size(); j < lettersColumnSize; j++) {
                if (letters.get(i).get(j) != this.letters.get(i).get(j)) {
                    this.letters.get(i).set(j, letters.get(i).get(j));
                }
            }
        }
    }

    @Override
    public ObservableList<WordInterface> getPlayedWords() {
        return FXCollections.unmodifiableObservableList(this.playedWords);
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

    protected void initialize() {
        this.squares = FXCollections.observableArrayList();
        this.letters = FXCollections.observableArrayList();
        this.playedWords = FXCollections.observableArrayList();
    }

    /**
     * Builds the {@link SquareInterface}'s list according to the Scrabble rules.
     */
    protected void buildSquaresList() {
        this.squares.addAll(
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList(),
                FXCollections.observableArrayList()
        );

        // Line 1
        this.squares.get(0).addAll(
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
        );

        // Line 2
        this.squares.get(1).addAll(
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
        );

        // Line 3
        this.squares.get(2).addAll(
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
        );

        // Line 4
        this.squares.get(3).addAll(
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
        );

        // Line 5
        this.squares.get(4).addAll(
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
        );

        // Line 6
        this.squares.get(5).addAll(
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
        );

        // Line 7
        this.squares.get(6).addAll(
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
        );

        // Line 8
        this.squares.get(7).addAll(
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
        );

        // Line 9
        this.squares.get(8).addAll(
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
        );

        // Line 10
        this.squares.get(9).addAll(
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
        );

        // Line 11
        this.squares.get(10).addAll(
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
        );

        // Line 12
        this.squares.get(11).addAll(
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
        );

        // Line 13
        this.squares.get(12).addAll(
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
        );

        // Line 14
        this.squares.get(13).addAll(
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
        );

        // Line 15
        this.squares.get(14).addAll(
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
        );
    }

    /**
     * Builds the {@link LetterInterface}'s list according to the Scrabble rules.
     */
    protected void buildLettersList() {
        for (int i = 0; i < BoardInterface.BOARD_SIZE; i++) {
            this.letters.add(i, FXCollections.observableArrayList());

            for (int j = 0; j < BoardInterface.BOARD_SIZE; j++) {
                this.letters.get(i).add(null);
            }
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        SquareInterface[][] squares = new SquareInterface[BoardInterface.BOARD_SIZE][BoardInterface.BOARD_SIZE];
        LetterInterface[][] letters = new LetterInterface[BoardInterface.BOARD_SIZE][BoardInterface.BOARD_SIZE];

        for(int i = 0; i < BoardInterface.BOARD_SIZE; i++) {
            for (int j = 0; j < BoardInterface.BOARD_SIZE; j++) {
                squares[i][j] = this.squares.get(i).get(j);
                letters[i][j] = this.letters.get(i).get(j);
            }
        }

        objectOutputStream.writeObject(squares);
        objectOutputStream.writeObject(letters);
        objectOutputStream.writeObject(this.playedWords.stream().collect(Collectors.toList()));
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.initialize();

        SquareInterface[][] squares = (SquareInterface[][]) objectInputStream.readObject();
        LetterInterface[][] letters = (LetterInterface[][]) objectInputStream.readObject();

        for (int i = 0; i < BoardInterface.BOARD_SIZE; i++) {
            this.squares.add(i, FXCollections.observableArrayList(squares[i]));
            this.letters.add(i, FXCollections.observableArrayList(letters[i]));
        }

        List<WordInterface> playedWords = (List<WordInterface>) objectInputStream.readObject();
        this.playedWords.addAll(playedWords);
    }
}
