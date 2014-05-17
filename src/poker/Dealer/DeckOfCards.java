/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package poker.Dealer;

import java.util.Collections;
import java.util.Arrays;
import java.util.Random;
/**
 * DeckOfCards class instantiates the 52 Card objects that make up a standard 
 * deck of cards.  It instantiated by instances of NewDeal which calls the 
 * NewDeckPlease() method to return a shuffled instance of theDeck list of 
 * Cards.  NewDeckPlease() generates a random Long number to use for a shuffle 
 * operation to randomise theDeck each time it is called.
 * @author Tim Tyler
 */
public class DeckOfCards {
    
    private static long seed;    // Random number seed used for shuffling deck.
    private final Card[] theDeck;  // Instance of the list of 52 x cards deck.
    
    // Static card objects for all 52 cards of a standard deck.
    // Calls the Card constructor with the rank and suit and sometimes is Ace boolean.
    private static final Card clubs2 = new Card(2, Card.Suit.CLUBS);
    private static final Card clubs3 = new Card(3, Card.Suit.CLUBS);
    private static final Card clubs4 = new Card(4, Card.Suit.CLUBS);
    private static final Card clubs5 = new Card(5, Card.Suit.CLUBS);
    private static final Card clubs6 = new Card(6, Card.Suit.CLUBS);
    private static final Card clubs7 = new Card(7, Card.Suit.CLUBS);
    private static final Card clubs8 = new Card(8, Card.Suit.CLUBS);
    private static final Card clubs9 = new Card(9, Card.Suit.CLUBS);
    private static final Card clubs10 = new Card(10, Card.Suit.CLUBS);
    private static final Card clubs11 = new Card(11, Card.Suit.CLUBS);
    private static final Card clubs12 = new Card(12, Card.Suit.CLUBS);
    private static final Card clubs13 = new Card(13, Card.Suit.CLUBS);
    private static final Card clubs14 = new Card(14, Card.Suit.CLUBS, true);
        
    private static final Card diamonds2 = new Card(2, Card.Suit.DIAMONDS);
    private static final Card diamonds3 = new Card(3, Card.Suit.DIAMONDS);
    private static final Card diamonds4 = new Card(4, Card.Suit.DIAMONDS);
    private static final Card diamonds5 = new Card(5, Card.Suit.DIAMONDS);
    private static final Card diamonds6 = new Card(6, Card.Suit.DIAMONDS);
    private static final Card diamonds7 = new Card(7, Card.Suit.DIAMONDS);
    private static final Card diamonds8 = new Card(8, Card.Suit.DIAMONDS);
    private static final Card diamonds9 = new Card(9, Card.Suit.DIAMONDS);
    private static final Card diamonds10 = new Card(10, Card.Suit.DIAMONDS);
    private static final Card diamonds11 = new Card(11, Card.Suit.DIAMONDS);
    private static final Card diamonds12 = new Card(12, Card.Suit.DIAMONDS);
    private static final Card diamonds13 = new Card(13, Card.Suit.DIAMONDS);
    private static final Card diamonds14 = new Card(14, Card.Suit.DIAMONDS, true);
                                
    private static final Card hearts2 = new Card(2, Card.Suit.HEARTS);
    private static final Card hearts3 = new Card(3, Card.Suit.HEARTS);
    private static final Card hearts4 = new Card(4, Card.Suit.HEARTS);
    private static final Card hearts5 = new Card(5, Card.Suit.HEARTS);
    private static final Card hearts6 = new Card(6, Card.Suit.HEARTS);
    private static final Card hearts7 = new Card(7, Card.Suit.HEARTS);
    private static final Card hearts8 = new Card(8, Card.Suit.HEARTS);
    private static final Card hearts9 = new Card(9, Card.Suit.HEARTS);
    private static final Card hearts10 = new Card(10, Card.Suit.HEARTS);
    private static final Card hearts11 = new Card(11, Card.Suit.HEARTS);
    private static final Card hearts12 = new Card(12, Card.Suit.HEARTS);
    private static final Card hearts13 = new Card(13, Card.Suit.HEARTS);
    private static final Card hearts14 = new Card(14, Card.Suit.HEARTS, true);

    private static final Card spades2 = new Card(2, Card.Suit.SPADES);
    private static final Card spades3 = new Card(3, Card.Suit.SPADES);
    private static final Card spades4 = new Card(4, Card.Suit.SPADES);
    private static final Card spades5 = new Card(5, Card.Suit.SPADES);
    private static final Card spades6 = new Card(6, Card.Suit.SPADES);
    private static final Card spades7 = new Card(7, Card.Suit.SPADES);
    private static final Card spades8 = new Card(8, Card.Suit.SPADES);
    private static final Card spades9 = new Card(9, Card.Suit.SPADES);
    private static final Card spades10 = new Card(10, Card.Suit.SPADES);
    private static final Card spades11 = new Card(11, Card.Suit.SPADES);
    private static final Card spades12 = new Card(12, Card.Suit.SPADES);
    private static final Card spades13 = new Card(13, Card.Suit.SPADES);
    private static final Card spades14 = new Card(14, Card.Suit.SPADES, true);
    

    // Constructor assembles a deck object and shuffles it.
    public DeckOfCards(){
        // 52 card deck array of Card Objects.
        this.theDeck = new Card[]{
            clubs2, clubs3, clubs4, 
            clubs5, clubs6, clubs7, 
            clubs8, clubs9, clubs10, 
            clubs11, clubs12, clubs13, 
            clubs14, diamonds2, diamonds3, 
            diamonds4, diamonds5, diamonds6, 
            diamonds7, diamonds8, diamonds9, 
            diamonds10, diamonds11, diamonds12, 
            diamonds13, diamonds14, hearts2, 
            hearts3, hearts4, hearts5, hearts6, 
            hearts7, hearts8, hearts9, 
            hearts10, hearts11, hearts12, 
            hearts13, hearts14, spades2, 
            spades3, spades4, spades5, 
            spades6, spades7, spades8, 
            spades9, spades10, spades11, 
            spades12, spades13, spades14
        };

        // Generate a seed (of long type) to shuffle the deck.
        seed = new java.util.Random().nextLong();

        // To recreate past hands (long seed = [INSERTS SEED HERE]). 
//        seed = -8306336246855041802L;

        // Print the new Random seed.
        System.out.println("Seed = " + seed);
            
        // Shuffle the static theDeck list of Cards.
        Collections.shuffle(Arrays.asList(theDeck), new Random(seed));
        
    }
    
    
    // Shuffles and returns instance of thisDeck of cards using a seed argument.
    public Card[] newDeckPlease() { 
        return this.theDeck;
    }
    
}



