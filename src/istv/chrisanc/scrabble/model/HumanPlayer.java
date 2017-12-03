package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.HumanPlayerInterface;

/**
 * @author Christopher Anciaux
 */
public class HumanPlayer extends Player implements HumanPlayerInterface {
    public HumanPlayer(String name) {
        super(name);

        // TODO: merge with helps system
    }
}
