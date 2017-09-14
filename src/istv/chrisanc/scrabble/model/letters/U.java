package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class U implements LetterInterface {
    @Override
    public char getLetter() {
        return 'U';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
