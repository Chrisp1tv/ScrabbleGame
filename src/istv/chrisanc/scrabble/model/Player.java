package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

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
public class Player implements PlayerInterface, Serializable {
    protected static final int NUMBER_OF_HELPS_FOR_HUMAN_PLAYER = 5;

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

    /**
     * If the player is a human player, the value is set to true, false otherwise
     */
    protected boolean human;

    /**
     * The available helps of the player
     */
    protected IntegerProperty availableHelps;

    public Player(String name, boolean human) {
        this.initialize();
        this.human = human;
        this.name = name;
        if (this.isHuman()) {
            this.availableHelps = new SimpleIntegerProperty(Player.NUMBER_OF_HELPS_FOR_HUMAN_PLAYER);
        }
    }

    public String getName() {
        return this.name;
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
    public void decreaseScore(int decrement) {
        this.score.set(this.score.get() - decrement);
    }

    @Override
    public ReadOnlyIntegerProperty scoreProperty() {
        return IntegerProperty.readOnlyIntegerProperty(this.score);
    }

    public int getAvailableHelps() {
        return availableHelps.get();
    }

    @Override
    public ReadOnlyIntegerProperty availableHelpsProperty() {
        return IntegerProperty.readOnlyIntegerProperty(this.availableHelps);
    }

    @Override
    public void decreaseAvailableHelps() {
    	if(availableHelps.get()==0) {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Ouuups !");
    		alert.setHeaderText(null);
    		alert.setContentText("You have used all the available helps !");

    		alert.showAndWait();
    	} else {
    		this.setAvailableHelps(this.getAvailableHelps() - 1);
    	}
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

    @Override
    public boolean isHuman() {
        return this.human;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected void setScore(int score) {
        this.score.set(score);
    }

    protected void setHuman(boolean human) {
        this.human = human;
    }

    protected void setAvailableHelps(int availableHelps) {
        this.availableHelps.set(availableHelps);
    }

    protected void initialize() {
        this.letters = new ArrayList<>();
        this.score = new SimpleIntegerProperty(0);
        this.availableHelps = new SimpleIntegerProperty(0);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeBoolean(this.human);
        objectOutputStream.writeObject(this.name);
        objectOutputStream.writeInt(this.score.getValue());
        objectOutputStream.writeInt(this.getAvailableHelps());
        objectOutputStream.writeObject(this.letters);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.initialize();

        this.setHuman(objectInputStream.readBoolean());
        this.setName((String) objectInputStream.readObject());
        this.setScore(objectInputStream.readInt());
        this.setAvailableHelps(objectInputStream.readInt());

        List<LetterInterface> letters = (List<LetterInterface>) objectInputStream.readObject();
        this.letters.addAll(letters);
    }
}
