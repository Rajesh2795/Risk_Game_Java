package sample.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sample.controller.MapEditorController;
import sample.model.GameDetails;

import java.io.File;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the map editor functions.
 * @author Team43
 */
public class MapEditorControllerTest1 {

    private File validfile;
    private MapEditorController mecObject = new MapEditorController();

    /**
     * This method creates the objects.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        validfile = new File("E:\\IntelliJ\\AppProject\\src\\resources\\Maps\\invalid.map");
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * This method tests the whether the map is valid or not before saving
     */
    @Test
    public void saveContent() {
        assertFalse(mecObject.ismapValid(validfile,"EDITMAP"));
    }

    /**
     * This method tests whether the map is valid or not.
     */
    @Test
    public void ismapValid() {
        GameDetails.getGamedetails().clearData();
        GameDetails.getGamedetails().getgamedetails().add(new GameDetails(validfile,"EDITMAP"));
        GameDetails.getGamedetails().createMap(0);
        assertFalse(GameDetails.getGamedetails().validateMap(0));
    }
}