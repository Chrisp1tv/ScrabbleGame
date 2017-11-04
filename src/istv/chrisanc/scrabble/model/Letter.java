package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

import java.io.Serializable;

/**
 * @author Christopher Anciaux
 * @see LetterInterface
 */
public class Letter implements LetterInterface, Serializable {
    /**
     * The letter represented by this object
     */
    protected String letter;

    /**
     * The value (the number of points) that can be given by using the letter
     */
    protected byte value;

    public Letter(String letter, byte value) {
        this.letter = letter;
        this.value = value;
    }

    @Override
    public byte getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        Letter otherLetter = (Letter) o;

        return this.getValue() == otherLetter.getValue() && this.toString().equals(otherLetter.toString());
    }

    @Override
    public int hashCode() {
        int result = (int) this.getValue();
        result = 31 * result + this.toString().hashCode();

        return result;
    }

    @Override
    public String toString() {
        return this.letter;
    }
}
