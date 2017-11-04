package istv.chrisanc.scrabble.exceptions.utils.GameSaver;

/**
 * This exception should be thrown when a game can't be loaded from the file system. For example after a reading problem,
 * a corrupted file etc.
 *
 * @author Christopher Anciaux
 */
public class UnableToLoadSaveException extends Exception {
    private static final long serialVersionUID = -57913511036210867L;

    public UnableToLoadSaveException() {
        super("exceptions.utils.gameSaver.unableToLoadSaveException");
    }
}
