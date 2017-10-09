package istv.chrisanc.scrabble.controllers;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

import istv.chrisanc.scrabble.Scrabble;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.control.ButtonType;

/**
 * This is the controller which recaps the game, announcing the winner, and redirecting the user to the home.
 *
 * @author Christopher Anciaux
 * TODO @Bouaggad Abdessamade
 */
public class GameEndedController extends BaseController {
	protected ResourceBundle i18nMessages;

	@FXML
	protected void handleHomeButton() {
		this.scrabble.showHome();
	}

	@FXML
	protected void handleNewGameButton() {
		this.scrabble.showNewGame();
	}



	@FXML
	protected void handleQuitButton() {

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Quitter Le Jeu");
		alert.setHeaderText("Vous Êtes Sur De Quitter Le Jeu ?");
		alert.setContentText("");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK){
			System.exit(0);
		} else {
			//TODO
		}

		//System.exit(0);

	}
}
