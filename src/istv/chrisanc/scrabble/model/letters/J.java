package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class J implements LetterInterface {
    @Override
    public char getLetter() {
        return 'J';
    }

    @Override
    public byte getValue() {
        return 8;
    }
}
