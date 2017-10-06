package istv.chrisanc.scrabble.model;

import istv.chrisanc.scrabble.exceptions.model.Bag.EmptyBagException;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.letters.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * This class represents the bag used in a Scrabble game. It holds in all the pieces ({@link LetterInterface}) at the start
 * of the game, and manages the draw, exchanges of {@link LetterInterface} etc.
 *
 * @author Christopher Anciaux
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

    public LetterInterface drawLetter() throws EmptyBagException {
        // TODO
    }

    public List<LetterInterface> exchangeLetters(List<LetterInterface> lettersToPutBackInTheBag) throws EmptyBagException {
        // TODO
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
