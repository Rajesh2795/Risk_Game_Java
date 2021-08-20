package sample.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class contains information about the continent and methods to store and retrieve information.
 * @author Team43
 */
public class Continent implements Serializable {

    private String continentName;
    private int continentScore;
    private ArrayList<String> territoriesHeld;

    /**
     * Constructor with parameters.
     * @param continentName
     * @param continentScore
     */
    public Continent(String continentName, int continentScore,ArrayList<String> territoriesHeld) {
        this.continentName = continentName;
        this.continentScore = continentScore;
        this.territoriesHeld = territoriesHeld;
    }

    /**
     * set the continent name.
     * @param continentName
     */
    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    /**
     * set the continent score.
     * @param continentScore
     */
    public void setContinentScore(int continentScore) {
        this.continentScore = continentScore;
    }


    /**
     * set the territories.
     * @param territoriesHeld
     */
    public void setTerritoriesHeld(ArrayList<String> territoriesHeld) {
        this.territoriesHeld = territoriesHeld;
    }

    /**
     * return the continent name.
     * @return continentName
     */
    public String getContinentName() {
        return continentName;
    }

    /**
     * return continent score.
     * @return continentScore
     */
    public int getContinentScore() {
        return continentScore;
    }

    /**
     * return the adjacent territories.
     * @return territoriesHeld
     */
    public ArrayList<String> getTerritoriesHeld() {
        return territoriesHeld;
    }

    /**
     * To copy elements from one array list to another.
     * @param territoriesList
     */
    public void copyArrayListElementsToAnother(ArrayList<String> territoriesList) {

        for(int i = 0; i<territoriesList.size(); i++) {
            territoriesHeld.add(territoriesList.get(i));
        }
    }
}
