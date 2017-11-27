package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.SquareInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class handles all the calculations of the player's score, and manages all score-relative logic.
 *
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 */
public class ScoreManager {
    /**
     * Calculates and returns the score obtained by the given turn
     *
     * @param playedLetters The played letters
     * @param playedWords   The played words
     * @param board         The actual board
     *
     * @return the amount of points given by the played turn
     */
    public static int getTurnScore(Collection<LetterInterface> playedLetters, List<WordInterface> playedWords, BoardInterface board) {
        return ScoreManager.getTurnScore(playedLetters, playedWords, board, true);
    }

    /**
     * Calculates and returns the score obtained by the given turn
     *
     * @param playedLetters     The played letters
     * @param playedWords       The played words
     * @param board             The actual board
     * @param updateMultipliers True if the method must update the board multipliers, false otherwise
     *
     * @return the amount of points given by the played turn
     */
    public static int getTurnScore(Collection<LetterInterface> playedLetters, List<WordInterface> playedWords, BoardInterface board, boolean updateMultipliers) {
        int i, j, turnScore = PlayerInterface.BASE_NUMBER_OF_LETTERS == playedLetters.size() ? 50 : 0;
        Set<SquareInterface> squaresToMakeUsed = new HashSet<>();

        for (i = 0; i < playedWords.size(); i++) {
            int currentWordMultiplier = 1, currentWordScore = 0;
            WordInterface currentWord = playedWords.get(i);

            for (j = 0; j < currentWord.getLetters().size(); j++) {
                LetterInterface currentLetter = currentWord.getLetters().get(j);

                if (currentWord.isHorizontal()) {
                    if (!board.getSquares().get(currentWord.getStartLine()).get(currentWord.getStartColumn() + j).isMultiplierUsed()) {
                        currentWordScore += currentLetter.getValue() * board.getSquares().get(currentWord.getStartLine()).get(currentWord.getStartColumn() + j).getLetterMultiplier();
                        currentWordMultiplier *= board.getSquares().get(currentWord.getStartLine()).get(currentWord.getStartColumn() + j).getWordMultiplier();

                        if (updateMultipliers) {
                            squaresToMakeUsed.add(board.getSquares().get(currentWord.getStartColumn()).get(currentWord.getStartLine() + j));
                        }
                    } else {
                        currentWordScore += currentLetter.getValue();
                    }
                } else {
                    if (!board.getSquares().get(currentWord.getStartLine() + j).get(currentWord.getStartColumn()).isMultiplierUsed()) {
                        currentWordScore += currentLetter.getValue() * board.getSquares().get(currentWord.getStartLine() + j).get(currentWord.getStartColumn()).getLetterMultiplier();
                        currentWordMultiplier *= board.getSquares().get(currentWord.getStartLine() + j).get(currentWord.getStartColumn()).getWordMultiplier();

                        if (updateMultipliers) {
                            squaresToMakeUsed.add(board.getSquares().get(currentWord.getStartLine() + j).get(currentWord.getStartColumn()));
                        }
                    } else {
                        currentWordScore += (currentLetter.getValue());
                    }
                }
            }

            turnScore += currentWordMultiplier * currentWordScore;
        }

        if (updateMultipliers) {
            // Update all squares to make their multipliers used
            squaresToMakeUsed.forEach(SquareInterface::makeMultiplierUsed);
        }

        return turnScore;
    }

    /**
     * Updates the score of all the players when a Scrabble game ended after one player played all his letters and the bag is empty
     *
     * @param players       The players
     * @param currentPlayer The player who hasn't any other letter
     */
    public static void updateScoreOnceBagIsEmptyAndOnePlayerUsedAllHisLetters(List<PlayerInterface> players, PlayerInterface currentPlayer) {
        int pointsSumToGiveToCurrentPlayer = 0;

        for (PlayerInterface player : players) {
            if (currentPlayer != player) {
                // We subtract the values of the letters of all other players to their respective score
                for (LetterInterface letter : player.getLetters()) {
                    player.decreaseScore(letter.getValue());
                    pointsSumToGiveToCurrentPlayer += letter.getValue();
                }
            }

            // We add all the points of the other players to the current player
            currentPlayer.increaseScore(pointsSumToGiveToCurrentPlayer);
        }
    }

    /**
     * Updates the score of all the players when a Scrabble game ended after players skip their turns too many times
     *
     * @param players The players
     */
    public static void updateScoreAfterPlayersSkippedTheirTurnsTooManyTimes(List<PlayerInterface> players) {
        // We subtract the letters values of each player to its total points
        for (PlayerInterface player : players) {
            for (LetterInterface letter : player.getLetters()) {
                player.decreaseScore(letter.getValue());
            }
        }
    }
}
