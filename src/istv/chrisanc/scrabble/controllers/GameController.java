package istv.chrisanc.scrabble.controllers;

import istv.chrisanc.scrabble.Scrabble;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

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

    }

    /**
     * Exchanges {@link LetterInterface} with the {@link BagInterface}. This method is triggered when the user clicks
     * on the "Exchange letters with the bag" button
     */
    @FXML
    protected void handleExchangeLetterWithBag() {
        // TODO @Anciaux Christopher
        try {
            // Load letters exchanging view
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(this.scrabble.getI18nMessages());
            loader.setLocation(Scrabble.class.getResource("view/ExchangeLetter.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle(this.scrabble.getI18nMessages().getString("exchangeLettersWithTheBag"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.scrabble.getPrimaryStage());

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ExchangeLettersController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setScrabble(this.scrabble);
            controller.initializeInterface();

            dialogStage.showAndWait();
        } catch (IOException e) {
            // TODO Manages the error in a more user-friendly way
            e.printStackTrace();
        }
    }

    /**
     * Skips the user turn, This method is triggered when the user clicks on the "Skip your turn" button
     */
    @FXML
    protected void handleSkipTurn() {
        // TODO @Bouaggad Abdessamade
    }

    /**
     * Opens a dialog to save the game
     */
    @FXML
    protected void handleSaveGame() {
        try {
            // Load game saving view
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(this.scrabble.getI18nMessages());
            loader.setLocation(Scrabble.class.getResource("view/SaveGame.fxml"));
            AnchorPane page = loader.load();

            // Create the dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setTitle(this.scrabble.getI18nMessages().getString("save"));
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(this.scrabble.getPrimaryStage());

            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            SaveGameController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setScrabble(this.scrabble);

            dialogStage.showAndWait();
        } catch (IOException e) {
            // TODO Manages the error in a more user-friendly way
            e.printStackTrace();
        }
    }
}
