package algs;

import java.io.IOException;
import java.util.List;

import bot_interfaces.Algorithm;
import bot_interfaces.DataCorrector;
import bot_interfaces.DataReader;
import bot_interfaces.DataSearcher;
import exceptions.UnCorrectDataException;
import exceptions.UnfoundedDataException;
import structures.UserInfo;

public class ListWriteAlg implements Algorithm {
	private DataCorrector listWriter;
	private DataSearcher source;
	private UserInfo user;
	private boolean willCome;
	private boolean isReady;
	private boolean haveUnsolvedExceptions;
	private boolean stoppedWork;
	private String answer;

	public ListWriteAlg(DataCorrector usersDataWriter, DataSearcher extSource, UserInfo user)
	{
		listWriter = usersDataWriter;
		source = extSource;
		this.user = user;
		haveUnsolvedExceptions = false;
		stoppedWork = false;
		isReady = false;
		answer = "";
		
		try {
			willCome = source.getAllData().containsKey(Long.toString(user.getId()));
		} catch (IOException e) {
			haveUnsolvedExceptions = true;
			answer = "Sorry, file working is incorrect...";
		} catch (UnCorrectDataException e) {
			haveUnsolvedExceptions = true;
			answer = "Sorry, the data is incorrect...";
		}
	}
	
	public void readMessage(String message) {
		//System.out.println("алгоритм прочитал: " + message);
		
		if (stoppedWork)
			return;
		
		isReady = true;
		
		if (haveUnsolvedExceptions)
			return;
		
		try {
			switch (message.toLowerCase())
			{
				case "will":
					System.out.println("User id: " + user.getId());
					if (!willCome)
					{
						listWriter.writeData(Long.toString(user.getId()), user.getName());
						willCome = true;
						answer = "Ok, I've recorded you.";
					}
					else
						answer = "Yes, I've just understand.";
					break;
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
				case "show":
					answer = "";
				try {
					for (String extended : source.getAllData().keySet()) {
						answer += source.getData(extended).get(0) + "\n";
					}
				} catch (UnCorrectDataException e) {
					answer = "Sorry, data is broken.";
					haveUnsolvedExceptions = true;
				}
				if (answer.equals(""))
					answer = "The list is empty.";
					break;
				case "help":
					answer = "Commands list:\n" +
								"help - show this help-list\n" +
								"will - record you to list\n" +
								"wont - struck off you from list\n" +
								"show - show the meeting-list\n";
					break;
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
			answer = "Sorry, file working is incorrect...";
		}
		
	}

	public boolean isReadyToGenerate() {
		if (haveUnsolvedExceptions && !stoppedWork) {
			stoppedWork = true;
			return true;
		}
		return isReady && !stoppedWork;
	}

	public String generateMessage() {
		isReady = false;
		return answer;
	}

	public Algorithm genererateSame(UserInfo another) {
		return new ListWriteAlg(listWriter, source, another);
	}
	
	
}
