package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.exceptions.utils.dictionaries.ErrorLoadingDictionaryException;
import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.languages.Global.letters.Joker;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * This class contains all the logic relative to words searching in the virtual language.
 *
 * @author Christopher Anciaux
 */
abstract public class SerializedDictionary implements DictionaryInterface {
    /**
     * The Set containing all the words of the language.
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
    public List<String> findWordsHavingLetters(List<LetterInterface> letters) {
        List<String> words = new ArrayList<>();

        // First, we count how many letters we have of each unique letter in the given letters and store this info in a Map
        Map<LetterInterface, Integer> nbLettersAvailable = countOccurrencesOfEachLetter(letters);
        int nbJokers = nbLettersAvailable.containsKey(new Joker()) ? nbLettersAvailable.get(new Joker()) : 0;

        // If there isn't any joker in the given letters, we use a regular expression to filter the words.
        if (0 == nbJokers) {
            // If there isn't any joker in the given letters, we use a Regex to filter the words.
            this.words.stream().filter(word -> word.matches("[" + LetterListToStringTransformer.transform(letters) + "]+")).forEach(words::add);

            // Now, we must check that each letter in each found word isn't present more times than it is in the given letters
            Iterator<String> wordsIterator = words.iterator();
            while (wordsIterator.hasNext()) {
                String word = wordsIterator.next();

                // If so, we delete the word from the found words
                if (!nbOfEachLetterMatches(nbLettersAvailable, word, nbJokers)) {
                    wordsIterator.remove();
                }
            }
        } else {
            words.addAll(this.words.stream().filter(word -> nbOfEachLetterWithJokerMatches(nbLettersAvailable, word, nbJokers)).collect(Collectors.toList()));
        }

        return words;
    }

    @Override
    public List<String> findWordsStartingWithAndHavingLetters(int minLength, int maxLength, List<LetterInterface> startingLetters, List<LetterInterface> availableLetters) {
        List<String> words = new ArrayList<>();

        // First, we count how many letters we have of each unique letter in the given letters and store this info in a Map
        Map<LetterInterface, Integer> nbLettersAvailable = countOccurrencesOfEachLetter(availableLetters);
        int nbJokersAvailable = nbLettersAvailable.containsKey(new Joker()) ? nbLettersAvailable.get(new Joker()) : 0;

        // We define the regular expression pattern, which depends on joker's presence
        String pattern = LetterListToStringTransformer.transform(startingLetters) + "[" + (nbJokersAvailable > 0 ? "A-Z" : LetterListToStringTransformer.transform(availableLetters)) + "]+";

        words.addAll(this.words.stream().filter(word -> word.length() >= minLength && word.length() <= maxLength && word.matches(pattern)).collect(Collectors.toList()));

        // Now, we must check that each letter in each found word isn't present more times than it is in the given letters
        Iterator<String> wordsIterator = words.iterator();
        while (wordsIterator.hasNext()) {
            String word = wordsIterator.next();

            if (!nbOfEachLetterMatches(nbLettersAvailable, word.substring(startingLetters.size(), word.length()), nbJokersAvailable)) {
                wordsIterator.remove();
            }
        }

        return words;
    }

    @Override
    public List<String> findWordsEndingWithAndHavingLetters(int minLength, int maxLength, List<LetterInterface> endingLetters, List<LetterInterface> availableLetters) {
        List<String> words = new ArrayList<>();

        // First, we count how many letters we have of each unique letter in the given letters and store this info in a Map
        Map<LetterInterface, Integer> nbLettersAvailable = countOccurrencesOfEachLetter(availableLetters);
        int nbJokersAvailable = nbLettersAvailable.containsKey(new Joker()) ? nbLettersAvailable.get(new Joker()) : 0;

        // We define the regular expression pattern, which depends on joker's presence
        String pattern = "[" + (nbJokersAvailable > 0 ? "A-Z" : LetterListToStringTransformer.transform(availableLetters)) + "]+" + LetterListToStringTransformer.transform(endingLetters);

        words.addAll(this.words.stream().filter(word -> word.length() >= minLength && word.length() <= maxLength && word.matches(pattern)).collect(Collectors.toList()));

        // Now, we must check that each letter in each found word isn't present more times than it is in the given letters
        Iterator<String> wordsIterator = words.iterator();

        while (wordsIterator.hasNext()) {
            String word = wordsIterator.next();

            if (!nbOfEachLetterMatches(nbLettersAvailable, word.substring(0, word.length() - endingLetters.size()), nbJokersAvailable)) {
                wordsIterator.remove();
            }
        }

        return words;
    }

    /**
     * @param nbLettersAvailable The number of each available letter to write the word
     * @param word               The word that we try to write with the given letters
     * @param nbJokersAvailable  The number of available jokers in the given letters
     *
     * @return true if it's possible to write the word with the given letters, false otherwise
     */
    protected boolean nbOfEachLetterMatches(Map<LetterInterface, Integer> nbLettersAvailable, String word, int nbJokersAvailable) {
        if (nbJokersAvailable > 0) {
            return nbOfEachLetterWithJokerMatches(nbLettersAvailable, word, nbJokersAvailable);
        }

        boolean wordMatches = true;

        // First, we count how many of each letter is present in the word and store it into an array
        Map<Character, Integer> nbLettersOfCurrentWord = countOccurrencesOfEachLetter(word);

        // Then, we check if we have enough of each letter, among the given letters, to write the word
        for (Map.Entry<Character, Integer> nbCurrentLetter : nbLettersOfCurrentWord.entrySet()) {
            LetterInterface currentLetter = LetterToStringTransformer.reverseTransform(nbCurrentLetter.getKey().toString(), this.getLanguage());

            if (nbCurrentLetter.getValue() > nbLettersAvailable.get(currentLetter)) {
                wordMatches = false;
                break;
            }
        }

        return wordMatches;
    }

    /**
     * @see #nbOfEachLetterMatches
     */
    protected boolean nbOfEachLetterWithJokerMatches(Map<LetterInterface, Integer> nbLettersAvailable, String word, int nbJokersAvailable) {
        boolean wordMatches = true;

        // First, we count how many of each letter is present in the word and store it into an array
        Map<Character, Integer> nbLettersOfCurrentWord = countOccurrencesOfEachLetter(word);

        // Then, we check if we have enough of each letter, among the given letters, to write the word
        for (Map.Entry<Character, Integer> nbCurrentLetter : nbLettersOfCurrentWord.entrySet()) {
            LetterInterface currentLetter = LetterToStringTransformer.reverseTransform(nbCurrentLetter.getKey().toString(), this.getLanguage());

            if (nbLettersAvailable.containsKey(currentLetter)) {
                // If we get here, the letter has been recognized among the given letters
                if (nbCurrentLetter.getValue() > nbLettersAvailable.get(currentLetter)) {
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
        }

        return wordMatches;
    }

    /**
     * @param word The word for which we want to count occurrences of each letter
     *
     * @return a map having for key the letter of the word, and for value the number of occurrences of the letter
     */
    protected Map<Character, Integer> countOccurrencesOfEachLetter(String word) {
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

        return nbLettersOfCurrentWord;
    }

    /**
     * @param letters The given letters we have to count
     *
     * @return a map having for key the {@link LetterInterface}, and for value the number of occurrences of the {@link LetterInterface}
     */
    protected Map<LetterInterface, Integer> countOccurrencesOfEachLetter(List<LetterInterface> letters) {
        Set<LetterInterface> uniqueLetters = new HashSet<>(letters);
        Map<LetterInterface, Integer> nbLetters = new HashMap<>();

        for (LetterInterface uniqueLetter : uniqueLetters) {
            nbLetters.put(uniqueLetter, Collections.frequency(letters, uniqueLetter));
        }

        return nbLetters;
    }

    /**
     * Loads the language from the included file resource.
     *
     * @throws ErrorLoadingDictionaryException if a problem occurred during the language's loading
     */
    private void loadSerializedDictionary() throws ErrorLoadingDictionaryException {
        try {
            // We find the language's file in the resources folder of the project
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream dictionaryFile = classloader.getResourceAsStream("dictionaries/" + this.getSourceFileName());

            // We open the language and load it in the corresponding object
            ObjectInputStream OIS = new ObjectInputStream(dictionaryFile);
            this.words = (TreeSet<String>) OIS.readObject();
        } catch (Exception e) {
            throw new ErrorLoadingDictionaryException();
        }
    }

    /**
     * @return the language's file name
     */
    abstract protected String getSourceFileName();

    /**
     * @return the language class associated with the language
     */
    abstract protected LanguageInterface getLanguage();
}