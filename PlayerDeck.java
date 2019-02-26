//C. Atkinson
import java.util.*;

public class PlayerDeck{
   private String playerName;
   private ArrayList<Card> deck; //arraylist that holds Card objects
   private ArrayList<Card> workingDeck; //arraylist that holds Card objects currently in player's hand
   private ArrayList<Card> playArea;//arraylist that holds Card objects that have been played
   private ArrayList<Card> discardDeck; //arraylist that holds Card objects in discard pile
   private int numActions;
   private int numBuys;
   private int cardsAvailable; //cards left in the deck
   
   public PlayerDeck(String pName){
      playerName = pName;
      deck = new ArrayList<Card>();      
      workingDeck = new ArrayList<Card>();
      discardDeck = new ArrayList<Card>();
      playArea = new ArrayList<Card>();
      numActions = 0; 
      numBuys = 0;
      cardsAvailable = deck.size();
   }//PlayerDeck
    
   public void addCard(Card a){  //used in initial deck building, one time only          
      deck.add(a);
      cardsAvailable++;
   }
   public void addDiscard(Card a){ //used to add a bought card to discard pile
      discardDeck.add(a);
      System.out.println("\t" + playerName + " " + a + " was added to discard pile.");
   } 
   public boolean attackProtection(){
      for(int i = 0; i < workingDeck.size(); i++){
         if(workingDeck.get(i).getName().equals("Moat"))
            return true;
      }
      return false;
   }
   public void addToWorkDeck(Card a){  //add a card directly to working deck 
      workingDeck.add(a);
   }

   public int buyingPower(){   //buying power of current workingDeck
      int buyPower = 0;
      for(int i = 0; i < workingDeck.size(); i++){
         if(workingDeck.get(i).getType() == 1){
            buyPower+= workingDeck.get(i).getValue();
         }
      }
      return buyPower;
   }
   
   //returns index of Card in workingDeck with matching name, used with cardPlayed method
   public int cardMatch(String checkName){
      for(int i = 0; i < workingDeck.size(); i++){
         if(workingDeck.get(i).getName().equals(checkName))
            return i;
      }
      return -1;   
   }
   //moves one card from workingDeck to playArea
   public void cardPlayed(int index){
      System.out.println(workingDeck.get(index).getName() + " was played.");
      playArea.add(workingDeck.get(index));
      workingDeck.remove(index);
   }
   //at the end of a player's turn, move everything that was played + left overs in working deck --> into discard pile
   public void cleanUpPhase(){
      for(int i = 0; i < workingDeck.size(); i++){
         discardDeck.add(workingDeck.get(i));
      }
      for(int i = 0; i < playArea.size(); i++){
         discardDeck.add(playArea.get(i));
      }
      workingDeck.clear();
      playArea.clear();  
   }
   public void clearWorkingDeck(){
      workingDeck.clear();
   }
   public void clearDiscardDeck(){
      discardDeck.clear();
   }
   
   //return card from working deck w method --> to trash, remove from working deck
   public Card dealCard(int i){
      Card temp = new Card();
      temp = workingDeck.get(i);          
      workingDeck.remove(i);
      return temp; 
   }
   public void dealCard(){   //only for moving cards from deck to workingDeck
      cardsAvailable--;
   
      if(cardsAvailable < 0){
         //error handling if no more in deck
         System.out.println("\tNo more cards in deck.");
         System.out.println("\tThe discard deck will be shuffled now.");
         shuffle();//transferring discard to deck         
         cardsAvailable--;
      }
      workingDeck.add(deck.get(cardsAvailable));
      deck.remove(cardsAvailable);      
   }
   public void deckToDiscard(){//moves deck to discard pile
   
      for(int i = 0; i < deck.size(); i++){
         discardDeck.add(deck.get(i));
      }
      deck.clear();
      cardsAvailable = deck.size();   
   }
   public void decAction(){
      numActions--;
   }
   public void decBuy(){
      numBuys--;
   }
   public int deckSize(){
      return deck.size();
   } 
   
   public void incAction(){
      numActions++;
   }
   public void incBuy(){
      numBuys++;
   }
   public int numActions(){
      return numActions;
   }
   public int numBuys(){
      return numBuys;
   }
   public String playerName(){
      return playerName;
   }
   
   public void printWorkDeckLast(){//prints directly to screen
      System.out.println();
      System.out.println("\t " + workingDeck.get(workingDeck.size()-1).toString());
   }
   
   public void printWorkingDeck(){
      System.out.println();
      for(int i = 0; i < workingDeck.size(); i++){
         System.out.println("\tindex-" + i + " " + workingDeck.get(i).toString());
      }  
   }     
   public void printWorkingDeck(int type){//prints a specific type of card directly to screen
      System.out.println();
      for(int i = 0; i < workingDeck.size(); i++){
         if(workingDeck.get(i).getType() == type){System.out.println("\tindex-" + i + " " + workingDeck.get(i).toString());}
      }  
   }     
   public boolean printWorkingDeckLast2(){//prints a specific type of card directly to screen
      boolean thief = false;
      System.out.println();
      for(int i = workingDeck.size()-2; i < workingDeck.size(); i++){
         if(workingDeck.get(i).getType() == 1){
            System.out.println("\tindex-" + i + " " + workingDeck.get(i).toString());
            thief = true;
         }
      }
      return thief;
   }
   public void setAB(int a, int b){
      numActions = a;
      numBuys = b;
   }
   public void setA(int a){
      numActions = a;
   }
   public void setB(int b){
      numBuys = b;
   }
   public void setPlayerName(String name){
      playerName = name;
   }
   //only shuffles discardDeck and then puts shuffled deck into deck, called from dealCard method
   public void shuffle(){
      int random;
      Card temp = new Card();
      
      for(int i = discardDeck.size()-1; i >= 0; i--){
         random = (int)(Math.random() * (i+1));
         temp = discardDeck.get(random);
         discardDeck.set(random, discardDeck.get(i));
         discardDeck.set(i, temp);         
      }//for, actaully shuffling
      
      for(int i = 0; i < discardDeck.size(); i++){
         deck.add(discardDeck.get(i));
      }//for, transferring shuffled discardDeck to deck
      
      cardsAvailable = deck.size();
      discardDeck.clear();      
   }//shuffle end
   public void shuffleStart(){   //used once at game start to shuffle first deck
      int random;
      Card temp = new Card();
      
      for(int i = deck.size()-1; i >= 0; i--){
         random = (int)(Math.random() * (i+1));
         temp = deck.get(random);
         deck.set(random, deck.get(i));
         deck.set(i, temp);         
      }//for, actaully shuffling
   
      cardsAvailable = deck.size();
   }//shuffleStart end
  
   //used at games conclusion to determine winner and show victory cards
   public int victoryPoints(){
      int vPoints = 0;
      for(int i = 0; i < deck.size(); i++){
         if(deck.get(i).getType() == 2){
            vPoints += deck.get(i).getValue();
            System.out.println(deck.get(i).toString());
         }
      }
      for(int i = 0; i < discardDeck.size(); i++){
         if(discardDeck.get(i).getType() == 2){
            vPoints += discardDeck.get(i).getValue();
            System.out.println(discardDeck.get(i).toString());         
         }
      }
      for(int i = 0; i < workingDeck.size(); i++){
         if(workingDeck.get(i).getType() == 2){
            vPoints += workingDeck.get(i).getValue();
            System.out.println(workingDeck.get(i).toString());         
         }
      }   
      return vPoints;
   }
  
   public int workDeckSize(){
      return workingDeck.size();
   }
   public boolean workDeckLastCard(int type){
      if(workingDeck.get(workingDeck.size()-1).getType() == type){
         return true;}
      
      return false;
   }
    public void work2Deck(){//add last card in workingDeck back to Deck, used with Spy Kingdom card  
      deck.add(workingDeck.get(workingDeck.size()-1));                  
      cardsAvailable++;
      workingDeck.remove(workingDeck.size()-1);
   }

   public void work2Discard(){//add last card in workingDeck to discardDeck  
      Card temp = workingDeck.get(workingDeck.size()-1);
      discardDeck.add(temp);                  
      System.out.println("\t" + playerName + " " + temp + " was added to discard pile.");
      workingDeck.remove(workingDeck.size()-1);
   }
   //pulls a specific type of card to the discard pile, returns the number of cards pulled
   public int workDiscard(int type){
      int numCards = 0;
      for(int i = 0; i < workingDeck.size(); i++){
         if(workingDeck.get(i).getType() == type){
            discardDeck.add(workingDeck.get(i));
            System.out.println("\t" + playerName + " " + workingDeck.get(i) + " was added to discard pile.");
            numCards++;
            workingDeck.remove(i);
            i--;
         }
      }
         //will recheck the index number again if it removed something there
      return numCards;   
   }
   //does the working deck contain that type of card
   public boolean workingDeckType(int type){
      for(int i = 0; i < workingDeck.size(); i++){
         if(workingDeck.get(i).getType() == type){
            return true;}
      }
      return false;  
   }     
   public boolean workingDeckType(String namez){
      for(int i = 0; i < workingDeck.size(); i++){
         if(workingDeck.get(i).getName().equals(namez)){
            return true;}
      }
      return false;  
   }     
  
}//player deck class end