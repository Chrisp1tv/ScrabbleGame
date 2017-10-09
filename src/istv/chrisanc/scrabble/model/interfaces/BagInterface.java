package istv.chrisanc.scrabble.model.interfaces;

import java.util.List;

import istv.chrisanc.scrabble.exceptions.model.Bag.EmptyBagException;

/**
 * <p>
 * This class represents the bag used in a Scrabble game. It holds in all the pieces ({@link LetterInterface}) at the start
 * of the game, and manages the draw, exchanges of {@link LetterInterface} etc.
 *
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 */
public interface BagInterface {
    LetterInterface drawLetter() throws EmptyBagException;

    List<LetterInterface> exchangeLetters(List<LetterInterface> letterToPutBackInTheBag) throws EmptyBagException;
}
