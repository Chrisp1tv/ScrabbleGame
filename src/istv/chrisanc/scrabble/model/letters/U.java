package istv.chrisanc.scrabble.model.letters;

import istv.chrisanc.scrabble.model.Letter;

/**
 * @author Christopher Anciaux
 */
public class U extends Letter {
    @Override
    public char getLetter() {
        return 'U';
    }

    @Override
    public byte getValue() {
        return 1;
    }
}
