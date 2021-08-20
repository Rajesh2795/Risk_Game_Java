package sample.strategies;

import javafx.collections.ObservableList;
import sample.model.Constants;
import sample.model.Player;
import sample.model.Territories;

import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

/**
 * This class implements the Strategy interface methods.
 * @author Team43
 */
public class Benevolent implements Strategy {

    Territories weakestCountry;

    /**
     * An empty constructor.
     */
    public Benevolent() {
    }

    /**
     * This method finds the weakest country to reinforce.
     * @param playersList
     * @param currentPlayer
     * @return country name
     */
    @Override
    public String reinforce(ObservableList<Player> playersList, int currentPlayer) {

        String reinforcingCountry = getWeakestTerritory(playersList.get(currentPlayer).getTerritoriesHeld());
        if(reinforcingCountry != null) {
            int noofArmies = playersList.get(currentPlayer).getPlayerArmies();
            playersList.get(currentPlayer).getTerritoriesHeld().get(reinforcingCountry).increaseArmyCountByValue(noofArmies);
            playersList.get(currentPlayer).decreaseArmyCountByValue(noofArmies);
        }
        return reinforcingCountry;
    }

    /**
     * This method skips to the next phase.
     * @param playersList
     * @param currentPlayer
     * @param isPlayerAttackerOrDefender
     * @return null
     */
    @Override
    public String attack(ObservableList<Player> playersList, int currentPlayer,String isPlayerAttackerOrDefender) {
        // Do nothing skip to next phase.
        return "";
    }

    /**
     * This method fortifies the weakest country with the strongest country adjacent to the weakest country
     * @param playersList
     * @param currentPlayer
     * @param isToFindFortifyingCountryOrSourceCountry
     * @return country name
     */
    @Override
    public String fortify(ObservableList<Player> playersList, int currentPlayer,String isToFindFortifyingCountryOrSourceCountry) {

        // To get the source country
        if(isToFindFortifyingCountryOrSourceCountry.equalsIgnoreCase(Constants.SOURCECOUNTRY)) {
            Territories strongestAdjacent = getWeakestTerritoryStrongestAdjacent(playersList.get(currentPlayer).getTerritoriesHeld());
            if(strongestAdjacent != null) {
                playersList.get(currentPlayer).setSourceCountry(playersList.get(currentPlayer).getTerritoriesHeld()
                        .get(strongestAdjacent.getTerritorieName()));
                playersList.get(currentPlayer).setFortifyingCountry(weakestCountry);
                return strongestAdjacent.getTerritorieName();
            }

        // To get the fortifying country.
        } else if(isToFindFortifyingCountryOrSourceCountry.equalsIgnoreCase(Constants.FORTIFYINGCOUNTRY)) {
            if(playersList.get(currentPlayer).getFortifyingCountry() != null) {
                String weakestTerritory = playersList.get(currentPlayer).getFortifyingCountry().getTerritorieName();
                return weakestTerritory;
            }
        }
        return "";
    }

    /**
     * This method returns the weakest territory from the territories of the player.
     * @param territoriesHeld
     * @return  country name
     */
    public String getWeakestTerritory(HashMap<String, Territories> territoriesHeld) {

        Territories weakTerritory = territoriesHeld.get(territoriesHeld.keySet().toArray()[0]);
        Vector<Territories> territories = new Vector<>();
        for(String key : territoriesHeld.keySet()) {
            Territories territory = territoriesHeld.get(key);
            if(territory.getArmiesHeld() < weakTerritory.getArmiesHeld()) {
                weakTerritory = territory;
            }

            if(territory.getArmiesHeld() == 1) {
                territories.add(territory);
            }
        }
        if(territories.size() > 1) {
            Random r = new Random();
            weakTerritory = territories.get(r.nextInt(territories.size()));
        }
        return weakTerritory.getTerritorieName();
    }

    /**
     * This function finds the strongest adjacent territory to the weakest territory to fortify the
     * weakest territory.
     * @param territoriesHeld
     * @return country name
     */
    public Territories getWeakestTerritoryStrongestAdjacent(HashMap<String, Territories> territoriesHeld) {

        String weakestTerritory = getWeakestTerritory(territoriesHeld);
        weakestCountry = territoriesHeld.get(weakestTerritory);
        Territories strongestTerritory = null;

        for(String adjacentTerritory : territoriesHeld.keySet()) {
            Territories territory = territoriesHeld.get(adjacentTerritory);
            if(!weakestCountry.getTerritorieName().equalsIgnoreCase(territory.getTerritorieName()) &&
                    territory.getArmiesHeld() > 1 && weakestCountry.getAdjacentTerritories()
                    .contains(territory.getTerritorieName())) {
                if(strongestTerritory == null) {
                    strongestTerritory = territory;
                } else if(territory.getArmiesHeld() > strongestTerritory.getArmiesHeld()) {
                    strongestTerritory = territory;
                }
            }
        }

        if(strongestTerritory == null) {

            for(String key : territoriesHeld.keySet()) {
                Territories territory = territoriesHeld.get(key);
                if(!weakestCountry.getTerritorieName().equalsIgnoreCase(territory.getTerritorieName()) &&
                        territory.getArmiesHeld() > 1 && weakestCountry.getAdjacentTerritories().contains(territory.getTerritorieName()
                )) {
                    if(strongestTerritory == null) {
                        strongestTerritory = territory;
                        break;
                    }
                }
            }
        }
        return strongestTerritory;
    }


}
