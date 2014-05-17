/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package poker.Dealer;
/**
 * Template for all card objects, instance variables are final.
 * Lists the number/rank, suit and whether an ace.
 * Includes get, set and getName string Methods
 * @author Tim
 */
public class Card {

    private final int num; // rank of card e.g. 13 for KING
    private final boolean ace; // If ace (used to calculate STRAIGHT hands.
    private final Suit suit;  // ENUM
    
    /* Enumerator for name of suit */
    public enum Suit {
        CLUBS,
        DIAMONDS,
        HEARTS,
        SPADES;
    }
    
    /* Default Constructor for non-Ace Card objects */    
    public Card(int n, Suit s) {
        this.num = n;
        this.suit = s;
        this.ace = false;
    }
    
    /* Alternative Constructor for Ace Card objects */    
    public Card(int n, Suit s, boolean a) {
        this.num = n;
        this.suit = s;
        this.ace = a;
    }
    
    /* Alternative Constructor for cloning objects */    
    public Card(Card card) {
        this.num = card.getNum();
        this.suit = card.getSuit();
        this.ace = card.getAce();
    }
    
    // Get rank of card.
    public int getNum() {
        return num;
    }
    
    // Get suit (ENUM) of card.
    public Suit getSuit() {
        return suit;
    }

    // Query whether card is an Ace or not.
    public boolean getAce() {
        return ace;
    }   
    
    // Return rank and suit as a String for printing to consle.
    // If card is a face card (+11) then convert to an appropriate name String.
    public String getName() {
        String rank;
        switch(this.num) {
            case 11: rank = "JACK";
                break;
            case 12: rank = "QUEEN";
                break;
            case 13: rank = "KING";
                break;
            case 14: rank = "ACE";
                break;
            // Convert int to String (or use String.ofValue(int))
            default: rank = Integer.toString(this.num);
                break;
        }
        String name = rank + " of " + this.suit;
        return name;
    }

    // Return a string based o rank and suit that matches name format of card images in \Assets\.
    public String getImage(){
        // Use toString instead
        String rank = "" + this.num;
        return rank + this.suit + ".png";
    }

}