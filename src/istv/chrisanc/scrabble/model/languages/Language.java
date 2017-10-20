package istv.chrisanc.scrabble.model.languages;

import istv.chrisanc.scrabble.exceptions.model.Bag.InitializationBagException;
import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.utils.DictionaryFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Christopher Anciaux
 */
public abstract class Language implements LanguageInterface, Serializable {
    protected Class<? extends DictionaryInterface> dictionaryClass;

    protected Language(Class<? extends DictionaryInterface> dictionaryClass) {
        this.dictionaryClass = dictionaryClass;
    }

    @Override
    public DictionaryInterface getDictionary() {
        return DictionaryFactory.getDictionary(this.dictionaryClass);
    }

    @Override
    public List<LetterInterface> getBagLettersDistribution() throws InitializationBagException {
        try {
            List<LetterInterface> letters = new ArrayList<>();

            for (int i = 0; i < this.getLettersDistribution().length; i++) {
                for (int numberOfCurrentLetter = 0; numberOfCurrentLetter < this.getLettersDistribution()[i]; numberOfCurrentLetter++) {
                    letters.add(this.getLetters().get(i).newInstance());
                }
            }

            return letters;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InitializationBagException();
        }
    }

    protected abstract List<Class<? extends LetterInterface>> getLetters();

    protected abstract int[] getLettersDistribution();
}
