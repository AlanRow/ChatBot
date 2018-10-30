package telegram_bot;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import bot_interfaces.Algorithm;
import bot_interfaces.MessageController;

public class TelegramTalker implements MessageController {
	
	private String userId;
	private Algorithm alg;
	private Queue<String[]> forSend;
	private Queue<String> forRead;
	
	
	public TelegramTalker(String user, Algorithm algorithm, Queue<String[]> forSending, Queue<String> forReading)
	{
		userId = user;
		alg = algorithm;
		forSend = forSending;
		forRead = forReading;
	}
	
	@Override
	public void send(String message) {
		forSend.add(new String[] {userId, message});
	}

	@Override
	public boolean areNewMessages() {
		return !forRead.isEmpty();
	}

	@Override
	public String[] getNewMessages() {
		List<String> newMes = new ArrayList<String>();
		
		while (!forRead.isEmpty())
			newMes.add(forRead.poll());
		
		return (String[]) newMes.toArray();
	}

	@Override
	public Algorithm getAlgorithm() {
		return alg;
	}

}
