package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class V extends Letter {
    @Override
    public char getLetter() {
        return 'V';
    }

    @Override
    public byte getValue() {
        return 4;
    }
}
