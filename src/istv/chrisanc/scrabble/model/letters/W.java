package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class W extends Letter {
    @Override
    public char getLetter() {
        return 'W';
    }

    @Override
    public byte getValue() {
        return 10;
    }
}
