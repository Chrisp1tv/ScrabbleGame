package istv.chrisanc.scrabble.controllers;



import java.util.ArrayList;

import istv.chrisanc.scrabble.exceptions.model.Bag.EmptyBagException;
import istv.chrisanc.scrabble.exceptions.utils.dictionaries.ErrorLoadingDictionaryException;
import istv.chrisanc.scrabble.model.Bag;
import istv.chrisanc.scrabble.model.Board;
import istv.chrisanc.scrabble.model.Player;
import istv.chrisanc.scrabble.utils.dictionaries.French;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * @author Christopher Anciaux
 * TODO @Bouaggad Abdessamade
 */
public class NewGameController extends BaseController{

	private Bag myBag;
	private French mydictionnaire;
	private Board myBoard;
	private ArrayList<Player> myPlayersList;
	@FXML
	protected TextField username;


    /**
     * Abdessamade BOUAGGAD
     * Redirects to the Game action
     * @throws ErrorLoadingDictionaryException
     * @throws EmptyBagException
     */
    @FXML
    protected void handleStartGame(ActionEvent event) throws ErrorLoadingDictionaryException, EmptyBagException {

        //this.scrabble.showNewGame();
    	myBag = new Bag();
    	mydictionnaire = new French();
    	myBoard = new Board();
    	myPlayersList.add(new Player(username.getText()));
    	myPlayersList.add(new Player("This Is IA"));

    	for(Player player : myPlayersList) {
    		player.addLetter(myBag.drawLetter());
    	}

    	this.scrabble.showGame();

    }

    /**
     * Redirects to Home Window
     */
    @FXML
    protected void handleCancelGame() {
       // this.scrabble.showNewGame();
    	this.scrabble.cancelGame();
    }

}
