package sample.model;

import java.util.ArrayList;

/**
 * This class is used to store the Map.
 * @author Team43
 */
public class Map {

    private static Map mapInstance = new Map();
    private ArrayList<Territories> territoriesList = new ArrayList<Territories>();
    private ArrayList<Continent> continentList = new ArrayList<Continent>();

    /**
     * Empty constructor.
     */
    public Map() {
    }

    /**
     * Constructor with parameters.
     * @param territoriesList
     * @param continentList
     */
    public Map(ArrayList<Territories> territoriesList, ArrayList<Continent> continentList) {
        this.territoriesList = territoriesList;
        this.continentList = continentList;
    }

    /**
     * This method is used to set the territories list.
     * @param territoriesList
     */
    public void setTerritoriesList(ArrayList<Territories> territoriesList) {
        this.territoriesList = territoriesList;
    }

    /**
     * This method is used to set the continent list.
     * @param continentList
     */
    public void setContinentList(ArrayList<Continent> continentList) {
        this.continentList = continentList;
    }

    /**
     * This method is used to return territories list.
     * @return territoriesList
     */
    public ArrayList<Territories> getTerritoriesList() {
        return territoriesList;
    }

    /**
     * This method is used to return the continent list.
     * @return continentList
     */
    public ArrayList<Continent> getContinentList() {
        return continentList;
    }

    /**
     * returns the Map object.
     * @return mapInstance
     */
    public static Map getMapInstance() {
        return mapInstance;
    }
}
