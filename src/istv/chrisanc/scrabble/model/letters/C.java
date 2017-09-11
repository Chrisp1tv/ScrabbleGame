package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class C extends Letter {
    @Override
    public char getLetter() {
        return 'C';
    }

    @Override
    public byte getValue() {
        return 3;
    }
}
