package istv.chrisanc.scrabble.controllers;

import istv.chrisanc.scrabble.exceptions.model.Bag.EmptyBagException;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.utils.LetterToStringTransformer;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    protected HBox lettersContainer;

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
            this.scrabble.getCurrentPlayer().addLetters(this.scrabble.getBag().exchangeLetters(this.lettersToPutBackInTheBag));
            this.closeExchangeLettersDialog();

            System.out.println(this.lettersToPutBackInTheBag);
        } catch (EmptyBagException e) {
            // TODO: Handle exception
            e.printStackTrace();
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
        this.lettersToPutBackInTheBag.addListener(new ListChangeListener<LetterInterface>() {
            @Override
            public void onChanged(Change<? extends LetterInterface> c) {
                // If the user puts a letter in the bag, we enable the button to validate the exchange
                validateExchangeButton.setDisable(0 == c.getList().size());
            }
        });
    }

    /**
     * Manages all the drag and drop logic
     */
    protected void initializeDragAndDropLetters() {
        // We add all the user's letters in the lettersContainer
        for (LetterInterface letter : this.scrabble.getCurrentPlayer().getLetters()) {
            Text draggableLetter = new Text(LetterToStringTransformer.transform(letter));

            // When an user starts to drag a letter
            draggableLetter.setOnDragDetected(event -> {
                Dragboard dragboard = draggableLetter.startDragAndDrop(TransferMode.ANY);
                dragboard.setDragView(draggableLetter.snapshot(null, null));
                draggableLetter.setVisible(false);

                ClipboardContent clipboardContent = new ClipboardContent();
                clipboardContent.put(LetterInterface.DATA_FORMAT, letter);

                dragboard.setContent(clipboardContent);

                event.consume();
            });

            // When the user has dropped the letter
            draggableLetter.setOnDragDone(event -> {
                if (event.isDropCompleted()) {
                    this.lettersContainer.getChildren().remove(draggableLetter);
                } else {
                    draggableLetter.setVisible(true);
                }
            });

            this.lettersContainer.getChildren().add(draggableLetter);
        }

        // Determines if the user can drag the element on the lettersReceiver
        this.lettersReceiver.setOnDragOver(event -> {
            if (event.getGestureSource() != lettersReceiver && event.getDragboard().hasContent(LetterInterface.DATA_FORMAT)) {
                event.acceptTransferModes(TransferMode.ANY);
            }

            event.consume();
        });

        // The user successfully dragged a letter on the lettersReceiver
        this.lettersReceiver.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasContent(LetterInterface.DATA_FORMAT)) {
                success = true;

                this.lettersContainer.getChildren().remove(event.getGestureSource());
                this.lettersToPutBackInTheBag.add((LetterInterface) db.getContent(LetterInterface.DATA_FORMAT));
            } else {
                ((Text) event.getGestureSource()).setVisible(true);
            }

            event.setDropCompleted(success);

            event.consume();
        });
    }

    /**
     * Closes the dialog
     */
    protected void closeExchangeLettersDialog() {
        this.dialogStage.close();
    }
}
