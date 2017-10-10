package istv.chrisanc.scrabble.controllers;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

/**
 * This controller manages the Home of the Scrabble, the first page displayed to the user, allowing him to choose between
 * a new game, loading one, or quitting the game.
 *
 * @author Christopher Anciaux
 */
public class HomeController extends BaseController {
    /**
     * Redirects to the NewGame action
     */
    @FXML
    protected void handleNewGame() {
        this.scrabble.showNewGame();
    }

    /**
     * Redirects to the LoadGame action
     */
    @FXML
    protected void handleLoadGame() {
        this.scrabble.showLoadGame();
    }

    /**
     * Closes the application.
     */
    @FXML
    protected void handleExit() {
    	ButtonType buttonTypeOui = new ButtonType("Oui");
    	ButtonType buttonTypeAnnuler = new ButtonType("Annuler");

    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Quitter Le Jeu");
		alert.setHeaderText("Vous Êtes Sur De Quitter Le Jeu ?");
		alert.setContentText("");

		alert.getButtonTypes().setAll(buttonTypeOui,buttonTypeAnnuler);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == buttonTypeOui){
			System.exit(0);
		} else {
			//TODO
		}
    }
}
