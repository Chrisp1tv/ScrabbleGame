package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class Joker extends Letter {
    @Override
    public String toString() {
        return " ";
    }

    @Override
    public byte getValue() {
        return 0;
    }
}
