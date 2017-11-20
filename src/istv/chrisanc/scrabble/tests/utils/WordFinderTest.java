/**
 * 
 */
package istv.chrisanc.scrabble.tests.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import istv.chrisanc.scrabble.model.Board;
import istv.chrisanc.scrabble.model.Player;
import istv.chrisanc.scrabble.model.Word;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;
import istv.chrisanc.scrabble.model.languages.French.French;
import istv.chrisanc.scrabble.model.languages.French.FrenchDictionary;
import istv.chrisanc.scrabble.utils.LetterToStringTransformer;
import istv.chrisanc.scrabble.utils.DictionaryFactory;
import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;

/**
 * @author Julien Basquin
 *
 */
public class WordFinderTest {
	private static LanguageInterface language;
	private DictionaryInterface dictionary;
	private List<LetterInterface> letters;
	private BoardInterface board;
	private WordInterface mot;
	private Player player;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		language = new French();
		dictionary = FrenchDictionary.getInstance();
		letters = new ArrayList<LetterInterface>();
		board = new Board();
		player = new Player("test", true);
	}

	/*@Test
	public void wordsFormedWithPlayersLetters() {
		letters = Arrays.asList(new A(), new B(), new C());
		Set<String> words = new HashSet<String>();
		int size = 0;
		for(int i = 0; i < letters.size(); i++)
		{
			for(String d : dictionary.getWords())
				if(d.contains(LetterToStringTransformer.transform(letters.get(i))))
					words.add(d);
			assertTrue(size <= words.size());
			size = words.size();
		}
	}*/
	
	
	@Test
	public void wordsFormedWithAlreadyPlayedWords(){
		letters = Arrays.asList(LetterToStringTransformer.reverseTransform("M", language),
					LetterToStringTransformer.reverseTransform("O", language),
					LetterToStringTransformer.reverseTransform("T", language));
		mot = new Word(player, letters, true, (short)6, (short)6);
		board.addWord(mot);
		
		for(WordInterface w:board.getPlayedWords())
    	{
    		Set<String> words = new HashSet<String>();
    		
    		for(LetterInterface l : w.getLetters())
    		{
    			for(String s:dictionary.getWords())
    				if(s.contains(LetterToStringTransformer.transform(l)))
    					words.add(s);
    		}
    		assertNotEquals(0, words.size());
    	}
		
	}
}