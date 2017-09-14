package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class D implements LetterInterface {
    @Override
    public char getLetter() {
        return 'D';
    }

    @Override
    public byte getValue() {
        return 2;
    }
}
