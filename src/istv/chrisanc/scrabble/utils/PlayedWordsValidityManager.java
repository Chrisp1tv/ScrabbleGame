package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.controllers.GameController;
import istv.chrisanc.scrabble.exceptions.NonExistentWordException;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * <p>
 * This class manages the checking of a played words on the Scrabble game. It checks if played words on a turn are valid, that is
 * to say if these words respect the Scrabble rules.
 *
 * @author Christopher Anciaux
 */
abstract public class PlayedWordsValidityManager {
    protected final static short MINIMAL_NUMBER_OF_LETTERS_IN_WORD = 2;

    public static boolean playedWordsAreValid(BoardInterface board, SortedMap<GameController.BoardPosition, LetterInterface> playedLetters) {
        boolean horizontal;

        try {
            horizontal = PlayedWordsValidityManager.playedLettersAreDisposedHorizontally(playedLetters);
        } catch (Exception e) {
            return false;
        }

        if (board.isEmpty()) {
            // If the board is empty, that is to say that no turn has been played yet
            return playedLetters.size() >= PlayedWordsValidityManager.MINIMAL_NUMBER_OF_LETTERS_IN_WORD && PlayedWordsValidityManager.isTurnPlayedOnTheCenterSquare(playedLetters) && PlayedWordsValidityManager.allPlayedLettersAreNextToEachOther(playedLetters, horizontal);
        } else {
            return PlayedWordsValidityManager.allPlayedLettersAreDisposedNextToOtherLetters(board, playedLetters, horizontal);
        }
    }

    public static List<WordInterface> findPlayedWords(BoardInterface board, SortedMap<GameController.BoardPosition, LetterInterface> playedLetters, PlayerInterface player) throws NonExistentWordException {
        // The player is given, to attribute him the played words
        // TODO
        List<WordInterface> playedWords = new ArrayList<>();

        return playedWords;
    }

    protected static boolean playedLettersAreDisposedHorizontally(SortedMap<GameController.BoardPosition, LetterInterface> playedLetters) throws Exception {
        Iterator<SortedMap.Entry<GameController.BoardPosition, LetterInterface>> playedLettersIterator = playedLetters.entrySet().iterator();

        Map.Entry<GameController.BoardPosition, LetterInterface> firstLetter = playedLettersIterator.next();
        Map.Entry<GameController.BoardPosition, LetterInterface> nextLetter = playedLettersIterator.next();

        boolean horizontal;
        if (firstLetter.getKey().getLine() == nextLetter.getKey().getLine()) {
            horizontal = true;
        } else if (firstLetter.getKey().getColumn() == nextLetter.getKey().getColumn()) {
            horizontal = false;
        } else {
            throw new Exception(); // TODO: throw a more explicative exception
        }

        // Checks if the letters are played on the same axis
        while (playedLettersIterator.hasNext()) {
            nextLetter = playedLettersIterator.next();

            if (horizontal) {
                if (firstLetter.getKey().getLine() != nextLetter.getKey().getLine()) {
                    throw new Exception(); // TODO: throw a more explicative exception
                }
            } else {
                if (firstLetter.getKey().getColumn() != nextLetter.getKey().getColumn()) {
                    throw new Exception(); // TODO: throw a more explicative exception
                }
            }
        }

        return horizontal;
    }

    protected static boolean isTurnPlayedOnTheCenterSquare(SortedMap<GameController.BoardPosition, LetterInterface> playedLetters) {
        int centerSquarePosition = BoardInterface.BOARD_SIZE / 2;

        for (GameController.BoardPosition playedLetterPosition : playedLetters.keySet()) {
            if (playedLetterPosition.getLine() == centerSquarePosition && playedLetterPosition.getColumn() == centerSquarePosition) {
                return true;
            }
        }

        return false;
    }

    protected static boolean allPlayedLettersAreNextToEachOther(SortedMap<GameController.BoardPosition, LetterInterface> playedLetters, boolean horizontal) {
        Iterator<SortedMap.Entry<GameController.BoardPosition, LetterInterface>> playedLettersIterator = playedLetters.entrySet().iterator();
        Map.Entry<GameController.BoardPosition, LetterInterface> currentLetter;
        Map.Entry<GameController.BoardPosition, LetterInterface> nextLetter;

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

    protected static boolean allPlayedLettersAreDisposedNextToOtherLetters(BoardInterface board, SortedMap<GameController.BoardPosition, LetterInterface> playedLetters, boolean horizontal) {
        for (SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetterEntry : playedLetters.entrySet()) {
            if (!PlayedWordsValidityManager.hasAdjacentLetters(board, playedLetters, playedLetterEntry, horizontal)) {
                return false;
            }
        }

        return true;
    }

    protected static boolean hasAdjacentLetters(BoardInterface board, SortedMap<GameController.BoardPosition, LetterInterface> playedLetters, SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetter, boolean horizontal) {
        // TODO: Check if this algorithm works as expected
        if (horizontal) {
            if (playedLetter.getKey().getColumn() > 0 && null != board.getLetters().get(playedLetter.getKey().getLine()).get(playedLetter.getKey().getColumn() - 1)
                    || playedLetter.getKey().getColumn() < BoardInterface.BOARD_SIZE - 1 && null != board.getLetters().get(playedLetter.getKey().getLine()).get(playedLetter.getKey().getColumn() + 1)) {
                return true;
            }

            for (SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetterEntry : playedLetters.entrySet()) {
                if (playedLetter.getKey().getColumn() > 0 && playedLetterEntry.getKey().getColumn() == playedLetter.getKey().getColumn() - 1
                        || playedLetter.getKey().getColumn() < BoardInterface.BOARD_SIZE - 1 && playedLetterEntry.getKey().getColumn() == playedLetter.getKey().getColumn() + 1) {
                    return true;
                }
            }
        } else {
            if (playedLetter.getKey().getLine() > 0 && null != board.getLetters().get(playedLetter.getKey().getLine() - 1).get(playedLetter.getKey().getColumn())
                    || playedLetter.getKey().getLine() < BoardInterface.BOARD_SIZE - 1 && null != board.getLetters().get(playedLetter.getKey().getLine() + 1).get(playedLetter.getKey().getColumn())) {
                return true;
            }

            for (SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetterEntry : playedLetters.entrySet()) {
                if (playedLetter.getKey().getLine() > 0 && playedLetterEntry.getKey().getLine() == playedLetter.getKey().getLine() - 1
                        || playedLetter.getKey().getLine() < BoardInterface.BOARD_SIZE - 1 && playedLetterEntry.getKey().getLine() == playedLetter.getKey().getLine() + 1) {
                    return true;
                }
            }
        }

        return false;
    }
}
