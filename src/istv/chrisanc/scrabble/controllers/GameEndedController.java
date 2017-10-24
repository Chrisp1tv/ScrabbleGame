package istv.chrisanc.scrabble.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import javafx.fxml.FXML;

/**
 * This is the controller which recaps the game, announces the winner, and redirects the user to the home if he wants to.
 *
 * @author Christopher Anciaux
 * @author Abdessamade Bouaggad
 * TODO @Bouaggad Abdessamade
 */
public class GameEndedController extends BaseController {
	@FXML
	protected void handleGoHome() {
		this.scrabble.showHome();
	}

	@FXML
	protected void handleNewGame() {
		this.scrabble.showNewGame();
	}


	@FXML
	protected void handleQuit() {
        System.exit(0);
	}

	protected void showWinner() {

		//int temp=0;
		//ObservableList<Person> personData = FXCollections.observableArrayList();
		//ArrayList<Integer> totalPoints = new ArrayList<>();
		List<PlayerInterface> players = this.scrabble.getPlayers();

		/**
		// Get Score Of Each Player
		for(PlayerInterface p : players) {
			totalPoints.add(p.getScore());
		}

		//Sort Players
		Collections.sort(totalPoints);
		ArrayList<Integer> totalPointsSorted = new ArrayList<>();

		for(int i=totalPoints.size()-1;i>=0;i--) {
			totalPointsSorted.add(totalPoints.get(i));
		}

		System.out.println("The Winner Is ");
		**/


		Collections.sort(players,new Comparator<PlayerInterface>() {

			@Override
			public int compare(PlayerInterface p1, PlayerInterface p2) {
				if(p1.getScore()>p2.getScore()) {
					return 1;
				} else if(p1.getScore()<p2.getScore()) {
					return -1;
				} else {
					return 0;
				}
			}

		});

		System.out.println("The Winner Is >> "+players.get(players.size()-1).getName());


	}
}
