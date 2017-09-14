package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class E implements LetterInterface {
    @Override
    public char getLetter() {
        return 'E';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
