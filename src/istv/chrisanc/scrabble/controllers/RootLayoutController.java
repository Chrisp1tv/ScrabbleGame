package istv.chrisanc.scrabble.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;

/**
 * This controller manages the root layout of the Application, that is to say the About action, and the main graphic container.
 *
 * @author Christopher Anciaux
 */
public class RootLayoutController extends BaseController {
    /**
     * Opens an about dialog
     */
    @FXML
    protected void handleAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(this.scrabble.getI18nMessages().getString("Scrabble"));
        alert.setHeaderText(this.scrabble.getI18nMessages().getString("aboutHeaderText"));
        alert.setContentText(this.scrabble.getI18nMessages().getString("aboutContentText"));

        alert.showAndWait();
    }

    /**
     * Closes the application.
     */
    @FXML
    protected void handleExit() {
        System.exit(0);
    }
}