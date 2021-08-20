package sample.test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sample.controller.LoadgameController;
import sample.model.*;
import sun.management.counter.perf.PerfLongArrayCounter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

/**
 * This class test the game controller methods.
 * @author Team43
 */
public class LoadgameControllerTest1 {

    private File mapfile;
    private ArrayList<Player> playerList;
    private ObservableList<Continent> continentList;
    private HashMap<String,String> playerCharacters = new HashMap<>();
    int currentPlayer = 1;
    LoadgameController lgcInstance;

    /**
     * This Method creates the Objects.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        setPlayerCharacters();
        GameDetails.getGamedetails().getgamedetails().clear();
        mapfile = new File("E:\\IntelliJ\\AppProject\\src\\resources\\Maps\\World.map");
        GameDetails gameObject = new GameDetails(2,mapfile,playerCharacters,"NEWGAME","STARTPHASE", Constants.SINGLEMODE
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
        playerList = GameDetails.getGamedetails().getgamedetails().get(0).getPlayersList();
        continentList = FXCollections.observableList(GameDetails.getGamedetails().getgamedetails().get(0).getContinentList());
        lgcInstance = new LoadgameController();
    }

    public void setPlayerCharacters() {

        playerCharacters.put("Player1","HUMAN");
        playerCharacters.put("Player2","CHEATER");
        playerCharacters.put("Player3","RANDOM");
        playerCharacters.put("Player4","AGGRESSIVE");
        playerCharacters.put("Player5","BENEVOLENT");
        playerCharacters.put("Player6","RANDOM");
    }

    @After
    public void tearDown() throws Exception {
    }

    /**
     * This method checks whether give is given to the player properly or not.
     */
    @Test
    public void giveControlToTheNextPlayerAndSetThePhases() {
        currentPlayer++;
        if(currentPlayer == playerList.size()) {
            currentPlayer = 0;
        }
        assertEquals(currentPlayer,0);
    }

    /**
     * This method checks the player of the territory.
     */
    @Test
    public void findPlayerofTheTerritory() {

        int index = 0;
        for(int i = 0; i<playerList.size(); i++) {
            if(playerList.get(i).getTerritoriesHeld().containsKey("Peru")) {
                index = i;
            }
        }
        assertEquals(index,0);
    }

    /**
     * This method checks if the player has lost the continent.
     */
    @Test
    public void checkIfPlayerHasLostTheContinent() {

        Territories territories = playerList.get(0).getTerritoriesHeld().get("Peru");
        assertFalse(playerList.get(0).getContinentHeld().containsKey(territories.getContinentName()));
    }

    /*@Test
    public void getIndexOfTheContinent() {

        int index = -1;
        int size = continentList.size();
        System.out.println("SIze is = " + size);
        for(int i = 0; i<size; i++) {
            Continent continent = continentList.get(i);
            if(continent.getContinentName().equalsIgnoreCase("South America")) {
                System.out.println("Continent index is = " + i);
                index = i;
            }
        }
        assertEquals(index,9);
    }*/
}