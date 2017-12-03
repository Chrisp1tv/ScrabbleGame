package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.exceptions.InvalidPlayedTurnException;
import istv.chrisanc.scrabble.model.BoardPosition;
import istv.chrisanc.scrabble.model.Word;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class manages the checking of played words on the Scrabble game. It checks if played words on a turn are valid, that is
 * to say if these words respect the Scrabble rules.
 * TODO: Some optimization of the code is possible, would be interesting to do it once other tasks have been done
 *
 * @author Christopher Anciaux
 */
abstract public class PlayedTurnValidityChecker {
    /**
     * The minimal number of letters in a word
     */
    protected final static short MINIMAL_NUMBER_OF_LETTERS_IN_WORD = 2;

    /**
     * Tells if the given word (actually, the letters of the given word) can be used to form new words, by checking its validity with the actual game
     *
     * @param word       The word to check
     * @param dictionary The dictionary used to check words existence
     * @param board      The actual board
     * @param player     The player who play this turn
     *
     * @return true if the turn is possible, false otherwise
     */
    public static boolean turnIsPossible(WordInterface word, DictionaryInterface dictionary, BoardInterface board, PlayerInterface player) {
        SortedMap<BoardPosition, LetterInterface> playedLetters = PlayedTurnValidityChecker.transformWordIntoPlayedTurn(word, board);

        try {
            PlayedTurnValidityChecker.findPlayedWords(dictionary, board, playedLetters, player);

            return true;
        } catch (InvalidPlayedTurnException e) {
            return false;
        }
    }

    /**
     * Finds all the played words of the current if the turn is valid
     *
     * @param dictionary    The dictionary used to check words existence
     * @param board         The actual board
     * @param playedLetters The letters played in the current turn
     * @param player        The player who played this turn
     *
     * @return The played words of the current turn
     * @throws InvalidPlayedTurnException if the turn isn't valid, that is to say it doesn't respect all Scrabble rules
     */
    public static List<WordInterface> findPlayedWords(DictionaryInterface dictionary, BoardInterface board, SortedMap<BoardPosition, LetterInterface> playedLetters, PlayerInterface player) throws InvalidPlayedTurnException {
        boolean horizontal;
        List<WordInterface> playedWords = new ArrayList<>();

        // If the board is empty, that is to say that no turn has been played yet
        if (board.isEmpty()) {
            if (playedLetters.size() < PlayedTurnValidityChecker.MINIMAL_NUMBER_OF_LETTERS_IN_WORD) {
                throw new InvalidPlayedTurnException("exceptions.invalidPlayedTurn.notEnoughPlayedLettersAtGameBeginning");
            }

            horizontal = PlayedTurnValidityChecker.playedLettersAreDisposedHorizontally(playedLetters);

            if (!PlayedTurnValidityChecker.isTurnPlayedOnTheCenterSquare(playedLetters)) {
                throw new InvalidPlayedTurnException("exceptions.invalidPlayedTurn.notPlayedOnCenterSquare");
            }

            if (!PlayedTurnValidityChecker.allPlayedLettersAreNextToEachOther(playedLetters, horizontal)) {
                throw new InvalidPlayedTurnException("exceptions.invalidPlayedTurn.playedLettersNotNextToEachOther");
            }

            PlayedTurnValidityChecker.findAllFormedWords(dictionary, board, true, playedLetters, playedWords, player, horizontal);
        } else {
            if (1 > playedLetters.size()) {
                throw new InvalidPlayedTurnException("exceptions.invalidPlayedTurn.notEnoughPlayedLettersDuringGame");
            }

            horizontal = PlayedTurnValidityChecker.playedLettersAreDisposedHorizontally(playedLetters);

            if (!PlayedTurnValidityChecker.allPlayedLettersAreDisposedNextToOtherLetters(board, playedLetters, horizontal)) {
                throw new InvalidPlayedTurnException("exceptions.invalidPlayedTurn.playedLettersNotAdjacentToAlreadyPlayedLetters");
            }

            PlayedTurnValidityChecker.findAllFormedWords(dictionary, board, false, playedLetters, playedWords, player, horizontal);
        }

        return playedWords;
    }

    /**
     * Checks whether the played letters have been played horizontally or not
     *
     * @param playedLetters The letters played in the current turn
     *
     * @return True if the letters have been played horizontally, false otherwise
     * @throws InvalidPlayedTurnException if the letters haven't been played horizontally, nor vertically
     */
    protected static boolean playedLettersAreDisposedHorizontally(SortedMap<BoardPosition, LetterInterface> playedLetters) throws InvalidPlayedTurnException {
        Iterator<SortedMap.Entry<BoardPosition, LetterInterface>> playedLettersIterator = playedLetters.entrySet().iterator();

        Map.Entry<BoardPosition, LetterInterface> firstLetter = playedLettersIterator.next();

        // If there is only one letter played
        if (!playedLettersIterator.hasNext()) {
            return true;
        }

        Map.Entry<BoardPosition, LetterInterface> nextLetter = playedLettersIterator.next();

        boolean horizontal;
        if (firstLetter.getKey().getLine() == nextLetter.getKey().getLine()) {
            horizontal = true;
        } else if (firstLetter.getKey().getColumn() == nextLetter.getKey().getColumn()) {
            horizontal = false;
        } else {
            throw new InvalidPlayedTurnException("exceptions.invalidPlayedTurn.lettersNotOnSameAxis");
        }

        // Checks if the letters are played on the same axis
        while (playedLettersIterator.hasNext()) {
            nextLetter = playedLettersIterator.next();

            if (horizontal) {
                if (firstLetter.getKey().getLine() != nextLetter.getKey().getLine()) {
                    throw new InvalidPlayedTurnException("exceptions.invalidPlayedTurn.lettersNotOnSameAxis");
                }
            } else {
                if (firstLetter.getKey().getColumn() != nextLetter.getKey().getColumn()) {
                    throw new InvalidPlayedTurnException("exceptions.invalidPlayedTurn.lettersNotOnSameAxis");
                }
            }
        }

        return horizontal;
    }

    /**
     * Checks if a turn has been played on the center square, that is to say that at least one letter has been putted on
     * the center square
     *
     * @param playedLetters The letters played in the current turn
     *
     * @return true if one of the played letters have been played on the center square, false otherwise
     */
    protected static boolean isTurnPlayedOnTheCenterSquare(SortedMap<BoardPosition, LetterInterface> playedLetters) {
        int centerSquarePosition = BoardInterface.BOARD_SIZE / 2;

        for (BoardPosition playedLetterPosition : playedLetters.keySet()) {
            if (playedLetterPosition.getLine() == centerSquarePosition && playedLetterPosition.getColumn() == centerSquarePosition) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the played letters are all next to each other (it just checks that all played letters are next to each other,
     * without considering letters played in a previous turn and already disposed on the board)
     *
     * @param playedLetters The letters played in the current turn
     * @param horizontal    True if the played letters are disposed horizontally, false otherwise
     *
     * @return true if the played letters are all next to each other, false otherwise
     */
    protected static boolean allPlayedLettersAreNextToEachOther(SortedMap<BoardPosition, LetterInterface> playedLetters, boolean horizontal) {
        Iterator<SortedMap.Entry<BoardPosition, LetterInterface>> playedLettersIterator = playedLetters.entrySet().iterator();
        Map.Entry<BoardPosition, LetterInterface> currentLetter;
        Map.Entry<BoardPosition, LetterInterface> nextLetter;

        currentLetter = playedLettersIterator.next();
        while (playedLettersIterator.hasNext()) {
            nextLetter = playedLettersIterator.next();

            if (horizontal) {
                if (currentLetter.getKey().getColumn() + 1 != nextLetter.getKey().getColumn()) {
                    return false;
                }
            } else if (currentLetter.getKey().getLine() + 1 != nextLetter.getKey().getLine()) {
                return false;
            }

            currentLetter = nextLetter;
        }

        return true;
    }

    /**
     * Checks if the played letters are all disposed next to each other or/and next to already played letters
     *
     * @param board         The actual board
     * @param playedLetters The letters played in the current turn
     * @param horizontal    True if the played letters are disposed horizontally, false otherwise
     *
     * @return true if the played letters are all next to each other or to already played letters
     */
    protected static boolean allPlayedLettersAreDisposedNextToOtherLetters(BoardInterface board, SortedMap<BoardPosition, LetterInterface> playedLetters, boolean horizontal) {
        // First, we check that all played letters are linked to other letters, without any "hole" (a square without letter on)
        // between the first and the last played letter
        Iterator<SortedMap.Entry<BoardPosition, LetterInterface>> playedLettersIterator = playedLetters.entrySet().iterator();
        if (horizontal) {
            for (int column = playedLetters.firstKey().getColumn(); column < BoardInterface.BOARD_SIZE && playedLettersIterator.hasNext(); column++) {
                if (null == board.getLetters().get(playedLetters.firstKey().getLine()).get(column) && column != playedLettersIterator.next().getKey().getColumn()) {
                    return false;
                }
            }
        } else {
            for (int line = playedLetters.firstKey().getLine(); line < BoardInterface.BOARD_SIZE && playedLettersIterator.hasNext(); line++) {
                if (null == board.getLetters().get(line).get(playedLetters.firstKey().getColumn()) && line != playedLettersIterator.next().getKey().getLine()) {
                    return false;
                }
            }
        }

        // Finally, we check that at least one letter is played next to a board letter (played in a previous turn)
        for (SortedMap.Entry<BoardPosition, LetterInterface> playedLetter : playedLetters.entrySet()) {
            if (PlayedTurnValidityChecker.hasAdjacentBoardLetter(board, playedLetter)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the given letter has an adjacent board letter
     *
     * @param board        The actual board
     * @param playedLetter The played letter
     *
     * @return true if the letter has an adjacent board letter, false otherwise
     */
    protected static boolean hasAdjacentBoardLetter(BoardInterface board, SortedMap.Entry<BoardPosition, LetterInterface> playedLetter) {
        return PlayedTurnValidityChecker.playedLetterHasBoardLetterAbove(board, playedLetter) || PlayedTurnValidityChecker.playedLetterHasBoardLetterAtRight(board, playedLetter) || PlayedTurnValidityChecker.playedLetterHasBoardLetterBelow(board, playedLetter) || PlayedTurnValidityChecker.playedLetterHasBoardLetterAtLeft(board, playedLetter);
    }

    /**
     * Checks if the given letter has a board letter to its left
     *
     * @param board        The actual board
     * @param playedLetter The played letter
     *
     * @return true if the latter has a letter to its left, false otherwise
     */
    protected static boolean playedLetterHasBoardLetterAtLeft(BoardInterface board, SortedMap.Entry<BoardPosition, LetterInterface> playedLetter) {
        return playedLetter.getKey().getColumn() > 0 && null != board.getLetters().get(playedLetter.getKey().getLine()).get(playedLetter.getKey().getColumn() - 1);
    }

    /**
     * Checks if the given letter has a board letter to its right
     *
     * @param board        The actual board
     * @param playedLetter The played letter
     *
     * @return true if the latter has a letter to its right, false otherwise
     */
    protected static boolean playedLetterHasBoardLetterAtRight(BoardInterface board, SortedMap.Entry<BoardPosition, LetterInterface> playedLetter) {
        return playedLetter.getKey().getColumn() < BoardInterface.BOARD_SIZE - 1 && null != board.getLetters().get(playedLetter.getKey().getLine()).get(playedLetter.getKey().getColumn() + 1);
    }

    /**
     * Checks if the given letter has a board letter above
     *
     * @param board        The actual board
     * @param playedLetter The played letter
     *
     * @return true if the latter has a letter above, false otherwise
     */
    protected static boolean playedLetterHasBoardLetterAbove(BoardInterface board, SortedMap.Entry<BoardPosition, LetterInterface> playedLetter) {
        return playedLetter.getKey().getLine() > 0 && null != board.getLetters().get(playedLetter.getKey().getLine() - 1).get(playedLetter.getKey().getColumn());
    }

    /**
     * Checks if the given letter has a board letter below
     *
     * @param board        The actual board
     * @param playedLetter The played letter
     *
     * @return true if the latter has a letter below, false otherwise
     */
    protected static boolean playedLetterHasBoardLetterBelow(BoardInterface board, SortedMap.Entry<BoardPosition, LetterInterface> playedLetter) {
        return playedLetter.getKey().getLine() < BoardInterface.BOARD_SIZE - 1 && null != board.getLetters().get(playedLetter.getKey().getLine() + 1).get(playedLetter.getKey().getColumn());
    }

    /**
     * Adds the given word to the actual played words if the given word exists in the current language
     *
     * @param dictionary  The dictionary used to check words existence
     * @param playedWords The actual played words
     * @param player      The player who played the word
     * @param wordLetters The word letters
     * @param horizontal  True if the word is horizontal, false otherwise
     * @param startLine   The line of the first letter of the word
     * @param startColumn The column of the first letter of the word
     *
     * @throws InvalidPlayedTurnException if the word doesn't exist in the given dictionary
     */
    protected static void addWordAfterChecking(DictionaryInterface dictionary, List<WordInterface> playedWords, PlayerInterface player, List<LetterInterface> wordLetters, boolean horizontal, short startLine, short startColumn) throws InvalidPlayedTurnException {
        if (!dictionary.wordExists(LetterListToStringTransformer.transform(wordLetters))) {
            throw new InvalidPlayedTurnException("exceptions.invalidPlayedTurn.nonExistentWord");
        }

        playedWords.add(new Word(player, wordLetters, horizontal, startLine, startColumn));
    }

    /**
     * Finds all the words formed by the given played letters
     *
     * @param dictionary    The dictionary used to check words existence
     * @param board         The actual board
     * @param boardEmpty    True if the board is empty, false otherwise
     * @param playedLetters The played letters
     * @param playedWords   The played words
     * @param player        The player who played the letters
     * @param horizontal    True if the letters have been disposed horizontally, false otherwise
     *
     * @throws InvalidPlayedTurnException if one of the formed words don't exist
     */
    protected static void findAllFormedWords(DictionaryInterface dictionary, BoardInterface board, boolean boardEmpty, SortedMap<BoardPosition, LetterInterface> playedLetters, List<WordInterface> playedWords, PlayerInterface player, boolean horizontal) throws InvalidPlayedTurnException {
        if (boardEmpty) {
            PlayedTurnValidityChecker.addWordAfterChecking(dictionary, playedWords, player, new ArrayList<>(playedLetters.values()), horizontal, playedLetters.firstKey().getLine(), playedLetters.firstKey().getColumn());

            return;
        }

        Iterator<SortedMap.Entry<BoardPosition, LetterInterface>> playedLettersIterator = playedLetters.entrySet().iterator();

        if (1 == playedLetters.size()) {
            SortedMap.Entry<BoardPosition, LetterInterface> currentLetterEntry = playedLettersIterator.next();

            PlayedTurnValidityChecker.addHorizontalWordFormedFromOnePlayedLetter(dictionary, player, playedWords, board, currentLetterEntry);
            PlayedTurnValidityChecker.addVerticalWordFormedFromOnePlayedLetter(dictionary, player, playedWords, board, currentLetterEntry);
        } else if (horizontal) {
            // First, we find the word formed on the actual line. Let's find the first letter of the word, which may be a letter played in a previous turn,
            // and the last letter

            int line = playedLetters.firstKey().getLine();
            int currentColumn = 0;
            while (playedLetters.firstKey().getColumn() - currentColumn - 1 >= 0 && null != board.getLetters().get(line).get(playedLetters.firstKey().getColumn() - currentColumn - 1)) {
                currentColumn++;
            }

            int startColumn = playedLetters.firstKey().getColumn() - currentColumn;

            List<LetterInterface> horizontalWordLetters = new ArrayList<>();
            LetterInterface currentLetter;
            currentColumn = startColumn;

            do {
                if (null != board.getLetters().get(line).get(currentColumn)) {
                    currentLetter = board.getLetters().get(line).get(currentColumn);
                } else if (playedLettersIterator.hasNext()) {
                    SortedMap.Entry<BoardPosition, LetterInterface> currentLetterEntry = playedLettersIterator.next();
                    currentLetter = currentLetterEntry.getValue();

                    PlayedTurnValidityChecker.addVerticalWordFormedFromOnePlayedLetter(dictionary, player, playedWords, board, currentLetterEntry);
                } else {
                    break;
                }

                horizontalWordLetters.add(currentLetter);
                currentColumn++;
            } while (currentColumn < BoardInterface.BOARD_SIZE);

            PlayedTurnValidityChecker.addWordAfterChecking(dictionary, playedWords, player, horizontalWordLetters, true, (short) line, (short) startColumn);
        } else {
            int column = playedLetters.firstKey().getColumn();
            int currentLine = 0;
            while (playedLetters.firstKey().getLine() - currentLine - 1 >= 0 && null != board.getLetters().get(playedLetters.firstKey().getLine() - currentLine - 1).get(column)) {
                currentLine++;
            }

            int startLine = playedLetters.firstKey().getLine() - currentLine;

            List<LetterInterface> verticalWordLetters = new ArrayList<>();
            LetterInterface currentLetter;
            currentLine = startLine;

            do {
                if (null != board.getLetters().get(currentLine).get(column)) {
                    currentLetter = board.getLetters().get(currentLine).get(column);
                } else if (playedLettersIterator.hasNext()) {
                    SortedMap.Entry<BoardPosition, LetterInterface> currentLetterEntry = playedLettersIterator.next();
                    currentLetter = currentLetterEntry.getValue();

                    PlayedTurnValidityChecker.addHorizontalWordFormedFromOnePlayedLetter(dictionary, player, playedWords, board, currentLetterEntry);
                } else {
                    break;
                }

                verticalWordLetters.add(currentLetter);
                currentLine++;
            } while (currentLine < BoardInterface.BOARD_SIZE);

            PlayedTurnValidityChecker.addWordAfterChecking(dictionary, playedWords, player, verticalWordLetters, true, (short) startLine, (short) column);
        }
    }

    /**
     * Checks if a word can be formed vertically from one played letter, and if so, checks if this word exists and adds
     * it to the played words
     *
     * @param dictionary   The dictionary used to check words existence
     * @param player       The player who played the word
     * @param playedWords  The actual played words
     * @param board        The actual board
     * @param playedLetter The played letter
     *
     * @throws InvalidPlayedTurnException if the formed word doesn't exist
     */
    protected static void addVerticalWordFormedFromOnePlayedLetter(DictionaryInterface dictionary, PlayerInterface player, List<WordInterface> playedWords, BoardInterface board, SortedMap.Entry<BoardPosition, LetterInterface> playedLetter) throws InvalidPlayedTurnException {
        if (!PlayedTurnValidityChecker.playedLetterHasBoardLetterAbove(board, playedLetter) && !PlayedTurnValidityChecker.playedLetterHasBoardLetterBelow(board, playedLetter)) {
            return;
        }

        int currentLine = 0;
        while (playedLetter.getKey().getLine() - currentLine - 1 >= 0 && null != board.getLetters().get(playedLetter.getKey().getLine() - currentLine - 1).get(playedLetter.getKey().getColumn())) {
            currentLine++;
        }

        short startLine = (short) (playedLetter.getKey().getLine() - currentLine);
        currentLine = startLine;
        LetterInterface currentLetter;
        List<LetterInterface> wordLetters = new ArrayList<>();
        do {
            if (null != board.getLetters().get(currentLine).get(playedLetter.getKey().getColumn())) {
                currentLetter = board.getLetters().get(currentLine).get(playedLetter.getKey().getColumn());
            } else if (playedLetter.getKey().getLine() == currentLine) {
                currentLetter = playedLetter.getValue();
            } else {
                break;
            }

            wordLetters.add(currentLetter);
            currentLine++;
        } while (currentLine < BoardInterface.BOARD_SIZE);

        PlayedTurnValidityChecker.addWordAfterChecking(dictionary, playedWords, player, wordLetters, false, startLine, playedLetter.getKey().getColumn());
    }

    /**
     * Checks if a word can be formed horizontally from one played letter, and if so, checks if this word exists and adds
     * it to the played words
     *
     * @param dictionary   The dictionary used to check words existence
     * @param player       The player who played the word
     * @param playedWords  The actual played words
     * @param board        The actual board
     * @param playedLetter The played letter
     *
     * @throws InvalidPlayedTurnException if the formed word doesn't exist
     */
    protected static void addHorizontalWordFormedFromOnePlayedLetter(DictionaryInterface dictionary, PlayerInterface player, List<WordInterface> playedWords, BoardInterface board, SortedMap.Entry<BoardPosition, LetterInterface> playedLetter) throws InvalidPlayedTurnException {
        if (!PlayedTurnValidityChecker.playedLetterHasBoardLetterAtLeft(board, playedLetter) && !PlayedTurnValidityChecker.playedLetterHasBoardLetterAtRight(board, playedLetter)) {
            return;
        }

        int currentColumn = 0;
        while (playedLetter.getKey().getColumn() - currentColumn - 1 >= 0 && null != board.getLetters().get(playedLetter.getKey().getLine()).get(playedLetter.getKey().getColumn() - currentColumn - 1)) {
            currentColumn++;
        }

        short startColumn = (short) (playedLetter.getKey().getColumn() - currentColumn);
        currentColumn = startColumn;
        LetterInterface currentLetter;
        List<LetterInterface> wordLetters = new ArrayList<>();
        do {
            if (null != board.getLetters().get(playedLetter.getKey().getLine()).get(currentColumn)) {
                currentLetter = board.getLetters().get(playedLetter.getKey().getLine()).get(currentColumn);
            } else if (playedLetter.getKey().getColumn() == currentColumn) {
                currentLetter = playedLetter.getValue();
            } else {
                break;
            }

            wordLetters.add(currentLetter);
            currentColumn++;
        } while (currentColumn < BoardInterface.BOARD_SIZE);

        PlayedTurnValidityChecker.addWordAfterChecking(dictionary, playedWords, player, wordLetters, false, playedLetter.getKey().getLine(), startColumn);
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
