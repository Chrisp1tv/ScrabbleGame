package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class K extends Letter {
    @Override
    public char getLetter() {
        return 'K';
    }

    @Override
    public byte getValue() {
        return 10;
    }
}
