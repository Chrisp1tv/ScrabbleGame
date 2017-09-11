package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class H extends Letter {
    @Override
    public char getLetter() {
        return 'H';
    }

    @Override
    public byte getValue() {
        return 4;
    }
}
