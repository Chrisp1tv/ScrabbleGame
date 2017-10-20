package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.GameSaveInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;

import java.io.Serializable;
import java.util.List;

/**
 * This class represents a save of a Scrabble game.
 *
 * @author Christopher Anciaux
 */
public class GameSave implements Serializable, GameSaveInterface {
    protected LanguageInterface language;

    protected BoardInterface board;

    protected List<PlayerInterface> players;

    protected PlayerInterface currentPlayer;

    protected BagInterface bag;

    public GameSave(LanguageInterface language, BoardInterface board, List<PlayerInterface> players, PlayerInterface currentPlayer, BagInterface bag) {
        this.language = language;
        this.board = board;
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.bag = bag;
    }

    public LanguageInterface getLanguage() {
        return this.language;
    }

    public void setLanguage(LanguageInterface language) {
        this.language = language;
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
