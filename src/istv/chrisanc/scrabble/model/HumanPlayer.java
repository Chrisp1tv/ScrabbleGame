package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.HumanPlayerInterface;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * @author Christopher Anciaux
 */
public class HumanPlayer extends Player implements HumanPlayerInterface {
    /**
     * The available helps of the player
     */
    protected IntegerProperty availableHelps;

    public HumanPlayer(String name) {
        super(name);
        this.availableHelps = new SimpleIntegerProperty(HumanPlayerInterface.NUMBER_OF_HELPS);
    }

    public int getAvailableHelps() {
        return availableHelps.get();
    }

    @Override
    public ReadOnlyIntegerProperty availableHelpsProperty() {
        return IntegerProperty.readOnlyIntegerProperty(this.availableHelps);
    }

    @Override
    public void decreaseAvailableHelps() {
        this.setAvailableHelps(this.getAvailableHelps() - 1);
    }

    protected void setAvailableHelps(int availableHelps) {
        this.availableHelps.set(availableHelps);
    }
}
