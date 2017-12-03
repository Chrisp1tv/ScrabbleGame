package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.Scrabble;
import istv.chrisanc.scrabble.exceptions.InvalidPlayedTurnException;
import istv.chrisanc.scrabble.exceptions.model.Bag.EmptyBagException;
import istv.chrisanc.scrabble.exceptions.model.Bag.NotEnoughLettersException;
import istv.chrisanc.scrabble.model.BoardPosition;
import istv.chrisanc.scrabble.model.interfaces.ArtificialIntelligencePlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class contains the behaviour of the Artificial Intelligence.
 *
 * @author Christopher Anciaux
 * @author Julien Basquin
 */
public abstract class ArtificialIntelligenceHelper {
    public static void playTurn(Scrabble scrabble, ArtificialIntelligencePlayerInterface player) {
        // First, try to find the possible words
        List<SortedMap<BoardPosition, LetterInterface>> foundTurns = PossibleTurnsFinder.findPossibleTurns(scrabble.getLanguage(), scrabble.getBoard(), player);

        if (!foundTurns.isEmpty()) {
            try {
                ArtificialIntelligenceHelper.sortTurnsByScore(scrabble.getLanguage().getDictionary(), scrabble.getBoard(), player, foundTurns);
                ArtificialIntelligenceHelper.playTurn(scrabble, player, foundTurns);

                return;
            } catch (InvalidPlayedTurnException ignored) {
            }
        }

        try {
            ArtificialIntelligenceHelper.exchangeLettersWithBag(scrabble, player);
        } catch (NotEnoughLettersException | EmptyBagException e) {
            scrabble.skipTurn();
        }
    }

    public static SortedMap<BoardPosition, LetterInterface> getBestTurnPossible(LanguageInterface language, BoardInterface board, PlayerInterface player) {
        List<SortedMap<BoardPosition, LetterInterface>> foundTurns = PossibleTurnsFinder.findPossibleTurns(language, board, player);

        if (foundTurns.isEmpty()) {
            return null;
        }

        try {
            ArtificialIntelligenceHelper.sortTurnsByScore(language.getDictionary(), board, player, foundTurns);

            return foundTurns.get(foundTurns.size() - 1);
        } catch (InvalidPlayedTurnException e) {
            return null;
        }
    }

    protected static void playTurn(Scrabble scrabble, ArtificialIntelligencePlayerInterface player, List<SortedMap<BoardPosition, LetterInterface>> sortedFoundTurns) throws InvalidPlayedTurnException {
        SortedMap<BoardPosition, LetterInterface> turnToPlay;

        // First, let's choose the word which will be played
        if (sortedFoundTurns.size() > ArtificialIntelligencePlayerInterface.LEVEL_VERY_HARD) {
            int fromIndex = ArtificialIntelligencePlayerInterface.LEVEL_VERY_EASY == player.getLevel() ? 0 : (player.getLevel() - 1) / ArtificialIntelligencePlayerInterface.LEVEL_VERY_HARD * sortedFoundTurns.size();
            int toIndex = 1 / player.getLevel() * sortedFoundTurns.size();

            List<SortedMap<BoardPosition, LetterInterface>> foundTurnsForPlayerLevel = sortedFoundTurns.subList(fromIndex, toIndex);
            turnToPlay = foundTurnsForPlayerLevel.get(ThreadLocalRandom.current().nextInt(0, foundTurnsForPlayerLevel.size()));
        } else {
            switch (player.getLevel()) {
                case ArtificialIntelligencePlayerInterface.LEVEL_VERY_EASY:
                case ArtificialIntelligencePlayerInterface.LEVEL_EASY:
                case ArtificialIntelligencePlayerInterface.LEVEL_MEDIUM:
                    turnToPlay = sortedFoundTurns.get(0);
                    break;
                case ArtificialIntelligencePlayerInterface.LEVEL_HARD:
                case ArtificialIntelligencePlayerInterface.LEVEL_VERY_HARD:
                default:
                    turnToPlay = sortedFoundTurns.get(sortedFoundTurns.size() - 1);
                    break;
            }
        }

        // Then, play it
        scrabble.playLetters(turnToPlay);
    }

    protected static void exchangeLettersWithBag(Scrabble scrabble, ArtificialIntelligencePlayerInterface player) throws NotEnoughLettersException, EmptyBagException {
        // First, let's find the letters the player will exchange, randomly
        List<LetterInterface> lettersToExchange = new ArrayList<>(player.getLetters().subList(0, ThreadLocalRandom.current().nextInt(1, player.getLetters().size())));

        // Then, let's proceed to the exchange
        scrabble.exchangeLetters(lettersToExchange);
    }

    protected static void sortTurnsByScore(DictionaryInterface dictionary, BoardInterface board, PlayerInterface player, List<SortedMap<BoardPosition, LetterInterface>> possibleTurns) throws InvalidPlayedTurnException {
        // First, we simulate the playing of each possible turn to find the words resulting of each of these possible turns
        // The key is the played turn, and the value is the score we obtained by playing it
        Map<SortedMap<BoardPosition, LetterInterface>, Integer> scores = new HashMap<>();

        for (SortedMap<BoardPosition, LetterInterface> possibleTurn : possibleTurns) {
            List<WordInterface> playedWords = PlayedTurnValidityChecker.findPlayedWords(dictionary, board, possibleTurn, player);

            scores.put(possibleTurn, ScoreManager.getTurnScore(possibleTurn.values(), playedWords, board, false));
        }

        possibleTurns.sort((o1, o2) -> scores.get(o1).compareTo(scores.get(o2)));
    }
}