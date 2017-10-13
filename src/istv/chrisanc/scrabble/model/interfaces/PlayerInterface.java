package istv.chrisanc.scrabble.model.interfaces;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.collections.ObservableList;

import java.util.Collection;
import java.util.List;

/**
 * <p>
 * This class represents a player of the Scrabble. The player has {@link LetterInterface} in his rack/hands, a score, a name etc.
 *
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 */
public interface PlayerInterface {
    /**
     * The number of letters that each player should have in his rack, until it isn't possible.
     */
    short BASE_NUMBER_OF_LETTERS = 7;

    String getName();

    int getScore();

    void increaseScore(int increment);

    void decreaseScore(int decrement);

    ReadOnlyIntegerProperty scoreProperty();

    List<LetterInterface> getLetters();

    void addLetter(LetterInterface letter);

    void addLetters(Collection<LetterInterface> lettersToAdd);

    void removeLetter(LetterInterface letter);

    void removeLetters(Collection<LetterInterface> letters);

    void removeLetter(short index);

    ObservableList<LetterInterface> lettersProperty();
}
