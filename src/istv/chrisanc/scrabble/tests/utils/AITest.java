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

import istv.chrisanc.scrabble.model.interfaces.AIInterface;
import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.Bag;
import istv.chrisanc.scrabble.model.Board;
import istv.chrisanc.scrabble.model.Player;
import istv.chrisanc.scrabble.model.Word;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.languages.French.French;
import istv.chrisanc.scrabble.model.languages.French.FrenchDictionary;
import istv.chrisanc.scrabble.utils.AI;
import istv.chrisanc.scrabble.utils.LetterToStringTransformer;

public class AITest {
	private static AIInterface AI;
	private static BagInterface bag;
	private static BoardInterface board;
	private static DictionaryInterface dictionary;
	private static LanguageInterface language;
	private static PlayerInterface player;

	@Before
	public void setUp() throws Exception {
		AI = new AI("AI");
		language = new French();
		dictionary = FrenchDictionary.getInstance();
		bag = new Bag(language.getBagLettersDistribution());
		board = new Board();
		player = new Player("test", false);
	}

	@Test
	public void testDrawLetters() {
		assertEquals(0, AI.getLetters().size());
		AI.drawLetters(bag);
		assertEquals(7, AI.getLetters().size());
	}

	@Test
	public void testPlayTurn() {
		assertEquals(0, board.getPlayedWords().size());
		AI.drawLetters(bag);
		AI.playTurn(board, bag, dictionary, language);
		assertEquals(1, board.getPlayedWords().size());
		AI.playTurn(board, bag, dictionary, language);
		assertEquals(2, board.getPlayedWords().size());
	}

}