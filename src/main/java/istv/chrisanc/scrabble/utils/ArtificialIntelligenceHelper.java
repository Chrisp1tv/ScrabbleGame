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
import javafx.concurrent.Task;

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
    /**
     * Plays the turn for the given player
     *
     * @param scrabble The actual game state
     * @param player   The player that should play his turn
     */
    public static void playTurn(Scrabble scrabble, ArtificialIntelligencePlayerInterface player) {
        // First, try to find the possible words
        Task<List<SortedMap<BoardPosition, LetterInterface>>> turnsFindingTask = new Task<List<SortedMap<BoardPosition, LetterInterface>>>() {
            @Override
            protected List<SortedMap<BoardPosition, LetterInterface>> call() throws Exception {
                return PossibleTurnsFinder.findPossibleTurns(scrabble.getLanguage(), scrabble.getBoard(), player);
            }
        };

        turnsFindingTask.setOnSucceeded(event -> {
            List<SortedMap<BoardPosition, LetterInterface>> foundTurns = turnsFindingTask.getValue();

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
        });

        new Thread(turnsFindingTask).start();
    }

    /**
     * Creates a task to find the best turn possible if it exists
     *
     * @param language The game language
     * @param board    The actual board
     * @param player   The player who wants to play the best turn possible
     *
     * @return the task that will return the best turn possible if exists, null otherwise
     */
    public static Task<SortedMap<BoardPosition, LetterInterface>> getBestTurnPossible(LanguageInterface language, BoardInterface board, PlayerInterface player) {
        return new Task<SortedMap<BoardPosition, LetterInterface>>() {
            @Override
            protected SortedMap<BoardPosition, LetterInterface> call() throws Exception {
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
        };
    }

    /**
     * Chooses a turn to be played between all the possible turns, according to the artificial intelligence difficulty and plays it
     *
     * @param scrabble         The actual game state
     * @param player           The played who should play
     * @param sortedFoundTurns The possible turns for the current, sorted by ascending score
     *
     * @throws InvalidPlayedTurnException if the played turn isn't valid
     */
    protected static void playTurn(Scrabble scrabble, ArtificialIntelligencePlayerInterface player, List<SortedMap<BoardPosition, LetterInterface>> sortedFoundTurns) throws InvalidPlayedTurnException {
        SortedMap<BoardPosition, LetterInterface> turnToPlay;

        // First, let's choose the word which will be played
        if (sortedFoundTurns.size() > ArtificialIntelligencePlayerInterface.LEVEL_VERY_HARD) {
            int fromIndex = ArtificialIntelligencePlayerInterface.LEVEL_VERY_EASY == player.getLevel() ? 0 : (player.getLevel() - 1) * sortedFoundTurns.size() / ArtificialIntelligencePlayerInterface.LEVEL_VERY_HARD;
            int toIndex = (player.getLevel() * sortedFoundTurns.size()) / ArtificialIntelligencePlayerInterface.LEVEL_VERY_HARD;

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

    /**
     * Chooses letters to be exchanged with the bag and proceeds to the exchange
     *
     * @param scrabble The actual game state
     * @param player   The played who should exchange his letters
     *
     * @throws NotEnoughLettersException if the bag hasn't enough letters to proceed to the exchange
     * @throws EmptyBagException         if the bag is empty
     */
    protected static void exchangeLettersWithBag(Scrabble scrabble, ArtificialIntelligencePlayerInterface player) throws NotEnoughLettersException, EmptyBagException {
        // First, let's find the letters the player will exchange, randomly
        List<LetterInterface> lettersToExchange = new ArrayList<>(player.getLetters().subList(0, ThreadLocalRandom.current().nextInt(1, player.getLetters().size())));

        // Then, let's proceed to the exchange
        scrabble.exchangeLetters(lettersToExchange);
    }

    /**
     * Sorts the possible turns by their respective hypothetical scores
     *
     * @param dictionary    The dictionary
     * @param board         The actual board
     * @param player        The player who should play this turn
     * @param possibleTurns The possible turns
     *
     * @throws InvalidPlayedTurnException if one of the turn is invalid
     */
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