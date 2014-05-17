/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package poker;

/**
 * The starting class file for all poker games (created once per session, 
 * rather than per game as games can 'rollover' by returning to the phase=START
 * state) - a game progresses through each phase state using a KeyListener event 
 * handler (any key) that calls advancePhase() method in PokerManager.
 * @author Tim
 */
public class Poker {

    /**
     * @param args the command line arguments
     */
    public static void main (String[] args) {
        // Instance persists for the duration of a session.
        PokerManager newGame = new PokerManager();
    }
}

