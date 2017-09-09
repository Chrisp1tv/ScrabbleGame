package istv.chrisanc.scrabble.Letters;

import istv.chrisanc.scrabble.Letter;

/**
 * @author Christopher Anciaux
 */
public class Joker extends Letter {
    @Override
    public char getLetter() {
        return ' ';
    }

    @Override
    public byte getValue() {
        return 0;
    }
}
