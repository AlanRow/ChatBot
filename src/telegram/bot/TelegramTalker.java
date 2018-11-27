package telegram.bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import bot.interfaces.Algorithm;
import bot.interfaces.MessageController;
import exceptions.IllegalReadingException;
import exceptions.IllegalSendingException;
import structures.UserInfo;

//класс для диалога с отдельным пользователем в телеграмме
public class TelegramTalker implements MessageController {
	
	//пользователь
	private UserInfo user;
	//алгоритм для пользователя
	private Algorithm alg;
	//котроллер бота, отвечающий за работу со всеми пользователями
	private MyAmazingBot bot;
	
	
	public TelegramTalker(UserInfo userInfo, Algorithm algorithm, MyAmazingBot tBot)
	{
		user = userInfo;
		alg = algorithm;
		bot = tBot;
	}
	
	//отправка сообщения пользователю
	public void send(String message) {
		try {
			bot.send(this, message);
		} catch (IllegalSendingException e) {
			System.out.println("Failed sending of the - <<" + message + ">> - message!");
		}
	}

	//проверка наличия новых сообщений
	public boolean areNewMessages() {
		return !bot.checkPostIsEmpty(this);
	}

	//получение новых сообщений
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

	//получение алгоритма
	public Algorithm getAlgorithm() {
		return alg;
	}

	//получение пользователя
	public UserInfo getUser() {
		return user;
	}

	//сгенерировать аналогичного толкера для другого пользователя
	public MessageController genererateSame(UserInfo another) {
		return new TelegramTalker(another, alg.genererateSame(another), bot);
	}

}
