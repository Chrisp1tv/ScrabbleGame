package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.exceptions.model.Bag.EmptyBagException;
import istv.chrisanc.scrabble.exceptions.model.Bag.NotEnoughLettersException;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.letters.A;
import istv.chrisanc.scrabble.model.letters.B;
import istv.chrisanc.scrabble.model.letters.C;
import istv.chrisanc.scrabble.model.letters.D;
import istv.chrisanc.scrabble.model.letters.E;
import istv.chrisanc.scrabble.model.letters.F;
import istv.chrisanc.scrabble.model.letters.G;
import istv.chrisanc.scrabble.model.letters.H;
import istv.chrisanc.scrabble.model.letters.I;
import istv.chrisanc.scrabble.model.letters.J;
import istv.chrisanc.scrabble.model.letters.Joker;
import istv.chrisanc.scrabble.model.letters.K;
import istv.chrisanc.scrabble.model.letters.L;
import istv.chrisanc.scrabble.model.letters.M;
import istv.chrisanc.scrabble.model.letters.N;
import istv.chrisanc.scrabble.model.letters.O;
import istv.chrisanc.scrabble.model.letters.P;
import istv.chrisanc.scrabble.model.letters.Q;
import istv.chrisanc.scrabble.model.letters.R;
import istv.chrisanc.scrabble.model.letters.S;
import istv.chrisanc.scrabble.model.letters.T;
import istv.chrisanc.scrabble.model.letters.U;
import istv.chrisanc.scrabble.model.letters.V;
import istv.chrisanc.scrabble.model.letters.W;
import istv.chrisanc.scrabble.model.letters.X;
import istv.chrisanc.scrabble.model.letters.Y;
import istv.chrisanc.scrabble.model.letters.Z;
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

    public Bag() {
        this.initialize();
        this.buildLettersList();
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

    protected void buildLettersList() {
        // TODO (possible but not mandatory improvement) : Scrabble letters are language-dependant (values too).
        // Below is the French bag
        this.letters.addAll(
                new A(), new A(), new A(), new A(), new A(), new A(), new A(), new A(), new A(),
                new B(), new B(),
                new C(), new C(),
                new D(), new D(), new D(),
                new E(), new E(), new E(), new E(), new E(), new E(), new E(), new E(), new E(), new E(), new E(), new E(), new E(), new E(), new E(),
                new F(), new F(),
                new G(), new G(),
                new H(), new H(),
                new I(), new I(), new I(), new I(), new I(), new I(), new I(), new I(),
                new J(),
                new K(),
                new L(), new L(), new L(), new L(), new L(),
                new M(), new M(), new M(),
                new N(), new N(), new N(), new N(), new N(), new N(),
                new O(), new O(), new O(), new O(), new O(), new O(),
                new P(), new P(),
                new Q(),
                new R(), new R(), new R(), new R(), new R(), new R(),
                new S(), new S(), new S(), new S(), new S(), new S(),
                new T(), new T(), new T(), new T(), new T(), new T(),
                new U(), new U(), new U(), new U(), new U(), new U(),
                new V(), new V(),
                new W(),
                new X(),
                new Y(),
                new Z(),
                new Joker(), new Joker()
        );

        Collections.shuffle(this.letters);
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
