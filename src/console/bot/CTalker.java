package console.bot;

import structures.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import bot.interfaces.Algorithm;
import bot.interfaces.MessageController;

public class CTalker implements MessageController
{
	private Algorithm mainAlg;
	private UserInfo user;
	
	public CTalker(Algorithm alg, UserInfo user)
	{
		mainAlg = alg;
		this.user = user;
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

	@Override
	public MessageController genererateSame(UserInfo another) {
		return new CTalker(mainAlg.genererateSame(another), another);
	}
	
}
