package istv.chrisanc.scrabble.model.squares;

import istv.chrisanc.scrabble.model.Square;

/**
 * This class represents the center square of the Scrabble board, the star.
 *
 * @author Christopher Anciaux
 */
public class Star extends Square {
    @Override
    public byte getWordMultiplier() {
        return 2;
    }

    @Override
    public String getInformation() {
        return "squares.information.star";
    }

    @Override
    public String getCssClass() {
        return "star";
    }
}
