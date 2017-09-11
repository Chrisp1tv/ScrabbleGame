package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class S extends Letter {
    @Override
    public char getLetter() {
        return 'S';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
