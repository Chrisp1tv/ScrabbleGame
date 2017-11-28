/**
 * 
 */
package istv.chrisanc.scrabble.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import istv.chrisanc.scrabble.exceptions.model.Bag.EmptyBagException;
import istv.chrisanc.scrabble.exceptions.model.Bag.NotEnoughLettersException;
import istv.chrisanc.scrabble.model.Player;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;
import istv.chrisanc.scrabble.model.interfaces.AIInterface;
import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;

/**
 * This class contains the behaviour of the AI
 * 
 * @author Julien Basquin
 */
public class AI extends Player implements AIInterface
{

	public AI(String name)
	{
		super(name, false);
	}

	//Draw letters from the bag until the AI has 7 letters or until the bag is empty
	@Override
	public void drawLetters(BagInterface bag)
	{
		while(this.getLetters().size() < 7)
			try {
				this.addLetter(bag.drawLetter());
			} catch (EmptyBagException e) {
				break;
			}
	}

	
	@Override
	public void playTurn(BoardInterface board, BagInterface bag, DictionaryInterface dictionary, LanguageInterface language) 
	{
		Map<WordInterface, Integer> listOfWords = new TreeMap<WordInterface, Integer>();
		
		//Finding all the words which can be played
			listOfWords = WordFinder.findWord(board, this, dictionary, language);
		
		//If words can be played, one of them is randomly chose and added to the list of played words
		//Else, a random amount of letters (between 0 and 7) are removed from the hand and put in the bag
		if(!listOfWords.isEmpty())
		{
			int number = (int)(Math.random()*(listOfWords.size()));
			Set<WordInterface> wordsSet = listOfWords.keySet();
			WordInterface[] words = wordsSet.toArray(new WordInterface[0]);
			WordInterface wordToPlay = words[number];

			board.addWord(wordToPlay);
			//Error : NullPointerException
			//this.increaseScore(listOfWords.get(wordToPlay));
			for(LetterInterface l:wordToPlay.getLetters())
				this.removeLetter(l);
		}
		else
		{
			List<LetterInterface> lettersToRemove = new ArrayList<LetterInterface>(); 
			int number = (int)Math.random()*(7+1);
			for(int i = 0; i < number; i++)
				lettersToRemove.add(this.letters.get(i));
				try {
					this.addLetters(bag.exchangeLetters(lettersToRemove));
				} catch (EmptyBagException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NotEnoughLettersException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		//At the end, letters are took from the bag if there are less than 7 letters in the hand
		drawLetters(bag);
	}
}