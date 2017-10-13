package istv.chrisanc.scrabble.model.interfaces;

import istv.chrisanc.scrabble.exceptions.model.Bag.EmptyBagException;
import istv.chrisanc.scrabble.exceptions.model.Bag.NotEnoughLettersException;

import java.util.List;

/**
 * <p>
 * This class represents the bag used in a Scrabble game. It holds in all the pieces ({@link LetterInterface}) at the start
 * of the game, and manages the draw, exchanges of {@link LetterInterface} etc.
 *
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 */
public interface BagInterface {
    /**
     * The minimum number of letters that the bag must have to proceed to an exchange
     */
    short MINIMAL_NUMBER_OF_LETTERS_TO_EXCHANGE = 7;

    LetterInterface drawLetter() throws EmptyBagException;

    List<LetterInterface> exchangeLetters(List<LetterInterface> lettersToPutBackInTheBag) throws EmptyBagException, NotEnoughLettersException;

    boolean isEmpty();
}
