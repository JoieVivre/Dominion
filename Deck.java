//C. Atkinson
public class Deck
{   
   private final Card[] deck; //array that holds Card objects --> this deck will never change, only will deal to ArrayLists
   private final int startAmount; //number of initial cards in a new deck
   private final int type; //type of card in deck
   private final String name; //name of card in deck
   private final int value; //value of card in deck
   private final int cost; //cost of card in deck
   private int cardsAvailable; //cards left in the deck
   
   public Deck(int theStartAmount, int theType, String theName, int theValue, int theCost){
      deck = new Card[theStartAmount];
      startAmount = theStartAmount;
      type = theType; 
      name = theName; 
      value = theValue; 
      cost = theCost;
            
      for(int i = 0; i < startAmount; i++){
         deck[i] = new Card(theType, theName, theValue, theCost);
      }
      cardsAvailable = theStartAmount;
   }//Deck
   
   public void shuffle(){
//improve with some error checking
      int random;
      Card temp = new Card();
      
      for(int i = startAmount-1; i >= 0; i--){
         random = (int)(Math.random() * (i+1));
         temp = deck[random];
         deck[random] = deck[i];
         deck[i] = temp;         
      }//for
   }//shuffle end
   
   public int cardsRemaining(){
      return cardsAvailable;
   }
   //returns card's cost
   public int getCost(){
      return cost;
   }
      //returns card's type
   public int getType(){
      return type;
   }

   //returns card's name
   public String getName(){
      return name;
   }
   
   public Card dealCard(){
      cardsAvailable--;
      
      if(cardsAvailable < 0){
         //error handling if no more in deck
         System.out.println("No more cards in deck.");
         Card temp = new Card();
         return temp;
      }
      else{
         return deck[cardsAvailable];      
      }
   }
   
}//deck class end