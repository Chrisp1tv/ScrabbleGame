package istv.chrisanc.scrabble.exceptions.utils.GameSaver;

/**
 * This exception should be thrown when a game save can't be saved to a file.
 *
 * @author Christopher Anciaux
 */
public class UnableToWriteSaveException extends Exception {
    private static final long serialVersionUID = -57913516936210867L;

    public UnableToWriteSaveException() {
        super("exceptions.utils.gameSaver.unableToWriteSaveException");
    }
}
