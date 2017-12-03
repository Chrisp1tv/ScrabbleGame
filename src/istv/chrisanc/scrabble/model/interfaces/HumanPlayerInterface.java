package istv.chrisanc.scrabble.model.interfaces;

import javafx.beans.property.ReadOnlyIntegerProperty;

/**
 * This class represents a human player.
 *
 * @author Christopher Anciaux
 */
public interface HumanPlayerInterface extends PlayerInterface {
    /**
     * The number of available helps at the beginning of a game for a human player
     */
    int NUMBER_OF_HELPS = 5;

    int getAvailableHelps();

    void decreaseAvailableHelps();

    ReadOnlyIntegerProperty availableHelpsProperty();
}
