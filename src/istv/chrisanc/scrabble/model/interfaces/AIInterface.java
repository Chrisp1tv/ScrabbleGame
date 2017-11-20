package istv.chrisanc.scrabble.model.interfaces;

import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;

/**
 * This class contains the behaviour of the AI
 * 
 * @author Julien
 */
public interface AIInterface extends PlayerInterface 
{
	/**
	 * Draw a number of letters from the bag
	 * 
	 * @param bag				Bag containing the letters
	 */
	void drawLetters(BagInterface bag);
	
	/**
	 * Play a turn like a human player would do (play a word, skip the turn or reject letters)
	 * 
	 * @param board			Board of the game
	 * @param dictionary	Dictionary where words are found
	 */
	void playTurn(BoardInterface board, BagInterface bag, DictionaryInterface dictionary, LanguageInterface language);
}