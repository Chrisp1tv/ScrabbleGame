package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class P extends Letter {
    @Override
    public char getLetter() {
        return 'P';
    }

    @Override
    public byte getValue() {
        return 3;
    }
}
