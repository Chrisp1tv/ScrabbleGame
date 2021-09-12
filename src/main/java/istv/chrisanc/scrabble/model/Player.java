package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 * @see PlayerInterface
 */
public abstract class Player implements PlayerInterface, Serializable {
    /**
     * The name of the player
     */
    protected String name;

    /**
     * The score of the player
     * The default value is 0, as a player begins the game without any point
     */
    protected IntegerProperty score;

    /**
     * The letters owned by the player in his rack/hands during the game
     * He can't have more than 7 letters.
     */
    protected List<LetterInterface> letters;

    protected Player(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public int getScore() {
        return this.score.get();
    }

    protected void setScore(int score) {
        this.score.set(score);
    }

    @Override
    public void increaseScore(int increment) {
        this.score.set(this.score.get() + increment);
    }

    @Override
    public void decreaseScore(int decrement) {
        if (decrement > this.score.get()) {
            this.score.set(0);
        } else {
            this.score.set(this.score.get() - decrement);
        }
    }

    @Override
    public ReadOnlyIntegerProperty scoreProperty() {
        return IntegerProperty.readOnlyIntegerProperty(this.score);
    }

    /**
     * @return a read-only list of the owned {@link LetterInterface} by the player
     */
    @Override
    public List<LetterInterface> getLetters() {
        return Collections.unmodifiableList(this.letters);
    }

    @Override
    public void addLetter(LetterInterface letter) {
        this.letters.add(letter);
    }

    @Override
    public void addLetters(Collection<LetterInterface> lettersToAdd) {
        this.letters.addAll(lettersToAdd);
    }

    @Override
    public void removeLetters(Collection<LetterInterface> letters) {
        // We don't use removeAll because removeAll use equals, and remove all occurrences of the same letter
        for (LetterInterface letter : letters) {
            this.letters.remove(letter);
        }
    }

    @Override
    public void removeLetter(LetterInterface letter) {
        this.letters.remove(letter);
    }

    @Override
    public void removeLetter(short index) {
        this.letters.remove(index);
    }

    protected void initialize() {
        this.letters = new ArrayList<>();
        this.score = new SimpleIntegerProperty(0);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.name);
        objectOutputStream.writeInt(this.score.getValue());
        objectOutputStream.writeObject(this.letters);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.initialize();
        this.setName((String) objectInputStream.readObject());
        this.setScore(objectInputStream.readInt());

        @SuppressWarnings("unchecked") List<LetterInterface> letters = (List<LetterInterface>) objectInputStream.readObject();
        this.letters.addAll(letters);
    }
}
