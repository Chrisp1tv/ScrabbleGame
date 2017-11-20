package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.exceptions.utils.LetterToStringTransformationException;
import istv.chrisanc.scrabble.model.Word;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;
import istv.chrisanc.scrabble.model.letters.Joker;
import istv.chrisanc.scrabble.model.letters.P;
import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * <p>
 * This class handles finding the words that could be played on the given {@link BoardInterface}.
 *
 * @author Christopher Anciaux 
 * @author Anthone Delétré
 * @author Julien Basquin
 */
public class WordFinder {
    /**
     * Search and return all the words who can be played on a given board
     *
     * @param board 		The board of the game
     * @param player		The player, used to get his letters
     * @param dictionary	A dictionary containing all the words
     *
     * @return A list of playable words sorted by their score
     */
    public static Set<WordInterface> findWord(BoardInterface board, PlayerInterface player, DictionaryInterface dictionary) throws LetterToStringTransformationException {
    	//Creation of 2 Lists :
    	//- a list of the player's letters
    	//- a list of words formed with the first list
    	LetterInterface lettersAvailable;
    	Set<String> wordsWithLettersAvailable = new HashSet<String>();
    	
    	//TODO Ajouter et tester cas avec Joker
    	/*if(player.getLetters().contains(Joker.class))
    		wordsWithLettersAvailable = dictionary.getWords();
    	else
    	{*/
    		//Add to the list of words all the words which contains the available letters
    		for(int i = 0; i < player.getLetters().size(); i++)
    		{
    			lettersAvailable = player.getLetters().get(i);
    			for(String s : dictionary.getWords())
    				if(!wordsWithLettersAvailable.contains(s) && s.contains(LetterToStringTransformer.transform(lettersAvailable)))
    					wordsWithLettersAvailable.add(s);
    		}
    	/*}*/
    	
    	//Creation of a map with :
    	//- words played on the board (key)
    	//- a list of words who can be played on a placed word (values)
    	Map<WordInterface, Set<String>> playableWords = new HashMap<WordInterface, Set<String>>();
    	
    	Set<WordInterface> wordListOfPlayableWords = new HashSet<WordInterface>();
    	Set<WordInterface> finalList = new HashSet<WordInterface>();
    	
    	//A list of playable words are selected from a dictionary and associated with an already played word
    	//These playable words are based on already played words (matching letters)
    	for(WordInterface w:board.getPlayedWords())
    	{
    		//List of String containing all the playable words for an already played word
    		//Playable words are stocked if they share at least one letter with the played word
    		Set<String> words = new HashSet<String>();
    		
    		for(LetterInterface l : w.getLetters())
    		{
    			for(String s:dictionary.getWords())
    				if(!words.contains(s) && s.contains(LetterToStringTransformer.transform(l)))
    					words.add(s);
    		}
    		
    		Set<String> wordsToAdd = new HashSet<String>();
    		for(String s:words)
    		{
    			if(wordsWithLettersAvailable.contains(s))
    				wordsToAdd.add(s);
    		}
    		
    		//Conversion of the playable words's list into a String HashSet
    		//Playable words are linked with a played word
    		playableWords.put(w, wordsToAdd);
    		
    		//Conversion of playable words into a list of their letters
    		//For each playable word (String) is associated their letters (List<LetterInterface>)
    		Set<String> listOfPlayableWords = playableWords.get(w);
    		Map<String, List<LetterInterface>> listOfPlayableWordsWithLetters = new HashMap<String, List<LetterInterface>>();
    		//TODO Long, � v�rifier
    		for(String p : listOfPlayableWords)
    			listOfPlayableWordsWithLetters.put(p, LetterListToStringTransformer.reverseTransform(p));

    		//Creation of a list to stock words from the dictionary and containing the played word (w)
    		List<String> listOfWordsContainingTheWord = dictionary.findWordsHavingLettersInOrder(w.getLetters());
    		
    		//Creation of 4 lists to stock words
    		List<String> horizontalPlayableWordsMatched = new ArrayList<String>();
    		List<String> verticalPlayableWordsMatched = new ArrayList<String>();
    		Set<String> playableWordsStartingWithPlayedWordsMatched = new HashSet<String>();
			Set<String> playableWordsEndingWithPlayedWordsMatched = new HashSet<String>();
    		
    		Set<Entry<String, List<LetterInterface>>> listOfPlayableWordsToCheck = listOfPlayableWordsWithLetters.entrySet();
    		int cpt = 0;
    		for(Entry<String, List<LetterInterface>> p : listOfPlayableWordsToCheck)
    		{
    			cpt++;
    			System.out.println(cpt);
    			if(wordsWithLettersAvailable.contains(p.getKey()))
    			{
    				//If the played word is horizontal
    				if(w.isHorizontal())
    				{
    					//If the possible word is horizontal (letters added before and/or after the played word) and bigger than the played word
    					if(listOfWordsContainingTheWord.contains(p.getKey()))
    						horizontalPlayableWordsMatched.add(p.getKey());
    					else
    						verticalPlayableWordsMatched.add(p.getKey());
    				}
    				else
    				{
    					if(listOfWordsContainingTheWord.contains(p.getKey()))
    						verticalPlayableWordsMatched.add(p.getKey());
    					else
    						horizontalPlayableWordsMatched.add(p.getKey());
    				}
    			}
    		}
			//List<String> testList = dictionary.findWordsHavingLettersInOrder(w.getLetters());
    		List<LetterInterface> listOfLettersAvailable = player.getLetters();
    			//Finding all the words in the dictionary starting with the played word
    			//(found words cannot be smaller than the played word based on or longer than the board's size)
				//TODO OPTIMIZE + MODIFIER : Les mots ne semblent pas se r�cup�rer
    			playableWordsStartingWithPlayedWordsMatched.addAll(dictionary.findWordsStartingWithAndHavingLetters(w.getLetters().size(), BoardInterface.BOARD_SIZE, w.getLetters(), listOfLettersAvailable));
				//Finding all the words in the dictionary ending with the played word
				//(found words cannot be smaller than the played word based on or longer than the board's size)
				//TODO OPTIMIZE + MODIFIER : Les mots ne semblent pas se r�cup�rer
    			playableWordsEndingWithPlayedWordsMatched.addAll(dictionary.findWordsEndingWithAndHavingLetters(w.getLetters().size(), BoardInterface.BOARD_SIZE, w.getLetters(), listOfLettersAvailable));
				
				//Deleting the two lists above from the playable words's list of the position (horizontal or vertical).
				//The remaining list contains playable words of the same position which neither. begin or end by the played word
				if(w.isHorizontal())
				{
					horizontalPlayableWordsMatched.removeAll(playableWordsStartingWithPlayedWordsMatched);
					horizontalPlayableWordsMatched.removeAll(playableWordsEndingWithPlayedWordsMatched);
				}
				else
				{
					verticalPlayableWordsMatched.removeAll(playableWordsStartingWithPlayedWordsMatched);
					verticalPlayableWordsMatched.removeAll(playableWordsEndingWithPlayedWordsMatched);
				}
				
				/*//Creation of lists containing playable words (with their position) matching words which can be played according to the player's letters
				Set<String> horizontalPlayableWordsMatched = new HashSet<String>();
				Set<String> verticalPlayableWordsMatched = new HashSet<String>();
				Set<String> playableWordsStartingWithPlayedWordsMatched = new HashSet<String>();
				Set<String> playableWordsEndingWithPlayedWordsMatched = new HashSet<String>();
				
				//Checking if playable words can be created with the player's letters
				for(String s : wordsWithLettersAvailable)
				{
					if(horizontalPlayableWords.contains((String)s))
						horizontalPlayableWordsMatched.add(s);
					
					if(verticalPlayableWords.contains((String)s))
						verticalPlayableWordsMatched.add(s);
					
					if(playableWordsStartingWithPlayedWords.contains((String)s))
						playableWordsStartingWithPlayedWordsMatched.add(s);
					
					if(playableWordsEndingWithPlayedWords.contains((String)s))
						playableWordsEndingWithPlayedWordsMatched.add(s);
				}*/
				System.out.println(wordListOfPlayableWords.size());
				//Conversion of playable words from String to Letters and then to Word
				for(String s : playableWordsStartingWithPlayedWordsMatched)
				{
					List<LetterInterface> conversion = LetterListToStringTransformer.reverseTransform(s);
					WordInterface word;
					if(w.isHorizontal())
						word = new Word(conversion, true, w.getStartLine(), w.getStartColumn());
					else
						word = new Word(conversion, false, w.getStartLine(), w.getStartColumn());
					
					if(PlayedTurnValidityChecker.isPlayable(board, word, dictionary))
						wordListOfPlayableWords.add(word);
				}
				for(String s : playableWordsEndingWithPlayedWordsMatched)
				{
					List<LetterInterface> conversion = LetterListToStringTransformer.reverseTransform(s);
					WordInterface word;
					if(w.isHorizontal())
						word = new Word(conversion, true, w.getStartLine(), (short) (w.getEndColumn() - conversion.size()));
					else
						word = new Word(conversion, false, (short) (w.getEndLine() - conversion.size()), w.getStartColumn());
					
					if(PlayedTurnValidityChecker.isPlayable(board, word, dictionary))
						wordListOfPlayableWords.add(word);
				}
				for(String s: horizontalPlayableWordsMatched)
				{
					List<LetterInterface> conversion = LetterListToStringTransformer.reverseTransform(s);
					WordInterface word;
					if(w.isHorizontal())
					{
						word = new Word(conversion, true, w.getStartLine(), (short) (w.getStartColumn()-findSizePrefix(conversion, w)));
						if(PlayedTurnValidityChecker.isPlayable(board, word, dictionary))
							wordListOfPlayableWords.add(word);
					}
					else
					{
						Set<WordInterface> possiblePlacementForTheWord = possiblePlacement(w, s);
						for(WordInterface placement : possiblePlacementForTheWord)
							if(PlayedTurnValidityChecker.isPlayable(board, placement, dictionary))
								wordListOfPlayableWords.add(placement);
					}
				}
				for(String s : verticalPlayableWordsMatched)
				{
					List<LetterInterface> conversion = LetterListToStringTransformer.reverseTransform(s);
					WordInterface word;
					if(w.isHorizontal())
					{
						Set<WordInterface> possiblePlacementForTheWord = possiblePlacement(w, s);
						for(WordInterface placement : possiblePlacementForTheWord)
							if(PlayedTurnValidityChecker.isPlayable(board, placement, dictionary))
								wordListOfPlayableWords.add(placement);
					}
					else
					{
						word = new Word(conversion, true, (short) (w.getStartLine()-findSizePrefix(conversion, w)), w.getStartColumn());
						if(PlayedTurnValidityChecker.isPlayable(board, word, dictionary))
							wordListOfPlayableWords.add(word);
					}	
				}
    		}
    	
    	for(WordInterface wordBasedOnPlayedWords : wordListOfPlayableWords)
   			if(wordsWithLettersAvailable.contains(LetterListToStringTransformer.transform(wordBasedOnPlayedWords.getLetters())))
   				finalList.add(wordBasedOnPlayedWords);
    	
    	/*//Creation of a map containing the playable words and their amount of points
    	Map<WordInterface, Integer> finalListWithPoints = new TreeMap<WordInterface, Integer>();
    	for(WordInterface w : finalList)
    	{
    		List<WordInterface> adjacentWords = PlayedTurnValidityChecker.AdjacentWord(board, w, dictionary);
    		finalListWithPoints.put(w, ScoreManager.getTurnScore(w.getLetters(), adjacentWords, board));
    	}
    	
    	return finalListWithPoints;*/
    	return finalList;
    }

    //Return the size of a word's prefix
    private static short findSizePrefix(List<LetterInterface> letters, WordInterface word)
    {
    	short size = 0;
    	List<LetterInterface> listToCheck = new ArrayList<LetterInterface>();
    	
    	//Go back in the playable word, check if the list contains the played word and increment the size of the list
    	while(!(size == letters.size() && listToCheck.containsAll(word.getLetters())))
    	{
    		listToCheck.add(letters.get(letters.size()-(size+1)));
    		size++;
    	}
    	
    	//Return the size of the playable word's prefix
    	return (short) (letters.size() - size);
    }
    
    /**Return a list of possible placement for a word based on an already placed word (the two words must be in opposite directions)
     * 
     * @param placedWord		An already placed word on the board
     * @param wordToPlace		A word to place on the board
     * @return					A list of all the possible positions for the wordToPlace
     * @throws LetterToStringTransformationException
     */
    private static Set<WordInterface> possiblePlacement(WordInterface placedWord, String wordToPlace) throws LetterToStringTransformationException
    {
    	//List containing all the possible positions to place a word based on an already placed word
    	Set<WordInterface> possibility = new HashSet<WordInterface>();
    	//Conversion of words into their letters
    	List<LetterInterface> placedWordLetters = placedWord.getLetters();
    	List<LetterInterface> wordToPlaceLetters = LetterListToStringTransformer.reverseTransform(wordToPlace);
    	
    	//Comparison of the letters of the two words.
    	//If their letters are equals, the playable word is added to the list of possible placement
    	for(int i = 0; i < placedWordLetters.size(); i++)
    	{
    		for(int j = 0; j < wordToPlaceLetters.size(); j++)
    		{
    			if(placedWordLetters.get(i).equals(wordToPlaceLetters.get(j)))
    			{
    				if(placedWord.isHorizontal())
    					possibility.add(new Word(wordToPlaceLetters, false, (short)(placedWord.getStartLine()-j), (short)(placedWord.getStartColumn()+i)));
    				else
    					possibility.add(new Word(wordToPlaceLetters, true, (short)(placedWord.getStartLine()+i), (short)(placedWord.getStartColumn()-j)));
    			}
    		}
    	}
    	
    	//Return the list of possible placement for a word
    	return possibility;
    }
}