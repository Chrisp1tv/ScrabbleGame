package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class B implements LetterInterface {
    @Override
    public char getLetter() {
        return 'B';
    }

    @Override
    public byte getValue() {
        return 3;
    }
}
