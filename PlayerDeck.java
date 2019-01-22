//C. Atkinson
import java.util.*;

public class PlayerDeck
{
   private final String playerName; //arraylist that holds Card objects
   private ArrayList<Card> deck; //arraylist that holds Card objects
   private ArrayList<Card> workingDeck; //arraylist that holds Card objects currently in player's hand
   private ArrayList<Card> discardDeck; //arraylist that holds Card objects in discard pile
   private ArrayList<Card> playArea;//arraylist that holds Card objects currently in play
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
   public void incAction(){
      numActions++;
   }
   public void incBuy(){
      numBuys++;
   }
   public void decAction(){
      numActions--;
   }
   public void decBuy(){
      numBuys--;
   }
   
   public String playerName(){
      return playerName;
   }
   public int numActions(){
      return numActions;
   }
   public int numBuys(){
      return numBuys;
   }
   public int deckSize(){
      return deck.size();
   }
   //determines if there are any action cards in workingDeck
   public boolean actionStart(){
      for(int i = 0; i < workingDeck.size(); i++){
         if(workingDeck.get(i).getType() == 3)return true;
      }
      return false;
   }
   //determines if there are any action cards in workingDeck
   public boolean buyStart(){
      for(int i = 0; i < workingDeck.size(); i++){
         if(workingDeck.get(i).getType() == 1)return true;
      }
      return false;
   }
   //used in initial deck building, one time only          
   public void addCard(Card a){
      deck.add(a);
      cardsAvailable++;
   }
   //used once at game start to shuffle first deck
   public void shuffleStart(){
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
   //pulls a specific type of card to the discard pile, returns the number of cards pulled
   public int workDiscard(int type){
         int numCards = 0;
         for(int i = 0; i < workingDeck.size(); i++){
            if(workingDeck.get(i).getType() == type){
                  discardDeck.add(workingDeck.get(i));
                  numCards++;
                  workingDeck.remove(i);
                  i--;
            }
         }
         //will recheck the index number again if it removed something there
         return numCards;   
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
   //only for moving cards from deck to workingDeck
   public void dealCard(){
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
   //buying power of current workingDeck
    public int buyingPower(){
      int buyPower = 0;
      for(int i = 0; i < workingDeck.size(); i++){
         if(workingDeck.get(i).getType() == 1){
            buyPower+= workingDeck.get(i).getValue();
         }
      }
      return buyPower;
   }
   //used to add a bought card to discard pile
   public void addDiscard(Card a){
      discardDeck.add(a);
      System.out.println(a + " added to discard pile.");
   }
   //returns index of Card in workingDeck with matching name, used with cardPlayed method
   public int cardMatch(String checkName){
      for(int i = 0; i < workingDeck.size(); i++){
         if(workingDeck.get(i).getName().equals(checkName))return i;
      }
      return -1;   
   }
   //moves one card from workingDeck to playArea
   public void cardPlayed(int index){
      System.out.println(workingDeck.get(index).getName() + " was played.");
      playArea.add(workingDeck.get(index));
      workingDeck.remove(index);
   }
   //prints directly to screen
   public void printWorkingDeck(){
         System.out.println();
       for(int i = 0; i < workingDeck.size(); i++){
            System.out.println("\t" + workingDeck.get(i).toString());
      }  
   }     
   //does the working deck contain that type of card --> similar to actionStart, butStart
   public boolean workingDeckType(int type){
       for(int i = 0; i < workingDeck.size(); i++){
            if(workingDeck.get(i).getType() == type){return true;}
      }
      return false;  
   }     
   //prints a specific type of card directly to screen
   public void printWorkingDeck(int type){
         System.out.println();
       for(int i = 0; i < workingDeck.size(); i++){
            if(workingDeck.get(i).getType() == type){System.out.println("\t" + workingDeck.get(i).toString());}
      }  
   }     
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
      return vPoints;
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
   //add a card directly to working deck 
   public void addToWorkDeck(Card a){
      workingDeck.add(a);
   }
      
}//player deck class end