package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class Z extends Letter {
    @Override
    public char getLetter() {
        return 'Z';
    }

    @Override
    public byte getValue() {
        return 10;
    }
}
