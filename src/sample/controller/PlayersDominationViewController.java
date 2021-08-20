package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sample.model.Continent;
import sample.model.GameDetails;

import java.util.Observable;
import java.util.Observer;

/**
 * This controller is controls the players domination view window
 * This controller responsible for displaying the actions happening in the background of the game.
 * @author Team43
 */
public class PlayersDominationViewController implements Observer {

    @FXML
    public TextArea dominationViewTextArea;

    /**
     * This method initializes the default text to the text area.
     */
    @FXML
    public void initialize() {

        this.dominationViewTextArea.setText("This window will display the background actions of the game.");
        this.dominationViewTextArea.appendText("\n");
        this.dominationViewTextArea.appendText("........................................................................." +
                "..................................................................................................."
                );
        this.dominationViewTextArea.appendText("\n");
    }

    /**
     * This method adds the text to the text area.
     * @param text
     */
    @FXML
    public void addTextToTextArea(String text) {

        this.dominationViewTextArea.appendText(text);
        this.dominationViewTextArea.appendText("\n");
    }

    /**
     * To set the text to the view.
     * @param text
     */
    @FXML
    public void setTextToTextArea(String text) {

        this.dominationViewTextArea.setText(text);
        this.dominationViewTextArea.appendText("\n");
    }

    /**
     * To clear the contents on the view.
     */
    @FXML
    public void clearTextInTextArea() {

        this.dominationViewTextArea.clear();
    }
    /**
     * This method closes the window.
     */
    public void closeWindow() {

        Stage stage = (Stage) dominationViewTextArea.getScene().getWindow();
        stage.close();
    }

    /**
     * This function updates and prints the contents to the view when there is any change using observers pattern.
     * @param o
     * @param args
     */
    @Override
    public void update(Observable o, Object args) {

        clearTextInTextArea();
        setTextToTextArea("PLAYER STATS.............................................................................");
        int noOfPlayer = ((GameDetails) o).getPlayersList().size();
        int mapSize = ((GameDetails) o).getMapSize();

        for(int i = 0; i< noOfPlayer; i++) {
            int percentage = calculatePercentageOfMapControlledByPlayer(o,i,mapSize);
            String continentList = getContinentListOfAPlayer(o,i);
            int noofArmies = ((GameDetails) o).getPlayersList().get(i).getPlayerArmies();
            StringBuilder builder = new StringBuilder();
            int index = i+1;
            builder.append("Player " + index +" Stats" +"\n")
                    .append("Percentage of Map Controlled by the Player = " + percentage)
                    .append("\n" +"Continents Held = " +"\n" + continentList)
                    .append("\nNo of armies Held by the player = "+noofArmies)
                    .append("\n");
            addTextToTextArea(builder.toString());
        }
        addTextToTextArea("---------------------------------------------------------------------------------");
    }

    /**
     * To calculate the percentage of map controlled by the player
     * @param o
     * @return percentage
     */
    public int calculatePercentageOfMapControlledByPlayer(Observable o,int index,int mapSize) {

        int percentage = (((GameDetails) o).getPlayersList().get(index).getTerritoriesHeld().size()*100) / mapSize;
        return percentage;
    }

    /**
     * This function will return the player continent list.
     * @param o
     * @param index
     * @return continentList
     */
    public String getContinentListOfAPlayer(Observable o,int index) {

        String continentList = "";
        if(!((GameDetails) o).getPlayersList().get(index).getContinentHeld().isEmpty()) {
            for(String key : ((GameDetails) o).getPlayersList().get(index).getContinentHeld().keySet()) {
                continentList = continentList + ((GameDetails) o).getPlayersList().get(index).getContinentHeld().get(key).getContinentName();
                continentList = continentList + "\n";
            }
            return continentList.trim();
        }
        return "Empty";
    }
}
