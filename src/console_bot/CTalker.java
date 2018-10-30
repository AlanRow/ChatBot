package console_bot;

import bot_interfaces.Algorithm;
import bot_interfaces.MessageController;
import structures.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CTalker implements MessageController
{
	private Algorithm mainAlg;
	private UserInfo user;
	
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

	public List<String> getNewMessages() 
	{
		Scanner input = new Scanner(System.in);
		List<String> messages = new ArrayList<String>();
		messages.add(input.nextLine());
		return messages;
	}

	public Algorithm getAlgorithm()
	{
		return mainAlg;
	}

	public UserInfo getUser() {
		return user;
	}
	
}
