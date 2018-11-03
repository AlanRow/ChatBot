package telegram_bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import bot_interfaces.Algorithm;
import bot_interfaces.MessageController;
import exceptions.IllegalReadingException;
import exceptions.IllegalSendingException;
import structures.UserInfo;

public class TelegramTalker implements MessageController {
	
	private UserInfo user;
	private Algorithm alg;
	private MyAmazingBot bot;
	
	
	public TelegramTalker(UserInfo userInfo, Algorithm algorithm, MyAmazingBot tBot)
	{
		user = userInfo;
		alg = algorithm;
		bot = tBot;
	}
	
	public void send(String message) {
		try {
			bot.send(this, message);
		} catch (IllegalSendingException e) {
			System.out.println("Failed sending of the - <<" + message + ">> - message!");
		}
	}

	public boolean areNewMessages() {
		return !bot.checkPostIsEmpty(this);
	}

	public List<String> getNewMessages() {
		try {
			List<String> messages = bot.getPost(this);
			//System.out.println("талкер передал: " + messages);
			return messages;
		} catch (IllegalReadingException e) {
			send("Sorry, I cannot read your mesages!");
			return new ArrayList<String>();
		}
	}

	public Algorithm getAlgorithm() {
		return alg;
	}

	public UserInfo getUser() {
		return user;
	}

	public MessageController genererateSame(UserInfo another) {
		return new TelegramTalker(another, alg.genererateSame(another), bot);
	}

}
