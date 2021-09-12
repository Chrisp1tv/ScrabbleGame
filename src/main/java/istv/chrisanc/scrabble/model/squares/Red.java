package istv.chrisanc.scrabble.model.squares;

import istv.chrisanc.scrabble.model.Square;

/**
 * @author Christopher Anciaux
 */
public class Red extends Square {
    @Override
    public byte getWordMultiplier() {
        return 3;
    }

    @Override
    public String getInformation() {
        return "squares.information.red";
    }

    @Override
    public String getCssClass() {
        return "red";
    }
}
