package sample.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sample.model.Card;
import sample.model.Constants;
import sample.model.GameDetails;
import sample.model.TournamentMode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * This controller class will controls the main user interface.
 * This class loads the mapeditor window, newmap window
 * and game window.
 * This class checks the map validation and if the map is valid
 * then it will load the game window. Otherwise it show the error.
 * @author Team43.
 */
public class Controller {

    @FXML
    private MenuBar menubarId;

    @FXML
    private BorderPane MainBorderPane;

    @FXML
    private Canvas canvasId;

    @FXML
    private Menu newMenuId;

    @FXML
    private MenuItem loadGameMenu;

    private File savedGame;

    /**
     * This methods initializes the elements related to the main user interface window.
     */
    @FXML
    public void initialize() {

        GraphicsContext graphicsContext = canvasId.getGraphicsContext2D();
        Image image = new Image("resources/risk.png");
        graphicsContext.drawImage(image,0,0,canvasId.getWidth(),canvasId.getHeight());
    }

    /**
     * This methods starts the new game and asks about the parameters needed for the game.
     */
    @FXML
    public void startGame() {

        // Intializing the dialog pane and loading the dialog.
        Dialog<ButtonType> gameParametersdialog = new Dialog<>();
        gameParametersdialog.initOwner(MainBorderPane.getScene().getWindow());
        FXMLLoader gameparametersDialogloader = new FXMLLoader();
        gameparametersDialogloader.setLocation(getClass().getResource("/sample/view/Gameparameters.fxml"));

        try {
            gameParametersdialog.getDialogPane().setContent(gameparametersDialogloader.load());

        } catch(IOException e){
            System.out.println("Cannot load the game parameters dialog");
            e.printStackTrace();
            return;
        }

        gameParametersdialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        gameParametersdialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        // if the user has entered finish then read the game parameters and store in the GameDetails object.
        Optional<ButtonType> result = gameParametersdialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.FINISH) {
            GameparametersDialogController gpdc_controller = gameparametersDialogloader.getController();
            gpdc_controller.createGameinstance();


            // Get the index of the Game details object of type new game.
            int index = returnIndex(Constants.NEWGAME);
            GameDetails.getGamedetails().setGameMode(Constants.SINGLEMODE);

            GameDetails object2 = GameDetails.getGamedetails().getgamedetails().get(index);

            System.out.println("The user selected = "+object2.getNumberOfPlayers());

            // Intialize the player objects and player colors and map.
            GameDetails.getGamedetails().IntializeColors(index);
            GameDetails.getGamedetails().IntializePlayers(index);
            GameDetails.getGamedetails().InitializeArmies(index);
            GameDetails.getGamedetails().createMap(index);

            // If map is valid then Load the game window.
            if(GameDetails.getGamedetails().validateMap(index)){
                System.out.println("Map is valid.");
                System.out.println("*********************************************************************************");
                System.out.println("Proceeding to the next step");
                //System.out.println("The player name was "+GameDetails.getGamedetails().getgamedetails().get(0).getPlayersList().get(0).getPlayerName());

                // To distribute the territories to the players.
                GameDetails.getGamedetails().distributeTerritories(index);

                // To distribute the no of armies to the players.
                GameDetails.getGamedetails().distributeArmies(index);

                // To distribute the armies to the territories.
                GameDetails.getGamedetails().distributeArmiestoTerritories(index);

                System.out.println(GameDetails.getGamedetails().getPlayersList().get(0).getTerritoriesHeld().size());
                System.out.println(GameDetails.getGamedetails().getPlayersList().get(1).getTerritoriesHeld().size());

                // To get the name of the map with out .map extension.
                String string = object2.getMapFile().getName();
                string = string.substring(0,string.length()-4);
                System.out.println("Map name with out extension is "+ string);
                GameDetails.getGamedetails().getgamedetails().get(index).setMapName(string);

                loadGamewindow();   // Loads the game window when map is valid.
            } else {
                System.out.println("********************************MAP IS INVALID***********************************");
                System.out.println("----------------------------Cannot Load the Game Window--------------------------");

                // Clear the data.
                GameDetails.getGamedetails().clearData();
            }
        } else {
            System.out.println("Cancel button is pressed");
        }
    }

    /**
     * Returns the index of the gamedetail object based on new game or editmap or newmap.
     * @param typeName
     * @return index
     */
    public int returnIndex(String typeName) {

        int size = GameDetails.getGamedetails().getgamedetails().size();
        int index = -1;

        for(int i = 0; i<size; i++) {
            GameDetails instance = GameDetails.getGamedetails().getgamedetails().get(i);
            if(instance.getTypeName().contains(typeName)) {
                index = i;
                System.out.printf("%s object index in arraylist is = %d%n", typeName, index);
                break;
            }
        }

        return index;
    }

    /**
     * To load the game Window.
     */
    public void loadGamewindow() {

        FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/sample/view/loadGame.fxml"));
        try{
            Parent root1 = gameLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Game window");
            stage.setScene(new Scene(root1,1000,700));
            stage.setResizable(false);
            stage.showAndWait();

            //Stage primaryStage = (Stage) MainBorderPane.getScene().getWindow();
            //primaryStage.hide();

            //stage.setOnHidden(e -> primaryStage.show());

        } catch (Exception e) {
            System.out.println("Cannot load the game window");
            e.printStackTrace();
            return;
        }

    }
    /**
     * This method is used to the edit the map.
     */
    public void editMap() {

        FXMLLoader mapeditLoader = new FXMLLoader(getClass().getResource("/sample/view/Mapeditor.fxml"));
        try {
            Parent root2 = mapeditLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Map Editor Window");
            stage.setScene(new Scene(root2, 900,700));
            stage.setResizable(false);
            stage.show();

            Stage primaryStage = (Stage) MainBorderPane.getScene().getWindow();
            primaryStage.hide();

            stage.setOnHidden(e -> primaryStage.show());

            GameDetails.getGamedetails().clearData();

        } catch (Exception e) {
            System.out.println("Cannot load the map editor window.");
            e.printStackTrace();
            return;
        }
    }

    /**
     * This method is used to construct new map.
     */
    public void constructNewmap() {

        FXMLLoader newMap = new FXMLLoader(getClass().getResource("/sample/view/NewMap.fxml"));
        try {
            Parent newMaproot = newMap.load();
            Stage stage = new Stage();
            stage.setTitle("New Map Window");
            stage.setScene(new Scene(newMaproot,1200,700));
            stage.setResizable(false);
            stage.show();

            Stage primaryStage = (Stage) MainBorderPane.getScene().getWindow();
            primaryStage.hide();

            stage.setOnHidden(e -> primaryStage.show());
            GameDetails.getGamedetails().clearData();
        } catch (Exception e) {
            System.out.println("Cannot load the new map window.");
            e.printStackTrace();
            return;
        }

    }

    /**
     *  This method will clear the game objects.
     */
    public void clearObjects(String typeName) {

        int index = -1;
        for(int i = 0; i<GameDetails.getGamedetails().getgamedetails().size(); i++) {
            GameDetails gd = GameDetails.getGamedetails().getgamedetails().get(i);
            if(gd.getTypeName().equalsIgnoreCase(typeName)) {
                index = i;
            }
        }
        GameDetails.getGamedetails().getgamedetails().remove(index);
        GameDetails.getGamedetails().getTerritoriesList().clear();
        GameDetails.getGamedetails().getContinentList().clear();
        GameDetails.getGamedetails().getPlayerColors().clear();
        GameDetails.getGamedetails().getNoofArmies().clear();
    }

    /**
     * This method loads the saved Game
     */
    public void loadsavedgame() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Saved Game");
        //fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("SER files(*.SER)","*.map"));
        savedGame = fileChooser.showOpenDialog(MainBorderPane.getScene().getWindow());

        if(savedGame != null) {
            try{
                FileInputStream fi = new FileInputStream(savedGame);
                ObjectInputStream oi = new ObjectInputStream(fi);
                GameDetails gd = (GameDetails) oi.readObject();
                if(gd.getGameMode().equalsIgnoreCase(Constants.SINGLEMODE)) {
                    GameDetails.getGamedetails().setGameMode(Constants.SINGLEMODE);
                }
                GameDetails.getGamedetails().getgamedetails().add(gd);
                verifyTheSavedGameAndLoadTheGame();
            } catch (Exception e) {
                System.out.println("Cannot load the saved game.");
                e.printStackTrace();
                return;
            }
        }
    }

    /**
     * This function will initializes the player colors and loads the game.
     */
    public void verifyTheSavedGameAndLoadTheGame() {

        int index = returnIndex(Constants.NEWGAME);
        GameDetails.getGamedetails().IntializeColors(index);
        GameDetails.getGamedetails().setPlayercolors(index);
        loadGamewindow();
    }

    /**
     * This method loads the tournament mode.
     */
    public void tournamentMode() {

        Dialog<ButtonType> tournamentDialog = new Dialog<>();
        tournamentDialog.initOwner(MainBorderPane.getScene().getWindow());
        FXMLLoader tournamentDialogLoader = new FXMLLoader();
        tournamentDialogLoader.setLocation(getClass().getResource("/sample/view/TournamentView.fxml"));
        try{
            tournamentDialog.getDialogPane().setContent(tournamentDialogLoader.load());
        } catch (Exception e){
            System.out.println("Cannot load the tournament dialog");
            e.printStackTrace();
            return;
        }

        tournamentDialog.getDialogPane().getButtonTypes().add(ButtonType.FINISH);
        tournamentDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = tournamentDialog.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.FINISH) {
            TournamentController tcCountroller = tournamentDialogLoader.getController();
            tcCountroller.storeTheInputsAndCreateGameInstances();
            System.out.println("The no of player is " + TournamentMode.getInstance().getNoofPlayers());
            GameDetails.getGamedetails().setGameMode(Constants.TOURNAMENTMODE);
            for(int i = 0; i<TournamentMode.getInstance().getNoofMaps(); i++) {
                TournamentMode.getInstance().setIndex(i);
                for (int j = 0; j < TournamentMode.getInstance().getNoofGames(); j++) {
                    createAGameObject(i);
                    int index = returnIndex(Constants.TOURNAMENTGAME);
                    GameDetails gdInstance = GameDetails.getGamedetails().getgamedetails().get(index);
                    // Intialize the player objects and player colors and map.
                    GameDetails.getGamedetails().IntializeColors(index);
                    GameDetails.getGamedetails().IntializePlayers(index);
                    GameDetails.getGamedetails().InitializeArmies(index);
                    GameDetails.getGamedetails().createMap(index);
                    if(GameDetails.getGamedetails().validateMap(index)) {
                        System.out.println("Map is valid.");
                        System.out.println("*********************************************************************************");
                        System.out.println("Proceeding to the next step");
                        // To distribute the territories to the players.
                        GameDetails.getGamedetails().distributeTerritories(index);
                        // To distribute the no of armies to the players.
                        GameDetails.getGamedetails().distributeArmies(index);
                        // To distribute the armies to the territories.
                        GameDetails.getGamedetails().distributeArmiestoTerritories(index);
                        String string = gdInstance.getMapFile().getName();
                        string = string.substring(0,string.length()-4);
                        System.out.println("Map name with out extension is "+ string);
                        GameDetails.getGamedetails().getgamedetails().get(index).setMapName(string);
                        loadGamewindow();
                    }
                }
            }
            TournamentMode.getInstance().printResults();
        } else {
            System.out.println("Cancel button is pressed");
        }
    }

    /**
     * Creates a game object for tournament mode.
     * @param index
     */
    public void createAGameObject(int index) {

        GameDetails gd = new GameDetails(TournamentMode.getInstance().getNoofPlayers(),TournamentMode
                .getInstance().getMapFiles().get(index),TournamentMode.getInstance().getPlayerCharacters()
                ,Constants.TOURNAMENTGAME,Constants.STARTPHASE,Constants.TOURNAMENTMODE,TournamentMode
                .getInstance().getNoofTurns());
        GameDetails.getGamedetails().getgamedetails().add(gd);
    }

    /**
     * This method closes the application.
     */
    public void exitApplication() {

        GameDetails.getGamedetails().clearData();
        Stage stage = (Stage) MainBorderPane.getScene().getWindow();
        stage.close();
    }


}
