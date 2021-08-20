package sample.strategies;

import javafx.collections.ObservableList;
import sample.model.Constants;
import sample.model.Player;
import sample.model.Territories;

import java.util.*;
import java.util.Random;

/**
 * This class implements the Strategy interface methods.
 * @author Team43
 */
public class Aggressive implements Strategy {

    private Territories strongestTerritory;

    /**
     * An empty constructor.
     */
    public Aggressive() {
    }

    /**
     * Returns the reinforcing country.
     * @param playersList
     * @param currentPlayer
     * @return country name
     */
    @Override
    public String reinforce(ObservableList<Player> playersList, int currentPlayer) {

        Territories territory = getStrongestCountry(playersList.get(currentPlayer).getTerritoriesHeld());
        if(territory != null) {
            int noofArmies = playersList.get(currentPlayer).getPlayerArmies();
            playersList.get(currentPlayer).getTerritoriesHeld().get(territory.getTerritorieName())
                    .increaseArmyCountByValue(noofArmies);
            playersList.get(currentPlayer).decreaseArmyCountByValue(noofArmies);
            return territory.getTerritorieName();
        }
        return "";
    }

    /**
     * This method returns the strongest country to start the attack from and attacking country.
     * @param playersList
     * @param currentPlayer
     * @param isPlayerAttackerOrDefender
     * @return country name
     */
    @Override
    public String attack(ObservableList<Player> playersList, int currentPlayer,String isPlayerAttackerOrDefender) {

        if(isPlayerAttackerOrDefender.equalsIgnoreCase(Constants.ATTACKER)) {
            Territories strongestCountry = getStrongestCountry(playersList.get(currentPlayer).getTerritoriesHeld());
            if(strongestCountry != null) {
                playersList.get(currentPlayer).setAttackingCountry(strongestCountry);
                return strongestCountry.getTerritorieName();
            }

        } else {
            // get the neighbouring country of the attacker country
            if(playersList.get(currentPlayer).getAttackingCountry() != null) {
                Territories strongestCountry = playersList.get(currentPlayer).getAttackingCountry();
                String attackedCountry = getAttackedCountry(strongestCountry, playersList.get(currentPlayer).getTerritoriesHeld());
                if (attackedCountry != null) {
                    if(!attackedCountry.trim().isEmpty()) {
                        playersList.get(currentPlayer).setDefendingCountry(attackedCountry);
                        return attackedCountry;
                    }
                }
            }
        }
        return "";
    }

    /**
     * Returns the source country from where fortification is done and the fortifying country.
     * @param playersList
     * @param currentPlayer
     * @return country name
     */
    @Override
    public String fortify(ObservableList<Player> playersList, int currentPlayer,String isToFindFortifyingCountryOrSourceCountry) {

        if(isToFindFortifyingCountryOrSourceCountry.equalsIgnoreCase(Constants.SOURCECOUNTRY)) {
            Territories sourceTerritory = getStrongestCountryAdjacentStrongestCountry(playersList.get(currentPlayer).getTerritoriesHeld());
            playersList.get(currentPlayer).setSourceCountry(sourceTerritory);

            if(sourceTerritory !=null && sourceTerritory.getArmiesHeld() > 1) {
                return sourceTerritory.getTerritorieName();
            }
        } else {
            Territories strongCountry = getStrongestCountry(playersList.get(currentPlayer).getTerritoriesHeld());
            if(strongCountry != null) {
                playersList.get(currentPlayer).setFortifyingCountry(strongCountry);
                return strongCountry.getTerritorieName();
            }
        }
        return "";
    }



    /**
     * This method returns the strongest country to start the attack from.
     * @param territoriesHeld
     * @return country name
     */
    public Territories getStrongestCountry(HashMap<String,Territories> territoriesHeld) {

        Territories strongestCountry = territoriesHeld.get(territoriesHeld.keySet().toArray()[0]);

        for(String key : territoriesHeld.keySet()) {
            Territories territory = territoriesHeld.get(key);
            if(territory.getArmiesHeld() > strongestCountry.getArmiesHeld()) {
                strongestCountry = territory;
            }
        }

        if(strongestCountry.getArmiesHeld() == 1) {
            Random r = new Random();
            List<String> keyset = new ArrayList<>(territoriesHeld.keySet());
            strongestCountry = territoriesHeld.get(keyset.get(r.nextInt(keyset.size())));
        }

        return strongestCountry;
    }

    /**
     * This method returns the adjacent country to the strongest country to attack.
     * @param strongestCountry
     * @param territoriesHeld
     * @return adjacent country
     */
    public String getAttackedCountry(Territories strongestCountry,HashMap<String,Territories> territoriesHeld) {

        String attackedTerritory = null;

        for(String adjacentTerritory : strongestCountry.getAdjacentTerritories()) {
            if(!territoriesHeld.containsKey(adjacentTerritory)) {
                attackedTerritory = adjacentTerritory;
                break;
            }
        }

        return attackedTerritory;
    }

    /**
     * Returns the second largest country from which we fortify
     * @return country name
     */
    public Territories getStrongestCountryAdjacentStrongestCountry(HashMap<String,Territories> territoriesHeld) {

        strongestTerritory = getStrongestCountry(territoriesHeld);
        Territories fromFortifyingCountry = null;

        for(String key : territoriesHeld.keySet()) {
            Territories teritory = territoriesHeld.get(key);
            if(!teritory.getTerritorieName().equalsIgnoreCase(strongestTerritory.getTerritorieName()) &&
                    (teritory.getArmiesHeld() > 1) && strongestTerritory.getAdjacentTerritories().contains(teritory.getTerritorieName())) {
                if(fromFortifyingCountry == null) {
                    fromFortifyingCountry = teritory;
                } else if(teritory.getArmiesHeld() > fromFortifyingCountry.getArmiesHeld()) {
                    fromFortifyingCountry = teritory;
                }
            }
        }

        return fromFortifyingCountry;
    }
}
