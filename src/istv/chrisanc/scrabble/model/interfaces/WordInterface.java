package istv.chrisanc.scrabble.model.interfaces;

import java.util.List;

/**
 * This class represents a word played on the {@link BoardInterface}
 *
 * @author Christopher Anciaux
 */
public interface WordInterface {
    PlayerInterface getPlayer();

    List<LetterInterface> getLetters();

    /**
     * @return true if the word is horizontal, false if the word is vertical
     */
    boolean isHorizontal();

    short getStartLine();

    short getEndLine();

    short getStartColumn();

    short getEndColumn();
}
