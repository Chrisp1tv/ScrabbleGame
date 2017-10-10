package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.GameSaveInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a save of a Scrabble game.
 *
 * @author Christopher Anciaux
 */
public class GameSave implements Serializable, GameSaveInterface {
    protected short dictionaryIdentifier;

    protected BoardInterface board;

    protected List<PlayerInterface> players;

    protected PlayerInterface currentPlayer;

    protected BagInterface bag;

    public GameSave(short dictionaryIdentifier, BoardInterface board, List<PlayerInterface> players, PlayerInterface currentPlayer, BagInterface bag) {
        this.dictionaryIdentifier = dictionaryIdentifier;
        this.board = board;
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.bag = bag;
    }

    public short getDictionaryIdentifier() {
        return dictionaryIdentifier;
    }

    public void setDictionaryIdentifier(short dictionaryIdentifier) {
        this.dictionaryIdentifier = dictionaryIdentifier;
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

    public PlayerInterface getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerInterface currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public BagInterface getBag() {
        return bag;
    }

    public void setBag(BagInterface bag) {
        this.bag = bag;
    }
}
