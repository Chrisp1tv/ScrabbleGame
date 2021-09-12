package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.HumanPlayerInterface;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author Christopher Anciaux
 * @see HumanPlayerInterface
 */
public class HumanPlayer extends Player implements HumanPlayerInterface {
    /**
     * The available helps of the player
     */
    protected IntegerProperty availableHelps;

    public HumanPlayer(String name) {
        super(name);
        this.initialize();
    }

    public int getAvailableHelps() {
        return this.availableHelps.get();
    }

    protected void setAvailableHelps(int availableHelps) {
        this.availableHelps.set(availableHelps);
    }

    @Override
    public ReadOnlyIntegerProperty availableHelpsProperty() {
        return IntegerProperty.readOnlyIntegerProperty(this.availableHelps);
    }

    @Override
    public void decreaseAvailableHelps() {
        this.setAvailableHelps(this.getAvailableHelps() - 1);
    }

    protected void initialize() {
        super.initialize();
        this.availableHelps = new SimpleIntegerProperty(HumanPlayerInterface.NUMBER_OF_HELPS);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeInt(this.getAvailableHelps());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.setAvailableHelps(objectInputStream.readInt());
    }
}
