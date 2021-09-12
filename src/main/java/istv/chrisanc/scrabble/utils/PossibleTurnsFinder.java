package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.model.BoardPosition;
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
 * This class handles the searching of the turns that could be played on the given board.
 *
 * @author Christopher Anciaux
 * @author Anthony Delétré
 * @author Julien Basquin
 */
public class PossibleTurnsFinder {
    /**
     * Searches and returns all the words which can be played on a given board
     *
     * @param language The language of the game
     * @param board    The board of the game
     * @param player   The player, used to get his letters
     *
     * @return a list of playable turns
     */
    public static List<SortedMap<BoardPosition, LetterInterface>> findPossibleTurns(LanguageInterface language, BoardInterface board, PlayerInterface player) {
        List<SortedMap<BoardPosition, LetterInterface>> possibleTurns = new ArrayList<>();

        if (board.isEmpty()) {
            // If no word has been played, a list of word is created with only letters of the player
            possibleTurns.addAll(language.getDictionary().findWordsHavingLetters(player.getLetters()).stream().map(word -> PossibleTurnsFinder.transformStringIntoTurn(word, BoardInterface.BOARD_SIZE / 2, BoardInterface.BOARD_SIZE / 2, true, language, false, board)).collect(Collectors.toList()));

            return possibleTurns;
        }

        List<String> wordsFeasibleWithPlayerLetters = language.getDictionary().findWordsHavingLetters(player.getLetters());

        // Creation of a map with :
        // - words played on the board (key)
        // - a list of words which can be played on a placed word (values)
        Map<WordInterface, Set<String>> playableWords = new HashMap<>();

        // A list of playable words are selected from a dictionary and associated with an already played word
        // These playable words are based on already played words (matching letters)
        for (WordInterface alreadyPlayedWord : board.getPlayedWords()) {
            // List of words containing the words from the dictionary that contains the played word
            List<String> wordsFeasibleFromAlreadyPlayedWords = language.getDictionary().findWordsHavingLettersInOrder(alreadyPlayedWord.getLetters());

            // Add to a new list words that can only be played with the letters of the player and based on already played words
            Set<String> wordsToUse = new HashSet<>();
            for (String feasibleWordWithPlayerLetters : wordsFeasibleWithPlayerLetters) {
                List<LetterInterface> lettersOfFeasibleWordWithPlayerLetters = LetterListToStringTransformer.reverseTransform(feasibleWordWithPlayerLetters, language);

                if (wordsFeasibleFromAlreadyPlayedWords.contains(feasibleWordWithPlayerLetters)) {
                    // If the words are in the same position
                    // Letters from the played word are removed from the word to keep only letters that need to be played
                    alreadyPlayedWord.getLetters().forEach(lettersOfFeasibleWordWithPlayerLetters::remove);

                    // Remaining letters are compared with the player's letters to know if the word can be played
                    if (player.getLetters().size() >= lettersOfFeasibleWordWithPlayerLetters.size()
                            && PossibleTurnsFinder.occurrencesOfPlayerLettersAreEquivalentToWordLetters(LettersCounter.countOccurrencesOfEachLetter(player.getLetters()), LettersCounter.countOccurrencesOfEachLetter(lettersOfFeasibleWordWithPlayerLetters))) {
                        wordsToUse.add(feasibleWordWithPlayerLetters);
                    }
                } else {
                    // If the words aren't in the same position
                    boolean removed = false;
                    int i = 0;

                    while (!removed && i < alreadyPlayedWord.getLetters().size()) {
                        // One Letter from the played word is removed from the word to keep only letters that need to be played
                        if (lettersOfFeasibleWordWithPlayerLetters.contains(alreadyPlayedWord.getLetters().get(i))) {
                            lettersOfFeasibleWordWithPlayerLetters.remove(alreadyPlayedWord.getLetters().get(i));
                        }

                        // Remaining letters are compared with the player's letters to know if the word can be played
                        if (player.getLetters().size() >= lettersOfFeasibleWordWithPlayerLetters.size()
                                && PossibleTurnsFinder.occurrencesOfPlayerLettersAreEquivalentToWordLetters(LettersCounter.countOccurrencesOfEachLetter(player.getLetters()), LettersCounter.countOccurrencesOfEachLetter(lettersOfFeasibleWordWithPlayerLetters))) {
                            wordsToUse.add(feasibleWordWithPlayerLetters);
                            removed = true;
                        }

                        if (!removed) {
                            // If the word can't be played, the removed letter is put back and an other one is removed
                            lettersOfFeasibleWordWithPlayerLetters = LetterListToStringTransformer.reverseTransform(feasibleWordWithPlayerLetters, language);
                            i++;
                        }
                    }
                }
            }

            // Conversion of the playable words's list into a Map
            // Playable words are linked with a played word
            playableWords.put(alreadyPlayedWord, wordsToUse);

            // Conversion of playable words into a list of their letters
            // For each playable word (String) is associated their letters (List<LetterInterface>)
            Set<String> listOfPlayableWords = playableWords.get(alreadyPlayedWord);
            Map<String, List<LetterInterface>> listOfPlayableWordsWithLetters = new HashMap<>();
            for (String word : listOfPlayableWords) {
                listOfPlayableWordsWithLetters.put(word, LetterListToStringTransformer.reverseTransform(word, language));
            }

            // Creation of 2 lists to store words
            List<String> horizontalPlayableWords = new ArrayList<>();
            List<String> verticalPlayableWords = new ArrayList<>();
            Set<Map.Entry<String, List<LetterInterface>>> listOfPlayableWordsToCheck = listOfPlayableWordsWithLetters.entrySet();

            for (Map.Entry<String, List<LetterInterface>> word : listOfPlayableWordsToCheck) {
                if (alreadyPlayedWord.isHorizontal()) {
                    // If the played word is horizontal
                    if (wordsFeasibleFromAlreadyPlayedWords.contains(word.getKey())) {
                        // If the possible word is horizontal (letters added before and/or after the played word) and bigger than the played word
                        horizontalPlayableWords.add(word.getKey());
                    } else {
                        verticalPlayableWords.add(word.getKey());
                    }
                } else {
                    // If the played word is vertical
                    if (wordsFeasibleFromAlreadyPlayedWords.contains(word.getKey())) {
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
                SortedMap<BoardPosition, LetterInterface> wordToPlay;

                if (alreadyPlayedWord.isHorizontal()) {
                    wordToPlay = PossibleTurnsFinder.transformStringIntoTurn(word, alreadyPlayedWord.getStartLine(), (short) (alreadyPlayedWord.getStartColumn() - PossibleTurnsFinder.findSizePrefix(conversion, alreadyPlayedWord)), true, language, true, board);

                    // Check if the word can be played on the board
                    if (null != wordToPlay && PlayedTurnValidityChecker.isTurnPlayable(wordToPlay, language.getDictionary(), board, player)) {
                        possibleTurns.add(wordToPlay);
                    }
                } else {
                    // Add all the possible placement for the word on the board
                    possibleTurns.addAll(PossibleTurnsFinder.getPossiblePositions(board, alreadyPlayedWord, word, player, language));
                }
            }

            for (String word : verticalPlayableWords) {
                if (alreadyPlayedWord.isHorizontal()) {
                    // Add all the possible placement for the word on the board
                    possibleTurns.addAll(PossibleTurnsFinder.getPossiblePositions(board, alreadyPlayedWord, word, player, language));
                } else {
                    // Check if the word can be played on the board
                    SortedMap<BoardPosition, LetterInterface> wordToPlay;
                    List<LetterInterface> wordToPlayLetters = LetterListToStringTransformer.reverseTransform(word, language);

                    wordToPlay = PossibleTurnsFinder.transformLettersListIntoTurn(wordToPlayLetters, (short) (alreadyPlayedWord.getStartLine() - PossibleTurnsFinder.findSizePrefix(wordToPlayLetters, alreadyPlayedWord)), alreadyPlayedWord.getStartColumn(), true, true, board);

                    if (null != wordToPlay && PlayedTurnValidityChecker.isTurnPlayable(wordToPlay, language.getDictionary(), board, player)) {
                        possibleTurns.add(wordToPlay);
                    }
                }
            }
        }

        return possibleTurns;
    }

    /**
     * Returns the size of a word's prefix
     *
     * @param letters Letters of a word, can be played on an already played word
     * @param word    A played word
     *
     * @return The size of the prefix
     */
    protected static short findSizePrefix(List<LetterInterface> letters, WordInterface word) {
        List<LetterInterface> listToCheck = new ArrayList<>();
        short size = 0;

        // Go back in the playable word, check if the list contains the played word and increment the size of the list
        while (!(size == letters.size() || listToCheck.containsAll(word.getLetters()))) {
            listToCheck.add(letters.get(letters.size() - (size + 1)));
            size++;
        }

        return (short) (letters.size() - size);
    }

    /**
     * Returns a list of possible positions for a word based on an already placed word
     *
     * @param placedWord  An already placed word on the board
     * @param wordToPlace A word to place on the board
     *
     * @return all the possible positions for the wordToPlace
     */
    protected static List<SortedMap<BoardPosition, LetterInterface>> getPossiblePositions(BoardInterface board, WordInterface placedWord, String wordToPlace, PlayerInterface player, LanguageInterface language) {
        List<SortedMap<BoardPosition, LetterInterface>> possiblePositions = new ArrayList<>();
        List<LetterInterface> wordToPlaceLetters = LetterListToStringTransformer.reverseTransform(wordToPlace, language);

        // Comparison of the letters of the two words.
        // If their letters are equals, the playable word is added to the list of possible placement
        for (int i = 0, placedWordLettersSize = placedWord.getLetters().size(); i < placedWordLettersSize; i++) {
            for (int j = 0, wordToPlaceLettersSize = wordToPlaceLetters.size(); j < wordToPlaceLettersSize; j++) {
                if (placedWord.getLetters().get(i).equals(wordToPlaceLetters.get(j))) {
                    SortedMap<BoardPosition, LetterInterface> possiblePosition;

                    if (placedWord.isHorizontal()) {
                        possiblePosition = PossibleTurnsFinder.transformLettersListIntoTurn(wordToPlaceLetters, (short) (placedWord.getStartLine() - j), (short) (placedWord.getStartColumn() + i), false, true, board);
                    } else {
                        possiblePosition = PossibleTurnsFinder.transformLettersListIntoTurn(wordToPlaceLetters, (short) (placedWord.getStartLine() + i), (short) (placedWord.getStartColumn() - j), true, true, board);
                    }

                    if (null != possiblePosition && PlayedTurnValidityChecker.isTurnPlayable(possiblePosition, language.getDictionary(), board, player)) {
                        possiblePositions.add(possiblePosition);
                    }
                }
            }
        }

        return possiblePositions;
    }

    /**
     * Checks if the players letters are equivalent, or at least, enough, to form the word given by its letters
     *
     * @param occurrencesOfPlayerLetters The number of occurrences of player letters
     * @param occurrencesOfWordLetters   The number of occurrences of word letters
     *
     * @return true if the word can be formed with player letters, false otherwise
     */
    protected static boolean occurrencesOfPlayerLettersAreEquivalentToWordLetters(Map<LetterInterface, Integer> occurrencesOfPlayerLetters, Map<LetterInterface, Integer> occurrencesOfWordLetters) {
        for (Map.Entry<LetterInterface, Integer> m : occurrencesOfWordLetters.entrySet()) {
            if (!occurrencesOfPlayerLetters.containsKey(m.getKey()) || m.getValue() > occurrencesOfPlayerLetters.get(m.getKey())) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns the structure turn to be sent to the game
     *
     * @param string            The {@link String} to be played
     * @param startLine         The line of the first letter of the played letters
     * @param startColumn       The column of the first letter of the played letters
     * @param horizontal        True if the letters are disposed horizontally, false otherwise
     * @param checkBoardLetters True if we must check if letters have already been disposed with the given letters
     * @param board             The actual board
     *
     * @return the structure turn
     */
    protected static SortedMap<BoardPosition, LetterInterface> transformStringIntoTurn(String string, int startLine, int startColumn, boolean horizontal, LanguageInterface language, boolean checkBoardLetters, BoardInterface board) {
        return PossibleTurnsFinder.transformLettersListIntoTurn(LetterListToStringTransformer.reverseTransform(string, language), startLine, startColumn, horizontal, checkBoardLetters, board);
    }

    /**
     * Returns the structure turn to be sent to the game
     *
     * @param letters           The letters to be played
     * @param startLine         The line of the first letter of the played letters
     * @param startColumn       The column of the first letter of the played letters
     * @param horizontal        True if the letters are disposed horizontally, false otherwise
     * @param checkBoardLetters True if we must check if letters have already been disposed with the given letters
     * @param board             The actual board
     *
     * @return the structure turn
     */
    protected static SortedMap<BoardPosition, LetterInterface> transformLettersListIntoTurn(List<LetterInterface> letters, int startLine, int startColumn, boolean horizontal, boolean checkBoardLetters, BoardInterface board) {
        SortedMap<BoardPosition, LetterInterface> turn = new TreeMap<>();

        for (int i = 0; i < letters.size(); i++) {
            short line, column;

            if (horizontal) {
                line = (short) startLine;
                column = (short) (startColumn + i);
            } else {
                line = (short) (startLine + i);
                column = (short) startColumn;
            }

            if (line < 0 || line >= BoardInterface.BOARD_SIZE || column < 0 || column >= BoardInterface.BOARD_SIZE) {
                return null;
            }

            if (!checkBoardLetters || null == board.getLetters().get(line).get(column)) {
                turn.put(new BoardPosition(line, column), letters.get(i));
            }
        }

        return !turn.isEmpty() ? turn : null;
    }
}