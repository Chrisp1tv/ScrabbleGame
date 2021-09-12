package istv.chrisanc.scrabble.model.interfaces;

/**
 * This class represents a player which is managed by the computer / Artificial Intelligence of the game.
 *
 * @author Christopher Anciaux
 */
public interface ArtificialIntelligencePlayerInterface extends PlayerInterface {
    /**
     * The level "Very easy" of the Artificial Intelligence
     */
    short LEVEL_VERY_EASY = 1;

    /**
     * The level "Easy" of the Artificial Intelligence
     */
    short LEVEL_EASY = 2;

    /**
     * The level "Medium" of the Artificial Intelligence
     */
    short LEVEL_MEDIUM = 3;

    /**
     * The level "Hard" of the Artificial Intelligence
     */
    short LEVEL_HARD = 4;

    /**
     * The level "Very hard" of the Artificial Intelligence
     */
    short LEVEL_VERY_HARD = 5;

    short getLevel();
}
