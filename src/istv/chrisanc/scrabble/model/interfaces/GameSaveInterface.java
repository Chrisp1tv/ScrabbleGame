package istv.chrisanc.scrabble.model.interfaces;

import java.util.List;

/**
 * This class represents a save of a Scrabble game.
 *
 * @author Christopher Anciaux
 */
public interface GameSaveInterface {
    short getDictionaryIdentifier();

    void setDictionaryIdentifier(short dictionaryIdentifier);

    BoardInterface getBoard();

    void setBoard(BoardInterface board);

    List<PlayerInterface> getPlayers();

    void setPlayers(List<PlayerInterface> players);

    PlayerInterface getCurrentPlayer();

    void setCurrentPlayer(PlayerInterface currentPlayer);

    BagInterface getBag();

    void setBag(BagInterface bag);
}
