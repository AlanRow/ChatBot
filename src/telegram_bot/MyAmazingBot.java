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

//бот, который работает со всеми пользовател€ми в телеграмме
public class MyAmazingBot extends TelegramLongPollingBot implements UserControl{
	
	//показывает создан ли бот. Ќужен дл€ исключени€ работы сразу двух ботов (будет ошибка)
	private static boolean started = false;
	//все пользователи, известные боту
	private List<UserInfo> users;
	//новые необработанные пользователи (возможно лучше сделать очередь)
	private List<UserInfo> newUsers;
	//набор пришедших сообщений дл€ каждого пользовател€
	private Map<UserInfo, List<String>> newMessages;
	
	private MyAmazingBot() {
		users = new ArrayList<UserInfo>();
		newUsers = new ArrayList<UserInfo>();
		newMessages = new HashMap<UserInfo, List<String>>();
	}
	
	//запуск работы бота. ѕозвол€ет не допустить создани€ двух ботов.
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
	
	//отправка сообщени€. ѕо факту используетс€ только внутри толкера, который его отправл€ет
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
	
	//проверка наличи€ новых сообщений. »сп. внутри толкера.
	public boolean checkPostIsEmpty(TelegramTalker talker){
		if (!newMessages.containsKey(talker.getUser()))
			return true;
		return newMessages.get(talker.getUser()).isEmpty();
	}
	
	//выемка почты. ¬нутри толкера.
	public List<String> getPost(TelegramTalker talker) throws IllegalReadingException{
		if (!users.contains(talker.getUser()))
			throw new IllegalReadingException();
		List<String> post = new ArrayList<String> (newMessages.get(talker.getUser())); 
		newMessages.get(talker.getUser()).clear();
		
		return post;
	}
	
	//метод, запускающийс€ при приходе нового сообщени€
	@Override
	public void onUpdateReceived(Update update) {

	    // We check if the update has a message and the message has text
	    if (update.hasMessage() && update.getMessage().hasText()) {
	    	UserInfo user = new UserInfo(update.getMessage().getFrom());
	    	
	    	if (!newMessages.containsKey(user)) {
	    		newMessages.put(user, new ArrayList<String>());
	    	}

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
    
    //есть кто новый?
	@Override
	public boolean areNewUsers() {
		return !newUsers.isEmpty();
	}
	
	//новые пользователи
	@Override
	public List<UserInfo> getNewUsers() {
		List<UserInfo> novices = new ArrayList<UserInfo>(newUsers);
		
		newUsers.clear();
		return novices;
	}
}