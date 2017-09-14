package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class P implements LetterInterface {
    @Override
    public char getLetter() {
        return 'P';
    }

    @Override
    public byte getValue() {
        return 3;
    }
}
