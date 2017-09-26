package istv.chrisanc.scrabble.model.interfaces;

import java.util.Calendar;
import java.util.List;

/**
 * This class represents a save of a Scrabble game.
 *
 * @author Christopher Anciaux
 */
public interface GameSaveInterface {
    Calendar getSaveDate();

    BoardInterface getBoard();

    void setBoard(BoardInterface board);

    List<PlayerInterface> getPlayers();

    void setPlayers(List<PlayerInterface> players);

    BagInterface getBag();

    void setBag(BagInterface bag);
}
