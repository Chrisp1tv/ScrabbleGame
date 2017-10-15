package istv.chrisanc.scrabble.controllers;

import istv.chrisanc.scrabble.Scrabble;
import istv.chrisanc.scrabble.exceptions.InvalidPlayedTurnException;
import istv.chrisanc.scrabble.exceptions.NonExistentWordException;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.SquareInterface;
import istv.chrisanc.scrabble.utils.ui.DraggableLetterManager;
import istv.chrisanc.scrabble.utils.ui.Templates;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the controller handling all the Game view logic, managing the Scrabble interface itself. All game-relative actions are done
 * through this controller but it doesn't handle the Scrabble logic, it only handles the interface of the Scrabble.
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
    protected VBox playersListContainer;

    /**
     * The container of the {@link LetterInterface} possessed by the {@link PlayerInterface}
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
     * the player.
     */
    @FXML
    protected Button takeBackLettersButton;

    /**
     * The letters being played (put on the board) by the user. The index is a list with, at index 0, the line of the letter,
     * and at index 1 the column of the letter. The value is the played letter.
     */
    protected Map<List<Integer>, LetterInterface> playedLetters = new HashMap<>();

    /**
     * Initializes the controller
     * <p>
     * When this method is triggered, the Scrabble game itself is already completely initialized by {@link NewGameController}
     * which handles the creation of a Scrabble game, or by {@link LoadGameController} which loads an already-started Scrabble
     * game
     */
    public void initializeInterface() {
        this.initializePlayedLetters();
        Templates.displayPlayers(this.playersListContainer, this.scrabble.currentPlayerProperty(), this.scrabble.getPlayers(), this.scrabble.getI18nMessages());
        this.displayBoardGrid();
        this.displayPlayerLettersList();
        this.createControlButtonsEvents();
    }

    /**
     * Validates the letters played by the player. This method is triggered when the user clicks on the "Validate the played
     * letters"
     */
    @FXML
    protected void handleValidatePlayedLetters() {
        try {
            this.scrabble.playLetters(this.playedLetters);
        } catch (InvalidPlayedTurnException | NonExistentWordException e) {
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
     * Exchanges a {@link LetterInterface} with the {@link BagInterface}. This method is triggered when the user clicks
     * on the "Exchange a letter with the bag" button
     */
    @FXML
    protected void handleExchangeLetterWithBag() {
        try {
            this.refreshScrabbleInterface();

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

            this.refreshLetters();
        } catch (IOException e) {
            this.scrabble.showGeneralApplicationError(e);
        }
    }

    /**
     * Skips the user turn, this method is triggered when the user clicks on the "Skip your turn" button
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
            this.scrabble.showGeneralApplicationError(e);
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
     * Initializes the letters of the temporary board
     */
    protected void initializePlayedLetters() {
        this.playedLetters = new HashMap<>();
    }

    /**
     * Displays the board grid
     */
    protected void displayBoardGrid() {
        ObservableList<ObservableList<SquareInterface>> squares = this.scrabble.getBoard().getSquares();

        for (int i = 0, squaresSize = squares.size(); i < squaresSize; i++) {
            ObservableList<SquareInterface> squaresLine = squares.get(i);

            for (int j = 0, squaresLineSize = squaresLine.size(); j < squaresLineSize; j++) {
                StackPane square;

                if (null != squaresLine.get(j).getInformation()) {
                    Text squareText = new Text(this.scrabble.getI18nMessages().getString(squaresLine.get(j).getInformation()));
                    squareText.getStyleClass().add("square-legend");

                    square = new StackPane(squareText);
                } else {
                    square = new StackPane();
                }

                square.getStyleClass().addAll("square", squaresLine.get(j).getCssClass());

                // First line
                if (0 == i) {
                    square.getStyleClass().add("first-line");
                }

                // First column
                if (0 == j) {
                    square.getStyleClass().add("first-column");
                }

                if (null == this.scrabble.getBoard().getLetters().get(i).get(j)) {
                    int finalI = i;
                    int finalJ = j;
                    DraggableLetterManager.makeElementReadyToReceiveLetter(square, true, (letter, event) -> {
                        square.getChildren().add((Node) event.getGestureSource());
                        this.playerLettersContainer.getChildren().remove(event.getGestureSource());
                        this.playedLetters.put(Arrays.asList(finalI, finalJ), letter);
                    });
                } else {
                    Templates.displayLetter(square, this.scrabble.getBoard().getLetters().get(i).get(j), false);
                }

                this.scrabbleGrid.add(square, j, i);
            }
        }
    }

    /**
     * Displays the player's letters
     */
    protected void displayPlayerLettersList() {
        // Initializes the letters list
        Templates.displayLetters(this.playerLettersContainer, this.scrabble.getHumanPlayer().getLetters(), true);
    }

    /**
     * Handles events of the action buttons
     */
    protected void createControlButtonsEvents() {
        this.scrabble.currentPlayerProperty().addListener((observable, oldValue, newValue) -> this.controlButtons.setDisable(newValue != this.scrabble.getHumanPlayer()));
    }
}
