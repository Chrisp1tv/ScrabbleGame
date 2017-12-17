package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.GameSaveInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;

import java.io.Serializable;
import java.util.List;

/**
 * @author Christopher Anciaux
 * @see GameSaveInterface
 */
public class GameSave implements Serializable, GameSaveInterface {
    /**
     * The language used in this Scrabble game
     */
    protected LanguageInterface language;

    /**
     * The actual state of the board
     */
    protected BoardInterface board;

    /**
     * The players playing this game
     */
    protected List<PlayerInterface> players;

    /**
     * The current player (the one who has to play once the game save has been loaded)
     */
    protected PlayerInterface currentPlayer;

    /**
     * The number of consecutive turns skipped by all the players (without distinction)
     */
    protected short consecutiveTurnsSkipped;

    /**
     * The actual state of the bag
     */
    protected BagInterface bag;

    public GameSave(LanguageInterface language, BoardInterface board, List<PlayerInterface> players, PlayerInterface currentPlayer, BagInterface bag, short consecutiveTurnsSkipped) {
        this.language = language;
        this.board = board;
        this.players = players;
        this.currentPlayer = currentPlayer;
        this.bag = bag;
        this.consecutiveTurnsSkipped = consecutiveTurnsSkipped;
    }

    public LanguageInterface getLanguage() {
        return this.language;
    }

    public void setLanguage(LanguageInterface language) {
        this.language = language;
    }

    public BoardInterface getBoard() {
        return this.board;
    }

    public void setBoard(BoardInterface board) {
        this.board = board;
    }

    public List<PlayerInterface> getPlayers() {
        return this.players;
    }

    public void setPlayers(List<PlayerInterface> players) {
        this.players = players;
    }

    public PlayerInterface getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setCurrentPlayer(PlayerInterface currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public BagInterface getBag() {
        return this.bag;
    }

    public void setBag(BagInterface bag) {
        this.bag = bag;
    }

    public short getConsecutiveTurnsSkipped() {
        return this.consecutiveTurnsSkipped;
    }

    public void setConsecutiveTurnsSkipped(short consecutiveTurnsSkipped) {
        this.consecutiveTurnsSkipped = consecutiveTurnsSkipped;
    }
}
