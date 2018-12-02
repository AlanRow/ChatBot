package algs;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import bot.interfaces.Algorithm;
import bot.interfaces.DataCorrector;
import bot.interfaces.DataManager;
import bot.interfaces.DataReader;
import bot.interfaces.DataSearcher;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import structures.Meeting;
import structures.UserInfo;

//алгоритм работы со списком встречи
public class ListWriteAlg implements Algorithm {
	
	private Algorithm main;//текущий работающий алгоритм user или его наследник
	
	private Map<Integer, Meeting> meetingsMap;
	private UserInfo user;
	private boolean isReady;
	private String info;
	private boolean haveUnsolvedExceptions;
	private String answer;
	String errorMessage;

	public ListWriteAlg(Map<Integer, Meeting> meetings, UserInfo user) {
		meetingsMap = meetings;
		this.user = user;
		haveUnsolvedExceptions = false;
		isReady = false;
		info = "";
		answer = "";
		errorMessage = "";
		

		for (Meeting meet : meetings.values()) {
			main = meet.getMember(user);
			if (main != null)
				meet.subscribe(this);
				break;
		}
		if (main == null)
			main = new UserAlg(this, meetings, user, 
					"Приветствую, это бот для ведения списка встречи. Чтобы узнать больше введите \"\"");
	}
	
	public ListWriteAlg(Map<Integer, Meeting> meetings, UserInfo user, String inform) {
		this(meetings, user);
		info = inform;
	}
	
	public UserInfo getUser() {
		return user;
	}
	
	public Map<Integer, Meeting> getMeetingsMap(){
		return meetingsMap;
	}
	
	public void switchMainAlg(Algorithm alg) {
		main = alg;
	}
	
	//обработка пришедшего сообщения
	public void readMessage(String message) {
		
		isReady = true;
		
		if (haveUnsolvedExceptions)
			return;
		
		//try {
		main.readMessage(message);//основной алгоритм читает сообщение

		if ("".equals(message)) {
			isReady = false;
			return;
		}
			
		switch (message.toLowerCase().split(" ")[0]) {
			//выводит информцию о встрече
			case "info":
				if (!info.equals(""))
					answer = info;
				else
					answer = "Sorry, there isn't information about this meeting.";
				break;
				//выводит информацию о боте
			case "/start":
				answer = "This bot makes list of your meeting. If you want to know more, please write \"help\"";
				break;
				//показ списка команд
			case "help":
				answer = "Commands list:\n" +
	   					 "help - show this help-list\n" +
					     "info - show the information about meeting (name, time, place, etc.)\n";
				break;
			default:
				answer = "Sorry, I haven't understood you...";
		}
	}

	public boolean isReadyToGenerate() {
		return isReady || main.isReadyToGenerate();//если алгоритм по умолчанию или текущий готов ответить, то отвечать
	}

	public String generateMessage() {
		isReady = false;
		
		//отправляет сообщение от основного алгоритма
		//реакция основного алгоритма считается приоритетной
		if (main.isReadyToGenerate())
			return main.generateMessage();
		
		if (haveUnsolvedExceptions)
			return errorMessage;
		
		return answer;
	}

	public Algorithm genererateSame(UserInfo another) {
		return new ListWriteAlg(meetingsMap, another);
	}
	
	
}
