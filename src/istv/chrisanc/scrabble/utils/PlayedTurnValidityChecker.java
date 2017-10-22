package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.controllers.GameController;
import istv.chrisanc.scrabble.exceptions.InvalidPlayedTurnException;
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
abstract public class PlayedTurnValidityChecker {
    protected final static short MINIMAL_NUMBER_OF_LETTERS_IN_WORD = 2;

    public static boolean playedWordsAreValid(BoardInterface board, SortedMap<GameController.BoardPosition, LetterInterface> playedLetters) throws InvalidPlayedTurnException {
        boolean horizontal;

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
        } else {
            if (1 > playedLetters.size()) {
                throw new InvalidPlayedTurnException("exceptions.invalidPlayedTurn.notEnoughPlayedLettersDuringGame");
            }

            horizontal = PlayedTurnValidityChecker.playedLettersAreDisposedHorizontally(playedLetters);

            if (!PlayedTurnValidityChecker.allPlayedLettersAreDisposedNextToOtherLetters(board, playedLetters, horizontal)) {
                throw new InvalidPlayedTurnException("exceptions.invalidPlayedTurn.playedLettersNotAdjacentToAlreadyPlayedLetters");
            }
        }

        return true;
    }

    public static List<WordInterface> findPlayedWords(BoardInterface board, SortedMap<GameController.BoardPosition, LetterInterface> playedLetters, PlayerInterface player) throws InvalidPlayedTurnException {
        // The player is given, to attribute him the played words
        // TODO
        List<WordInterface> playedWords = new ArrayList<>();

        return playedWords;
    }

    protected static boolean playedLettersAreDisposedHorizontally(SortedMap<GameController.BoardPosition, LetterInterface> playedLetters) throws InvalidPlayedTurnException {
        Iterator<SortedMap.Entry<GameController.BoardPosition, LetterInterface>> playedLettersIterator = playedLetters.entrySet().iterator();

        Map.Entry<GameController.BoardPosition, LetterInterface> firstLetter = playedLettersIterator.next();

        // If there is only one letter played
        if (!playedLettersIterator.hasNext()) {
            return true;
        }

        Map.Entry<GameController.BoardPosition, LetterInterface> nextLetter = playedLettersIterator.next();

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
            if (!PlayedTurnValidityChecker.hasAdjacentLetters(board, playedLetters, playedLetterEntry, horizontal)) {
                return false;
            }
        }

        return true;
    }

    protected static boolean hasAdjacentLetters(BoardInterface board, SortedMap<GameController.BoardPosition, LetterInterface> playedLetters, SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetter, boolean horizontal) {
        if (1 == playedLetters.size()) {
            return PlayedTurnValidityChecker.playedLetterHasBoardLetterAtLeft(board, playedLetter)
                    || PlayedTurnValidityChecker.playedLetterHasBoardLetterAtRight(board, playedLetter)
                    || PlayedTurnValidityChecker.playedLetterHasBoardLetterAbove(board, playedLetter)
                    || PlayedTurnValidityChecker.playedLetterHasBoardLetterBelow(board, playedLetter);
        }

        if (horizontal) {
            if (PlayedTurnValidityChecker.playedLetterHasBoardLetterAtLeft(board, playedLetter) || PlayedTurnValidityChecker.playedLetterHasBoardLetterAtRight(board, playedLetter)) {
                return true;
            }

            for (SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetterEntry : playedLetters.entrySet()) {
                if (playedLetter.getKey().getColumn() > 0 && playedLetterEntry.getKey().getColumn() == playedLetter.getKey().getColumn() - 1
                        || playedLetter.getKey().getColumn() < BoardInterface.BOARD_SIZE - 1 && playedLetterEntry.getKey().getColumn() == playedLetter.getKey().getColumn() + 1) {
                    return true;
                }
            }
        } else {
            if (PlayedTurnValidityChecker.playedLetterHasBoardLetterAbove(board, playedLetter) || PlayedTurnValidityChecker.playedLetterHasBoardLetterBelow(board, playedLetter)) {
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

    protected static boolean playedLetterHasBoardLetterAtLeft(BoardInterface board, SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetter) {
        return playedLetter.getKey().getColumn() > 0 && null != board.getLetters().get(playedLetter.getKey().getLine()).get(playedLetter.getKey().getColumn() - 1);
    }

    static protected boolean playedLetterHasBoardLetterAtRight(BoardInterface board, SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetter) {
        return playedLetter.getKey().getColumn() < BoardInterface.BOARD_SIZE - 1 && null != board.getLetters().get(playedLetter.getKey().getLine()).get(playedLetter.getKey().getColumn() + 1);
    }

    static protected boolean playedLetterHasBoardLetterAbove(BoardInterface board, SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetter) {
        return playedLetter.getKey().getLine() > 0 && null != board.getLetters().get(playedLetter.getKey().getLine() - 1).get(playedLetter.getKey().getColumn());
    }

    static protected boolean playedLetterHasBoardLetterBelow(BoardInterface board, SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetter) {
        return playedLetter.getKey().getLine() < BoardInterface.BOARD_SIZE - 1 && null != board.getLetters().get(playedLetter.getKey().getLine() + 1).get(playedLetter.getKey().getColumn());
    }
}
