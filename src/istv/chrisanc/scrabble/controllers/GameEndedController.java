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
    @FXML
    protected Text winnerText;

    @FXML
    protected VBox scoreboardVBox;

    public void initializeInterface() {
        this.orderPlayersByNumberPoints();
        this.displayWinnerText();
        this.displayScoreboard();
    }

	@FXML
	protected void handleGoHome() {
		this.scrabble.showHome();
	}

	@FXML
	protected void handleNewGame() {
		this.scrabble.showNewGame();
	}

	protected void orderPlayersByNumberPoints() {
        Collections.sort(this.scrabble.getPlayers(), (PlayerInterface p1, PlayerInterface p2) -> -Integer.compare(p1.getScore(), p2.getScore()));
    }

    protected void displayWinnerText() {
        this.winnerText.setText(this.scrabble.getI18nMessages().getString("theWinnerIs") + " " + this.scrabble.getPlayers().get(0).getName());
    }

    protected void displayScoreboard() {
        for (int i = 0, playersSize = this.scrabble.getPlayers().size(); i < playersSize; i++) {
            this.scoreboardVBox.getChildren().add(new Text((i+1) + ". " + this.scrabble.getPlayers().get(i).getName()));
        }
    }
}
