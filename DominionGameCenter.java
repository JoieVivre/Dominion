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
      
      
      System.out.println("\tWhich 10 KINGDOM decks do you want?");
      System.out.println("\tAvailable KINGDOM choices -->");
      
      System.out.println("Cost 2 Coins:\n\tC-Cellar \n\tH-cHapel \n\tM-Moat");
      System.out.println("Cost 3 Coins:\n\tN-chaNcellor \n\tV-Village \n\tW-Woodcutter \n\tO-wOrkshop");
      System.out.println("Cost 4 Coins:\n\tB-Bureaucrat \n\tF-Feast \n\tG-Gardens \n\tI-mIlitia \n\tE-monEylender \n\tR-Remodel \n\tS-Smithy \n\tP-sPy \n\tT-Thief \n\tD-throne room");      
      System.out.println("Cost 5 Coins:\n\tU-coUncil Room \n\tQ-festival \n\tL-Laboratory \n\tX-library \n\tK-marKet \n\tY-mine \n\tZ-witch");      
      System.out.println("Cost 6 Coins:\n\tA-Adventurer");      

      String kingdomChoices;
      //auto games and shit
      
      /*
      First Game: CMVWOIRSKY 
      Big Money: HNBFDLKYEA
      Interaction:
      Size Distortion:
      Village Square:
      */
      do{
         System.out.println("Enter the capital letter associated with each KINGDOM pile, pick 10 capital letters:");
         System.out.println("FOR EXAMPLE: first game would be - CMVWOIRSKY");
   
         kingdomChoices = input.next();
      }while(kingdomChoices.length() != 10);
      
      play(numPlayers, playerNames, kingdomChoices);
   
   //Dominion game = new Dominion(numPlayers, p1name, p2name);
   }//end of main
   
}