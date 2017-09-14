package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class I implements LetterInterface {
    @Override
    public char getLetter() {
        return 'I';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
