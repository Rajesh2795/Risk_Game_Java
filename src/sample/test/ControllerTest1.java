package sample.test;

import javafx.stage.FileChooser;
import org.junit.Before;
import org.junit.Test;
import sample.controller.Controller;
import sample.model.Constants;
import sample.model.GameDetails;

import java.io.File;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * This class tests the controller class.
 */
public class ControllerTest1 {

    Controller controller;
    File validfile;
    HashMap<String,String> playercharacters = new HashMap<>();

    /**
     * Set up method to set the objects
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        controller = new Controller();
        validfile = new File("E:\\IntelliJ\\AppProject\\src\\resources\\Maps\\World.map");
        setPlayercharacters();
        GameDetails gd = new GameDetails(5,validfile,playercharacters,Constants.NEWGAME,Constants.STARTPHASE
                ,Constants.SINGLEMODE,0);
        GameDetails.getGamedetails().addgamedetailsInstance(gd);
    }

    /**
     * Sets the player characters.
     */
    public void setPlayercharacters() {

        playercharacters.put("Player1", Constants.HUMAN);
        playercharacters.put("Player2",Constants.AGGRESSIVE);
        playercharacters.put("Player3",Constants.BENEVOLENT);
        playercharacters.put("Player4",Constants.RANDOM);
        playercharacters.put("Player5",Constants.CHEATER);
        playercharacters.put("Player6",Constants.HUMAN);
    }

    /**
     * To test whether the function returns correct object index.
     */
    @Test
    public void returnIndex() {

        assertEquals(controller.returnIndex(Constants.NEWGAME),0);
    }

    /**
     * This method check whether the object is deleted or not.
     */
    @Test
    public void clearObjects() {

        controller.clearObjects(Constants.NEWGAME);
        assertEquals(GameDetails.getGamedetails().getgamedetails().size(),1);
    }

    @Test
    public void loadsavedgame() {

        File saveFile = new File("E:\\IntelliJ\\AppProject\\src\\resources\\SavedGame.ser");
        assertNotNull(saveFile);
    }
}