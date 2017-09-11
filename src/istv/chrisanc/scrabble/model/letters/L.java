package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class L extends Letter {
    @Override
    public char getLetter() {
        return 'L';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
