package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class O extends Letter {
    @Override
    public char getLetter() {
        return 'O';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
