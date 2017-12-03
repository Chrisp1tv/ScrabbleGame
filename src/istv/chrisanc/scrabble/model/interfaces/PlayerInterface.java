package istv.chrisanc.scrabble.model.interfaces;

import javafx.beans.property.ReadOnlyIntegerProperty;

import java.util.Collection;
import java.util.List;

/**
 * This class represents a player of the Scrabble. The player has letters in his rack/hands, a score, a name etc.
 *
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 */
public interface PlayerInterface {
    /**
     * The number of letters that each player should have in his rack, until it isn't possible (because the bag is empty,
     * for example)
     */
    short BASE_NUMBER_OF_LETTERS = 7;

    String getName();

    int getScore();

    /**
     * Increases the score by the given value
     *
     * @param increment The number of points to add to the score
     */
    void increaseScore(int increment);

    /**
     * Decreases the score by the given value
     *
     * @param decrement The number of points to add to the score
     */
    void decreaseScore(int decrement);

    ReadOnlyIntegerProperty scoreProperty();

    List<LetterInterface> getLetters();

    void addLetter(LetterInterface letter);

    void addLetters(Collection<LetterInterface> lettersToAdd);

    void removeLetter(LetterInterface letter);

    void removeLetters(Collection<LetterInterface> letters);

    void removeLetter(short index);

    int getAvailableHelps();

    void decreaseAvailableHelps();

    ReadOnlyIntegerProperty availableHelpsProperty();
}
