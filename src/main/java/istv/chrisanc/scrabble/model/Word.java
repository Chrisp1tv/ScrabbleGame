package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 * @see WordInterface
 */
public class Word implements WordInterface, Serializable {
    /**
     * The player who created the word with his letters
     */
    protected PlayerInterface player;

    /**
     * The letters forming the word
     */
    protected List<LetterInterface> letters;

    /**
     * True if the word is horizontal, false if it is vertical
     */
    protected boolean horizontal;

    /**
     * The line of the first letter of the word
     */
    protected short startLine;

    /**
     * The column of the first letter of the word
     */
    protected short startColumn;

    public Word(PlayerInterface player, Collection<LetterInterface> letters, boolean horizontal, short startLine, short startColumn) {
        this.letters = new ArrayList<>(letters);
        this.horizontal = horizontal;
        this.startLine = startLine;
        this.startColumn = startColumn;
        this.player = player;
    }

    public PlayerInterface getPlayer() {
        return this.player;
    }

    public List<LetterInterface> getLetters() {
        return Collections.unmodifiableList(this.letters);
    }

    public boolean isHorizontal() {
        return this.horizontal;
    }

    public short getStartLine() {
        return this.startLine;
    }

    public short getEndLine() {
        if (this.isHorizontal()) {
            return this.getStartLine();
        } else {
            return (short) (this.getStartLine() + this.letters.size() - 1);
        }
    }

    public short getStartColumn() {
        return this.startColumn;
    }

    public short getEndColumn() {
        if (this.isHorizontal()) {
            return (short) (this.getStartColumn() + (this.letters.size() - 1));
        } else {
            return this.getStartColumn();
        }
    }
}
