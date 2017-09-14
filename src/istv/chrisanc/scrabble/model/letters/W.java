package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class W implements LetterInterface {
    @Override
    public char getLetter() {
        return 'W';
    }

    @Override
    public byte getValue() {
        return 10;
    }
}
