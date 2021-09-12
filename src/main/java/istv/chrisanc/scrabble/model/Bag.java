package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.exceptions.model.Bag.EmptyBagException;
import istv.chrisanc.scrabble.exceptions.model.Bag.NotEnoughLettersException;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 * @see BagInterface
 */
public class Bag implements BagInterface, Serializable {
    /**
     * The letters of the bag
     */
    protected List<LetterInterface> letters = new ArrayList<>();

    public Bag(List<LetterInterface> startingLetters) {
        this.letters.addAll(startingLetters);
        Collections.shuffle(this.letters);
    }

    @Override
    public LetterInterface drawLetter() throws EmptyBagException {
        if (this.letters.isEmpty()) {
            throw new EmptyBagException();
        }

        Collections.shuffle(this.letters);

        LetterInterface letter = this.letters.get(ThreadLocalRandom.current().nextInt(0, this.letters.size()));
        this.letters.remove(letter);

        return letter;
    }

    @Override
    public List<LetterInterface> exchangeLetters(List<LetterInterface> lettersToPutBackInTheBag) throws EmptyBagException, NotEnoughLettersException {
        // If the bag doesn't contain enough letters to proceed to the exchange, we throw an exception
        if (this.letters.size() < BagInterface.MINIMAL_NUMBER_OF_LETTERS_TO_EXCHANGE) {
            throw new NotEnoughLettersException();
        }

        // We add the letters to be given to the user
        List<LetterInterface> lettersToGetFromTheBag = new ArrayList<>();
        for (int i = 0, lettersToPutBackInTheBagSize = lettersToPutBackInTheBag.size(); i < lettersToPutBackInTheBagSize; i++) {
            lettersToGetFromTheBag.add(this.drawLetter());
        }

        // We put back the letters given by the user, in the bag
        this.putBackLetters(lettersToPutBackInTheBag);

        return lettersToGetFromTheBag;
    }

    @Override
    public void putBackLetters(List<LetterInterface> letters) {
        this.letters.addAll(letters);
    }

    @Override
    public void putBackLetter(LetterInterface letter) {
        this.letters.add(letter);
    }

    @Override
    public boolean isEmpty() {
        return this.letters.isEmpty();
    }
}
