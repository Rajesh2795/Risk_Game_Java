package sample.test;

import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ComboBox;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import sample.controller.GameparametersDialogController;

import static org.junit.Assert.*;

/**
 * This test class tests the game parameters are taking correctly or not.
 * @author Team43
 */
public class GameparametersDialogControllerTest1 {

    private GameparametersDialogController GDCObject = new GameparametersDialogController();
    private int noofPlayers;

    /**
     * This method check whether the user input correctly retrieved or not.
     */
    @Test
    public void getNumberofPlayers() {

        JFXPanel fxPanel = new JFXPanel();
        final ComboBox<Integer> selectnoofPlayers = new ComboBox<>();
        selectnoofPlayers.getItems().addAll(2,3,4,5,6);
        selectnoofPlayers.getSelectionModel().select(2);

        noofPlayers = (int) selectnoofPlayers.getSelectionModel().getSelectedItem();
        assertEquals(4,noofPlayers);
    }
}