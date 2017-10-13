package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.exceptions.NonExistentWordException;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class manages the checking of a played words on the Scrabble game. It checks if played words on a turn are valid, that is
 * to say if these words respect the Scrabble rules.
 *
 * TODO: merge this class with Checker class once finished
 *
 * @author Christopher Anciaux
 */
abstract public class PlayedWordsValidityManager {
    public static boolean playedWordsAreValid(List<List<LetterInterface>> currentBoardLetters, HashMap<List<Integer>, LetterInterface> playedLetters) {
        // TODO: Check if the positioning of the letters are correct
        // For example (might need supplementary logic) :
        // - If it's the first turn, the player must place a letter on the star square and all his letters must be next to each other
        // - Else, he must place his letters next to other letters (already placed)

        // This method MUSTN'T check if a word exists in the linked dictionary

        return true;
    }

    public static List<WordInterface> findPlayedWords(List<List<LetterInterface>> currentLetters, HashMap<List<Integer>, LetterInterface> playedLetters, PlayerInterface player) throws NonExistentWordException {
        // The player is given, to attribute him the played words
        // TODO
        List<WordInterface> playedWords = new ArrayList<>();

        return playedWords;
    }
}
