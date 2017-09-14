package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class Joker implements LetterInterface {
    @Override
    public char getLetter() {
        return ' ';
    }

    @Override
    public byte getValue() {
        return 0;
    }
}
