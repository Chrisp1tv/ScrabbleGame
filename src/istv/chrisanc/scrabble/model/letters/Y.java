package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class Y extends Letter {
    @Override
    public char getLetter() {
        return 'Y';
    }

    @Override
    public byte getValue() {
        return 10;
    }
}
