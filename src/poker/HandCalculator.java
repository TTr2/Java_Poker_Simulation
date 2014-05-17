/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package poker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import poker.Dealer.Hand;
import poker.Dealer.Card;
/**
 * The purpose of the HandCalculator class is to analyse a player's hand 
 * for the best hand type possible from their two hole cards and 5 community 
 * cards.   A new HandCalculator class instance is generated for each player's 
 * hand at each phase of a game, as new community cards are added to the pool of 
 * available cards increases with each phase of the game.  A handCalculator 
 * instance does not persist beyond the for loop that it is generated within, 
 * instead the construction of each HC instance results in various fields with 
 * each player Hand instance being assigned new values.  During construction, 
 * the local fields "Straights", "Multiples" and "Flushes" are populated if the 
 * appropriate conditions are met.  These fields are then called upon by the 
 * various hand type assessment functions that are called by the 
 * calculateHandType() method..  
 * @author Tim
 */
public class HandCalculator {
    
    private final Hand hand; // Reference to the Hand instance under inspection.    
    private final ArrayList<Card> cards; // Reference to the Hand's card list.
    private final ArrayList<Card> multiples = new ArrayList<>();
    private ArrayList<Card> flushes;
    private final ArrayList<Card> straights = new ArrayList<>();

    /** Constructor for HandCalculator objects that are created for each player
     * after each round.  
     * @param p for player hand.
     */
    public HandCalculator(Hand p){

        this.hand = p; // Assign a reference to the Hand instance under inspection.
        this.cards =  p.getCardList(); // Call a hand instance's getCardList method.
        sortCards(this.cards);  // Sort cards in descending order of rank.

        // Populates the multiples, flushes and straights lists where applicable.
        setMultiFlushStraightLists(this.hand);

        // Assigns best handtype and best cards to relevant Hand object.
        calculateHandType();
    }
    
    // Analyses a player's Hands for the important patterns - duplicate ranks,
    // contiguous ranks and 5 cards or more with the same suit.
    private void setMultiFlushStraightLists(Hand p) {
        ArrayList<Card> clubs = new ArrayList<>();
        ArrayList<Card> diamonds = new ArrayList<>();        
        ArrayList<Card> hearts = new ArrayList<>();
        ArrayList<Card> spades = new ArrayList<>();
        sortCards(this.cards);
        
        // SET FLUSH & MULTIPLE LISTS
        for(Card c : this.cards){
            // Add card objects to each suit list, for processing later.
            switch(c.getSuit()) {
                case CLUBS:
                    clubs.add(c);
                    break;
                case DIAMONDS:
                    diamonds.add(c);
                    break;
                case HEARTS:
                    hearts.add(c);
                    break;
                case SPADES:
                    spades.add(c);
                    break;
                }

            // Still in the for cards loop, check for repeated rank and add to 
            // list of multiples.  This should catch pairs, trips, quads and 
            // full houses - to be processed further by handTypeCalculator(). 

            for(int i=0; i< this.cards.size();i++) {
                if(c.getNum() == this.cards.get(i).getNum() && 
                        c != this.cards.get(i)){
                    this.multiples.add(c);
                    break;
                }
            }
        }
        sortCards(this.multiples);
        
        this.flushes = calcMaxSuitedCards(clubs, diamonds, hearts, spades);

        // SET STRAIGHTS LIST
        // Check for any Aces and if so add a new Card object with rank of 1.
        ArrayList<Card> addAces = insertAces(this.cards);

        // Remove duplicates from cards then sort in decsending rank.
        ArrayList<Card> nonDupes = removeDuplicates(addAces);
        sortCards(nonDupes);

        if(nonDupes.size() >= 5){
            // For n cycles (between 1 - 3 cycles)
            for(int i=0; i< (nonDupes.size()-4); i++){
                // k sets the upper limit for the while loop below
                // j replicates the i value
                int j = i;
                int k = j+5;
                // Check for contiguos ranks in ordered list of cards.
                if(nonDupes.get(i).getNum() == (nonDupes.get(i+1).getNum() + 1)
                        && nonDupes.get(i+1).getNum() 
                        == (nonDupes.get(i+2).getNum() + 1)
                        && nonDupes.get(i+2).getNum() 
                        == (nonDupes.get(i+3).getNum() + 1)
                        && nonDupes.get(i+3).getNum() 
                        == (nonDupes.get(i+4).getNum() + 1)
                        )
                {
                    while (j < k){
                        this.straights.add(nonDupes.get(j));
                        j++;
                    }
                    
                // To stop execution at highest available straight.
                break;
                }
            }
        }
        
      }        

    public void calculateHandType() {
        // INSTEAD? See http://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
        
        // Get ordinal of current handType then check for all higher hand types.
        // All HandChecks assume that (this) multiples, straight and flush lists are sorted.
        switch(this.hand.getHandType().ordinal()){
                case 0: checkHighCard();
                case 1: checkPair();
                case 2: checkTwoPair();
                case 3: checkTrip();                    
                case 4: checkStraight();
                case 5: checkFlush();
                case 6: checkFullHouse();
                case 7: checkQuad();
                case 8: checkStraightFlush();
                case 9: checkRoyalFlush();
         }
    }

    private void checkHighCard() {
        ArrayList<Card> tmpCards = new ArrayList<>();
        // For preflop analysis of 2 x hole cards only.
        if(this.cards.size() == 2){
            for(int i=0; i<2;i++){
                tmpCards.add(cards.get(i));
            }
            this.hand.setBestHand(tmpCards); // Assign best 2 cards to Hand instance.
        }
        // For post flop analysis of 5 or more available cards.
        else {
            for(int i=0; i<5;i++) {
                tmpCards.add(cards.get(i));
            }
            this.hand.setBestHand(tmpCards); // Assign best 2 or 5 cards to Hand instance.
        }
    }
    
    private void checkPair() {
        // Check for 2 duplicates and only 2 duplicates.
        if(multiples.size() == 2){
            ArrayList<Card> tmpCards = new ArrayList<>();
            for(int i=0; i<2;i++){
                tmpCards.add(multiples.get(i));
            }
            if(this.cards.size() > 2){
                tmpCards = pickKickers(tmpCards);                   
            }
            this.hand.setHandType(Hand.hands.PAIR);  // Assign handtype to Hand instance.
            this.hand.setBestHand(tmpCards);  // Assign best 2 or 5 cards to Hand instance.
        } 
    };    
    
    private void checkTwoPair() {
        if(multiples.size() == 4 || multiples.size() == 6){
            ArrayList<Card> tmpCards = new ArrayList<>();
            for(int i=0; i<4;i++){
                tmpCards.add(multiples.get(i));
            }
            tmpCards = pickKickers(tmpCards);                   
            this.hand.setHandType(Hand.hands.TWOPAIR); // Assign handtype to Hand instance.
            this.hand.setBestHand(tmpCards); // Assign best 5 cards to Hand instance.
        }
    };
    
    private void checkTrip() {
        if(multiples.size() == 3 || multiples.size() == 6){
            /** To avoid analysing 3 x pairs, Sum the highest trips then 
             * divide by first num - Should be equal if all nums are the same.
             */ 
            int sum = multiples.get(0).getNum() 
                    + multiples.get(1).getNum() 
                    + multiples.get(2).getNum();
            if(sum/3 == multiples.get(0).getNum()) {
                ArrayList<Card> tmpCards = new ArrayList<>();
                for(int i=0; i<3;i++){
                    tmpCards.add(multiples.get(i));
                }
                tmpCards = pickKickers(tmpCards);                   
                this.hand.setHandType(Hand.hands.TRIP);
                this.hand.setBestHand(tmpCards); // Assign best 5 cards to Hand instance.
            }
        }
    };
    
    private void checkStraight() {
        // If straights list is not empty.
        if(!(straights.isEmpty())){
        /** setting BestHand to straights object will mean that this HandCaculator
         * object will persist beyond the current phase.
         */
            this.hand.setHandType(Hand.hands.STRAIGHT);
//            this.hand.setBestHand(tmpCards);            
            this.hand.setBestHand(straights); // Assign best 5 cards to Hand instance.
        }
    };

    private void checkFlush() {
        if(flushes.size() >= 5){
//            ArrayList<Card> tmpCards = new ArrayList<>();
//            for(int i=0; i<5;i++){
//                tmpCards.add(cards.get(i));
//                tmpCards = pickKickers(tmpCards);                   
//            }
            this.hand.setHandType(Hand.hands.FLUSH);
//            this.hand.setBestHand(tmpCards);            
        /** setting BestHand to flushes object will mean that this HandCaculator
         * object will persist beyond the current phase.
         */
            this.hand.setBestHand(flushes);  // Assign best 5 cards to Hand instance.
        }
    };
    
    private void checkFullHouse() {
        // Watch out for 3 x pairs, or lower sorted 2 x pairs + 1 x trips.
        if(multiples.size() >= 5){
            /** To avoid analysing 3 x pairs, Sum the highest trips then 
             * divide by first num - Should be equal if all nums are the same.
             */ 
            if( (multiples.get(0).getNum() 
                    + multiples.get(1).getNum() 
                    + multiples.get(2).getNum() ) 
                    /3 
                    == multiples.get(0).getNum()) {
                ArrayList<Card> tmpCards = new ArrayList<>();
                for(int i=0; i<3;i++){
                    tmpCards.add(multiples.get(i));
                }
                tmpCards.add(multiples.get(3));
                tmpCards.add(multiples.get(4));                
                this.hand.setHandType(Hand.hands.FULLHOUSE);
                this.hand.setBestHand(tmpCards);  // Assign best 5 cards to Hand instance.
            }
            else if( (multiples.get(2).getNum() 
                    + multiples.get(3).getNum() 
                    + multiples.get(4).getNum() ) 
                    /3
                    == multiples.get(2).getNum()) {
                ArrayList<Card> tmpCards = new ArrayList<>();
                for(int i=2; i<5;i++){
                    tmpCards.add(multiples.get(i));
                }
                tmpCards.add(multiples.get(0));
                tmpCards.add(multiples.get(1));                
                this.hand.setHandType(Hand.hands.FULLHOUSE);
                this.hand.setBestHand(tmpCards);  // Assign best 5 cards to Hand instance.
            }      
            else if (multiples.size() == 7 && 
                    (multiples.get(4).getNum() 
                    + multiples.get(5).getNum() 
                    + multiples.get(6).getNum() )
                    /3 
                    == multiples.get(4).getNum() ) {
                ArrayList<Card> tmpCards = new ArrayList<>();
                for(int i=4; i<7;i++){
                    tmpCards.add(multiples.get(i));
                }
                tmpCards.add(multiples.get(0));
                tmpCards.add(multiples.get(1));
                this.hand.setHandType(Hand.hands.FULLHOUSE);
                this.hand.setBestHand(tmpCards); // Assign best 5 cards to Hand instance.
                }
        }
    };
    
    private void checkQuad() {
        if (multiples.size() >= 4) {
            if ( (multiples.get(0).getNum()
                    + multiples.get(1).getNum()
                    + multiples.get(2).getNum()
                    + multiples.get(3).getNum())
                    /4
                    == multiples.get(0).getNum()) {
                ArrayList<Card> tmpCards = new ArrayList<>();
                for(int i=0; i<4;i++){
                    tmpCards.add(multiples.get(i));
                }
                tmpCards = pickKickers(tmpCards);
                this.hand.setHandType(Hand.hands.QUAD);
                this.hand.setBestHand(tmpCards); // Assign best 5 cards to Hand instance.
            }
            else if (multiples.size() == 6 && 
                    (multiples.get(2).getNum()
                    + multiples.get(3).getNum()
                    + multiples.get(4).getNum()
                    + multiples.get(5).getNum()
                    )
                    /4
                    == multiples.get(2).getNum()) {
                ArrayList<Card> tmpCards = new ArrayList<>();
                for(int i=2; i<6;i++){
                    tmpCards.add(multiples.get(i));
                }
                tmpCards = pickKickers(tmpCards);
                this.hand.setHandType(Hand.hands.QUAD);
                this.hand.setBestHand(tmpCards); // Assign best 5 cards to Hand instance.
            }
            else if (multiples.size() == 7 &&
                    (multiples.get(3).getNum()
                    + multiples.get(4).getNum()
                    + multiples.get(5).getNum()
                    + multiples.get(6).getNum()
                    )
                    /4
                    == multiples.get(3).getNum()) {
                ArrayList<Card> tmpCards = new ArrayList<>();
                for(int i=3; i<7;i++){
                    tmpCards.add(multiples.get(i));
                }
                tmpCards = pickKickers(tmpCards);
                this.hand.setHandType(Hand.hands.QUAD);
                this.hand.setBestHand(tmpCards); // Assign best 5 cards to Hand instance.
            }
        }    
    }
    
    private void checkStraightFlush() {
        if(!(straights.isEmpty()) && flushes.size() >= 5) {
            ArrayList<Card> tmpCards = new ArrayList<>();
            // For n cycles (between 1 - 3 cycles)
            for(int i=0; i< (flushes.size()-4); i++){
                // j & k set the args for the while loop below
                int j = i;
                int k = j+5;
                if(flushes.get(i).getNum() == (flushes.get(i+1).getNum() + 1)
                        && flushes.get(i+1).getNum() 
                        == (flushes.get(i+2).getNum() + 1)
                        && flushes.get(i+2).getNum() 
                        == (flushes.get(i+3).getNum() + 1)
                        && flushes.get(i+3).getNum() 
                        == (flushes.get(i+4).getNum() + 1)
                        )
                    // Replicates a for i<5 loop without intereference from i.
                    {
                    while (j < k){
                        tmpCards.add(flushes.get(j));
                        j++;
                    }
                    this.hand.setHandType(Hand.hands.STRAIGHTFLUSH);
                    this.hand.setBestHand(tmpCards);  // Assign best 5 cards to Hand instance.
                    // To stop at highest available straight.
                    break;
                }
            }
        }
    }
    
    private void checkRoyalFlush() {
        if(flushes.size() >= 5 && flushes.get(0).getAce()) {
            if(flushes.get(0).getNum() == (flushes.get(1).getNum() + 1)
                    && flushes.get(1).getNum() == (flushes.get(2).getNum() + 1)
                    && flushes.get(2).getNum() == (flushes.get(3).getNum() + 1)
                    && flushes.get(3).getNum() == (flushes.get(4).getNum() + 1)
                    ){
                ArrayList<Card> tmpCards = new ArrayList<>();
                for(int i=0; i<5;i++){
                    tmpCards.add(flushes.get(i));
                }
                this.hand.setHandType(Hand.hands.ROYALFLUSH);
                this.hand.setBestHand(tmpCards); // Assign best 5 cards to Hand instance.   
            }
        }
    }

    
    // To pick best remaining cards if best hand = HIGHCARD, PAIR, TRIP or QUAD
    private ArrayList<Card> pickKickers(ArrayList<Card> keeps){
        ArrayList<Card> bestCards = new ArrayList<>();
        for(Card k : keeps){
            bestCards.add(k);
        }
        for (Card c : cards){
            if(!(bestCards.contains(c)) && bestCards.size() < 5){
                    bestCards.add(c);
            }
        }
        return bestCards; // Added to HIGHCARD, PAIR, TRIP or QUAD cards.
    }
    
    
    // To check for Aces and insert new Card object with same suit but rank of 1.
    private ArrayList<Card> insertAces(ArrayList<Card> d){
        ArrayList<Card> cds = d;
        ArrayList<Card> ncds = new ArrayList<>();
        for(Card c : cds){
            ncds.add(c);
            if(c.getAce()){
                Card ace = new Card(1, c.getSuit(), true);
                ncds.add(ace);
            }    
        }
        return ncds;
    }
    
    // Applied to straight list only, so that only instance in case of multipes.
    private ArrayList<Card> removeDuplicates(ArrayList<Card> c){
        ArrayList<Card> nonDupeCards = new ArrayList<>();
        nonDupeCards.add(c.get(0));
        ArrayList<Integer> listOfInts = new ArrayList<>();
        listOfInts.add(c.get(0).getNum());
        for(Card comp : c){
            int compInt = comp.getNum();
             if(!(listOfInts.contains(compInt))){
                 nonDupeCards.add(comp);
                 listOfInts.add(compInt);
                }
            }
        return nonDupeCards;
    }

    class sortCardsComparator implements Comparator<Card> {
        @Override
        public int compare(Card a, Card b){
            return a.getNum() - b.getNum();
        }
    }
    

    // Sort a list of cards by rank in descending order.
    // For initial card list to decide if a straight, multiples & flushes     
    private void sortCards(ArrayList<Card> c){
        ArrayList C = c;
        C.iterator();
        sortCardsComparator mySCC = new sortCardsComparator();
        // Reverse (descending) order
        Collections.sort(C, Collections.reverseOrder(mySCC));
    }

               

    // Compare list of cards of each suit and return a sorted list of the cards of max suit .
    private ArrayList<Card> calcMaxSuitedCards(ArrayList<Card> c, 
                                               ArrayList<Card> d, 
                                               ArrayList<Card> h, 
                                               ArrayList<Card> s){
        ArrayList<ArrayList<Card>> suitList = new ArrayList<>();
        suitList.add(c);
        suitList.add(d);
        suitList.add(h);
        suitList.add(s);        
        int maxSize = 0;
        ArrayList maxSuit = c;
        for(ArrayList<Card> suit : suitList){
            if(suit.size() > maxSize){
                maxSize = suit.size();
                maxSuit = suit;
            }
        }
        sortCards(maxSuit);
        return maxSuit;
    }
    
    public void printBestPlayerHand(Hand playerHand){
        System.out.println("Player " + playerHand.getPlayerNum() 
                                         + " has " 
                                         + playerHand.getHandType() 
                                         + " with " 
                                         + playerHand.getBestHandString());    
    }
    
}
    