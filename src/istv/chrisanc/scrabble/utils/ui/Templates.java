package istv.chrisanc.scrabble.utils.ui;

import istv.chrisanc.scrabble.model.BoardPosition;
import istv.chrisanc.scrabble.model.interfaces.ArtificialIntelligencePlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.BoardInterface;
import istv.chrisanc.scrabble.model.interfaces.HumanPlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.LetterInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.interfaces.SquareInterface;
import istv.chrisanc.scrabble.utils.LetterToStringTransformer;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedMap;

/**
 * This class contains (mostly) all the programmatically displaying of the application.
 *
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

        // Spinner indicating that player is "thinking" (only for non-local and non-human players)
        ProgressIndicator progressIndicator = new ProgressIndicator();
        progressIndicator.getStyleClass().add("player-progress-indicator");
        HBox.setHgrow(progressIndicator, Priority.NEVER);

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

        TextFlow playerStatusInformation = new TextFlow();

        if (player instanceof HumanPlayerInterface) {
            Text boundPlayerAvailableHelps = new Text();
            boundPlayerAvailableHelps.textProperty().bind(((HumanPlayerInterface) player).availableHelpsProperty().asString());

            Text playerAvailableHelpsLegend = new Text(" " + i18nMessages.getString("availableHelps"));
            playerStatusInformation.getChildren().addAll(boundPlayerAvailableHelps, playerAvailableHelpsLegend);
        } else if (player instanceof ArtificialIntelligencePlayerInterface) {
            Text artificalIntelligenceLevelInformation = new Text(i18nMessages.getString("computer") + " - " + i18nMessages.getString("artificialIntelligenceLevels." + ((ArtificialIntelligencePlayerInterface) player).getLevel()));
            playerStatusInformation.getChildren().add(artificalIntelligenceLevelInformation);
        }

        // Assembling all elements of the player frame and adding it to the list of players
        innerVBox.getChildren().addAll(playerName, playerPoints, playerStatusInformation);
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

                if (player instanceof ArtificialIntelligencePlayerInterface) {
                    innerHBox.getChildren().set(innerHBox.getChildren().indexOf(playerNumber), progressIndicator);
                }
            } else {
                playerContainer.getStyleClass().remove("active");
                int indexOfProgressIndicator = innerHBox.getChildren().indexOf(progressIndicator);

                if (-1 != indexOfProgressIndicator && player instanceof ArtificialIntelligencePlayerInterface) {
                    innerHBox.getChildren().set(indexOfProgressIndicator, playerNumber);
                }
            }
        });

        parent.getChildren().add(playerContainer);
    }

    public static void initializeBoardGrid(GridPane scrabbleGrid, BorderPane scrabbleContainer) {
        double gridCellSize = (100 / (double) BoardInterface.BOARD_SIZE);

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setPercentWidth(gridCellSize);
        RowConstraints rowConstraints = new RowConstraints();
        rowConstraints.setPercentHeight(gridCellSize);
        for (int i = 0; i < BoardInterface.BOARD_SIZE; i++) {
            scrabbleGrid.getColumnConstraints().add(columnConstraints);
            scrabbleGrid.getRowConstraints().addAll(rowConstraints);
        }

        scrabbleContainer.widthProperty().addListener((observable, oldValue, newValue) -> {
            double size = newValue.doubleValue() - 500;
            double maxHeight = scrabbleContainer.getHeight() - 85;

            if (size > maxHeight) {
                size = maxHeight;
            }

            scrabbleGrid.setMaxSize(size, size);
        });

        scrabbleContainer.heightProperty().addListener((observable, oldValue, newValue) -> {
            double size = newValue.doubleValue() - 85;
            double maxWidth = scrabbleContainer.getWidth() - 500;

            if (size > maxWidth) {
                size = maxWidth;
            }

            scrabbleGrid.setMaxSize(size, size);
        });
    }

    public static void displayBoardGrid(ResourceBundle i18nMessages, List<List<SquareInterface>> squares, SortedMap<BoardPosition, LetterInterface> playedLetters, List<List<LetterInterface>> boardLetters, HBox playerLettersContainer, GridPane scrabbleGrid) {
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
                        playedLetters.put(new BoardPosition((short) finalI, (short) finalJ), letter);
                    });
                } else {
                    Templates.displayLetter(square, boardLetters.get(i).get(j), false);
                }

                scrabbleGrid.add(square, j, i);
            }
        }
    }
}
