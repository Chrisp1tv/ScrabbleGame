package istv.chrisanc.scrabble.exceptions.utils.GameSaver;

import istv.chrisanc.scrabble.model.interfaces.GameSaveInterface;

/**
 * This exception should be thrown when a {@link GameSaveInterface} can't be saved to a file.
 *
 * @author Christopher Anciaux
 */
public class UnableToWriteSaveException extends Exception {
    public UnableToWriteSaveException() {
        super("exceptions.utils.gameSaver.unableToWriteSaveException");
    }
}
