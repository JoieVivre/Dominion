//C. Atkinson
import java.util.*;

public class Dominion{
   int holdoverCoins = 0;

   public static void play(int numOfPlayers, ArrayList<String> currentPlayerNames, String supplyChoice)
   {
      Scanner input = new Scanner (System.in);
      
      Supply thisGame = new Supply(numOfPlayers, supplyChoice);
      PlayerDeck player1 = new PlayerDeck(currentPlayerNames.get(0));
      PlayerDeck player2 = new PlayerDeck(currentPlayerNames.get(1));
      PlayerDeck player3 = new PlayerDeck("temp");
      PlayerDeck player4 = new PlayerDeck("temp");
   
      ArrayList<PlayerDeck> turnOrder= new ArrayList<PlayerDeck>();
   
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
      turnOrder.add(player1);
      turnOrder.add(player2);
       
      if(numOfPlayers > 2){
         player3.setPlayerName(currentPlayerNames.get(2));
         for(int i = 1; i < 8; i++){player3.addCard(thisGame.returnDeck(0).dealCard());}
         for(int i = 1; i < 4; i++){player3.addCard(thisGame.returnDeck(3).dealCard());}     
         player3.shuffleStart();
         turnOrder.add(player3);
      }
      if(numOfPlayers > 3){
         player4.setPlayerName(currentPlayerNames.get(3));
         for(int i = 1; i < 8; i++){player4.addCard(thisGame.returnDeck(0).dealCard());}
         for(int i = 1; i < 4; i++){player4.addCard(thisGame.returnDeck(3).dealCard());}     
         player4.shuffleStart();
         turnOrder.add(player4);
      }
      
      PlayerDeck whosTurn = new PlayerDeck("blank");
      int turns = 1;
   
      for(int i = 0; i < turnOrder.size(); i++){
         System.out.println(turnOrder.get(i).playerName() + " dealing FIRST 5 cards to your hand now..");
         for(int j = 1; j <= 5; j++){turnOrder.get(i).dealCard();}
      }      
      System.out.println();
      
      //START OF TURN ALGO        
      int turnCounter = 0;               
      do{
               //for(int i = 0; i < turnorder.size(); i++){
         whosTurn = turnOrder.get(turnCounter);
         whosTurn.setAB(1, 1);
         playerTurn(turnCounter, whosTurn, thisGame, turnOrder);//could pass i as well
      
               //CLEANUP
         System.out.print("CLEANUP: cards moved to discard pile..");
         whosTurn.cleanUpPhase();
         System.out.println(" and dealing the next 5 cards to your hand now..");
         for(int i = 1; i <= 5; i++){whosTurn.dealCard();}
         turnOrder.set(turnCounter, whosTurn);
               //END OF TURN ALGORITHM
                              
         System.out.println();
         System.out.println();
      
         turns++;
         turnCounter++;
         if(turnCounter >= numOfPlayers){turnCounter = 0;}
               
      }while((thisGame.haveProvince()) && (!thisGame.supply3PilesEmpty()));   
      
   //after end of turns need to summarize decks, who had most number of victory points
      int temp = 0;
      int winner = 0;
      String winnerName = "";
      boolean gards = thisGame.gardensOn();
      
      for(int i = 0; i < turnOrder.size(); i++){
      
         System.out.println(turnOrder.get(i).playerName() + " your score was: "); //+ );
         temp = turnOrder.get(i).victoryPoints(gards);
         if(temp > winner){
            winner = temp;
            winnerName = turnOrder.get(i).playerName();
         }
         System.out.println(temp + " VICTORY POINTS\n");
      }      
      System.out.println(winnerName + " you're the game winner with " + winner + " points. \nCONGRATS");
      //System.out.println(player1.VictoryPoints() > player2.VictoryPoints() ? );
   }//end of play
     
   public static void playerTurn(int turnCounter, PlayerDeck whosTurn, Supply checkSupply, ArrayList<PlayerDeck> turnOrder){//pass player name or number maybe
      Scanner input = new Scanner (System.in);
      int holdoverCoins = 0;
      //ACTION
      System.out.println(whosTurn.playerName() + " ACTION PHASE:");
      if(!whosTurn.workingDeckType(3)){//checking for any action cards      
         System.out.println("\t..but there are no action cards in your hand.");
      }
      else{
         //start of action loop
         String actionChoice = "";
         do{
            //need to cycle through workingDeck to make sure actually have that card
               do{
                  System.out.println("Number of actions: " + whosTurn.numActions() + " What action will you play? ");
                  whosTurn.printWorkingDeck();
                  actionChoice = input.next();
               }while(whosTurn.cardMatch(actionChoice) == -1);
            
            if(actionChoice.equals("Adventurer") || actionChoice.equals("Bureaucrat") || actionChoice.equals("Cellar") || actionChoice.equals("Chapel") || actionChoice.equals("CouncilRoom")){
               actionMethod(actionChoice, whosTurn, turnCounter, checkSupply, turnOrder);
            }
            else if(actionChoice.equals("Remodel") || actionChoice.equals("Smithy") || actionChoice.equals("Spy") || actionChoice.equals("Thief")){
               actionMethod(actionChoice, whosTurn, turnCounter, checkSupply, turnOrder);   
            }
            else if(actionChoice.equals("Laboratory") || actionChoice.equals("Library") || actionChoice.equals("Mine") || actionChoice.equals("Moat")){
               actionMethod(actionChoice, whosTurn, turnCounter, checkSupply, turnOrder);
            }
            else if(actionChoice.equals("Village") || actionChoice.equals("Witch") || actionChoice.equals("Workshop") || actionChoice.equals("Feast")){
               actionMethod(actionChoice, whosTurn, turnCounter, checkSupply, turnOrder);
            }
            else if(actionChoice.equals("Moneylender") || actionChoice.equals("Woodcutter") || actionChoice.equals("ThroneRoom")){
               holdoverCoins += actionMethod(actionChoice, whosTurn, turnCounter, checkSupply, turnOrder);
            }
            else if(actionChoice.equals("Chancellor") || actionChoice.equals("Festival") || actionChoice.equals("Market") || actionChoice.equals("Militia")){
               holdoverCoins += actionMethod(actionChoice, whosTurn, turnCounter, checkSupply, turnOrder);
            }
            else{
               System.out.print("That's not how that's spelled..");
            }          
         }while((whosTurn.numActions() > 0) && (whosTurn.workingDeckType(3)));         
      }//else --> have action card in first place
   
   //BUY
      System.out.println();
      System.out.println(whosTurn.playerName() + " BUY PHASE");      
      
      whosTurn.printWorkingDeck();
      int buyTemp = whosTurn.buyingPower() + holdoverCoins;
      if(holdoverCoins > 0){System.out.println("\n\tYou have " + holdoverCoins + " additional gold coins to purchase with.");}
      
      do{
         System.out.println("Your total buying power is: " + buyTemp + " and you have " + whosTurn.numBuys() + " buy(s).");      
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
            
               buyTemp -= costBoughtCard;//have to subtract that amount from buyTemp               
            
               whosTurn.addDiscard(checkSupply.returnDeck(checkSupply.matchName(buyChoice)).dealCard());
               whosTurn.decBuy();              
            }
            else{
               System.out.println("That card is too expensive, try again.");    
            }
         }//buyAnswer == y     
         else if(buyAnswer.equals("n")){//no buys
            whosTurn.setB(0);
         }
         else{
            System.out.println("\t..just checking if that was the correct input..");    
         }
      }while((whosTurn.numBuys() > 0) && (buyTemp > 0));         
   
   }//playerTurn
   
   public static int actionMethod(String actionCard, PlayerDeck whosTurn, int turnCounter, Supply checkSupply, ArrayList<PlayerDeck> turnOrder){
      int j = 0, holdoverCoins = 0;
      Scanner input = new Scanner (System.in);
      whosTurn.decAction();
      
      if(!actionCard.equals("Feast")){whosTurn.cardPlayed(whosTurn.cardMatch(actionCard));}

      if(actionCard.equals("Adventurer")){
         System.out.println("ADVENTURER: \nDraw until you have 2 additional TREASURE cards in hand.");
         System.out.println("\tall other cards will be discarded.");
      
         int adventure = 0;
         do{
            whosTurn.dealCard();
            whosTurn.printWorkDeckLast();
            if(whosTurn.workDeckLastCard(1)){//dealt card to workingDeck, checking if its treasure card
               adventure++;
               System.out.print("\tThe last card dealt is a treasure card and has been added to your hand.");
            }
            else{
               System.out.println("\tThis card will be discarded."); 
               whosTurn.work2Discard();//last card to discard
            }                     
         }while(adventure < 2);  
      }
      else if(actionCard.equals("Bureaucrat")){
         System.out.println("BUREAUCRAT: \nYou have gained a Silver Card on the top of your deck.");
         System.out.println("\tAll other players must reveal a Victory card in their hand(which will then go on top of their deck)..");
         System.out.println("\t..or a hand with no victory cards.\n");
            
         whosTurn.addToWorkDeck(checkSupply.returnDeck(checkSupply.matchName("Silver")).dealCard());
         whosTurn.work2Deck();//work2Deck takes last card from working deck and puts on top of deck

         int i = turnCounter + 1;               
         for(j = 0; j < turnOrder.size() - 1; j++){
            if(i >= turnOrder.size()){i = 0;} 
                  
            PlayerDeck attackVictim = turnOrder.get(i);      
            if(attackVictim.attackProtection()){
               System.out.println("\t" + attackVictim.playerName() + ", you are protected from the Bureaucrat attack card");
            }//has protection aka Moat card
            else{
               System.out.println("\t" + attackVictim.playerName() + ", you are not protected from the Bureaucrat attack card");
                     
               if(attackVictim.workingDeckType(2)){//have victory card(s) in hand
                  boolean burInput = false;
                  int burChoice = -1;                        
                  do{
                     attackVictim.printWorkingDeck(2);
                     try{
                        System.out.print("WHICH Victory Card will you choose? Your current hand is above: ");
                        burChoice = input.nextInt();
                        burInput = true;
                     }
                     catch (InputMismatchException ex){
                        System.out.println("You need to enter the index NUMBER of the card you wish to choose..");
                        input.nextLine();
                     }
                  }while(!burInput);
                              
                  Card burTemp = attackVictim.dealCard(burChoice);
                  attackVictim.addToWorkDeck(burTemp);
                  attackVictim.work2Deck();//can use work2Deck, card needs to be last card in working Deck
               }
               else{
                  System.out.println("\t..there are no victory cards in your hand.");
               }//no victory cards   
            }//else no protect
            turnOrder.set(i, attackVictim);
            System.out.println();
            i++;
         }//for               
         System.out.println("BUREAUCRAT ATTACK has been resolved.\n");
      }
      else if(actionCard.equals("Cellar")){
         System.out.println("CELLAR: \nyou have gained one additional action (if feasible)");
         whosTurn.incAction();//add 1 action
                  
         int temp = 0;
         System.out.print("Would you like to discard your victory cards? You are dealt that number of new cards from your deck. (y/n) ");
         String cellarAnswer = input.next();
         if(cellarAnswer.equals("y")){
            temp = whosTurn.workDiscard(2);//moving
            System.out.println("You will get " + temp + " extra card(s).");
         }
                
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
            else if(cellarAnswer.equals("n")){}
            else{
               System.out.println("\tYOU MUST ENTER y OR n");
               cellarAnswer = "y";
            }
         
         }while(cellarAnswer.equals("y"));
                     
         if(temp > 0){
            System.out.println("You will now be dealt " + temp + " extra card(s).");
            for(int i = 1; i <= temp; i++){whosTurn.dealCard();}      
         }  
      }
      else if(actionCard.equals("Chancellor")){
         System.out.println("CHANCELLOR: \nYou have gained 2 extra gold coins and you may immediately put your remaining deck into the discard pile.");
      
         System.out.print("\tWould you like to discard your remaining deck? (y/n)");
         String chancellorChoicez = input.next();
                  
         if(chancellorChoicez.equals("y")){
            whosTurn.deckToDiscard();
            System.out.println("Your deck has been moved to the discard pile.");                  
         }//need to discard deck
                  
         holdoverCoins = 2; //was +=
      }
      else if(actionCard.equals("Chapel")){
         System.out.println("CHAPEL: \nTrash up to 4 cards from your hand.");
               
         System.out.print("\tWould you like to trash any cards? ");
         String chapelChoicez = input.next();
               
         if(chapelChoicez.equals("y")){
            int chapelCount = 0;
            int chapelChoice = 0;
            Card chapelTemp = new Card();                                                      
                  
            do{
               whosTurn.printWorkingDeck();
               System.out.print("Which card will you trash? Your current hand is above (enter the index number): ");
               chapelChoice = input.nextInt();//need to add error checking               
               chapelTemp = whosTurn.dealCard(chapelChoice);//removed from working deck when method called
               checkSupply.addTrash(chapelTemp);//card actually added to trash
               chapelCount++;
               System.out.print("Do you want to trash any more cards (y/n)? ");
               chapelChoicez = input.next();
                  
            }while((chapelCount < 5) && (chapelChoicez.equals("y")));
               
         }//if trashing cards   

      }
      else if(actionCard.equals("CouncilRoom")){
         System.out.println("COUNCIL ROOM: \nYou gained 4 additional cards and 1 additional buy (if feasible).");
               System.out.println("\tEvery other players gets to draw one card as well.");
               
               for(int i = 1; i <= 4; i++){whosTurn.dealCard();}
               whosTurn.incBuy();
                  
               int i = turnCounter + 1;               
               for(j = 0; j < turnOrder.size() - 1; j++){
                  if(i >= turnOrder.size()){i = 0;}    
                  turnOrder.get(i).dealCard();
                  i++;   
               }
      }
      else if(actionCard.equals("Feast")){
               System.out.println("FEAST: \nTrash this card from you hand, gain a card costing up to 5 coins");
               checkSupply.addTrash(whosTurn.dealCard(whosTurn.cardMatch(actionCard)));
               
               checkSupply.canBeBought(5);
               System.out.println("What card will you take? ");
               String feastChoicez = input.next();
               whosTurn.addDiscard(checkSupply.returnDeck(checkSupply.matchName(feastChoicez)).dealCard());            
      }
      else if(actionCard.equals("Festival")){
         
               System.out.println("FESTIVAL: \nYou gained 2 additional actions, 1 additional buy (if feasible,)");
               System.out.println("\tin addition to 2 extra gold coins");
               
               for(int i = 1; i <= 2; i++){whosTurn.incAction();}
               whosTurn.incBuy();
               holdoverCoins = 2; //was +=
      }      
      else if(actionCard.equals("Laboratory")){                     
               System.out.println("LABORATORY: \n2 cards have been added to your hand and you gained one more Action(if feasible)");
               
               for(int i = 1; i < 3; i++){whosTurn.dealCard();}
               whosTurn.incAction();              
      }      
      else if(actionCard.equals("Library")){
               System.out.println("LIBRARY: \nDraw until you have 7 cards in hand.");
               System.out.println("\tyou may discard any action cards drawn this way");
             
               do{
                  whosTurn.dealCard();
                  whosTurn.printWorkDeckLast();
                  
                  if(whosTurn.workDeckLastCard(3)){//dealt card to workingDeck, checking if its action card
                     System.out.print("The last card dealt is an action card: do you want to keep it in your hand? (y/n) ");
                     String libraryChoice = input.next();
                     if(libraryChoice.equals("n")){
                        System.out.println("That card will be discarded."); 
                        whosTurn.work2Discard();//last card to discard
                     }
                  }
                  else{
                     System.out.println("This card has been added to your hand."); 
                  } 
               }while(whosTurn.workDeckSize() < 7);  
      }      
      else if(actionCard.equals("Market")){
               System.out.println("MARKET: \n1 card has been added to your hand, you gained one additional action (if feasible),");
               System.out.println("\t1 additional buy (if feasible), in addition to 1 extra gold coin");
            
               whosTurn.dealCard();
               whosTurn.incAction();
               whosTurn.incBuy();
               holdoverCoins = 1; //was ++
      }            
      else if(actionCard.equals("Militia")){
               System.out.println("MILITIA: \nyou gained 2 additional coins to buy with");
               System.out.println("\tother players will have to discard down to 3 cards NOW if they don't have protection\n");
            
               holdoverCoins = 2;               //was +=

               int i = turnCounter + 1;               
               for(j = 0; j < turnOrder.size() - 1; j++){
                  if(i >= turnOrder.size()){i = 0;} 

                  PlayerDeck attackVictim = turnOrder.get(i);      
                                    
                  if(attackVictim.attackProtection()){
                     System.out.println("\t" + attackVictim.playerName() + ", you are protected from the previous Militia attack card");
                  }//has protection
                  else{
                     System.out.println("\t" + attackVictim.playerName() + ", you are not protected from the previous Militia attack card");
                           //only do if whosTurn has at least 4 cards
                     for(int x = 1; x < 3; x++){
                        if(attackVictim.workDeckSize() > 3){
                           boolean correctInput = false;
                           int attackChoice = -1;                        
                           do{
                              attackVictim.printWorkingDeck();
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
                                 
                           attackVictim.addDiscard(attackVictim.dealCard(attackChoice));                             
                        }
                        else{
                           System.out.println(".....but you're already down to 3 cards in your hand.");
                           x = 2;
                        }
                     }//for loop w x going through twice
                  }//doesn't have protection
                  turnOrder.set(i, attackVictim);
                  System.out.println();
                  i++;
               }//for loop going through turnOrder
               System.out.println("MILITIA ATTACK is now resolved.\n");
      
      }
      else if(actionCard.equals("Mine")){
      
               System.out.println("MINE: \nTrash a Treasure card from you hand, gain a treasure card in your hand costing up to 3 more coins than the trashed card");
               //check that there are treasure cards
               if(whosTurn.workingDeckType(1)){
                  whosTurn.printWorkingDeck(1);
                  System.out.print("Which card will you trash? Your current hand is above: ");
                  int mineChoice = input.nextInt();               
                  //trashing one card, still on honor system
                  Card mineTemp = whosTurn.dealCard(mineChoice);//removed from working deck when method called
                  mineChoice = mineTemp.getCost(); //cost of trashed card
                  checkSupply.addTrash(mineTemp);//card actually added to trash
                  checkSupply.canBeBought2(mineChoice + 3, 1);
                  System.out.print("What card will you take? ");
                  String mineChoicez = input.next();
                  
                  whosTurn.addToWorkDeck(checkSupply.returnDeck(checkSupply.matchName(mineChoicez)).dealCard());
               }
               else{
                  System.out.println("There are no treasure cards in your hand to trash");
               }
      }
      else if(actionCard.equals("Moat")){
         System.out.println("MOAT: \n2 cards have been added to your hand");
                  //add 2 cards to working Deck            
         for(int i = 1; i < 3; i++){whosTurn.dealCard();}              
      }
      else if(actionCard.equals("Moneylender")){
         System.out.println("MONEYLENDER: \nTrash a copper from your hand, if you do you gain 3 gold coins");
               
               if(whosTurn.workingDeckType("Copper")){
                  whosTurn.printWorkingDeck(1);
                  System.out.print("Which card will you trash? Your current hand is above: ");
                  int moneylenderChoice = input.nextInt();               
                  Card moneylenderTemp = whosTurn.dealCard(moneylenderChoice);//removed from working deck when method called
                  checkSupply.addTrash(moneylenderTemp);//card actually added to trash
                  holdoverCoins = 3; //was +=                                 
               }
               else{
                  System.out.println("You don't have any coppers to trash.");
               }               

      }
      else if(actionCard.equals("Remodel")){
         System.out.println("REMODEL: \nTrash a card from you hand, gain a card costing up to 2 more coins than trashed card");
               //add card to trash, gain additional card            
               whosTurn.printWorkingDeck();
               System.out.print("Which card will you trash? Your current hand is above: ");
               int remodelChoice = input.nextInt();               
               //trashing one card
               Card remodelTemp = whosTurn.dealCard(remodelChoice);//removed from working deck when method called
               remodelChoice = remodelTemp.getCost(); //cost of trashed card
               checkSupply.addTrash(remodelTemp);//card actually added to trash
            
               checkSupply.canBeBought(remodelChoice + 2);
               System.out.println("What card will you take? ");
               String remodelChoicez = input.next();
                
               whosTurn.addDiscard(checkSupply.returnDeck(checkSupply.matchName(remodelChoicez)).dealCard());            
      }
      else if(actionCard.equals("Smithy")){
         System.out.println("SMITHY: \n3 cards have been added to your hand");
               //add 3 cards to working Deck            
         for(int i = 1; i < 4; i++){whosTurn.dealCard();}              
      }      
      else if(actionCard.equals("Spy")){
         System.out.println("SPY: \n1 card has been added to your hand and you gained one more Action(if feasible)");
               System.out.println("\tALL players(including the one who played the Spy) will show the top card of their deck and " + whosTurn.playerName());
               System.out.println("\twill decide if they return the card to their deck or put it in the discard pile.\n");
                              
               whosTurn.dealCard();
               whosTurn.incAction();
               
         int i = turnCounter;               
         for(j = 0; j < turnOrder.size(); j++){
            if(i >= turnOrder.size()){i = 0;}
             
                  PlayerDeck attackVictim = turnOrder.get(i);      
               
                  if(attackVictim.attackProtection()){
                     System.out.println("\t" + attackVictim.playerName() + ", you are protected from the previous Spy attack card");
                  }//has protection
                  else{
                     System.out.println("\t" + attackVictim.playerName() + ", you are not protected from the previous Spy attack card");
                  
                     attackVictim.dealCard();//pulls top card of deck
                     attackVictim.printWorkDeckLast();
                     //discards or puts it back, player who played spy's choice                     
                     System.out.print("This is the top card of " + attackVictim.playerName() + "'s deck, do they DISCARD it or PUT it back? (d/p) ");
                     String spyChoice = input.next();
                     if(spyChoice.equals("d")){
                        System.out.println("That card will be discarded."); 
                        attackVictim.work2Discard();//last card to discard
                     }
                     else{//put back
                        System.out.println("That card will be put back on top of the deck."); 
                        attackVictim.work2Deck();//last card back to deck
                     }
                  }//didn't have protection                                       
                  turnOrder.set(i, attackVictim);   
                  System.out.println();
                  i++;
               }//for                
               System.out.println("SPY ATTACK is now resolved.\n");
      }
      else if(actionCard.equals("Thief")){
      
         System.out.println("THIEF: \nEach player reveals the top 2 cards of their deck:");
         System.out.println("\tif any Treasure cards are revealed, " + whosTurn.playerName() + " may trash one of these cards.");
         System.out.println("\tThe other revealed cards are discarded. " + whosTurn.playerName() + " may gain any/all of trashed Treasure cards.\n");
                                             
         int i = turnCounter + 1;               
         for(j = 0; j < turnOrder.size() - 1; j++){
            if(i >= turnOrder.size()){i = 0;} 
               
            PlayerDeck attackVictim = turnOrder.get(i);                       
                  
            if(attackVictim.attackProtection()){
                     System.out.println("\t" + attackVictim.playerName() + ", you are protected from the previous Thief attack card");
            }//has protection
            else{
               System.out.println("\t" + attackVictim.playerName() + ", you are not protected from the previous Thief attack card");
                     //pulls top card of deck
                     //if Treasure can trash one card, player who played Thief's choice
                     System.out.println("These are the top 2 cards of " + attackVictim.playerName() + "'s deck,");
                     attackVictim.dealCard();
                     attackVictim.dealCard();
                     
                     if(attackVictim.printWorkingDeckLast2()){
                        System.out.println(whosTurn.playerName() + " can trash/gain one Treasure card." );
                        String thiefChoicez = "";
                        System.out.print("WILL YOU TRASH A TREASURE CARD? The options are above: (y/n) ");
                        thiefChoicez = input.next();
                        
                        if(thiefChoicez.equals("y")){
                           boolean correctInput = false;
                           int thiefChoice = -1;                        
                           do{
                              try{
                                 System.out.print("Which CARD will u trash? ");
                                 thiefChoice = input.nextInt();
                                 correctInput = true;  
                              }
                              catch (InputMismatchException ex){
                                 System.out.println("You need to enter the index NUMBER of the card you wish to trash..");
                                 input.nextLine();
                              }
                           }while(!correctInput);
                                
                           Card temp = attackVictim.dealCard(thiefChoice);
                           System.out.print(whosTurn.playerName() + " do you want to keep this card? (y/n)");
                           thiefChoicez = input.next();
                           if(thiefChoicez.equals("y")){
                              whosTurn.addDiscard(temp);                                
                           }
                           else{
                              checkSupply.addTrash(temp);
                           }
                            
                           attackVictim.addDiscard(attackVictim.dealCard(attackVictim.workDeckSize()-1));                                
                        }//will trash one treasure                        
                        
                     }//treasure card is available
                     else{
                        attackVictim.addDiscard(attackVictim.dealCard(attackVictim.workDeckSize()-1));                                
                        attackVictim.addDiscard(attackVictim.dealCard(attackVictim.workDeckSize()-1));                                
                     }                     
                  }//else didn't have protection                                       
               
                  turnOrder.set(i, attackVictim);
                  System.out.println();
                  i++;
               }//for                
            
               System.out.println("\tTHIEF ATTACK is now resolved.\n");
      }
      else if(actionCard.equals("ThroneRoom")){
               System.out.println("THRONE ROOM: \nChoose an action card in your hand: play it twice.\n");

               if(whosTurn.workingDeckType(3)){
                  whosTurn.printWorkingDeck();
                  System.out.print("What action card will you play twice (enter name): ");
                  actionCard = input.next();
                  //can do input error checking here
                  /*            do{
                  System.out.println("What card will you BUY? ");
                  buyChoice = input.next();
                  }while(checkSupply.matchName(buyChoice) == -1); */

                  System.out.println("Playing action first time: \n");
                  holdoverCoins += actionMethod(actionCard, whosTurn, turnCounter, checkSupply, turnOrder);
                  
                  if(!actionCard.equals("Feast")){whosTurn.throneRoom();}
                  else{whosTurn.addToWorkDeck(checkSupply.getFromTrash());}//if Feast have to pull card from Trash
      
                  whosTurn.incAction();

                  System.out.println("Playing action second time: \n");
                  holdoverCoins += actionMethod(actionCard, whosTurn, turnCounter, checkSupply, turnOrder);
                  whosTurn.incAction();
                  System.out.println("Throne Room resolved.");                  
               }            
               else{
                  System.out.println("\t..But you dont't have any more action cards.");
               }
      }
      else if(actionCard.equals("Woodcutter")){
         System.out.println("WOODCUTTER: \nYou gained one additional buy (if feasible), and 2 extra gold coins.");      
         whosTurn.incBuy();
         holdoverCoins = 2; //was +=
      }
      else if(actionCard.equals("Workshop")){
         System.out.println("WORKSHOP: \nYou gain any of the following cards costing up to 4 coins.");
               //add new card to discard pile
         checkSupply.canBeBought(4);
         String workShopChoice;               
         do{
            System.out.print("What card will you take? ");
            workShopChoice = input.next();               
         }while(workShopChoice.equals("y"));//better error checking
            
         whosTurn.addDiscard(checkSupply.returnDeck(checkSupply.matchName(workShopChoice)).dealCard());      
      }
      else if(actionCard.equals("Village")){   
         System.out.println("VILLAGE: \n1 card has been added to your hand and you gained two more Actions(if feasible)");
                  //add 1 card to working Deck, +2 actions            
         whosTurn.dealCard();
         for(int i = 1; i < 3; i++){whosTurn.incAction();}                             
      }
      else{//Witch
         System.out.println("WITCH: \n2 cards have been added to your hand and ");
         System.out.println("\tother players will gain a curse card NOW if they don't have protection.\n");
            
         for(int i = 1; i < 3; i++){whosTurn.dealCard();}              
            
         int i = turnCounter + 1;               
         for(j = 0; j < turnOrder.size() - 1; j++){
            if(i >= turnOrder.size()){i = 0;} 
            
            PlayerDeck attackVictim = turnOrder.get(i);
                                 
            if(attackVictim.attackProtection()){
                     System.out.println("\t" + attackVictim.playerName() + ", you are protected from the previous Witch attack card");
            }//has protection
            else{
                     System.out.println("\t" + attackVictim.playerName() + ", you are not protected from the previous Witch attack card");
                     Card temp = checkSupply.returnDeck(checkSupply.matchName("Curse")).dealCard(); 
                     if(temp.getName().equals("Curse")){attackVictim.addDiscard(temp);}
                     else{
                        System.out.println("\t but luckily for you there are no more curse cards.");
                     }
                  
            }//else does not have protection   
            System.out.println();
            turnOrder.set(i, attackVictim);               
            i++;
         }//for
      System.out.println("\tWITCH ATTACK is now resolved.\n");
      }
      
      return holdoverCoins;         
   }//end of actionMethod
   
}