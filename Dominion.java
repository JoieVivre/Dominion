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
               playerTurn(whosTurn, thisGame, turnOrder);//could pass i as well

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
         System.out.println(temp + " VICTORY POINTS");
      }      
      System.out.println(winnerName + " you're the game winner with " + winner + " points. \nCONGRATS");
      //System.out.println(player1.VictoryPoints() > player2.VictoryPoints() ? );
   }//end of play
     
   public static void playerTurn(PlayerDeck whosTurn, Supply checkSupply, ArrayList<PlayerDeck> turnOrder){//pass player name or number maybe
      Scanner input = new Scanner (System.in);
      int holdoverCoins = 0;
      boolean throneOn = false;
      int throneInt = 0;
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
            if(!throneOn){
               actionChoice = "";
               do{
                  System.out.println("Number of actions: " + whosTurn.numActions() + " What action will you play? ");
                  whosTurn.printWorkingDeck();
                  actionChoice = input.next();
               }while(whosTurn.cardMatch(actionChoice) == -1);
            }
            else{
               throneInt++;
            }

               if(actionChoice.equals("Adventurer")){
                  whosTurn.decAction();
                  
                  if(throneOn){
                     System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                     if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
                  }
                  else{
                     whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
                  }
                  
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
               else if(actionChoice.equals("Bureaucrat")){
                  whosTurn.decAction();
               
                 if(throneOn){
                     System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                     if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
                  }
                  else{
                     whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
                  }
               System.out.println("BUREAUCRAT: \nYou have gained a Silver Card on the top of your deck.");
               System.out.println("\tAll other players must reveal a Victory card in their hand(which will then go on top of their deck)..");
               System.out.println("\t..or a hand with no victory cards.");

               whosTurn.addToWorkDeck(checkSupply.returnDeck(checkSupply.matchName("Silver")).dealCard());
               whosTurn.work2Deck();//work2Deck takes last card from working deck and puts on top of deck
               
               for(int i = 0; i < turnOrder.size(); i++){
               //have to do i++ and turn over if its at the end
                  if(turnOrder.get(i).playerName().equals(whosTurn.playerName())){
                     i++;
                     if(i == turnOrder.size()){break;}
                  }
                  
                  PlayerDeck attackVictim = turnOrder.get(i);      
                  if(attackVictim.attackProtection()){
                       System.out.println("\t" + attackVictim.playerName() + ", you are protected from the previous Bureaucrat attack card");
                  }//has protection aka Moat card
                  else{
                     System.out.println("\t" + attackVictim.playerName() + ", you are not protected from the previous Bureaucrat attack card");
                     
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
               }//for               
            System.out.println("BUREAUCRAT ATTACK has been resolved.\n");
            }
            else if(actionChoice.equals("Cellar")){
               whosTurn.decAction();
                if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
   
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
            else if(actionChoice.equals("Chancellor")){
               whosTurn.decAction();
              
                if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               System.out.println("CHANCELLOR: \nYou have gained 2 extra gold coins and you may immediately put your remaining deck into the discard pile.");

               System.out.print("\tWould you like to discard your remaining deck? ");
               String chancellorChoicez = input.next();
               
               if(chancellorChoicez.equals("y")){
                  whosTurn.deckToDiscard();
                  System.out.println("Your deck has been moved to the discard pile.");                  
               }//need to discard deck
               
               holdoverCoins += 2;
            }
            else if(actionChoice.equals("Chapel")){
               whosTurn.decAction();
              
                if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               System.out.println("CHAPEL: \nTrash up to 4 cards from your hand.");

               System.out.print("\tWould you like to trash any cards? ");
               String chapelChoicez = input.next();
               
               if(chapelChoicez.equals("y")){
                  int chapelCount = 0;
                  int chapelChoice = 0;
                  Card chapelTemp = new Card();                                                      
                  
                  do{
                     whosTurn.printWorkingDeck();
                     System.out.print("Which card will you trash? Your current hand is above: ");
                     chapelChoice = input.nextInt();//need to add error checking               
                     chapelTemp = whosTurn.dealCard(chapelChoice);//removed from working deck when method called
                     checkSupply.addTrash(chapelTemp);//card actually added to trash
                     chapelCount++;
                     System.out.print("Do you want to trash any more cards? ");
                     chapelChoicez = input.next();
                  
                  }while((chapelCount < 5) && (chapelChoicez.equals("y")));
               
               }//if trashing cards   
            }
            else if(actionChoice.equals("CouncilRoom")){
               whosTurn.decAction();
               
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               
               System.out.println("COUNCIL ROOM: \nYou gained 4 additional cards and 1 additional buy (if feasible).");
               System.out.println("\tEvery other players gets to draw one card as well.");
               
               for(int i = 1; i <= 4; i++){whosTurn.dealCard();}
               whosTurn.incBuy();

               for(int i = 0; i < turnOrder.size(); i++){
                  if(turnOrder.get(i).playerName().equals(whosTurn.playerName())){
                     i++;
                     if(i == turnOrder.size()){break;}
                  }
                  turnOrder.get(i).dealCard();
               }
            }
            else if(actionChoice.equals("Feast")){
               whosTurn.decAction();
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){checkSupply.addTrash(whosTurn.dealCard(whosTurn.cardMatch(actionChoice)));}      
               }
               else{
                  checkSupply.addTrash(whosTurn.dealCard(whosTurn.cardMatch(actionChoice)));//card actually added to trash
               }
               
               System.out.println("FEAST: \nTrash this card from you hand, gain a card costing up to 5 coins");

               checkSupply.canBeBought(5);
               System.out.println("What card will you take? ");
               String feastChoicez = input.next();
               whosTurn.addDiscard(checkSupply.returnDeck(checkSupply.matchName(feastChoicez)).dealCard());            
            }
            else if(actionChoice.equals("Festival")){
               whosTurn.decAction();
                if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               System.out.println("FESTIVAL: \nYou gained 2 additional actions, 1 additional buy (if feasible,)");
               System.out.println("\tin addition to 2 extra gold coins");
               
               for(int i = 1; i <= 2; i++){whosTurn.incAction();}
               whosTurn.incBuy();
               holdoverCoins += 2;
            }
            else if(actionChoice.equals("Laboratory")){
               whosTurn.decAction();
               
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               
               System.out.println("LABORATORY: \n2 cards have been added to your hand and you gained one more Action(if feasible)");
               
               for(int i = 1; i < 3; i++){whosTurn.dealCard();}
               whosTurn.incAction();              
            }
            else if(actionChoice.equals("Library")){
               whosTurn.decAction();
              if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
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
            else if(actionChoice.equals("Market")){
               //move moat from working Deck to discard   
               whosTurn.decAction();
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               System.out.println("MARKET: \n1 card has been added to your hand, you gained one additional action (if feasible),");
               System.out.println("\t1 additional buy (if feasible), in addition to 1 extra gold coin");
            
               whosTurn.dealCard();
               whosTurn.incAction();
               whosTurn.incBuy();
               holdoverCoins++;
            }
            else if(actionChoice.equals("Militia")){ 
               whosTurn.decAction();
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               System.out.println("MILITIA: \nyou gained 2 additional coins to buy with");
               System.out.println("\tother players will have to discard down to 3 cards NOW if they don't have protection\n");

               holdoverCoins += 2;               //add 2 coins

               for(int i = 0; i < turnOrder.size(); i++){
                  if(turnOrder.get(i).playerName().equals(whosTurn.playerName())){
                     i++;
                     if(i == turnOrder.size()){break;}
                  }
                  PlayerDeck attackVictim = turnOrder.get(i);      
                                    
                  if(attackVictim.attackProtection()){
                       System.out.println("\t" + attackVictim.playerName() + ", you are protected from the previous Militia attack card");
                  }//has protection
                  else{
                     System.out.println("\t" + attackVictim.playerName() + ", you are not protected from the previous Militia attack card");
                           //only do if whosTurn has at least 4 cards
                           for(int j = 1; j < 3; j++){
                           
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
                                 j = 2;
                              }
                        }//for loop w j going through twice
                  }//doesn't have protection
               turnOrder.set(i, attackVictim);
               System.out.println();
               }//for loop going through turnOrder
            System.out.println("MILITIA ATTACK is now resolved.\n");
            }
            else if(actionChoice.equals("Mine")){
               whosTurn.decAction();
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
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
            else if(actionChoice.equals("Moat")){
               whosTurn.decAction();
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               System.out.println("MOAT: \n2 cards have been added to your hand");
               //add 2 cards to working Deck            
               for(int i = 1; i < 3; i++){whosTurn.dealCard();}              
            }    
            else if(actionChoice.equals("Moneylender")){
               whosTurn.decAction();
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               System.out.println("MONEYLENDER: \nTrash a copper from your hand, if you do you gain 3 gold coins");
               
               if(whosTurn.workingDeckType("Copper")){
                  whosTurn.printWorkingDeck(1);
                  System.out.print("Which card will you trash? Your current hand is above: ");
                  int moneylenderChoice = input.nextInt();               
                  Card moneylenderTemp = whosTurn.dealCard(moneylenderChoice);//removed from working deck when method called
                  checkSupply.addTrash(moneylenderTemp);//card actually added to trash
                  holdoverCoins += 3;                                 
               }
               else{
                  System.out.println("You don't have any coppers to trash.");
               }               
            }                        
            else if(actionChoice.equals("Remodel")){
               whosTurn.decAction();
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
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
            else if(actionChoice.equals("Smithy")){
               whosTurn.decAction();
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               System.out.println("SMITHY: \n3 cards have been added to your hand");
               //add 3 cards to working Deck            
               for(int i = 1; i < 4; i++){whosTurn.dealCard();}              
            }
            else if(actionChoice.equals("Spy")){
               whosTurn.decAction();
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               System.out.println("SPY: \n1 card has been added to your hand and you gained one more Action(if feasible)");
               System.out.println("\tALL players(including the one who played the Spy) will show the top card of their deck and " + whosTurn.playerName());
               System.out.println("\twill decide if they return the card to their deck or put it in the discard pile.\n");
                              
               whosTurn.dealCard();
               whosTurn.incAction();
               
               for(int i = 0; i < turnOrder.size(); i++){
               //not the correct order
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
               }//for                
            System.out.println("SPY ATTACK is now resolved.\n");
            }
            else if(actionChoice.equals("Thief")){
               whosTurn.decAction();
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               System.out.println("THIEF: \nEach player reveals the top 2 cards of their deck:");
               System.out.println("\tif any Treasure cards are revealed, " + whosTurn.playerName() + " may trash one of these cards.");
               System.out.println("\tThe other revealed cards are discarded. " + whosTurn.playerName() + " may gain any/all of trashed Treasure cards.\n");
                                             
               for(int i = 0; i < turnOrder.size(); i++){
               //not the correct order 
                  if(turnOrder.get(i).playerName().equals(whosTurn.playerName())){
                     i++;
                     if(i == turnOrder.size()){break;}
                  }
   
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
               }//for                
            
               System.out.println("\tTHIEF ATTACK is now resolved.\n");
            }
            else if(actionChoice.equals("ThroneRoom")){
                  whosTurn.decAction();
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
                  System.out.println("THRONE ROOM: \nChoose an action card in your hand: play it twice.\n");

                  whosTurn.printWorkingDeck();
                  if(whosTurn.workingDeckType(3)){
                     System.out.println("What action will you play twice: ");
                     actionChoice = input.next();
                  //can do input error checking here
                  
                     throneOn = true;
                     throneInt = 0;                  
                     whosTurn.incAction();
                     whosTurn.incAction();
                  }            
                  else{
                     System.out.println("You dont't have any more action cards.");
                  }
            }
            else if(actionChoice.equals("Village")){
               whosTurn.decAction();
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               System.out.println("VILLAGE: \n1 card has been added to your hand and you gained two more Actions(if feasible)");
               //add 1 card to working Deck, +2 actions            
               whosTurn.dealCard();
               for(int i = 1; i < 3; i++){whosTurn.incAction();}              
            }
            else if(actionChoice.equals("Witch")){
               whosTurn.decAction();
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               System.out.println("WITCH: \n2 cards have been added to your hand and ");
               System.out.println("\tother players will gain a curse card NOW if they don't have protection.\n");

               for(int i = 1; i < 3; i++){whosTurn.dealCard();}              

               for(int i = 0; i < turnOrder.size(); i++){
                  if(turnOrder.get(i).playerName().equals(whosTurn.playerName())){
                     i++;
                     if(i == turnOrder.size()){break;}
                  }
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
               }//for
               System.out.println("\tWITCH ATTACK is now resolved.\n");
            }            
            else if(actionChoice.equals("Woodcutter")){
               whosTurn.decAction();
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
               System.out.println("WOODCUTTER: \nYou gained one additional buy (if feasible), and 2 extra gold coins.");
            
               whosTurn.incBuy();
               holdoverCoins += 2;
            }
            else if(actionChoice.equals("Workshop")){
               whosTurn.decAction();
               if(throneOn){
                  System.out.println("You are Throne Room-ing this action card: " + throneInt + " time.");
                  if(throneInt >= 2){whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));}      
               }
               else{
                  whosTurn.cardPlayed(whosTurn.cardMatch(actionChoice));
               }
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
            else{
               System.out.print("That's not how that's spelled..");
            }
            
               if(throneInt >= 2){
                  throneOn = false;
                  throneInt = 0;
               }            
         }while((whosTurn.numActions() > 0) && (whosTurn.workingDeckType(3)));         
      }//else --> have action card in first place

   //BUY
      System.out.println();
      System.out.println(whosTurn.playerName() + " BUY PHASE");      
      
      whosTurn.printWorkingDeck();
      int buyTemp = whosTurn.buyingPower() + holdoverCoins;
      if(holdoverCoins > 0){System.out.println("\tYou have " + holdoverCoins + " additional gold coins to purchase with.");}
      
      do{
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
   
}   