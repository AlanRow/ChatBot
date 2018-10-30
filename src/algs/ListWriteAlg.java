package algs;

import java.io.IOException;
import java.util.List;

import bot_interfaces.Algorithm;
import bot_interfaces.DataCorrector;
import exceptions.UnfoundedDataException;
import structures.UserInfo;

public class ListWriteAlg implements Algorithm {
	private DataCorrector listWriter;
	private UserInfo user;
	private boolean willCome;
	private boolean isReady;
	private boolean isTautology;
	private boolean haveUnsolvedExceptions;
	private String answer;
	
	private List userList;

	public ListWriteAlg(DataCorrector usersDataWriter, UserInfo user)
	{
		listWriter = usersDataWriter;
		this.user = user;
	}
	
	public void readMessage(String message) {
		System.out.println("алгоритм прочитал: " + message);
		
		if (haveUnsolvedExceptions)
			return;
		
		isReady = true;
		try {
			switch (message.toLowerCase())
			{
				case "will":
					if (!willCome)
					{
						listWriter.writeData(Long.toString(user.getId()), user.getName());
						willCome = true;
						answer = "Хорошо, я вас записал.";
					}
					else
						answer = "Да, я так и понял.";
					break;
				case "wont":
					if (willCome)
					{
						willCome = false;
						try {
							listWriter.removeData(Long.toString(user.getId()), user.getName());
							answer = "Хорошо, я вас вычеркнул.";
						}
						catch (UnfoundedDataException ex) {
							answer = "Странно, вас не было в списке...";
						}
					}
					else
						answer = "Да, я так и понял.";
					break;
				default:
					isReady = false;
					break;
			}
		} 
		catch (IOException ex) {
			haveUnsolvedExceptions = true;
			answer = "Простие, допущена ошибка в работе с фалами.";
		}
		
	}

	public boolean isReadyToGenerate() {
		return isReady;
	}

	public String generateMessage() {
		isReady = false;
		return answer;
	}
	
	
}
