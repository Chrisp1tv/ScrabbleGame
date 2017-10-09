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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;

/**
 * @author Christopher Anciaux TODO @Bouaggad Abdessamade
 */
public class NewGameController extends BaseController {

	private Bag myBag;
	private French mydictionnaire;
	private Board myBoard;
	private ArrayList<Player> myPlayersList;
	@FXML
	protected TextField username;

	/**
	 * Abdessamade BOUAGGAD Redirects to the Game action
	 *
	 * @throws ErrorLoadingDictionaryException
	 * @throws EmptyBagException
	 */
	@FXML
	protected void handleStartGame() throws ErrorLoadingDictionaryException, EmptyBagException {
		// this.scrabble.showNewGame();
		if (username.getText().trim().isEmpty()) {
			 Alert fail= new Alert(AlertType.INFORMATION);
		        fail.setHeaderText("Attention");
		        fail.setContentText("Veuillez Saisir Votre Nom Avant De Lancer La Partie SVP ! Merci De Réessayer");
		        fail.showAndWait();
		}
		else if(username.getText().length()<3) {
			Alert fail= new Alert(AlertType.INFORMATION);
	        fail.setHeaderText("Attention");
	        fail.setContentText("Votre Nom Doit Contenir Au Moins 3 Lettres ! Merci De Réessayer .");
	        fail.showAndWait();
		}

		else {
			myPlayersList= new ArrayList<Player>();
			myBag = new Bag();
			mydictionnaire = new French();
			myBoard = new Board();
			myPlayersList.add(new Player(username.getText()));
			myPlayersList.add(new Player("This Is IA"));

			for (Player player : myPlayersList) {
				player.addLetter(myBag.drawLetter());
			}

			this.scrabble.showGame();
		}

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
