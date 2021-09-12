package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.exceptions.utils.dictionaries.ErrorLoadingDictionaryException;
import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.languages.Global.letters.Joker;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * This class contains all the logic relative to words searching in the virtual dictionary.
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
        Map<LetterInterface, Integer> nbLettersAvailable = LettersCounter.countOccurrencesOfEachLetter(letters);
        int nbJokers = nbLettersAvailable.containsKey(new Joker()) ? nbLettersAvailable.get(new Joker()) : 0;
        Pattern pattern = Pattern.compile("[" + LetterListToStringTransformer.transform(letters) + "]+");

        // If there isn't any joker in the given letters, we use a regular expression to filter the words.
        if (0 == nbJokers) {
            // If there isn't any joker in the given letters, we use a Regex to filter the words.
            for (String word : this.words) {
                Matcher matcher = pattern.matcher(word);

                if (matcher.matches()) {
                    words.add(word);
                }
            }

            // Now, we must check that each letter in each found word isn't present more times than it is in the given letters
            Iterator<String> wordsIterator = words.iterator();
            while (wordsIterator.hasNext()) {
                String word = wordsIterator.next();

                // If so, we delete the word from the found words
                if (this.nbOfEachLetterDoesNotMatches(nbLettersAvailable, word, nbJokers)) {
                    wordsIterator.remove();
                }
            }
        } else {
            words.addAll(this.words.stream().filter(word -> this.nbOfEachLetterWithJokerMatches(nbLettersAvailable, word, nbJokers)).collect(Collectors.toList()));
        }

        return words;
    }

    @Override
    public List<String> findWordsHavingLettersInOrder(List<LetterInterface> letters) {
        List<String> words = new ArrayList<>();
        String word = LetterListToStringTransformer.transform(letters);

        for (String currentWord : this.words) {
            int i = 0, j = 0;
            while (currentWord.length() >= word.length() && i < currentWord.length() && j < word.length()) {
                if (currentWord.charAt(i) == word.charAt(j)) {
                    // Check if the letters are equals
                    i++;
                    j++;
                } else {
                    // If the letters are not equals, we return at the beginning of word and check again
                    j = 0;
                    if (currentWord.charAt(i) == word.charAt(j)) {
                        i++;
                        j++;
                    } else {
                        // If the letters are still not equals, we continue in the word of the dictionary
                        i++;
                    }
                }

                if (j == word.length()) {
                    // If the word is included in the word of the dictionary, it is added to the list
                    words.add(currentWord);
                }
            }
        }

        return words;
    }

    @Override
    public List<String> findWordsStartingWithAndHavingLetters(int minLength, int maxLength, List<LetterInterface> startingLetters, List<LetterInterface> availableLetters) {
        List<String> words = new ArrayList<>();

        // First, we count how many letters we have of each unique letter in the given letters and store this info in a Map
        Map<LetterInterface, Integer> nbLettersAvailable = LettersCounter.countOccurrencesOfEachLetter(availableLetters);
        int nbJokersAvailable = nbLettersAvailable.containsKey(new Joker()) ? nbLettersAvailable.get(new Joker()) : 0;

        // We define the regular expression pattern, which depends on joker's presence
        Pattern pattern = Pattern.compile(LetterListToStringTransformer.transform(startingLetters) + "[" + (nbJokersAvailable > 0 ? "A-Z" : LetterListToStringTransformer.transform(availableLetters)) + "]+");

        for (String word : this.words) {
            Matcher matcher = pattern.matcher(word);

            if (word.length() >= minLength && word.length() <= maxLength && matcher.matches()) {
                words.add(word);
            }
        }

        // Now, we must check that each letter in each found word isn't present more times than it is in the given letters
        Iterator<String> wordsIterator = words.iterator();
        while (wordsIterator.hasNext()) {
            String word = wordsIterator.next();

            if (this.nbOfEachLetterDoesNotMatches(nbLettersAvailable, word.substring(startingLetters.size(), word.length()), nbJokersAvailable)) {
                wordsIterator.remove();
            }
        }

        return words;
    }

    @Override
    public List<String> findWordsEndingWithAndHavingLetters(int minLength, int maxLength, List<LetterInterface> endingLetters, List<LetterInterface> availableLetters) {
        List<String> words = new ArrayList<>();

        // First, we count how many letters we have of each unique letter in the given letters and store this info in a Map
        Map<LetterInterface, Integer> nbLettersAvailable = LettersCounter.countOccurrencesOfEachLetter(availableLetters);
        int nbJokersAvailable = nbLettersAvailable.containsKey(new Joker()) ? nbLettersAvailable.get(new Joker()) : 0;

        // We define the regular expression pattern, which depends on joker's presence
        Pattern pattern = Pattern.compile("[" + (nbJokersAvailable > 0 ? "A-Z" : LetterListToStringTransformer.transform(availableLetters)) + "]+" + LetterListToStringTransformer.transform(endingLetters));

        for (String word : this.words) {
            Matcher matcher = pattern.matcher(word);

            if (word.length() >= minLength && word.length() <= maxLength && matcher.matches()) {
                words.add(word);
            }
        }

        // Now, we must check that each letter in each found word isn't present more times than it is in the given letters
        Iterator<String> wordsIterator = words.iterator();

        while (wordsIterator.hasNext()) {
            String word = wordsIterator.next();

            if (this.nbOfEachLetterDoesNotMatches(nbLettersAvailable, word.substring(0, word.length() - endingLetters.size()), nbJokersAvailable)) {
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
    protected boolean nbOfEachLetterDoesNotMatches(Map<LetterInterface, Integer> nbLettersAvailable, String word, int nbJokersAvailable) {
        if (nbJokersAvailable > 0) {
            return !this.nbOfEachLetterWithJokerMatches(nbLettersAvailable, word, nbJokersAvailable);
        }

        boolean wordMatches = true;

        // First, we count how many of each letter is present in the word and store it into an array
        Map<Character, Integer> nbLettersOfCurrentWord = LettersCounter.countOccurrencesOfEachLetter(word);

        // Then, we check if we have enough of each letter, among the given letters, to write the word
        for (Map.Entry<Character, Integer> nbCurrentLetter : nbLettersOfCurrentWord.entrySet()) {
            LetterInterface currentLetter = LetterToStringTransformer.reverseTransform(nbCurrentLetter.getKey().toString(), this.getLanguage());

            if (nbCurrentLetter.getValue() > nbLettersAvailable.get(currentLetter)) {
                wordMatches = false;
                break;
            }
        }

        return !wordMatches;
    }

    /**
     * @see #nbOfEachLetterDoesNotMatches
     */
    protected boolean nbOfEachLetterWithJokerMatches(Map<LetterInterface, Integer> nbLettersAvailable, String word, int nbJokersAvailable) {
        boolean wordMatches = true;

        // First, we count how many of each letter is present in the word and store it into an array
        Map<Character, Integer> nbLettersOfCurrentWord = LettersCounter.countOccurrencesOfEachLetter(word);

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
            //noinspection unchecked
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