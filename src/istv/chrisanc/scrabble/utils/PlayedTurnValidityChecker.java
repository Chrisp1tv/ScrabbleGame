package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.controllers.GameController;
import istv.chrisanc.scrabble.exceptions.InvalidPlayedTurnException;
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

/**
 * <p>
 * This class manages the checking of a played words on the Scrabble game. It checks if played words on a turn are valid, that is
 * to say if these words respect the Scrabble rules.
 *
 * @author Christopher Anciaux
 * @author Julien Basquin
 * @author Anthony Delétré
 */
abstract public class PlayedTurnValidityChecker {
    protected final static short MINIMAL_NUMBER_OF_LETTERS_IN_WORD = 2;

    public static List<WordInterface> findPlayedWords(DictionaryInterface dictionary, BoardInterface board, SortedMap<GameController.BoardPosition, LetterInterface> playedLetters, PlayerInterface player) throws InvalidPlayedTurnException {
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

    protected static void addWordAfterChecking(DictionaryInterface dictionary, List<WordInterface> playedWords, PlayerInterface player, List<LetterInterface> wordLetters, boolean horizontal, short startLine, short startColumn) throws InvalidPlayedTurnException {
        System.out.println(wordLetters);

        if(!dictionary.wordExists(LetterListToStringTransformer.transform(wordLetters))) {
            throw new InvalidPlayedTurnException("exceptions.invalidPlayedTurn.nonExistentWord");
        }

        playedWords.add(new Word(player, wordLetters, horizontal, startLine, startColumn));
    }

    protected static void findAllFormedWords(DictionaryInterface dictionary, BoardInterface board, boolean boardEmpty, SortedMap<GameController.BoardPosition, LetterInterface> playedLetters, List<WordInterface> playedWords, PlayerInterface player, boolean horizontal) throws InvalidPlayedTurnException {
        if (boardEmpty) {
            PlayedTurnValidityChecker.addWordAfterChecking(dictionary, playedWords, player, new ArrayList<>(playedLetters.values()), horizontal, playedLetters.firstKey().getLine(), playedLetters.firstKey().getColumn());

            return;
        }

        if (1 == playedLetters.size()) {
            // Find all the words which might have been formed at left, top, right, bottom...
        } else if (horizontal) {
            // TODO: Buggy, find the problem :)
            // First, add the horizontal word done with all the played letters (and eventually the letters already disposed on the board)
            // Then, for each letter, find the vertical word which may have been formed

            // First, we find the word formed on the actual line. Let's find the first letter of the word, which may be a letter played in a previous turn
            int currentColumnAtLeft = 0;
            while (null != board.getLetters().get(playedLetters.firstKey().getLine()).get(playedLetters.firstKey().getColumn() - currentColumnAtLeft)
                    && playedLetters.firstKey().getColumn() - currentColumnAtLeft >= 0) {
                currentColumnAtLeft++;
            }

            // Then, we construct the word
            int startColumn = playedLetters.firstKey().getColumn() - currentColumnAtLeft, endColumn;
            Iterator<SortedMap.Entry<GameController.BoardPosition, LetterInterface>> playedLettersIterator = playedLetters.entrySet().iterator();
            LetterInterface currentLetter;
            List<LetterInterface> horizontalWordLetters = new ArrayList<>();
            do {
                if (null != board.getLetters().get(playedLetters.firstKey().getLine()).get(playedLetters.firstKey().getColumn() - currentColumnAtLeft)) {
                    currentLetter = board.getLetters().get(playedLetters.firstKey().getLine()).get(playedLetters.firstKey().getColumn() - currentColumnAtLeft);
                } else {
                    if (playedLettersIterator.hasNext()) {
                        currentLetter = playedLettersIterator.next().getValue();
                        // Here, check for vertical words formed by new played letters
                    } else {
                        currentLetter = null;
                    }
                }

                if (null == currentLetter) {
                    // When there is no more letter, we can add the horizontal word to the played words, after we checked it really exists
                    PlayedTurnValidityChecker.addWordAfterChecking(dictionary, playedWords, player, horizontalWordLetters, true, playedLetters.firstKey().getLine(), (short) startColumn);
                } else {
                    horizontalWordLetters.add(currentLetter);
                }

                currentColumnAtLeft++;
            } while (null != currentLetter && playedLetters.firstKey().getColumn() - currentColumnAtLeft < BoardInterface.BOARD_SIZE);
        } else {
            // First, add the vertical word done with all the played letters (and eventually the letters already disposed on the board)
            // Then, for each letter, find the horizontal word which may have been formed
        }
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

    protected static boolean playedLetterHasBoardLetterAtRight(BoardInterface board, SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetter) {
        return playedLetter.getKey().getColumn() < BoardInterface.BOARD_SIZE - 1 && null != board.getLetters().get(playedLetter.getKey().getLine()).get(playedLetter.getKey().getColumn() + 1);
    }

    protected static boolean playedLetterHasBoardLetterAbove(BoardInterface board, SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetter) {
        return playedLetter.getKey().getLine() > 0 && null != board.getLetters().get(playedLetter.getKey().getLine() - 1).get(playedLetter.getKey().getColumn());
    }

    protected static boolean playedLetterHasBoardLetterBelow(BoardInterface board, SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetter) {
        return playedLetter.getKey().getLine() < BoardInterface.BOARD_SIZE - 1 && null != board.getLetters().get(playedLetter.getKey().getLine() + 1).get(playedLetter.getKey().getColumn());
    }
}
