package sample.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import sample.model.*;
import sample.strategies.Aggressive;
import sample.strategies.Benevolent;
import sample.strategies.Cheater;
import sample.strategies.Random;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.sql.SQLOutput;
import java.util.*;

import static sample.model.Constants.*;

/**
 * This class loads the map into game window panel. Places the territories at their coordinates.
 * Displays the player statistics such as no of armies held by the player and the no of cards holded by the player
 * @author Team43.
 */
public class LoadgameController extends Observable implements Serializable {

    public ObservableList<Player> playersList;

    public ObservableList<Card> cardsList;

    private ObservableList<Continent> continentList;

    @FXML
    public GraphicsContext gc;

    private String imageName;

    private GameDetails gd;

    private Controller controller = new Controller();

    private int index;

    private HashMap<String, Territories> territoriesHashMap = new HashMap<String, Territories>();

    @FXML
    public Canvas canvasId;

    @FXML
    public Pane canvasPane,centerPane,bottomPane;

    @FXML
    public BorderPane mainBorderPane;

    @FXML
    public Group group;

    @FXML
    private ObservableList<Group> groupList;

    @FXML
    public Label labelPlayer1,labelPlayer2,labelPlayer3,labelPlayer4,labelPlayer5,labelPlayer6,fortifyLabel
            ,armiesCountLabel,attackLabel,diceLabel,armiesToMoveLabel;

    @FXML
    public TextField armiesPlayer1,armiesPlayer2,armiesPlayer3,armiesPlayer4,armiesPlayer5,armiesPlayer6,cardsPlayer1
            ,cardsPlayer2,cardsPlayer3,cardsPlayer4,cardsPlayer5,cardsPlayer6,armiesCountTextField
            ,attackTextField,diceTextField,fortifyTextField,armiesToMoveTextField;

    public int currentPlayer = 0, attackerDice = 0, defenderDice = 0;

    @FXML
    public Button finishedmoveButton,attackButton,fortifyButton,skipButton,loadCardExchangeButton,allButton
            ,proceedButton,attackProceedButton,closeButton,confirmButton;

    private HashMap<Integer,TextField> textFieldHashMap = new HashMap<Integer, TextField>();

    private Image image;

    private PlayersDominationViewController pdvcInstance;

    private PhaseViewController pvcInstance;

    private FXMLLoader loader,phaseViewLoader;

    private String sourceNodeId,destinationNodeId;

    private int count = 1,noOfArmiesToMove = 0;

    private Text sourceText,destinationText,attackerCountryNode,defenderCountryNode,clickedtext;

    private String isPlayerAttackerOrDefender = Constants.ATTACKER;

    private boolean isAllButtonPressed = false;

    private boolean isPlayerWonTheTerritoryDuringTheAttack = false,isTerritoryWon = false;

    private FXMLLoader cardExchangeLoader;

    private CardExchangeController cecController;

    private Aggressive aggressiveObject;

    private Benevolent benevolentObject;

    private Random randomObject;

    private Cheater cheaterObject;

    private CardExchangeController cardControllerInstance = new CardExchangeController();

    @FXML
    private Button saveGameButton;

    private Stage gameStage;

    /**
     * To set up the map and buttons.
     */
    @FXML
    public void initialize() {

        groupList = FXCollections.observableArrayList();
        gc = canvasId.getGraphicsContext2D();

        if (GameDetails.getGamedetails().getGameMode().equalsIgnoreCase(SINGLEMODE)) {
            index = controller.returnIndex(Constants.NEWGAME);
        } else {
            index = controller.returnIndex(TOURNAMENTGAME);
        }


        aggressiveObject = new Aggressive();
        benevolentObject = new Benevolent();
        randomObject = new Random();
        cheaterObject = new Cheater();

        loadPlayersDominationView();
        loadPhaseView();

        gd = GameDetails.getGamedetails().getgamedetails().get(index);
        System.out.println("Total territories size is = " + gd.getTerritoriesList().size());

        pdvcInstance = loader.getController();
        pdvcInstance.initialize();

        pvcInstance = phaseViewLoader.getController();
        pvcInstance.initialize();

        gd.addObserver(pdvcInstance);
        gd.addObserver(pvcInstance);

        this.addObserver(pdvcInstance);
        this.addObserver(pvcInstance);

        playersList = FXCollections.observableArrayList(gd.getPlayersList());
        continentList = FXCollections.observableArrayList(gd.getContinentList());
        cardsList = FXCollections.observableArrayList(gd.getCardsList());

        //territoriesHashMap = gd.getTerritoriesList();

        textFieldHashMap.put(0,armiesPlayer1);
        textFieldHashMap.put(1,armiesPlayer2);
        textFieldHashMap.put(2,armiesPlayer3);
        textFieldHashMap.put(3,armiesPlayer4);
        textFieldHashMap.put(4,armiesPlayer5);
        textFieldHashMap.put(5,armiesPlayer6);

        drawMap();
        displayPlayerstats();
        setPlayerstats();

        armiesPlayer1.setDisable(false);
        cardsPlayer1.setDisable(false);
        labelPlayer1.setDisable(false);

        connectCountries();

        if(gd.getGameMode().equalsIgnoreCase(TOURNAMENTMODE)) {
            gd.setCurrentTurn(0);
        }

        currentPlayer = gd.getCurrentPlayer();
        if(gd.getGamePhase().equalsIgnoreCase(REINFORCEMENTPHASE)) {
            isVisbleCardsExchangeButtons(true);
            setThePlayerControls();
        } else {
            gd.setGamePhase(STARTPHASE);
        }

        if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.HUMAN)) {
            disableTextnodes(currentPlayer);
        } else {
            isToDisableOrEnableAllTextNodes(true);
            implementGamePhaseMethods();
        }
    }

    /**
     * This method will set the player controls.
     */
    public void setThePlayerControls() {

        if(currentPlayer == 0) {
            //disableTextnodes(currentPlayer);
            displayPlayer1stats(false);
            displayPlayer2stats(true);
            displayPlayer3stats(true);
            displayPlayer4stats(true);
            displayPlayer5stats(true);
            displayPlayer6stats(true);
        } else if (currentPlayer == 1) {
           // setUserInterface(armiesPlayer2,cardsPlayer2);
            //disableTextnodes(currentPlayer);
            displayPlayer1stats(true);
            displayPlayer2stats(false);
            displayPlayer3stats(true);
            displayPlayer4stats(true);
            displayPlayer5stats(true);
            displayPlayer6stats(true);
        } else if(currentPlayer == 2) {
            //setUserInterface(armiesPlayer3,cardsPlayer3);
            //disableTextnodes(currentPlayer);
            displayPlayer1stats(true);
            displayPlayer2stats(true);
            displayPlayer3stats(false);
            displayPlayer4stats(true);
            displayPlayer5stats(true);
            displayPlayer6stats(true);
        } else if(currentPlayer == 3) {
            //setUserInterface(armiesPlayer4,cardsPlayer5);
            //disableTextnodes(currentPlayer);
            displayPlayer1stats(true);
            displayPlayer2stats(true);
            displayPlayer3stats(true);
            displayPlayer4stats(false);
            displayPlayer5stats(true);
            displayPlayer6stats(true);
        } else if(currentPlayer == 4) {
            //setUserInterface(armiesPlayer5,cardsPlayer5);
            //disableTextnodes(currentPlayer);
            displayPlayer1stats(true);
            displayPlayer2stats(true);
            displayPlayer3stats(true);
            displayPlayer4stats(true);
            displayPlayer5stats(false);
            displayPlayer6stats(true);
        } else if(currentPlayer == 5) {
            //setUserInterface(armiesPlayer6,cardsPlayer6);
            //disableTextnodes(currentPlayer);
            displayPlayer1stats(true);
            displayPlayer2stats(true);
            displayPlayer3stats(true);
            displayPlayer4stats(true);
            displayPlayer5stats(true);
            displayPlayer6stats(false);
        }
    }

    /**
     * Draws the map image into the window.
     */
    public void drawMap() {

        String mapImageName = gd.getMapName();
        System.out.println("Map name in LoadgameController class is " + mapImageName);
        image = new Image("resources/Maps/" + mapImageName + ".bmp");
        double imageWidth = image.getWidth();
        double imageHeight = image.getHeight();

        centerPane.prefWidth(imageWidth);
        centerPane.prefHeight(imageHeight);

        canvasId.setWidth(imageWidth);
        canvasId.setHeight(imageHeight);

        canvasPane.prefWidth(imageWidth);
        canvasPane.prefHeight(imageHeight);

        mainBorderPane.prefWidth(imageWidth);

        bottomPane.prefWidth(imageWidth);

        gc.drawImage(image,0,0,image.getWidth(),image.getHeight());
        drawTerritories();
        //connectCountries();
    }

    /**
     * Displays the no of armies in the territories by assigning Text node to each territory.
     * By default the no of armies will be places as zero in the territory.
     */
    public void drawTerritories() {

        int noofPlayers = playersList.size();
        for(int i = 0; i < noofPlayers; i++) {
            territoriesHashMap = playersList.get(i).getTerritoriesHeld();
            group = new Group();
            for(String key : territoriesHashMap.keySet()) {
                Territories territory = territoriesHashMap.get(key);

                Text text = new Text();
                text.setText(Integer.toString(territory.getArmiesHeld()));
                text.setFont(Font.font(25));
                text.setFill(playersList.get(i).getPlayerColor());
                text.setY(territory.getY_Position());
                text.setX(territory.getX_Position());
                text.setId(key);
                text.onMouseClickedProperty().set(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        clickedtext = (Text) event.getTarget();
                        mouseclicked(clickedtext);
                    }
                });
                group.getChildren().add(text);
            }
            //groupList.add(group);
            canvasPane.getChildren().add(group);
        }
    }

    /**
     * This function will connect the adjacent countries by drawing the line to adjacent countries.
     * To help the user to know the adjacent countries.
     */
    public void connectCountries() {

        territoriesHashMap = gd.getTerritoriesList();
        for(int i = 0; i < playersList.size(); i++) {
            HashMap<String,Territories> playersTerritoriesmap = playersList.get(i).getTerritoriesHeld();
            Color color = playersList.get(i).getPlayerColor();

            for(String key : playersTerritoriesmap.keySet()) {
                Territories territories = playersTerritoriesmap.get(key);
                if(territories.getAdjacentTerritories().size() > 0) {
                    for(String adjacentTerritoriekey : territories.getAdjacentTerritories()) {
                        Territories adjTerritory = territoriesHashMap.get(adjacentTerritoriekey);
                        connectadjacentCountries(territories.getX_Position(),territories.getY_Position(),adjTerritory
                                .getX_Position(),adjTerritory.getY_Position(),color);
                    }
                }
            }
        }
    }

    /**
     * This function will draw the line between the countries.
     * @param srcX_pos
     * @param srcY_pos
     * @param desX_pos
     * @param desY_pos
     */
    public void connectadjacentCountries(int srcX_pos,int srcY_pos,int desX_pos,int desY_pos,Color color) {

        gc.setLineWidth(1);
        gc.setLineDashes(2d);
        gc.setStroke(color);
        gc.strokeLine(srcX_pos,srcY_pos,desX_pos,desY_pos);
    }

    /**
     * This method implements the logic according to the game current phase.
     */
    public void mouseclicked(Text clickedtext) {

        switch(gd.getGamePhase()) {
            case Constants.STARTPHASE:
                startUpPhase(clickedtext);
                break;

            case Constants.REINFORCEMENTPHASE:
                startUpPhase(clickedtext);
                break;

            case Constants.ATTACKPHASE:
                attackTextField.setText(clickedtext.getId());

                if(isPlayerAttackerOrDefender.equalsIgnoreCase(Constants.ATTACKER)) {
                    if((Integer.parseInt(clickedtext.getText())) >= 4) {
                        allButton.setVisible(true);
                        isAllButtonPressed = false;
                    } else {
                        allButton.setVisible(false);
                    }
                }
                break;

            case Constants.FORTIFICATIONPHASE:
                fortifyTextField.setText(clickedtext.getId());
                if(count == 1) {
                    armiesCountLabel.setVisible(true);
                    armiesCountTextField.setVisible(true);
                    proceedButton.setVisible(true);
                } else if(count == 2) {
                    proceedButton.setVisible(true);
                }
                break;
        }
    }

    /**
     * This will send the control to the next player when current player has done with the moving the
     * armies.
     */
    public void finishedMove() {

        updateThePlayerObjectIfPlayerWonAnyCard();
        giveControlToTheNextPlayerAndSetThePhases();

        System.out.println("**************************CurrentPlayer = " + currentPlayer + "**************************");

        switch (currentPlayer) {
            case 0:
                displayPlayer1stats(false);
                displayPlayer2stats(true);
                displayPlayer3stats(true);
                displayPlayer4stats(true);
                displayPlayer5stats(true);
                displayPlayer6stats(true);

                setUserInterface(armiesPlayer1,cardsPlayer1);
                break;

            case 1:
                displayPlayer1stats(true);
                displayPlayer2stats(false);
                displayPlayer3stats(true);
                displayPlayer4stats(true);
                displayPlayer5stats(true);
                displayPlayer6stats(true);

                setUserInterface(armiesPlayer2,cardsPlayer2);
                break;

            case 2:
                displayPlayer1stats(true);
                displayPlayer2stats(true);
                displayPlayer3stats(false);
                displayPlayer4stats(true);
                displayPlayer5stats(true);
                displayPlayer6stats(true);

                setUserInterface(armiesPlayer3,cardsPlayer3);
                break;

            case 3:
                displayPlayer1stats(true);
                displayPlayer2stats(true);
                displayPlayer3stats(true);
                displayPlayer4stats(false);
                displayPlayer5stats(true);
                displayPlayer6stats(true);

                setUserInterface(armiesPlayer4,cardsPlayer4);
                break;

            case 4:
                displayPlayer1stats(true);
                displayPlayer2stats(true);
                displayPlayer3stats(true);
                displayPlayer4stats(true);
                displayPlayer5stats(false);
                displayPlayer6stats(true);

                setUserInterface(armiesPlayer5,cardsPlayer5);
                break;

            case 5:
                displayPlayer1stats(true);
                displayPlayer2stats(true);
                displayPlayer3stats(true);
                displayPlayer4stats(true);
                displayPlayer5stats(true);
                displayPlayer6stats(false);

                setUserInterface(armiesPlayer6,cardsPlayer6);
                break;
        }
    }

    /**
     * This method increaments the turns and if it reaches the maximum turns then closes the game.
     */
    public void checkTheTurnsAndUpdate() {

        if(gd.getGameMode().equalsIgnoreCase(TOURNAMENTMODE)) {
            gd.increamentTheNumberOfTurns();
            if(gd.getCurrentTurn() == gd.getNoofTurns()) {
                storeTheResult("TIE");
                clearDataAndCloseWindow();
            }
        }
    }


    /**
     * This method changes the GUI based on the player characters.
     * @param armiesPlayer
     * @param cardsPlayer
     */
    public void setUserInterface(TextField armiesPlayer,TextField cardsPlayer) {

        if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.HUMAN)) {
            count = 1;
            proceedToNextPhase(armiesPlayer,cardsPlayer);
            isToDisableOrEnableAllTextNodes(false);
            disableTextnodes(currentPlayer);
        } else {
            isToDisableOrEnableAllTextNodes(true);
            implementGamePhaseMethods();
        }
    }

    /**
     * This will increament the current player and sets the
     * game phase to reinforcement phase.
     */
    public void giveControlToTheNextPlayerAndSetThePhases() {

        currentPlayer++;
        if(currentPlayer < playersList.size()) {
            if(!playersList.get(currentPlayer).getKnocked()) {
                gd.setCurrentPlayer(currentPlayer);
                if(!gd.getGamePhase().equalsIgnoreCase(Constants.STARTPHASE)) {
                    gd.setGamePhase(Constants.REINFORCEMENTPHASE);
                    gd.setPreviousGamePhase(gd.getGamePhase());
                }
            } else {
                giveControlToTheNextPlayerAndSetThePhases();
            }
        } else {
            gd.setCurrentPlayer(0);
            saveGameButton.setVisible(true);
            currentPlayer = 0;
            if(!playersList.get(currentPlayer).getKnocked()) {
                if(gd.getGamePhase().equalsIgnoreCase(Constants.STARTPHASE)) {
                    gd.setGamePhase(Constants.REINFORCEMENTPHASE);
                    gd.setPreviousGamePhase(gd.getGamePhase());
                } else {
                    gd.setGamePhase(Constants.REINFORCEMENTPHASE);
                    gd.setPreviousGamePhase(gd.getGamePhase());
                }
            } else {
                giveControlToTheNextPlayerAndSetThePhases();
            }
        }
    }

    /**
     * This will check if player has won any card during the attack phase if yes
     * then it will draw a random card and adds to the player list and
     * updates to the console.
     */
    public void updateThePlayerObjectIfPlayerWonAnyCard() {

        if(isPlayerWonTheTerritoryDuringTheAttack) {
            Card card = generateRandomCard();
            System.out.println("Card type won in battle is = " + card.getCardType());
            pvcInstance.addTextToTextArea("Card type won in battle is = " + card.getCardType());
            playersList.get(currentPlayer).addCard(card);
            playersList.get(currentPlayer).setCardsHolded(playersList.get(currentPlayer).getCardsHeld().size());
            displayCardStats();
            isPlayerWonTheTerritoryDuringTheAttack = false;
        }
    }

    /**
     * To display the current player no of cards holded.
     */
    public void displayCardStats() {

        if(currentPlayer == 0) {
            cardsPlayer1.setText(Integer.toString(playersList.get(currentPlayer).getCardsHeld().size()));
        } else if(currentPlayer == 1) {
            cardsPlayer2.setText(Integer.toString(playersList.get(currentPlayer).getCardsHeld().size()));
        } else if(currentPlayer == 2) {
            cardsPlayer3.setText(Integer.toString(playersList.get(currentPlayer).getCardsHeld().size()));
        } else if(currentPlayer == 3) {
            cardsPlayer4.setText(Integer.toString(playersList.get(currentPlayer).getCardsHeld().size()));
        } else if(currentPlayer == 4) {
            cardsPlayer5.setText(Integer.toString(playersList.get(currentPlayer).getCardsHeld().size()));
        } else if(currentPlayer == 5) {
            cardsPlayer6.setText(Integer.toString(playersList.get(currentPlayer).getCardsHeld().size()));
        }
    }

    /**
     * This function displays the armies count and cards holded by each player.
     */
    public void displayPlayerstats() {

        int noofPlayers = playersList.size();

        switch (noofPlayers) {
            case 2:
                displayPlayersdetails(true,true,false,false,false,false);
                break;

            case 3:
                displayPlayersdetails(true,true,true,false,false,false);
                break;

            case 4:
                displayPlayersdetails(true,true,true,true,false,false);
                break;

            case 5:
                displayPlayersdetails(true,true,true,true,true,false);
                break;

            case 6:
                displayPlayersdetails(true,true,true,true,true,true);
                break;
        }
    }

    /**
     * This is to display the players information in the game panel.
     * By default these are hidden.
     * This function will set the visible property to true.
     * @param p1
     * @param p2
     * @param p3
     * @param p4
     * @param p5
     * @param p6
     */
    public void displayPlayersdetails(boolean p1,boolean p2,boolean p3,boolean p4,boolean p5,boolean p6) {

        armiesPlayer1.setVisible(p1);
        cardsPlayer1.setVisible(p1);
        labelPlayer1.setVisible(p1);

        armiesPlayer2.setVisible(p2);
        cardsPlayer2.setVisible(p2);
        labelPlayer2.setVisible(p2);

        armiesPlayer3.setVisible(p3);
        cardsPlayer3.setVisible(p3);
        labelPlayer3.setVisible(p3);

        armiesPlayer4.setVisible(p4);
        cardsPlayer4.setVisible(p4);
        labelPlayer4.setVisible(p4);

        armiesPlayer5.setVisible(p5);
        cardsPlayer5.setVisible(p5);
        labelPlayer5.setVisible(p5);

        armiesPlayer6.setVisible(p6);
        cardsPlayer6.setVisible(p6);
        labelPlayer6.setVisible(p6);
    }

    /**
     * This function will place the players army count and cards holded to the text field.
     */
    public void setPlayerstats() {

        int noofPlayers = playersList.size();
        switch (noofPlayers) {
            case 2:
                armiesPlayer1.setText(Integer.toString(playersList.get(0).getPlayerArmies()));
                cardsPlayer1.setText(Integer.toString(playersList.get(0).getCardsHolded()));

                armiesPlayer2.setText(Integer.toString(playersList.get(1).getPlayerArmies()));
                cardsPlayer2.setText(Integer.toString(playersList.get(1).getCardsHolded()));
                break;

            case 3:
                armiesPlayer1.setText(Integer.toString(playersList.get(0).getPlayerArmies()));
                cardsPlayer1.setText(Integer.toString(playersList.get(0).getCardsHolded()));

                armiesPlayer2.setText(Integer.toString(playersList.get(1).getPlayerArmies()));
                cardsPlayer2.setText(Integer.toString(playersList.get(1).getCardsHolded()));

                armiesPlayer3.setText(Integer.toString(playersList.get(2).getPlayerArmies()));
                cardsPlayer3.setText(Integer.toString(playersList.get(2).getCardsHolded()));
                break;

            case 4:
                armiesPlayer1.setText(Integer.toString(playersList.get(0).getPlayerArmies()));
                cardsPlayer1.setText(Integer.toString(playersList.get(0).getCardsHolded()));

                armiesPlayer2.setText(Integer.toString(playersList.get(1).getPlayerArmies()));
                cardsPlayer2.setText(Integer.toString(playersList.get(1).getCardsHolded()));

                armiesPlayer3.setText(Integer.toString(playersList.get(2).getPlayerArmies()));
                cardsPlayer3.setText(Integer.toString(playersList.get(2).getCardsHolded()));

                armiesPlayer4.setText(Integer.toString(playersList.get(3).getPlayerArmies()));
                cardsPlayer4.setText(Integer.toString(playersList.get(3).getCardsHolded()));
                break;

            case 5:
                armiesPlayer1.setText(Integer.toString(playersList.get(0).getPlayerArmies()));
                cardsPlayer1.setText(Integer.toString(playersList.get(0).getCardsHolded()));

                armiesPlayer2.setText(Integer.toString(playersList.get(1).getPlayerArmies()));
                cardsPlayer2.setText(Integer.toString(playersList.get(1).getCardsHolded()));

                armiesPlayer3.setText(Integer.toString(playersList.get(2).getPlayerArmies()));
                cardsPlayer3.setText(Integer.toString(playersList.get(2).getCardsHolded()));

                armiesPlayer4.setText(Integer.toString(playersList.get(3).getPlayerArmies()));
                cardsPlayer4.setText(Integer.toString(playersList.get(3).getCardsHolded()));

                armiesPlayer5.setText(Integer.toString(playersList.get(4).getPlayerArmies()));
                cardsPlayer5.setText(Integer.toString(playersList.get(4).getCardsHolded()));
                break;

            case 6:
                armiesPlayer1.setText(Integer.toString(playersList.get(0).getPlayerArmies()));
                cardsPlayer1.setText(Integer.toString(playersList.get(0).getCardsHolded()));

                armiesPlayer2.setText(Integer.toString(playersList.get(1).getPlayerArmies()));
                cardsPlayer2.setText(Integer.toString(playersList.get(1).getCardsHolded()));

                armiesPlayer3.setText(Integer.toString(playersList.get(2).getPlayerArmies()));
                cardsPlayer3.setText(Integer.toString(playersList.get(2).getCardsHolded()));

                armiesPlayer4.setText(Integer.toString(playersList.get(3).getPlayerArmies()));
                cardsPlayer4.setText(Integer.toString(playersList.get(3).getCardsHolded()));

                armiesPlayer5.setText(Integer.toString(playersList.get(4).getPlayerArmies()));
                cardsPlayer5.setText(Integer.toString(playersList.get(4).getCardsHolded()));

                armiesPlayer6.setText(Integer.toString(playersList.get(5).getPlayerArmies()));
                cardsPlayer6.setText(Integer.toString(playersList.get(5).getCardsHolded()));
                break;
        }
    }

    /**
     * This will display or disable the player1 stats to the game panel.
     * @param value
     */
    public void displayPlayer1stats(boolean value) {

        armiesPlayer1.setDisable(value);
        cardsPlayer1.setDisable(value);
        labelPlayer1.setDisable(value);
    }

    /**
     * This will display or disable the player2 stats to the game panel.
     * @param value
     */
    public void displayPlayer2stats(boolean value) {

        armiesPlayer2.setDisable(value);
        cardsPlayer2.setDisable(value);
        labelPlayer2.setDisable(value);
    }

    /**
     * This will display or disable the player3 stats to the game panel.
     * @param value
     */
    public void displayPlayer3stats(boolean value) {

        armiesPlayer3.setDisable(value);
        cardsPlayer3.setDisable(value);
        labelPlayer3.setDisable(value);
    }

    /**
     * This will display or disable the player4 stats to the game panel.
     * @param value
     */
    public void displayPlayer4stats(boolean value) {

        armiesPlayer4.setDisable(value);
        cardsPlayer4.setDisable(value);
        labelPlayer4.setDisable(value);
    }

    /**
     * This will display or disable the player5 stats to the game panel.
     * @param value
     */
    public void displayPlayer5stats(boolean value) {

        armiesPlayer5.setDisable(value);
        cardsPlayer5.setDisable(value);
        labelPlayer5.setDisable(value);
    }

    /**
     * This will display or disable the player6 stats to the game panel.
     * @param value
     */
    public void displayPlayer6stats(boolean value) {

        armiesPlayer6.setDisable(value);
        cardsPlayer6.setDisable(value);
        labelPlayer6.setDisable(value);
    }

    /**
     * This will set the current player mouseclick actions and disables other countries mouse click actions.
     * @param currentPlayer
     */
    public void disableTextnodes(int currentPlayer) {

        ObservableList<Node> nodes = canvasPane.getChildren();
        System.out.println("Node size is = " + nodes.size());

        for(int i = 0; i < nodes.size(); i++) {
            if(i == currentPlayer) {
                nodes.get(i).setDisable(false);
            } else {
                nodes.get(i).setDisable(true);
            }
        }
    }

    /**
     * This function diables player nodes so that the player can choose the which country to attack.
     * @param currentPlayer
     */
    public void disableOnlyPlayerNodes(int currentPlayer) {

        ObservableList<Node> nodes = canvasPane.getChildren();

        for(int i = 0; i< nodes.size(); i++) {
            if(i == currentPlayer) {
                nodes.get(i).setDisable(true);
            } else {
                nodes.get(i).setDisable(false);
            }
        }
    }

    /**
     * This method will disable or enables the all player nodes.
     * @param value
     */
    public void isToDisableOrEnableAllTextNodes(boolean value) {

        ObservableList<Node> nodes = canvasPane.getChildren();
        for(int i = 0; i< nodes.size(); i++) {
            nodes.get(i).setDisable(value);
        }
    }

    /**
     * To move text node from one group to another group
     * @param tobeMovedNode
     */
    public void moveTextNodeFromOneGroupToAnother(Text tobeMovedNode,int index) {

        try {
            Node nodes = canvasPane.getChildren().get(index);
            Node currentPlayerNodes = canvasPane.getChildren().get(currentPlayer);
            int position = 0;
            if(nodes instanceof Group && currentPlayerNodes instanceof Group) {
                for(int i = 0;i<((Group) nodes).getChildren().size(); i++) {
                    Node node = (Text) ((Group) nodes).getChildren().get(i);
                    if (node.getId().equalsIgnoreCase(tobeMovedNode.getId())) {
                        position = i;
                        System.out.println("Node position is at = " + position);
                    }
                }
                Node node = ((Group) nodes).getChildren().get(position);
                node.setDisable(false);
                ((Group) nodes).getChildren().remove(node);
                ((Group) currentPlayerNodes).getChildren().add(node);
            }
        } catch (Exception e) {
            System.out.println("Cannot move the node.");
            e.printStackTrace();
            return;
        }
    }

    public Text returnTextNodeBasedOnTerritoryName(String countryName,int index) {

        Text countryText = null;
        Node currentPlayerNodes = canvasPane.getChildren().get(index);
        if(currentPlayerNodes instanceof Group) {
            for(int i = 0; i<((Group) currentPlayerNodes).getChildren().size(); i++) {
                Text node = (Text) ((Group) currentPlayerNodes).getChildren().get(i);
                if(node.getId().equalsIgnoreCase(countryName)) {
                    if(countryText == null) {
                        countryText = node;
                        break;
                    }
                    return countryText;
                }
            }
        }
        return countryText;
    }

    /**
     * This method displays the attack phase fields when the phase changed to attack phase.
     * @param value
     * @param value1
     */
    public void displayAttackPhaseFields(boolean value,boolean value1) {

        attackTextField.clear();
        diceTextField.clear();

        attackLabel.setVisible(value);
        attackTextField.setVisible(value);

        diceLabel.setVisible(value1);
        diceTextField.setVisible(value1);
        attackProceedButton.setVisible(value);
    }

    /**
     * This method displays the fortification fields.
     * @param value
     * @param value1
     */
    public void displayFortificationFields(boolean value,boolean value1) {

        armiesCountTextField.clear();
        fortifyTextField.clear();

        fortifyLabel.setVisible(value);
        fortifyTextField.setVisible(value);

        armiesCountTextField.setVisible(value1);
        armiesCountLabel.setVisible(value1);

        proceedButton.setVisible(value1);
    }

    /**
     * This method will increase the army count by 1 on the territory in the startup phase.
     */
    public void startUpPhase(Text clickedtext) {

        System.out.println("Mouse has been clicked.");

        // Get the text Id.
        // using text Id find the country on which the user has clicked.
        String countryName = clickedtext.getId();
        System.out.println("Mouse clicked country name is = " + countryName);

        if(playersList.get(currentPlayer).getPlayerArmies() > 0) {
            playersList.get(currentPlayer).getTerritoriesHeld().get(countryName).increamentarmyCountby1();
            playersList.get(currentPlayer).decreamentArmycountby1();
            clickedtext.setText(Integer.toString(playersList.get(currentPlayer).getTerritoriesHeld().get(countryName)
                    .getArmiesHeld()));
            textFieldHashMap.get(currentPlayer).setText(Integer.toString(playersList.get(currentPlayer)
                    .getPlayerArmies()));
            pvcInstance.addTextToTextArea("Incremented the army count by 1 on " + clickedtext.getId());
        } else {
            System.out.println("There are no armies left. Cannot increament the army count");
            pvcInstance.addTextToTextArea("There are no armies left. Cannot increament the army count");
        }
    }
    /**
     * This method calls the reinforcement method from the player class and updates the armies to the console
     * @param armiesPlayer
     */
    public void reinforcementPhase(TextField armiesPlayer) {

        playersList.get(currentPlayer).calculateReinforcementArmies();
        armiesPlayer.setText(Integer.toString(playersList.get(currentPlayer).getPlayerArmies()));
        pvcInstance.addTextToTextArea(playersList.get(currentPlayer).getPlayerName() + " reinforcement armies" +
                "has been calculated");
    }

    /**
     * This method implements the attackPhase.
     */
    public void attackPhase() {

        isVisibleAttackOrFortifyButtons(false);
        isPlayerAttackerOrDefender = Constants.ATTACKER;
        attackLabel.setText(Constants.SETTEXTFROMWHICHCOUNTRYATTACK);
        displayAttackPhaseFields(true,true);
    }

    /**
     * This method implements all-out mode, where once the attacker and attacked country have been identified,
     * the attack proceeds with maximum number of dice and end only when either the attacker conquers the attacked,
     * or the attacker cannot attack anymore.
     */
    public void allButtonAction() {

        System.out.println("Player has selected to attack with full force.");
        pvcInstance.addTextToTextArea("Player has decided to attack with the full force.");
        isAllButtonPressed = true;
    }

    /**
     * This function will asks the attacker country and attacking country and no of dice the player want
     * roll.
     */
    public void attackProceedButtonAction() {

        System.out.println("attackProceedButtonAction has been entered, and proceed button has been pressed.");

        if(isPlayerAttackerOrDefender.equalsIgnoreCase(Constants.ATTACKER)) {
            System.out.println("Entered attacker loop");
            readAttackerFields();
            validateAttackerChoices();
        } else if(isPlayerAttackerOrDefender.equalsIgnoreCase(Constants.DEFENDER)) {
            defenderCountryNode = clickedtext;
            pvcInstance.addTextToTextArea("The player has choosen the attacked country = " + defenderCountryNode.getId());
            if(!isAllButtonPressed) {
                readDefenderFields();
                validateDefenderChoicesAndProceedToAttack();
            } else {
                allOutMode();
            }
        }
    }

    /**
     * This method reads the attacker input.
     */
    public void readAttackerFields() {

        attackerCountryNode = clickedtext;
        pvcInstance.addTextToTextArea("The player has choosen the attacking country = " + attackerCountryNode.getId());
        if(isAllButtonPressed) {
            allButton.setVisible(false);
            pvcInstance.addTextToTextArea("The Player has choosen the all-out mode.");
        } else {
            if(!diceTextField.getText().isEmpty()) {
                attackerDice = Integer.parseInt(diceTextField.getText());
                System.out.println("ATTacker army is = " + attackerDice);
                pvcInstance.addTextToTextArea("The player has choosen the no of dice = " + attackerDice);
            } else {
                attackerDice = 0;
            }
        }
    }

    /**
     * This method reads the defender input.
     */
    public void readDefenderFields() {

        if (!diceTextField.getText().isEmpty()) {
            defenderDice = Integer.parseInt(diceTextField.getText());
            pvcInstance.addTextToTextArea("The defender has choosen no of dice = " + defenderDice);
        } else {
            defenderDice = 0;
        }
    }

    public void validateDefenderChoicesAndProceedToAttack() {

        if((defenderDice <= 2) && (defenderDice != 0) && (defenderDice <= Integer.parseInt(defenderCountryNode.getText()))) {
            diceTextField.clear();
            displayAttackPhaseFields(false,false);
            startAttacks();
        } else {
            System.out.println("The attacked country should select maximum of 2 dice. if the army is greater than " +
                    "2");
            pvcInstance.addTextToTextArea("The attacked country should select maximum of 2 dice. if the army is greater than " +
                    "2");
            disableTextnodes(currentPlayer);
            attackPhase();
        }
    }

    /**
     * This method validates the attacker choices.
     */
    public void validateAttackerChoices() {

        if(((attackerDice < Integer.parseInt(attackerCountryNode.getText())) && (attackerDice <= 3) && (attackerDice != 0))
                || isAllButtonPressed)  {
            isPlayerAttackerOrDefender = Constants.DEFENDER;
            disableOnlyPlayerNodes(currentPlayer);
            attackLabel.setText(Constants.SETTEXTTOWHICHCOUNTRYATTACK);
            allButton.setVisible(false);

            if(isAllButtonPressed) {
                displayAttackPhaseFields(true,false);
            } else {
                displayAttackPhaseFields(true,true);
            }
        } else {
            attackPhase();
        }
    }

    /**
     * This method implements the all out mode where the attacker attacks the country untill he gets that
     * country or not have enough army.
     */
    public void allOutMode() {

        isAllButtonPressed = false;
        if(playersList.get(currentPlayer).canAttack(attackerCountryNode.getId(),defenderCountryNode.getId())) {
            String battleResult = "";
            int opponentPlayerIndex = findPlayerofTheTerritory(defenderCountryNode.getId());
            if(playersList.get(opponentPlayerIndex).getTerritoriesHeld().get(defenderCountryNode.getId())
                    .getArmiesHeld() >= 2) {
                defenderDice = 2;
            } else {
                defenderDice = 1;
            }

            if(playersList.get(currentPlayer).getTerritoriesHeld().get(attackerCountryNode.getId())
                    .getArmiesHeld() == 2) {
                attackerDice = 1;
            } else if(playersList.get(currentPlayer).getTerritoriesHeld().get(attackerCountryNode.getId())
                    .getArmiesHeld() == 3) {
                attackerDice = 2;
            } else if(playersList.get(currentPlayer).getTerritoriesHeld().get(attackerCountryNode.getId())
                    .getArmiesHeld() > 3) {
                attackerDice = 3;
            }

            battleResult = playersList.get(currentPlayer).doAttack(attackerDice, defenderDice,attackerCountryNode.getId()
                        ,defenderCountryNode.getId(),playersList.get(opponentPlayerIndex));

            pvcInstance.addTextToTextArea("The battle result is = " + battleResult);
            if(playersList.get(currentPlayer).getTerritoriesHeld().get(attackerCountryNode.getId())
                    .getArmiesHeld() == 1 || playersList.get(opponentPlayerIndex).getTerritoriesHeld().get(defenderCountryNode.getId())
                    .getArmiesHeld() == 0) {
                determineWhatToHappenAfterBattle(battleResult,opponentPlayerIndex);
                return;
            } else {
                allOutMode();
            }
        } else {
            System.out.println("Cannot attack the country. It is not adjacent");
            pvcInstance.addTextToTextArea("Cannot attack the country. It is not adjacent");
            disableTextnodes(currentPlayer);
            attackPhase();
        }
    }

    /**
     * This function will check for adjacency between the attacking country and attacked country and
     * then performs the attack.
     */
    public void startAttacks() {

        if(playersList.get(currentPlayer).canAttack(attackerCountryNode.getId(),defenderCountryNode.getId())) {
            int opponentPlayerIndex = findPlayerofTheTerritory(defenderCountryNode.getId());
            String battleResult = playersList.get(currentPlayer).doAttack(attackerDice, defenderDice,attackerCountryNode.getId()
                    ,defenderCountryNode.getId(),playersList.get(opponentPlayerIndex));

            pvcInstance.addTextToTextArea("The battle result is = " + battleResult);
            determineWhatToHappenAfterBattle(battleResult,opponentPlayerIndex);
        } else {
            System.out.println("Cannot attack the country. It is not adjacent");
            pvcInstance.addTextToTextArea("Cannot attack the country. It is not adjacent");
            disableTextnodes(currentPlayer);
            attackPhase();
        }
    }

    /**
     * This function will implement the logic according to the battle result.
     * @param battleResult
     * @param opponentPlayerIndex
     */
    public void determineWhatToHappenAfterBattle(String battleResult,int opponentPlayerIndex) {

        if(battleResult.equalsIgnoreCase(Constants.WINNER)) {
            System.out.println("Congratulations, You won the battle.");
            pvcInstance.addTextToTextArea("Congratulation, You won the battle.");
            displayAttackPhaseFields(false,false);
            battleWinnerAction(opponentPlayerIndex);

        } else if(battleResult.equalsIgnoreCase(Constants.LOSER)) {
            System.out.println("Player" +currentPlayer + " lost the battle");
            pvcInstance.addTextToTextArea("Player" +currentPlayer + " lost the battle");
            updateThePlayerFieldsAfterAttack(opponentPlayerIndex);

        } else if(battleResult.equalsIgnoreCase(Constants.TIE)) {
            System.out.println("The battle is a TIE");
            pvcInstance.addTextToTextArea("The battle is a TIE");
            updateThePlayerFieldsAfterAttack(opponentPlayerIndex);
        }
    }

    /**
     * This method will update the player fields after attack is done.
     * @param opponentPlayerIndex
     */
    public void updateThePlayerFieldsAfterAttack(int opponentPlayerIndex) {
        defenderCountryNode.setText(Integer.toString(playersList.get(opponentPlayerIndex).getTerritoriesHeld()
                .get(defenderCountryNode.getId()).getArmiesHeld()));
        attackerCountryNode.setText(Integer.toString(playersList.get(currentPlayer).getTerritoriesHeld()
                .get(attackerCountryNode.getId()).getArmiesHeld()));
        resetTheControlsAfterAttackIsDone();
    }

    /**
     * This function will set the UI to the default after attack is done.
     */
    public void resetTheControlsAfterAttackIsDone() {

        isPlayerAttackerOrDefender = Constants.ATTACKER;
        disableTextnodes(currentPlayer);
        isVisibleAttackOrFortifyButtons(true);
    }

    /**
     * This function will implement the logic when the battle is won by the player
     * checks if the player has won the territory, if yes the it checks it results in the winning the
     * continent. And also it draws a card if player has won any territory during the battle.
     * checks if the defending player has lost the territory, if yes then it check if it results in loosing the
     * continent. if yes it removes the continent.
     * @param opponentPlayerIndex
     */
    public void battleWinnerAction(int opponentPlayerIndex) {

        if(playersList.get(opponentPlayerIndex).checkIfATerritoryHasLostAllArmies(defenderCountryNode.getId())) {
            Territories territory = playersList.get(opponentPlayerIndex).getTerritoriesHeld().get(defenderCountryNode.getId());

            if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.HUMAN)) {
                displayNumberOfArmiesYouWantToMoveAfterWinningTerritory(true);
            } else {
                isTerritoryWon = true;
            }

            // set the player of defeated country to attacker
            territory.setPlayer(playersList.get(currentPlayer).getPlayerName());
            playersList.get(currentPlayer).getTerritoriesHeld().put(territory.getTerritorieName(),territory);

            // check if the attacker has won the continent.
            if(checkIfPlayerHasWonAnyContinent(defenderCountryNode)) {
                System.out.println("Congratulations, you have won the continent");
                assignTheContinentToThePlayer(defenderCountryNode);
            }

            // check if the attacked player has lost the continent due to attack phase.
            if(checkIfPlayerHasLostTheContinent(defenderCountryNode,opponentPlayerIndex)) {
                System.out.println("The Player has lost the continent");
                removeThecontinentFromThePlayerList(defenderCountryNode,opponentPlayerIndex);
            }

            // Then, remove the defeated country from the player list
            playersList.get(opponentPlayerIndex).getTerritoriesHeld().remove(defenderCountryNode.getId());

            defenderCountryNode.setFill(playersList.get(currentPlayer).getPlayerColor());
            defenderCountryNode.setText(Integer.toString(playersList.get(currentPlayer).getTerritoriesHeld().get(defenderCountryNode.getId()
            ).getArmiesHeld()));
            attackerCountryNode.setText(Integer.toString(playersList.get(currentPlayer).getTerritoriesHeld()
                    .get(attackerCountryNode.getId()).getArmiesHeld()));

            moveTextNodeFromOneGroupToAnother(defenderCountryNode,opponentPlayerIndex);
            isPlayerWonTheTerritoryDuringTheAttack = true;

            // if attacked country player lost all the countries then remove the player from the list
            // Transfer the cards to the attacker.
            checkIfPlayerIsKnocked(opponentPlayerIndex);

            if(playersList.get(currentPlayer).getTerritoriesHeld().size() == gd.getMapSize()) {
                gd.setGamePhase(ENDPHASE);

                if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(HUMAN)) {
                    showTheWinnerDialogAndCloseTheWindow();
                }
            }
        } else {
            updateThePlayerFieldsAfterAttack(opponentPlayerIndex);
        }
    }

    /**
     * This method checks if the player has lost, then it transfers the cards to the player.
     * @param opponentPlayerIndex
     */
    public void checkIfPlayerIsKnocked(int opponentPlayerIndex) {

        if(playersList.get(opponentPlayerIndex).checkIfPlayerHasLostAllCountries()) {
            playersList.get(opponentPlayerIndex).transferCardsFromOnePlayerToAnother(playersList.get(currentPlayer));
            playersList.get(opponentPlayerIndex).setKnocked(true);
            System.out.println("The " + playersList.get(opponentPlayerIndex).getPlayerName() + " has been" +
                    "knocked out.");
            displayCardStats();
        }
    }

    /**
     * This function displays the winner dialog and closes the game window.
     */
    public void showTheWinnerDialogAndCloseTheWindow() {

        Alert WinAlert = new Alert(Alert.AlertType.CONFIRMATION);
        WinAlert.setTitle("Win Alert");
        WinAlert.setContentText("Congratulation, you won the game.");
        Optional<ButtonType> result = WinAlert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK) {
            System.out.println("Ending Game");
        }
        return;
    }

    /**
     * get the no of armies to move after winning the territory from the player.
     */
    @FXML
    public void getTheNoOfArmiesToMoveFromPlayerAfterWinningTerritory() {

        int getNumberofArmiesToMove = 0;
        System.out.println("Confirm button has been clicked");

        if(!armiesToMoveTextField.getText().isEmpty()) {
            getNumberofArmiesToMove = Integer.parseInt(armiesToMoveTextField.getText());
            pvcInstance.addTextToTextArea("The Player has choosen the armies to move is = " + getNumberofArmiesToMove);
            System.out.println("getNumberofArmiesToMove = " + getNumberofArmiesToMove);
        }

        if(playersList.get(currentPlayer).getTerritoriesHeld().get(attackerCountryNode.getId())
                .getArmiesHeld() > getNumberofArmiesToMove && getNumberofArmiesToMove != 0) {
            System.out.println("You can move the armies.");

            if(playersList.get(currentPlayer).doFortification(getNumberofArmiesToMove,attackerCountryNode.getId()
                    ,defenderCountryNode.getId())) {
                attackerCountryNode.setText(Integer.toString(playersList.get(currentPlayer).getTerritoriesHeld()
                        .get(attackerCountryNode.getId()).getArmiesHeld()));
                defenderCountryNode.setText(Integer.toString(playersList.get(currentPlayer).getTerritoriesHeld()
                        .get(defenderCountryNode.getId()).getArmiesHeld()));

                System.out.println("Successfully Move the armies to the location");
                pvcInstance.addTextToTextArea("Successfully Move the armies to the location");
                displayNumberOfArmiesYouWantToMoveAfterWinningTerritory(false);
                if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.HUMAN)) {
                    resetTheControlsAfterAttackIsDone();
                }
            }
        } else {
            System.out.println("Cannot give the no of armies to move as 0. Please give valid count");
        }
    }

    /**
     * This function displays the label and textfield of the ountry from which player can start fortify.
     */
    public void fortifyButtonAction() {

        gd.setGamePhase(FORTIFICATIONPHASE);
        isVisibleAttackOrFortifyButtons(false);
        count = 1;
        fortifyLabel.setText(Constants.SETTEXTTOFROMWHICHCOUNTRYFORTIFY);
        displayFortificationFields(true,false);
    }

    /**
     * This functions implements the card exchange view.
     */
    public void loadCardsExchangeView() {

        if(playersList.get(currentPlayer).getCardsHolded() >= 3) {
            Dialog<ButtonType> cardsExchangeDialog = new Dialog<>();
            cardsExchangeDialog.initOwner(mainBorderPane.getScene().getWindow());
            cardExchangeLoader = new FXMLLoader();
            cardExchangeLoader.setLocation(getClass().getResource("/sample/view/CardExchange.fxml"));
            try {
                cardsExchangeDialog.getDialogPane().setContent(cardExchangeLoader.load());
                cecController = cardExchangeLoader.getController();

                cecController.getCurrentPlayer(currentPlayer);
                cecController.getPlayerListFromTheController(playersList);
                cecController.getOriginalCardList(cardsList);
                cardControllerInstance.getCardsListFromThePlayer(FXCollections.observableList(playersList.get(currentPlayer).getCardsHeld()));
                cecController.functionCallToAddDataToListView();

                cardsExchangeDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                Optional<ButtonType> result = cardsExchangeDialog.showAndWait();

                if(result.isPresent() && (result.get() == ButtonType.OK)) {
                    playersList = cecController.returnPlayerList();
                    cardsList = cecController.returnOrginalCardList();

                    if(playersList.get(currentPlayer).getCardsHeld().size() >= 5) {
                        System.out.println("You must trade in the cards. Please trade now.");
                        loadCardsExchangeView();
                    } else {
                        updateThePlayerArmyField();
                        displayCardStats();

                        gd.setGamePhase(Constants.ATTACKPHASE);
                        isVisbleCardsExchangeButtons(false);
                        isVisibleAttackOrFortifyButtons(true);
                    }
                }
            } catch (Exception e) {
                System.out.println("Cannot load the cards exchange dialog");
                e.printStackTrace();
                return;
            }
        } else {
            Alert cardsExchangeAlert = new Alert(Alert.AlertType.WARNING);
            cardsExchangeAlert.setTitle("Cards Exchange Alert");
            cardsExchangeAlert.setContentText("You Should have minimum of 3 cards to load the cards Exchange");
            Optional<ButtonType> result = cardsExchangeAlert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK) {
                gd.setGamePhase(Constants.ATTACKPHASE);
                isVisbleCardsExchangeButtons(false);
                isVisibleAttackOrFortifyButtons(true);
            }
        }
    }

    /**
     * This method sets the playerlist and cards to load the cards exchange view.
     */
    public void initializeCardsExchangeObjectAndData() {

        //cecController.initialize();

        cardControllerInstance.getCurrentPlayer(currentPlayer);
        cardControllerInstance.getPlayerListFromTheController(playersList);
        cardControllerInstance.getOriginalCardList(cardsList);
        cardControllerInstance.getCardsListFromThePlayer(FXCollections.observableList(playersList.get(currentPlayer).getCardsHeld()));
    }

    /**
     * This function updates the armies field of the current player.
     */
    public void updateThePlayerArmyField() {

        if(currentPlayer == 0) {
            armiesPlayer1.setText(Integer.toString(playersList.get(currentPlayer).getPlayerArmies()));

        } else if(currentPlayer == 1) {
            armiesPlayer2.setText(Integer.toString(playersList.get(currentPlayer).getPlayerArmies()));

        } else if(currentPlayer == 2) {
            armiesPlayer3.setText(Integer.toString(playersList.get(currentPlayer).getPlayerArmies()));

        } else if(currentPlayer == 3) {
            armiesPlayer4.setText(Integer.toString(playersList.get(currentPlayer).getPlayerArmies()));

        } else if(currentPlayer == 4) {
            armiesPlayer5.setText(Integer.toString(playersList.get(currentPlayer).getPlayerArmies()));

        } else if(currentPlayer == 5) {
            armiesPlayer6.setText(Integer.toString(playersList.get(currentPlayer).getPlayerArmies()));
        }
    }

    /**
     * This functions is used to skip from reinforcement phase to attack phase or fortify phase.
     */
    public void skipToNextPhase() {

        if(playersList.get(currentPlayer).getCardsHeld().size() < 5) {

            gd.setGamePhase(Constants.ATTACKPHASE);

            isVisbleCardsExchangeButtons(false);
            isVisibleAttackOrFortifyButtons(true);
        } else {
            System.out.println("Entered the else loop of skip to next phase");
            Alert cardsExchangeAlert = new Alert(Alert.AlertType.WARNING);
            cardsExchangeAlert.setTitle("Cards Exchange Alert");
            cardsExchangeAlert.setContentText("Cannot skip to next phase, you have minimum 5 cards. So you " +
                    "must trade the cards.");
            Optional<ButtonType> result = cardsExchangeAlert.showAndWait();

            if(result.isPresent() && result.get() == ButtonType.OK) {
                isVisbleCardsExchangeButtons(false);
                loadCardsExchangeView();
            }

            System.out.println("Cannot skip to the next phase. You have 5 cards, So you have to trade the cards");
        }
    }

    /**
     * This function will call the reinforcementPhase function and display buttons for cards exchange or skip the
     * cards exchange and proceed to next phase.
     * @param armiesPlayer
     * @param cardsPlayer
     */
    public void proceedToNextPhase(TextField armiesPlayer, TextField cardsPlayer) {

        if(gd.getGamePhase().equalsIgnoreCase(Constants.REINFORCEMENTPHASE)) {
            reinforcementPhase(armiesPlayer);
            isVisbleCardsExchangeButtons(true);
            isVisibleAttackOrFortifyButtons(false);
        }
    }

    /**
     * This function displays the no of armies player want to move after winning the territory
     * @param value
     */
    public void displayNumberOfArmiesYouWantToMoveAfterWinningTerritory(boolean value) {

        armiesToMoveTextField.clear();
        armiesToMoveLabel.setVisible(value);
        armiesToMoveTextField.setVisible(value);
        confirmButton.setVisible(value);
    }

    /**
     * This method will display the confirmation button when selecting the source and destination countries and
     * if both source and destination are different, then it will ask the player how many armies he want to move
     * then it will call the player class can fortify method. If it returns true, then it will call a method from
     * Player class that can do fortify and return true if it fortified.
     */
    public void proceedAction() {

        // count = 1 means to select the source country.
        if(count == 1) {
            // get the no of armies to move from the text field
            if(!armiesCountTextField.getText().isEmpty()) {
                noOfArmiesToMove = Integer.parseInt(armiesCountTextField.getText());
                System.out.println("noOfArmiesToMove is = " +noOfArmiesToMove);
                pvcInstance.addTextToTextArea("The player has choosen number of armies to move is = " + noOfArmiesToMove);
            } else {
                System.out.println("Cannot leave the text field empty..");
                noOfArmiesToMove = 0;
            }

            displayFortificationFields(false,false);

            // get the source country
            sourceNodeId = clickedtext.getId();
            sourceText = clickedtext;
            pvcInstance.addTextToTextArea("The has choosen the country = " + sourceText.getId());

            // if the selected country has more than one army and no of armies to move should be less than armies in territory.
            if((Integer.parseInt(clickedtext.getText()) > 1) && (noOfArmiesToMove < (Integer.parseInt(clickedtext.getText()))) && (noOfArmiesToMove != 0)) {
                pvcInstance.addTextToTextArea("Player has selected " + sourceNodeId + " country from which he want" +
                        "to fortify.");
                pvcInstance.addTextToTextArea("Player has selected " + noOfArmiesToMove + " armies to move.");

                count = 2;
                fortifyLabel.setText(SETTEXTTOWHICHCOUNTRYFORTIFY);
                displayFortificationFields(true,false);
            } else {
                fortifyButtonAction();
                pvcInstance.addTextToTextArea("Player has selected country that has minimum armies or no of armies" +
                        "count to maximum");
            }
        // count = 2 means to select the destination country.
        } else if(count == 2) {
            // hide the text fields and labels.
            displayFortificationFields(false,false);

            // get the destination country
            destinationNodeId = clickedtext.getId();
            destinationText = clickedtext;

            // if source and destination country not same then do fortify.
            if(!sourceNodeId.equalsIgnoreCase(destinationNodeId)) {
                if(playersList.get(currentPlayer).canFortify(sourceNodeId,destinationNodeId)) {
                    if(playersList.get(currentPlayer).doFortification(noOfArmiesToMove,sourceNodeId,destinationNodeId)){
                        pvcInstance.addTextToTextArea("Player has selected " + destinationNodeId + " country to which he want" +
                                "to fortify.");
                        int noofArmies = playersList.get(currentPlayer).getTerritoriesHeld().get(sourceNodeId).getArmiesHeld();
                        sourceText.setText(Integer.toString(noofArmies));

                        noofArmies = playersList.get(currentPlayer).getTerritoriesHeld().get(destinationNodeId).getArmiesHeld();
                        destinationText.setText(Integer.toString(noofArmies));

                        count = 0;
                        pvcInstance.addTextToTextArea("Player has sucessfully done the fotification.");
                        finishedMove();
                    } else {
                        fortifyButtonAction();
                    }
                } else {
                    System.out.println("The selected country is not adjacent. Please select another country");
                    pvcInstance.addTextToTextArea("The selected country is not adjacent. Please select another country");
                    fortifyButtonAction();
                }
            } else {
                System.out.println("Both source country and destination country are equal. Select the another country.");
                pvcInstance.addTextToTextArea("The selected country is not adjacent. Please select another country");
                fortifyButtonAction();
            }
        }
    }

     /**
     * This loads the players domination view where we can see the players control over the map
     * and other actions.
     */
    public void loadPlayersDominationView() {

        loader = new FXMLLoader(getClass().getResource("/sample/view/PlayersDominationView.fxml"));
        try {
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("PlayersDominationWindow");
            stage.setScene(new Scene(root,600,448));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            System.out.println("Cannot load the players domination view.");
            e.printStackTrace();
            return;
        }
    }

    /**
     * This loads the phase view.
     */
    public void loadPhaseView() {

        phaseViewLoader = new FXMLLoader(getClass().getResource("/sample/view/PhaseView.fxml"));
        try{
            Parent root = phaseViewLoader.load();
            Stage stage = new Stage();
            stage.setTitle("PhaseView Window");
            stage.setScene(new Scene(root,800,700));
            stage.setResizable(false);
            stage.show();

        } catch (Exception e) {
            System.out.println("Cannot load the phase view.");
            e.printStackTrace();
            return;
        }
    }

    /**
     * To find the player who hold the particular territory.
     * @param country
     * @return position of player object in arraylist
     */
    public int findPlayerofTheTerritory(String country) {

        for(int i = 0; i<playersList.size(); i++) {
            if(playersList.get(i).getTerritoriesHeld().containsKey(country)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * To set visible or hide the attack and fortify buttons.
     * @param value
     */
    public void isVisibleAttackOrFortifyButtons(boolean value) {

        attackButton.setVisible(value);
        fortifyButton.setVisible(value);
    }

    /**
     * To set visible or hide the cards exchange and skip buttons.
     * @param value
     */
    public void isVisbleCardsExchangeButtons(boolean value) {

        loadCardExchangeButton.setVisible(value);
        skipButton.setVisible(value);
    }

    /**
     * This method returns a random card from a deck of cards;
     * @return Card
     */
    public Card generateRandomCard() {

        int min = 0;
        int max = cardsList.size()-1;
        int range = max - min + 1;
        int randomIndex = (int) (Math.random() * range) + min;
        Card card = cardsList.get(randomIndex);
        cardsList.remove(randomIndex);
        return card;
    }

    /**
     * To check if the player has won any continent.
     * @param territoryWon
     * @return boolean
     */
    public boolean checkIfPlayerHasWonAnyContinent(Text territoryWon) {

        boolean result = false;
        Territories territory = playersList.get(currentPlayer).getTerritoriesHeld().get(territoryWon.getId());
        String continentName = territory.getContinentName().trim();
        int continentIndex = getIndexOfTheContinent(continentName);
        System.out.println("Continent Index = " + continentIndex);
        ArrayList<String> adjacentCountries = continentList.get(continentIndex).getTerritoriesHeld();
        for(int i = 0; i<adjacentCountries.size(); i++) {
            if(playersList.get(currentPlayer).getTerritoriesHeld().containsKey(adjacentCountries.get(i))) {
                result = true;
            } else {
                result = false;
                break;
            }
        }
        return result;
    }

    /**
     * To assign the continent to the player.
     * @param territoryWon
     */
    public void assignTheContinentToThePlayer(Text territoryWon) {

        Territories territory = playersList.get(currentPlayer).getTerritoriesHeld().get(territoryWon.getId());
        int continentIndex = getIndexOfTheContinent(territory.getContinentName());
        playersList.get(currentPlayer).addContinent(continentList.get(continentIndex));
    }

    /**
     * To check if the player has lost the continent.
     * @param territoryLost
     * @param defenderPlayerIndex
     * @return boolean
     */
    public boolean checkIfPlayerHasLostTheContinent(Text territoryLost,int defenderPlayerIndex) {

        Territories territory = playersList.get(defenderPlayerIndex).getTerritoriesHeld().get(territoryLost.getId());
        if(playersList.get(defenderPlayerIndex).getContinentHeld().containsKey(territory.getContinentName())) {
            return true;
        }
        return false;
    }

    /**
     * This function removes the continent from the player list, if the player has lost the territory during
     * the battle.
     * @param territoryLost
     * @param defenderPlayerIndex
     */
    public void removeThecontinentFromThePlayerList(Text territoryLost,int defenderPlayerIndex) {

        Territories territory = playersList.get(defenderPlayerIndex).getTerritoriesHeld().get(territoryLost.getId());
        boolean result = playersList.get(defenderPlayerIndex).removeContinent(territory.getContinentName());
    }

    /**
     * To get the continent index from the list of continents.
     * @param continentName
     * @return continentIndex
     */
    public int getIndexOfTheContinent(String continentName) {

        System.out.println("Continent name is = " + continentName);
        int size = continentList.size();
        System.out.println("SIze is = " + size);
        for(int i = 0; i<size; i++) {
            Continent continent = continentList.get(i);
            if(continent.getContinentName().equalsIgnoreCase(continentName)) {
                System.out.println("Continent index is = " + i);
                return i;
            }
        }
        return -1;
    }

    /**
     * This method calls the player strategies functions for all the game phases.
     */
    public void implementGamePhaseMethods() {

        switch (gd.getGamePhase()) {
            case Constants.STARTPHASE:
                startUp();
                break;

            case Constants.REINFORCEMENTPHASE:
                reinforceStrategy();
                break;

            case Constants.ATTACKPHASE:
                attackStrategy();
                break;

            case FORTIFICATIONPHASE:
                fortificationStrategy();
                break;

            case Constants.ENDPHASE:
                System.out.println("Game came to an End");
                storeTheResult(playersList.get(currentPlayer).getPlayerCharacter());
                if(gd.getGameMode().equalsIgnoreCase(TOURNAMENTMODE)) {
                    clearDataAndCloseWindow();
                } else {
                    showTheWinnerDialogAndCloseTheWindow();
                    clearDataAndCloseWindow();
                }
                break;
        }
    }

    /**
     * This method clears the objects and closes the window.
     */
    public void clearDataAndCloseWindow() {

        clearObjects();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                closeGame();
            }
        });
    }

    public void storeTheResult(String result) {

        if(GameDetails.getGamedetails().getGameMode().equalsIgnoreCase(TOURNAMENTMODE)) {
            int aListIndex = TournamentMode.getInstance().getIndex();
            TournamentMode.getInstance().getResults().get(aListIndex).add(result);
        }
    }

    /**
     * This method will increament the territory armies untill the player is left out with zero armies.
     */
    public void startUp() {

        for(String key : playersList.get(currentPlayer).getTerritoriesHeld().keySet()) {
            Territories territory = playersList.get(currentPlayer).getTerritoriesHeld().get(key);
            if(playersList.get(currentPlayer).getPlayerArmies() > 0) {
                Text CountryNode = returnTextNodeBasedOnTerritoryName(territory.getTerritorieName(),currentPlayer);
                startUpPhase(CountryNode);
            }
        }

        if(playersList.get(currentPlayer).getPlayerArmies() > 0) {
            List<String> keys = new ArrayList<>(playersList.get(currentPlayer).getTerritoriesHeld().keySet());
            java.util.Random random = new java.util.Random();

            while(playersList.get(currentPlayer).getPlayerArmies() > 0) {
                Territories territory = playersList.get(currentPlayer).getTerritoriesHeld()
                        .get(keys.get(random.nextInt(keys.size())));
                territory.increamentarmyCountby1();
                playersList.get(currentPlayer).decreamentArmycountby1();
                Text countryNode = returnTextNodeBasedOnTerritoryName(territory.getTerritorieName(),currentPlayer);
                countryNode.setText(Integer.toString(territory.getArmiesHeld()));
                updateThePlayerArmyField();
            }
        }
        finishedMove();
    }

    /**
     * This method loads the cardsExchange when the number of cards are equal to or more than 5.
     */
    public void loadCardsExhangeForStrategies() {

        if(playersList.get(currentPlayer).getCardsHeld().size() >= 5) {
            initializeCardsExchangeObjectAndData();
            cardControllerInstance.handInButtonAction();

            playersList = cardControllerInstance.returnPlayerList();
            cardsList = cardControllerInstance.returnOrginalCardList();

            if(playersList.get(currentPlayer).getCardsHeld().size() >= 5) {
                loadCardsExhangeForStrategies();
            } else {
                updateThePlayerArmyField();
                displayCardStats();
            }
        }
    }

    /**
     * This method uses the player strategies to reinforce the countries.
     */
    public void reinforceStrategy() {

        playersList.get(currentPlayer).calculateReinforcementArmies();
        loadCardsExhangeForStrategies();

        if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.AGGRESSIVE)) {
            String reinforcingCountry = aggressiveObject.reinforce(playersList,currentPlayer);
            updateReinforcementFields(reinforcingCountry);
        } else if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.BENEVOLENT)) {
            String reinforcingCountry = benevolentObject.reinforce(playersList,currentPlayer);
            updateReinforcementFields(reinforcingCountry);
        } else if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.RANDOM)) {
            String reinforcingCountry = randomObject.reinforce(playersList,currentPlayer);
            updateReinforcementFields(reinforcingCountry);
        } else if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.CHEATER)) {
            String result = cheaterObject.reinforce(playersList,currentPlayer);
            System.out.println("entered the loop of cheater reinforcement");
            for(String key : playersList.get(currentPlayer).getTerritoriesHeld().keySet()) {
                updateReinforcementFields(key);
            }
        }
        gd.setGamePhase(Constants.ATTACKPHASE);
        attackStrategy();
    }

    /**
     * This method updates the fields after reinforcementPhase.
     * @param reinforcingCountry
     */
    public void updateReinforcementFields(String reinforcingCountry) {
        if (reinforcingCountry != null) {
            pvcInstance.addTextToTextArea("The reinforced country is = " + reinforcingCountry);
            Text reinforcingNode = returnTextNodeBasedOnTerritoryName(reinforcingCountry,currentPlayer);
            reinforcingNode.setText(Integer.toString(playersList.get(currentPlayer).getTerritoriesHeld()
                    .get(reinforcingCountry).getArmiesHeld()));
            updateThePlayerArmyField();
        } else {
            System.out.println("There is no proper reinforcement country to reinforce.");
        }
    }

    /**
     * This method implements fortification method for player strategies.
     */
    public void fortificationStrategy() {

        if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.AGGRESSIVE)) {
            String sourceCountry = aggressiveObject.fortify(playersList,currentPlayer,Constants.SOURCECOUNTRY);
            if(!sourceCountry.trim().isEmpty()) {
                System.out.println("The fortification source country is " + sourceCountry);
                String fortifyingCountry = aggressiveObject.fortify(playersList,currentPlayer,Constants.FORTIFYINGCOUNTRY);
                if(!fortifyingCountry.trim().isEmpty()) {
                    System.out.println("The fortifiying country is = " + fortifyingCountry);
                    updateFortificationFields(sourceCountry, fortifyingCountry);
                }
            }

        } else if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.BENEVOLENT)) {
            String sourceCountry = benevolentObject.fortify(playersList,currentPlayer,Constants.SOURCECOUNTRY);
            if(!sourceCountry.trim().isEmpty()) {
                System.out.println("The fortification source country is " + sourceCountry);
                String fortifyingCountry = benevolentObject.fortify(playersList,currentPlayer,Constants.FORTIFYINGCOUNTRY);
                if(!fortifyingCountry.trim().isEmpty()) {
                    updateFortificationFields(sourceCountry, fortifyingCountry);
                }
            }

        } else if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.RANDOM)) {
            String sourceCountry = randomObject.fortify(playersList,currentPlayer,Constants.SOURCECOUNTRY);
            System.out.println("Random source country is " + sourceCountry);
            if(!sourceCountry.trim().isEmpty()) {
                String fortifyingCountry = randomObject.fortify(playersList,currentPlayer,Constants.FORTIFYINGCOUNTRY);
                System.out.println("Fortifying country random is " + fortifyingCountry);
                if(!fortifyingCountry.trim().isEmpty()) {
                    updateFortificationFields(sourceCountry,fortifyingCountry.trim());
                }
            }

        } else if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.CHEATER)) {
            String countries = cheaterObject.fortify(playersList,currentPlayer,Constants.SOURCECOUNTRY);
            if(!countries.trim().isEmpty()) {
                String[] countriesList = countries.split(",");
                for(int i = 0; i<countriesList.length; i++) {
                    Text countryNode = returnTextNodeBasedOnTerritoryName(countriesList[i],currentPlayer);
                    countryNode.setText(Integer.toString(playersList.get(currentPlayer).getTerritoriesHeld()
                            .get(countriesList[i]).getArmiesHeld()));
                }
            }
        }

        playersList.get(currentPlayer).setSourceCountry(null);
        playersList.get(currentPlayer).setFortifyingCountry(null);

        finishedMove();
    }

    /**
     * This method calls the fortification function and then updates the fields.
     * @param sourceCountry
     * @param fortifyingCountry
     */
    public void updateFortificationFields(String sourceCountry, String fortifyingCountry) {

        if(sourceCountry != null && fortifyingCountry != null) {
            pvcInstance.addTextToTextArea("The fortification country is done from = " + sourceCountry);
            pvcInstance.addTextToTextArea("The fortifying country is = " + fortifyingCountry);
            int noofArmies = playersList.get(currentPlayer).getTerritoriesHeld().get(sourceCountry).getArmiesHeld()-1;
            playersList.get(currentPlayer).doFortification(noofArmies,sourceCountry,fortifyingCountry);
            sourceText = returnTextNodeBasedOnTerritoryName(sourceCountry,currentPlayer);
            destinationText = returnTextNodeBasedOnTerritoryName(fortifyingCountry,currentPlayer);
            sourceText.setText(Integer.toString(playersList.get(currentPlayer).getTerritoriesHeld().get(sourceCountry)
                    .getArmiesHeld()));
            destinationText.setText(Integer.toString(playersList.get(currentPlayer).getTerritoriesHeld()
                    .get(fortifyingCountry).getArmiesHeld()));
        } else {
            System.out.println("Cannot do the fortification. one of the country is null");
        }
    }

    /**
     * This method calls the attack method from the strategies.
     */
    public void attackStrategy() {

        if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.AGGRESSIVE)) {
            String attackerCountry = aggressiveObject.attack(playersList,currentPlayer,Constants.ATTACKER);
            System.out.println("The attacking country of aggressive is " + attackerCountry);
            if(!attackerCountry.trim().isEmpty()) {
                String defendingCountry = aggressiveObject.attack(playersList,currentPlayer,Constants.DEFENDER);
                System.out.println("The defender country is = " + defendingCountry);
                if(!defendingCountry.trim().isEmpty()) {
                    performChecksAndDoAttackAndUpdateFields(attackerCountry, defendingCountry);
                }
            }

        } else if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.BENEVOLENT)) {
            String result = benevolentObject.attack(playersList,currentPlayer,Constants.ATTACKER);

        } else if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.RANDOM)) {
            String attackerCountry = randomObject.attack(playersList,currentPlayer,Constants.ATTACKER);
            System.out.println("Random Player attacker country is = "+attackerCountry);
            if(!attackerCountry.trim().isEmpty()) {
                String defendingCountry = randomObject.attack(playersList,currentPlayer,Constants.DEFENDER);
                if(!defendingCountry.trim().isEmpty()) {
                    System.out.println("Random Player defending country is = "+defendingCountry);
                    performChecksAndDoAttackAndUpdateFields(attackerCountry, defendingCountry);
                }
            }
        } else if(playersList.get(currentPlayer).getPlayerCharacter().equalsIgnoreCase(Constants.CHEATER)) {
            String cheaterOccupiedCountries = cheaterObject.attack(playersList,currentPlayer,Constants.ATTACKER);
            if(!cheaterOccupiedCountries.trim().isEmpty()) {
                String[] cheaterOccupiedList = cheaterOccupiedCountries.split(",");
                for(int i = 0; i<cheaterOccupiedList.length; i++) {
                    System.out.println("Occupied country " + i + cheaterOccupiedList[i]);
                    int playerIndex = findPlayerofTheTerritory(cheaterOccupiedList[i]);
                    Text OccupiedNode = returnTextNodeBasedOnTerritoryName(cheaterOccupiedList[i],playerIndex);
                    updateTheCheaterOccupiedCountriesNodes(cheaterOccupiedList[i], playerIndex, OccupiedNode);
                }
                if(playersList.get(currentPlayer).getTerritoriesHeld().size() == gd.getMapSize()) {
                    gd.setGamePhase(ENDPHASE);
                }
            }
        }

        playersList.get(currentPlayer).setAttackingCountry(null);
        playersList.get(currentPlayer).setDefendingCountry(null);

        if(!gd.getGamePhase().equalsIgnoreCase(ENDPHASE)) {
            gd.setGamePhase(FORTIFICATIONPHASE);
            fortificationStrategy();
        } else {
            implementGamePhaseMethods();
        }
    }

    /**
     * This method will update the cheater occupied countries.
     * @param key
     * @param playerIndex
     * @param occupiedNode
     */
    public void updateTheCheaterOccupiedCountriesNodes(String key, int playerIndex, Text occupiedNode) {

        pvcInstance.addTextToTextArea("The cheater has occupied = " + occupiedNode.getId());
        Territories territory = playersList.get(playerIndex).getTerritoriesHeld().get(key);
        territory.setArmiesHeld(1);
        // set the player of defeated country to attacker
        territory.setPlayer(playersList.get(currentPlayer).getPlayerName());
        playersList.get(currentPlayer).getTerritoriesHeld().put(territory.getTerritorieName(),territory);

        // check if the attacker has won the continent.
        if(checkIfPlayerHasWonAnyContinent(occupiedNode)) {
            System.out.println("Congratulations, you have won the continent");
            assignTheContinentToThePlayer(occupiedNode);
        }

        // check if the attacked player has lost the continent due to attack phase.
        if(checkIfPlayerHasLostTheContinent(occupiedNode,playerIndex)) {
            System.out.println("The Player has lost the continent");
            removeThecontinentFromThePlayerList(occupiedNode,playerIndex);
        }

        // Then, remove the defeated country from the player list
        playersList.get(playerIndex).getTerritoriesHeld().remove(occupiedNode.getId());

        occupiedNode.setFill(playersList.get(currentPlayer).getPlayerColor());
        occupiedNode.setText(Integer.toString(playersList.get(currentPlayer).getTerritoriesHeld().get(occupiedNode.getId()
        ).getArmiesHeld()));
        moveTextNodeFromOneGroupToAnother(occupiedNode,playerIndex);
        checkIfPlayerIsKnocked(playerIndex);
    }

    /**
     * This method performs the attack and updates the fields.
     * @param attackerCountry
     * @param defendingCountry
     */
    public void performChecksAndDoAttackAndUpdateFields(String attackerCountry, String defendingCountry) {

        pvcInstance.addTextToTextArea("The attacker country is = " + attackerCountry);
        pvcInstance.addTextToTextArea("The defender country is = " + defendingCountry);
        attackerCountryNode = returnTextNodeBasedOnTerritoryName(attackerCountry,currentPlayer);
        System.out.println("The attacker country node is = " + attackerCountryNode.getId());
        int defenderPlayerIndex = findPlayerofTheTerritory(defendingCountry);
        System.out.println("defending player index is = " + defenderPlayerIndex);

        defenderCountryNode = returnTextNodeBasedOnTerritoryName(defendingCountry,defenderPlayerIndex);
        System.out.println("The attacker country node is = " + defenderCountryNode.getId());

        if(playersList.get(currentPlayer).getTerritoriesHeld().get(attackerCountry).getArmiesHeld() > 3) {
            allOutMode();
        } else {
            if(playersList.get(currentPlayer).getTerritoriesHeld().get(attackerCountry).getArmiesHeld() == 2 ) {
                attackerDice = 1;
            } else if(playersList.get(currentPlayer).getTerritoriesHeld().get(attackerCountry).getArmiesHeld() == 3){
                attackerDice = 2;
            }

            if(Integer.parseInt(defenderCountryNode.getText()) >= 2) {
                defenderDice = 2;
            } else {
                defenderDice = 1;
            }
            startAttacks();
        }
        if(isTerritoryWon) {
            armiesToMoveTextField.setText("1");
            getTheNoOfArmiesToMoveFromPlayerAfterWinningTerritory();
            isTerritoryWon = false;
        }
    }

    /**
     * This method saves the game.
     */
    @FXML
    public void saveTheGame() {

        gd.setPlayersList(new ArrayList<>(playersList));
        gd.setCardsList(new ArrayList<>(cardsList));
        gd.setContinentList(new ArrayList<>(continentList));
        saveGameButton.setVisible(false);
        try{
            FileOutputStream f = new FileOutputStream(new File("E:\\IntelliJ\\AppProject\\src\\resources\\SavedGame\\save1.ser"));
            ObjectOutputStream o = new ObjectOutputStream(f);
            o.writeObject(gd);
            o.close();
            f.close();
        } catch (Exception e) {
            System.out.println("Cannot save the game");
            e.printStackTrace();
            return;
        }
    }

    /**
     * This function will clear the objects.
     */
    public void clearObjects() {

        playersList.clear();
        continentList.clear();
        cardsList.clear();
        textFieldHashMap.clear();
        attackerDice = -1;
        defenderDice = -1;
        sourceText = null;
        destinationText = null;
        attackerCountryNode = null;
        defenderCountryNode = null;
        loader = null;
        gc = null;
        canvasPane.getChildren().clear();
        group = null;
        groupList.clear();
        currentPlayer = 0;
        GameDetails.getGamedetails().getgamedetails().get(index).setGamePhase(STARTPHASE);
        GameDetails.getGamedetails().getgamedetails().get(index).setCurrentPlayer(0);
        GameDetails.getGamedetails().getgamedetails().get(index).setPlayersList(null);
        noOfArmiesToMove = 0;
        sourceNodeId = null;
        destinationNodeId = null;
        count = 1;
        isAllButtonPressed = false;
        isPlayerAttackerOrDefender = ATTACKER;
        isPlayerWonTheTerritoryDuringTheAttack = false;
        isTerritoryWon = false;
        cardExchangeLoader = null;
        cecController = null;
        aggressiveObject = null;
        benevolentObject = null;
        randomObject = null;
        cheaterObject = null;
        cardControllerInstance = null;
        if(GameDetails.getGamedetails().getGameMode().equalsIgnoreCase(SINGLEMODE)) {
            controller.clearObjects(NEWGAME);
        } else {
            controller.clearObjects(TOURNAMENTGAME);
        }
    }

    /**
     * Closes the window.
     */
    @FXML
    public void closeGame() {

        pdvcInstance.closeWindow();
        pvcInstance.closeWindow();
       // FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/view/loadGame.fxml"));

        gameStage = (Stage) this.bottomPane.getScene().getWindow();
        gameStage.close();
    }
}
