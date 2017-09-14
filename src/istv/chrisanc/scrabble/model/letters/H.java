package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class H implements LetterInterface {
    @Override
    public char getLetter() {
        return 'H';
    }

    @Override
    public byte getValue() {
        return 4;
    }
}
