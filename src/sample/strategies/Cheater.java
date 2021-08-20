package sample.strategies;

import javafx.collections.ObservableList;
import sample.model.Player;
import sample.model.Territories;

import java.util.Vector;

/**
 * This class implements the Strategy interface methods.
 * @author Team43
 */
public class Cheater implements Strategy {

    /**
     * An empty constructor.
     */
    public Cheater() {
    }

    /**
     * This function reinforces all the countries by doubling the armies of the territory.
     * @param playersList
     * @param currentPlayer
     * @return null
     */
    @Override
    public String reinforce(ObservableList<Player> playersList, int currentPlayer) {

        for(String key : playersList.get(currentPlayer).getTerritoriesHeld().keySet()) {
            Territories territory = playersList.get(currentPlayer).getTerritoriesHeld().get(key);
            playersList.get(currentPlayer).getTerritoriesHeld().get(key).increaseArmyCountByValue(territory.getArmiesHeld());
            System.out.println("Doubling the armies of the country = " + territory.getTerritorieName());
        }
        playersList.get(currentPlayer).setPlayerArmies(0);
        return "Done";
    }

    /**
     * Method returns the attacking countries
     * @param playersList
     * @param currentPlayer
     * @param isPlayerAttackerOrDefender
     * @return country name
     */
    @Override
    public String attack(ObservableList<Player> playersList, int currentPlayer,String isPlayerAttackerOrDefender) {

        Vector<String> territories = new Vector<>();

        for(String key : playersList.get(currentPlayer).getTerritoriesHeld().keySet()) {
            Territories territory = playersList.get(currentPlayer).getTerritoriesHeld().get(key);
            for(String adjacentTerritory : territory.getAdjacentTerritories()) {
                if(!playersList.get(currentPlayer).getTerritoriesHeld().containsKey(adjacentTerritory)) {
                    if(!territories.contains(adjacentTerritory)) {
                        territories.add(adjacentTerritory);
                    }
                }
            }
        }
        String str = "";
        if(!territories.isEmpty()) {
            for(String country : territories) {
                str += country + ",";
            }
        }
        return str.trim();
    }

    /**
     * This method fortifies the armies of the countries having neightbours.
     * @param playersList
     * @param currentPlayer
     * @param isToFindFortifyingCountryOrSourceCountry
     * @return null
     */
    @Override
    public String fortify(ObservableList<Player> playersList, int currentPlayer,String isToFindFortifyingCountryOrSourceCountry) {

        String result = "";
        for(String key : playersList.get(currentPlayer).getTerritoriesHeld().keySet()) {
            Territories territory = playersList.get(currentPlayer).getTerritoriesHeld().get(key);
            boolean hasNeightbour = false;
            for(String adjacentTerritory : territory.getAdjacentTerritories()) {
                if(!playersList.get(currentPlayer).getTerritoriesHeld().containsKey(adjacentTerritory)) {
                    hasNeightbour = true;
                    break;
                }
            }
            if(hasNeightbour) {
                territory.increaseArmyCountByValue(territory.getArmiesHeld());
                result = result + territory.getTerritorieName() + ",";
                System.out.println("The " + territory.getTerritorieName() + " country armies has been doubled");
            }
        }
        return result.trim();
    }

}
