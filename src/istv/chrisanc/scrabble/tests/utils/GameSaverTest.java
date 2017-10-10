package istv.chrisanc.scrabble.tests.utils;

import istv.chrisanc.scrabble.model.Bag;
import istv.chrisanc.scrabble.model.Board;
import istv.chrisanc.scrabble.model.GameSave;
import istv.chrisanc.scrabble.model.Player;
import istv.chrisanc.scrabble.model.interfaces.GameSaveInterface;
import istv.chrisanc.scrabble.model.interfaces.PlayerInterface;
import istv.chrisanc.scrabble.utils.GameSaver;
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

    @Test
    public void saveGameToGameSavesDirectory() throws Exception {
        GameSaver.saveGameToGameSavesDirectory(GameSaverTest.gameSave, testSavePath);
        GameSaver.deleteGameSave(new File(GameSaver.GAME_SAVES_DIRECTORY + File.separator + GameSaverTest.testSavePath));
    }

    @Test
    public void findGameSaves() throws Exception {
        GameSaver.saveGameToGameSavesDirectory(GameSaverTest.gameSave, GameSaverTest.testSavePath);
        File[] saves = GameSaver.findGameSaves();

        assertEquals(1, saves.length);

        GameSaver.deleteGameSave(saves[0]);
    }

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
        PlayerInterface player = new Player("Christopher Anciaux");
        PlayerInterface player2 = new Player("AI");
        GameSaverTest.gameSave = new GameSave((short) 0, new Board(), Arrays.asList(player, player2), player, new Bag());
    }
}
