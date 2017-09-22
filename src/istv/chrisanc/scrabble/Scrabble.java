package istv.chrisanc.scrabble;

import istv.chrisanc.scrabble.model.Bag;
import istv.chrisanc.scrabble.model.Board;
import istv.chrisanc.scrabble.model.interfaces.BagInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This class represents the "main" class of the ScrabbleGame. It starts, manages and ends the game.
 *
 * @author Christopher Anciaux
 */
public class Scrabble extends Application {
    protected BoardInterface board = new Board();

    protected PlayerInterface[] players = new PlayerInterface[4];

    protected BagInterface bag = new Bag();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // TODO
    }
}

