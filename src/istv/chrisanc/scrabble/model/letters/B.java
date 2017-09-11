package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class B extends Letter {
    @Override
    public char getLetter() {
        return 'B';
    }

    @Override
    public byte getValue() {
        return 3;
    }
}
