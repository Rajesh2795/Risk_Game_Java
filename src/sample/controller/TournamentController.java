package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import sample.model.Constants;
import sample.model.GameDetails;
import sample.model.TournamentMode;

import java.io.Console;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is resposible for handling the tournament view.
 * @author Team43
 */
public class TournamentController {

    @FXML
    public Label noofPlayersLabel,player1Label,player2Label,player3Label,player4Label,noOfGamesLabel,noOfTurnsLabel,
            noOfMapsLabel;

    @FXML
    public ComboBox noofPlayersComboBox,player1ComboBox,player2ComboBox,player3ComboBox,player4ComboBox,
            noOfGamesComboBox,noOfTurnsComboBox,noOfMapsComboBox;

    @FXML
    public Button chooseMapButton1,chooseMapButton2,chooseMapButton3,chooseMapButton4,chooseMapButton5;

    public String noofPlayers,noofGames,noofTurns,noofMaps;

    public File mapFile1,mapFile2,mapFile3,mapFile4,mapFile5;

    public HashMap<String,String> playerCharacters = new HashMap<>();

    @FXML
    private AnchorPane tAnchorPane;


    /**
     * To get the no of players from the view.
     */
    public void getNoOfPlayers() {

        displayPlayerComboBox(false,false,false,false);
        noofPlayers = (String) noofPlayersComboBox.getSelectionModel().getSelectedItem();
        setPlayers(noofPlayers);
    }

    /**
     * To display the player characters.
     * @param noofPlayers
     */
    public void setPlayers(String noofPlayers) {

        switch (noofPlayers) {
            case "2":
                displayPlayerComboBox(true,true,false,false);
                break;

            case "3":
                displayPlayerComboBox(true,true,true,false);
                break;

            case "4":
                displayPlayerComboBox(true,true,true,true);
                break;
        }
    }

    /**
     * displays the player comboboxes
     * @param value
     * @param value1
     * @param value2
     * @param value3
     */
    public void displayPlayerComboBox(boolean value,boolean value1,boolean value2,boolean value3) {

        player1Label.setVisible(value);
        player1ComboBox.setVisible(value);

        player2Label.setVisible(value1);
        player2ComboBox.setVisible(value1);

        player3Label.setVisible(value2);
        player3ComboBox.setVisible(value2);

        player4Label.setVisible(value3);
        player4ComboBox.setVisible(value3);
    }

    /**
     * To get the no of games.
     */
    public void getnoofGames() {

        noofGames = (String) noOfGamesComboBox.getSelectionModel().getSelectedItem();
    }

    /**
     * To get the no of turns.
     */
    public void getnoofTurns() {

        noofTurns = (String) noOfTurnsComboBox.getSelectionModel().getSelectedItem();
    }

    /**
     * To get the no of maps.
     */
    public void getnoofMaps() {

        disableMapButtons(false,false,false,false,false);
        noofMaps = (String) noOfMapsComboBox.getSelectionModel().getSelectedItem();
        displayMapbuttons(noofMaps);
    }

    public void displayMapbuttons(String noofMaps) {

        switch (noofMaps) {

            case "1":
                disableMapButtons(true,false,false,false,false);
                break;

            case "2":
                disableMapButtons(true,true,false,false,false);
                break;

            case "3":
                disableMapButtons(true,true,true,false,false);
                break;

            case "4":
                disableMapButtons(true,true,true,true,false);
                break;

            case "5":
                disableMapButtons(true,true,true,true,true);
                break;
        }
    }

    /**
     * To showor hide the map buttons.
     * @param value
     * @param value1
     * @param value2
     * @param value3
     * @param value4
     */
    public void disableMapButtons(boolean value,boolean value1,boolean value2,boolean value3,boolean value4) {

        chooseMapButton1.setVisible(value);
        chooseMapButton2.setVisible(value1);
        chooseMapButton3.setVisible(value2);
        chooseMapButton4.setVisible(value3);
        chooseMapButton5.setVisible(value4);
    }

    /**
     * This method is used to choose the map file using file chooser.
     */
    public void chooseMap1() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Map files(*.map)","*.map"));
        mapFile1 = fileChooser.showOpenDialog(tAnchorPane.getScene().getWindow());
    }

    /**
     * This method is used to choose the map file using file chooser.
     */
    public void chooseMap2() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Map files(*.map)","*.map"));
        mapFile2 = fileChooser.showOpenDialog(tAnchorPane.getScene().getWindow());
    }

    /**
     * This method is used to choose the map file using file chooser.
     */
    public void chooseMap3() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Map files(*.map)","*.map"));
        mapFile3 = fileChooser.showOpenDialog(tAnchorPane.getScene().getWindow());
    }

    /**
     * This method is used to choose the map file using file chooser.
     */
    public void chooseMap4() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Map files(*.map)","*.map"));
        mapFile4 = fileChooser.showOpenDialog(tAnchorPane.getScene().getWindow());
    }

    /**
     * This method is used to choose the map file using file chooser.
     */
    public void chooseMap5() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Map files(*.map)","*.map"));
        mapFile5 = fileChooser.showOpenDialog(tAnchorPane.getScene().getWindow());
    }

    /**
     * Used to store the player characters to the hashmap based on the number of players.
     */
    public void storePlayerCharacters() {

        switch (noofPlayers) {
            case "2":
                playerCharacters.put("Player1",(String) player1ComboBox.getValue());
                playerCharacters.put("Player2",(String) player2ComboBox.getValue());
                break;
            case "3":
                playerCharacters.put("Player1",(String) player1ComboBox.getValue());
                playerCharacters.put("Player2",(String) player2ComboBox.getValue());
                playerCharacters.put("Player3",(String) player3ComboBox.getValue());
                break;
            case "4":
                playerCharacters.put("Player1",(String) player1ComboBox.getValue());
                playerCharacters.put("Player2",(String) player2ComboBox.getValue());
                playerCharacters.put("Player3",(String) player3ComboBox.getValue());
                playerCharacters.put("Player4",(String) player4ComboBox.getValue());
                break;
        }
    }

    /**
     * This method stores the inputs to the objects.
     */
    public void storeTheInputsAndCreateGameInstances() {

        storePlayerCharacters();
        TournamentMode.getInstance().setNoofMaps(Integer.parseInt(noofMaps));
        TournamentMode.getInstance().setNoofGames(Integer.parseInt(noofGames));
        TournamentMode.getInstance().setNoofPlayers(Integer.parseInt(noofPlayers));
        TournamentMode.getInstance().setNoofTurns(Integer.parseInt(noofTurns));
        TournamentMode.getInstance().setPlayerCharacters(playerCharacters);

        ArrayList<ArrayList<String>> results = new ArrayList<>();
        TournamentMode.getInstance().setResults(results);
        for(int i = 0; i<Integer.parseInt(noofMaps); i++) {
            TournamentMode.getInstance().getResults().add(new ArrayList<String>());
        }
        storeMapFiles();
    }

    /**
     * This method creates the game objects for all the map files.
     */
    public void storeMapFiles() {

        ArrayList<File> mapFiles = new ArrayList<>();
        switch(noofMaps) {

            case "1":
                mapFiles.add(mapFile1);
                break;

            case "2":
                mapFiles.add(mapFile1);
                mapFiles.add(mapFile2);
                break;

            case "3":
                mapFiles.add(mapFile1);
                mapFiles.add(mapFile2);
                mapFiles.add(mapFile3);
                break;

            case "4":
                mapFiles.add(mapFile1);
                mapFiles.add(mapFile2);
                mapFiles.add(mapFile3);
                mapFiles.add(mapFile4);
                break;

            case "5":
                mapFiles.add(mapFile1);
                mapFiles.add(mapFile2);
                mapFiles.add(mapFile3);
                mapFiles.add(mapFile4);
                mapFiles.add(mapFile5);
                break;
        }
        TournamentMode.getInstance().setMapFiles(mapFiles);
    }


}
