package istv.chrisanc.scrabble;

import istv.chrisanc.scrabble.interfaces.BagInterface;
import istv.chrisanc.scrabble.interfaces.BoardInterface;
import istv.chrisanc.scrabble.interfaces.PlayerInterface;

/**
 * This class represents the "main" class of the ScrabbleGame. It starts, manages and ends the game.
 *
 * @author Christopher Anciaux
 */
public class Game {
    protected BoardInterface board = new Board();

    protected PlayerInterface[] players = new PlayerInterface[4];

    protected BagInterface bag = new Bag();

    public static void main(String[] args) {
        // TODO
    }
}
