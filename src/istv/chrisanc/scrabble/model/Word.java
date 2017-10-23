package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * This class represents a word played on the {@link BoardInterface}
 *
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 */
public class Word implements WordInterface, Serializable {
    protected PlayerInterface player;

    protected List<LetterInterface> letters;

    protected boolean horizontal;

    protected short startLine;

    protected short startColumn;

    public Word(PlayerInterface player, Collection<LetterInterface> letters, boolean horizontal, short startLine, short startColumn) {
        this.letters = new ArrayList<>(letters);
        this.horizontal = horizontal;
        this.startLine = startLine;
        this.startColumn = startColumn;
        this.player = player;
    }

    /**
     * @return the player who played the word on the board
     */
    public PlayerInterface getPlayer() {
        return player;
    }

    /**
     * @return the word's letters
     */
    public List<LetterInterface> getLetters() {
        return Collections.unmodifiableList(letters);
    }

    /**
     * @return true if the word was played horizontally, false if it was played vertically
     */
    public boolean isHorizontal() {
        return horizontal;
    }

    /**
     * @return the line's index of the first word's {@link LetterInterface}
     */
    public short getStartLine() {
        return startLine;
    }

    /**
     * @return the line's index of the first word's {@link LetterInterface}
     */
    public short getEndLine() {
        if (isHorizontal()) {
            return getStartLine();
        } else {
            return (short) (getStartLine() + this.letters.size() - 1);
        }
    }

    /**
     * @return the column's index of the first word's {@link LetterInterface}
     */
    public short getStartColumn() {
        return startColumn;
    }

    /**
     * @return the column's index of the last word's {@link LetterInterface}
     */
    public short getEndColumn() {
        if (isHorizontal()) {
            return (short) (getStartColumn() + (this.letters.size() - 1));
        } else {
            return getStartColumn();
        }
    }
}
