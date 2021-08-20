package sample.strategies;

import javafx.collections.ObservableList;
import sample.model.Constants;
import sample.model.Player;
import sample.model.Territories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

/**
 * This class implements the Strategy interface methods.
 */
public class Random implements Strategy {

    public Random() {
    }

    /**
     * Returns the reinforcing country.
     * @param playersList
     * @param currentPlayer
     * @return reinforcing country
     */
    @Override
    public String reinforce(ObservableList<Player> playersList, int currentPlayer) {

        Territories reinforcingCountry = getRandomTerritory(playersList.get(currentPlayer).getTerritoriesHeld());
        if(reinforcingCountry != null) {
            int noofArmies = playersList.get(currentPlayer).getPlayerArmies();
            playersList.get(currentPlayer).getTerritoriesHeld().get(reinforcingCountry.getTerritorieName())
                    .increaseArmyCountByValue(noofArmies);
            playersList.get(currentPlayer).decreaseArmyCountByValue(noofArmies);
        }
        return reinforcingCountry.getTerritorieName();
    }

    @Override
    public String attack(ObservableList<Player> playersList, int currentPlayer,String isPlayerAttackerOrDefender) {

        if(isPlayerAttackerOrDefender.equalsIgnoreCase(Constants.ATTACKER)) {
            String randomAttackingCountry = getRandomTerritoryToAttack(playersList.get(currentPlayer).getTerritoriesHeld());
            if(!randomAttackingCountry.trim().isEmpty()) {
                Territories attackingTerritory = playersList.get(currentPlayer).getTerritoriesHeld().get(randomAttackingCountry);
                playersList.get(currentPlayer).setAttackingCountry(attackingTerritory);
                return randomAttackingCountry;
            }

        } else if(isPlayerAttackerOrDefender.equalsIgnoreCase(Constants.DEFENDER)) {
            if(playersList.get(currentPlayer).getAttackingCountry() != null) {
                String defendingCountry = getNeighbourCountryToAttack(playersList.get(currentPlayer).getAttackingCountry().getTerritorieName(),
                        playersList.get(currentPlayer).getTerritoriesHeld());
                playersList.get(currentPlayer).setDefendingCountry(defendingCountry);
                return defendingCountry;
            }
        }
        return "";
    }

    /**
     * returns the fortifying country and source country.
     * @param playersList
     * @param currentPlayer
     * @param isToFindFortifyingCountryOrSourceCountry
     * @return country name
     */
    @Override
    public String fortify(ObservableList<Player> playersList, int currentPlayer,String isToFindFortifyingCountryOrSourceCountry) {

        if(isToFindFortifyingCountryOrSourceCountry.equalsIgnoreCase(Constants.SOURCECOUNTRY)) {
            Territories sourceCountry = getRandomTerritoryToFortifyFrom(playersList.get(currentPlayer).getTerritoriesHeld());
            if(sourceCountry != null) {
                playersList.get(currentPlayer).setSourceCountry(sourceCountry);
                return sourceCountry.getTerritorieName();
            }

        } else {
            Territories fortifyingCountry = getRandomFortifyingCountry(playersList.get(currentPlayer).getSourceCountry(),
                    playersList.get(currentPlayer).getTerritoriesHeld());
            if(fortifyingCountry != null) {
                playersList.get(currentPlayer).setFortifyingCountry(fortifyingCountry);
                return fortifyingCountry.getTerritorieName();
            }
        }
        return "";
    }

    /**
     * This function returns the random territory from the player occupied territories.
     * @param territoriesHeld
     * @return random territory
     */
    public Territories getRandomTerritory(HashMap<String,Territories> territoriesHeld) {

        java.util.Random r = new java.util.Random();
        List<String> keyset = new ArrayList<>(territoriesHeld.keySet());
        Territories randomCountry = territoriesHeld.get(keyset.get(r.nextInt(keyset.size())));
        return randomCountry;
    }

    /**
     * Returns the country to start the attack from
     * @param territoriesHeld
     * @return country name
     */
    public String getRandomTerritoryToAttack(HashMap<String,Territories> territoriesHeld) {

        java.util.Random random = new java.util.Random();
        Vector<Territories> territories = new Vector<>();
        for(String key : territoriesHeld.keySet()) {
            Territories territory = territoriesHeld.get(key);
            if(territory.getArmiesHeld() > 1) {
                for(String adjacentKey : territory.getAdjacentTerritories()) {
                    if(!territoriesHeld.containsKey(adjacentKey)) {
                        territories.add(territory);
                    }
                }
            }
        }

        if(territories.size() > 1) {
            Territories randomTerritoryToAttack = territories.get(random.nextInt(territories.size()));
            return randomTerritoryToAttack.getTerritorieName();
        }

        return "";
    }

    public String getNeighbourCountryToAttack(String countryName,HashMap<String,Territories> territoriesHeld) {

        java.util.Random random = new java.util.Random();
        Territories attackingCountry = territoriesHeld.get(countryName);
        Vector<String> attackingTerritories = new Vector<>();
        for(String adjacentCountries : attackingCountry.getAdjacentTerritories()) {
            if(!territoriesHeld.containsKey(adjacentCountries)) {
                attackingTerritories.add(adjacentCountries);
            }
        }

        if(attackingTerritories.size() > 1) {
            String attackingTerritory = attackingTerritories.get(random.nextInt(attackingTerritories.size()));
            return attackingTerritory;
        }
        return "";
    }

    /**
     * This function returns the fortify from country.
     * @param territoriesHeld
     * @return country name
     */
    public Territories getRandomTerritoryToFortifyFrom(HashMap<String,Territories> territoriesHeld) {

        java.util.Random random = new java.util.Random();
        Territories fortifyFrom = null;
        Vector<Territories> territories = new Vector<>();
        for(String key : territoriesHeld.keySet()) {
            Territories territory = territoriesHeld.get(key);
            if(territory.getArmiesHeld() > 1) {
                territories.add(territory);
            }
        }

        if(territories.size() > 1) {
            fortifyFrom = territories.get(random.nextInt(territories.size()));
        }
        return fortifyFrom;
    }

    /**
     * Method returs the fortifying country.
     * @param fortifyFrom
     * @param territoriesHeld
     * @return fortifying country
     */
    public Territories getRandomFortifyingCountry(Territories fortifyFrom,HashMap<String,Territories> territoriesHeld) {

        java.util.Random random = new java.util.Random();
        Vector<Territories> territories = new Vector<>();
        for(String key : territoriesHeld.keySet()) {
            Territories territory = territoriesHeld.get(key);
            if(!fortifyFrom.getTerritorieName().equalsIgnoreCase(territory.getTerritorieName()) && fortifyFrom.getAdjacentTerritories()
                    .contains(territory.getTerritorieName())) {
                if(!territories.contains(territory)) {
                    territories.add(territory);
                }
            }
        }

        Territories fortifyingCountry = null;
        if(territories.size() > 1) {
            fortifyingCountry = territories.get(random.nextInt(territories.size()));
        }
        return fortifyingCountry;
    }
}
