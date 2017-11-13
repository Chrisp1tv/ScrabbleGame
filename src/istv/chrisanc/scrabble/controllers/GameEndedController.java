package istv.chrisanc.scrabble.controllers;

import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.Collections;

/**
 * This is the controller which recaps the game, announces the winner, and redirects the user to the home if he wants to.
 *
 * @author Christopher Anciaux
 * @author Abdessamade Bouaggad
 */
public class GameEndedController extends BaseController {
    /**
     * The text announcing the winner of the game, to be updated programmatically
     */
    @FXML
    protected Text winnerText;

    /**
     * The Scoreboard, listing the players ordered according to their respective score
     */
    @FXML
    protected VBox scoreboardVBox;

    public void initializeInterface() {
        this.orderPlayersByNumberPoints();
        this.displayWinnerText();
        this.displayScoreboard();
    }

    /**
     * Redirects to the Home action
     */
	@FXML
	protected void handleGoHome() {
		this.scrabble.showHome();
	}

    /**
     * Redirects to the NewGame action
     */
	@FXML
	protected void handleNewGame() {
		this.scrabble.showNewGame();
	}

    /**
     * Orders the players by their number of points (the first one has the most points)
     */
	protected void orderPlayersByNumberPoints() {
        Collections.sort(this.scrabble.getPlayers(), (PlayerInterface p1, PlayerInterface p2) -> -Integer.compare(p1.getScore(), p2.getScore()));
    }

    /**
     * Updates the winner text to display the winner of the game
     */
    protected void displayWinnerText() {
        this.winnerText.setText(this.scrabble.getI18nMessages().getString("theWinnerIs") + " " + this.scrabble.getPlayers().get(0).getName());
    }

    /**
     * Updates the scoreboard to display the players
     */
    protected void displayScoreboard() {
        for (int i = 0, playersSize = this.scrabble.getPlayers().size(); i < playersSize; i++) {
            this.scoreboardVBox.getChildren().add(new Text((i+1) + ". " + this.scrabble.getPlayers().get(i).getName()));
        }
    }
}
