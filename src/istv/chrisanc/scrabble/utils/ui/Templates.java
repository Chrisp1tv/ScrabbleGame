package istv.chrisanc.scrabble.utils.ui;

import istv.chrisanc.scrabble.controllers.GameController;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.SquareInterface;
import istv.chrisanc.scrabble.utils.LetterToStringTransformer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedMap;

/**
 * @author Christopher Anciaux
 */
public class Templates {
    public static void displayLetters(Pane parent, List<LetterInterface> letters, boolean draggable) {
        for (LetterInterface letter : letters) {
            Templates.displayLetter(parent, letter, draggable);
        }
    }

    public static void displayLetter(Pane parent, LetterInterface letter, boolean draggable) {
        Text tileText = new Text(LetterToStringTransformer.transform(letter));
        tileText.getStyleClass().add("tile-letter");

        Text tileValue = new Text(String.valueOf(letter.getValue()));
        tileValue.getStyleClass().add("tile-value");
        tileValue.setTranslateX(8);
        tileValue.setTranslateY(10);

        StackPane tile = new StackPane(tileText, tileValue);
        tile.getStyleClass().add("tile");

        if (draggable) {
            DraggableLetterManager.makeLetterDraggable(tile, letter);
        }

        parent.getChildren().add(tile);
    }

    public static void displayPlayers(Pane parent, ObjectProperty<PlayerInterface> currentPlayerProperty, List<PlayerInterface> players, ResourceBundle i18nMessages) {
        for (int i = 0, playersSize = players.size(); i < playersSize; i++) {
            Templates.displayPlayer(parent, currentPlayerProperty, players.get(i), i, i18nMessages);
        }
    }

    public static void displayPlayer(Pane parent, ObjectProperty<PlayerInterface> currentPlayerProperty, PlayerInterface player, int playerIndex, ResourceBundle i18nMessages) {
        // Layout of each player frame
        StackPane playerContainer = new StackPane();
        playerContainer.getStyleClass().add("player");

        HBox innerHBox = new HBox();
        innerHBox.getStyleClass().add("inner-player");

        // Texts in each player frame
        Text playerNumber = new Text(Integer.toString(playerIndex + 1));
        playerNumber.getStyleClass().add("player-number");
        HBox.setHgrow(playerNumber, Priority.NEVER);

        VBox innerVBox = new VBox();
        innerVBox.getStyleClass().add("player-information");
        HBox.setHgrow(innerVBox, Priority.ALWAYS);

        Text playerName = new Text(player.getName());
        playerName.getStyleClass().add("player-name");

        // Bound number of points
        TextFlow playerPoints = new TextFlow();
        playerPoints.getStyleClass().add("player-points");

        Text boundPlayerPoints = new Text();
        boundPlayerPoints.textProperty().bind(player.scoreProperty().asString());
        Text playerPointsLegend = new Text(" " + i18nMessages.getString("points"));
        playerPoints.getChildren().addAll(boundPlayerPoints, playerPointsLegend);

        // Assembling all elements of the player frame and adding it to the list of players
        innerVBox.getChildren().addAll(playerName, playerPoints);
        innerHBox.getChildren().addAll(playerNumber, innerVBox);

        playerContainer.getChildren().add(innerHBox);

        // Make player active if he is the current player
        if (player.equals(currentPlayerProperty.getValue())) {
            playerContainer.getStyleClass().add("active");
        }

        // Create the event to listen if the user is a current player
        currentPlayerProperty.addListener((ObservableValue<? extends PlayerInterface> observable, PlayerInterface oldValue, PlayerInterface newValue) -> {
            if (player.equals(newValue)) {
                playerContainer.getStyleClass().add("active");
            } else {
                playerContainer.getStyleClass().remove("active");
            }
        });

        parent.getChildren().add(playerContainer);
    }

    public static void displayBoardGrid(ResourceBundle i18nMessages, List<List<SquareInterface>> squares, SortedMap<GameController.BoardPosition, LetterInterface> playedLetters, List<List<LetterInterface>> boardLetters, HBox playerLettersContainer, GridPane scrabbleGrid) {
        for (int i = 0, squaresSize = squares.size(); i < squaresSize; i++) {
            List<SquareInterface> squaresLine = squares.get(i);

            for (int j = 0, squaresLineSize = squaresLine.size(); j < squaresLineSize; j++) {
                StackPane square;

                if (null != squaresLine.get(j).getInformation()) {
                    Text squareText = new Text(i18nMessages.getString(squaresLine.get(j).getInformation()));
                    squareText.getStyleClass().add("square-legend");

                    square = new StackPane(squareText);
                } else {
                    square = new StackPane();
                }

                square.getStyleClass().addAll("square", squaresLine.get(j).getCssClass());

                // First line
                if (0 == i) {
                    square.getStyleClass().add("first-line");
                }

                // First column
                if (0 == j) {
                    square.getStyleClass().add("first-column");
                }

                if (null == boardLetters.get(i).get(j)) {
                    int finalI = i;
                    int finalJ = j;
                    DraggableLetterManager.makeElementReadyToReceiveLetter(square, true, (letter, event) -> {
                        square.getChildren().add((Node) event.getGestureSource());
                        playerLettersContainer.getChildren().remove(event.getGestureSource());
                        playedLetters.put(new GameController.BoardPosition((short) finalI, (short) finalJ), letter);
                    });
                } else {
                    Templates.displayLetter(square, boardLetters.get(i).get(j), false);
                }

                scrabbleGrid.add(square, j, i);
            }
        }
    }
}
