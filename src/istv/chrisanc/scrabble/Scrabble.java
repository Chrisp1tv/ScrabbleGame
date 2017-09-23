package istv.chrisanc.scrabble;

import istv.chrisanc.scrabble.controllers.HomeController;
import istv.chrisanc.scrabble.controllers.RootLayoutController;
import istv.chrisanc.scrabble.model.Bag;
import istv.chrisanc.scrabble.model.Board;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ResourceBundle;

/**
 * This class represents the "main" class of the ScrabbleGame. It starts, manages and ends the game.
 *
 * @author Christopher Anciaux
 */
public class Scrabble extends Application {
    protected ResourceBundle i18nMessages;

    private Stage primaryStage;

    private BorderPane rootLayout;

    protected BoardInterface board = new Board();

    protected PlayerInterface[] players = new PlayerInterface[4];

    protected BagInterface bag = new Bag();

    public static void main(String[] args) {
        launch(args);
    }

    public ResourceBundle getI18nMessages() {
        return i18nMessages;
    }

    @Override
    public void start(Stage primaryStage) {
        // TODO
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
            e.printStackTrace();
        }
    }

    /**
     * Shows the NewGame wizard, allowing the player to enter his name, the number of players etc
     */
    public void showNewGame() {
        // TODO @Bouaggad Abdessamade
    }

    /**
     * Shows the LoadGame wizard, allowing the player to load an externally saved game or choose one save between the stored ones
     */
    public void showLoadGame() {
        // TODO @Anciaux Christopher
    }

    /**
     * Shows the Game, that is to say the Scrabble Game itself. It opens the main controller, dealing with all the Scrabble logic
     */
    public void showGame() {
        // TODO @Anciaux Christopher @Bouaggad Abdessamade
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
}

