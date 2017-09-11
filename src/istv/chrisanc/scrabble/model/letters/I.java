package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class I extends Letter {
    @Override
    public char getLetter() {
        return 'I';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
