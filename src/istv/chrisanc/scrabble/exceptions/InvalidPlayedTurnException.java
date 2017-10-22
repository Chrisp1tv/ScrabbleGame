package istv.chrisanc.scrabble.exceptions;

/**
 * @author Christopher Anciaux
 */
public class InvalidPlayedTurnException extends Exception {
    public InvalidPlayedTurnException() {
        super("exceptions.invalidPlayedTurn");
    }

    public InvalidPlayedTurnException(String message) {
        super(message);
    }
}
