package istv.chrisanc.scrabble.controllers;

import istv.chrisanc.scrabble.exceptions.utils.GameSaver.UnableToLoadSaveException;
import istv.chrisanc.scrabble.model.interfaces.GameSaveInterface;
import istv.chrisanc.scrabble.utils.GameSaver;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

/**
 * @author Christopher Anciaux
 */
public class LoadGameController extends BaseController {
    /**
     * The ListView showing the files available in the game saves directory
     */
    @FXML
    protected ListView<File> filesList;

    @FXML
    protected Text noGameSaveFoundText;

    /**
     * The files found in the game saves directory
     */
    protected File[] gameSaves;

    /**
     * Initializes the controller, constructs the ListView and listen for any user choice
     */
    @FXML
    protected void initialize() {
        try {
            this.gameSaves = GameSaver.findGameSaves();
        } catch (IOException e) {
            showErrorWhileLoadingGameSaves();
        }


        if (this.gameSaves.length > 0) {
            this.filesList.setItems(FXCollections.observableArrayList(this.gameSaves));
        } else {
            this.filesList.setVisible(false);
            this.filesList.setManaged(false);
            this.noGameSaveFoundText.setVisible(true);
        }

        this.filesList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> this.handleLoadGame(newValue));
    }

    /**
     * Loads the game from the chosen file
     *
     * @param gameSaveFile The File to use to load the game
     */
    @FXML
    protected void handleLoadGame(File gameSaveFile) {
        try {
            GameSaveInterface gameSave = GameSaver.loadGameSave(gameSaveFile);

            this.scrabble.resumeGameFromSaveAndShowGame(gameSave);

        } catch (UnableToLoadSaveException e) {
            this.showErrorWhileLoading(e);
        }
    }

    /**
     * Opens a file choosing dialog and if the user selects a file, uses it to load a game from this file.
     */
    @FXML
    protected void handleChooseFile() {
        FileChooser fileChooser = new FileChooser();

        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(this.scrabble.getI18nMessages().getString("ScrabbleSaves"), "*" + GameSaver.GAME_SAVES_FILES_EXTENSION);
        fileChooser.getExtensionFilters().add(extFilter);

        // Show file chooser
        File file = fileChooser.showOpenDialog(this.scrabble.getPrimaryStage());

        // If no file was selected, there is nothing to do
        if (null == file) {
            return;
        }

        // If a file has been selected, we use it to load the game
        this.handleLoadGame(file);
    }

    /**
     * Goes back to home page of the application
     */
    @FXML
    protected void handleQuit() {
        this.scrabble.showHome();
    }

    /**
     * Shows an alert indicating that the game couldn't be loaded from the file
     */
    protected void showErrorWhileLoading(UnableToLoadSaveException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(this.scrabble.getI18nMessages().getString("error"));
        alert.setHeaderText(this.scrabble.getI18nMessages().getString("errorWhileLoading"));
        alert.setContentText(this.scrabble.getI18nMessages().getString(e.getMessage()));

        alert.showAndWait();
    }

    /**
     * Shows an alert indicating that the game saves couldn't be loaded
     */
    protected void showErrorWhileLoadingGameSaves() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(this.scrabble.getI18nMessages().getString("error"));
        alert.setHeaderText(this.scrabble.getI18nMessages().getString("errorWhileLoading"));
        alert.setContentText(this.scrabble.getI18nMessages().getString("errorWhileLoadingGameSavesList"));

        alert.showAndWait();
    }
}
