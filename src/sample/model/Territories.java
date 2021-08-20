package sample.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is used to maintain territories and methods used to store and retrieve information.
 * @author Team43
 */

public class Territories implements Serializable {

    private String territorieName;
    private int x_Position;
    private int y_Position;
    private int armiesHeld;
    private Boolean hasPlayer;
    private String playerName;
    private String continentName;
    private ArrayList<String> adjacentTerritories;

    /**
     * Constructor with parameters.
     * @param territorieName
     * @param x_Position
     * @param y_Position
     * @param armiesHeld
     * @param hasPlayer
     */
    public Territories(String territorieName, int x_Position, int y_Position, int armiesHeld, Boolean hasPlayer) {
        this.territorieName = territorieName;
        this.x_Position = x_Position;
        this.y_Position = y_Position;
        this.armiesHeld = armiesHeld;
        this.hasPlayer = hasPlayer;
    }

    /**
     * Constructor with parameters.
     * @param territorieName
     * @param x_Position
     * @param y_Position
     * @param continentName
     * @param adjacentTerritories
     */
    public Territories(String territorieName, int x_Position, int y_Position, String continentName, ArrayList<String> adjacentTerritories) {
        this.territorieName = territorieName;
        this.x_Position = x_Position;
        this.y_Position = y_Position;
        this.continentName = continentName;
        this.adjacentTerritories = adjacentTerritories;
    }

    /**
     * To set the territorie name.
     * @param territorieName
     */
    public void setTerritorieName(String territorieName) {
        this.territorieName = territorieName;
    }

    /**
     * To set x-coordinate of the territorie.
     * @param x_Position
     */
    public void setX_Position(int x_Position) {
        this.x_Position = x_Position;
    }

    /**
     * To set y-coordinate of the territorie.
     * @param y_Position
     */
    public void setY_Position(int y_Position) {
        this.y_Position = y_Position;
    }

    /**
     * To set the number of armies.
     * @param armiesHeld
     */
    public void setArmiesHeld(int armiesHeld) {
        this.armiesHeld = armiesHeld;
    }

    public void setHasPlayer(Boolean hasPlayer) {
        this.hasPlayer = hasPlayer;
    }

    /**
     * Set the player occupied the territorie.
     * @param playerName
     */
    public void setPlayer(String playerName) {
        this.playerName = playerName;
    }

    /**
     * set the adjacent territories.
     * @param adjacentTerritories
     */
    public void setAdjacentTerritories(ArrayList<String> adjacentTerritories) {
        this.adjacentTerritories = adjacentTerritories;
    }

    /**
     * To set the continent name.
     * @param continentName
     */
    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    /**
     * get the territorie name.
     * @return territorieName
     */
    public String getTerritorieName() {
        return territorieName;
    }

    /**
     * get the x-coordinate.
     * @return x_Position
     */
    public int getX_Position() {
        return x_Position;
    }

    /**
     * get the y-coordinate.
     * @return y_Position
     */
    public int getY_Position() {
        return y_Position;
    }

    /**
     * get the number of armies held by the territorie.
     * @return armiesHeld
     */
    public int getArmiesHeld() {
        return armiesHeld;
    }

    /**
     * return true if the territorie has a player.
     * @return hasPlayer
     */
    public Boolean getHasPlayer() {
        return hasPlayer;
    }

    /**
     * return the player occupied the territorie.
     * @return player
     */
    public String getPlayer() {
        return playerName;
    }

    /**
     * return the adjacent territories.
     * @return adjacentTerritories
     */
    public ArrayList<String> getAdjacentTerritories() {
        return adjacentTerritories;
    }

    /**
     * To return the continent name.
     * @return continentName
     */
    public String getContinentName() {
        return continentName;
    }

    /**
     * To increament the army count by 1.
     */
    public void increamentarmyCountby1() {
        armiesHeld += 1;
    }

    /**
     * To decreament army count by 1;
     */
    public void decreamentarmyCountby1() {
        armiesHeld -= 1;
    }

    /**
     * To decreament army count by value
     * @param value
     */
    public void decreaseArmyCountByvalue(int value) {
        armiesHeld -= value;
    }

    /**
     * To increase the army count by value.
     * @param value
     */
    public void increaseArmyCountByValue(int value) {

        this.armiesHeld += value;
    }

}
