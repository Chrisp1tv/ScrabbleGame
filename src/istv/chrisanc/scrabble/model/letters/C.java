package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class C implements LetterInterface {
    @Override
    public char getLetter() {
        return 'C';
    }

    @Override
    public byte getValue() {
        return 3;
    }
}
