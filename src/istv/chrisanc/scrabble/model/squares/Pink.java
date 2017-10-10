package istv.chrisanc.scrabble.model.squares;

import istv.chrisanc.scrabble.model.Square;

/**
 * @author Christopher Anciaux
 */
public class Pink extends Square {
    @Override
    public byte getWordMultiplier() {
        return 2;
    }

    @Override
    public String getInformation() {
        return "squares.information.pink";
    }

    @Override
    public String getCssClass() {
        return "pink";
    }
}
