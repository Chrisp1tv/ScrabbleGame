package istv.chrisanc.scrabble.model.squares;

import istv.chrisanc.scrabble.model.Square;

/**
 * @author Christopher Anciaux
 */
public class SkyBlue extends Square {
    @Override
    public byte getLetterMultiplier() {
        return 2;
    }

    @Override
    public String getInformation() {
        return "squares.information.skyBlue";
    }

    @Override
    public String getCssClass() {
        return "sky-blue";
    }
}
