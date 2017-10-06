package istv.chrisanc.scrabble.controllers;

import istv.chrisanc.scrabble.exceptions.utils.GameSaver.UnableToWriteSaveException;
import istv.chrisanc.scrabble.model.GameSave;
import istv.chrisanc.scrabble.model.interfaces.GameSaveInterface;
import istv.chrisanc.scrabble.utils.GameSaver;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

/**
 * This controller manages the saving of a game during the player is playing. It is triggered once the user asks the program
 * to save the game.
 *
 * @author Christopher Anciaux
 */
public class SaveGameController extends BaseController {
    /**
     * The game save file name that has to be written in the saves directory
     */
    @FXML
    protected TextField fileNameField;

    /**
     * The complete path to the file that has to be written. This variable is used only if the user didn't want to save
     * his file in the saves directory
     */
    protected File externalFile;

    /**
     * The stage containing the dialog
     */
    protected Stage dialogStage;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    /**
     * Opens the dialog to choose an external file to store the game
     */
    @FXML
    protected void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(this.scrabble.getI18nMessages().getString("ScrabbleSaves"), "*" + GameSaver.GAME_SAVES_FILES_EXTENSION);
        fileChooser.getExtensionFilters().add(extFilter);

        this.externalFile = fileChooser.showSaveDialog(this.dialogStage);
    }

    /**
     * Handles the game saving, as the user confirmed his choice
     */
    @FXML
    protected void handleOk() {
        GameSaveInterface gameSave = new GameSave(this.scrabble.getBoard(), this.scrabble.getPlayers(), this.scrabble.getBag());

        try {
            // If the user entered a filename
            if (!this.fileNameField.getText().isEmpty()) {
                GameSaver.saveGameToGameSavesDirectory(gameSave, this.fileNameField.getText());
            }

            // If the user chose to save the game to an external file
            if (null != this.externalFile) {
                GameSaver.saveGame(gameSave, this.externalFile);
            }

            this.closeSavingDialog();
        } catch (UnableToWriteSaveException e) {
            this.showSavingErrorAlert(e);
        }
    }

    /**
     * Cancels the game saving and close the dialog
     */
    @FXML
    protected void handleCancel() {
        this.closeSavingDialog();
    }

    /**
     * Closes the dialog
     */
    protected void closeSavingDialog() {
       this.dialogStage.close();
    }

    /**
     * Displays an error if an error occurred during game saving
     *
     * @param e The exception to display
     */
    protected void showSavingErrorAlert(UnableToWriteSaveException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(this.scrabble.getI18nMessages().getString("error"));
        alert.setHeaderText(this.scrabble.getI18nMessages().getString("errorWhileSaving"));
        alert.setContentText(this.scrabble.getI18nMessages().getString(e.getMessage()));

        alert.showAndWait();
    }
}
