package sample.strategies;

import javafx.collections.ObservableList;
import sample.model.Player;

/**
 * This interface contains methods to handle the phases of the game.
 * @author Team43
 */
public interface Strategy {

    /**
     * This method implements the reinforcement phase using player strategies.
     * @param playersList
     * @param currentPlayer
     * @return country name
     */
    String reinforce(ObservableList<Player> playersList, int currentPlayer);

    /**
     * This method implements the attack phase using player strategies.
     * @param playersList
     * @param currentPlayer
     * @return country name
     */
    String attack(ObservableList<Player> playersList, int currentPlayer,String isPlayerAttackerOrDefender);

    /**
     * This method implements the fortification using the player strategies.
     * @param playersList
     * @param currentPlayer
     * @return country name
     */
    String fortify(ObservableList<Player> playersList, int currentPlayer,String isToFindFortifyingCountryOrSourceCountry);
}
