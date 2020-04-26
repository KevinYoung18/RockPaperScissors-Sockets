import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;



public class Player 
{
	
	private final static String HOST = "127.0.0.1";
	static int playerNum;
	static int[] games;
	
	
	//args are your port, followed by opponent's port
	public static void main(String args[])
	{
		playerNum =  Integer.parseInt(args[0]) - Main.INIT_PORT;
		
		try 
		{
			//open up server socket
			ServerSocket server = new ServerSocket(Integer.parseInt(args[0]), 2);
			
			//open up client socket
			Socket opponent1 = new Socket(HOST, Integer.parseInt(args[1]));
			Socket opponent2 = new Socket(HOST, Integer.parseInt(args[2]));
			
			//connect to other players
			ObjectOutputStream opponent1Out = new ObjectOutputStream(opponent1.getOutputStream());
			ObjectOutputStream opponent2Out = new ObjectOutputStream(opponent2.getOutputStream());
			ObjectInputStream opponent1In = new ObjectInputStream(server.accept().getInputStream());
			ObjectInputStream opponent2In = new ObjectInputStream(server.accept().getInputStream());
			
			//play games
			games = playGames(Integer.parseInt(args[3]));
			
			//send games
			opponent1Out.writeObject(games);
			opponent2Out.writeObject(games);
			
			//receive games
			int[] opponent1Games = (int[]) opponent1In.readObject();
			int[] opponent2Games = (int[]) opponent2In.readObject();
		
			//ouput results
			int results = getTotalScore(opponent1Games, opponent2Games);
			System.out.println("Player " + playerNum + "Score: " + results);
			
			//close resources
			server.close();
			opponent1.close();
			opponent2.close();
			
		} 
		catch (ClassNotFoundException |NumberFormatException | IOException e) 
		{
			System.out.println("Player " + playerNum +" Error: " + e.getMessage());
			e.printStackTrace();
	
		} 
		
	}
	
	//choose rock, paper or scissors for all of the games
	//returns an int array representing all choices
	//0 = rock, 1 = paper, 2 = scissors
	private static int[] playGames(int numGames) 
	{
		Random rand = new Random();
		int[] games = new int[numGames];
		for(int i = 0; i < numGames; i++)
		{
			games[i] = rand.nextInt(3);
		}
		return games;
		
	}
	
	//returns the total score of all the rock paper scissors games
	private static int getTotalScore(int[] opponent1Games, int[] opponent2Games)
	{
		int score = 0;
		for(int i = 0; i < games.length; i++)
		{
			score += getScore(games[i], opponent1Games[i], opponent2Games[i]);
		}
		return score;
	}
	
	//return score of a single rock paper scissors game
	//Award player 2 points if it beats both the others; 
	//award two players 1 point each if they both beat the third; otherwise award no points
	private static int getScore(int thisGame, int game1, int game2)
	{
		if(didIWin(thisGame, game1))
		{
			//if player beats both players
			if(didIWin(thisGame, game2))
				return 2;
			
			//if two players beat the third
			if (didIWin(game2, game1))
				return 1;
		}
		else if(didIWin(thisGame, game2))
		{
			//if two players beat the third
			if(didIWin(game1, game2))
				return 1;
		}
		
		//else return 0
		return 0;
	}
	
	// returns true if me beats them in rock paper scissors
	//0 = rock, 1 = paper, 2 = scissors
	private static boolean didIWin(int me, int them)
	{
		switch (me) 
		{
			case 0: 
				return them == 2;
			case 1:
				return them == 0;
			case 2:
				return them == 1;
			default:
				throw new IllegalArgumentException("Unexpected value: " + me);
		}
	}
}
