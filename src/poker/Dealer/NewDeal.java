/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package poker.Dealer;

/**
 * Generates 8 player objects and 1 community object, from Hand/Community 
 * constructors and assigns cards from newDeck with x and y coords.
 * 
 * Should be assigned Null at the end of each game, and reinitialised at start
 * of each new game.
 * 
 * @author Tim
 */
public class NewDeal {
   
    public Hand player1;
    public Hand player2;
    public Hand player3;
    public Hand player4;
    public Hand player5;
    public Hand player6;
    public Hand player7;
    public Hand player8;
    public Community community;
    private final Card[] newDeck;
    
    /** Constructor creates 8 instances of Hand class plus 1 instance of 
     * community class.  Also creates a shuffled instance of a Deck of cards and
     * assigns cards from newDeck to each Hand/Community instance.
     */
    public NewDeal() {
        
        DeckOfCards myDeck = new DeckOfCards(); // Instantiates a new DeckOfCards instance.
        newDeck = myDeck.newDeckPlease(); // Returns a new instance of deck of cards list.
        
        // Constructs 8 new instances of Hand, sets each pair of hole cards from newDeck list.
        player1 = new Hand(1, newDeck[0], newDeck[8]);
        player2 = new Hand(2, newDeck[1], newDeck[9]);
        player3 = new Hand(3, newDeck[2], newDeck[10]);
        player4 = new Hand(4, newDeck[3], newDeck[11]);
        player5 = new Hand(5, newDeck[4], newDeck[12]);
        player6 = new Hand(6, newDeck[5], newDeck[13]);
        player7 = new Hand(7, newDeck[6], newDeck[14]);
        player8 = new Hand(8, newDeck[7], newDeck[15]);

        // Constructs a new instances of Community (hand), containing 5 cards from newDeck list.
        community = new Community(
                    newDeck[16], 
                    newDeck[17], 
                    newDeck[18], 
                    newDeck[19], 
                    newDeck[20] 
        );
        
    }
    
    
    // Adds and returns the 8 player Hands as a List for use by other classes.
    public Hand[] getListOfPlayers() {
        Hand[] playerList = {this.player1, this.player2, this.player3, 
                             this.player4, this.player5, this.player6, 
                             this.player7, this.player8};
        return playerList;
    }
    
    // Returns the list of 5 community cards for use by other classes.
    public Card[] getCommunityCards() {
        Card[] cards = community.getCommunityCards();
        return cards;
    }       
}
