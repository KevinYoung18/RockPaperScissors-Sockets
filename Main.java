import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Main
{
	final static int NUM_PLAYERS = 3;
	final static Integer INIT_PORT = 1040;//arbitrary initial port
	static Integer NUM_GAMES;
	
	
	//receives number of games to play as args[0]
	public static void main(String[] args) throws IOException, InterruptedException
	{
		try
		{
			NUM_GAMES = Integer.parseInt(args[0]);
		} 
		catch (Exception e) 
		{
			System.out.println("Enter number of games to play: ");
			Scanner kbScanner = new Scanner(System.in);
			NUM_GAMES = kbScanner.nextInt();
			kbScanner.close();
		}
		
		
		//ensure Player.java is compiled
		try {
			Process process = new ProcessBuilder("javac", "Player.java").start();
			process.waitFor(10000, TimeUnit.MILLISECONDS);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	
		//get deque of port numbers
		ArrayDeque<Integer> deque = new ArrayDeque<Integer>();
		for(int i = 0; i < NUM_PLAYERS; i++)
			deque.add(INIT_PORT + i);
		
		//create processes
		Process[] processes = new Process[NUM_PLAYERS];
		for(int i = 0; i < NUM_PLAYERS; i++)
		{
			ArrayList<String> argList = new ArrayList<String>();
			argList.add("java");
			argList.add("Player");
			
			for(int j = 0; j < NUM_PLAYERS; j++)
			{
				Integer port = deque.poll();
				argList.add(port.toString());
				deque.add(port);
				
			}
			argList.add(NUM_GAMES.toString());
			processes[i] = new ProcessBuilder(argList).inheritIO().start();
		
			//offset port deque
			deque.add(deque.poll());
		}
		
		//wait for processes to finish
		for(int i = 0; i < NUM_PLAYERS; i++)
		{
			processes[i].waitFor(10000, TimeUnit.MILLISECONDS);
		}
		
		
	}

}
