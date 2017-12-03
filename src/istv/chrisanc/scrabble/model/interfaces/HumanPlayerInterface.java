package istv.chrisanc.scrabble.model.interfaces;

import javafx.beans.property.ReadOnlyIntegerProperty;

/**
 * @author Christopher Anciaux
 */
public interface HumanPlayerInterface extends PlayerInterface {
    int NUMBER_OF_HELPS = 5;

    int getAvailableHelps();

    void decreaseAvailableHelps();

    ReadOnlyIntegerProperty availableHelpsProperty();
}
