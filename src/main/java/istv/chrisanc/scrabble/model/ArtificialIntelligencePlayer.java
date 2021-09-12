package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.ArtificialIntelligencePlayerInterface;

/**
 * @author Christopher Anciaux
 * @see ArtificialIntelligencePlayerInterface
 */
public class ArtificialIntelligencePlayer extends Player implements ArtificialIntelligencePlayerInterface {
    /**
     * The level of the current Artificial Intelligence
     */
    protected short level;

    public ArtificialIntelligencePlayer(String name, short level) {
        super(name);
        super.initialize();

        this.level = level;
    }

    @Override
    public short getLevel() {
        return this.level;
    }
}
