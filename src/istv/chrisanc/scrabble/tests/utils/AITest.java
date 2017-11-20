package istv.chrisanc.scrabble.tests.utils;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
* @author Julien Basquin
*
*/

import org.junit.Before;
import org.junit.Test;

import istv.chrisanc.scrabble.utils.interfaces.AIInterface;
import istv.chrisanc.scrabble.utils.interfaces.DictionaryInterface;
import istv.chrisanc.scrabble.model.Bag;
import istv.chrisanc.scrabble.model.Board;
import istv.chrisanc.scrabble.model.Word;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.letters.B;
import istv.chrisanc.scrabble.model.letters.M;
import istv.chrisanc.scrabble.model.letters.N;
import istv.chrisanc.scrabble.model.letters.O;
import istv.chrisanc.scrabble.model.letters.T;
import istv.chrisanc.scrabble.utils.AI;
import istv.chrisanc.scrabble.utils.dictionaries.DictionaryFactory;

public class AITest {
	private static AIInterface AI;
	private static BagInterface bag;
	private static BoardInterface board;
	private static DictionaryInterface dictionary;

	@Before
	public void setUp() throws Exception {
		AI = new AI("AI");
		bag = new Bag();
		board = new Board();
		List<LetterInterface> letters =  new ArrayList<LetterInterface>();
		letters = Arrays.asList(new B(), new O(), new N());
		board.addWord(new Word(letters, true, (short)6, (short)6));
		DictionaryFactory dictionaryFactory = new DictionaryFactory();
		dictionary = dictionaryFactory.getDictionary(DictionaryFactory.FRENCH);
	}

	@Test
	public void testDrawLetters() {
		assertEquals(0, AI.getLetters().size());
		AI.drawLetters(bag);
		assertEquals(7, AI.getLetters().size());
	}

	@Test
	public void testPlayTurn() {
		AI.drawLetters(bag);
		AI.playTurn(board, bag, dictionary);
		assertEquals(2, board.getPlayedWords().size());
	}

}