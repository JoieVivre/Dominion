//C. Atkinson
import java.util.*;

public class DominionGameCenter extends Dominion
{
   public static void main(String [] args)
   {
   Scanner input = new Scanner (System.in);
   
   System.out.println("Hello there, friend! How many players?");
   int numPlayers = input.nextInt();
   
   int count = 1;
   String p1name = "";
   String p2name = "";
   
      System.out.print("Player " + count + ": what's your name? ");
      p1name = input.next();
      count++;
      System.out.println();
      System.out.print("Player " + count + ": what's your name? ");
      p2name = input.next(); 
      play(p1name, p2name);
   
   //Dominion game = new Dominion(numPlayers, p1name, p2name);
   }//end of main
   
}