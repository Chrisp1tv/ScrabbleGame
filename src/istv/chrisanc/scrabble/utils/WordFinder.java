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
import java.util.stream.Collectors;

/**
 * This class handles the research of the words that could be played on the given {@link BoardInterface}.
 *
 * TODO: Change all one-letter variable names to clearer names
 * TODO: Remove all unnecessary comments, clean up the code
 * TODO: Add comments explaining what is done, and why it's done
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
    public static Map<WordInterface, Integer> findWord(BoardInterface board, PlayerInterface player, DictionaryInterface dictionary, LanguageInterface language) {
        //Creation of 2 Lists :
        //- a list of the player's letters
        //- a list of words formed with the first list
        LetterInterface lettersAvailable;
        Set<String> wordsWithLettersAvailable = new HashSet<>();

        //TODO Ajouter et tester cas avec Joker
        /*if(player.getLetters().contains(Joker.class))
            wordsWithLettersAvailable = dictionary.getWords();
    	else
    	{*/
        //Add to the list of words all the words which contains the available letters
        for (int i = 0; i < player.getLetters().size(); i++) {
            lettersAvailable = player.getLetters().get(i);
            dictionary.getWords().stream().filter(s -> !wordsWithLettersAvailable.contains(s) && s.contains(LetterToStringTransformer.transform(lettersAvailable))).forEach(wordsWithLettersAvailable::add);
        }
        /*}*/

    	/* TODO: The code between the lines 46 and 56 could be replaced by this call ?
            wordsWithLettersAvailable = new HashSet<>(dictionary.findWordsHavingLetters(player.getLetters()));
    	 */

        //Creation of a map with :
        //- words played on the board (key)
        //- a list of words which can be played on a placed word (values)
        Map<WordInterface, Set<String>> playableWords = new HashMap<>();

        Set<WordInterface> wordListOfPlayableWords = new HashSet<>();
        Set<WordInterface> finalList = new HashSet<>();

        //A list of playable words are selected from a dictionary and associated with an already played word
        //These playable words are based on already played words (matching letters)
        for (WordInterface playedWord : board.getPlayedWords()) {
            //List of String containing all the playable words for an already played word
            //Playable words are stocked if they share at least one letter with the played word
            Set<String> words = new HashSet<>();

            // TODO: This part of the code should use the methods DictionaryInterface#findWordsStartingWithAndHavingLetters
            // and DictionaryInterface#findWordsEndingWithAndHavingLetters, or findWordsHavingLettersInOrder
            for (LetterInterface l : playedWord.getLetters()) {
                dictionary.getWords().stream().filter(s -> !words.contains(s) && s.contains(LetterToStringTransformer.transform(l))).forEach(words::add);
            }

            Set<String> wordsToAdd = words.stream().filter(wordsWithLettersAvailable::contains).collect(Collectors.toSet());

            //Add to a new list words that can only be played with the letters of the player and based on already played words
            Set<String> wordsToUse = new HashSet<>();
            for (String wordToAdd : wordsToAdd) {
                List<LetterInterface> letters = LetterListToStringTransformer.reverseTransform(wordToAdd, language);
                String lettersString = LetterListToStringTransformer.transform(playedWord.getLetters());

                //If the words are in the same position (if the played word is included in the word s)
                if (wordToAdd.contains(lettersString)) {
                    //Letters from the played word are removed from the word s to keep only letters that need to be played
                    for (int i = 0; i < playedWord.getLetters().size(); i++) {
                        letters.remove(playedWord.getLetters().get(i));
                    }

                    //Remaining letters are compared with the player's letters to know if the word can be played
                    if (player.getLetters().size() >= letters.size()) {
                        int i = 0;
                        boolean contains = true;
                        while (contains && i < letters.size()) {
                            if (!player.getLetters().contains(letters.get(i))) {
                                contains = false;
                            }
                            i++;
                        }

                        if (contains) {
                            /*
                            TODO: Use the occurrencesOfPlayerLettersAreEquivalentToWordLetters method to avoid duplicate code
                             */
                            Map<LetterInterface, Integer> lettersOfPlayerOccurence = countOccurrencesOfEachLetter(player.getLetters());
                            Map<LetterInterface, Integer> lettersOfWordOccurence = countOccurrencesOfEachLetter(letters);
                            for (Map.Entry<LetterInterface, Integer> m : lettersOfPlayerOccurence.entrySet()) {
                                if (letters.contains(m.getKey())) {
                                    if (m.getValue() < lettersOfWordOccurence.get(m.getKey())) {
                                        contains = false;
                                    }
                                }
                            }
                        }
                        if (contains) {
                            wordsToUse.add(wordToAdd);
                        }
                    }
                }
                //If the words are not in the same position (if the played word is not included in the word s but at least one letter is present in the two words)
                else {
                    boolean removed = false;
                    int i = 0;
                    while (!removed && i < playedWord.getLetters().size()) {
                        //One Letter from the played word is removed from the word s to keep only letters that need to be played
                        if (letters.contains(playedWord.getLetters().get(i))) {
                            letters.remove(playedWord.getLetters().get(i));
                        }

                        //Remaining letters are compared with the player's letters to know if the word can be played
                        if (player.getLetters().size() >= letters.size()) {
                            int j = 0;
                            boolean contains = true;
                            while (contains && j < letters.size()) {
                                if (!player.getLetters().contains(letters.get(j))) {
                                    contains = false;
                                }
                                j++;
                            }

                            if (contains) {
                                /*
                                TODO: Use the occurrencesOfPlayerLettersAreEquivalentToWordLetters method to avoid duplicate code
                                 */
                                Map<LetterInterface, Integer> lettersOfPlayerOccurence = countOccurrencesOfEachLetter(player.getLetters());
                                Map<LetterInterface, Integer> lettersOfWordOccurence = countOccurrencesOfEachLetter(letters);
                                for (Map.Entry<LetterInterface, Integer> m : lettersOfPlayerOccurence.entrySet()) {
                                    if (letters.contains(m.getKey())) {
                                        if (m.getValue() < lettersOfWordOccurence.get(m.getKey())) {
                                            contains = false;
                                        }
                                    }
                                }
                            }

                            if (contains) {
                                wordsToUse.add(wordToAdd);
                                removed = true;
                            }
                        }

                        if (!removed) {
                            letters = LetterListToStringTransformer.reverseTransform(wordToAdd, language);
                            i++;
                        }
                    }
                }
            }

            //Conversion of the playable words's list into a String HashSet
            //Playable words are linked with a played word
            playableWords.put(playedWord, wordsToUse);

            //System.out.println(playableWords.size());

            //Conversion of playable words into a list of their letters
            //For each playable word (String) is associated their letters (List<LetterInterface>)
            Set<String> listOfPlayableWords = playableWords.get(playedWord);
            Map<String, List<LetterInterface>> listOfPlayableWordsWithLetters = new HashMap<>();
            //TODO Long, � v�rifier
            for (String p : listOfPlayableWords) {
                listOfPlayableWordsWithLetters.put(p, LetterListToStringTransformer.reverseTransform(p, language));
            }

            //Creation of a list to stock words from the dictionary and containing the played word (w)
            List<String> listOfWordsContainingTheWord = dictionary.findWordsHavingLettersInOrder(playedWord.getLetters());

            //Creation of 4 lists to stock words
            List<String> horizontalPlayableWordsMatched = new ArrayList<>();
            List<String> verticalPlayableWordsMatched = new ArrayList<>();
            /*Set<String> playableWordsStartingWithPlayedWordsMatched = new HashSet<String>();
            Set<String> playableWordsEndingWithPlayedWordsMatched = new HashSet<String>();*/

            Set<Entry<String, List<LetterInterface>>> listOfPlayableWordsToCheck = listOfPlayableWordsWithLetters.entrySet();
            //int cpt = 0;
            //cpt++;
            //System.out.println(cpt);
            //If the played word is horizontal
            //If the possible word is horizontal (letters added before and/or after the played word) and bigger than the played word
            listOfPlayableWordsToCheck.stream().filter(p -> wordsWithLettersAvailable.contains(p.getKey())).forEach(p -> {
                //If the played word is horizontal
                if (playedWord.isHorizontal()) {
                    //If the possible word is horizontal (letters added before and/or after the played word) and bigger than the played word
                    if (listOfWordsContainingTheWord.contains(p.getKey())) {
                        horizontalPlayableWordsMatched.add(p.getKey());
                    } else {
                        verticalPlayableWordsMatched.add(p.getKey());
                    }
                } else {
                    if (listOfWordsContainingTheWord.contains(p.getKey())) {
                        verticalPlayableWordsMatched.add(p.getKey());
                    } else {
                        horizontalPlayableWordsMatched.add(p.getKey());
                    }
                }
            });
            //List<LetterInterface> listOfLettersAvailable = player.getLetters();
            //Finding all the words in the dictionary starting with the played word
            //(found words cannot be smaller than the played word based on or longer than the board's size)
            //TODO OPTIMIZE + MODIFIER : Les mots ne semblent pas se r�cup�rer
            //playableWordsStartingWithPlayedWordsMatched.addAll(dictionary.findWordsStartingWithAndHavingLetters(w.getLetters().size(), BoardInterface.BOARD_SIZE, w.getLetters(), listOfLettersAvailable));
            //Finding all the words in the dictionary ending with the played word
            //(found words cannot be smaller than the played word based on or longer than the board's size)
            //TODO OPTIMIZE + MODIFIER : Les mots ne semblent pas se r�cup�rer
            //playableWordsEndingWithPlayedWordsMatched.addAll(dictionary.findWordsEndingWithAndHavingLetters(w.getLetters().size(), BoardInterface.BOARD_SIZE, w.getLetters(), listOfLettersAvailable));

            //Deleting the two lists above from the playable words's list of the position (horizontal or vertical).
            //The remaining list contains playable words of the same position which neither. begin or end by the played word
                /*if(w.isHorizontal())
                {
					horizontalPlayableWordsMatched.removeAll(playableWordsStartingWithPlayedWordsMatched);
					horizontalPlayableWordsMatched.removeAll(playableWordsEndingWithPlayedWordsMatched);
				}
				else
				{
					verticalPlayableWordsMatched.removeAll(playableWordsStartingWithPlayedWordsMatched);
					verticalPlayableWordsMatched.removeAll(playableWordsEndingWithPlayedWordsMatched);
				}*/
				
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
            //System.out.println(wordListOfPlayableWords.size());
            //Conversion of playable words from String to Letters and then to Word
				/*for(String s : playableWordsStartingWithPlayedWordsMatched)
				{
					List<LetterInterface> conversion = LetterListToStringTransformer.reverseTransform(s, language);
					WordInterface word;
					if(w.isHorizontal())
						word = new Word(player, conversion, true, w.getStartLine(), w.getStartColumn());
					else
						word = new Word(player, conversion, false, w.getStartLine(), w.getStartColumn());
					
					if(PlayedTurnValidityChecker_Alternatif.isPlayable(board, word, dictionary))
						wordListOfPlayableWords.add(word);
				}
				for(String s : playableWordsEndingWithPlayedWordsMatched)
				{
					List<LetterInterface> conversion = LetterListToStringTransformer.reverseTransform(s, language);
					WordInterface word;
					if(w.isHorizontal())
						word = new Word(player, conversion, true, w.getStartLine(), (short) (w.getEndColumn() - conversion.size()));
					else
						word = new Word(player, conversion, false, (short) (w.getEndLine() - conversion.size()), w.getStartColumn());
					
					if(PlayedTurnValidityChecker_Alternatif.isPlayable(board, word, dictionary))
						wordListOfPlayableWords.add(word);
				}*/
            for (String s : horizontalPlayableWordsMatched) {
                List<LetterInterface> conversion = LetterListToStringTransformer.reverseTransform(s, language);
                WordInterface word;
                if (playedWord.isHorizontal()) {
                    word = new Word(player, conversion, true, playedWord.getStartLine(), (short) (playedWord.getStartColumn() - findSizePrefix(conversion, playedWord)));
                    if (PlayedTurnValidityChecker_Alternatif.isPlayable(board, word, dictionary)) {
                        wordListOfPlayableWords.add(word);
                    }
                } else {
                    wordListOfPlayableWords.addAll(possiblePlacement(board, dictionary, playedWord, s, player, language));
                }
            }
            for (String s : verticalPlayableWordsMatched) {
                //System.out.println(wordListOfPlayableWords.size());
                List<LetterInterface> conversion = LetterListToStringTransformer.reverseTransform(s, language);
                WordInterface word;
                if (playedWord.isHorizontal()) {
                    wordListOfPlayableWords.addAll(possiblePlacement(board, dictionary, playedWord, s, player, language));
                } else {
                    word = new Word(player, conversion, true, (short) (playedWord.getStartLine() - findSizePrefix(conversion, playedWord)), playedWord.getStartColumn());
                    if (PlayedTurnValidityChecker_Alternatif.isPlayable(board, word, dictionary)) {
                        wordListOfPlayableWords.add(word);
                    }
                }
            }
            //System.out.println(horizontalPlayableWordsMatched.size());
            //System.out.println(verticalPlayableWordsMatched.size());
            //System.out.println(wordListOfPlayableWords.size());
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
        for (WordInterface w : finalList) {
            List<WordInterface> adjacentWords = PlayedTurnValidityChecker_Alternatif.AdjacentWord(board, w, dictionary, player);
            adjacentWords.add(w);
            finalListWithPoints.put(w, ScoreManager.getTurnScore(w.getLetters(), adjacentWords, board));
        }

        return finalListWithPoints;
    }

    //TODO La taille du préfixe est toujours égale à 0
    //Return the size of a word's prefix
    private static short findSizePrefix(List<LetterInterface> letters, WordInterface word) {
        short size = 0;
        List<LetterInterface> listToCheck = new ArrayList<>();

        //Go back in the playable word, check if the list contains the played word and increment the size of the list
        while (!(size == letters.size() || listToCheck.containsAll(word.getLetters()))) {
            listToCheck.add(letters.get(letters.size() - (size + 1)));
            size++;
        }

        //Return the size of the playable word's prefix
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

    protected boolean occurrencesOfPlayerLettersAreEquivalentToWordLetters(Map<LetterInterface, Integer> occurrencesOfPlayerLetters, Map<LetterInterface, Integer> occurrencesOfWordLetters) {
        for (Map.Entry<LetterInterface, Integer> m : occurrencesOfPlayerLetters.entrySet()) {
            if (occurrencesOfWordLetters.containsKey(m.getKey()) && m.getValue() < occurrencesOfWordLetters.get(m.getKey())) {
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
}