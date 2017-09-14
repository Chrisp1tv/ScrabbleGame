package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class M implements LetterInterface {
    @Override
    public char getLetter() {
        return 'M';
    }

    @Override
    public byte getValue() {
        return 2;
    }
}
