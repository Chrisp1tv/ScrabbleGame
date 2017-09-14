package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class K implements LetterInterface {
    @Override
    public char getLetter() {
        return 'K';
    }

    @Override
    public byte getValue() {
        return 10;
    }
}
