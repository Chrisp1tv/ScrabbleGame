package istv.chrisanc.scrabble.interfaces;

import java.util.List;

/**
 * <p>
 * This class represents a player of the Scrabble. The player has {@link LetterInterface} in his rack/hands, a score, a name etc.
 *
 * @author Christopher Anciaux
 */
public interface PlayerInterface {
    String getName();

    short getScore();

    PlayerInterface increaseScore(short increment);

    List<LetterInterface> getLetters();

    PlayerInterface addLetter(LetterInterface letter);

    PlayerInterface removeLetter(LetterInterface letter);

    PlayerInterface removeLetter(short index);
}
