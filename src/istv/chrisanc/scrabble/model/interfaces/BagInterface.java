package istv.chrisanc.scrabble.model.interfaces;

import istv.chrisanc.scrabble.exceptions.model.Bag.EmptyBagException;
import istv.chrisanc.scrabble.exceptions.model.Bag.NotEnoughLettersException;

import java.util.List;

/**
 * This class represents the bag used in a Scrabble game. It holds in all the letters at the start of the game, and manages
 * the draw, exchanges of letters etc.
 *
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 */
public interface BagInterface {
    /**
     * The minimum number of letters that the bag must have to proceed to an exchange
     */
    short MINIMAL_NUMBER_OF_LETTERS_TO_EXCHANGE = 7;

    /**
     * Draws and returns a letter from the bag, randomly
     *
     * @return The drawn letter
     * @throws EmptyBagException if the bag is empty, that is to say that no letter can be drawn
     */
    LetterInterface drawLetter() throws EmptyBagException;

    /**
     * Exchanges given letters against drawn ones and returns the same number of letters as the given letters
     *
     * @param lettersToPutBackInTheBag The letters given in exchange of the exchanged letters
     *
     * @return the drawn letters
     * @throws EmptyBagException         if the bag is empty, that is to say that no letter can be drawn
     * @throws NotEnoughLettersException if the bag hasn't enough letters to proceed to the exchange
     */
    List<LetterInterface> exchangeLetters(List<LetterInterface> lettersToPutBackInTheBag) throws EmptyBagException, NotEnoughLettersException;

    /**
     * Add back the given letters to the bag
     *
     * @param letters The letters to be added
     */
    void putBackLetters(List<LetterInterface> letters);

    /**
     * Add back the given letters to the bag
     *
     * @param letter The letter to be added
     */
    void putBackLetter(LetterInterface letter);

    /**
     * @return true if the bag hasn't any letter anymore, false otherwise
     */
    boolean isEmpty();
}
