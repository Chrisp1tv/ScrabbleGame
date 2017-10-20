package istv.chrisanc.scrabble.model.interfaces;

import istv.chrisanc.scrabble.exceptions.model.Bag.InitializationBagException;

import java.util.List;

/**
 * @author Christopher Anciaux
 */
public interface LanguageInterface {
    DictionaryInterface getDictionary();

    List<LetterInterface> getBagLettersDistribution() throws InitializationBagException;
}
