package istv.chrisanc.scrabble.utils;

import java.util.List;

import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;

/**
 * This class handles all the calculations of the {@link Player}'s score, and manages all score-relative logic.
 *
 * @author Christopher Anciaux
 * @author Eguinane Chavatte
 */
public class ScoreManager {
    public static int updateScore(List<WordInterface> playedWords, BoardInterface board) {
    	int i,j,Somme=0;
    	for(i=0;i<playedWords.size();i++){
    		int wordMulti=1;
    		int Score=0;
    		for(j=0;j<playedWords.get(i).getLetters().size();j++){
    			if(playedWords.get(i).isHorizontal()){
    				if(board.getSquares()
						.get(playedWords.get(i).getStartColumn()+j)
						.get(playedWords.get(i).getStartLine()).isMultiplierUsed()==false)
    				{	//Multiplier unused and Horizontal Word
    					Score += ((int) playedWords.get(i).getLetters().get(j).getValue())
    								*board.getSquares()
    								.get(playedWords.get(i).getStartColumn()+j)
    								.get(playedWords.get(i).getStartLine()).getLetterMultiplier();
    					wordMulti *= board.getSquares()
    								.get(playedWords.get(i).getStartColumn()+j)
    								.get(playedWords.get(i).getStartLine()).getWordMultiplier();
    				}else{//Multiplier used
    					Score += ((int) playedWords.get(i).getLetters().get(j).getValue());
    				}
    			}else{	
    				if(board.getSquares()
    						.get(playedWords.get(i).getStartColumn())
    						.get(playedWords.get(i).getStartLine()+j).isMultiplierUsed()==false)
        				{	//Multiplier unused and Vertical word
    						Score += ((int) playedWords.get(i).getLetters().get(j).getValue())
    									*board.getSquares()
    									.get(playedWords.get(i).getStartColumn())
    									.get(playedWords.get(i).getStartLine()+j).getLetterMultiplier();
        					wordMulti *= board.getSquares()
        								.get(playedWords.get(i).getStartColumn())
        								.get(playedWords.get(i).getStartLine()+j).getWordMultiplier();
        				}else{//Multiplier used
        					Score += ((int) playedWords.get(i).getLetters().get(j).getValue());
    				}
    			}
    		}
    		Somme += Score*wordMulti;
    	}
    	int nb=0;
    	for(i=0;i<playedWords.size();i++){
    		for(j=0;j<playedWords.get(i).getLetters().size();j++){
    			if(playedWords.get(i).isHorizontal()){
    				if(board.getSquares()
    						.get(playedWords.get(i).getStartColumn()+j)
    						.get(playedWords.get(i).getStartLine()).isMultiplierUsed()==false)
        				{
    						nb+=1;
    						board.getSquares()
    							.get(playedWords.get(i).getStartColumn()+j)
    							.get(playedWords.get(i).getStartLine()).makeMultiplierUsed();
        				}
    			}else{
    				if(board.getSquares()
    						.get(playedWords.get(i).getStartColumn())
    						.get(playedWords.get(i).getStartLine()+j).isMultiplierUsed()==false)
        			{
    					nb+=1;
    					board.getSquares()
							.get(playedWords.get(i).getStartColumn())
							.get(playedWords.get(i).getStartLine()+j).makeMultiplierUsed();
        			}
    			}
    		}
    	}
    	if(nb==7){
    		Somme+=50;
    	}
    	return Somme;
    }
    		/*
    		*	that function is a specific ask from the IA's programming team to compare the best moves to do 
    		*	this is like the updateScore function but it let the multipliers unchanged
    		*/
    public static int testScore(List<WordInterface> playedWords, BoardInterface board) {
    	int i,j,Somme=0;
    	for(i=0;i<playedWords.size();i++){
    		int wordMulti=1;
    		int Score=0;
    		for(j=0;j<playedWords.get(i).getLetters().size();j++){
    			if(playedWords.get(i).isHorizontal()){
    				if(board.getSquares()
						.get(playedWords.get(i).getStartColumn()+j)
						.get(playedWords.get(i).getStartLine()).isMultiplierUsed()==false)
    				{	//Multiplier unused and Horizontal Word
    					Score += ((int) playedWords.get(i).getLetters().get(j).getValue())
    								*board.getSquares()
    								.get(playedWords.get(i).getStartColumn()+j)
    								.get(playedWords.get(i).getStartLine()).getLetterMultiplier();
    					wordMulti *= board.getSquares()
    								.get(playedWords.get(i).getStartColumn()+j)
    								.get(playedWords.get(i).getStartLine()).getWordMultiplier();
    				}else{//Multiplier used
    					Score += ((int) playedWords.get(i).getLetters().get(j).getValue());
    				}
    			}else{	
    				if(board.getSquares()
    						.get(playedWords.get(i).getStartColumn())
    						.get(playedWords.get(i).getStartLine()+j).isMultiplierUsed()==false)
        				{	//Multiplier unused and Vertical word
    						Score += ((int) playedWords.get(i).getLetters().get(j).getValue())
    									*board.getSquares()
    									.get(playedWords.get(i).getStartColumn())
    									.get(playedWords.get(i).getStartLine()+j).getLetterMultiplier();
        					wordMulti *= board.getSquares()
        								.get(playedWords.get(i).getStartColumn())
        								.get(playedWords.get(i).getStartLine()+j).getWordMultiplier();
        				}else{//Multiplier used
        					Score += ((int) playedWords.get(i).getLetters().get(j).getValue());
    				}
    			}
    		}
    		Somme += Score*wordMulti;
    	}
    	int nb=0;
    	for(i=0;i<playedWords.size();i++){
    		for(j=0;j<playedWords.get(i).getLetters().size();j++){
    			if(playedWords.get(i).isHorizontal()){
    				if(board.getSquares()
    						.get(playedWords.get(i).getStartColumn()+j)
    						.get(playedWords.get(i).getStartLine()).isMultiplierUsed()==false)
        			{
    					nb+=1;
    				}
    			}else{
    				if(board.getSquares()
    						.get(playedWords.get(i).getStartColumn())
    						.get(playedWords.get(i).getStartLine()+j).isMultiplierUsed()==false)
        			{
    					nb+=1;
    				}
    			}
    		}
    	}
    	if(nb==7){
    		Somme+=50;
    	}
    	return Somme;
    }
}
