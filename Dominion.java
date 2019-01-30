//C. Atkinson
import java.util.*;

public class Dominion{

   public static void play(int numOfPlayers, ArrayList<String> currentPlayerNames, String supplyChoice)
   {
      Scanner input = new Scanner (System.in);
      
      Supply thisGame = new Supply(numOfPlayers, supplyChoice);
      PlayerDeck player1 = new PlayerDeck(currentPlayerNames.get(0));
      PlayerDeck player2 = new PlayerDeck(currentPlayerNames.get(1));
      PlayerDeck player3 = new PlayerDeck("temp");
      PlayerDeck player4 = new PlayerDeck("temp");

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

      if(numOfPlayers > 2){
            player3.setPlayerName(currentPlayerNames.get(2));
            for(int i = 1; i < 8; i++){player3.addCard(thisGame.returnDeck(0).dealCard());}
            for(int i = 1; i < 4; i++){player3.addCard(thisGame.returnDeck(3).dealCard());}     
            player3.shuffleStart();
      }
      if(numOfPlayers > 3){
            player4.setPlayerName(currentPlayerNames.get(3));
            for(int i = 1; i < 8; i++){player4.addCard(thisGame.returnDeck(0).dealCard());}
            for(int i = 1; i < 4; i++){player4.addCard(thisGame.returnDeck(3).dealCard());}     
            player4.shuffleStart();
      }
      
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
               //check for attackCard
               if(!thisGame.attackCounter.isEmpty()){
               
               //pull first 2 charcters from String
               //process ArrayList
               for(int i = 0; i < thisGame.attackCounter.size(); i++){
                  
                  String attackName = thisGame.attackCounter.get(i);
                  int attackCount = Integer.valueOf(attackName.substring(0,1));
                  attackName = attackName.substring(1);
 
                  if(attackCount < numOfPlayers){
                     attackCount++;
                     
                     if(!whosTurn.attackProtection()){
                        System.out.println("You are not protected from the previous " + attackName + " attack card");
                        //specific for militia
                        //only do if whosTurn has at least 4 cards
                        for(int j = 1; j < 3; j++){
                        
                           if(whosTurn.workDeckSize() > 3){
                              boolean correctInput = false;
                              int attackChoice = -1;                        
                              
                              do{
                                 whosTurn.printWorkingDeck();
                                 
                                 try{
                                    System.out.print("WHICH CARD WILL YOU DISCARD? Your current hand is above: ");
                                    attackChoice = input.nextInt();
                                    correctInput = true;  
                                 }
                                 catch (InputMismatchException ex){
                                    System.out.println("You need to enter the index NUMBER of the card you wish to discard..");
                                    input.nextLine();
                                 }
                              }while(!correctInput);
                              
                              whosTurn.addDiscard(whosTurn.dealCard(attackChoice));                             
                           }
                           else{
                              System.out.println(".....but you're already down to 3 cards in your hand.");
                              j = 2;
                           }
                        }//for loop w j going through twice
                     }
                     else{//have protection
                         // attackCount++;
                          System.out.println("You are protected from the previous " + attackName + " attack card");
                     }
                     
                     attackName = attackCount + attackName;
                     //System.out.println("\t\tAttckName string is: " + attackName);
                     thisGame.attackCounter.set(i, attackName);
                  }//attackCount < numPlayers
                  else{
                     thisGame.attackCounter.remove(i);
                     i--;
                  }
               }//for loop processing attack array

               }//attack on
               
               //pass to playerTurn method, ACTION and BUY happen there
               whosTurn.setAB(1, 1);
               playerTurn(whosTurn, thisGame);
               
               //CLEANUP
               System.out.println("CLEANUP: cards moved to discard pile");
               whosTurn.cleanUpPhase();

               System.out.println();
               System.out.println();
               //END OF TURN ALGORITHM
               turns++;
               
               if(numOfPlayers == 2){
                  if(whosTurn == player1){whosTurn = player2;}
                  else{whosTurn = player1;}
               }
               else if(numOfPlayers == 3){
                  if(whosTurn == player1){whosTurn = player2;}
                  else if(whosTurn == player2){whosTurn = player3;}
                  else{whosTurn = player1;}
               }
               else{
                  if(whosTurn == player1){whosTurn = player2;}
                  else if(whosTurn == player2){whosTurn = player3;}
                  else if(whosTurn == player3){whosTurn = player4;}
                  else {whosTurn = player1;}
               }
               /*
               if(turns % 2 == 0){
               whosTurn = player2;
               }
               else{
               whosTurn = player1;
               } */
               
         }while((thisGame.haveProvince()) && (!thisGame.supply3PilesEmpty()));   
      
//after end of turns need to summarize decks, who had most number of victory points
//      int winner = 0;
//    String winner = "";
      
      System.out.println(player1.playerName() + " your score was: " + player1.victoryPoints());
      System.out.println();
      System.out.println(player2.playerName() + " your score was: " + player2.victoryPoints());
      System.out.println();      
      
      if(numOfPlayers > 2){
         System.out.println(player3.playerName() + " your score was: " + player3.victoryPoints());
         System.out.println();      
      }
      if(numOfPlayers > 3){
         System.out.println(player4.playerName() + " your score was: " + player4.victoryPoints());
         System.out.println();      
      }
      
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
            String actionChoice = "";
            //need to cycle through workingDeck to make sure actually have that card
            
            do{
               System.out.println("Number of actions: " + whosTurn.numActions() + " What action will you play? ");
               whosTurn.printWorkingDeck();
               actionChoice = input.next();
            }while(whosTurn.cardMatch(actionChoice) == -1);

            
            if(actionChoice.equals("Cellar")){
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
   
               System.out.println("CELLAR: \nyou have gained one additional action (if feasible)");
               //add 1 action
               whosTurn.incAction();
               
               int temp = 0;
               System.out.print("Would you like to discard your victory cards? You are dealt that number of new cards from your deck. (y/n) ");
               String cellarAnswer = input.next();
               if(cellarAnswer.equals("y")){
                  temp = whosTurn.workDiscard(2);//moving
                  System.out.println("You will get " + temp + " extra card(s).");
                  //for(int i = 1; i <= temp; i++){whosTurn.dealCard();}              
               }
                                
               //temp = 0;                  
               do{
                  whosTurn.printWorkingDeck();
                  System.out.print("Would you like to discard any more cards? (y/n) ");
                  cellarAnswer = input.next();
                  
                  if(cellarAnswer.equals("y")){
                          boolean cellarInput = false;
                              int cellarChoice = -1;                        
                              
                              do{
                                 whosTurn.printWorkingDeck();
                                 try{
                                    System.out.print("WHICH CARD WILL YOU DISCARD? Your current hand is above: ");
                                    cellarChoice = input.nextInt();
                                    cellarInput = true;
                                 }
                                 catch (InputMismatchException ex){
                                    System.out.println("You need to enter the index NUMBER of the card you wish to discard..");
                                    input.nextLine();
                                 }
                              }while(!cellarInput);
                              
                              whosTurn.addDiscard(whosTurn.dealCard(cellarChoice));
                              temp++;

                  }//if --> discard more after victory cards
                  else if(cellarAnswer.equals("n")){
                     //cellarAnswer = "n";
                  }
                  else{
                     System.out.println("\tYOU MUST ENTER y OR n");
                     cellarAnswer = "y";
                  }

                  }while(cellarAnswer.equals("y"));
                  
                  if(temp > 0){
                     System.out.println("You have been dealt " + temp + " extra card(s).");
                     for(int i = 1; i <= temp; i++){whosTurn.dealCard();}
                     //whosTurn.printWorkingDeck();              
                  }
                  
            }
            else if(actionChoice.equals("Feast")){
               whosTurn.decAction();
               System.out.println("FEAST: \nTrash this card from you hand, gain a card costing up to 5 coins");
               checkSupply.addTrash(whosTurn.dealCard(whosTurn.cardMatch(actionChoice)));//card actually added to trash

               checkSupply.canBeBought(5);
               System.out.println("What card will you take? ");
               String feastChoicez = input.next();
               //match Choice to 
               whosTurn.addDiscard(checkSupply.returnDeck(checkSupply.matchName(feastChoicez)).dealCard());            
            }
            else if(actionChoice.equals("Festival")){
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
   
               System.out.println("Festival: \nYou gained 2 additional actions, 1 additional buy (if feasible)");
               System.out.println("\tin addition to 2 extra gold coin");
               
               for(int i = 1; i <= 2; i++){whosTurn.incAction();}
               whosTurn.incBuy();
               for(int i = 1; i <= 2; i++){holdoverCoins++;}
            }

            else if(actionChoice.equals("Library")){
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
   
               System.out.println("LIBRARY: \nDraw until you have 7 cards in hand.");
               System.out.println("\tyou may discard any action cards drawn this way");
             //print out action card  
               do{
                  whosTurn.dealCard();
                  if(whosTurn.workDeckLastCard()){//dealt card to workingDeck, if its action card
                     whosTurn.printWorkDeckLast();
                     System.out.print("The last card dealt is an action card: do you want to keep it in your hand? (y/n) ");
                     String libraryChoice = input.next();
                     if(libraryChoice.equals("n")){
                        System.out.println("That card will be discarded."); 
                        whosTurn.work2Discard();//last card to discard
                     }
                  }
                  else{
                       whosTurn.printWorkDeckLast();
                       System.out.println("This card has been added to your hand."); 
                  } 
               }while(whosTurn.workDeckSize() < 7);  
            }
            
            else if(actionChoice.equals("Market")){
               //move moat from working Deck to discard   
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
   
               System.out.println("MARKET: \n1 card has been added to your hand, you gained one additional action (if feasible),");
               System.out.println("\t1 additional buy (if feasible), in addition to 1 extra gold coin");
               //add 1 cards to working Deck            
               whosTurn.dealCard();
               whosTurn.incAction();
               whosTurn.incBuy();
               holdoverCoins++;
            }
            else if(actionChoice.equals("Militia")){
               //move moat from working Deck to discard   
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));//moved to discard
   
               System.out.println("MILITIA: \nyou gained 2 additional coins to buy with");
               System.out.println("other players will have to discard down to 3 cards if they don't have protection");
               //add 2 coins, set attack to on
               holdoverCoins += 2;
               //could add player's name to String
               checkSupply.attackCounter.add("1Militia");
               //checkSupply.setAttack(true);
            }
            else if(actionChoice.equals("Mine")){
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               System.out.println("MINE: \nTrash a Treasure card from you hand, gain a treasure card in your hand costing up to 3 more coins than thetrashed card");
               //add card to trash, gain additional card            
               
               //check that there are treasure cards
               if(whosTurn.workingDeckType(1)){
                  whosTurn.printWorkingDeck(1);
                  System.out.print("Which card will you trash? Your current hand is above: ");
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
            else if(actionChoice.equals("Moat")){
               //move moat from working Deck to discard   
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
   
               System.out.println("MOAT: \n2 cards have been added to your hand");
               //add 2 cards to working Deck            
               for(int i = 1; i < 3; i++){whosTurn.dealCard();}              
            }    
                    
            else if(actionChoice.equals("Remodel")){
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               System.out.println("REMODEL: \nTrash a card from you hand, gain a card costing up to 2 more coins than trashed card");
               //add card to trash, gain additional card            
               whosTurn.printWorkingDeck();
               System.out.print("Which card will you trash? Your current hand is above: ");
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
            
            else if(actionChoice.equals("Smithy")){
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
   
               System.out.println("SMITHY: \n3 cards have been added to your hand");
               //add 3 cards to working Deck            
               for(int i = 1; i < 4; i++){whosTurn.dealCard();}              
            }
            
            else if(actionChoice.equals("Village")){
               whosTurn.decAction();
               whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               System.out.println("VILLAGE: \n1 card has been added to your hand and you gained two more Actions(if feasible)");
               //add 1 card to working Deck, +2 actions            
               whosTurn.dealCard();
               for(int i = 1; i < 3; i++){whosTurn.incAction();}              
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