public class Card
{  
   public final static int TREASURE = 1; //Codes for four main types of card
   public final static int VICTORY = 2;
   public final static int KINGDOM = 3; 
   //public final static int CURSE = 4;//not always necessary 
  
   private final int type; //type of card (1, 2, 3)
   private final String name; //card name
   private final int value; //value
   private final int cost; //cost to buy
      
   public Card(){
      type = 0;
      name = "none";
      value = 0;
      cost = 0;
   }
   
   public Card(int theType, String theName, int theValue, int theCost){//Card created
      type = theType;
      name = theName;
      value = theValue;
      cost = theCost;
   }
   //returns card's type
   public int getType(){
      return type;
   }
   //returns card's name
   public String getName(){
      return name;
   }
   //returns card's value
   public int getValue(){
      return value;
   }
   //returns card's cost
   public int getCost(){
      return cost;
   }
   
   public String getTypeAsString(){
      switch ( type ){
      case 1:   return "TREASURE";
      case 2:   return " VICTORY";
      default:   return " KINGDOM";
      }
   }
   
   public String toString(){
      return getTypeAsString() + "-" + getName() + " Value: " + String.valueOf(getValue()) + " Cost: " + 
      String.valueOf(getCost());
   }

}