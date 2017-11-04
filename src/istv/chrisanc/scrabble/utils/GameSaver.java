package istv.chrisanc.scrabble.utils;

import istv.chrisanc.scrabble.exceptions.utils.GameSaver.UnableToLoadSaveException;
import istv.chrisanc.scrabble.exceptions.utils.GameSaver.UnableToWriteSaveException;
import istv.chrisanc.scrabble.model.interfaces.GameSaveInterface;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;

/**
 * This class manages all the save-relative logic : saving, loading and retrieving the saves stored in the dedicated directory.
 *
 * @author Christopher Anciaux
 */
public class GameSaver {
    /**
     * The extension of the saved files
     */
    public static final String GAME_SAVES_FILES_EXTENSION = ".scrabbleSave";

    /**
     * The directory where to save the "user saves"
     */
    public static final String GAME_SAVES_DIRECTORY = System.getProperty("user.home") + File.separator + ".scrabbleGame";

    /**
     * @param gameSave The GameSave to store into a file
     * @param fileName The filename that will be used to store the file in the game saves directory
     *
     * @see #saveGame
     */
    public static void saveGameToGameSavesDirectory(GameSaveInterface gameSave, String fileName) throws UnableToWriteSaveException {
        GameSaver.saveGame(gameSave, new File(GAME_SAVES_DIRECTORY + File.separator + fileName));
    }

    /**
     * Saves the given {@link GameSaveInterface} into the file having the given file path
     *
     * @param gameSave         The GameSave to store into a file
     * @param file The filename where to store the file
     *
     * @throws UnableToWriteSaveException if the Save can't be wrote on the file system because of a writing problem
     */
    public static void saveGame(GameSaveInterface gameSave, File file) throws UnableToWriteSaveException {
        // We try to save the game in the file located at the given path, completeFilePath
        try {
            createGameSavesDirectoryIfNotExists();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));

            objectOutputStream.writeObject(gameSave);
            objectOutputStream.close();
        } catch (IOException e) {
            throw new UnableToWriteSaveException();
        }
    }

    /**
     * Finds all the game saves stored in the saves game directory
     *
     * @return the list of files that appear to be game saves
     * @throws IOException if the directory doesn't exist and can't be created
     */
    public static File[] findGameSaves() throws IOException {
        File gameSavesFolder = new File(GAME_SAVES_DIRECTORY);

        GameSaver.createGameSavesDirectoryIfNotExists();

        return gameSavesFolder.listFiles((dir, name) -> name.endsWith(GameSaver.GAME_SAVES_FILES_EXTENSION));
    }

    /**
     * Loads the requested {@link GameSaveInterface} from the requested file
     *
     * @param gameSaveFile The file from which to get the save
     *
     * @return the {@link GameSaveInterface} stored into the given file
     * @throws UnableToLoadSaveException if a problem happens during the loading of the save, for example if the file can't
     *                                   be opened or the content of the file isn't a serialized {@link GameSaveInterface}
     */
    public static GameSaveInterface loadGameSave(File gameSaveFile) throws UnableToLoadSaveException {
        // We check the file's integrity, if the save is acceptable.
        if (!gameSaveFile.isFile() || !gameSaveFile.getName().endsWith(GAME_SAVES_FILES_EXTENSION)) {
            throw new UnableToLoadSaveException();
        }

        // We try to load the GameSave from the file. If an error happens, we throw an Exception
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new BufferedInputStream(new FileInputStream(gameSaveFile)));

            GameSaveInterface gameSave = (GameSaveInterface) objectInputStream.readObject();
            objectInputStream.close();

            return gameSave;
        } catch (IOException | ClassNotFoundException e) {
            throw new UnableToLoadSaveException();
        }
    }

    /**
     * Deletes the given {@link GameSaveInterface} from the file system
     *
     * @param gameSaveFile The {@link GameSaveInterface} to delete from the file system
     * @throws IOException If the {@link GameSaveInterface} can't be deleted
     */
    public static void deleteGameSave(File gameSaveFile) throws IOException {
        Files.delete(gameSaveFile.toPath());
    }

    /**
     * Creates the saves directory, containing all the games saved by the user
     *
     * @throws IOException if the directory can't be created
     */
    private static void createGameSavesDirectoryIfNotExists() throws IOException {
        File gameSavesFolder = new File(GAME_SAVES_DIRECTORY);

        // If the folder doesn't exist, we create it. In case it isn't possible to, we throw an Exception
        if (!gameSavesFolder.exists() && !gameSavesFolder.mkdirs()) {
            throw new IOException();
        }
    }
}
