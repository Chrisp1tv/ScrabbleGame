package istv.chrisanc.scrabble.model.interfaces;

import istv.chrisanc.scrabble.exceptions.Bag.EmptyBagException;

/**
 * <p>
 * This class represents the bag used in a Scrabble game. It holds in all the pieces ({@link LetterInterface}) at the start
 * of the game, and manages the draw, exchanges of {@link LetterInterface} etc.
 *
 * @author Christopher Anciaux
 */
public interface BagInterface {
    LetterInterface drawLetter() throws EmptyBagException;

    LetterInterface exchangeLetters(LetterInterface letterToPutBackInTheBag) throws EmptyBagException;
}
