package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class V implements LetterInterface {
    @Override
    public char getLetter() {
        return 'V';
    }

    @Override
    public byte getValue() {
        return 4;
    }
}
