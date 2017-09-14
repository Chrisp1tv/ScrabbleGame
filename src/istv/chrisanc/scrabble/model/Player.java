package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

/**
 * <p>
 * This class represents a player of the Scrabble. The player has {@link LetterInterface} in his rack/hands, a score, a name etc.
 *
 * @author Christopher Anciaux
 */
public class Player implements PlayerInterface {
    /**
     * The name of the player
     */
    protected StringProperty name;

    /**
     * The score of the player. The default value is 0, as a player begins the game without any point
     */
    protected IntegerProperty score = new SimpleIntegerProperty(0);

    /**
     * The letters owned by the player in his rack/hands during the game. He can't have more than 7 letters.
     */
    protected ObservableList<LetterInterface> letters = FXCollections.observableArrayList();

    /**
     * @param name The player's name
     */
    public Player(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public int getScore() {
        return score.get();
    }

    public PlayerInterface increaseScore(int increment) {
        this.score.set(this.score.get() + increment);

        return this;
    }

    /**
     * @return a read-only list of the owned {@link LetterInterface} by the player
     */
    public List<LetterInterface> getLetters() {
        return FXCollections.unmodifiableObservableList(this.letters);
    }

    /**
     * @param letter the {@link LetterInterface} to be added in the rack of the player
     */
    public void addLetter(LetterInterface letter) {
        // TODO

        return this;
    }

    /**
     * @param letter the {@link LetterInterface} to be removed from the rack of the player
     */
    public void removeLetter(LetterInterface letter) {
        // TODO

        return this;
    }

    /**
     * @param index the index of the {@link LetterInterface} to be removed from the rack of the player
     */
    public void removeLetter(short index) {
        // TODO

        return this;
    }
}
