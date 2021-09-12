package istv.chrisanc.scrabble.controllers;

import istv.chrisanc.scrabble.Scrabble;
import istv.chrisanc.scrabble.model.ArtificialIntelligencePlayer;
import istv.chrisanc.scrabble.model.HumanPlayer;
import istv.chrisanc.scrabble.model.interfaces.ArtificialIntelligencePlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.languages.English.English;
import istv.chrisanc.scrabble.model.languages.French.French;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
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
 * This controller handles the creation of a new Scrabble game. It asks the player for the number of players, the language
 * of the game and more, to start a new game.
 *
 * @author Christopher Anciaux
 * @author Abdessamade Bouaggad
 */
public class NewGameController extends BaseController {
    /**
     * The default number of players
     */
    protected final static Integer DEFAULT_NUMBER_OF_PLAYERS = 2;

    /**
     * The maximum number of characters in player's name
     */
    protected final static short PLAYER_NAME_MAX_CHARACTERS = 20;

    /**
     * The ChoiceBox allowing users to choice a language to start the game
     */
    @FXML
    protected ChoiceBox<LanguageInterface> languageChoiceBox;

    /**
     * The TextField allowing user to change the number of players
     */
    @FXML
    protected TextField numberPlayersField;

    /**
     * Element containing all the information about each player
     */
    @FXML
    protected VBox playersInformationVBox;

    /**
     * Number of players who will play the game
     */
    protected int numberPlayers;

    /**
     * Initializes the interface
     */
    public void initializeInterface() {
        this.initializeLanguageChoiceBox();
        this.initializeNumberPlayersField();
    }

    /**
     * Initializes the language ChoiceBox, retrieving all the available languages
     */
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

    /**
     * Initializes the field of number of players, setting the default value and the rules that the inputted value must respect
     */
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

        if (!this.numberOfPlayersMatchesScrabbleRules() || !this.numberOfHumanPlayerMatchesTheRules()) {
            this.showErrorInvalidPlayersNumber();

            return;
        }

        for (int i = 0; i < this.numberPlayers; i++) {
            HBox playerInformationHBox = (HBox) this.playersInformationVBox.getChildren().get(i);
            String playerName = ((TextField) playerInformationHBox.getChildren().get(0)).getText().trim().isEmpty() ? this.scrabble.getI18nMessages().getString("player") + " " + (i + 1) : ((TextField) playerInformationHBox.getChildren().get(0)).getText();
            boolean playerIsHuman = ((CheckBox) playerInformationHBox.getChildren().get(1)).isSelected();

            if (playerIsHuman) {
                players.add(new HumanPlayer(playerName));
            } else {
                //noinspection unchecked
                players.add(new ArtificialIntelligencePlayer(playerName, ((ChoiceBox<Short>) playerInformationHBox.getChildren().get(2)).getValue()));
            }
        }

        this.scrabble.initializeScrabbleGame(this.languageChoiceBox.getValue(), players);
        this.scrabble.showGame();
    }

    /**
     * Redirects to Home action
     */
    @FXML
    protected void handleCancel() {
        this.scrabble.showHome();
    }

    protected void showErrorInvalidPlayersNumber() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(this.scrabble.getI18nMessages().getString("error"));
        alert.setHeaderText(this.scrabble.getI18nMessages().getString("incorrectNumberOfPlayers"));
        alert.setContentText(this.scrabble.getI18nMessages().getString("numberOfPlayersMustFitRules"));

        alert.showAndWait();
    }

    protected boolean numberOfPlayersMatchesScrabbleRules() {
        return Scrabble.MIN_PLAYERS <= this.numberPlayers && this.numberPlayers <= Scrabble.MAX_PLAYERS;
    }

    protected boolean numberOfHumanPlayerMatchesTheRules() {
        int numberOfHumanPlayers = 0;

        for (int i = 0; i < this.numberPlayers; i++) {
            HBox playerInformationHBox = (HBox) this.playersInformationVBox.getChildren().get(i);

            if (((CheckBox) playerInformationHBox.getChildren().get(1)).isSelected()) {
                numberOfHumanPlayers++;
            }
        }

        return numberOfHumanPlayers >= 1;
    }

    /**
     * Updates the form of players information, according to the number of players inputted
     */
    protected void updatePlayersForm() {
        // If the user entered a smaller number of players than the previous value
        if (this.playersInformationVBox.getChildren().size() > this.numberPlayers) {
            this.playersInformationVBox.getChildren().subList(this.numberPlayers, this.playersInformationVBox.getChildren().size()).clear();

            return;
        }

        // If the user entered a greater number of players than the previous value
        for (int i = this.playersInformationVBox.getChildren().size(); i < this.numberPlayers; i++) {
            this.playersInformationVBox.getChildren().add(this.getPlayerInformationForm(i));
        }
    }

    /**
     * Creates and returns the created form containing the fields about a player
     *
     * @param i The number of the player
     *
     * @return the form containing the fields that can be inputted about the player
     */
    protected HBox getPlayerInformationForm(int i) {
        ResourceBundle i18nMessages = this.scrabble.getI18nMessages();

        HBox playerInformationHBox = new HBox();
        playerInformationHBox.getStyleClass().add("player");

        TextField playerNameTextField = new TextField(this.scrabble.getI18nMessages().getString("player") + " " + (i + 1));
        playerNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() > NewGameController.PLAYER_NAME_MAX_CHARACTERS) {
                playerNameTextField.setText(newValue.substring(0, NewGameController.PLAYER_NAME_MAX_CHARACTERS));
            }
        });

        CheckBox playerIsHumanCheckBox = new CheckBox(this.scrabble.getI18nMessages().getString("isPlayerHuman"));

        ChoiceBox<Short> levelChoiceBox = new ChoiceBox<>();
        List<Short> levels = Arrays.asList(ArtificialIntelligencePlayerInterface.LEVEL_VERY_EASY,
                ArtificialIntelligencePlayerInterface.LEVEL_EASY,
                ArtificialIntelligencePlayerInterface.LEVEL_MEDIUM,
                ArtificialIntelligencePlayerInterface.LEVEL_HARD,
                ArtificialIntelligencePlayerInterface.LEVEL_VERY_HARD);

        levelChoiceBox.getItems().addAll(levels);
        levelChoiceBox.setValue(levels.get(2));
        levelChoiceBox.setConverter(new StringConverter<Short>() {
            @Override
            public String toString(Short object) {
                return i18nMessages.getString("artificialIntelligenceLevels." + object.toString());
            }

            @Override
            public Short fromString(String string) {
                return null;
            }
        });

        levelChoiceBox.disableProperty().bind(playerIsHumanCheckBox.selectedProperty());

        playerInformationHBox.getChildren().addAll(playerNameTextField, playerIsHumanCheckBox, levelChoiceBox);

        return playerInformationHBox;
    }
}
