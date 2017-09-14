package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class Z implements LetterInterface {
    @Override
    public char getLetter() {
        return 'Z';
    }

    @Override
    public byte getValue() {
        return 10;
    }
}
