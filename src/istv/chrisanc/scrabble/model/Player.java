package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.utils.WordFinder;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;
import istv.chrisanc.scrabble.exceptions.NoHelpException;
import istv.chrisanc.scrabble.exceptions.utils.LetterToStringTransformationException;
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
import java.util.Map;
import java.util.Set;

/**
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 * @see PlayerInterface
 */
public class Player implements PlayerInterface, Serializable {
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
     * Number of hint the player can get.
     * the value is set to 5 at the begin and decrease for each help the played asks.
     */

    protected IntegerProperty help;


    public Player(String name, boolean human) {
        this.initialize();
        this.human = human;
        this.name = name;
        if(human){
        	this.help = new SimpleIntegerProperty(5);
        } else{
        	this.help = new SimpleIntegerProperty(-1);
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

    protected void initialize() {
        this.letters = new ArrayList<>();
        this.score = new SimpleIntegerProperty(0);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeBoolean(this.human);
        objectOutputStream.writeObject(this.name);
        objectOutputStream.writeInt(this.score.getValue());
        objectOutputStream.writeObject(this.letters);
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.initialize();

        this.setHuman(objectInputStream.readBoolean());
        this.setName((String) objectInputStream.readObject());
        this.setScore(objectInputStream.readInt());

        List<LetterInterface> letters = (List<LetterInterface>) objectInputStream.readObject();
        this.letters.addAll(letters);
    }

    /**
     * Decrease the number of hints everytime the button "Help" is clicked
     */

    public void decreaseHelp(){

    	// Test if the player is a Human not an IA

    	if(human) {

    		// Test if the player has the possibility to get a hint

    	if(this.help.get()==0) {
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("Ouups !");
    		alert.setHeaderText(null);
    		alert.setContentText("Vous n'avez plus d\'aides possibles !");
    		alert.showAndWait();
    	} else {

    		// Decrease Help

    	this.help.set(this.help.get()-1);
    	}
    	}
    }

    // Return Help as Integer to display it on the board

	@Override
	public int getHelp() {
		return this.help.get();
	}

    @Override
    public ReadOnlyIntegerProperty playerHelpProperty() {
        return IntegerProperty.readOnlyIntegerProperty(this.help);
    }


    /*
     * Let the player have a hint in order to know what is the best word he can do
     * for a price of 1 help.
     * If the player has no help anymore, it return "0"
     * the list returned by findWord function must be sorted by score
     */

    public WordInterface help(BoardInterface board,DictionaryInterface dictionary) throws NoHelpException{
    	if(this.help.get()>0){
    		this.decreaseHelp();
    		WordFinder WF = new WordFinder();
    		List<WordInterface> playableWords = null;
			try {
				playableWords = (List<WordInterface>) WF.findWord(board, this, dictionary);
			} catch (LetterToStringTransformationException e) {
				e.printStackTrace();
			}
    		return playableWords.get(playableWords.size());
    	}else{
    		throw new NoHelpException();
    	}
    }

}
