package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.exceptions.model.Bag.EmptyBagException;
import istv.chrisanc.scrabble.exceptions.model.Bag.NotEnoughLettersException;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * <p>
 * This class represents the bag used in a Scrabble game. It holds in all the pieces ({@link LetterInterface}) at the start
 * of the game, and manages the draw, exchanges of {@link LetterInterface} etc.
 *
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 */
public class Bag implements BagInterface, Serializable {
    /**
     * The letters that the bag contain
     */
    protected ObservableList<LetterInterface> letters;

    public Bag(List<LetterInterface> startingLetters) {
        this.initialize();
        this.letters.addAll(startingLetters);
        Collections.shuffle(this.letters);
    }

    @Override
    public LetterInterface drawLetter() throws EmptyBagException {
        if (this.letters.isEmpty()) {
            throw new EmptyBagException();
        }

        Collections.shuffle(this.letters);

        LetterInterface letter = letters.get(new Random().nextInt(this.letters.size()));
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
        this.letters.addAll(lettersToPutBackInTheBag);

        return lettersToGetFromTheBag;
    }

    @Override
    public boolean isEmpty() {
        return this.letters.isEmpty();
    }

    protected void initialize() {
        this.letters = FXCollections.observableArrayList();
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.writeObject(this.letters.stream().collect(Collectors.toList()));
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.initialize();

        List<LetterInterface> letters = (List<LetterInterface>) objectInputStream.readObject();
        this.letters.addAll(letters);
    }
}
