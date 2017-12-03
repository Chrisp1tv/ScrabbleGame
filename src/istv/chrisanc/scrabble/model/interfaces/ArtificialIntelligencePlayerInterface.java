package istv.chrisanc.scrabble.model.interfaces;

/**
 * @author Christopher Anciaux
 */
public interface ArtificialIntelligencePlayerInterface extends PlayerInterface {
    short LEVEL_VERY_EASY = 1;

    short LEVEL_EASY = 2;

    short LEVEL_MEDIUM = 3;

    short LEVEL_HARD = 4;

    short LEVEL_VERY_HARD = 5;

    short getLevel();
}
