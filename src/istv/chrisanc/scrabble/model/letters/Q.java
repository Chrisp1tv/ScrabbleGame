package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class Q implements LetterInterface {
    @Override
    public char getLetter() {
        return 'Q';
    }

    @Override
    public byte getValue() {
        return 8;
    }
}
