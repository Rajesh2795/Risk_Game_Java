package sample.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sample.model.GameDetails;

import java.io.File;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This method tests the newmap controller methods.
 * @author Team43
 */
public class NewMapControllerTest1 {

    private File validfile;

    /**
     * This method creates the objects.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        validfile = new File("E:\\IntelliJ\\AppProject\\src\\resources\\Maps\\World.map");
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * This method tests the map valid or not before saving.
     */
    @Test
    public void saveButtonAction() {
        GameDetails.getGamedetails().clearData();
        GameDetails.getGamedetails().getgamedetails().add(new GameDetails(validfile,"NEWMAP"));
        GameDetails.getGamedetails().createMap(0);
        assertTrue(GameDetails.getGamedetails().validateMap(0));
    }
}