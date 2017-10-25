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
 * This class manages the checking of played words on the Scrabble game. It checks if played words on a turn are valid, that is
 * to say if these words respect the Scrabble rules.
 * TODO: Some optimization of the code is possible, would be interesting to do it once other tasks have been done
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
        // First, we check that all played letters are linked to other letters, without any "hole" (a square without letter on)
        // between the first and the last played letter
        Iterator<SortedMap.Entry<GameController.BoardPosition, LetterInterface>> playedLettersIterator = playedLetters.entrySet().iterator();
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
        for (SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetter : playedLetters.entrySet()) {
            if (PlayedTurnValidityChecker.hasAdjacentBoardLetter(board, playedLetter)) {
                return true;
            }
        }

        return false;
    }

    protected static boolean hasAdjacentBoardLetter(BoardInterface board, SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetter) {
        return PlayedTurnValidityChecker.playedLetterHasBoardLetterAbove(board, playedLetter) || PlayedTurnValidityChecker.playedLetterHasBoardLetterAtRight(board, playedLetter) || PlayedTurnValidityChecker.playedLetterHasBoardLetterBelow(board, playedLetter) || PlayedTurnValidityChecker.playedLetterHasBoardLetterAtLeft(board, playedLetter);
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

    protected static void addWordAfterChecking(DictionaryInterface dictionary, List<WordInterface> playedWords, PlayerInterface player, List<LetterInterface> wordLetters, boolean horizontal, short startLine, short startColumn) throws InvalidPlayedTurnException {
        if (!dictionary.wordExists(LetterListToStringTransformer.transform(wordLetters))) {
            throw new InvalidPlayedTurnException("exceptions.invalidPlayedTurn.nonExistentWord");
        }

        playedWords.add(new Word(player, wordLetters, horizontal, startLine, startColumn));
    }

    protected static void findAllFormedWords(DictionaryInterface dictionary, BoardInterface board, boolean boardEmpty, SortedMap<GameController.BoardPosition, LetterInterface> playedLetters, List<WordInterface> playedWords, PlayerInterface player, boolean horizontal) throws InvalidPlayedTurnException {
        if (boardEmpty) {
            PlayedTurnValidityChecker.addWordAfterChecking(dictionary, playedWords, player, new ArrayList<>(playedLetters.values()), horizontal, playedLetters.firstKey().getLine(), playedLetters.firstKey().getColumn());

            return;
        }


        Iterator<SortedMap.Entry<GameController.BoardPosition, LetterInterface>> playedLettersIterator = playedLetters.entrySet().iterator();

        if (1 == playedLetters.size()) {
            SortedMap.Entry<GameController.BoardPosition, LetterInterface> currentLetterEntry = playedLettersIterator.next();

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
                    SortedMap.Entry<GameController.BoardPosition, LetterInterface> currentLetterEntry = playedLettersIterator.next();
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
                    SortedMap.Entry<GameController.BoardPosition, LetterInterface> currentLetterEntry = playedLettersIterator.next();
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

    protected static void addVerticalWordFormedFromOnePlayedLetter(DictionaryInterface dictionary, PlayerInterface player, List<WordInterface> playedWords, BoardInterface board, SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetter) throws InvalidPlayedTurnException {
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

    protected static void addHorizontalWordFormedFromOnePlayedLetter(DictionaryInterface dictionary, PlayerInterface player, List<WordInterface> playedWords, BoardInterface board, SortedMap.Entry<GameController.BoardPosition, LetterInterface> playedLetter) throws InvalidPlayedTurnException {
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
}
