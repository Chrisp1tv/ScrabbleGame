package istv.chrisanc.scrabble.model.languages;

import istv.chrisanc.scrabble.model.Letter;
import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.languages.Global.letters.Joker;
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
    public List<LetterInterface> getBagLettersDistribution() {
        List<LetterInterface> letters = new ArrayList<>();

        for (int i = 0; i < this.getLettersDistribution().length; i++) {
            for (int numberOfCurrentLetter = 0; numberOfCurrentLetter < this.getLettersDistribution()[i]; numberOfCurrentLetter++) {
                letters.add(new Letter(this.getAuthorizedLetters().get(i), this.getLettersValues()[i]));
            }
        }

        for (int i = 0; i < this.getNumberOfJokers(); i++) {
            letters.add(new Joker());
        }

        return letters;
    }

    @Override
    public LetterInterface getLetter(String letter) {
        if (!this.getAuthorizedLetters().contains(letter)) {
            throw new IllegalArgumentException();
        }

        return new Letter(letter, this.getLettersValues()[this.getAuthorizedLetters().indexOf(letter)]);
    }

    protected abstract List<String> getAuthorizedLetters();

    protected abstract byte[] getLettersValues();

    protected abstract byte[] getLettersDistribution();

    protected abstract byte getNumberOfJokers();
}
