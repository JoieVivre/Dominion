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
      play(numPlayers, playerNames);
   
   //Dominion game = new Dominion(numPlayers, p1name, p2name);
   }//end of main
   
}