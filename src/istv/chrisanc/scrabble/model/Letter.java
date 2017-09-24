package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

import java.io.Serializable;

/**
 * <p>
 * This class defines a letter piece used in the Scrabble, represented by the letter itself,
 * and the value of the letter (the number of points it can give to the player).
 *
 * @author Christopher Anciaux
 */
abstract public class Letter implements LetterInterface, Serializable {
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
        return this.getClass().getSimpleName();
    }
}
