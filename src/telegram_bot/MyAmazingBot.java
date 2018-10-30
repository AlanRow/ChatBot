package telegram_bot;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import bot_interfaces.UserControl;
import exceptions.IllegalReadingException;
import exceptions.IllegalSendingException;
import exceptions.ManyTelBotsException;
import structures.UserInfo;

public class MyAmazingBot extends TelegramLongPollingBot implements UserControl{
	
	private static boolean started = false;
	private List<UserInfo> users;
	private List<UserInfo> newUsers;
	private Map<UserInfo, List<String>> newMessages;
	
	private MyAmazingBot() {
		//System.out.println("Ok!");
		users = new ArrayList<UserInfo>();
		newUsers = new ArrayList<UserInfo>();
		newMessages = new HashMap<UserInfo, List<String>>();
	}
	
	public static MyAmazingBot getBot() throws ManyTelBotsException {
		if (started)
			throw new ManyTelBotsException();
	
		ApiContextInitializer.init();
		MyAmazingBot bot = new MyAmazingBot();
		TelegramBotsApi botsApi = new TelegramBotsApi();
	    try {
	        botsApi.registerBot(bot);
	    } catch (TelegramApiException e) {
	        e.printStackTrace();
	    }
	    
	    started = true;
	    return bot;
	}
	
	public void send(TelegramTalker talker, String text) throws IllegalSendingException {
		if (!users.contains(talker.getUser()))
			throw new IllegalSendingException();
		
		SendMessage message = new SendMessage()
        		.setChatId(talker.getUser().getId())
        		.setText(text);
		
		try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}
	
	public boolean checkPost(TelegramTalker talker){
		if (!users.contains(talker.getUser()))
			return false;
		return !newMessages.get(talker.getUser()).isEmpty();
	}
	
	public List<String> getPost(TelegramTalker talker) throws IllegalReadingException{
		if (!users.contains(talker.getUser()))
			throw new IllegalReadingException();
		//System.out.println("бот знал: " + newMessages.get(talker.getUser()));
		List<String> post = new ArrayList<String> (newMessages.get(talker.getUser())); 
		newMessages.get(talker.getUser()).clear();
		
		return post;
	}
	
	@Override
	public void onUpdateReceived(Update update) {

	    // We check if the update has a message and the message has text
	    if (update.hasMessage() && update.getMessage().hasText()) {
			System.out.println("бот прочитал: " + update.getMessage().getText());
	    	UserInfo user = new UserInfo(update.getMessage().getFrom());
	    	
	    	if (!newMessages.containsKey(user))
	    		newMessages.put(user, new ArrayList<String>());
	    		newMessages.get(user).add(update.getMessage().getText());
	    	
	    	if (!users.contains(user)) {
	    		users.add(user);
	    		newUsers.add(user);
	    	}
	    }
	}
	
    @Override
    public String getBotUsername() {
        return "JohhoBot";
    }

    @Override
    public String getBotToken() {
        return "650267550:AAHdCM9pFFKe8UKs8mhZ22a99Jt7OoNOSY4";
    }
    
	@Override
	public boolean areNewUsers() {
		if (newUsers == null)
			System.out.println("It's not ok");
		return !newUsers.isEmpty();
	}
	
	@Override
	public List<UserInfo> getNewUsers() {
		List<UserInfo> novices = new ArrayList<UserInfo>(newUsers);
		newUsers.clear();
		return novices;
	}
}