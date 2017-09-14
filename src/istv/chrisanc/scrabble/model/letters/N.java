package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class N implements LetterInterface {
    @Override
    public char getLetter() {
        return 'N';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
