/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package poker.Dealer;

/**
* Constructor for the community hand consisting of five cards picked from theDeck.
*
 * @author Tim
 */
public class Community {

    Card card1,card2,card3,card4,card5; // the 5 community cards for this instance.
    
    // Constructor for new instances of the 5 x community cards.
    public Community (Card c1, Card c2, Card c3, Card c4, Card c5) {
       // Card obj from theDeck.
       this.card1 = c1;
       this.card2 = c2;
       this.card3 = c3;
       this.card4 = c4;
       this.card5 = c5;
    }    

    // Converts and returns the names of the community cards in to a string. 
    public String getCommunityHand() {
        return "the " + this.card1.getName() + ", the " + 
               this.card2.getName() + ", the " + 
               this.card3.getName() + ", the " + 
               this.card4.getName() + " & the " + 
               this.card5.getName();
    }    
    
    // Returns rank of card, calls the argument card's getNum method().
    public int getCardNum(Card c) {
        int n = c.getNum();
        return n;
    }

    // Returns suit of card, calls the argument card's getSuit method().   
    public Card.Suit getCardSuit(Card c) {
        Card.Suit s = c.getSuit();
        return s;
    }
    
    // Returns a boolean whether card is an ace, calls the card's getAce method().
    public boolean getCardAce(Card c) {
        boolean a = c.getAce();
        return a;
    }    
    
    // Returns array of the five community cards.
    public Card[] getCommunityCards() {
        Card[] cards = {this.card1, 
                        this.card2,
                        this.card3,
                        this.card4,
                        this.card5};
        return cards;
    }
}