package istv.chrisanc.scrabble.model.interfaces;

import javafx.beans.property.IntegerProperty;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * <p>
 * This class represents a player of the Scrabble. The player has {@link LetterInterface} in his rack/hands, a score, a name etc.
 *
 * @author Christopher Anciaux
 */
public interface PlayerInterface {
    String getName();

    int getScore();

    void increaseScore(int increment);

    IntegerProperty scoreProperty();

    List<LetterInterface> getLetters();

    void addLetter(LetterInterface letter);

    void addLetters(List<LetterInterface> letters);

    void removeLetter(LetterInterface letter);

    void removeLetter(short index);

    ObservableList<LetterInterface> lettersProperty();
}
