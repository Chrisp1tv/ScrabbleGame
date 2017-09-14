package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class F implements LetterInterface {
    @Override
    public char getLetter() {
        return 'F';
    }

    @Override
    public byte getValue() {
        return 4;
    }
}
