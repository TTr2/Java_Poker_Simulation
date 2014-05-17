/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package poker.Dealer;

import java.util.ArrayList;

/**
 * Constructor for an individual (player) hand, initially consisting of two cards.
 * Cards 3,4,5,6 & 7 are initialised (and assigned to each Hand) at Flop, 
 * turn and river phase STATES.  Contains cardList array for iterating in for loops. 
 * Includes get methods for querying values of cards in each hand.
 * @author Tim
 */

public class Hand {
    
    private final int player; // As in player X.
    private final Card card1;  // 1st hole card.
    private final Card card2;  // 2nd hole card.
    
    private Card card3;  // 1st card from the flop.
    private Card card4;  // 2nd card from the flop.
    private Card card5;  // 3rd card from the flop.
    private Card card6;  // The Turn card.
    private Card card7;  // the River card.
    
    private final ArrayList<Card> cardList = new ArrayList<>();  // Player cards as a list.
    public ArrayList<Card> bestHand;  // The best 5 cards for this (player) Hand instance.
    private hands handType = hands.HIGHCARD;  // Initialises with the lowest ranked hand type.

    // ENUMS of the possible handtypes, in ascending rank.
    public enum hands {
        HIGHCARD,
        PAIR,
        TWOPAIR,
        TRIP,
        STRAIGHT,
        FLUSH,
        FULLHOUSE,
        QUAD,
        STRAIGHTFLUSH,
        ROYALFLUSH;
    } 

        
    // Constructor generates instance of a (player) hand, assigning the two hole cards.
    public Hand (int p, Card c1, Card c2) {
        this.player = p; // Number of Player 'X'
        this.card1 = c1;     // Card object assigned from theDeck.
        this.card2 = c2;     
        this.cardList.add(this.card1);
        this.cardList.add(this.card2);
    }
    
    public int getPlayerNum(){
        return this.player;
    }

    // USES 1 based index!! Beware! Called by getPlayerList in GFX updateCards().
    public Card getCard(int i) {
        Card c;
        switch (i) {
            case 1:  c = this.card1;
                break;
            case 2:  c = this.card2;
                break;
            case 3:  c = this.card3;
                break;
            case 4:  c = this.card4;
                break;
            case 5:  c = this.card5;
                break;
            case 6:  c = this.card6;
                break;
            case 7:  c = this.card7;
                break;
            default: c = this.card1;
                break;
       }
    return c;
    }
    
    // Constructs and prints a string of the two hole cards.
    public void printPlayerHand() {
        System.out.println(this.card1.getName() + " & the " + this.card2.getName());
    }
    
    // Constructs and returns a string of the 2 or 5 best cards in besrHand list.
    public String getBestHandString() {    
        String line = "";
        for(Card c : this.bestHand){
            String card = c.getName() + ", ";
            line = line + card; 
        }
        return line;
    }
    // Retrieve card X of BestCards array for each Hand
    public Card getBestCard(int i) {
        Card c;
        switch (i) {
            case 0:  c = this.bestHand.get(0);
                break;
            case 1:  c = this.bestHand.get(1);
                break;
            case 2:  c = this.bestHand.get(2);
                break;
            case 3:  c = this.bestHand.get(3);
                break;
            case 4:  c = this.bestHand.get(4);
                break;
            default: c = this.bestHand.get(0);
                break;
       }
    return c;
    }    
    
    // Returns a String of the 2 hole cards, for printing to console.
    public String getPlayerHand() {
        return this.card1.getName() + " & the " + this.card2.getName();
    }
    
    /* Returns rank of card, calls the arg card's getNum method() */
    public int getCardNum(Card c) {
        int n = c.getNum();
        return n;
    }
    /* Returns suit of card, calls the arg card's getSuit method() */    
    public Card.Suit getCardSuit(Card c) {
        Card.Suit s = c.getSuit();
        return s;
    }
    /* Returns bool whether card is an ace, calls the card's getAce method() */
    public boolean getCardAce(Card c) {
        boolean a = c.getAce();
        return a;
    }
    // Sets the handtype of the (player) hand instance, called by HandCalculator
    public void setHandType(hands newHandType) {
        this.handType = newHandType;
    }
    
    /* Returns hand type */
    public hands getHandType() {
        return this.handType;
    }

    /* Returns hand type as a String */
    public String getHandTypeString() {
        return this.handType.name();
    }    

    // The 5 best cards in each hand, as caluclated by handCalculator.
    public ArrayList getBestHand(){
        return bestHand;
    }
    
    // The 5 best cards in each hand, as caluclated by handCalculator.
    public void setBestHand(ArrayList<Card> cards) {
        this.bestHand = cards;
    }

    // Called by PokerManager at FLOP phase state, assigns cards to each players 3, 4 & 5 cards.
    public void addFlopCards(Card c3, Card c4, Card c5) {
        card3 = c3;
        card4 = c4;
        card5 = c5;
        cardList.add(this.card3);
        cardList.add(this.card4);
        this.cardList.add(this.card5);
    }

    // Called by PokerManager at TURN phase state, assigns card to each players 6th card.
    public void addTurnCard(Card c6) {
        this.card6 = c6;
        this.cardList.add(this.card6);
    }    
    
    // Called by PokerManager at RIVER phase state, assigns card to each players 7th card.    
    public void addRiverCard(Card c7) {
        this.card7 = c7;
        this.cardList.add(this.card7);
    }
    
    // List of playerCards, updated after of the above add[PHASE]card() methods.
    public ArrayList getCardList(){
        return this.cardList;
    }
    
    
    /* Clone cards method */
    public static ArrayList<Card> cloneCardList(ArrayList<Card> cardList) {
        ArrayList<Card> clonedList = new ArrayList(cardList.size());
        for (Card card : cardList) {
            clonedList.add(new Card(card));
        }
        return clonedList;
    }    
}