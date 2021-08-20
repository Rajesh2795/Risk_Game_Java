package sample.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class will store the tournament mode inputs and results.
 * @author Team43
 */
public class TournamentMode {

    private int noofMaps;
    private int noofGames;
    private int noofPlayers;
    private int noofTurns;
    private int index;

    private ArrayList<File> mapFiles = new ArrayList<>();

    private HashMap<String,String> playerCharacters = new HashMap<>();

    private ArrayList<ArrayList<String>> results = new ArrayList<>();

    private static TournamentMode ourInstance = new TournamentMode();

    public static TournamentMode getInstance() {
        return ourInstance;
    }

    private TournamentMode() {
    }

    /**
     * This method sets the no of maps.
     * @param noofMaps
     */
    public void setNoofMaps(int noofMaps) {
        this.noofMaps = noofMaps;
    }

    /**
     * This method sets the no of games.
     * @param noofGames
     */
    public void setNoofGames(int noofGames) {
        this.noofGames = noofGames;
    }

    /**
     * This method sets the no of players.
     * @param noofPlayers
     */
    public void setNoofPlayers(int noofPlayers) {
        this.noofPlayers = noofPlayers;
    }

    /**
     * This method sets the results.
     * @param results
     */
    public void setResults(ArrayList<ArrayList<String>> results) {
        this.results = results;
    }

    /**
     * To set the index
     * @param index
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Stores the user input map files
     * @param mapFiles
     */
    public void setMapFiles(ArrayList<File> mapFiles) {
        this.mapFiles = mapFiles;
    }

    /**
     * Stores the user input characters.
     * @param playerCharacters
     */
    public void setPlayerCharacters(HashMap<String, String> playerCharacters) {
        this.playerCharacters = playerCharacters;
    }

    /**
     * Stores the no of turns.
     * @param noofTurns
     */
    public void setNoofTurns(int noofTurns) {
        this.noofTurns = noofTurns;
    }

    /**
     * Returns the no of turns
     * @return noofTurns
     */
    public int getNoofTurns() {
        return noofTurns;
    }

    /**
     * Returns the map files.
     * @return mapfiles
     */
    public ArrayList<File> getMapFiles() {
        return mapFiles;
    }

    /**
     * Returns the player characters.
     * @return playercharacters.
     */
    public HashMap<String, String> getPlayerCharacters() {
        return playerCharacters;
    }

    /**
     * To get the index.
     * @return index
     */
    public int getIndex() {
        return index;
    }

    /**
     * This method gets the no of maps.
     * @return noofMaps
     */
    public int getNoofMaps() {
        return noofMaps;
    }

    /**
     * This method gets the no of games.
     * @return noofGames
     */
    public int getNoofGames() {
        return noofGames;
    }

    /**
     * This method gets the no of players.
     * @return noofPlayers
     */
    public int getNoofPlayers() {
        return noofPlayers;
    }

    /**
     * This method gets the results.
     * @return results
     */
    public ArrayList<ArrayList<String>> getResults() {
        return results;
    }

    /**
     * This method prints the tournament results to the console.
     */
    public void printResults() {

        System.out.print("        ");
        for(int k = 0; k<noofGames; k++) {
            int var = k+1;
            System.out.print("Game " +var + "   ");
        }
        System.out.println();

        for(int i = 0; i<noofMaps; i++) {
            int temp = i+1;
            System.out.print("Map" +temp + " -> ");
            for(int j = 0; j<noofGames; j++) {
                System.out.print(results.get(i).get(j) + "   ");
            }
            System.out.println();
        }
    }
}
