package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

/**
 * @author Christopher Anciaux
 */
public class G implements LetterInterface {
    @Override
    public char getLetter() {
        return 'G';
    }

    @Override
    public byte getValue() {
        return 2;
    }
}
