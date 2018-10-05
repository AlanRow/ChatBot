package console_bot;

import bot_interfaces.Algorithm;
import bot_interfaces.MessageController;
import java.util.Scanner;

public class CTalker implements MessageController
{
	private Algorithm mainAlg;
	
	public CTalker(Algorithm alg)
	{
		mainAlg = alg;
	}
	
	public void send(String message) 
	{
		System.out.println(message);
	}

	public boolean areNewMessages() 
	{
		return true;
	}

	public String[] getNewMessages() 
	{
		Scanner input = new Scanner(System.in);
		return new String[] {input.nextLine()};
	}

	public Algorithm getAlgorithm()
	{
		return mainAlg;
	}
	
}
