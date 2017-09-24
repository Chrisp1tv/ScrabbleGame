package istv.chrisanc.scrabble.exceptions.utils.GameSaver;

import istv.chrisanc.scrabble.model.interfaces.GameSaveInterface;

/**
 * This exception should be thrown when a {@link GameSaveInterface} can't be loaded from the file system. For example after a reading problem,
 * a corrupted file etc.
 *
 * @author Christopher Anciaux
 */
public class UnableToLoadSaveException extends Exception {
    public UnableToLoadSaveException() {
        super("exceptions.utils.gameSaver.unableToLoadSaveException");
    }
}
