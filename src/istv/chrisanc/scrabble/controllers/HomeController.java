package istv.chrisanc.scrabble.controllers;

import javafx.fxml.FXML;

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
        System.exit(0);
    }
}
