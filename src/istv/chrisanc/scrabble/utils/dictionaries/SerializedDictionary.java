package istv.chrisanc.scrabble.utils.dictionaries;

import istv.chrisanc.scrabble.exceptions.utils.LetterToStringTransformationException;
import istv.chrisanc.scrabble.exceptions.utils.dictionaries.ErrorLoadingDictionaryException;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.letters.Joker;
import istv.chrisanc.scrabble.utils.LetterListToStringTransformer;
import istv.chrisanc.scrabble.utils.LetterToStringTransformer;
import istv.chrisanc.scrabble.utils.interfaces.DictionaryInterface;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.*;

/**
 * This class contains all the logic relative to words searching in the virtual dictionary.
 *
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
    // TODO: Try to improve the both algorithms (using a regex is kind of slow, and Joker logic too)
    public List<String> findWordsHavingLetters(List<LetterInterface> letters) {
        // First, we count how many letters we have of each unique letter in the given letters and store this info in a HashMap
        Set<LetterInterface> uniqueLetters = new HashSet<>(letters);
        Map<LetterInterface, Integer> nbLetters = new HashMap<>();

        for (LetterInterface uniqueLetter : uniqueLetters) {
            nbLetters.put(uniqueLetter, Collections.frequency(letters, uniqueLetter));
        }

        // We check the presence of Jokers in the letters list
        for (LetterInterface letter : letters) {
            if (letter instanceof Joker) {
                // If so, we use the other logic, taking consideration of the presence of a Joker
                return this.findWordsHavingLettersAndJoker(nbLetters, letter);
            }
        }

        // Then let's filter the words by given letters
        List<String> words = new ArrayList<>();
        this.words.stream().filter(word -> word.matches("[" + LetterListToStringTransformer.transform(letters) + "]+")).forEach(words::add);

        // Now, we must check that each letter in each found word isn't present more times than in the given letters
        Iterator<String> wordsIterator = words.iterator();
        while (wordsIterator.hasNext()) {
            String word = wordsIterator.next();

            // If the letter is present more than we have it in the given letters
            for (Map.Entry<LetterInterface, Integer> letter : nbLetters.entrySet()) {
                if ((word.length() - word.replace(letter.getKey().toString(), "").length()) > letter.getValue()) {
                    wordsIterator.remove();
                    break;
                }
            }
        }

        return words;
    }

    protected List<String> findWordsHavingLettersAndJoker(Map<LetterInterface, Integer> nbLetters, LetterInterface joker) {
        List<String> words = new ArrayList<>();
        int nbJokers = nbLetters.get(joker);

        for (String word : this.words) {
            int nbJokersAvailable = nbJokers;
            boolean wordMatches = true;

            // First, we count how many of each letter is present in the word and store it into an array
            Map<Character, Integer> nbLettersOfCurrentWord = new HashMap<>();
            for (int i = 0; i < word.length(); i++) {
                char c = word.charAt(i);
                if (nbLettersOfCurrentWord.containsKey(c)) {
                    int cnt = nbLettersOfCurrentWord.get(c);
                    nbLettersOfCurrentWord.put(c, ++cnt);
                } else {
                    nbLettersOfCurrentWord.put(c, 1);
                }
            }

            // Then, we check if we have enough of each letters, among the given letters, to write the word
            for (Map.Entry<Character, Integer> nbCurrentLetter : nbLettersOfCurrentWord.entrySet()) {
                try {
                    LetterInterface currentLetter = LetterToStringTransformer.reverseTransform(nbCurrentLetter.getKey().toString());

                    if (nbLetters.containsKey(currentLetter)) {
                        // If we get here, the letter has been recognized among the given letters
                        if (nbCurrentLetter.getValue() > nbLetters.get(currentLetter)) {
                            wordMatches = false;
                            break;
                        }
                    } else {
                        // If we get here, the letter hasn't been recognized among the given letters so we use the Joker
                        if (nbCurrentLetter.getValue() > nbJokersAvailable) {
                            wordMatches = false;
                            break;
                        }

                        nbJokersAvailable -= nbCurrentLetter.getValue();
                    }
                } catch (LetterToStringTransformationException e) {
                    wordMatches = false;
                    break;
                }
            }

            if (wordMatches) {
                words.add(word);
            }
        }

        return words;
    }

    private void loadSerializedDictionary() throws ErrorLoadingDictionaryException {
        ObjectInputStream OIS = null;
        try {
            // We find the dictionary's file in the resources folder of the project
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream dictionaryFile = classloader.getResourceAsStream("dictionaries/" + this.getSourceFileName());

            // We open the dictionary and load it in the corresponding object
            OIS = new ObjectInputStream(dictionaryFile);
            this.words = (TreeSet<String>) OIS.readObject();
        } catch (Exception e) {
            throw new ErrorLoadingDictionaryException();
        }
    }

    abstract protected String getSourceFileName();
}
