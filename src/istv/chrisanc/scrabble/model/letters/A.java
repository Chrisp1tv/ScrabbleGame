package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class A implements LetterInterface {
    @Override
    public char getLetter() {
        return 'A';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
