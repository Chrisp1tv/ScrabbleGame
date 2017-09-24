package istv.chrisanc.scrabble.model.interfaces;

import java.util.Calendar;

/**
 * This class represents a save of a Scrabble game.
 *
 * @author Christopher Anciaux
 */
public interface GameSaveInterface {
    public Calendar getSaveDate();

    BoardInterface getBoard();

    void setBoard(BoardInterface board);

    PlayerInterface[] getPlayers();

    void setPlayers(PlayerInterface[] players);

    BagInterface getBag();

    void setBag(BagInterface bag);
}
