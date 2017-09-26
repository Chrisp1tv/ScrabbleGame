package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.GameSaveInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * This class represents a save of a Scrabble game.
 *
 * @author Christopher Anciaux
 */
public class GameSave implements Serializable, GameSaveInterface {
    protected Calendar saveDate;

    protected BoardInterface board;

    protected List<PlayerInterface> players;

    protected BagInterface bag;

    public GameSave(BoardInterface board, List<PlayerInterface> players, BagInterface bag) {
        this.saveDate = Calendar.getInstance();
        this.board = board;
        this.players = players;
        this.bag = bag;
    }

    public Calendar getSaveDate() {
        return saveDate;
    }

    public BoardInterface getBoard() {
        return board;
    }

    public void setBoard(BoardInterface board) {
        this.board = board;
    }

    public List<PlayerInterface> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerInterface> players) {
        this.players = players;
    }

    public BagInterface getBag() {
        return bag;
    }

    public void setBag(BagInterface bag) {
        this.bag = bag;
    }
}
