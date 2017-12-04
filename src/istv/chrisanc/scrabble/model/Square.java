package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.SquareInterface;

import java.io.Serializable;

/**
 * @author Christopher Anciaux
 * @see SquareInterface
 */
public class Square implements SquareInterface, Serializable {
    /**
     * This boolean states whether the multiplier has already been used or not
     */
    protected boolean multiplierUsed = false;

    @Override
    public byte getLetterMultiplier() {
        return 1;
    }

    @Override
    public byte getWordMultiplier() {
        return 1;
    }

    @Override
    public String getInformation() {
        return null;
    }

    @Override
    public String getCssClass() {
        return "normal-square";
    }

    @Override
    public void makeMultiplierUsed() {
        this.multiplierUsed = true;
    }

    @Override
    public boolean isMultiplierUnused() {
        return !this.multiplierUsed;
    }
}
