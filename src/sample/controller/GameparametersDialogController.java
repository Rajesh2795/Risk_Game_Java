package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import sample.model.Constants;
import sample.model.GameDetails;

import java.io.File;
import java.util.HashMap;

/**
 * This class contains the methods that are used to get the no of players based on the user selection
 * and their characters and the choosen map.
 * All these details are retrieved and stored.
 * @author Team43
 */
public class GameparametersDialogController {

    @FXML
    private ComboBox NumberofPlayers,player1,player2,player3,player4,player5,player6;

    @FXML
    private Label labelPlayer1,labelPlayer2,labelPlayer3,labelPlayer4,labelPlayer5,labelPlayer6;

    private Boolean setPlayer1,setPlayer2,setPlayer3,setPlayer4,setPlayer5,setPlayer6;

    @FXML
    private Label mapLabel;

    @FXML
    private Button chooseMapbutton;

    @FXML
    private DialogPane gameSettingsdialog;

    private File selectedfile;

    private String noofPlayers;

    private HashMap<String,String> playerCharacters = new HashMap<String,String>();

    /**
     * Get the number of players count.
     */
    public void getNumberofPlayers() {

        noofPlayers = (String) NumberofPlayers.getSelectionModel().getSelectedItem();
        System.out.println("User has selected number of players to be = "+noofPlayers);
        disableMapbutton(false);
        setPlayersCharacters(noofPlayers);
    }

    /**
     * This method is used to display combo boxes based on number of players choosed.
     * @param noofPlayers
     */
    public void setPlayersCharacters(String noofPlayers) {

        disablePlayerCombobox(false,false,false,false,false,false);

        switch (noofPlayers) {
            case "2":
                disablePlayerCombobox(true,true,false,false,false,false);
                disableMapbutton(true);
                break;
            case "3":
                disablePlayerCombobox(true,true,true,false,false,false);
                disableMapbutton(true);
                break;
            case "4":
                disablePlayerCombobox(true,true,true,true,false,false);
                disableMapbutton(true);
                break;
            case "5":
                disablePlayerCombobox(true,true,true,true,true,false);
                disableMapbutton(true);
                break;
            case "6":
                disablePlayerCombobox(true,true,true,true,true,true);
                disableMapbutton(true);
                break;

        }
    }

    /**
     * This method is used to set visible or hide the combo boxes for the players to select the characters
     * @param setPlayer1
     * @param setPlayer2
     * @param setPlayer3
     * @param setPlayer4
     * @param setPlayer5
     * @param setPlayer6
     */
    public void disablePlayerCombobox(Boolean setPlayer1,Boolean setPlayer2,Boolean setPlayer3,
                                      Boolean setPlayer4,Boolean setPlayer5,Boolean setPlayer6) {

        labelPlayer1.setVisible(setPlayer1);
        player1.setVisible(setPlayer1);
        labelPlayer2.setVisible(setPlayer2);
        player2.setVisible(setPlayer2);
        labelPlayer3.setVisible(setPlayer3);
        player3.setVisible(setPlayer3);
        labelPlayer4.setVisible(setPlayer4);
        player4.setVisible(setPlayer4);
        labelPlayer5.setVisible(setPlayer5);
        player5.setVisible(setPlayer5);
        labelPlayer6.setVisible(setPlayer6);
        player6.setVisible(setPlayer6);

    }


    /**
     * This method is used to set map button to visible state or hide.
     * @param disablemap
     */
    public void disableMapbutton(Boolean disablemap) {

        mapLabel.setVisible(disablemap);
        chooseMapbutton.setVisible(disablemap);
    }

    /**
     * This method is used to choose the map file using file chooser.
     */
    public void chooseMap() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Map");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Map files(*.map)","*.map"));
        selectedfile = fileChooser.showOpenDialog(gameSettingsdialog.getScene().getWindow());
    }

    /**
     * Used to store the game settings to the game details class using game details class object.
     */
    public void createGameinstance() {

        StorePlayercharacters();
        GameDetails object = new GameDetails(Integer.parseInt(noofPlayers),selectedfile,playerCharacters,
                Constants.NEWGAME,Constants.STARTPHASE,Constants.SINGLEMODE,0);
        GameDetails.getGamedetails().addgamedetailsInstance(object);
    }

    /**
     * Used to store the player characters to the hashmap based on the number of players.
     */
    public void StorePlayercharacters() {

        switch (noofPlayers) {
            case "2":
                playerCharacters.put("Player1",(String) player1.getValue());
                playerCharacters.put("Player2",(String) player2.getValue());
                break;
            case "3":
                playerCharacters.put("Player1",(String) player1.getValue());
                playerCharacters.put("Player2",(String) player2.getValue());
                playerCharacters.put("Player3",(String) player3.getValue());
                break;
            case "4":
                playerCharacters.put("Player1",(String) player1.getValue());
                playerCharacters.put("Player2",(String) player2.getValue());
                playerCharacters.put("Player3",(String) player3.getValue());
                playerCharacters.put("Player4",(String) player4.getValue());
                break;
            case "5":
                playerCharacters.put("Player1",(String) player1.getValue());
                playerCharacters.put("Player2",(String) player2.getValue());
                playerCharacters.put("Player3",(String) player3.getValue());
                playerCharacters.put("Player4",(String) player4.getValue());
                playerCharacters.put("Player5",(String) player5.getValue());
                break;
            case "6":
                playerCharacters.put("Player1",(String) player1.getValue());
                playerCharacters.put("Player2",(String) player2.getValue());
                playerCharacters.put("Player3",(String) player3.getValue());
                playerCharacters.put("Player4",(String) player4.getValue());
                playerCharacters.put("Player5",(String) player5.getValue());
                playerCharacters.put("Player6",(String) player6.getValue());
                break;
        }
    }
}
