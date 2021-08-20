package sample.strategies;

import javafx.collections.ObservableList;
import sample.model.Player;

/**
 * This class implements interface strategy
 * @author Team43
 */
public class Human implements Strategy {

    /**
     * This method implements reinforcement method
     * @param playersList
     * @param currentPlayer
     * @return String
     */
    @Override
    public String reinforce(ObservableList<Player> playersList, int currentPlayer) {

        return "";
    }

    /**
     * This method implements attack method
     * @param playersList
     * @param currentPlayer
     * @param isPlayerAttackerOrDefender
     * @return String
     */
    @Override
    public String attack(ObservableList<Player> playersList, int currentPlayer,String isPlayerAttackerOrDefender) {

        return "";
    }

    /**
     * This method implements fortify method
     * @param playersList
     * @param currentPlayer
     * @param isToFindFortifyingCountryOrSourceCountry
     * @return String
     */
    @Override
    public String fortify(ObservableList<Player> playersList, int currentPlayer,String isToFindFortifyingCountryOrSourceCountry) {

        return "";
    }
}
