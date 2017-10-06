package istv.chrisanc.scrabble.model.interfaces;

import javafx.scene.input.DataFormat;

/**
 * <p>
 * This class defines a letter piece used in the Scrabble, represented by the letter itself,
 * and the value of the letter (the number of points it can give to the player).
 *
 * @author Christopher Anciaux
 */
public interface LetterInterface {
    DataFormat DATA_FORMAT = new DataFormat("LetterInterface");

    /**
     * @return the value, the amount of points the {@link LetterInterface} can give to the player
     */
    byte getValue();
}
