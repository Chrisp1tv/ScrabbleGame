package istv.chrisanc.scrabble.controllers;

import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * This is the controller handling all the Game logic, managing the Scrabble itself. All game-relative actions are done
 * via this controller.
 *
 * @author Christopher Anciaux
 */
public class GameController extends BaseController {
    /**
     * The view representation of the Scrabble {@link BoardInterface}
     */
    @FXML
    protected GridPane scrabbleGrid;

    /**
     * The container of the players list (an {@link HBox} because players are shown one next to the other)
     */
    @FXML
    protected HBox playersListContainer;

    /**
     * The container of the {@link LetterInterface} possessed by the {@link PlayerInterface}
     */
    @FXML
    protected HBox playerLettersContainer;

    /**
     * Initializes the controller.
     *
     * When this method is triggered, the Scrabble game itself is already completely initialized by {@link NewGameController}
     * which handles the creation of a Scrabble game, or by {@link LoadGameController} which loads an already-started Scrabble
     * game
     */
    @FXML
    protected void initialize() {
        // TODO
    }

    /**
     * Validates the word played by the player. This method is triggered when the user clicks on the "Validate the played
     * word". This method must verify the played word's validity, call the ScoreManager etc.
     */
    @FXML
    protected void handleValidatePlayedWord() {
        // TODO
    }

    /**
     * Exchanges a {@link LetterInterface} with the {@link BagInterface}. This method is triggered when the user clicks
     * on the "Exchange a letter with the bag" button
     */
    @FXML
    protected void handleExchangeLetterWithBag() {
        // TODO
    }

    /**
     * Skips the user turn, This method is triggered when the user clicks on the "Skip your turn" button
     */
    @FXML
    protected void handleSkipTurn() {
        // TODO
    }

    /**
     * Opens a dialog to save the game
     */
    @FXML
    protected void handleSaveGame() {
        // TODO @Anciaux Christopher
    }
}
