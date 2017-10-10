package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * This class represents a player of the Scrabble. The player has {@link LetterInterface} in his rack/hands, a score, a name etc.
 *
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 */
public class Player implements PlayerInterface, Serializable {
    /**
     * The name of the player
     */
    protected StringProperty name;

    /**
     * The score of the player. The default value is 0, as a player begins the game without any point
     */
    protected IntegerProperty score;

    /**
     * The letters owned by the player in his rack/hands during the game. He can't have more than 7 letters.
     */
    protected ObservableList<LetterInterface> letters;

    /**
     * @param name The player's name
     */
    public Player(String name) {
        this.initialize();
        this.name.set(name);
    }

    public String getName() {
        return name.get();
    }

    @Override
    public int getScore() {
        return score.get();
    }

    @Override
    public void increaseScore(int increment) {
        this.score.set(this.score.get() + increment);
    }

    @Override
    public IntegerProperty scoreProperty() {
        return score;
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
        this.letters.add(letter);
    }

    public void addLetters(List<LetterInterface> lettersToAdd) {
        this.letters.addAll(lettersToAdd);
    }

    public void addLetters(List<LetterInterface> letters) {
        // TODO
    }

    /**
     * @param letter the {@link LetterInterface} to be removed from the rack of the player
     */
    public void removeLetter(LetterInterface letter) {
        this.letters.remove(letter);
    }

    /**
     * @param index the index of the {@link LetterInterface} to be removed from the rack of the player
     */
    public void removeLetter(short index) {
        this.letters.remove(index);
    }

    @Override
    public ObservableList<LetterInterface> lettersProperty() {
        return letters;
    }

    protected void setName(String name) {
        this.name.set(name);
    }

    protected void setScore(int score) {
        this.score.set(score);
    }

    protected void initialize() {
        this.name = new SimpleStringProperty();
        this.score = new SimpleIntegerProperty(0);
        this.letters = FXCollections.observableArrayList();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.name.getValue());
        objectOutputStream.writeInt(this.score.getValue());
        objectOutputStream.writeObject(this.letters.stream().collect(Collectors.toList()));
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.initialize();

        this.setName((String) objectInputStream.readObject());
        this.setScore(objectInputStream.readInt());

        List<LetterInterface> letters = (List<LetterInterface>) objectInputStream.readObject();
        this.letters.addAll(letters);
    }
}
