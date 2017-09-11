package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * This class represents a player of the Scrabble. The player has {@link LetterInterface} in his rack/hands, a score, a name etc.
 *
 * @author Christopher Anciaux
 */
public class Player implements PlayerInterface {
    /**
     * The name of the player
     */
    protected String name;

    /**
     * The score of the player. The default value is 0, as a player begins the game without any point
     */
    protected short score = 0;

    /**
     * The letters owned by the player in his rack/hands during the game. He can't has more than 7 letters.
     */
    protected List<LetterInterface> letters = new ArrayList<>();

    /**
     * @param name The {@link Player}'s name
     */
    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public short getScore() {
        return score;
    }

    public PlayerInterface increaseScore(short increment) {
        this.score += increment;

        return this;
    }

    /**
     * @return a read-only list of the owned {@link LetterInterface} by the player
     */
    public List<LetterInterface> getLetters() {
        return Collections.unmodifiableList(this.letters);
    }

    /**
     * @param letter the {@link LetterInterface} to be added in the rack of the {@link Player}
     * @return the {@link Player}
     */
    public PlayerInterface addLetter(LetterInterface letter) {
        // TODO

        return this;
    }

    /**
     * @param letter the {@link LetterInterface} to be removed from the rack of the {@link Player}
     * @return the {@link Player}
     */
    public PlayerInterface removeLetter(LetterInterface letter) {
        // TODO

        return this;
    }

    /**
     * @param index the index of the {@link LetterInterface} to be removed from the rack of the {@link Player}
     * @return the {@link Player}
     */
    public PlayerInterface removeLetter(short index) {
        // TODO

        return this;
    }
}
