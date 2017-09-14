package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class O implements LetterInterface {
    @Override
    public char getLetter() {
        return 'O';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
