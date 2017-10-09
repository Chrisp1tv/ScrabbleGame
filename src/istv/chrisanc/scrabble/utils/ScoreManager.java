package istv.chrisanc.scrabble.utils;

import java.util.ArrayList;
import java.util.List;

import istv.chrisanc.scrabble.model.Player;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;
import javafx.collections.ObservableList;

/**
 * This class handles all the calculations of the {@link Player}'s score, and manages all score-relative logic.
 *
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 */
public class ScoreManager {
    public static int updateScore(List<WordInterface> playedWords, BoardInterface board) {
    	int i,j,Somme=0;
    	List<Integer> Score = new ArrayList<Integer>();
    	for(i=0;i<playedWords.size();i++){
    		Score.add(0);
    	}
    	for(i=0;i<playedWords.size();i++){
    		int wordMulti=1;
    		for(j=0;j<playedWords.get(i).getLetters().size();j++){
    			if(playedWords.get(i).isHorizontal()){
    				if(board.getSquares()
						.get(playedWords.get(i).getStartColumn()+j)
						.get(playedWords.get(i).getStartLine()).isMultiplierUsed()==false)
    				{
    					Score.set(i, Score.get(i)+ ((int) playedWords.get(i).getLetters().get(j).getValue())
    							*board.getSquares()
    							.get(playedWords.get(i).getStartColumn()+j)
    							.get(playedWords.get(i).getStartLine()).getLetterMultiplier());
    					wordMulti *= board.getSquares()
    								.get(playedWords.get(i).getStartColumn()+j)
    								.get(playedWords.get(i).getStartLine()).getWordMultiplier();
    					board.getSquares()
							.get(playedWords.get(i).getStartColumn()+j)
							.get(playedWords.get(i).getStartLine()).makeMultiplierUsed();
    				}
    			}else{
    				if(board.getSquares()
    						.get(playedWords.get(i).getStartColumn())
    						.get(playedWords.get(i).getStartLine()+j).isMultiplierUsed()==false)
        				{
    						Score.set(i,Score.get(i)+((int) playedWords.get(i).getLetters().get(j).getValue())
    								*board.getSquares()
    								.get(playedWords.get(i).getStartColumn())
    								.get(playedWords.get(i).getStartLine()+j).getLetterMultiplier());
        					wordMulti *= board.getSquares()
        								.get(playedWords.get(i).getStartColumn())
        								.get(playedWords.get(i).getStartLine()+j).getWordMultiplier();
        					board.getSquares()
    							.get(playedWords.get(i).getStartColumn())
    							.get(playedWords.get(i).getStartLine()+j).makeMultiplierUsed();
        				}
    				}
    		}
    		Somme += Score.get(i)*wordMulti;
    	}
    	return Somme;
        /* TODO
        This method should actualize all the Square states :
        For each letter placed on the square, the MultiplierUsed value of the square must be changed to true
        */
    }
}
