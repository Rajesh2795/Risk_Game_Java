package sample.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import sample.model.Card;
import sample.model.GameDetails;
import sample.model.Player;

/**
 * This class is responsible for handling the cards exchange view.
 * This class checks for cards sets from the player cards.
 * if any set has found then it will exchange the cards for armies.
 * @author Team43
 */
public class CardExchangeController {

    @FXML
    private Label cardHoldedLabel;

    @FXML
    private ListView cardsListView;

    @FXML
    private Button handInButton;

    @FXML
    private AnchorPane anchorPane;
    private ObservableList<Player> playerList;
    private ObservableList<Card> originalCardList;
    private ObservableList<Card> cardsList;
    private int currentPlayer;
    private boolean isTerritoryPresentInPlayerList = false;

    /**
     * This method adds the cards to the list view.
     * @param cardsList
     */
    @FXML
    public void addDataToTheListView(ObservableList<Card> cardsList) {

        cardsListView.getItems().addAll(cardsList);
    }

    /**
     * This method gets the playerList the gameController.
     * @param playerList
     */
    public void getPlayerListFromTheController(ObservableList<Player> playerList) {

        this.playerList = playerList;
    }

    /**
     * This method gets the current player from the game controller.
     * @param currentPlayer
     */
    public void getCurrentPlayer(int currentPlayer) {

        this.currentPlayer = currentPlayer;
    }

    /**
     * This method gets the cards list from the game controller.
     * @param originalCardList
     */
    public void getOriginalCardList(ObservableList<Card> originalCardList) {

        this.originalCardList = originalCardList;
    }

    /**
     * This function gets the cards from the player.
     * @param cardsList
     */
    public void getCardsListFromThePlayer(ObservableList<Card> cardsList) {

        this.cardsList = cardsList;
    }

    /**
     * This method calls the function call to display the cards to the list view.
     */
    public void functionCallToAddDataToListView() {

        addDataToTheListView(this.cardsList);
    }

    /**
     * This method returns the updated cards list to the game controller.
     * @return cardsList
     */
    public ObservableList<Card> returnCardsListFromThePlayer() {

        return this.cardsList;
    }

    /**
     * This method returns the updated original card list to the game controller.
     * @return originalCardList
     */
    public ObservableList<Card> returnOrginalCardList() {

        return this.originalCardList;
    }

    /**
     * This method returns the updated player list.
     * @return playerList
     */
    public ObservableList<Player> returnPlayerList() {

        return this.playerList;
    }

    /**
     * This method handles the hand in button action.
     * It checks for the suitable set from the list of cards from the player
     * if found, then it exchanges the cards for the army
     */
    public void handInButtonAction() {

        String result = findSetType();
        if(result.equalsIgnoreCase("INFANTRY")) {
            System.out.println("Card set type removing from the player is = INFANTRY");

            removeTheSetFromPlayerCards("INFANTRY",1);
            removeTheSetFromPlayerCards("INFANTRY",1);
            removeTheSetFromPlayerCards("INFANTRY",1);

            IncreaseTheArmyCount();

        } else if(result.equalsIgnoreCase("CAVALRY")) {
            System.out.println("Card set type removing from the player is = CAVALRY");

            removeTheSetFromPlayerCards("CAVALRY",1);
            removeTheSetFromPlayerCards("CAVALRY",1);
            removeTheSetFromPlayerCards("CAVALRY",1);

            IncreaseTheArmyCount();

        } else if(result.equalsIgnoreCase("ARTILLERY")) {
            System.out.println("Card set type removing from the player is = ARTILLERY");

            removeTheSetFromPlayerCards("ARTILLERY",1);
            removeTheSetFromPlayerCards("ARTILLERY",1);
            removeTheSetFromPlayerCards("ARTILLERY",1);

            IncreaseTheArmyCount();

        } else if(result.equalsIgnoreCase("ALL")) {

            System.out.println("Card set type removing from the player is = ALL");

            removeTheSetFromPlayerCards("INFANTRY",1);
            removeTheSetFromPlayerCards("CAVALRY",1);
            removeTheSetFromPlayerCards("ARTILLERY",1);

            IncreaseTheArmyCount();

        } else if(result.equalsIgnoreCase("ICW")) {
            System.out.println("Card set type removing from the player is = ICW");

            removeTheSetFromPlayerCards("INFANTRY",1);
            removeTheSetFromPlayerCards("CAVALRY",1);
            removeTheSetFromPlayerCards("WILD",1);

            IncreaseTheArmyCount();

        } else if(result.equalsIgnoreCase("IAW")) {
            System.out.println("Card set type removing from the player is = IAW");

            removeTheSetFromPlayerCards("INFANTRY",1);
            removeTheSetFromPlayerCards("CAVALRY",1);
            removeTheSetFromPlayerCards("WILD",1);

            IncreaseTheArmyCount();

        } else if(result.equalsIgnoreCase("CAW")) {
            System.out.println("Card set type removing from the player is = CAW");

            removeTheSetFromPlayerCards("CAVALRY",1);
            removeTheSetFromPlayerCards("ARTILLERY",1);
            removeTheSetFromPlayerCards("WILD",1);

            IncreaseTheArmyCount();

        } else {
            System.out.println("Cannot exchange the cards, there are no proper set to exchange");
        }
    }

    /**
     * This method increase the army of the player after exchanging the cards.
     */
    public void IncreaseTheArmyCount() {
        int noOfArmies = findCorrectSetofArmies(playerList.get(currentPlayer).getCardTurn());

        if(isTerritoryPresentInPlayerList) {
            noOfArmies += 2;
            System.out.println("Card territory present in the cards, no of armies = " + noOfArmies);
            isTerritoryPresentInPlayerList = false;
        }
        playerList.get(currentPlayer).increaseArmyCountByValue(noOfArmies);
        playerList.get(currentPlayer).setCardTurn(playerList.get(currentPlayer).getCardTurn()+1);
    }

    /**
     * This method finds set of cards from the player list.
     * @return set type
     */
    public String findSetType() {

        int infantryCount = 0,cavalryCount = 0,artileryCount = 0,wildCount = 0;
        for(int i = 0; i<cardsList.size(); i++) {
            Card card = cardsList.get(i);
            if(card.getCardType().equalsIgnoreCase("INFANTRY")) {
                infantryCount++;
            } else if(card.getCardType().equalsIgnoreCase("CAVALRY")) {
                cavalryCount++;
            } else if(card.getCardType().equalsIgnoreCase("ARTILLERY")) {
                artileryCount++;
            } else if(card.getCardType().equalsIgnoreCase("WILD")) {
                wildCount++;
            }
        }
        String result = findCorrectType(infantryCount,cavalryCount,artileryCount,wildCount);
        return result;
    }

    /**
     * This method returns the type of cards to remove from the players list.
     * @param infantryCount
     * @param cavalryCount
     * @param artileryCount
     * @return correct set type
     */
    public String findCorrectType(int infantryCount,int cavalryCount,int artileryCount,int wildCount) {

        if(infantryCount >= 3) {
            return "INFANTRY";

        } else if(cavalryCount >=3) {
            return "CAVALRY";

        } else if(artileryCount >= 3) {
            return "ARTILLERY";

        } else if(infantryCount != 0 && cavalryCount != 0 && artileryCount != 0) {
            return "ALL";

        } else if(infantryCount != 0 && cavalryCount != 0 && wildCount != 0) {
            return "ICW";

        } else if(infantryCount != 0 && artileryCount !=0 && wildCount !=0) {
            return "IAW";

        } else if(cavalryCount != 0 && artileryCount != 0 && wildCount !=0) {
            return "CAW";

        } else {
            return "NONE";
        }
    }

    /**
     * This function removes the cards from the player and
     * adds the cards to original cards list.
     * @param type
     * @param count
     */
    public void removeTheSetFromPlayerCards(String type,int count) {

        int cardNumber = 0;
        for(int i = 0; i<cardsList.size(); i++) {
            Card card = cardsList.get(i);
            if(card.getCardType().equalsIgnoreCase(type)) {
                if(cardNumber<count) {
                    if(playerList.get(currentPlayer).getTerritoriesHeld().containsKey(card.getTerritoryName())) {
                        isTerritoryPresentInPlayerList = true;
                    }
                    originalCardList.add(card);
                    cardsList.remove(i);
                    cardNumber++;
                }
            }
        }
    }

    /**
     * This function finds the how many armies to add to the player list.
     * @param cardsTurn
     * @return armies
     */
    public int findCorrectSetofArmies(int cardsTurn) {

        if(cardsTurn == 1) {
            return 4;
        } else if(cardsTurn == 2) {
            return 6;
        } else if(cardsTurn == 3) {
            return 8;
        } else if(cardsTurn == 4) {
            return 10;
        } else if(cardsTurn == 5) {
            return 12;
        } else if(cardsTurn == 6) {
            return 15;
        } else if(cardsTurn > 6) {
            return 15 + (cardsTurn - 6) * 5;
        }
        return 0;
    }


}
