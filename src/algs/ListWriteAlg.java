package algs;

import java.io.IOException;
import java.util.List;

import bot_interfaces.Algorithm;
import bot_interfaces.DataCorrector;
import bot_interfaces.DataReader;
import bot_interfaces.DataSearcher;
import exceptions.UncorrectDataException;
import exceptions.UnfoundedDataException;
import structures.UserInfo;

//алгоритм работы со списком встречи
public class ListWriteAlg implements Algorithm {
	private DataCorrector listWriter;
	private DataSearcher source;
	private UserInfo user;
	private boolean willCome;
	private boolean isReady;
	private String info;
	private boolean haveUnsolvedExceptions;
	private boolean stoppedWork;
	private String answer;
	String errorMessage;

	public ListWriteAlg(DataCorrector usersDataWriter, DataSearcher extSource, UserInfo user) {
		listWriter = usersDataWriter;
		source = extSource;
		this.user = user;
		haveUnsolvedExceptions = false;
		stoppedWork = false;
		isReady = false;
		info = "";
		answer = "";
		errorMessage = "";
		
		try {
			willCome = source.getAllData().containsKey(Long.toString(user.getId()));
		} catch (IOException e) {
			haveUnsolvedExceptions = true;
			answer = "Sorry, file working is incorrect...";
		} catch (UncorrectDataException e) {
			haveUnsolvedExceptions = true;
			answer = "Sorry, the data is incorrect...";
		}
	}
	
	public ListWriteAlg(DataCorrector usersDataWriter, DataSearcher extSource, UserInfo user, String inform) {
		this(usersDataWriter, extSource, user);
		info = inform;
	}
	
	//обработка пришедшего сообщения
	public void readMessage(String message) {
		
		isReady = true;
		
		if (haveUnsolvedExceptions)
			return;
		
		try {
			//обработка комманд
			switch (message.toLowerCase())
			{
				//запись пользователя в список
				case "will":
					if (!willCome)
					{
						listWriter.writeData(Long.toString(user.getId()), user.getName());
						willCome = true;
						answer = "Ok, I've recorded you.";
					}
					else
						answer = "Yes, I've just understand.";
					break;
				//вычеркивание из списка
				case "wont":
					if (willCome)
					{
						willCome = false;
						listWriter.removeData(Long.toString(user.getId()));
						answer = "Ok, I've struck off you";
					}
					else
						answer = "You haven't recorded, yet.";
					break;
				//показ списка встречи
				case "show":
					answer = "";
				try {
					for (String extended : source.getAllData().keySet()) {
						answer += source.getData(extended).get(0) + "\n";
					}
				} catch (UncorrectDataException e) {
					errorMessage = "Sorry, data is broken.";
					haveUnsolvedExceptions = true;
				}
				if (answer.equals(""))
					answer = "The list is empty.";
					break;
				//показ списка команд
				case "help":
					answer = "Commands list:\n" +
								"help - show this help-list\n" +
								"info - show the information about meeting (name, time, place, etc.)\n" +
								"will - record you to list\n" +
								"wont - struck off you from list\n" +
								"show - show the meeting-list\n";
					break;
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
				default:
					isReady = false;
					break;
			}
		} 
		catch (IOException ex) {
			haveUnsolvedExceptions = true;
			errorMessage = "Sorry, file working is incorrect...";
		}
		
		
		
	}

	public boolean isReadyToGenerate() {
		return isReady;
	}

	public String generateMessage() {
		isReady = false;
		
		if (haveUnsolvedExceptions)
			return errorMessage;
		return answer;
	}

	public Algorithm genererateSame(UserInfo another) {
		return new ListWriteAlg(listWriter, source, another);
	}
	
	
}
