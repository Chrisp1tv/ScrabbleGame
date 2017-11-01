package istv.chrisanc.scrabble.model.interfaces;

import java.util.List;

/**
 * @author Christopher Anciaux
 */
public interface LanguageInterface {
    String getName();

    DictionaryInterface getDictionary();

    List<LetterInterface> getBagLettersDistribution();

    LetterInterface getLetter(String letter);
}
