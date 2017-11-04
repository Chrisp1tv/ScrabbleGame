package istv.chrisanc.scrabble.controllers;

import istv.chrisanc.scrabble.Scrabble;
import istv.chrisanc.scrabble.exceptions.InvalidPlayedTurnException;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.utils.ui.Templates;
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
        this.displayPlayerLettersList();
        this.listenCurrentPlayer();
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
        }

        this.refreshScrabbleInterface();
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
            dialogStage.setMinWidth(640);
            dialogStage.setMaxWidth(640);
            dialogStage.setMinHeight(360);
            dialogStage.setMaxHeight(360);

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
            dialogStage.setMinWidth(640);
            dialogStage.setMaxWidth(640);
            dialogStage.setMinHeight(200);
            dialogStage.setMaxHeight(200);

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
            this.controlButtons.setDisable(!newValue.isHuman());

            if (newValue.isHuman()) {
                this.refreshScrabbleInterface();
            }
        });
    }

    /**
     * This class is used to send a board position to other objects during turn validation only, and only in this case.
     *
     * @author Christopher Anciaux
     */
    public static class BoardPosition implements Comparable<BoardPosition> {
        /**
         * The line on which the letter has been played
         */
        protected short line;

        /**
         * The column on which the letter has been played
         */
        protected short column;

        public BoardPosition(short line, short column) {
            this.line = line;
            this.column = column;
        }

        public short getLine() {
            return line;
        }

        public short getColumn() {
            return column;
        }

        @Override
        public int compareTo(BoardPosition o) {
            if (o.getLine() == this.getLine()) {
                return this.getColumn() - o.getColumn();
            }

            return this.getLine() - o.getLine();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            BoardPosition that = (BoardPosition) o;

            return line == that.line && column == that.column;
        }

        @Override
        public int hashCode() {
            int result = (int) line;
            result = 31 * result + (int) column;

            return result;
        }
    }
}
