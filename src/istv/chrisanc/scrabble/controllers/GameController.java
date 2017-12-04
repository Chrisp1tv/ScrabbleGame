package istv.chrisanc.scrabble.controllers;

import istv.chrisanc.scrabble.Scrabble;
import istv.chrisanc.scrabble.exceptions.InvalidPlayedTurnException;
import istv.chrisanc.scrabble.model.BoardPosition;
import istv.chrisanc.scrabble.model.interfaces.HumanPlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.utils.ArtificialIntelligenceHelper;
import istv.chrisanc.scrabble.utils.ui.Templates;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This is the controller handling all the Game view logic, managing the Scrabble interface itself. All game-relative actions are done
 * through this controller but it doesn't handle the Scrabble logic, it only handles the interface of the Scrabble.
 *
 * @author Christopher Anciaux
 */
public class GameController extends BaseController {
    /**
     * The container of the Scrabble game
     */
    @FXML
    protected BorderPane scrabbleContainer;

    /**
     * The view representation of the Scrabble board
     */
    @FXML
    protected GridPane scrabbleGrid;

    /**
     * The container of the players list (an {@link HBox} because players are shown one next to the other)
     */
    @FXML
    protected VBox playersListContainer;

    /**
     * The container of the letters possessed by the player
     */
    @FXML
    protected HBox playerLettersContainer;

    /**
     * The container of the control buttons
     */
    @FXML
    protected VBox controlButtons;

    /**
     * The button used to ask for help
     */
    @FXML
    protected Button askHelpButton;

    /**
     * The button used to exchange a letter with the bag
     */
    @FXML
    protected Button exchangeLetterButton;

    /**
     * The button used to validate a played word (the letters placed on the board)
     */
    @FXML
    protected Button validatePlayedLettersButton;

    /**
     * The button used to cancel the current turn and take back the letters disposed on the Board that aren't validated by
     * the player
     */
    @FXML
    protected Button takeBackLettersButton;

    /**
     * The letters being played (put on the board) by the user
     * The played letters are ordered in the grid order (from top to bottom and from left to right)
     */
    protected SortedMap<BoardPosition, LetterInterface> playedLetters = new TreeMap<>();

    /**
     * Initializes the controller
     *
     * When this method is triggered, the Scrabble game itself is already completely initialized by {@link NewGameController}
     * which handles the creation of a Scrabble game, or by {@link LoadGameController} which loads an already-started Scrabble
     * game
     */
    public void initializeInterface() {
        this.initializePlayedLetters();
        Templates.displayPlayers(this.playersListContainer, this.scrabble.currentPlayerProperty(), this.scrabble.getPlayers(), this.scrabble.getI18nMessages());
        this.initializeBoardGrid();
        this.displayBoardGrid();
        this.listenCurrentPlayer();

        if (this.scrabble.getCurrentPlayer() instanceof HumanPlayerInterface) {
            this.displayPlayerLettersList();
        } else {
            this.playerLettersContainer.setDisable(true);
            this.controlButtons.setDisable(true);
        }
    }

    /**
     * Helps the player to play his turn by playing the best turn possible for him
     */
    @FXML
    protected void handleAskHelp() {
        this.controlButtons.setDisable(true);
        this.playerLettersContainer.setDisable(true);

        HumanPlayerInterface currentPlayer = (HumanPlayerInterface) this.scrabble.getCurrentPlayer();

        Task<SortedMap<BoardPosition, LetterInterface>> bestTurnTaskFinder = ArtificialIntelligenceHelper.getBestTurnPossible(this.scrabble.getLanguage(), this.scrabble.getBoard(), currentPlayer);

        bestTurnTaskFinder.setOnSucceeded(event -> {
            if (null != bestTurnTaskFinder.getValue()) {
                try {
                    this.scrabble.playLetters(bestTurnTaskFinder.getValue());
                    currentPlayer.decreaseAvailableHelps();

                    return;
                } catch (InvalidPlayedTurnException ignored) {
                }
            }

            this.askHelpButton.setDisable(true);
            this.controlButtons.setDisable(false);
            this.playerLettersContainer.setDisable(false);

            this.showAlertOfNoBestTurnPossible();
        });

        new Thread(bestTurnTaskFinder).start();
    }

    /**
     * Validates the letters played by the player
     */
    @FXML
    protected void handleValidatePlayedLetters() {
        try {
            this.scrabble.playLetters(this.playedLetters);
        } catch (InvalidPlayedTurnException e) {
            this.showAlertOfInvalidTurn(e);
            this.refreshScrabbleInterface();
        }
    }

    /**
     * Cancels the current turn, and give back to the user his letters that he didn't validate
     */
    @FXML
    protected void handleTakeBackLetters() {
        this.refreshScrabbleInterface();
    }

    /**
     * Exchanges a letter with the bag
     */
    @FXML
    protected void handleExchangeLetterWithBag() {
        try {
            this.refreshScrabbleInterface();

            // Load letters exchanging view
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(this.scrabble.getI18nMessages());
            loader.setLocation(Scrabble.class.getResource("view/ExchangeLetter.fxml"));
            VBox page = loader.load();

            // Create the dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setMinWidth(ExchangeLettersController.EXCHANGE_LETTERS_STAGE_WIDTH);
            dialogStage.setMaxWidth(ExchangeLettersController.EXCHANGE_LETTERS_STAGE_WIDTH);
            dialogStage.setMinHeight(ExchangeLettersController.EXCHANGE_LETTERS_STAGE_HEIGHT);
            dialogStage.setMaxHeight(ExchangeLettersController.EXCHANGE_LETTERS_STAGE_HEIGHT);

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

            this.refreshLetters();
        } catch (IOException e) {
            this.scrabble.showGeneralApplicationError(e);
        }
    }

    /**
     * Skips the user turn
     */
    @FXML
    protected void handleSkipTurn() {
        this.scrabble.skipTurn();
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
            VBox page = loader.load();

            // Create the dialog stage
            Stage dialogStage = new Stage();
            dialogStage.setMinWidth(SaveGameController.STAGE_WIDTH);
            dialogStage.setMaxWidth(SaveGameController.STAGE_WIDTH);
            dialogStage.setMinHeight(SaveGameController.STAGE_HEIGHT);
            dialogStage.setMaxHeight(SaveGameController.STAGE_HEIGHT);

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
            this.scrabble.showGeneralApplicationError(e);
        }
    }

    /**
     * Quits the game after the user confirmed his choice
     */
    @FXML
    protected void handleQuitGame() {
        Optional<ButtonType> quitDialog = this.showQuitConfirmationDialog();

        if (quitDialog.isPresent() && quitDialog.get() == ButtonType.OK) {
            this.scrabble.showHome();
        }
    }

    /**
     * Refreshes the Scrabble interface, to keep it synchronized with the actual Scrabble state
     */
    protected void refreshScrabbleInterface() {
        this.initializePlayedLetters();

        this.refreshBoard();
        this.refreshLetters();
    }

    /**
     * Refreshes the board
     */
    protected void refreshBoard() {
        this.scrabbleGrid.getChildren().clear();
        this.displayBoardGrid();
    }

    /**
     * Refreshes the letters
     */
    protected void refreshLetters() {
        this.playerLettersContainer.getChildren().clear();
        this.displayPlayerLettersList();
    }

    /**
     * Shows an alert when the user is doing an invalid turn, no respecting Scrabble rules
     *
     * @param e The exception to be handled
     */
    protected void showAlertOfInvalidTurn(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(this.scrabble.getI18nMessages().getString("error"));
        alert.setHeaderText(this.scrabble.getI18nMessages().getString("errorInPlayedTurn"));
        alert.setContentText(this.scrabble.getI18nMessages().getString(e.getMessage()));

        alert.showAndWait();
    }

    /**
     * Shows an alert when the user tries to ask for the best turn, but the Artificial Intelligence couldn't find one
     */
    protected void showAlertOfNoBestTurnPossible() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(this.scrabble.getI18nMessages().getString("error"));
        alert.setHeaderText(this.scrabble.getI18nMessages().getString("noBestTurnPossible"));
        alert.setContentText(this.scrabble.getI18nMessages().getString("noBestTurnIsPossible"));

        alert.showAndWait();
    }

    /**
     * Shows a confirmation dialog when the player is leaving the game
     */
    protected Optional<ButtonType> showQuitConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(this.scrabble.getI18nMessages().getString("confirmation"));
        alert.setHeaderText(this.scrabble.getI18nMessages().getString("quitConfirmationDialogTitle"));
        alert.setContentText(this.scrabble.getI18nMessages().getString("quitConfirmationDialogInformation"));

        return alert.showAndWait();
    }

    /**
     * Initializes the letters of the temporary board
     */
    protected void initializePlayedLetters() {
        this.playedLetters = new TreeMap<>();
    }

    /**
     * Initializes the board grid
     */
    protected void initializeBoardGrid() {
        Templates.initializeBoardGrid(this.scrabbleGrid, this.scrabbleContainer);
    }

    /**
     * Displays the board grid
     */
    protected void displayBoardGrid() {
        Templates.displayBoardGrid(this.scrabble.getI18nMessages(), this.scrabble.getBoard().getSquares(), this.playedLetters, this.scrabble.getBoard().getLetters(), this.playerLettersContainer, this.scrabbleGrid);
    }

    /**
     * Displays the player's letters
     */
    protected void displayPlayerLettersList() {
        Templates.displayLetters(this.playerLettersContainer, this.scrabble.getCurrentPlayer().getLetters(), true);
    }

    /**
     * Handles events of the action buttons and manages the refresh of the interface according to the current player
     */
    protected void listenCurrentPlayer() {
        this.scrabble.currentPlayerProperty().addListener((observable, oldValue, newValue) -> {
            this.controlButtons.setDisable(!(newValue instanceof HumanPlayerInterface));

            if (newValue instanceof HumanPlayerInterface) {
                this.askHelpButton.setDisable(0 >= ((HumanPlayerInterface) newValue).getAvailableHelps());
                this.playerLettersContainer.setDisable(false);
                this.refreshScrabbleInterface();
            } else {
                this.playerLettersContainer.setDisable(true);
            }
        });
    }
}
