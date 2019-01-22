//C. Atkinson
import java.util.*;

public class Dominion{

   public static void play(String p1, String p2)
   {
      Scanner input = new Scanner (System.in);
      
      Supply thisGame = new Supply();
      PlayerDeck player1 = new PlayerDeck(p1);
      PlayerDeck player2 = new PlayerDeck(p2);
      
      //building initial decks
      for(int i = 1; i < 8; i++){
         player1.addCard(thisGame.returnDeck(0).dealCard());
         player2.addCard(thisGame.returnDeck(0).dealCard());
      }
      for(int i = 1; i < 4; i++){
         player1.addCard(thisGame.returnDeck(3).dealCard());
         player2.addCard(thisGame.returnDeck(3).dealCard());
      }
      player1.shuffleStart();
      player2.shuffleStart();
      //only happens once

      PlayerDeck whosTurn = new PlayerDeck("blank");
      //String whosTurn = "player1";
      whosTurn = player1;
      int turns = 1;
         
         //START OF TURN ALGO     
         do{
               System.out.println(whosTurn.playerName() + " dealing 5 cards to your hand now..");
               for(int i = 1; i <= 5; i++){
                  whosTurn.dealCard();
               }                  
//create method to deal specified number of cards         
               
               //pass to playerTurn method, ACTION and BUY happen there
               whosTurn.setAB(1, 1);
               playerTurn(whosTurn, thisGame);
               
               //CLEANUP
               System.out.println("CLEANUP: cards moved to discard pile");
               whosTurn.cleanUpPhase();
//with militia will need to decrease cards of opponents --> could deal cards now or do some kind of flag               
               System.out.println();
               System.out.println();
               
               //END OF TURN ALGORITHM
               turns++;
               if(turns % 2 == 0){
               whosTurn = player2;
               }
               else{
               whosTurn = player1;
               }                        
         }while((thisGame.haveProvince()) && (!thisGame.supply3PilesEmpty()));   
         /* how game would actually end
         (thisGame.haveProvince()) && (!thisGame.supply3PilesEmpty())
         
         }while((provinceDeck.cardsRemaining() > 0)  && 
               ((cellarDeck.cardsRemaining() > 0) || (moatDeck.cardsRemaining() > 0) || (cellarDeck.cardsRemaining() > 0))); 
          
          (thisGame.returnDeck(5).cardsRemaining() > 0)
          
          */
      
//after end of turns need to summarize decks, who had most number of victory points
      System.out.println(player1.playerName() + " your score was: " + player1.victoryPoints());
      System.out.println(player2.playerName() + " your score was: " + player2.victoryPoints());
      //System.out.println(player1.VictoryPoints() > player2.VictoryPoints() ? );
   }//end of play
     
   public static void playerTurn(PlayerDeck whosTurn, Supply checkSupply){//pass player name or number maybe
      Scanner input = new Scanner (System.in);
      int holdoverCoins = 0;

      //ACTION
      //checking for any action cards      
      if(!whosTurn.actionStart()){
         System.out.println("No action cards in your hand.");
      }
      else{
         //start of action loop
         do{
            System.out.println("Number of actions: " + whosTurn.numActions() + " What action will you play? ");
            whosTurn.printWorkingDeck();
            String actionChoice = input.next();
            
            if(actionChoice.equals("Moat")){
               //move moat from working Deck to discard   
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
   
               System.out.println("MOAT: \n2 cards have been added to your hand");
               //add 2 cards to working Deck            
               for(int i = 1; i < 3; i++){whosTurn.dealCard();}              
            }
            else if(actionChoice.equals("Market")){
               //move moat from working Deck to discard   
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
   
               System.out.println("MARKET: \n1 card has been added to your hand, you gained one additional action and one additional buy(if feasible),\nin addition to 1 extra gold coin");
               //add 2 cards to working Deck            
               whosTurn.dealCard();
               whosTurn.incAction();
               whosTurn.incBuy();
               holdoverCoins++;
            }
             else if(actionChoice.equals("Cellar")){
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
   
               System.out.println("CELLAR: \nyou have gained one additional action(if feasible)");
               //add 1 action
               whosTurn.incAction();
               System.out.print("Would you like to discard any cards? You are dealt that number of new cards from your deck. (y/n) ");
               String cellarAnswer = input.next();
               if(cellarAnswer.equals("y")){
               
                     System.out.print("Would you like to discard your victory cards? (y/n) ");
                     cellarAnswer = input.next();
                     if(cellarAnswer.equals("y")){
                        int temp = whosTurn.workDiscard(2);//moving
                        System.out.println("You will get " + temp + " extra card(s).");
                        for(int i = 1; i <= temp; i++){whosTurn.dealCard();}              
                     }
               }
            }
             else if(actionChoice.equals("Smithy")){
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
   
               System.out.println("SMITHY: \n3 cards have been added to your hand");
               //add 3 cards to working Deck            
               for(int i = 1; i < 4; i++){whosTurn.dealCard();}              
            }
             else if(actionChoice.equals("Woodcutter")){
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
   
               System.out.println("WOODCUTTER: \nYou now have an extra buy and 2 extra coins to buy with");
               //add 2 coins to holdover, add extra buy
               whosTurn.incBuy();
               holdoverCoins += 2;
            }
            else if(actionChoice.equals("Workshop")){
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
   
               System.out.println("WORKSHOP: \nYou gain any of the following cards costing up to 4 coins");
               //add new card to discard pile
               checkSupply.canBeBought(4);
               System.out.print("What card will you take? ");
               String workShopChoice = input.next();
               
               //match Choice to 
               whosTurn.addDiscard(checkSupply.returnDeck(checkSupply.matchName(workShopChoice)).dealCard());
            }            
            else if(actionChoice.equals("Village")){
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               System.out.println("VILLAGE: \n1 card has been added to your hand and you gained two more Actions(if feasible)");
               //add 1 card to working Deck, +2 actions            
               whosTurn.dealCard();
               for(int i = 1; i < 3; i++){whosTurn.incAction();}              
            }
            else if(actionChoice.equals("Remodel")){
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               System.out.println("REMODEL: \nTrash a card from you hand, gain a card costing up to 2 more coins than trashed card");
               //add card to trash, gain additional card            
               whosTurn.printWorkingDeck();
               System.out.print("Which card will you trash (1st card = 0, 2nd = 1, ..)? Your current hand is above: ");
               int remodelChoice = input.nextInt();               
               //trashing one card
               Card remodelTemp = new Card();
               remodelTemp = whosTurn.dealCard(remodelChoice);//removed from working deck when method called
               remodelChoice = remodelTemp.getCost(); //cost of trashed card
               checkSupply.addTrash(remodelTemp);//card actually added to trash

               
               checkSupply.canBeBought(remodelChoice + 2);
               System.out.println("What card will you take? ");
               String remodelChoicez = input.next();
                
               //match Choice to 
               whosTurn.addDiscard(checkSupply.returnDeck(checkSupply.matchName(remodelChoicez)).dealCard());            
               }
               else if(actionChoice.equals("Mine")){
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               System.out.println("MINE: \nTrash a Treasure card from you hand, gain a treasure card in your hand costing up to 3 more coins than thetrashed card");
               //add card to trash, gain additional card            
               
               //check that there are treasure cards
               if(whosTurn.workingDeckType(1)){
                  whosTurn.printWorkingDeck();
                  System.out.print("Which card will you trash (1st card = 0, 2nd = 1, ..)? Your current hand is above: ");
                  int mineChoice = input.nextInt();               
                  //trashing one card, still on honor system
                  Card mineTemp = new Card();
                  mineTemp = whosTurn.dealCard(mineChoice);//removed from working deck when method called
                  mineChoice = mineTemp.getCost(); //cost of trashed card
                  checkSupply.addTrash(mineTemp);//card actually added to trash
                  System.out.println(mineTemp + " has been trashed.");
                  checkSupply.canBeBought2(mineChoice + 3, 1);
                  System.out.print("What card will you take? ");
                  String mineChoicez = input.next();
                   
                  //match Choice to , 
                  whosTurn.addToWorkDeck(checkSupply.returnDeck(checkSupply.matchName(mineChoicez)).dealCard());
   //               whosTurn.addDiscard();            
               }
                  else{
                  System.out.print("There are no treasure cards in your hand to trash");
                  }
               }
            else{
            System.out.print("That's not how that's spelled..");
            }
            
         }while((whosTurn.numActions() > 0) && (whosTurn.actionStart()));         
         
      }//else --> have action card in first place

   //BUY
      //need to increase buypower by 2 some variable set to 0
      
      whosTurn.printWorkingDeck();
      int buyTemp = whosTurn.buyingPower() + holdoverCoins;
         do{
            //whosTurn.printWorkingDeck();
            System.out.println("Your buying power is: " + (buyTemp));      
            checkSupply.canBeBought(buyTemp);
            
            System.out.print("Will you BUY anything? (y/n) ");
            String buyAnswer = input.next();
            
            if (buyAnswer.equals("y")){
               String buyChoice = "";
               //doesn't check first before dropping in               
               do{
                  System.out.println("What card will you BUY? ");
                  buyChoice = input.next();
               }while(checkSupply.matchName(buyChoice) == -1); 
               
              int costBoughtCard = checkSupply.returnDeck(checkSupply.matchName(buyChoice)).getCost();
              if(costBoughtCard <= buyTemp){//can actually do buy
                  //have to subtract that amount from buyTemp               
                  buyTemp -= costBoughtCard;
   
                 //match buyChoice to
                 whosTurn.addDiscard(checkSupply.returnDeck(checkSupply.matchName(buyChoice)).dealCard());
                 whosTurn.decBuy();              
              }
              else{
               System.out.println("That card is too expensive, try again.");
              
              }
              
              
              }//buyAnswer == y
              
              
              else{//no buys
              whosTurn.setB(0);
              }
              
         }while((whosTurn.numBuys() > 0) && (buyTemp > 0));         
//buyStart might not work unless move the cards out of working Deck whosTurn.buyStart())

   }//playerTurn
   
}   