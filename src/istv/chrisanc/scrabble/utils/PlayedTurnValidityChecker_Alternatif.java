package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.model.Word;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;
import istv.chrisanc.scrabble.model.interfaces.DictionaryInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * This Class checks if a word can be played on the board or not
 *
 * @author Julien Basquin
 * @author Anthony Delétré
 */
public class PlayedTurnValidityChecker_Alternatif {

	public static boolean isPlayable(BoardInterface board, WordInterface word, DictionaryInterface dictionary) {
        /*
        We may not keep this method as most of its verifications are already done in PlayedTurnValidityChecker.playedWordsAreValid()
*/
		//Size of the board
		short size = BoardInterface.BOARD_SIZE;

		//Letters of the word
		List<LetterInterface> wordLetters = word.getLetters();

		//Convert letters of the word to String
		String wordToString = LetterListToStringTransformer.transform(word.getLetters());

		//Check if the word exists
		if(!(dictionary.wordExists(wordToString)))
			return false;

		//Check if the word as already been played
		if(board.getPlayedWords().contains(word))
			return false;

		//Check if the word is on the board
		if(word.getStartLine() < 0 || word.getStartLine() >= size)
			return false;
		if(word.getStartColumn() < 0 || word.getStartColumn() >= size)
			return false;
		if(word.getEndLine() < 0 || word.getEndLine() >= size)
			return false;
		if(word.getEndColumn() < 0 || word.getEndColumn() >= size)
			return false;

		//Check if letters of the word are on an empty square or on a corresponding letter
		for(int k = 0; k < wordLetters.size(); k++)
		{
			//If the word is horizontal
			if(word.isHorizontal()){
				if(!(board.getLetters().get(word.getStartLine()).get(word.getStartColumn()+k) == null ||
						board.getLetters().get(word.getStartLine()).get(word.getStartColumn()+k).equals(wordLetters.get(k)))){
					return false;
				}
			}

			//If the word is vertical
			else
				if(!(board.getLetters().get(word.getStartLine()+k).get(word.getStartColumn()) == null || 
						board.getLetters().get(word.getStartLine()+k).get(word.getStartColumn()).equals(wordLetters.get(k))))
					return false;
		}

		//Check if letters of the word are attached to an other word and check if this combination means something
		if(word.isHorizontal())
		{
			// First letter			
			if(word.getStartColumn()-1 > 0 && 
					!(board.getLetters().get(word.getStartLine()).get(word.getStartColumn()-1) == null)){

				//verification of the beginning letters to find the complete word
				int w=1;
				while(!(board.getLetters().get(word.getStartLine()).get(word.getStartColumn()-w) == null)){
					w = w+1;
				}

				//Recovery of all the letters
				while(board.getLetters().get(word.getStartColumn()-w) != board.getLetters().get(word.getEndColumn())){

					wordLetters.add(board.getLetters().get(word.getStartLine()).get(word.getStartColumn() - w));
					w = w-1;
				}

				String findWord = LetterListToStringTransformer.transform(wordLetters);
				return (!dictionary.wordExists(findWord));
			}

			// Last letter
			if(word.getEndColumn()+1 < size && 
					!(board.getLetters().get(word.getEndLine()).get(word.getEndColumn()+1) == null)){

				//verification of the beginning letters to find the complete word
				int w=1;
				while(!(board.getLetters().get(word.getEndLine()).get(word.getEndColumn()+w) == null)){
					w = w+1;
				}

				//Recovery of all the letters
				int z=0;
				while(board.getLetters().get(word.getStartColumn()+z) != board.getLetters().get(word.getEndColumn()+w)){

					wordLetters.add(board.getLetters().get(word.getStartLine()).get(word.getStartColumn()+z));
					z = z+1;
				}

				String findWord = LetterListToStringTransformer.transform(wordLetters);
				return (!dictionary.wordExists(findWord));
			}

			//The letters between
			for(int k = 0; k < wordLetters.size(); k++)
			{

				if(!(board.getLetters().get(word.getStartLine()-1).get(word.getStartColumn()+k) == null || 
						board.getLetters().get(word.getStartLine()+1).get(word.getStartColumn()+k) == null)){

					int w = 1;
					//verification above the letter to find the beginning of the word
					while(!(board.getLetters().get(word.getStartLine()-w).get(word.getStartColumn()) == null)){
						w = w+1;
					}


					int z = 1;
					//verification underneath the letter to find the end of the word
					while(!(board.getLetters().get(word.getStartLine()+z).get(word.getStartColumn()) == null)){
						z = z+1;
					}

					//Recovery of all the letters
					while(board.getLetters().get(word.getStartLine() - w) != board.getLetters().get(word.getStartLine() + z)){

						wordLetters.add(board.getLetters().get(word.getStartLine() - w).get(word.getStartColumn()));
						w = w-1;
					}

					String findWord = LetterListToStringTransformer.transform(wordLetters);
					return (!dictionary.wordExists(findWord));
				}
			}

		}

		// Verification of adjacent squares for a vertical Word

		else{

			// First letter
			if(word.getStartLine()-1 > 0 && 
					!(board.getLetters().get(word.getStartLine()-1).get(word.getStartColumn()) == null)){

				//verification of the beginning letters to find the complete word
				int w=1;
				while(!(board.getLetters().get(word.getStartLine()-w).get(word.getStartColumn()) == null)){
					w = w+1;
				}

				//Recovery of all the letters
				while(board.getLetters().get(word.getStartLine()-w) != board.getLetters().get(word.getEndLine())){

					wordLetters.add(board.getLetters().get(word.getStartLine()-w).get(word.getStartColumn()));
					w = w-1;
				}

				String findWord = LetterListToStringTransformer.transform(wordLetters);
				return (!dictionary.wordExists(findWord));
			}

			// Last letter
			if(word.getEndLine()+1 < size && 
					!(board.getLetters().get(word.getEndLine()+1).get(word.getStartColumn()) == null)){

				//verification of the beginning letters to find the complete word
				int w=1;
				while(!(board.getLetters().get(word.getEndLine()+w).get(word.getStartColumn()) == null)){
					w = w+1;
				}

				//Recovery of all the letters
				int z=0;
				while(board.getLetters().get(word.getStartLine()+z) != board.getLetters().get(word.getEndLine() + w)){

					wordLetters.add(board.getLetters().get(word.getStartLine()+z).get(word.getStartColumn()));
					z = z+1;
				}

				String findWord = LetterListToStringTransformer.transform(wordLetters);
				return (!dictionary.wordExists(findWord));
			}

			for(int k = 0; k < wordLetters.size(); k++)
			{
				if(!(board.getLetters().get(word.getStartLine()+k).get(word.getStartColumn()-1) == null || 
						board.getLetters().get(word.getStartLine()+k).get(word.getStartColumn()+1) == null)){

					int w = 1;
					//verification above the letter to find the beginning of the word
					while(!(board.getLetters().get(word.getStartColumn()-w).get(word.getStartLine()) == null)){
						w = w+1;
					}


					int z = 1;
					//verification underneath the letter to find the end of the word
					while(!(board.getLetters().get(word.getStartColumn()+z).get(word.getStartLine()) == null)){
						z = z+1;
					}

					//Recovery of all the letters
					while(board.getLetters().get(word.getStartColumn() - w) != board.getLetters().get(word.getStartColumn() + z)){

						wordLetters.add(board.getLetters().get(word.getStartColumn() - w).get(word.getStartLine()));
						w = w-1;
					}

					String findWord = LetterListToStringTransformer.transform(wordLetters);
					return (!dictionary.wordExists(findWord));
				}
			}			
		}
        return true;
    }

    /**
     * Method to return the adjacent word list of the word played
     *
     * @param board
     * @param word
     * @param dictionary
     *
     * @return word list
     */
    public static List<WordInterface> AdjacentWord(BoardInterface board, WordInterface word, DictionaryInterface dictionary, PlayerInterface player) {
        List<WordInterface> wordsList = new ArrayList<>();
        List<LetterInterface> wordLetters = word.getLetters();
        //Size of the board
        short size = BoardInterface.BOARD_SIZE;

        //Check if letters of the word are attached to an other word and check if this combination means something
        if (word.isHorizontal()) {
            // First letter			
        	if(word.getStartColumn()-1 > 0 && 
        			!(board.getLetters().get(word.getStartLine()).get(word.getStartColumn()-1) == null)){

				//verification of the beginning letters to find the complete word
				int w=1;
				while(!(board.getLetters().get(word.getStartLine()).get(word.getStartColumn()-w) == null)){
					w = w+1;
				}

				//Recovery of all the letters
				while(board.getLetters().get(word.getStartColumn()-w) != board.getLetters().get(word.getEndColumn())){

					wordLetters.add(board.getLetters().get(word.getStartLine()).get(word.getStartColumn() - w));
					w = w-1;
				}

                String findWord = LetterListToStringTransformer.transform(word.getLetters());
                if (dictionary.wordExists(findWord)) {
                    WordInterface AdjacentWord = new Word(player, wordLetters, true, word.getStartLine(), word.getStartColumn());
                    wordsList.add(AdjacentWord);
                }
        	
            }

            // Last letter
        	if(word.getEndColumn()+1 < size && 
        			!(board.getLetters().get(word.getEndLine()).get(word.getEndColumn()+1) == null)){

				//verification of the beginning letters to find the complete word
				int w=1;
				while(!(board.getLetters().get(word.getEndLine()).get(word.getEndColumn()+w) == null)){
					w = w+1;
				}

				//Recovery of all the letters
				int z=0;
				while(board.getLetters().get(word.getStartColumn()+z) != board.getLetters().get(word.getEndColumn()+w)){

					wordLetters.add(board.getLetters().get(word.getStartLine()).get(word.getStartColumn()+z));
					z = z+1;
				}

                String findWord = LetterListToStringTransformer.transform(word.getLetters());

                if (dictionary.wordExists(findWord)) {
                    WordInterface AdjacentWord = new Word(player, wordLetters, true, word.getStartLine(), word.getStartColumn());
                    wordsList.add(AdjacentWord);
                }
            }

            //The letters between
            for (int k = 0; k < word.getLetters().size(); k++) {

            	if(!(board.getLetters().get(word.getStartLine()-1).get(word.getStartColumn()+k) == null || 
						board.getLetters().get(word.getStartLine()+1).get(word.getStartColumn()+k) == null)){

					int w = 1;
					//verification above the letter to find the beginning of the word
					while(!(board.getLetters().get(word.getStartLine()-w).get(word.getStartColumn()) == null)){
						w = w+1;
					}


					int z = 1;
					//verification underneath the letter to find the end of the word
					while(!(board.getLetters().get(word.getStartLine()+z).get(word.getStartColumn()) == null)){
						z = z+1;
					}

					//Recovery of all the letters
					while(board.getLetters().get(word.getStartLine() - w) != board.getLetters().get(word.getStartLine() + z)){

						wordLetters.add(board.getLetters().get(word.getStartLine() - w).get(word.getStartColumn()));
						w = w-1;
					}

                    String findWord = LetterListToStringTransformer.transform(word.getLetters());

                    if (dictionary.wordExists(findWord)) {
                        WordInterface AdjacentWord = new Word(player, wordLetters, true, word.getStartLine(), word.getStartColumn());
                        wordsList.add(AdjacentWord);
                    }
                }
            }
        }

        // Verification of adjacent squares for a vertical Word
        else {
            // First letter
			if(word.getStartLine()-1 > 0 && 
					!(board.getLetters().get(word.getStartLine()-1).get(word.getStartColumn()) == null)){

				//verification of the beginning letters to find the complete word
				int w=1;
				while(!(board.getLetters().get(word.getStartLine()-w).get(word.getStartColumn()) == null)){
					w = w+1;
				}

				//Recovery of all the letters
				while(board.getLetters().get(word.getStartLine()-w) != board.getLetters().get(word.getEndLine())){

					wordLetters.add(board.getLetters().get(word.getStartLine()-w).get(word.getStartColumn()));
					w = w-1;
				}

                String findWord = LetterListToStringTransformer.transform(word.getLetters());
                if (dictionary.wordExists(findWord)) {
                    WordInterface AdjacentWord = new Word(player, wordLetters, false, word.getStartLine(), word.getStartColumn());
                    wordsList.add(AdjacentWord);
                }
            }

            // Last letter
			if(word.getEndLine()+1 < size && 
					!(board.getLetters().get(word.getEndLine()+1).get(word.getStartColumn()) == null)){

				//verification of the beginning letters to find the complete word
				int w=1;
				while(!(board.getLetters().get(word.getEndLine()+w).get(word.getStartColumn()) == null)){
					w = w+1;
				}

				//Recovery of all the letters
				int z=0;
				while(board.getLetters().get(word.getStartLine()+z) != board.getLetters().get(word.getEndLine() + w)){

					wordLetters.add(board.getLetters().get(word.getStartLine()+z).get(word.getStartColumn()));
					z = z+1;
				}

                String findWord = LetterListToStringTransformer.transform(word.getLetters());
                if (dictionary.wordExists(findWord)) {
                    WordInterface AdjacentWord = new Word(player, wordLetters, false, word.getStartLine(), word.getStartColumn());
                    wordsList.add(AdjacentWord);
                }
            }

            for (int k = 0; k < word.getLetters().size(); k++) {
				if(!(board.getLetters().get(word.getStartLine()+k).get(word.getStartColumn()-1) == null || 
						board.getLetters().get(word.getStartLine()+k).get(word.getStartColumn()+1) == null)){

					int w = 1;
					//verification above the letter to find the beginning of the word
					while(!(board.getLetters().get(word.getStartColumn()-w).get(word.getStartLine()) == null)){
						w = w+1;
					}


					int z = 1;
					//verification underneath the letter to find the end of the word
					while(!(board.getLetters().get(word.getStartColumn()+z).get(word.getStartLine()) == null)){
						z = z+1;
					}

					//Recovery of all the letters
					while(board.getLetters().get(word.getStartColumn() - w) != board.getLetters().get(word.getStartColumn() + z)){

						wordLetters.add(board.getLetters().get(word.getStartColumn() - w).get(word.getStartLine()));
						w = w-1;
					}

                    String findWord = LetterListToStringTransformer.transform(word.getLetters());
                    if (dictionary.wordExists(findWord)) {
                        WordInterface AdjacentWord = new Word(player, wordLetters, false, word.getStartLine(), word.getStartColumn());
                        wordsList.add(AdjacentWord);
                    }
                }
            }
        }

        return wordsList;
    }
}
