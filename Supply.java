//C. Atkinson

import java.util.*;

public class Supply
{
   private Deck[] supply; //array of deck objects, holds all the piles of cards ever used in game
   private ArrayList<Card> trashDeck;
   private boolean attackCard;
   public ArrayList<String> attackCounter;
      
   public Supply(int numberPlayers, String supplyString){
            
      trashDeck = new ArrayList<Card>();
      supply = new Deck[16];
      attackCard = false;
      attackCounter = new ArrayList<String>();  
      
      //add curse deck in      
      Deck copperDeck = new Deck(60, 1, "Copper", 1, 0);              
      supply[0] = copperDeck; 
      Deck silverDeck = new Deck(40, 1, "Silver", 2, 3);  
      supply[1] = silverDeck;
      Deck goldDeck = new Deck(30, 1, "Gold", 3, 6);
      supply[2] = goldDeck;

      Deck estateDeck;
      if(numberPlayers == 2){estateDeck = new Deck(14, 2, "Estate", 1, 2);}                        
      else if(numberPlayers == 3){estateDeck = new Deck(17, 2, "Estate", 1, 2);}                  
      else {estateDeck = new Deck(20, 2, "Estate", 1, 2);}                  
      supply[3] = estateDeck;            
      
      Deck duchyDeck = new Deck(8, 2, "Duchy", 3, 5);  
      supply[4] = duchyDeck;
      Deck provinceDeck = new Deck(8, 2, "Province", 6, 8);
      supply[5] = provinceDeck;
            
            
 //KINGDOM CARDS           
           
   int j = 0;          
   for(int i = 6; i <= 15; i++){
   
      if (supplyString.charAt(j) == 'C'){
               Deck cellarDeck = new Deck(8, 3, "Cellar", 0, 2);  
               supply[i] = cellarDeck;               
         }      
      else if (supplyString.charAt(j) == 'M'){
         Deck moatDeck = new Deck(8, 3, "Moat", 0, 2);  
         supply[i] = moatDeck;
         }      
      else if (supplyString.charAt(j) == 'V'){
         Deck villageDeck = new Deck(8, 3, "Village", 0, 3);
         supply[i] = villageDeck;            
        }
        else if (supplyString.charAt(j) == 'W'){
          Deck woodcutterDeck = new Deck(8, 3, "Woodcutter", 0, 3);
          supply[i] = woodcutterDeck;            
        }
        else if (supplyString.charAt(j) == 'O'){
         Deck workshopDeck = new Deck(8, 3, "Workshop", 0, 3);
         supply[i] = workshopDeck;            
        }        
        else if (supplyString.charAt(j) == 'F'){
         Deck feastDeck = new Deck(8, 3, "Feast", 0, 4);
         supply[i] = feastDeck;            
        }        
        else if (supplyString.charAt(j) == 'I'){
         Deck militiaDeck = new Deck(8, 3, "Militia", 0, 4);
         supply[i] = militiaDeck;            
        }        
        else if (supplyString.charAt(j) == 'R'){
         Deck remodelDeck = new Deck(8, 3, "Remodel", 0, 4);
         supply[i] = remodelDeck;            
        }        
        else if (supplyString.charAt(j) == 'S'){
         Deck smithyDeck = new Deck(8, 3, "Smithy", 0, 4);
         supply[i] = smithyDeck;            
        }        
        else if (supplyString.charAt(j) == 'Q'){
          Deck festivalDeck = new Deck(8, 3, "Festival", 0, 5);
           supply[i] = festivalDeck;            
        }        
        else if (supplyString.charAt(j) == 'X'){
         Deck libraryDeck = new Deck(8, 3, "Library", 0, 5);
         supply[i] = libraryDeck;            
        }        
        else if (supplyString.charAt(j) == 'K'){
           Deck marketDeck = new Deck(8, 3, "Market", 0, 5);
           supply[i] = marketDeck;            
        }        
        else if (supplyString.charAt(j) == 'Y'){
         Deck mineDeck = new Deck(8, 3, "Mine", 0, 5);
         supply[i] = mineDeck;            
        }        
        
        j++;    
   }//for loop --> supply[]
            
   
   }//Supply
   
   public boolean attackOn(){
      return attackCard;
   }

   public void setAttack(boolean status){
      attackCard = status;
   }

   //returns a deck in the supply --> used to deal out cards in other methods
   public Deck returnDeck(int i){
      return supply[i];
   }
   //used with methods to trash card from player's deck, have to be passed card
   public void addTrash(Card a){
      System.out.println("\t" + a + " was just trashed.");
      trashDeck.add(a);
   }
   //which decks can be acquired depending on amount
//do printf   
   public void canBeBought(int buyPower){
      System.out.println("You can acquire: ");
      for(int i = 0; i < supply.length; i++){
         
         if((buyPower >= supply[i].getCost()) && (supply[i].cardsRemaining() <= 0)){
            System.out.printf("\t%10s  DECK EMPTY\n", supply[i].getName());
         }
         if((buyPower >= supply[i].getCost()) && (supply[i].cardsRemaining() > 0)){
//            System.out.println("\t" + supply[i].getName() + "  cost-" + supply[i].getCost());
            System.out.printf("\t%10s  COST - %d\n", supply[i].getName(), supply[i].getCost());
         }
         if((i == 2) || (i == 5)){System.out.println();}
      }
   }
   public void canBeBought2(int buyPower, int type){
      System.out.println("You can acquire: ");
      for(int i = 0; i < supply.length; i++){
         if(supply[i].getType() == type){
            if((buyPower >= supply[i].getCost()) && (supply[i].cardsRemaining() > 0)){
            System.out.printf("\t%10s  COST - %d\n", supply[i].getName(), supply[i].getCost());
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
         if(supply[i].cardsRemaining() <= 0){emptyPiles++;}
      }   
      return emptyPiles >= 3;
   }
   
}