package istv.chrisanc.scrabble.model.squares;

import istv.chrisanc.scrabble.model.Square;

/**
 * @author Christopher Anciaux
 */
public class DarkBlue extends Square {
    @Override
    public byte getLetterMultiplier() {
        return 3;
    }

    @Override
    public String getInformation() {
        return "squares.information.darkBlue";
    }

    @Override
    public String getCssClass() {
        return "dark-blue";
    }
}
