package istv.chrisanc.scrabble.controllers;

import javafx.fxml.FXML;

/**
 * This is the controller which recaps the game, announces the winner, and redirects the user to the home if he wants to.
 *
 * @author Christopher Anciaux
 * @author Abdessamade Bouaggad
 * TODO @Bouaggad Abdessamade
 */
public class GameEndedController extends BaseController {
	@FXML
	protected void handleGoHome() {
		this.scrabble.showHome();
	}

	@FXML
	protected void handleNewGame() {
		this.scrabble.showNewGame();
	}

	@FXML
	protected void handleQuit() {
        System.exit(0);
	}

	protected void showWinner() {
		//ObservableList<Person> personData = FXCollections.observableArrayList();
	}
}
