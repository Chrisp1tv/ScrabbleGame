package istv.chrisanc.scrabble;

import istv.chrisanc.scrabble.controllers.GameController;
import istv.chrisanc.scrabble.controllers.HomeController;
import istv.chrisanc.scrabble.controllers.LoadGameController;
import istv.chrisanc.scrabble.controllers.NewGameController;
import istv.chrisanc.scrabble.controllers.RootLayoutController;
import istv.chrisanc.scrabble.model.Bag;
import istv.chrisanc.scrabble.model.Board;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.GameSaveInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.utils.interfaces.DictionaryInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;

/**
 * This class represents the "main" class of the ScrabbleGame. It starts, manages and ends the game.
 *
 * @author Christopher Anciaux
 */
public class Scrabble extends Application {
    protected ResourceBundle i18nMessages;

    protected DictionaryInterface dictionary;

    protected Stage primaryStage;

    protected BorderPane rootLayout;

    protected BoardInterface board;

    protected List<PlayerInterface> players;

    protected BagInterface bag;

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

        // TODO Set the application icon.
        // this.primaryStage.getIcons().add(new Image(""));

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
            // TODO: Manages the error in a more user-friendly way
            e.printStackTrace();
        }
    }

    /**
     * Shows the NewGame wizard, allowing the player to enter his name, the number of players etc
     */
    public void showNewGame() {
        // TODO @Bouaggad Abdessamade

        // TODO @Anciaux Christopher @Bouaggad Abdessamade
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
            // TODO Manages the error in a more user-friendly way
            e.printStackTrace();
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
            // TODO Manages the error in a more user-friendly way
            e.printStackTrace();
        }
    }

    /**
     * Shows the Game, that is to say the Scrabble Game itself. It opens the main controller, dealing with all the Scrabble logic
     */
    public void showGame() {
        // TODO @Anciaux Christopher @Bouaggad Abdessamade
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
        } catch (IOException e) {
            // TODO Manages the error in a more user-friendly way
            e.printStackTrace();
        }
    }

    /**
     * Resumes a game, according to the data present in the {@link GameSaveInterface}.
     *
     * @param gameSave The {@link GameSaveInterface} to use to construct the Scrabble and resume the game
     */
    public void resumeGameFromSaveAndShowGame(GameSaveInterface gameSave) {
        this.initializeScrabbleGame(gameSave.getBoard(), gameSave.getPlayers(), gameSave.getBag());

        this.showGame();
    }

    /**
     * Shows the EndGame, that is to say the message congratulating the player or encouraging him to retry wby playing a new game.
     */
    public void showEndGame() {
        // TODO @Bouaggad Abdessamade
    }

    /**
     * Initializes the root layout and tries to load the last opened
     * person file.
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
     * Initializes the ScrabbleGame with the given players only.
     *
     * @param players The players
     * @see #initializeScrabbleGame(BoardInterface, List, BagInterface)
     */
    protected void initializeScrabbleGame(List<PlayerInterface> players) {
        this.initializeScrabbleGame(new Board(), players, new Bag());
    }

    /**
     * Initializes the ScrabbleGame with all the needed information
     *
     * @param board   The board
     * @param players The players
     * @param bag     The bag
     */
    protected void initializeScrabbleGame(BoardInterface board, List<PlayerInterface> players, BagInterface bag) {
        this.board = board;
        this.players = players;
        this.bag = bag;
    }
}

