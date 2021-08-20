package sample.test;

import javafx.collections.ObservableList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sample.model.*;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * This method tests the player methods.
 * @author Team43
 */
public class PlayerTest1 {

    private File mapfile;
    private ArrayList<Player> playerList;
    private HashMap<String,String> playerCharacters = new HashMap<>();

    /**
     * This method creates the objects.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {

        setPlayerCharacters();
        GameDetails.getGamedetails().getgamedetails().clear();
        mapfile = new File("E:\\IntelliJ\\AppProject\\src\\resources\\Maps\\World.map");
        GameDetails gameObject = new GameDetails(2,mapfile,playerCharacters,"NEWGAME","STARTPHASE"
                ,Constants.SINGLEMODE,0);
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
     * This method tests the increase army count by value method.
     */
    @Test
    public void increaseArmyCountByValue() {
        playerList.get(0).increaseArmyCountByValue(5);
        assertEquals(playerList.get(0).getPlayerArmies(),24);
    }

    /**
     * This method tests the decrease army count by value method.
     */
    @Test
    public void decreaseArmyCountByValue() {
        playerList.get(0).decreaseArmyCountByValue(5);
        assertEquals(playerList.get(0).getPlayerArmies(),14);
    }

    /**
     * This method test add card method.
     */
    @Test
    public void addCard() {
        playerList.get(0).addCard(new Card("ALASKA","INFANTRY"));
        assertEquals(playerList.get(0).getCardsHeld().size(),1);
    }

    /**
     * This method tests the remove card method.
     */
    @Test
    public void removeCard() {
        playerList.get(0).addCard(new Card("ALASKA","INFANTRY"));
        Card card = new Card("AUSTRALIA","CAVALRY");
        playerList.get(0).addCard(card);
        playerList.get(0).removeCard(card);
        assertEquals(playerList.get(0).getCardsHeld().size(),1);
    }

    /**
     * This method tests the add territory method.
     */
    @Test
    public void addTerritory() {
        Territories territory = playerList.get(1).getTerritoriesHeld().get("Venezuala");
        playerList.get(0).addTerritory(territory);
        playerList.get(1).getTerritoriesHeld().remove("Venezuala");
        assertEquals(playerList.get(0).getTerritoriesHeld().size(),22);
    }

    /**
     * This method tests the remove territory method.
     */
    @Test
    public void removeTerritory() {
        Territories territory = playerList.get(1).getTerritoriesHeld().get("Venezuala");
        playerList.get(1).removeTerritory(territory);
        assertEquals(playerList.get(1).getTerritoriesHeld().size(),20);
    }

    /**
     * This method tests the add continent method.
     */
    @Test
    public void addContinent() {
        Continent continent = GameDetails.getGamedetails().getgamedetails().get(0).getContinentList().get(0);
        playerList.get(0).addContinent(continent);
        assertEquals(playerList.get(0).getContinentHeld().size(),1);
    }

    /**
     * This method tests the remove continent method.
     */
    @Test
    public void removeContinent() {
        Continent continent = GameDetails.getGamedetails().getgamedetails().get(0).getContinentList().get(0);
        playerList.get(0).addContinent(continent);
        playerList.get(0).removeContinent(continent.getContinentName());
        assertEquals(playerList.get(0).getContinentHeld().size(),0);
    }

    /**
     * this method tests the reinforcementarmies method.
     */
    @Test
    public void calculateReinforcementArmies() {
        playerList.get(0).calculateReinforcementArmies();
        assertEquals(playerList.get(0).getPlayerArmies(),26);
    }

    /**
     * This method tests the fortification methods.
     */
    @Test
    public void canFortify() {
        assertTrue(playerList.get(0).canFortify("Peru","Brazil"));
    }

    /**
     * This method tests the do fortification method.
     */
    @Test
    public void doFortification() {
        playerList.get(0).getTerritoriesHeld().get("Peru").setArmiesHeld(10);
        playerList.get(0).doFortification(3,"Peru","Brazil");
        assertEquals(playerList.get(0).getTerritoriesHeld().get("Brazil").getArmiesHeld(),4);
        assertEquals(playerList.get(0).getTerritoriesHeld().get("Peru").getArmiesHeld(),7);
    }

    /**
     * This method tests the whether the attacked country is adjacent or not.
     */
    @Test
    public void canAttack() {
        assertTrue(playerList.get(0).canAttack("Peru","Venezuala"));
    }

    /**
     * This method tests attack method.
     */
    @Test
    public void doAttack() {
        playerList.get(0).getTerritoriesHeld().get("Peru").setArmiesHeld(10);
        //assertNotEquals(playerList.get(0).doAttack(3,2,"Peru","Venezuala",playerList.get(1)),"WINNER");
        assertNotNull(playerList.get(0).doAttack(3,2,"Peru","Venezuala",playerList.get(1)));
    }

    /**
     * This method check whether dice are rolled exactly by specific times.
     */
    @Test
    public void rollDice() {

        ArrayList<Integer> diceList = playerList.get(0).rollDice(3);
        assertEquals(diceList.size(),3);
    }

    /**
     * This method checks winner after the attack method.
     */
    @Test
    public void determineWinner() {
        ArrayList<Integer> diceA = new ArrayList<Integer>();
        diceA.add(5);
        diceA.add(3);
        diceA.add(2);

        ArrayList<Integer> diceB = new ArrayList<Integer>();
        diceA.add(4);
        diceA.add(4);

        int attackerDecreamentArmy=0,attackedDecreamentArmy=0,index = 0;

        if(diceB.size() < diceA.size()) {
            index = diceB.size();
        } else if(diceA.size() < diceB.size()) {
            index = diceA.size();
        } else if(diceA.size() == diceB.size()) {
            index = diceB.size();
        }

        for(int i = 0; i<index; i++) {
            if(diceA.get(i) > diceB.get(i)) {
                attackedDecreamentArmy++;
            } else {
                attackerDecreamentArmy++;
            }
        }

        String result = "";
        if(attackerDecreamentArmy < attackedDecreamentArmy) {
            result = "WINNER";
        } else if(attackerDecreamentArmy > attackedDecreamentArmy) {
            result = "LOSER";
        } else if(attackedDecreamentArmy == attackerDecreamentArmy) {
            result = "TIE";
        }

        assertEquals(result,"TIE");
    }

    /**
     * This method checks if the territory has lost all armies after attack.
     */
    @Test
    public void checkIfATerritoryHasLostAllArmies() {
        assertFalse(playerList.get(0).checkIfATerritoryHasLostAllArmies("Peru"));
    }

    /**
     * This method checks if the player has lost all countries.
     */
    @Test
    public void checkIfPlayerHasLostAllCountries() {
        assertFalse(playerList.get(0).checkIfPlayerHasLostAllCountries());
    }

    /**
     * This method checks the transfer card from other player method.
     */
    @Test
    public void transferCardsFromOnePlayerToAnother() {
        Card card = new Card("ALASKA","INFANTRY");
        playerList.get(1).addCard(card);
        playerList.get(1).transferCardsFromOnePlayerToAnother(playerList.get(0));
        assertEquals(playerList.get(0).getCardsHeld().size(),1);
        assertEquals(playerList.get(1).getCardsHeld().size(),0);
    }
}