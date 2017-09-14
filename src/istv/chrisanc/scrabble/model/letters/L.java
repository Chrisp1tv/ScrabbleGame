package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class L implements LetterInterface {
    @Override
    public char getLetter() {
        return 'L';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
