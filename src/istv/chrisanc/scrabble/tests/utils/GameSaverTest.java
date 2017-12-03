package istv.chrisanc.scrabble.tests.utils;

import istv.chrisanc.scrabble.model.Bag;
import istv.chrisanc.scrabble.model.Board;
import istv.chrisanc.scrabble.model.GameSave;
import istv.chrisanc.scrabble.model.HumanPlayer;
import istv.chrisanc.scrabble.model.interfaces.GameSaveInterface;
import istv.chrisanc.scrabble.model.interfaces.LanguageInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.model.languages.French.French;
import istv.chrisanc.scrabble.utils.GameSaver;
import istv.chrisanc.scrabble.utils.LetterToStringTransformer;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

/**
 * @author Christopher Anciaux
 */
public class GameSaverTest {
    protected static GameSaveInterface gameSave;

    protected static String testSavePath = "testSave" + GameSaver.GAME_SAVES_FILES_EXTENSION;

    /**
     * Tests if a game can be save to the dedicated directory
     */
    @Test
    public void saveGameToGameSavesDirectory() throws Exception {
        GameSaver.saveGameToGameSavesDirectory(GameSaverTest.gameSave, testSavePath);
        GameSaver.deleteGameSave(new File(GameSaver.GAME_SAVES_DIRECTORY + File.separator + GameSaverTest.testSavePath));
    }

    /**
     * Tests if game saves finding works as expected
     */
    @Test
    public void findGameSaves() throws Exception {
        GameSaver.saveGameToGameSavesDirectory(GameSaverTest.gameSave, GameSaverTest.testSavePath);
        File[] saves = GameSaver.findGameSaves();

        assertEquals(1, saves.length);

        GameSaver.deleteGameSave(saves[0]);
    }

    /**
     * Tests if a saved game can be loaded correctly
     */
    @Test
    public void loadGame() throws Exception {
        GameSaver.saveGameToGameSavesDirectory(GameSaverTest.gameSave, testSavePath);
        File saveFile = GameSaver.findGameSaves()[0];

        GameSaveInterface gameSave = GameSaver.loadGameSave(saveFile);

        assertEquals("Christopher Anciaux", gameSave.getPlayers().get(0).getName());

        GameSaver.deleteGameSave(new File(GameSaver.GAME_SAVES_DIRECTORY + File.separator + GameSaverTest.testSavePath));
    }

    @BeforeClass
    public static void setUp() throws Exception {
        LanguageInterface language = new French();
        PlayerInterface player = new HumanPlayer("Christopher Anciaux");
        PlayerInterface player2 = new HumanPlayer("Joueur 2");
        player.addLetters(Arrays.asList(LetterToStringTransformer.reverseTransform("A", language), LetterToStringTransformer.reverseTransform("B", language), LetterToStringTransformer.reverseTransform("C", language), LetterToStringTransformer.reverseTransform("J", language)));
        player2.addLetters(Arrays.asList(LetterToStringTransformer.reverseTransform("D", language), LetterToStringTransformer.reverseTransform("B", language), LetterToStringTransformer.reverseTransform("J", language), LetterToStringTransformer.reverseTransform("C", language)));

        GameSaverTest.gameSave = new GameSave(language, new Board(), Arrays.asList(player, player2), player, new Bag(language.getBagLettersDistribution()));
    }
}
