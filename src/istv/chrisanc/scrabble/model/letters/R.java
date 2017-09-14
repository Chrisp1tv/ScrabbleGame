package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class R implements LetterInterface {
    @Override
    public char getLetter() {
        return 'R';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
