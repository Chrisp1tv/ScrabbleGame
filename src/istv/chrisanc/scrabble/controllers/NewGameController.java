package istv.chrisanc.scrabble.controllers;

import istv.chrisanc.scrabble.Scrabble;
import istv.chrisanc.scrabble.model.Player;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.languages.English.English;
import istv.chrisanc.scrabble.model.languages.French.French;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author Christopher Anciaux
 * @author Abdessamade Bouaggad
 */
public class NewGameController extends BaseController {
    protected final static Integer DEFAULT_NUMBER_OF_PLAYERS = 2;

    protected final static short PLAYER_NAME_MAX_CHARACTERS = 20;

    @FXML
    protected ChoiceBox<LanguageInterface> languageChoiceBox;

    @FXML
    protected TextField numberPlayersField;

    @FXML
    protected VBox playersInformationVBox;

    protected int numberPlayers;

    public void initializeInterface() {
        this.initializeLanguageChoiceBox();
        this.initializeNumberPlayersField();
    }

    protected void initializeLanguageChoiceBox() {
        ResourceBundle i18nMessages = this.scrabble.getI18nMessages();
        List<LanguageInterface> availableLanguages = Arrays.asList(new French(), new English());

        this.languageChoiceBox.getItems().addAll(availableLanguages);
        this.languageChoiceBox.setValue(availableLanguages.get(0));
        this.languageChoiceBox.setConverter(new StringConverter<LanguageInterface>() {
            @Override
            public String toString(LanguageInterface object) {
                return i18nMessages.getString(object.getName());
            }

            @Override
            public LanguageInterface fromString(String string) {
                return null;
            }
        });
    }

    protected void initializeNumberPlayersField() {
        this.numberPlayersField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                return;
            }

            if (!newValue.matches("\\d*")) {
                this.numberPlayersField.setText(newValue.replaceAll("[^\\d]", ""));

                return;
            }

            this.numberPlayers = Integer.valueOf(this.numberPlayersField.getText());
            if (this.numberOfPlayersMatchesScrabbleRules()) {
                this.updatePlayersForm();
            }
        });

        this.numberPlayersField.setText(NewGameController.DEFAULT_NUMBER_OF_PLAYERS.toString());
    }

	/**
	 * Initializes the game and redirects to the created game.
	 */
	@FXML
	protected void handleStartGame() {
        List<PlayerInterface> players = new ArrayList<>();

        for (int i = 0; i < this.numberPlayers; i++) {
            HBox playerInformationHBox = (HBox) this.playersInformationVBox.getChildren().get(i);
            String playerName = ((TextField) playerInformationHBox.getChildren().get(0)).getText();
            boolean playerIsHuman = ((CheckBox) playerInformationHBox.getChildren().get(1)).isSelected();

            players.add(new Player(playerName.trim().isEmpty() ? this.scrabble.getI18nMessages().getString("player") + " " + (i+1) : playerName, playerIsHuman));
        }

        this.scrabble.initializeScrabbleGame(this.languageChoiceBox.getValue(), players);
        this.scrabble.showGame();
	}

	/**
	 * Redirects to Home
	 */
	@FXML
	protected void handleCancel() {
		this.scrabble.showHome();
	}

	protected boolean numberOfPlayersMatchesScrabbleRules() {
        return Scrabble.MIN_PLAYERS <= this.numberPlayers && this.numberPlayers <= Scrabble.MAX_PLAYERS;
    }

    protected void updatePlayersForm() {
        if (this.playersInformationVBox.getChildren().size() > this.numberPlayers) {
            for (int i = this.numberPlayers, oldNumberPlayers = this.playersInformationVBox.getChildren().size(); i < oldNumberPlayers; i++) {
                this.playersInformationVBox.getChildren().remove(i);
            }

            return;
        }

        for (int i = this.playersInformationVBox.getChildren().size(); i < this.numberPlayers; i++) {
            this.playersInformationVBox.getChildren().add(this.getPlayerInformationForm(i));
        }
    }

    protected HBox getPlayerInformationForm(int i) {
        HBox playerInformationHBox = new HBox();
        playerInformationHBox.getStyleClass().add("player");

        TextField playerNameTextField = new TextField(this.scrabble.getI18nMessages().getString("player") + " " + (i+1));
        playerNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > NewGameController.PLAYER_NAME_MAX_CHARACTERS) {
                playerNameTextField.setText(newValue.substring(0, NewGameController.PLAYER_NAME_MAX_CHARACTERS));
            }
        });

        CheckBox playerIsHumanCheckBox = new CheckBox(this.scrabble.getI18nMessages().getString("isPlayerHuman"));

        playerInformationHBox.getChildren().addAll(playerNameTextField, playerIsHumanCheckBox);

        return playerInformationHBox;
    }
}
