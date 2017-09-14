package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class S implements LetterInterface {
    @Override
    public char getLetter() {
        return 'S';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
