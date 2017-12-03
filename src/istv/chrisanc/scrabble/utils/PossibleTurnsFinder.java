package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.model.BoardPosition;
import istv.chrisanc.scrabble.model.Word;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * This class handles the research of the turns that could be played on the given board.
 *
 * @author Christopher Anciaux
 * @author Anthony Delétré
 * @author Julien Basquin
 */
public class PossibleTurnsFinder {
    /**
     * Searches and returns all the words which can be played on a given board
     *
     * @param board      The board of the game
     * @param player     The player, used to get his letters
     *
     * @return a list of playable turns
    */
    public static List<SortedMap<BoardPosition, LetterInterface>> findPossibleTurns(LanguageInterface language, BoardInterface board, PlayerInterface player) {
        List<String> wordsWithLettersAvailable = new ArrayList<String>();
        Set<WordInterface> wordListOfPlayableWords = new HashSet<>();
        Set<WordInterface> finalList = new HashSet<>();

        if (board.isEmpty()) {
            // If no word has been played, a list of word is created with only letters of the player
            wordsWithLettersAvailable = language.getDictionary().findWordsHavingLetters(player.getLetters());

            for (String word : wordsWithLettersAvailable) {
                List<LetterInterface> conversion = LetterListToStringTransformer.reverseTransform(word, language);
                WordInterface wordToPlay = new Word(player, conversion, true, (short) 7, (short) 7);
                wordListOfPlayableWords.add(wordToPlay);
            }
        } else {
            // If words have been played, a list of all the words from the dictionary that contain at least one letter present in the player's letters is created
            List<LetterInterface> lettersAvailable = new ArrayList<>();

            // Add to the list of words all the words which contain the available letters
            for (int i = 0; i < player.getLetters().size(); i++) {
                lettersAvailable.add(player.getLetters().get(i));
                Set<String> list = new HashSet<>(language.getDictionary().findWordsHavingLettersInOrder(lettersAvailable));
                wordsWithLettersAvailable.addAll(list);
            }

            // Creation of a map with :
            // - words played on the board (key)
            // - a list of words which can be played on a placed word (values)
            Map<WordInterface, Set<String>> playableWords = new HashMap<>();

            // A list of playable words are selected from a dictionary and associated with an already played word
            // These playable words are based on already played words (matching letters)
            for (WordInterface playedWord : board.getPlayedWords()) {
                Set<String> words = new HashSet<>();

                for (LetterInterface letter : playedWord.getLetters()) {
                    language.getDictionary().getWords().stream().filter(s -> !words.contains(s) && s.contains(LetterToStringTransformer.transform(letter))).forEach(words::add);
                }

                // List of words containing the words from the dictionary that contains the played word
                List<String> wordsContainingLetters = language.getDictionary().findWordsHavingLettersInOrder(playedWord.getLetters());

                // Add to a new list words that can only be played with the letters of the player and based on already played words
                Set<String> wordsToUse = new HashSet<>();
                for (String wordToAdd : wordsWithLettersAvailable) {
                    List<LetterInterface> letters = LetterListToStringTransformer.reverseTransform(wordToAdd, language);

                    if (wordsContainingLetters.contains(wordToAdd)) {
                        // If the words are in the same position
                        // Letters from the played word are removed from the word to keep only letters that need to be played
                        playedWord.getLetters().forEach(letters::remove);

                        // Remaining letters are compared with the player's letters to know if the word can be played
                        if (player.getLetters().size() >= letters.size()
                                && occurrencesOfPlayerLettersAreEquivalentToWordLetters(LettersCounter.countOccurrencesOfEachLetter(player.getLetters()), LettersCounter.countOccurrencesOfEachLetter(letters))) {
                            wordsToUse.add(wordToAdd);
                        }
                    } else {
                        // If the words aren't in the same position
                        boolean removed = false;
                        int i = 0;

                        while (!removed && i < playedWord.getLetters().size()) {
                            // One Letter from the played word is removed from the word to keep only letters that need to be played
                            if (letters.contains(playedWord.getLetters().get(i))) {
                                letters.remove(playedWord.getLetters().get(i));
                            }

                            // Remaining letters are compared with the player's letters to know if the word can be played
                            if (player.getLetters().size() >= letters.size()
                                    && occurrencesOfPlayerLettersAreEquivalentToWordLetters(LettersCounter.countOccurrencesOfEachLetter(player.getLetters()), LettersCounter.countOccurrencesOfEachLetter(letters))) {
                                wordsToUse.add(wordToAdd);
                                removed = true;
                            }

                            if (!removed) {
                                // If the word can't be played, the removed letter is put back and an other one is removed
                                letters = LetterListToStringTransformer.reverseTransform(wordToAdd, language);
                                i++;
                            }
                        }
                    }
                }

                // Conversion of the playable words's list into a Map
                // Playable words are linked with a played word
                playableWords.put(playedWord, wordsToUse);

                // Conversion of playable words into a list of their letters
                // For each playable word (String) is associated their letters (List<LetterInterface>)
                Set<String> listOfPlayableWords = playableWords.get(playedWord);
                Map<String, List<LetterInterface>> listOfPlayableWordsWithLetters = new HashMap<>();
                for (String word : listOfPlayableWords) {
                    listOfPlayableWordsWithLetters.put(word, LetterListToStringTransformer.reverseTransform(word, language));
                }

                //Creation of 2 lists to stock words
                List<String> horizontalPlayableWords = new ArrayList<>();
                List<String> verticalPlayableWords = new ArrayList<>();
                Set<Map.Entry<String, List<LetterInterface>>> listOfPlayableWordsToCheck = listOfPlayableWordsWithLetters.entrySet();

                for (Map.Entry<String, List<LetterInterface>> word : listOfPlayableWordsToCheck) {
                    if (playedWord.isHorizontal()) {
                        // If the played word is horizontal
                        if (wordsContainingLetters.contains(word.getKey())) {
                            // If the possible word is horizontal (letters added before and/or after the played word) and bigger than the played word
                            horizontalPlayableWords.add(word.getKey());
                        } else {
                            verticalPlayableWords.add(word.getKey());
                        }
                    } else {
                        // If the played word is vertical
                        if (wordsContainingLetters.contains(word.getKey())) {
                            // If the possible word is vertical (letters added before and/or after the played word) and bigger than the played word
                            verticalPlayableWords.add(word.getKey());
                        } else {
                            horizontalPlayableWords.add(word.getKey());
                        }
                    }
                }

                // Conversion of a word from String to Word depending of its position and the position of the played word
                for (String word : horizontalPlayableWords) {
                    List<LetterInterface> conversion = LetterListToStringTransformer.reverseTransform(word, language);
                    WordInterface wordToPlay;

                    if (playedWord.isHorizontal()) {
                        wordToPlay = new Word(player, conversion, true, playedWord.getStartLine(), (short) (playedWord.getStartColumn() - findSizePrefix(conversion, playedWord)));
                        // Check if the word can be played on the board
                        if (PlayedTurnValidityChecker.turnIsPossible(wordToPlay, language.getDictionary(), board, player)) {
                            wordListOfPlayableWords.add(wordToPlay);
                        }
                    } else {
                        // Add all the possible placement for the word on the board
                        wordListOfPlayableWords.addAll(possiblePlacement(board, playedWord, word, player, language));
                    }
                }

                for (String word : verticalPlayableWords) {
                    List<LetterInterface> conversion = LetterListToStringTransformer.reverseTransform(word, language);
                    WordInterface wordToPlay;
                    //Add all the possible placement for the word on the board
                    if (playedWord.isHorizontal()) {
                        wordListOfPlayableWords.addAll(possiblePlacement(board, playedWord, word, player, language));
                    }
                    //Check if the word can be played on the board
                    else {
                        wordToPlay = new Word(player, conversion, true, (short) (playedWord.getStartLine() - findSizePrefix(conversion, playedWord)), playedWord.getStartColumn());
                        if (PlayedTurnValidityChecker.turnIsPossible(wordToPlay, language.getDictionary(), board, player)) {
                            wordListOfPlayableWords.add(wordToPlay);
                        }
                    }
                }
            }
        }

        List<String> finalWordsWithLettersAvailable = wordsWithLettersAvailable;
        finalList.addAll(wordListOfPlayableWords.stream().filter(wordBasedOnPlayedWords -> finalWordsWithLettersAvailable.contains(LetterListToStringTransformer.transform(wordBasedOnPlayedWords.getLetters()))).collect(Collectors.toList()));

        // TODO: optimize all !
        List<SortedMap<BoardPosition, LetterInterface>> finalReturn = finalList.stream().map(wordToBeTransformed -> transformWordIntoPlayedTurn(wordToBeTransformed, board)).collect(Collectors.toList());
        return finalReturn;
    }

    /**
     * Return the size of a word's prefix
     *
     * @param letters Letters of a word, can be played on an already played word
     * @param word    A played word
     *
     * @return The size of the prefix
     */
    protected static short findSizePrefix(List<LetterInterface> letters, WordInterface word) {
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
    private static Set<WordInterface> possiblePlacement(BoardInterface board, WordInterface placedWord, String wordToPlace, PlayerInterface player, LanguageInterface language) {
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
                        if (PlayedTurnValidityChecker.turnIsPossible(word, language.getDictionary(), board, player)) {
                            possibility.add(word);
                        }
                    } else {
                        WordInterface word = new Word(player, wordToPlaceLetters, true, (short) (placedWord.getStartLine() + i), (short) (placedWord.getStartColumn() - j));
                        if (PlayedTurnValidityChecker.turnIsPossible(word, language.getDictionary(), board, player)) {
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
            if (!occurrencesOfPlayerLetters.containsKey(m.getKey()) || m.getValue() > occurrencesOfPlayerLetters.get(m.getKey())) {
                return false;
            }
        }

        return true;
    }

    protected static SortedMap<BoardPosition, LetterInterface> transformWordIntoPlayedTurn(WordInterface word, BoardInterface board) {
        SortedMap<BoardPosition, LetterInterface> playedLetters = new TreeMap<>();

        List<LetterInterface> letters = word.getLetters();
        for (int i = 0, lettersSize = letters.size(); i < lettersSize; i++) {
            LetterInterface letter = letters.get(i);

            if (word.isHorizontal() && board.getLetters().get(word.getStartLine()).get(word.getStartColumn() + i) == null) {
                playedLetters.put(new BoardPosition(word.getStartLine(), (short) (word.getStartColumn() + i)), letter);
            } else if (board.getLetters().get(word.getStartLine() + i).get(word.getStartColumn()) == null) {
                playedLetters.put(new BoardPosition((short) (word.getStartLine() + i), word.getStartColumn()), letter);
            }
        }

        return  playedLetters;
    }
}