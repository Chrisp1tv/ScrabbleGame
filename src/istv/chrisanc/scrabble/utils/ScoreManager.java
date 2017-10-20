package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.SquareInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;

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
    public static int getTurnScore(List<LetterInterface> playedLetters, List<WordInterface> playedWords, BoardInterface board) {
        return ScoreManager.getTurnScore(playedLetters, playedWords, board, true);
    }

    public static int getTurnScore(List<LetterInterface> playedLetters, List<WordInterface> playedWords, BoardInterface board, boolean updateMultipliers) {
        int i, j, turnScore = PlayerInterface.BASE_NUMBER_OF_LETTERS == playedLetters.size() ? 50 : 0;
        Set<SquareInterface> squaresToMakeUsed = new HashSet<>();

        for (i = 0; i < playedWords.size(); i++) {
            int currentWordMultiplier = 1, currentWordScore = 0;
            WordInterface currentWord = playedWords.get(i);

            for (j = 0; j < currentWord.getLetters().size(); j++) {
                LetterInterface currentLetter = currentWord.getLetters().get(j);

                if (currentWord.isHorizontal()) {
                    if (!board.getSquares().get(currentWord.getStartColumn() + j).get(currentWord.getStartLine()).isMultiplierUsed()) {
                        currentWordScore += currentLetter.getValue() * board.getSquares().get(currentWord.getStartColumn() + j).get(currentWord.getStartLine()).getLetterMultiplier();
                        currentWordMultiplier *= board.getSquares().get(currentWord.getStartColumn() + j).get(currentWord.getStartLine()).getWordMultiplier();

                        if (updateMultipliers) {
                            squaresToMakeUsed.add(board.getSquares().get(currentWord.getStartColumn() + j).get(currentWord.getStartLine()));
                        }
                    } else {
                        currentWordScore += (currentLetter.getValue());
                    }
                } else {
                    if (!board.getSquares().get(currentWord.getStartColumn()).get(currentWord.getStartLine() + j).isMultiplierUsed()) {
                        currentWordScore += currentLetter.getValue() * board.getSquares().get(currentWord.getStartColumn()).get(currentWord.getStartLine() + j).getLetterMultiplier();
                        currentWordMultiplier *= board.getSquares().get(currentWord.getStartColumn()).get(currentWord.getStartLine() + j).getWordMultiplier();

                        if (updateMultipliers) {
                            squaresToMakeUsed.add(board.getSquares().get(currentWord.getStartColumn()).get(currentWord.getStartLine() + j));
                        }
                    } else {
                        currentWordScore += currentLetter.getValue();
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
}
