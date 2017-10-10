package istv.chrisanc.scrabble.exceptions;

/**
 * @author Christopher Anciaux
 */
public class NonExistentWordException extends Exception {
    protected String nonExistentWord;

    public NonExistentWordException() {
        super("exceptions.nonExistentWord");
    }

    public NonExistentWordException(String nonExistentWord) {
        this();

        this.nonExistentWord = nonExistentWord;
    }

    public String getNonExistentWord() {
        return this.nonExistentWord;
    }
}
