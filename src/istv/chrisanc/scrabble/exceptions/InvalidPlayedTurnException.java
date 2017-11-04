package istv.chrisanc.scrabble.exceptions;

/**
 * This exception should be thrown when a player tries to play a turn which doesn't respect the Scrabble rules.
 *
 * @author Christopher Anciaux
 */
public class InvalidPlayedTurnException extends Exception {
    private static final long serialVersionUID = -57913502036566567L;

    public InvalidPlayedTurnException() {
        super("exceptions.invalidPlayedTurn");
    }

    public InvalidPlayedTurnException(String message) {
        super(message);
    }
}
