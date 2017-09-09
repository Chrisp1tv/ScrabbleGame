package istv.chrisanc.scrabble;

import istv.chrisanc.scrabble.exceptions.Bag.EmptyBagException;
import istv.chrisanc.scrabble.interfaces.LetterInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * This class represents the bag used in a Scrabble game. It holds in all the pieces ({@link LetterInterface}) at the start
 * of the game, and manages the draw, exchanges of {@link LetterInterface} etc.
 *
 * @author Christopher Anciaux
 */
public class Bag implements istv.chrisanc.scrabble.interfaces.BagInterface {
    /**
     * The letters that the bag contain
     */
    protected List<LetterInterface> letters = new ArrayList<>();

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
