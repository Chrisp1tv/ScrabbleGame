package istv.chrisanc.scrabble.model.languages.Global.letters;

import istv.chrisanc.scrabble.model.Letter;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class Joker extends Letter {
    /**
     * The letter represented by the joker
     */
    protected LetterInterface representedLetter = null;

    public Joker() {
        super(" ", (byte) 0);
    }

    public LetterInterface getRepresentedLetter() {
        return this.representedLetter;
    }

    public void setRepresentedLetter(LetterInterface representedLetter) {
        this.representedLetter = representedLetter;
    }

    @Override
    public String toString() {
        if (null == this.representedLetter) {
            return this.letter;
        }

        return this.representedLetter.toString();
    }

    @Override
    public byte getValue() {
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || !(o == null || this.getClass() != o.getClass());
    }

    @Override
    public int hashCode() {
        int result = (int) this.getValue();
        result = 31 * result + " ".hashCode();

        return result;
    }
}
