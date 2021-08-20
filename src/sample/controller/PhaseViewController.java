package sample.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import sample.model.GameDetails;

import java.util.Observable;
import java.util.Observer;

/**
 * This class controls the phase view window.
 * This class is responsible for displaying the game current phase and actions happening in the game.
 * @author Team43
 */
public class PhaseViewController implements Observer {

    @FXML
    public TextArea phaseViewTextArea;

    private static PhaseViewController pvcInstance = new PhaseViewController();
    private Object currentGamePhase;

    /**
     * Empty constructor.
     */
    public PhaseViewController() {
    }

    /**
     * This method by default displays the sample message to the window.
     */
    @FXML
    public void initialize() {

        phaseViewTextArea.setText("This windows displays information about the actions in the phase");
        phaseViewTextArea.appendText("\n");
    }

    /**
     * To return the class object.
     * @return class instance
     */
    public static PhaseViewController getPvcInstance() {
        return pvcInstance;
    }

    /**
     * To add the text to the view.
     * @param text
     */
    @FXML
    public void addTextToTextArea(String text) {

        phaseViewTextArea.appendText(text);
        phaseViewTextArea.appendText("\n");
    }

    /**
     * To set the text to the view.
     * @param text
     */
    @FXML
    public void setTextToTextArea(String text) {

        phaseViewTextArea.setText(text);
        phaseViewTextArea.appendText("\n");
    }

    /**
     * To clear the contents on the view.
     */
    @FXML
    public void clearTextInTextArea() {

        phaseViewTextArea.clear();
    }

    /**
     * This method displays the game phase and the current player using observer pattern.
     * @param o
     * @param currentGamePhase
     */
    @Override
    public void update(Observable o,Object currentGamePhase) {

        if(currentGamePhase instanceof Integer) {
            int currentPlayer = ((GameDetails) o).getCurrentPlayer();
            String text = "The current player is = " + ((GameDetails) o).getPlayersList().get(currentPlayer).getPlayerName();
            addTextToTextArea(text);
        } else if(currentGamePhase instanceof String) {
            String GamePhase = ((GameDetails) o).getGamePhase();
            int currentPlayer = ((GameDetails) o).getCurrentPlayer();
            String text = "Game phase has been changed to = " + GamePhase + ". The current player is " +
                    ((GameDetails) o).getPlayersList().get(currentPlayer);
            setTextToTextArea(text);
        }
    }

    /**
     * Function closes the window.
     */
    public void closeWindow() {

        Stage stage = (Stage) phaseViewTextArea.getScene().getWindow();
        stage.close();
    }

}
