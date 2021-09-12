package istv.chrisanc.scrabble;

import istv.chrisanc.scrabble.controllers.BaseController;
import istv.chrisanc.scrabble.controllers.GameController;
import istv.chrisanc.scrabble.controllers.GameEndedController;
import istv.chrisanc.scrabble.controllers.HomeController;
import istv.chrisanc.scrabble.controllers.LoadGameController;
import istv.chrisanc.scrabble.controllers.NewGameController;
import istv.chrisanc.scrabble.controllers.RootLayoutController;
import istv.chrisanc.scrabble.exceptions.InvalidPlayedTurnException;
import istv.chrisanc.scrabble.exceptions.model.Bag.EmptyBagException;
import istv.chrisanc.scrabble.exceptions.model.Bag.NotEnoughLettersException;
import istv.chrisanc.scrabble.exceptions.utils.dictionaries.ErrorLoadingDictionaryException;
import istv.chrisanc.scrabble.model.Bag;
import istv.chrisanc.scrabble.model.Board;
import istv.chrisanc.scrabble.model.BoardPosition;
import istv.chrisanc.scrabble.model.interfaces.ArtificialIntelligencePlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.GameSaveInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.WordInterface;
import istv.chrisanc.scrabble.model.languages.Global.letters.Joker;
import istv.chrisanc.scrabble.utils.ArtificialIntelligenceHelper;
import istv.chrisanc.scrabble.utils.LetterToStringTransformer;
import istv.chrisanc.scrabble.utils.PlayedTurnValidityChecker;
import istv.chrisanc.scrabble.utils.ScoreManager;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * This class represents the "main" class of the ScrabbleGame. It starts, manages and ends the game.
 *
 * @author Christopher Anciaux
 */
public class Scrabble extends Application {
    /**
     * The minimal number of players of a Scrabble game
     */
    public static final short MIN_PLAYERS = 2;

    /**
     * The maximal number of players of a Scrabble game
     */
    public static final short MAX_PLAYERS = 4;

    /**
     * The maximum number of turns that each user can skip
     */
    protected static final short MAX_SKIPPED_TURNS_PER_USER = 3;

    /**
     * The translated messages in the current locale
     */
    protected ResourceBundle i18nMessages;

    /**
     * The primary stage
     */
    protected Stage primaryStage;

    /**
     * The root layout, handling About action and general interface
     */
    protected BorderPane rootLayout;

    /**
     * The language of the current Scrabble game
     */
    protected LanguageInterface language;

    /**
     * The board of the current Scrabble game
     */
    protected BoardInterface board;

    /**
     * The players of the current Scrabble game
     */
    protected List<PlayerInterface> players;

    /**
     * The current player
     */
    protected SimpleObjectProperty<PlayerInterface> currentPlayer;

    /**
     * The bag of the current Scrabble game
     */
    protected BagInterface bag;

    /**
     * The number of consecutive turns skipped by all the players (without distinction)
     */
    protected short consecutiveTurnsSkipped = 0;

    public static void main(String[] args) {
        Application.launch(args);
    }

    public ResourceBundle getI18nMessages() {
        return this.i18nMessages;
    }

    public Stage getPrimaryStage() {
        return this.primaryStage;
    }

    @Override
    public void start(Stage primaryStage) {
        this.i18nMessages = ResourceBundle.getBundle("translations/MessagesBundle");
        this.primaryStage = primaryStage;

        this.primaryStage.setMinWidth(1168);
        this.primaryStage.setMinHeight(686);

        this.primaryStage.setTitle(this.i18nMessages.getString("Scrabble"));
        this.primaryStage.getIcons().add(new Image("file:resources/icon/icon.png"));

        this.initializeRootLayout();
        this.showHome();
    }

    /**
     * Shows the Homepage of the Scrabble, the page allowing the player to choose between "New Game", "Load Game" etc
     */
    public void showHome() {
        HomeController homeController = (HomeController) this.getControllerByView("view/Home.fxml");

        homeController.setScrabble(this);
    }

    /**
     * Shows the NewGame wizard, allowing the player to enter his name, the number of players etc
     */
    public void showNewGame() {
        NewGameController newGameController = (NewGameController) this.getControllerByView("view/NewGame.fxml");

        newGameController.setScrabble(this);
        newGameController.initializeInterface();
    }

    /**
     * Shows the LoadGame wizard, allowing the player to load an externally saved game or choose one save between the stored ones
     */
    public void showLoadGame() {
        LoadGameController loadGameController = (LoadGameController) this.getControllerByView("view/LoadGame.fxml");

        loadGameController.setScrabble(this);
    }

    /**
     * Shows the Game, that is to say the Scrabble Game itself. It opens the main controller, dealing with all the Scrabble logic
     */
    public void showGame() {
        GameController gameController = (GameController) this.getControllerByView("view/Game.fxml");

        gameController.setScrabble(this);
        gameController.initializeInterface();
    }

    /**
     * Shows the EndGame, that is to say the message congratulating the player or encouraging him to retry wby playing a new game.
     */
    public void showEndGame() {
        GameEndedController gameEndedController = (GameEndedController) this.getControllerByView("view/GameEnded.fxml");

        gameEndedController.setScrabble(this);
        gameEndedController.initializeInterface();
    }

    public BaseController getControllerByView(String viewName) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setResources(this.i18nMessages);
            loader.setLocation(Scrabble.class.getResource(viewName));
            Node view = loader.load();

            this.rootLayout.setCenter(view);

            return loader.getController();
        } catch (IOException e) {
            this.showGeneralApplicationError(e);

            return null;
        }
    }

    /**
     * Resumes a game, according to the data present in the {@link GameSaveInterface}.
     *
     * @param gameSave The {@link GameSaveInterface} to use to construct the Scrabble and resume the game
     */
    public void resumeGameFromSaveAndShowGame(GameSaveInterface gameSave) {
        try {
            this.initializeScrabbleGame(gameSave.getLanguage(), gameSave.getPlayers(), gameSave.getCurrentPlayer(), gameSave.getBag(), gameSave.getBoard());

            this.showGame();
        } catch (ErrorLoadingDictionaryException e) {
            e.printStackTrace();
        }
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
    public void playLetters(SortedMap<BoardPosition, LetterInterface> playedLetters) throws InvalidPlayedTurnException {
        for (LetterInterface playedLetter : playedLetters.values()) {
            if (!(playedLetter instanceof Joker)) {
                continue;
            }

            Optional<String> representedLetterString = this.showRepresentedLetterByJokerSelectorDialog();

            try {
                // noinspection OptionalGetWithoutIsPresent
                ((Joker) playedLetter).setRepresentedLetter(LetterToStringTransformer.reverseTransform(representedLetterString.get().toUpperCase(), this.getLanguage()));
            } catch (Exception e) {
                throw new InvalidPlayedTurnException("exceptions.invalidPlayedTurn.jokerValueNonExistent");
            }
        }

        List<WordInterface> playedWords = PlayedTurnValidityChecker.findPlayedWords(this.getLanguage().getDictionary(), this.board, playedLetters, this.getCurrentPlayer());

        this.board.addLetters(playedLetters);
        this.board.addWords(playedWords);

        this.getCurrentPlayer().increaseScore(ScoreManager.getTurnScore(playedLetters.values(), playedWords, this.getBoard()));
        this.getCurrentPlayer().removeLetters(playedLetters.values());
        this.giveLettersToCurrentPlayerToFillHisRack();

        this.reinitializeSkippedTurns();
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
        this.getCurrentPlayer().addLetters(this.getBag().exchangeLetters(letters));
        this.getCurrentPlayer().removeLetters(letters);

        this.reinitializeSkippedTurns();
        this.nextTurn();
    }

    /**
     * Skips the turn
     */
    public void skipTurn() {
        this.increaseNumberOfSkippedTurns();
        this.nextTurn();
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
        this.checkIfArtificialIntelligenceShouldPlay();
    }

    /**
     * Determines the player who should be play first, according to the Scrabble rules
     *
     * @param bag     The bag of the Scrabble game
     * @param players The players of the Scrabble game
     *
     * @return the player who should play first
     * @throws EmptyBagException if the bag is empty
     */
    protected PlayerInterface determineFirstPlayer(BagInterface bag, List<PlayerInterface> players) throws EmptyBagException {
        NavigableMap<LetterInterface, PlayerInterface> playersDrawing = new TreeMap<>((o1, o2) -> o1.toString().compareTo(o2.toString()));

        for (PlayerInterface player : players) {
            playersDrawing.put(bag.drawLetter(), player);
        }

        for (NavigableMap.Entry<LetterInterface, PlayerInterface> playerDrawing : playersDrawing.entrySet()) {
            bag.putBackLetter(playerDrawing.getKey());
        }

        return playersDrawing.firstEntry().getValue();
    }

    /**
     * Distributes all the letters to each player
     *
     * @param bag     The bag of the Scrabble game
     * @param players The players of the Scrabble game
     *
     * @throws EmptyBagException if the bag is empty
     */
    protected void distributeLettersToAllPlayers(BagInterface bag, List<PlayerInterface> players) throws EmptyBagException {
        for (PlayerInterface player : players) {
            for (int i = 0; i < PlayerInterface.BASE_NUMBER_OF_LETTERS; i++) {
                player.addLetter(bag.drawLetter());
            }
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
            ScoreManager.updateScoreOnceBagIsEmptyAndOnePlayerUsedAllHisLetters(this.getPlayers(), this.getCurrentPlayer());

            return true;
        }

        // If the players skipped their turns too many times, the game stops
        if (this.getPlayers().size() * Scrabble.MAX_SKIPPED_TURNS_PER_USER <= this.consecutiveTurnsSkipped) {
            ScoreManager.updateScoreAfterPlayersSkippedTheirTurnsTooManyTimes(this.players);

            return true;
        }

        return false;
    }

    protected void checkIfArtificialIntelligenceShouldPlay() {
        if (this.getCurrentPlayer() instanceof ArtificialIntelligencePlayerInterface) {
            ArtificialIntelligenceHelper.playTurn(this, (ArtificialIntelligencePlayerInterface) this.getCurrentPlayer());
        }
    }

    /**
     * Initializes the root layout
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
            this.primaryStage.setScene(scene);

            RootLayoutController controller = loader.getController();
            controller.setScrabble(this);

            this.primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see #initializeScrabbleGame(LanguageInterface, List, PlayerInterface, BagInterface, BoardInterface)
     */
    public void initializeScrabbleGame(LanguageInterface language, List<PlayerInterface> players) {
        try {
            BagInterface bag = new Bag(language.getBagLettersDistribution());
            PlayerInterface firstPlayer = this.determineFirstPlayer(bag, players);

            this.distributeLettersToAllPlayers(bag, players);
            this.initializeScrabbleGame(language, players, firstPlayer, bag, new Board());
        } catch (EmptyBagException e) {
            this.showGeneralApplicationError(e);
        }
    }

    /**
     * Initializes the ScrabbleGame with all the needed information
     *
     * @param language The language to be used during the game
     * @param players  The players
     * @param bag      The bag
     * @param board    The board
     */
    protected void initializeScrabbleGame(LanguageInterface language, List<PlayerInterface> players, PlayerInterface currentPlayer, BagInterface bag, BoardInterface board) {
        this.consecutiveTurnsSkipped = 0;
        this.language = language;
        this.board = board;
        this.players = new ArrayList<>(players);
        this.currentPlayer = new SimpleObjectProperty<>(currentPlayer);
        this.bag = bag;
        this.checkIfArtificialIntelligenceShouldPlay();
    }

    protected void increaseNumberOfSkippedTurns() {
        this.consecutiveTurnsSkipped++;
    }

    protected void reinitializeSkippedTurns() {
        this.consecutiveTurnsSkipped = 0;
    }

    public LanguageInterface getLanguage() {
        return this.language;
    }

    public BoardInterface getBoard() {
        return this.board;
    }

    public List<PlayerInterface> getPlayers() {
        return this.players;
    }

    /**
     * @return The player who should play this turn
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

    public short getConsecutiveTurnsSkipped() {
        return this.consecutiveTurnsSkipped;
    }
}

