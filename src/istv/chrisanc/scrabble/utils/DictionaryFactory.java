package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.exceptions.utils.dictionaries.ErrorLoadingDictionaryException;
import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Christopher Anciaux
 */
abstract public class DictionaryFactory {
    public static DictionaryInterface getDictionary(Class<? extends DictionaryInterface> dictionaryClass) throws ErrorLoadingDictionaryException {
        try {
            Method getInstanceMethod = dictionaryClass.getMethod("getInstance");

            return (DictionaryInterface) getInstanceMethod.invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new ErrorLoadingDictionaryException();
        }
    }
}