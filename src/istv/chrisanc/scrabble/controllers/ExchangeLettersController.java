package istv.chrisanc.scrabble.controllers;

import istv.chrisanc.scrabble.exceptions.model.Bag.EmptyBagException;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.utils.LetterToStringTransformer;
import istv.chrisanc.scrabble.utils.ui.DraggableLetterManager;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This controller manages the exchange of {@link LetterInterface} between the {@link BagInterface} and the {@link PlayerInterface}'s
 * {@link LetterInterface}.
 *
 * @author Christopher Anciaux
 */
public class ExchangeLettersController extends BaseController {
    /**
     * The element on which we want to drop the letters to be put back in the {@link BagInterface}
     */
    @FXML
    protected Pane lettersReceiver;

    /**
     * The element containing the {@link PlayerInterface}'s {@link LetterInterface}
     */
    @FXML
    protected HBox playerLettersContainer;

    /**
     * The button validating the exchange, enabled only if letters have been dropped in the lettersReceiver
     */
    @FXML
    protected Button validateExchangeButton;

    /**
     * The stage containing the dialog
     */
    protected Stage dialogStage;

    /**
     * The letters to be put back in the bag
     */
    protected ObservableList<LetterInterface> lettersToPutBackInTheBag = FXCollections.observableArrayList();

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Initializes the interface of the dialog, by listening events
     */
    protected void initializeInterface() {
        this.initializeDragAndDropLetters();
        this.initializeButtonValidateExchangeEvents();
    }

    /**
     * Handles the exchange, after it has been validated by the user
     */
    @FXML
    protected void handleValidateExchange() {
        try {
            this.scrabble.exchangeLetters(this.lettersToPutBackInTheBag);
            this.closeExchangeLettersDialog();
        } catch (EmptyBagException e) {
            this.scrabble.showErrorDrawingLetterFromBagAlert(e);
            this.closeExchangeLettersDialog();
        }
    }

    /**
     * Cancels the exchange, and closes the dialog
     */
    @FXML
    protected void handleCancel() {
        this.closeExchangeLettersDialog();
    }

    /**
     * Creates the event listener to enable/disable the button, if letters have been prepared to be dropped in the bag
     */
    protected void initializeButtonValidateExchangeEvents() {
        this.lettersToPutBackInTheBag.addListener((ListChangeListener.Change<? extends LetterInterface> c) -> {
            // If the user puts a letter in the bag, we enable the button to validate the exchange
            validateExchangeButton.setDisable(0 == c.getList().size());
        });
    }

    /**
     * Manages all the drag and drop logic
     */
    protected void initializeDragAndDropLetters() {
        // We add all the user's letters in the playerLettersContainer
        for (LetterInterface letter : this.scrabble.getCurrentPlayer().getLetters()) {
            Text tileText = new Text(LetterToStringTransformer.transform(letter));

            StackPane tile = new StackPane(tileText);
            tile.getStyleClass().add("tile");

            DraggableLetterManager.makeLetterDraggable(tile, letter);

            this.playerLettersContainer.getChildren().add(tile);
        }

        DraggableLetterManager.makeElementReadyToReceiveLetter(this.lettersReceiver, false, (letter, event) -> {
            this.lettersToPutBackInTheBag.add(letter);
            this.playerLettersContainer.getChildren().remove((Node) event.getGestureSource());
        });
    }

    /**
     * Closes the dialog
     */
    protected void closeExchangeLettersDialog() {
        this.dialogStage.close();
    }
}
