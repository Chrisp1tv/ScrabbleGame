package istv.chrisanc.scrabble.utils.ui;

import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import javafx.scene.Node;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

/**
 * This class handles all the events relative to the draggable letters.
 *
 * @author Christopher Anciaux
 */
abstract public class DraggableLetterManager {
    public interface DraggedLetterHandler {
        void manipulateLetter(LetterInterface letter, DragEvent dragEvent);
    }

    public static void makeLetterDraggable(Node letterElement, LetterInterface letter) {
        // When an user starts to drag a letter
        letterElement.setOnDragDetected(event -> {
            Dragboard dragboard = letterElement.startDragAndDrop(TransferMode.ANY);
            dragboard.setDragView(letterElement.snapshot(null, null));
            letterElement.setVisible(false);

            ClipboardContent clipboardContent = new ClipboardContent();
            clipboardContent.put(LetterInterface.DATA_FORMAT, letter);

            dragboard.setContent(clipboardContent);

            event.consume();
        });

        // When the user has dropped the letter
        letterElement.setOnDragDone(event -> {
            letterElement.setVisible(true);
        });
    }

    public static void makeElementReadyToReceiveLetter(Pane lettersReceiver, boolean allowOneLetterOnly, DraggedLetterHandler onLetterDragDone) {
        // Determines if the user can drag the element on the lettersReceiver
        lettersReceiver.setOnDragOver(event -> {
            if (event.getGestureSource() != lettersReceiver && event.getDragboard().hasContent(LetterInterface.DATA_FORMAT)) {
                event.acceptTransferModes(TransferMode.ANY);
            }

            event.consume();
        });

        // The user successfully dragged a letter on the lettersReceiver
        lettersReceiver.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasContent(LetterInterface.DATA_FORMAT)) {
                success = true;

                if (allowOneLetterOnly) {
                    lettersReceiver.setOnDragOver(null);
                    lettersReceiver.setOnDragDropped(null);
                }

                ((Node) event.getGestureSource()).setOnDragDetected(null);

                onLetterDragDone.manipulateLetter((LetterInterface) db.getContent(LetterInterface.DATA_FORMAT), event);
            }

            event.setDropCompleted(success);
            event.consume();
        });
    }
}
