/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package poker.GFX;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.Timer;
import java.util.ArrayList;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import poker.Dealer.Card;
import poker.Dealer.Hand;
import poker.PokerManager;
import poker.PokerManager.PhaseTypes;

/**
 * Manages the 2D graphics, displayed on theJFrame window in PokerManager.
 * Constructor loads card images (large/small) from disk, and sets card 
 * assignments to a default 'start' position of blank card backs.
 * Also defines and draws text and 2D text boxes.
 * @author Tim
 */
public class GFX_SetScene extends JComponent{

    private static Graphics G;
    private static Hand[] thisPlayerList;
    private static Card[] thisCommunity;    

    // Used as a shortcut in all sub classes
    private static final String path = "./Assets/";
    private static final String BH = "/BH_Cards/";

    private static BufferedImage baize; // Background image.
    private static BufferedImage cardBackPNG; // Blank card (big) used by all hands.
    private static BufferedImage BHcardBackPNG; // Blank card (small) used by all hands.
    
    // List of BufferedImages (cards), that are drawn by paintComponent.
    private static BufferedImage[] cardList;
    
    // List of BufferedImages (small best hand cards), that are drawn by paintComponent.
    private static BufferedImage[] bestHandCardList;
    
    // List of HOLDER card images, for assigning to cardX list variables on a timer.
    private static BufferedImage[] cardHolderList;

    // List of HOLDER best card images, for assigning to BHcardX list variables on a timer.    
    private static BufferedImage[] bestHandCardHolderList;

    // Coordinates for displaying BH cards (used for card images, text and icons).
    private static final int BH_X = 1055;
    private static final int BH_Y = 130;
    private static final int BH_X_Space = 42;
    private static final int BH_Y_Space = 56;    
    
    // counter and Delay for use in card update timer animations.
    private static int counter = 0;
    private static int BHcounter = -1; // counter for BH cards.
    private static int delay = 50;
        
    // Animation action for drawing player cards sequentially
    private static ActionListener playerDealTaskPerformer;
    private static ActionListener communityDealTaskPerformer;
    private static ActionListener bestHandRefreshTaskPerformer;
    
    // Phase specific timers with specific actions, called during PREFLOP and FLOP.
    private static Timer dealTimer;
    private static Timer flopTimer;
    private static Timer bestHandTimer;
    
    // BEST HAND INDICATOR ICONS
    private static BufferedImage star; // Currently winning hand icon.
    private static BufferedImage starFiller;  // Losing player icon (single pixel).
    private static BufferedImage chip;  // Winning hand icon (at RESULT).
    
    private static BufferedImage P1_Icon, P2_Icon, P3_Icon, P4_Icon, 
                                 P5_Icon, P6_Icon, P7_Icon, P8_Icon;
    
    // TEXT LABELS
    private static String pLabel = "NEW DEAL!"; // For phase text.
    private static String bhLabel = "BEST HAND"; // For best hand text.    
    private static String gameIdLabel = "Game ID Not Found"; // For gameIdBox text. TEMPORARY
    private static String mLabel = "ANY OLD STRING"; // For 'scroll' bar text. TEMPORARY.

    // FONTS AND FONT METRICS
    private static FontMetrics FM; // Font Metrics object used for text allignment.
    private static final Font BHCardsFont = new Font("MyriadPro-Cond", Font.BOLD, 40);
    private static final Font phaseFont = new Font("MyriadPro-Cond", Font.BOLD, 90);
    private static final Font pCardsFont = new Font("MyriadPro-Cond", Font.PLAIN, 40);
    private static final Font gCardsFont = new Font("MyriadPro-Cond", Font.PLAIN, 40);
    private static Font messageFont = new Font("MyriadPro-Cond", Font.PLAIN, 40); 
    
    
    // Box and border shapes and colours
    private static final Color crimsonFill = new Color(127,0,55);
    private static final Color redBorder = new Color(223,0,14);
    private static final Color white = new Color(255,255,255);
    private static final Color yellow = new Color(255,216,0);

    // 2D TEXT BOXES DRAWN IN CODE.
    private static final RoundRectangle2D.Double phaseLabelBox = new RoundRectangle2D.Double(
            952, 10,
            318, 100,
            10, 10);    
    private static final RoundRectangle2D.Double bestCardsBox = new RoundRectangle2D.Double(
            952, 123, 
            318, 458, 
            10, 10);
    private static final RoundRectangle2D.Double bestHandBox = new RoundRectangle2D.Double(
            952, 591, 
            318, 55, 
            10, 10);    
    private static final RoundRectangle2D.Double messageBar = new RoundRectangle2D.Double(
            10, 660,
            932, 47,
            10, 10);
    private static final RoundRectangle2D.Double gameIdBox = new RoundRectangle2D.Double(
            952, 660, 
            318, 47, 
            10, 10);
 
    
    // Constructor - which intialises and loads all cards to blank card backs.
    public GFX_SetScene() {

        // List of cardX variables, that are drawn by paintComponent.
        cardList = new BufferedImage[21];
    
        // List of HOLDER card variables, for assigning to cardX variables.
        cardHolderList = new BufferedImage[21];

        // List of HOLDER card variables, for assigning to cardX variables.
        bestHandCardHolderList = new BufferedImage[40];
        bestHandCardList = new BufferedImage[40];

        // LOAD image files from disk.    
        try {
            cardBackPNG = ImageIO.read(new File(path + "b2fv.png"));
            BHcardBackPNG = ImageIO.read(new File(path + BH + "BH_" + "b2fv.png"));
            baize = ImageIO.read(new File(path + "redbaize.jpg"));
            star = ImageIO.read(new File(path + "star.bmp"));
            starFiller = ImageIO.read(new File(path + "starFiller.bmp"));
            chip = ImageIO.read(new File(path + "winpokerchip.jpg"));
        } catch (IOException e) {
           System.out.println("The image was not loaded");
        }

        P1_Icon = starFiller;
        P2_Icon = starFiller;
        P3_Icon = starFiller;
        P4_Icon = starFiller;
        P5_Icon = starFiller;
        P6_Icon = starFiller;
        P7_Icon = starFiller;
        P8_Icon = starFiller;
        
        // Resets card images to blank card backs.
        for (int i=0; i > cardList.length; i++) {
            cardList[i] = cardBackPNG;
        }

        
        // Code for loading GameID setting, duplicated from PokerManager
        Properties prop = new Properties();
        InputStream input = null;
        OutputStream output = null;

        // Load poker.properties and assign/increment GameID to gameIdLabel
        try {
            // Props file found in class path.
            String filename = "poker.properties";
                
            // Load up properties file as input stream.
            input = PokerManager.class.getClassLoader().getResourceAsStream(filename);
    
            if(input==null){
                System.out.println("Unable to load " + filename + " as input.");
                return;
            }                      
            
            // Load properties file as output stream.
            output = new FileOutputStream(filename);
    
            // load data props file in to output stream.
            prop.load(input);

            // Retrieve GameID and assign to static gameID var.
            String GFXgameID = prop.getProperty("GameID", "69");

            // Set GFX gameIdLabel using static gameID var.
            setGameIdLabel(GFXgameID);
            
            // Create a new gameID string and increment by 1.
            String temp = String.valueOf(Integer.valueOf(GFXgameID) +1);
    
            output = new FileOutputStream("./build/classes/poker.properties");
            
            // Set properties GameID val (in-memory) to (temp) incremented gameID val.
            prop.setProperty("GameID", temp);
            
            // Save properties to project root folder.
            prop.store(output, null);
        } 
        catch (IOException io) {
            io.printStackTrace();
        } 
        finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                     e.printStackTrace();
                }
            } 
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        // Animation action for drawing player cards sequentially
        playerDealTaskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // Updates cardX reference with holder ref, to delayed timer.
                if(counter <= 15){
                    cardList[counter] = cardHolderList[counter];
                    counter++;
                    PokerManager.getJFrame().repaint();                    
                }
                else if(counter >= 16){
                    dealTimer.stop();
                }
            }
        };

        // Animation action for drawing flop cards sequentially.
        communityDealTaskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // Updates cardX reference with holder ref, to delayed timer.
                if(counter >= 16 && counter <= 18){
                    cardList[counter] = cardHolderList[counter];
                    counter++;
                    PokerManager.getJFrame().repaint();
                }
                else if(counter >= 19){
                    flopTimer.stop();
                    counter = 0;
                }
            }
        };

        // Animation action for drawing best hand cards sequentially.
        // Updates best hand card list with the new assignments from best card holder list, to delayed timer.        
        bestHandRefreshTaskPerformer = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                // At START phase, BHcounter = -1. 
                if (BHcounter < 0){
                    resetBestHandCardList(); // Set BH cards to blank card back.
                    BHcounter++; // increment counter to execute next block.
                    PokerManager.getJFrame().repaint();  // Redraw new assignments.
                }
                // Executes after resetting BH cards to blank card backs.
                else if (BHcounter >= 0 && BHcounter < 5){
                    bestHandCardList[BHcounter] = bestHandCardHolderList[BHcounter];
                    bestHandCardList[BHcounter + 5] = bestHandCardHolderList[BHcounter + 5];
                    bestHandCardList[BHcounter + 10] = bestHandCardHolderList[BHcounter + 10];
                    bestHandCardList[BHcounter + 15] = bestHandCardHolderList[BHcounter + 15];
                    bestHandCardList[BHcounter + 20] = bestHandCardHolderList[BHcounter + 20];
                    bestHandCardList[BHcounter + 25] = bestHandCardHolderList[BHcounter + 25];
                    bestHandCardList[BHcounter + 30] = bestHandCardHolderList[BHcounter + 30];
                    bestHandCardList[BHcounter + 35] = bestHandCardHolderList[BHcounter + 35];                
                    BHcounter++;
                    PokerManager.getJFrame().repaint();
                }
                else if(BHcounter >= 5){
                    bestHandTimer.stop();  // Stop timer to finish assigning cards.
                    BHcounter = -1;  // Redraw new assignments.
                }
           }
        };
    
        // Phase specific timers with specific actions, called during PREFLOP and FLOP.
        dealTimer = new Timer(delay, playerDealTaskPerformer);
        flopTimer = new Timer(delay*2, communityDealTaskPerformer);
        // Called at each phase to update best hand cards.
        bestHandTimer = new Timer(delay, bestHandRefreshTaskPerformer);           
        }

    // The JComponent drawing method for all GFX.
    @Override
    public void paintComponent(Graphics g) {
        GFX_SetScene.G = g;
        Graphics2D g2 = (Graphics2D) g;  // Create a 2DGraphics object.

        g.drawImage(baize, 0, 0, null);  // Draw background baize image.

        g.setColor(crimsonFill); // Set colour of gameIdBox and cards boxes.
        g2.fill(phaseLabelBox);
        g2.fill(bestCardsBox);
        g2.fill(bestHandBox);
        g2.fill(gameIdBox);
        g2.fill(messageBar);

        g.setColor(redBorder);  // Set colour of shape borders.
        g2.setStroke(new BasicStroke(3));  // Increase border width.
        g2.draw(phaseLabelBox);
        g2.draw(bestCardsBox);
        g2.draw(bestHandBox);
        g2.draw(gameIdBox);
        g2.draw(messageBar);

        g.setColor(white); // Set colour of text to white.
        
        g.setFont(gCardsFont); // Draw BH Players and gameID text.
        FM = g.getFontMetrics();
        g.drawString(gameIdLabel, 1111 - (FM.stringWidth(gameIdLabel)/2), 697);
        g.drawString(bhLabel, 1110 - (FM.stringWidth(bhLabel)/2), 632);
        
        g.setFont(messageFont); // Draw the message bar text at bottom of screen.
        FM = g.getFontMetrics();
        g.drawString(mLabel, 476 - (FM.stringWidth(mLabel)/2), 697 );
 
        g.setFont(phaseFont); // Draw the PHASE box text at top right.
        FM = g.getFontMetrics();
        g.drawString(pLabel, 1108 - (FM.stringWidth(pLabel)/2), 90 );
         
        g.setColor(yellow); // Set colour of text to yellow.

        g.setFont(pCardsFont);  // Draw the player X text beneath each (big) pair of cards. 
        g.drawString("Player 1", 70, 180);
        g.drawString("Player 2", 292, 180);
        g.drawString("Player 3", 524, 180);
        g.drawString("Player 4", 746, 180);
        g.drawString("Player 5", 70, 510);
        g.drawString("Player 6", 292, 510);
        g.drawString("Player 7", 524, 510);
        g.drawString("Player 8", 746, 510);

        g.setFont(BHCardsFont); // Draw the Px text next to each row of BH (small) cards.
        g.drawString("P1", BH_X - 50, BH_Y + 40);
        g.drawString("P2", BH_X - 50, BH_Y + 40 + BH_Y_Space);
        g.drawString("P3", BH_X - 50, BH_Y + 40 + (BH_Y_Space*2));
        g.drawString("P4", BH_X - 50, BH_Y + 40 + (BH_Y_Space*3));
        g.drawString("P5", BH_X - 50, BH_Y + 40 + (BH_Y_Space*4));
        g.drawString("P6", BH_X - 50, BH_Y + 40 + (BH_Y_Space*5));
        g.drawString("P7", BH_X - 50, BH_Y + 40 + (BH_Y_Space*6));
        g.drawString("P8", BH_X - 50, BH_Y + 40 + (BH_Y_Space*7));
        

        // Set coordinates and Draw hole cards for players 1-4 (top row), from cardList.
        g.drawImage(cardList[0], 40, 40, null);
        g.drawImage(cardList[1], 132, 40, null);
        g.drawImage(cardList[2], 262, 40, null);
        g.drawImage(cardList[3], 354, 40, null);
        g.drawImage(cardList[4], 486, 40, null);
        g.drawImage(cardList[5], 578, 40, null);
        g.drawImage(cardList[6], 710, 40, null);
        g.drawImage(cardList[7], 802, 40, null);
        
        // Hole cards for players 5-8 (bottom row)        
        g.drawImage(cardList[8], 40, 534, null);
        g.drawImage(cardList[9], 132, 534, null);
        g.drawImage(cardList[10], 262, 534, null);
        g.drawImage(cardList[11], 354, 534, null);
        g.drawImage(cardList[12], 486, 534, null);
        g.drawImage(cardList[13], 578, 534, null);
        g.drawImage(cardList[14], 710, 534, null);
        g.drawImage(cardList[15], 802, 534, null);
        
        // Community cards 1-5 (middle row)        
        g.drawImage(cardList[16], 236, 282, null);
        g.drawImage(cardList[17], 328, 282, null);
        g.drawImage(cardList[18], 420, 282, null);
        g.drawImage(cardList[19], 512, 282, null);
        g.drawImage(cardList[20], 604, 282, null);

        // Best cards for player 1
        // Best Card images use BH_X and BH_Y coordinates.
        g.drawImage(bestHandCardList[0], BH_X, BH_Y, null);
        g.drawImage(bestHandCardList[1], BH_X + BH_X_Space, BH_Y, null);
        g.drawImage(bestHandCardList[2], BH_X + (BH_X_Space*2), BH_Y, null);
        g.drawImage(bestHandCardList[3], BH_X + (BH_X_Space*3), BH_Y, null);
        g.drawImage(bestHandCardList[4], BH_X + (BH_X_Space*4), BH_Y, null);

        // Best cards for player 2
        g.drawImage(bestHandCardList[5], BH_X, BH_Y + BH_Y_Space, null);
        g.drawImage(bestHandCardList[6], BH_X + BH_X_Space, BH_Y + BH_Y_Space, null);
        g.drawImage(bestHandCardList[7], BH_X + (BH_X_Space*2), BH_Y + BH_Y_Space, null);
        g.drawImage(bestHandCardList[8], BH_X + (BH_X_Space*3), BH_Y + BH_Y_Space, null);
        g.drawImage(bestHandCardList[9], BH_X + (BH_X_Space*4), BH_Y + BH_Y_Space, null);
        // Best cards for player 3        
        g.drawImage(bestHandCardList[10], BH_X, BH_Y + (BH_Y_Space*2), null);
        g.drawImage(bestHandCardList[11], BH_X + BH_X_Space, BH_Y + (BH_Y_Space*2), null);
        g.drawImage(bestHandCardList[12], BH_X + (BH_X_Space*2), BH_Y + (BH_Y_Space*2), null);
        g.drawImage(bestHandCardList[13], BH_X + (BH_X_Space*3), BH_Y + (BH_Y_Space*2), null);
        g.drawImage(bestHandCardList[14], BH_X + (BH_X_Space*4), BH_Y + (BH_Y_Space*2), null);   
        // Best cards for player 4        
        g.drawImage(bestHandCardList[15], BH_X, BH_Y + (BH_Y_Space*3), null);
        g.drawImage(bestHandCardList[16], BH_X + BH_X_Space, BH_Y + (BH_Y_Space*3), null);
        g.drawImage(bestHandCardList[17], BH_X + (BH_X_Space*2), BH_Y + (BH_Y_Space*3), null);
        g.drawImage(bestHandCardList[18], BH_X + (BH_X_Space*3), BH_Y + (BH_Y_Space*3), null);
        g.drawImage(bestHandCardList[19], BH_X + (BH_X_Space*4), BH_Y + (BH_Y_Space*3), null);
        // Best cards for player 5        
        g.drawImage(bestHandCardList[20], BH_X, BH_Y + (BH_Y_Space*4), null);
        g.drawImage(bestHandCardList[21], BH_X + BH_X_Space, BH_Y + (BH_Y_Space*4), null);
        g.drawImage(bestHandCardList[22], BH_X + (BH_X_Space*2), BH_Y + (BH_Y_Space*4), null);
        g.drawImage(bestHandCardList[23], BH_X + (BH_X_Space*3), BH_Y + (BH_Y_Space*4), null);
        g.drawImage(bestHandCardList[24], BH_X + (BH_X_Space*4), BH_Y + (BH_Y_Space*4), null);           
        // Best cards for player 6        
        g.drawImage(bestHandCardList[25], BH_X, BH_Y + (BH_Y_Space*5), null);
        g.drawImage(bestHandCardList[26], BH_X + BH_X_Space, BH_Y + (BH_Y_Space*5), null);
        g.drawImage(bestHandCardList[27], BH_X + (BH_X_Space*2), BH_Y + (BH_Y_Space*5), null);
        g.drawImage(bestHandCardList[28], BH_X + (BH_X_Space*3), BH_Y + (BH_Y_Space*5), null);
        g.drawImage(bestHandCardList[29], BH_X + (BH_X_Space*4), BH_Y + (BH_Y_Space*5), null);
        // Best cards for player 7        
        g.drawImage(bestHandCardList[30], BH_X, BH_Y + (BH_Y_Space*6), null);
        g.drawImage(bestHandCardList[31], BH_X + BH_X_Space, BH_Y + (BH_Y_Space*6), null);
        g.drawImage(bestHandCardList[32], BH_X + (BH_X_Space*2), BH_Y + (BH_Y_Space*6), null);
        g.drawImage(bestHandCardList[33], BH_X + (BH_X_Space*3), BH_Y + (BH_Y_Space*6), null);
        g.drawImage(bestHandCardList[34], BH_X + (BH_X_Space*4), BH_Y + (BH_Y_Space*6), null);   
        // Best cards for player 8        
        g.drawImage(bestHandCardList[35], BH_X, BH_Y + (BH_Y_Space*7), null);
        g.drawImage(bestHandCardList[36], BH_X + BH_X_Space, BH_Y + (BH_Y_Space*7), null);
        g.drawImage(bestHandCardList[37], BH_X + (BH_X_Space*2), BH_Y + (BH_Y_Space*7), null);
        g.drawImage(bestHandCardList[38], BH_X + (BH_X_Space*3), BH_Y + (BH_Y_Space*7), null);
        g.drawImage(bestHandCardList[39], BH_X + (BH_X_Space*4), BH_Y + (BH_Y_Space*7), null);

        // Display a star for winning hand or the filler (single red pixel).
        g.drawImage(P1_Icon, 958, 5 + BH_Y, null);
        g.drawImage(P2_Icon, 958, 5 + BH_Y + BH_Y_Space, null);
        g.drawImage(P3_Icon, 958, 5 + BH_Y + (BH_Y_Space*2), null);
        g.drawImage(P4_Icon, 958, 5 + BH_Y + (BH_Y_Space*3), null);
        g.drawImage(P5_Icon, 958, 5 + BH_Y + (BH_Y_Space*4), null);
        g.drawImage(P6_Icon, 958, 5 + BH_Y + (BH_Y_Space*5), null);
        g.drawImage(P7_Icon, 958, 5 + BH_Y + (BH_Y_Space*6), null);
        g.drawImage(P8_Icon, 958, 5 + BH_Y + (BH_Y_Space*7), null);        
   
    }

    // Reassigns new card images to cardList indices, action varies depending on the phase STATE.
    public static void updateCards(PhaseTypes phase){        
        // Reload different cards depending on the phase.
        switch (phase){
            case START: 

                bhLabel = "BEST HAND"; 
                setMessageFont("ANY OLD STRING");
                
                cardList[0] = cardBackPNG;
                cardList[1] = cardBackPNG;
                cardList[2] = cardBackPNG;
                cardList[3] = cardBackPNG;
                cardList[4] = cardBackPNG;
                cardList[5] = cardBackPNG;
                cardList[6] = cardBackPNG;
                cardList[7] = cardBackPNG;
           
                cardList[8] = cardBackPNG;
                cardList[9] = cardBackPNG;
                cardList[10] = cardBackPNG;
                cardList[11] = cardBackPNG;
                cardList[12] = cardBackPNG;
                cardList[13] = cardBackPNG;
                cardList[14] = cardBackPNG;
                cardList[15] = cardBackPNG;
             
                cardList[16] = cardBackPNG;
                cardList[17] = cardBackPNG;
                cardList[18] = cardBackPNG;
                cardList[19] = cardBackPNG;
                cardList[20] = cardBackPNG;

                resetBestHandCardList(); // Assigns blank card backs to all BH cards.
                
                // Assign single pixel filler icon to each player icon.
                P1_Icon = starFiller;
                P2_Icon = starFiller;
                P3_Icon = starFiller;
                P4_Icon = starFiller;
                P5_Icon = starFiller;
                P6_Icon = starFiller;
                P7_Icon = starFiller;
                P8_Icon = starFiller;

                break;
            
            // PHASE = PRE FLOP.
            case PREFLOP:  
                
                thisPlayerList = PokerManager.getThisDealPlayerList();
                thisCommunity = PokerManager.getThisDealCommunityList();        
                mLabel = "The cards have been dealt, do you feel lucky Punk?  Well, do you?";
                
                try {
                    cardHolderList[0] = ImageIO.read(new File(path + thisPlayerList[0].getCard(1).getImage()));
                    cardHolderList[1] = ImageIO.read(new File(path + thisPlayerList[0].getCard(2).getImage()));
                    cardHolderList[2] = ImageIO.read(new File(path + thisPlayerList[1].getCard(1).getImage()));
                    cardHolderList[3] = ImageIO.read(new File(path + thisPlayerList[1].getCard(2).getImage()));
                    cardHolderList[4] = ImageIO.read(new File(path + thisPlayerList[2].getCard(1).getImage()));
                    cardHolderList[5] = ImageIO.read(new File(path + thisPlayerList[2].getCard(2).getImage()));
                    cardHolderList[6] = ImageIO.read(new File(path + thisPlayerList[3].getCard(1).getImage()));
                    cardHolderList[7] = ImageIO.read(new File(path + thisPlayerList[3].getCard(2).getImage()));

                    cardHolderList[8] = ImageIO.read(new File(path + thisPlayerList[4].getCard(1).getImage()));
                    cardHolderList[9] = ImageIO.read(new File(path + thisPlayerList[4].getCard(2).getImage()));
                    cardHolderList[10] = ImageIO.read(new File(path + thisPlayerList[5].getCard(1).getImage()));
                    cardHolderList[11] = ImageIO.read(new File(path + thisPlayerList[5].getCard(2).getImage()));
                    cardHolderList[12] = ImageIO.read(new File(path + thisPlayerList[6].getCard(1).getImage()));
                    cardHolderList[13] = ImageIO.read(new File(path + thisPlayerList[6].getCard(2).getImage()));
                    cardHolderList[14] = ImageIO.read(new File(path + thisPlayerList[7].getCard(1).getImage()));
                    cardHolderList[15] = ImageIO.read(new File(path + thisPlayerList[7].getCard(2).getImage()));
                    
                    ArrayList<Card> P1BestHand = thisPlayerList[0].getBestHand();            
                    bestHandCardHolderList[0] = ImageIO.read(new File(path + BH + "BH_" + P1BestHand.get(0).getImage()));
                    bestHandCardHolderList[1] = ImageIO.read(new File(path + BH + "BH_" + P1BestHand.get(1).getImage()));
                    bestHandCardHolderList[2] = BHcardBackPNG;
                    bestHandCardHolderList[3] = BHcardBackPNG;
                    bestHandCardHolderList[4] = BHcardBackPNG;
                    
                    ArrayList<Card> P2BestHand = thisPlayerList[1].getBestHand();            
                    bestHandCardHolderList[5] = ImageIO.read(new File(path + BH + "BH_" + P2BestHand.get(0).getImage()));
                    bestHandCardHolderList[6] = ImageIO.read(new File(path + BH + "BH_" + P2BestHand.get(1).getImage()));
                    bestHandCardHolderList[7] = BHcardBackPNG;
                    bestHandCardHolderList[8] = BHcardBackPNG;
                    bestHandCardHolderList[9] = BHcardBackPNG;
                    
                    ArrayList<Card> P3BestHand = thisPlayerList[2].getBestHand();            
                    bestHandCardHolderList[10] = ImageIO.read(new File(path + BH + "BH_" + P3BestHand.get(0).getImage()));
                    bestHandCardHolderList[11] = ImageIO.read(new File(path + BH + "BH_" + P3BestHand.get(1).getImage()));
                    bestHandCardHolderList[12] = BHcardBackPNG;
                    bestHandCardHolderList[13] = BHcardBackPNG;
                    bestHandCardHolderList[14] = BHcardBackPNG;

                    ArrayList<Card> P4BestHand = thisPlayerList[3].getBestHand();            
                    bestHandCardHolderList[15] = ImageIO.read(new File(path + BH + "BH_" + P4BestHand.get(0).getImage()));
                    bestHandCardHolderList[16] = ImageIO.read(new File(path + BH + "BH_" + P4BestHand.get(1).getImage()));
                    bestHandCardHolderList[17] = BHcardBackPNG;
                    bestHandCardHolderList[18] = BHcardBackPNG;
                    bestHandCardHolderList[19] = BHcardBackPNG;
                    
                    ArrayList<Card> P5BestHand = thisPlayerList[4].getBestHand();            
                    bestHandCardHolderList[20] = ImageIO.read(new File(path + BH + "BH_" + P5BestHand.get(0).getImage()));
                    bestHandCardHolderList[21] = ImageIO.read(new File(path + BH + "BH_" + P5BestHand.get(1).getImage()));
                    bestHandCardHolderList[22] = BHcardBackPNG;
                    bestHandCardHolderList[23] = BHcardBackPNG;
                    bestHandCardHolderList[24] = BHcardBackPNG;
                    
                    ArrayList<Card> P6BestHand = thisPlayerList[5].getBestHand();            
                    bestHandCardHolderList[25] = ImageIO.read(new File(path + BH + "BH_" + P6BestHand.get(0).getImage()));
                    bestHandCardHolderList[26] = ImageIO.read(new File(path + BH + "BH_" + P6BestHand.get(1).getImage()));
                    bestHandCardHolderList[27] = BHcardBackPNG;
                    bestHandCardHolderList[28] = BHcardBackPNG;
                    bestHandCardHolderList[29] = BHcardBackPNG;
                    
                    ArrayList<Card> P7BestHand = thisPlayerList[6].getBestHand();            
                    bestHandCardHolderList[30] = ImageIO.read(new File(path + BH + "BH_" + P7BestHand.get(0).getImage()));
                    bestHandCardHolderList[31] = ImageIO.read(new File(path + BH + "BH_" + P7BestHand.get(1).getImage()));
                    bestHandCardHolderList[32] = BHcardBackPNG;
                    bestHandCardHolderList[33] = BHcardBackPNG;
                    bestHandCardHolderList[34] = BHcardBackPNG;
                    
                    ArrayList<Card> P8BestHand = thisPlayerList[7].getBestHand();            
                    bestHandCardHolderList[35] = ImageIO.read(new File(path + BH + "BH_" + P8BestHand.get(0).getImage()));
                    bestHandCardHolderList[36] = ImageIO.read(new File(path + BH + "BH_" + P8BestHand.get(1).getImage()));
                    bestHandCardHolderList[37] = BHcardBackPNG;
                    bestHandCardHolderList[38] = BHcardBackPNG;
                    bestHandCardHolderList[39] = BHcardBackPNG;
                    
                } catch (IOException e) {
                        System.out.println("The image was not loaded");
                    }
                dealTimer.start(); // Assigns new card values to card image list on a timer.
                bestHandTimer.start();  // Assigns new BH card values to BH card image list on a timer.

                break;

            // PHASE = THE FLOP
            case FLOP:
                mLabel = "BOOM! The dealer has flopped his cards down.";
                try {
                    // Community Flop cards (1st, 2nd, 3rd)                    
                    cardHolderList[16] = ImageIO.read(new File(path + thisCommunity[0].getImage()));
                    cardHolderList[17] = ImageIO.read(new File(path + thisCommunity[1].getImage()));
                    cardHolderList[18] = ImageIO.read(new File(path + thisCommunity[2].getImage()));
                    
                    loadNewBestHandCardImages();
                    
                } catch (IOException e) {
                        System.out.println("The image was not loaded");
                    }       
                flopTimer.start(); // Assigns the dealt flop card values to card list images, on a timer. 
                bestHandTimer.start(); // Re-assigns new BH card values to BH card lists, on a timer.
                break;
                
            // PHASE = THE TURN.    
            case TURN:  
                try {
                    mLabel = "And that's the Turn card.  Will it turn the game for someone?";
                    
                    // Community Turn card (4th)
                    cardList[19] = ImageIO.read(new File(path + thisCommunity[3].getImage()));

                    loadNewBestHandCardImages();
                    
                } catch (IOException e) {
                    System.out.println("The image was not loaded");
                }
                
                bestHandTimer.start(); // Re-assigns new BH card values to BH card lists, on a timer.
                
                break;
                
            // PHASE = THE RIVER.    
            case RIVER:  
                try {
                    
                    mLabel = "And that's the River card.  Won't someone cry me a river?";

                    // Community River card (5th)                    
                    cardList[20] = ImageIO.read(new File(path + thisCommunity[4].getImage()));

                    loadNewBestHandCardImages();
                    
                } catch (IOException e) {
                        System.out.println("The image was not loaded");
                }

                bestHandTimer.start(); // Re-assigns new BH card values to BH card lists, on a timer.
                
                break;
            
            // PHASE = THE RESULT (no action required, handled within AnalyseWinner() method).
            case RESULT:
                
                break;
                
        }
    }

    // Reload the latest BestHand cards for all 8 player Hands.  Throws IO exception.
    private static void loadNewBestHandCardImages() throws IOException {

        try {
                    ArrayList<Card> P1BestHand = thisPlayerList[0].getBestHand();            
                    bestHandCardHolderList[0] = ImageIO.read(new File(path + BH + "BH_" + P1BestHand.get(0).getImage()));
                    bestHandCardHolderList[1] = ImageIO.read(new File(path + BH + "BH_" + P1BestHand.get(1).getImage()));
                    bestHandCardHolderList[2] = ImageIO.read(new File(path + BH + "BH_" + P1BestHand.get(2).getImage()));
                    bestHandCardHolderList[3] = ImageIO.read(new File(path + BH + "BH_" + P1BestHand.get(3).getImage()));
                    bestHandCardHolderList[4] = ImageIO.read(new File(path + BH + "BH_" + P1BestHand.get(4).getImage()));
                    
                    ArrayList<Card> P2BestHand = thisPlayerList[1].getBestHand();            
                    bestHandCardHolderList[5] = ImageIO.read(new File(path + BH + "BH_" + P2BestHand.get(0).getImage()));
                    bestHandCardHolderList[6] = ImageIO.read(new File(path + BH + "BH_" + P2BestHand.get(1).getImage()));
                    bestHandCardHolderList[7] = ImageIO.read(new File(path + BH + "BH_" + P2BestHand.get(2).getImage()));
                    bestHandCardHolderList[8] = ImageIO.read(new File(path + BH + "BH_" + P2BestHand.get(3).getImage()));
                    bestHandCardHolderList[9] = ImageIO.read(new File(path + BH + "BH_" + P2BestHand.get(4).getImage()));
                    
                    ArrayList<Card> P3BestHand = thisPlayerList[2].getBestHand();            
                    bestHandCardHolderList[10] = ImageIO.read(new File(path + BH + "BH_" + P3BestHand.get(0).getImage()));
                    bestHandCardHolderList[11] = ImageIO.read(new File(path + BH + "BH_" + P3BestHand.get(1).getImage()));
                    bestHandCardHolderList[12] = ImageIO.read(new File(path + BH + "BH_" + P3BestHand.get(2).getImage()));
                    bestHandCardHolderList[13] = ImageIO.read(new File(path + BH + "BH_" + P3BestHand.get(3).getImage()));
                    bestHandCardHolderList[14] = ImageIO.read(new File(path + BH + "BH_" + P3BestHand.get(4).getImage()));

                    ArrayList<Card> P4BestHand = thisPlayerList[3].getBestHand();            
                    bestHandCardHolderList[15] = ImageIO.read(new File(path + BH + "BH_" + P4BestHand.get(0).getImage()));
                    bestHandCardHolderList[16] = ImageIO.read(new File(path + BH + "BH_" + P4BestHand.get(1).getImage()));
                    bestHandCardHolderList[17] = ImageIO.read(new File(path + BH + "BH_" + P4BestHand.get(2).getImage()));
                    bestHandCardHolderList[18] = ImageIO.read(new File(path + BH + "BH_" + P4BestHand.get(3).getImage()));
                    bestHandCardHolderList[19] = ImageIO.read(new File(path + BH + "BH_" + P4BestHand.get(4).getImage()));                    

                    ArrayList<Card> P5BestHand = thisPlayerList[4].getBestHand();            
                    bestHandCardHolderList[20] = ImageIO.read(new File(path + BH + "BH_" + P5BestHand.get(0).getImage()));
                    bestHandCardHolderList[21] = ImageIO.read(new File(path + BH + "BH_" + P5BestHand.get(1).getImage()));
                    bestHandCardHolderList[22] = ImageIO.read(new File(path + BH + "BH_" + P5BestHand.get(2).getImage()));
                    bestHandCardHolderList[23] = ImageIO.read(new File(path + BH + "BH_" + P5BestHand.get(3).getImage()));
                    bestHandCardHolderList[24] = ImageIO.read(new File(path + BH + "BH_" + P5BestHand.get(4).getImage()));
                    
                    ArrayList<Card> P6BestHand = thisPlayerList[5].getBestHand();            
                    bestHandCardHolderList[25] = ImageIO.read(new File(path + BH + "BH_" + P6BestHand.get(0).getImage()));
                    bestHandCardHolderList[26] = ImageIO.read(new File(path + BH + "BH_" + P6BestHand.get(1).getImage()));
                    bestHandCardHolderList[27] = ImageIO.read(new File(path + BH + "BH_" + P6BestHand.get(2).getImage()));
                    bestHandCardHolderList[28] = ImageIO.read(new File(path + BH + "BH_" + P6BestHand.get(3).getImage()));
                    bestHandCardHolderList[29] = ImageIO.read(new File(path + BH + "BH_" + P6BestHand.get(4).getImage()));
                    
                    ArrayList<Card> P7BestHand = thisPlayerList[6].getBestHand();            
                    bestHandCardHolderList[30] = ImageIO.read(new File(path + BH + "BH_" + P7BestHand.get(0).getImage()));
                    bestHandCardHolderList[31] = ImageIO.read(new File(path + BH + "BH_" + P7BestHand.get(1).getImage()));
                    bestHandCardHolderList[32] = ImageIO.read(new File(path + BH + "BH_" + P7BestHand.get(2).getImage()));
                    bestHandCardHolderList[33] = ImageIO.read(new File(path + BH + "BH_" + P7BestHand.get(3).getImage()));
                    bestHandCardHolderList[34] = ImageIO.read(new File(path + BH + "BH_" + P7BestHand.get(4).getImage()));

                    ArrayList<Card> P8BestHand = thisPlayerList[7].getBestHand();            
                    bestHandCardHolderList[35] = ImageIO.read(new File(path + BH + "BH_" + P8BestHand.get(0).getImage()));
                    bestHandCardHolderList[36] = ImageIO.read(new File(path + BH + "BH_" + P8BestHand.get(1).getImage()));
                    bestHandCardHolderList[37] = ImageIO.read(new File(path + BH + "BH_" + P8BestHand.get(2).getImage()));
                    bestHandCardHolderList[38] = ImageIO.read(new File(path + BH + "BH_" + P8BestHand.get(3).getImage()));
                    bestHandCardHolderList[39] = ImageIO.read(new File(path + BH + "BH_" + P8BestHand.get(4).getImage()));
  
                } catch (IOException e) {
                        System.out.println("The image was not loaded");
                    }       

    }
    
    // Sets the font of the message bar to ensure that SPLIT POT strings don't exceed the box borders.
    // Called by AnalyseWinner() method in PokerManager.
    public static void setMessageFont(String label){

        if(G == null){
            messageFont = new Font("MyriadPro-Cond", Font.PLAIN, 40);
        }
        else {
            G.setFont(messageFont);
            FM = G.getFontMetrics();

            if(FM.stringWidth(mLabel) > 680){
                messageFont = new Font("MyriadPro-Cond", Font.PLAIN, 34);
            }
            else{
                messageFont = new Font("MyriadPro-Cond", Font.PLAIN, 40);
            }            
        }
    }    
    
    public static void setGameIdLabel(String gameID){
        gameIdLabel = "Game # " + gameID;
    }
    
    public static void setPLabel(String label){
        pLabel = label;
    }

    public static void setMLabel(String label){
        mLabel = label;
    }
    
    public static void setBHLabel(String handtype){
        bhLabel = handtype;
    }    

    public void setGameID(String gameID){
        gameIdLabel = gameID;
    }    
    
    public static void setPlayerIcon(ArrayList<Hand> winnerList){
        // Reset all icons to filler first.
        P1_Icon = starFiller;
        P2_Icon = starFiller;
        P3_Icon = starFiller;
        P4_Icon = starFiller;
        P5_Icon = starFiller;
        P6_Icon = starFiller;
        P7_Icon = starFiller;
        P8_Icon = starFiller;

        // If PHASE is NOT equal to RESULT.
        if( ! PokerManager.getPhase().matches("RESULT") ){
            // Then update star icons if player in winnerList.
            for(Hand p : winnerList){
                switch(p.getPlayerNum()){
                    case 1: P1_Icon = star;
                        break;
                    case 2: P2_Icon = star;
                        break; 
                    case 3: P3_Icon = star;
                        break;
                    case 4: P4_Icon = star;
                        break; 
                    case 5: P5_Icon = star;
                        break; 
                    case 6: P6_Icon = star;
                        break;
                    case 7: P7_Icon = star;
                        break;
                    case 8: P8_Icon = star;
                        break;
                }
            }
        } 
        else{
            // If phase DOES equal "RESULT" then update star icons if player in winnerList.
            for(Hand p : winnerList){
                switch(p.getPlayerNum()){
                    case 1: P1_Icon = chip;
                        break;
                    case 2: P2_Icon = chip;
                        break; 
                    case 3: P3_Icon = chip;
                        break;
                    case 4: P4_Icon = chip;
                        break; 
                    case 5: P5_Icon = chip;
                        break; 
                    case 6: P6_Icon = chip;
                        break;
                    case 7: P7_Icon = chip;
                        break;
                    case 8: P8_Icon = chip;
                        break;
                }
            }        
        }
    }    

    // Resets best hand card list variables to small 'back of card' image.
    private static void resetBestHandCardList(){
        
        bestHandCardList[0] = BHcardBackPNG;
        bestHandCardList[1] = BHcardBackPNG;
        bestHandCardList[2] = BHcardBackPNG;
        bestHandCardList[3] = BHcardBackPNG;
        bestHandCardList[4] = BHcardBackPNG;
        bestHandCardList[5] = BHcardBackPNG;
        bestHandCardList[6] = BHcardBackPNG;
        bestHandCardList[7] = BHcardBackPNG;
        bestHandCardList[8] = BHcardBackPNG;
        bestHandCardList[9] = BHcardBackPNG;
        bestHandCardList[10] = BHcardBackPNG;
        bestHandCardList[11] = BHcardBackPNG;
        bestHandCardList[12] = BHcardBackPNG;
        bestHandCardList[13] = BHcardBackPNG;
        bestHandCardList[14] = BHcardBackPNG;
        bestHandCardList[15] = BHcardBackPNG;
        bestHandCardList[16] = BHcardBackPNG;
        bestHandCardList[17] = BHcardBackPNG;
        bestHandCardList[18] = BHcardBackPNG;
        bestHandCardList[19] = BHcardBackPNG;
        bestHandCardList[20] = BHcardBackPNG;
        bestHandCardList[21] = BHcardBackPNG;
        bestHandCardList[22] = BHcardBackPNG;
        bestHandCardList[23] = BHcardBackPNG;
        bestHandCardList[24] = BHcardBackPNG;
        bestHandCardList[25] = BHcardBackPNG;
        bestHandCardList[26] = BHcardBackPNG;
        bestHandCardList[27] = BHcardBackPNG;
        bestHandCardList[28] = BHcardBackPNG;
        bestHandCardList[29] = BHcardBackPNG;
        bestHandCardList[30] = BHcardBackPNG;
        bestHandCardList[31] = BHcardBackPNG;
        bestHandCardList[32] = BHcardBackPNG;
        bestHandCardList[33] = BHcardBackPNG;
        bestHandCardList[34] = BHcardBackPNG;
        bestHandCardList[35] = BHcardBackPNG;
        bestHandCardList[36] = BHcardBackPNG;
        bestHandCardList[37] = BHcardBackPNG;
        bestHandCardList[38] = BHcardBackPNG;
        bestHandCardList[39] = BHcardBackPNG;   
    }
    
    public Dimension getPreferredSize() {
        if (baize == null) {
             return new Dimension(1280,720);
        } 
        else {
           return new Dimension(baize.getWidth(null), baize.getHeight(null));
       }
    }
}