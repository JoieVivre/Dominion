//C. Atkinson
import java.util.*;

public class DominionGameCenter extends Dominion
{
   public static void main(String [] args)
   {
   Scanner input = new Scanner (System.in);
   
   int numPlayers = 0;
   ArrayList<String> playerNames = new ArrayList<String>();

   do{
      System.out.print("Hello there, friend! How many players? (2, 3, or 4) ");
      numPlayers = input.nextInt();
   }while((numPlayers < 2) || (numPlayers > 4));
   
   
   for(int a = 1; a <= numPlayers; a++){
      System.out.print("Player " + a + ": what's your name? ");
      playerNames.add(input.next());
      
   }
      System.out.println();
      
      
      System.out.println("\tWhich KINGDOM decks do you want?");
      System.out.println("\tAvailable KINGDOM choices -->");
      //H-cHapel 
      //N-ChaNcellor
      //B-Bureaucrat
      //G-Gardens
      //E-Moneylender
      //P-SPy
      //T-Thief
      //D-throne room
            //U-coUncil Room
      //L-Laboratory            
      //Z-witch
      //A-Adventurer
                        
      System.out.println("Cost 2 Coins:\n\tC-Cellar \n\tM-Moat");
      System.out.println("Cost 3 Coins:\n\tV-Village \n\tW-Woodcutter \n\tO-wOrkshop");
      System.out.println("Cost 4 Coins:\n\tF-Feast \n\tI-mIlitia \n\tR-Remodel \n\tS-Smithy");      
      System.out.println("Cost 5 Coins:\n\tQ-festival \n\tX-library \n\tK-marKet \n\tY-mine");      
      
      System.out.println("Enter the capital letter associated with the KINGDOM pile followed by the next capital letter:");
      System.out.println("FOR EXAMPLE: first game would be - CKIYMRSVWO");

      String kingdomChoices = input.next();
      
      
      play(numPlayers, playerNames, kingdomChoices);
   
   //Dominion game = new Dominion(numPlayers, p1name, p2name);
   }//end of main
   
}