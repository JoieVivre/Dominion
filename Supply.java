//C. Atkinson

import java.util.*;

public class Supply
{
   private Deck[] supply; //array of deck objects, holds all the piles of cards ever used in game
   private ArrayList<Card> trashDeck;

   public Supply(){
            
      trashDeck = new ArrayList<Card>();
      supply = new Deck[15];
      
      //add curse deck in      
      Deck copperDeck = new Deck(60, 1, "Copper", 1, 0);              
      supply[0] = copperDeck; 
      Deck silverDeck = new Deck(40, 1, "Silver", 2, 3);  
      supply[1] = silverDeck;
      Deck goldDeck = new Deck(30, 1, "Gold", 3, 6);
      supply[2] = goldDeck;
                        
      Deck estateDeck = new Deck(14, 2, "Estate", 1, 2);  
      supply[3] = estateDeck;            
      Deck duchyDeck = new Deck(8, 2, "Duchy", 3, 5);  
      supply[4] = duchyDeck;
      Deck provinceDeck = new Deck(8, 2, "Province", 6, 8);
      supply[5] = provinceDeck;
            
      Deck cellarDeck = new Deck(8, 3, "Cellar", 0, 2);  
      supply[6] = cellarDeck;
      Deck moatDeck = new Deck(8, 3, "Moat", 0, 2);  
      supply[7] = moatDeck;
      Deck smithyDeck = new Deck(8, 3, "Smithy", 0, 4);
      supply[8] = smithyDeck;            
      Deck villageDeck = new Deck(8, 3, "Village", 0, 3);
      supply[9] = villageDeck;            
      Deck woodcutterDeck = new Deck(8, 3, "Woodcutter", 0, 3);
      supply[10] = woodcutterDeck;            
      Deck marketDeck = new Deck(8, 3, "Market", 0, 5);
      supply[11] = marketDeck;            
      Deck workshopDeck = new Deck(8, 3, "Workshop", 0, 3);
      supply[12] = workshopDeck;            
      Deck remodelDeck = new Deck(8, 3, "Remodel", 0, 4);
      supply[13] = remodelDeck;            
      Deck mineDeck = new Deck(8, 3, "Mine", 0, 5);
      supply[14] = mineDeck;            
                  
   
   }//Supply
   
   //returns a deck in the supply --> used to deal out cards in other methods
   public Deck returnDeck(int i){
      return supply[i];
   }
   //used with methods to trash card from player's deck, have to be passed card
   public void addTrash(Card a){
      trashDeck.add(a);
   }
   //which decks can be acquired depending on amount
//do printf   
   public void canBeBought(int buyPower){
      System.out.println("You can acquire: ");
      for(int i = 0; i < supply.length; i++){
         if((buyPower >= supply[i].getCost()) && (supply[i].cardsRemaining() > 0)){
            System.out.println("\t" + supply[i].getName() + "  cost-" + supply[i].getCost());
         }
      }
   }
   public void canBeBought2(int buyPower, int type){
      System.out.println("You can acquire: ");
      for(int i = 0; i < supply.length; i++){
         if(supply[i].getType() == type){
            if((buyPower >= supply[i].getCost()) && (supply[i].cardsRemaining() > 0)){
            System.out.println("\t" + supply[i].getName() + "  cost-" + supply[i].getCost());
            }
         }
            
      }
   }
   
   //pulls index of matching deck
   public int matchName(String deckName){
      for(int i = 0; i < supply.length; i++){
         if(deckName.equals(supply[i].getName())){
            return i;
         }
      }   
      System.out.println("\tNo Deck with that name in the supply..check spelling.");
      return -1;
   }
   //does the game continue       
   public boolean haveProvince(){
      return supply[5].cardsRemaining() > 0;
   }       
   //does the game continue
   public boolean supply3PilesEmpty(){
      int emptyPiles = 0;
      for(int i = 0; i < supply.length; i++){
         if(supply[i].cardsRemaining() == 0){emptyPiles++;}
      }   
      return emptyPiles >= 3;
   }
   
}