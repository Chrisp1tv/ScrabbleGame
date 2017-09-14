package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class Y implements LetterInterface {
    @Override
    public char getLetter() {
        return 'Y';
    }

    @Override
    public byte getValue() {
        return 10;
    }
}
