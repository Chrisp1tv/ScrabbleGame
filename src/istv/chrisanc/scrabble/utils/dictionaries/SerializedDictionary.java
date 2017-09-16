package istv.chrisanc.scrabble.utils.dictionaries;

import istv.chrisanc.scrabble.exceptions.utils.dictionaries.ErrorLoadingDictionaryException;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.utils.interfaces.DictionaryInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * @author Christopher Anciaux
 */
abstract public class SerializedDictionary implements DictionaryInterface {
    /**
     * The Set containing all the words of the dictionary.
     */
    protected NavigableSet<String> words;

    protected SerializedDictionary() throws ErrorLoadingDictionaryException {
        this.loadSerializedDictionary();
    }

    @Override
    public boolean wordExists(String word) {
        return this.words.contains(word);
    }

    @Override
    public List<String> findWordsByLetters(List<LetterInterface> letters) {
        // TODO
    }

    private void loadSerializedDictionary() throws ErrorLoadingDictionaryException {
        ObjectInputStream OIS = null;
        try {
            // We find the dictionary's file in the resources folder of the project
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream dictionaryFile = classloader.getResourceAsStream("/resources/dictionaries/" + this.getSourceFileName());

            // We open the dictionary and load it in the corresponding object
            OIS = new ObjectInputStream(dictionaryFile);
            this.words = (TreeSet<String>) OIS.readObject();
        } catch (Exception e) {
            throw new ErrorLoadingDictionaryException();
        }
    }

    abstract protected String getSourceFileName();
}
