package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.exceptions.Bag.EmptyBagException;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * <p>
 * This class represents the bag used in a Scrabble game. It holds in all the pieces ({@link LetterInterface}) at the start
 * of the game, and manages the draw, exchanges of {@link LetterInterface} etc.
 *
 * @author Christopher Anciaux
 */
public class Bag implements BagInterface {
    /**
     * The letters that the bag contain
     */
    protected ObservableList<LetterInterface> letters = FXCollections.observableArrayList();

    public Bag() {
        // TODO: Fill-in the bag with the needed letters at the game's begin (according the Scrabble rules)
    }

    public LetterInterface drawLetter() throws EmptyBagException {
        // TODO
    }

    public LetterInterface exchangeLetters(LetterInterface letterToPutBackInTheBag) throws EmptyBagException {
        // TODO
    }
}
