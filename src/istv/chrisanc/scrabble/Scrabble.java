package istv.chrisanc.scrabble;

import istv.chrisanc.scrabble.controllers.GameController;
import istv.chrisanc.scrabble.controllers.HomeController;
import istv.chrisanc.scrabble.controllers.LoadGameController;
import istv.chrisanc.scrabble.controllers.NewGameController;
import istv.chrisanc.scrabble.controllers.RootLayoutController;
import istv.chrisanc.scrabble.exceptions.InvalidPlayedTurnException;
import istv.chrisanc.scrabble.exceptions.NonExistentWordException;
import istv.chrisanc.scrabble.exceptions.model.Bag.EmptyBagException;
import istv.chrisanc.scrabble.exceptions.model.Bag.NotEnoughLettersException;
import istv.chrisanc.scrabble.exceptions.utils.dictionaries.ErrorLoadingDictionaryException;
import istv.chrisanc.scrabble.model.Bag;
import istv.chrisanc.scrabble.model.Board;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.GameSaveInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;
import istv.chrisanc.scrabble.model.letters.Joker;
import istv.chrisanc.scrabble.utils.LetterToStringTransformer;
import istv.chrisanc.scrabble.utils.PlayedWordsValidityManager;
import istv.chrisanc.scrabble.utils.ScoreManager;
import istv.chrisanc.scrabble.utils.dictionaries.DictionaryFactory;
import istv.chrisanc.scrabble.utils.interfaces.DictionaryInterface;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.SortedMap;

/**
 * This class represents the "main" class of the ScrabbleGame. It starts, manages and ends the game.
 *
 * @author Christopher Anciaux
 */
public class Scrabble extends Application {
    /**
     * The maximum number of turns that each user can skip
     */
    protected static final short MAX_SKIPPED_TURNS_PER_USER = 3;

    protected ResourceBundle i18nMessages;

    protected Stage primaryStage;

    protected BorderPane rootLayout;

    protected DictionaryInterface dictionary;

    protected BoardInterface board;

    protected List<PlayerInterface> players;

    protected SimpleObjectProperty<PlayerInterface> currentPlayer;

    protected BagInterface bag;

    /**
     * The number of consecutive turns skipped by all the players (without distinction)
     */
    protected short consecutiveTurnsSkipped = 0;

    public static void main(String[] args) {
        launch(args);
    }

    public ResourceBundle getI18nMessages() {
        return i18nMessages;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    @Override
    public void start(Stage primaryStage) {
        this.i18nMessages = ResourceBundle.getBundle("translations/MessagesBundle");

        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(this.i18nMessages.getString("Scrabble"));
        this.primaryStage.getIcons().add(new Image("file:resources/icon/icon.png"));

        this.initializeRootLayout();
        this.showHome();
    }

    /**
     * Shows the Homepage of the Scrabble, the page allowing the player to choose between "New Game", "Load Game" etc
     */
    public void showHome() {
        try {
            // Load home
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(this.i18nMessages);
            loader.setLocation(Scrabble.class.getResource("view/Home.fxml"));
            VBox home = loader.load();

            // Set home into the center of the root layout
            rootLayout.setCenter(home);

            HomeController controller = loader.getController();
            controller.setScrabble(this);
        } catch (IOException e) {
            this.showGeneralApplicationError(e);
        }
    }

    /**
     * Shows the NewGame wizard, allowing the player to enter his name, the number of players etc
     */
    public void showNewGame() {
        // TODO @Bouaggad Abdessamade
        try {
            // Load game
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(this.i18nMessages);
            loader.setLocation(Scrabble.class.getResource("view/NewGame.fxml"));
            AnchorPane game = loader.load();

            // Set home into the center of the root layout
            rootLayout.setCenter(game);

            NewGameController controller = loader.getController();
            controller.setScrabble(this);
        } catch (IOException e) {
            this.showGeneralApplicationError(e);
        }

    }

    /**
     * Shows the LoadGame wizard, allowing the player to load an externally saved game or choose one save between the stored ones
     */
    public void showLoadGame() {
        try {
            // Load home
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(this.i18nMessages);
            loader.setLocation(Scrabble.class.getResource("view/LoadGame.fxml"));
            AnchorPane loadGame = loader.load();

            // Set home into the center of the root layout
            rootLayout.setCenter(loadGame);

            LoadGameController controller = loader.getController();
            controller.setScrabble(this);
        } catch (IOException e) {
            this.showGeneralApplicationError(e);
        }
    }

    /**
     * Shows the Game, that is to say the Scrabble Game itself. It opens the main controller, dealing with all the Scrabble logic
     */
    public void showGame() {
        try {
            // Load game
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(this.i18nMessages);
            loader.setLocation(Scrabble.class.getResource("view/Game.fxml"));
            BorderPane game = loader.load();

            // Set home into the center of the root layout
            rootLayout.setCenter(game);

            GameController controller = loader.getController();
            controller.setScrabble(this);
            controller.initializeInterface();
        } catch (IOException e) {
            this.showGeneralApplicationError(e);
        }
    }

    /**
     * Resumes a game, according to the data present in the {@link GameSaveInterface}.
     *
     * @param gameSave The {@link GameSaveInterface} to use to construct the Scrabble and resume the game
     */
    public void resumeGameFromSaveAndShowGame(GameSaveInterface gameSave) {
        try {
            DictionaryFactory dictionaryFactory = new DictionaryFactory();
            DictionaryInterface dictionary = dictionaryFactory.getDictionary(gameSave.getDictionaryIdentifier());

            this.initializeScrabbleGame(dictionary, gameSave.getPlayers(), gameSave.getCurrentPlayer(), gameSave.getBag(), gameSave.getBoard());

            this.showGame();
        } catch (ErrorLoadingDictionaryException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the EndGame, that is to say the message congratulating the player or encouraging him to retry wby playing a new game.
     */
    public void showEndGame() {
        // TODO @Bouaggad Abdessamade
    }

    /**
     * Shows an alert when a general / no handled error happens in the application
     */
    public void showGeneralApplicationError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(this.getI18nMessages().getString("error"));
        alert.setHeaderText(this.getI18nMessages().getString("generalError"));
        alert.setContentText(this.getI18nMessages().getString(e.getMessage()));

        alert.showAndWait();
    }

    /**
     * Shows an alert when a letter's drawing happens
     *
     * @param e EmptyBagException The exception to be managed
     */
    public void showErrorDrawingLetterFromBagAlert(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(this.getI18nMessages().getString("error"));
        alert.setHeaderText(this.getI18nMessages().getString("errorWhileDrawingLetterFromBag"));
        alert.setContentText(this.getI18nMessages().getString(e.getMessage()));

        alert.showAndWait();
    }

    /**
     * Shows a dialog to ask the user to select the represented letter by his played joker
     *
     * @return the choice of the user, false if he clicked on cancel button
     */
    public Optional<String> showRepresentedLetterByJokerSelectorDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(this.getI18nMessages().getString("chooseLetterRepresentedByJoker"));
        dialog.setHeaderText(this.getI18nMessages().getString("chooseLetterRepresentedByJokerInformation"));
        dialog.setContentText(this.getI18nMessages().getString("chooseLetterRepresentedByJokerLabel"));

        return dialog.showAndWait();
    }

    /**
     * Plays the given letters on the board, after checking all the placed letters respect the Scrabble rules
     *
     * @param playedLetters All the letters of the board, with the new letters placed by the user
     */
    public void playLetters(SortedMap<GameController.BoardPosition, LetterInterface> playedLetters) throws InvalidPlayedTurnException, NonExistentWordException {
        if (!PlayedWordsValidityManager.playedWordsAreValid(this.board, playedLetters)) {
            throw new InvalidPlayedTurnException();
        }

        for(LetterInterface playedLetter : playedLetters.values()) {
            if (!(playedLetter instanceof Joker)) {
                continue;
            }

            Optional<String> representedLetterString = this.showRepresentedLetterByJokerSelectorDialog();

            try {
                // noinspection OptionalGetWithoutIsPresent
                ((Joker) playedLetter).setRepresentedLetter(LetterToStringTransformer.reverseTransform(representedLetterString.get().toUpperCase()));
            } catch (Exception e) {
                throw new InvalidPlayedTurnException();
            }
        }

        List<WordInterface> playedWords = PlayedWordsValidityManager.findPlayedWords(this.board, playedLetters, this.getCurrentPlayer());

        this.board.addLetters(playedLetters);
        this.board.addWords(playedWords);

        this.getCurrentPlayer().increaseScore(ScoreManager.getTurnScore(playedLetters.values(), playedWords, this.getBoard()));
        this.getCurrentPlayer().removeLetters(playedLetters.values());
        this.giveLettersToCurrentPlayerToFillHisRack();

        this.nextTurn();
    }

    /**
     * Handles the logic of letters exchanging
     *
     * @param letters The letters to be exchanged
     *
     * @throws EmptyBagException if the bag is empty
     */
    public void exchangeLetters(List<LetterInterface> letters) throws EmptyBagException, NotEnoughLettersException {
        this.getCurrentPlayer().removeLetters(letters);
        this.getCurrentPlayer().addLetters(this.getBag().exchangeLetters(letters));

        this.nextTurn();
    }

    /**
     * Skips the turn
     */
    public void skipTurn() {
        this.consecutiveTurnsSkipped++;
        // TODO @Bouaggad Abdessamade
    }

    /**
     * Goes to the next turn of the game
     */
    public void nextTurn() {
        if (this.gameIsFinished()) {
            this.showEndGame();

            return;
        }

        int currentPlayerIndex = this.getPlayers().indexOf(this.getCurrentPlayer()) + 1;

        if (this.players.size() == currentPlayerIndex) {
            currentPlayerIndex = 0;
        }

        this.currentPlayer.setValue(this.getPlayers().get(currentPlayerIndex));

        if (!this.getCurrentPlayer().isHuman()) {
            // TODO: ask IA to play
        }
    }

    /**
     * Gives letters to the user to fill his rack
     */
    protected void giveLettersToCurrentPlayerToFillHisRack() {
        int numberOfLettersToDraw = PlayerInterface.BASE_NUMBER_OF_LETTERS - this.getCurrentPlayer().getLetters().size();

        for (int i = 0; i < numberOfLettersToDraw; i++) {
            try {
                this.getCurrentPlayer().addLetter(this.getBag().drawLetter());
            } catch (EmptyBagException e) {
                // The bag is empty, so we can't draw any letter anymore
                break;
            }
        }
    }

    /**
     * Tells if the game is finished or not, according to the Scrabble rules
     *
     * @return true if the game is finished, false otherwise
     */
    protected boolean gameIsFinished() {
        // If the current player has no more letter and the bag is empty
        if (this.getCurrentPlayer().getLetters().isEmpty() && this.getBag().isEmpty()) {
            int pointsSumToGiveToCurrentPlayer = 0;

            for (PlayerInterface player : this.getPlayers()) {
                if (this.getCurrentPlayer() != player) {
                    // We subtract the values of the letters of all other players to their respective score
                    for (LetterInterface letter: player.getLetters()) {
                        player.decreaseScore(letter.getValue());
                        pointsSumToGiveToCurrentPlayer += letter.getValue();
                    }
                }

                // We add all the points of the other players to the current player
                this.getCurrentPlayer().increaseScore(pointsSumToGiveToCurrentPlayer);
            }

            return true;
        }

        // If the players skipped their turns too many times, the game stops
        if (this.getPlayers().size() * Scrabble.MAX_SKIPPED_TURNS_PER_USER <= this.consecutiveTurnsSkipped) {
            // We subtract the letters values of each player to its total points
            for (PlayerInterface player : this.players) {
                for (LetterInterface letter : player.getLetters()) {
                    player.decreaseScore(letter.getValue());
                }
            }

            return true;
        }

        return false;
    }

    /**
     * Initializes the root layout and tries to load the last opened
     * person file
     */
    protected void initializeRootLayout() {
        try {
            // Load root layout from fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(this.i18nMessages);
            loader.setLocation(Scrabble.class.getResource("view/RootLayout.fxml"));
            this.rootLayout = loader.load();

            // Show the scene containing the root layout
            Scene scene = new Scene(this.rootLayout);
            primaryStage.setScene(scene);

            RootLayoutController controller = loader.getController();
            controller.setScrabble(this);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see #initializeScrabbleGame(DictionaryInterface, List, PlayerInterface, BagInterface, BoardInterface)
     */
    protected void initializeScrabbleGame(DictionaryInterface dictionary, List<PlayerInterface> players, PlayerInterface currentPlayer) {
        this.initializeScrabbleGame(dictionary, players, currentPlayer, new Bag(), new Board());
    }

    /**
     * Initializes the ScrabbleGame with all the needed information
     *
     * @param dictionary The dictionary to be used during the game
     * @param players    The players
     * @param bag        The bag
     * @param board      The board
     */
    protected void initializeScrabbleGame(DictionaryInterface dictionary, List<PlayerInterface> players, PlayerInterface currentPlayer, BagInterface bag, BoardInterface board) {
        this.dictionary = dictionary;
        this.board = board;
        this.players = players;
        this.currentPlayer = new SimpleObjectProperty<>(currentPlayer);
        this.bag = bag;
    }

    public DictionaryInterface getDictionary() {
        return dictionary;
    }

    public BoardInterface getBoard() {
        return this.board;
    }

    public List<PlayerInterface> getPlayers() {
        return this.players;
    }

    /**
     * @return The {@link PlayerInterface} who should play this turn
     */
    public PlayerInterface getCurrentPlayer() {
        return this.currentPlayer.get();
    }

    public SimpleObjectProperty<PlayerInterface> currentPlayerProperty() {
        return this.currentPlayer;
    }

    public BagInterface getBag() {
        return this.bag;
    }
}

