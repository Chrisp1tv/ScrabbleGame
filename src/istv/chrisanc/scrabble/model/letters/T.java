package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class T implements LetterInterface {
    @Override
    public char getLetter() {
        return 'T';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
