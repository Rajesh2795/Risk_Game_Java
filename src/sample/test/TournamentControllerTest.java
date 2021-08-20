package sample.test;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import org.junit.Before;
import org.junit.Test;
import sample.model.TournamentMode;

import static org.junit.Assert.*;

/**
 * This method tests the Tournament controller class.
 * @author Team43
 */
public class TournamentControllerTest {

    int noOfPlayers;
    int noOfGames;
    int noOfMaps;
    int noOfTurns;

    /**
     * This method tests whether the no of players are retrived properly or not.
     */
    @Test
    public void getNoOfPlayers() {

        JFXPanel fxPanel = new JFXPanel();
        final ComboBox<String> selectnoofPlayers = new ComboBox<>();
        selectnoofPlayers.getItems().addAll("2","3","4");
        selectnoofPlayers.getSelectionModel().select("4");
        String noofPlayers = selectnoofPlayers.getSelectionModel().getSelectedItem();
        assertNotNull(noofPlayers);
        assertEquals(noofPlayers,"4");
        noOfPlayers = Integer.parseInt(noofPlayers);
    }

    /**
     * This method checks if the noofgames selected is retrieved correctly or not.
     */
    @Test
    public void getnoofGames() {

        JFXPanel fxPanel = new JFXPanel();
        final ComboBox<String> selectnoofGames = new ComboBox<>();
        selectnoofGames.getItems().addAll("2","3","4","5");
        selectnoofGames.getSelectionModel().select("3");
        String noofGames  = selectnoofGames.getSelectionModel().getSelectedItem();
        assertNotNull(noofGames);
        assertEquals(noofGames,"3");
        noOfGames = Integer.parseInt(noofGames);
    }

    /**
     * This method checks if the noofTurns selected is retrieved correctly or not.
     */
    @Test
    public void getnoofTurns() {

        JFXPanel fxPanel = new JFXPanel();
        final ComboBox<String> selectnoofTurns = new ComboBox<>();
        selectnoofTurns.getItems().addAll("12","13","14");
        selectnoofTurns.getSelectionModel().select("14");
        String noofTurns = selectnoofTurns.getSelectionModel().getSelectedItem();
        noOfTurns = Integer.parseInt(noofTurns);
        assertNotNull(noofTurns);
        assertEquals(noofTurns,"14");
    }

    /**
     * This method checks if the noofmaps selected is retrieved correctly or not.
     */
    @Test
    public void getnoofMaps() {

        JFXPanel fxPanel = new JFXPanel();
        final ComboBox<String> selectnoofMaps = new ComboBox<>();
        selectnoofMaps.getItems().addAll("2","3","4");
        selectnoofMaps.getSelectionModel().select("3");
        String noofMaps = selectnoofMaps.getSelectionModel().getSelectedItem();
        noOfMaps = Integer.parseInt(noofMaps);
        assertNotNull(noofMaps);
        assertEquals(noofMaps,"3");
    }

    /**
     * Method tests whether the player character selected is retrieved correctly or not
     */
    @Test
    public void storePlayerCharacters() {

        JFXPanel fxPanel = new JFXPanel();
        final ComboBox<String> characters = new ComboBox<>();
        characters.getItems().addAll("HUMAN","CHEATER","RANDOM");
        characters.getSelectionModel().select("CHEATER");
        String PlayerCharacter = characters.getSelectionModel().getSelectedItem();
        assertNotNull(PlayerCharacter);
        assertEquals(PlayerCharacter,"CHEATER");
    }

    /**
     * Method tests whether inputs are store properly or not.
     *//*
    @Test
    public void storeTheInputsAndCreateGameInstances() {

        TournamentMode.getInstance().setNoofMaps(noOfMaps);
        TournamentMode.getInstance().setNoofGames(noOfGames);
        TournamentMode.getInstance().setNoofPlayers(noOfPlayers);
        TournamentMode.getInstance().setNoofTurns(noOfTurns);
        assertEquals(TournamentMode.getInstance().getNoofPlayers(),4);
    }*/
}