package sample.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;
import sample.model.Constants;
import sample.model.GameDetails;
import sample.model.Player;
import sample.strategies.Aggressive;
import sample.strategies.Benevolent;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;

/**
 * This class tests the benevolent strategy methods.
 * @author Team43
 */
public class BenevolentTest {

    File mapFile;
    private HashMap<String,String> playerCharacters = new HashMap<>();
    ObservableList<Player> playerList;
    Benevolent benevolentObject = new Benevolent();

    /**
     * setup method to initialize the objects
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        setPlayerCharacters();
        GameDetails.getGamedetails().getgamedetails().clear();
        mapFile = new File("E:\\IntelliJ\\AppProject\\src\\resources\\Maps\\World.map");
        GameDetails gameObject = new GameDetails(2,mapFile,playerCharacters,"NEWGAME","STARTPHASE", Constants.SINGLEMODE
                ,0);
        GameDetails.getGamedetails().getgamedetails().add(gameObject);
        GameDetails.getGamedetails().createMap(0);
        boolean result = GameDetails.getGamedetails().validateMap(0);
        GameDetails.getGamedetails().IntializeColors(0);
        GameDetails.getGamedetails().IntializePlayers(0);
        GameDetails.getGamedetails().InitializeArmies(0);
        GameDetails.getGamedetails().distributeArmies(0);
        GameDetails.getGamedetails().distributeTerritories(0);
        GameDetails.getGamedetails().distributeArmiestoTerritories(0);
        playerList = FXCollections.observableList(GameDetails.getGamedetails().getgamedetails().get(0).getPlayersList());
    }

    /**
     * sets the player characters.
     */
    public void setPlayerCharacters() {

        playerCharacters.put("Player1","HUMAN");
        playerCharacters.put("Player2","BENEVOLENT");
        playerCharacters.put("Player3","RANDOM");
        playerCharacters.put("Player4","CHEATER");
        playerCharacters.put("Player5","AGGRESSIVE");
        playerCharacters.put("Player6","RANDOM");
    }

    /**
     * This method tests the reinforcement strategy of benevolent player.
     */
    @Test
    public void reinforce() {

        assertNotNull(benevolentObject.reinforce(playerList,1));
    }

    /**
     * This method tests the attackphase strategy of benevolent player.
     */
    @Test
    public void attack() {

        assertNotNull(benevolentObject.attack(playerList,1,Constants.ATTACKER));
    }

    /**
     * This method tests the fortify phase strategy of benevolent player.
     */
    @Test
    public void fortify() {

        assertNotNull(benevolentObject.fortify(playerList,1,Constants.SOURCECOUNTRY));
    }

    /**
     * This method tests if it is returning weakest country.
     */
    @Test
    public void getWeakestTerritory() {

        assertNotNull(benevolentObject.getWeakestTerritory(playerList.get(1).getTerritoriesHeld()));
    }
}