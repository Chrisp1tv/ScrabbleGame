package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class A extends Letter {
    @Override
    public char getLetter() {
        return 'A';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
