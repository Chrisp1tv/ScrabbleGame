package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.model.Word;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * This class handles the research of the words that could be played on the given {@link BoardInterface}.
 *
 *
 * @author Christopher Anciaux
 * @author Anthony Delétré
 * @author Julien Basquin
 */
public class WordFinder {
    /**
     * Searches and returns all the words which can be played on a given board
     *
     * @param board      The board of the game
     * @param player     The player, used to get his letters
     * @param dictionary A dictionary containing all the words
     *
     * @return A list of playable words sorted by their score
     */
    public static Map<WordInterface, Integer> findWord(BoardInterface board, PlayerInterface player, DictionaryInterface dictionary, LanguageInterface language) 
    {
    	List<String> wordsWithLettersAvailable = new ArrayList<String>();
        Set<WordInterface> wordListOfPlayableWords = new HashSet<>();
        Set<WordInterface> finalList = new HashSet<>();
    	
    	//If no word has been played, a list of word is created with only letters of the player
    	if(board.getPlayedWords().isEmpty())
        {
        	wordsWithLettersAvailable = dictionary.findWordsHavingLetters(player.getLetters());
        	for(String word : wordsWithLettersAvailable)
        	{
        		List<LetterInterface> conversion = LetterListToStringTransformer.reverseTransform(word, language);
            	WordInterface wordToPlay = new Word(player, conversion, true, (short) 7, (short) 7);
        			wordListOfPlayableWords.add(wordToPlay);
        	}
        }
        //If words have been played, a list of all the words from the dictionary that contains at least one letter present in the player's letters is created
    	else
    	{
    		List<LetterInterface> lettersAvailable = new ArrayList<LetterInterface>();
    		
    		//Add to the list of words all the words which contains the available letters
    		for(int i = 0; i < player.getLetters().size(); i++)
    		{
    			lettersAvailable.add(player.getLetters().get(i));
    			List<String> temporaryList = dictionary.findWordsHavingLettersInOrder(lettersAvailable);
    			Set<String> list = new HashSet<String>(temporaryList);
    			wordsWithLettersAvailable.addAll(list);
    		}
    	
        //Creation of a map with :
        //- words played on the board (key)
        //- a list of words which can be played on a placed word (values)
        Map<WordInterface, Set<String>> playableWords = new HashMap<>();

        //A list of playable words are selected from a dictionary and associated with an already played word
        //These playable words are based on already played words (matching letters)
        for (WordInterface playedWord : board.getPlayedWords()) {
            Set<String> words = new HashSet<>();

            for (LetterInterface letter : playedWord.getLetters()) {
                dictionary.getWords().stream().filter(s -> !words.contains(s) && s.contains(LetterToStringTransformer.transform(letter))).forEach(words::add);
            }
            
            //List of words containing the words from the dictionary that contains the played word
            List<String> wordsContainingLetters = dictionary.findWordsHavingLettersInOrder(playedWord.getLetters());
            
            //Add to a new list words that can only be played with the letters of the player and based on already played words
            Set<String> wordsToUse = new HashSet<>();
            for (String wordToAdd : wordsWithLettersAvailable) 
            {
            	List<LetterInterface> letters = LetterListToStringTransformer.reverseTransform(wordToAdd, language);

            	//If the words are in the same position
            	if (wordsContainingLetters.contains(wordToAdd)) 
            	{
            		//Letters from the played word are removed from the word to keep only letters that need to be played
            		letters = removeAllLetters(letters, playedWord.getLetters());

            		//Remaining letters are compared with the player's letters to know if the word can be played
            		if (player.getLetters().size() >= letters.size()) 
            		{
            			Map<LetterInterface, Integer> lettersOfPlayerOccurence = countOccurrencesOfEachLetter(player.getLetters());
            			Map<LetterInterface, Integer> lettersOfWordOccurence = countOccurrencesOfEachLetter(letters);
            			if(occurrencesOfPlayerLettersAreEquivalentToWordLetters(lettersOfPlayerOccurence, lettersOfWordOccurence))
            				wordsToUse.add(wordToAdd);
            		}
            	}
                //If the words are not in the same position
                else {
                    boolean removed = false;
                    int i = 0;
                    while (!removed && i < playedWord.getLetters().size()) {
                        //One Letter from the played word is removed from the word to keep only letters that need to be played
                        if (letters.contains(playedWord.getLetters().get(i))) {
                            letters.remove(playedWord.getLetters().get(i));
                        }

                        //Remaining letters are compared with the player's letters to know if the word can be played
                        if (player.getLetters().size() >= letters.size()) 
                        {
                        	Map<LetterInterface, Integer> lettersOfPlayerOccurence = countOccurrencesOfEachLetter(player.getLetters());
                			Map<LetterInterface, Integer> lettersOfWordOccurence = countOccurrencesOfEachLetter(letters);
                			if(occurrencesOfPlayerLettersAreEquivalentToWordLetters(lettersOfPlayerOccurence, lettersOfWordOccurence))
                			{
                				wordsToUse.add(wordToAdd);
                				removed = true;
                			}
                        }

                        //If the word can't be played, the removed letter is put back and an other one is removed
                        if (!removed) {
                            letters = LetterListToStringTransformer.reverseTransform(wordToAdd, language);
                            i++;
                        }
                    }
                }
            }

            //Conversion of the playable words's list into a Map
            //Playable words are linked with a played word
            playableWords.put(playedWord, wordsToUse);


            //Conversion of playable words into a list of their letters
            //For each playable word (String) is associated their letters (List<LetterInterface>)
            Set<String> listOfPlayableWords = playableWords.get(playedWord);
            Map<String, List<LetterInterface>> listOfPlayableWordsWithLetters = new HashMap<>();
            for (String word : listOfPlayableWords) {
                listOfPlayableWordsWithLetters.put(word, LetterListToStringTransformer.reverseTransform(word, language));
            }

            //Creation of 2 lists to stock words
            List<String> horizontalPlayableWords = new ArrayList<>();
            List<String> verticalPlayableWords = new ArrayList<>();

            Set<Entry<String, List<LetterInterface>>> listOfPlayableWordsToCheck = listOfPlayableWordsWithLetters.entrySet();
            for(Entry<String, List<LetterInterface>> word : listOfPlayableWordsToCheck)
            {
            	//If the played word is horizontal
            	if (playedWord.isHorizontal()) {
            		//If the possible word is horizontal (letters added before and/or after the played word) and bigger than the played word
            		if (wordsContainingLetters.contains(word.getKey())) {
            			horizontalPlayableWords.add(word.getKey());
            		} else {
            			verticalPlayableWords.add(word.getKey());
            		}
            	} 
            	//If the played word is vertical
            	else {
            		//If the possible word is vertical (letters added before and/or after the played word) and bigger than the played word
            		if (wordsContainingLetters.contains(word.getKey())) {
            			verticalPlayableWords.add(word.getKey());
            		} else {
            			horizontalPlayableWords.add(word.getKey());
            		}
            	}
            }
            
            //Conversion of a word from String to Word depending of its position and the position of the played word
            for (String word : horizontalPlayableWords) {
            	List<LetterInterface> conversion = LetterListToStringTransformer.reverseTransform(word, language);
            	WordInterface wordToPlay;
            	if (playedWord.isHorizontal()) {
            		wordToPlay = new Word(player, conversion, true, playedWord.getStartLine(), (short) (playedWord.getStartColumn() - findSizePrefix(conversion, playedWord)));
            		//Check if the word can be played on the board
            		if (PlayedTurnValidityChecker_Alternatif.isPlayable(board, wordToPlay, dictionary)) {
            			wordListOfPlayableWords.add(wordToPlay);
            		}
            	} else {
            		//Add all the possible placement for the word on the board
            		wordListOfPlayableWords.addAll(possiblePlacement(board, dictionary, playedWord, word, player, language));
            	}
            }
            for (String word : verticalPlayableWords) {
            	List<LetterInterface> conversion = LetterListToStringTransformer.reverseTransform(word, language);
            	WordInterface wordToPlay;
            	//Add all the possible placement for the word on the board
            	if (playedWord.isHorizontal()) {
            		wordListOfPlayableWords.addAll(possiblePlacement(board, dictionary, playedWord, word, player, language));
            	} 
            	//Check if the word can be played on the board
            	else {
            		wordToPlay = new Word(player, conversion, true, (short) (playedWord.getStartLine() - findSizePrefix(conversion, playedWord)), playedWord.getStartColumn());
            		if (PlayedTurnValidityChecker_Alternatif.isPlayable(board, wordToPlay, dictionary)) {
            			wordListOfPlayableWords.add(wordToPlay);
            		}
            	}
            }
        }
    	}

        for (WordInterface wordBasedOnPlayedWords : wordListOfPlayableWords) {
            if (wordsWithLettersAvailable.contains(LetterListToStringTransformer.transform(wordBasedOnPlayedWords.getLetters()))) {
                finalList.add(wordBasedOnPlayedWords);
            }
        }

        //Creation of a map containing the playable words and their amount of points
        //Added a comparator to sort the words by score
        Map<WordInterface, Integer> finalListWithPoints = new TreeMap<>(new Comparator<WordInterface>() {

            @Override
            public int compare(WordInterface word1, WordInterface word2) {
                List<WordInterface> words1 = PlayedTurnValidityChecker_Alternatif.AdjacentWord(board, word1, dictionary, player);
                words1.add(word1);
                List<WordInterface> words2 = PlayedTurnValidityChecker_Alternatif.AdjacentWord(board, word2, dictionary, player);
                words2.add(word2);
                Integer score1 = ScoreManager.getTurnScore(word1.getLetters(), words1, board, false);
                Integer score2 = ScoreManager.getTurnScore(word1.getLetters(), words2, board, false);

                if (score1 <= score2) {
                    return -1;
                } else {
                    return 1;
                }
            }
        });

        //Add score to words with a map
        for (WordInterface word : finalList) {
            List<WordInterface> adjacentWords = PlayedTurnValidityChecker_Alternatif.AdjacentWord(board, word, dictionary, player);
            adjacentWords.add(word);
            finalListWithPoints.put(word, ScoreManager.getTurnScore(word.getLetters(), adjacentWords, board));
        }

        return finalListWithPoints;
    }

    /**
     * Return the size of a word's prefix
     * 
     * @param letters	Letters of a word, can be played on an already played word
     * @param word		A played word
     * @return			The size of the prefix
     */
    private static short findSizePrefix(List<LetterInterface> letters, WordInterface word) {
        short size = 0;
        List<LetterInterface> listToCheck = new ArrayList<>();

        //Go back in the playable word, check if the list contains the played word and increment the size of the list
        while (!(size == letters.size() || listToCheck.containsAll(word.getLetters()))) {
            listToCheck.add(letters.get(letters.size() - (size + 1)));
            size++;
        }

        return (short) (letters.size() - size);
    }

    /**
     * Return a list of possible placement for a word based on an already placed word (the two words must be in opposite directions)
     *
     * @param placedWord  An already placed word on the board
     * @param wordToPlace A word to place on the board
     *
     * @return A list of all the possible positions for the wordToPlace
     */
    private static Set<WordInterface> possiblePlacement(BoardInterface board, DictionaryInterface dictionary, WordInterface placedWord, String wordToPlace, PlayerInterface player, LanguageInterface language) {
        //List containing all the possible positions to place a word based on an already placed word
        Set<WordInterface> possibility = new HashSet<>();
        //Conversion of words into their letters
        List<LetterInterface> placedWordLetters = placedWord.getLetters();
        List<LetterInterface> wordToPlaceLetters = LetterListToStringTransformer.reverseTransform(wordToPlace, language);

        //Comparison of the letters of the two words.
        //If their letters are equals, the playable word is added to the list of possible placement
        for (int i = 0; i < placedWordLetters.size(); i++) {
            for (int j = 0; j < wordToPlaceLetters.size(); j++) {
                if (placedWordLetters.get(i).equals(wordToPlaceLetters.get(j))) {
                    if (placedWord.isHorizontal()) {
                        WordInterface word = new Word(player, wordToPlaceLetters, false, (short) (placedWord.getStartLine() - j), (short) (placedWord.getStartColumn() + i));
                        if (PlayedTurnValidityChecker_Alternatif.isPlayable(board, word, dictionary)) {
                            possibility.add(word);
                        }
                    } else {
                        WordInterface word = new Word(player, wordToPlaceLetters, true, (short) (placedWord.getStartLine() + i), (short) (placedWord.getStartColumn() - j));
                        if (PlayedTurnValidityChecker_Alternatif.isPlayable(board, word, dictionary)) {
                            possibility.add(word);
                        }
                    }
                }
            }
        }

        //Return the list of possible placement for a word
        return possibility;
    }

    protected static boolean occurrencesOfPlayerLettersAreEquivalentToWordLetters(Map<LetterInterface, Integer> occurrencesOfPlayerLetters, Map<LetterInterface, Integer> occurrencesOfWordLetters) {
    	for (Map.Entry<LetterInterface, Integer> m : occurrencesOfWordLetters.entrySet()) {
    		if(!occurrencesOfPlayerLetters.containsKey(m.getKey()) || m.getValue() > occurrencesOfPlayerLetters.get(m.getKey()))
    		{
    			return false;
    		}
    	}

        return true;
    }

    /**
     * @param letters The given letters we have to count
     *
     * @return a map having for key the {@link LetterInterface}, and for value the number of occurrences of the {@link LetterInterface}
     * TODO: Move this method (and the other) to an Util class
     */
    private static Map<LetterInterface, Integer> countOccurrencesOfEachLetter(List<LetterInterface> letters) {
        Set<LetterInterface> uniqueLetters = new HashSet<>(letters);
        Map<LetterInterface, Integer> nbLetters = new HashMap<>();

        for (LetterInterface uniqueLetter : uniqueLetters) {
            nbLetters.put(uniqueLetter, Collections.frequency(letters, uniqueLetter));
        }

        return nbLetters;
    }
    
    /**
     * Remove a list of letters from a word
     * 
     * @param word				Word which letters will be removed
     * @param lettersToRemove	Letters to remove
     * @return
     */
    private static List<LetterInterface> removeAllLetters(List<LetterInterface> word, List<LetterInterface> lettersToRemove)
    {
    	List<LetterInterface> newWord = word;
    	
    	for (int i = 0; i < lettersToRemove.size(); i++) 
    	{
    		newWord.remove(lettersToRemove.get(i));
    	}
    	
    	return newWord;
    }
}