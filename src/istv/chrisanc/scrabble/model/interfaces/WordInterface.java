package istv.chrisanc.scrabble.model.interfaces;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 * This class represents a word played on the {@link BoardInterface}
 *
 * @author Christopher Anciaux
 */
public interface WordInterface {
    PlayerInterface getPlayer();

    List<LetterInterface> getLetters();

    boolean isHorizontal();

    short getStartLine();

    short getEndLine();

    short getStartColumn();

    short getEndColumn();
}
