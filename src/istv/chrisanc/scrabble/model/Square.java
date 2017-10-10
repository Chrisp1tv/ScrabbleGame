package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.SquareInterface;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * <p>
 * This class represents a square on the board, that is to say a position where a {@link PlayerInterface} can put a
 * {@link LetterInterface} on the board.
 *
 * @author Christopher Anciaux
 */
public class Square implements SquareInterface, Serializable {
    /**
     * This boolean states whether the multiplier has already been used or not
     */
    protected BooleanProperty multiplierUsed;

    public Square() {
        this.initialize();
    }

    /**
     * This number represents the multiplier applied to the won points (of the current square only !) when a letter of the word is placed on the actual square
     */
    @Override
    public byte getLetterMultiplier() {
        return 1;
    }

    /**
     * This number represents the multiplier applied to the won points (of the newly formed word) when a letter of the word is placed on the actual square
     */
    @Override
    public byte getWordMultiplier() {
        return 1;
    }

    /**
     * This string represents the key translation of the information displayed about the board, like "Word double" for example
     */
    @Override
    public String getInformation() {
        return null;
    }

    /**
     * This string represents the CSS class used to stylise the square of the board
     */
    @Override
    public String getCssClass() {
        return "square";
    }

    @Override
    public boolean isMultiplierUsed() {
        return multiplierUsed.get();
    }

    @Override
    public SquareInterface makeMultiplierUsed() {
        this.multiplierUsed.set(true);

        return this;
    }

    protected void setMultiplierUsed(boolean multiplierUsed) {
        this.multiplierUsed = new SimpleBooleanProperty();
        this.multiplierUsed.set(multiplierUsed);
    }

    protected void initialize() {
        this.multiplierUsed = new SimpleBooleanProperty(false);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeBoolean(this.multiplierUsed.get());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.initialize();
        this.setMultiplierUsed(objectInputStream.readBoolean());
    }
}
